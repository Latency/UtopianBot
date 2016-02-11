package org.rsbot.script.methods;

import java.awt.Point;

import org.rsbot.constants.IBank;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

/**
 * Bank related operations.
 */
public class Bank extends MethodProvider {
  private final OutputManager   om                                               = methods.locale;

  public Bank(final MethodContext ctx) {
    super(ctx);
  }
  
  public final Filter<RSObject> OBJECT_BANKS                                     = new Filter<RSObject>() {
                                                                                   private final String[] bankNames = { om.Server.toString("bankBooth"), om.Server.toString("bankShanty"), om.Server.toString("bankChest"), om.Server.toString("bankCounter")         };

                                                                                   @Override
                                                                                   public boolean accept(final RSObject rsObject) {
                                                                                     final String name = rsObject != null ? rsObject.getName() : null;
                                                                                     for (String bankName : bankNames) {
                                                                                       if (name.equalsIgnoreCase(bankName)) {
                                                                                         return true;
                                                                                       }
                                                                                     }
                                                                                     return false;
                                                                                   }
                                                                                 };

  public final Filter<RSObject> OBJECT_DEPOSIT_BOX                               = new Filter<RSObject>() {
                                                                                   private final String[] depositBoxNames = { om.Server.toString("bankDepositBox") };

                                                                                   @Override
                                                                                   public boolean accept(final RSObject rsObject) {
                                                                                     final String name = rsObject != null ? rsObject.getName() : null;
                                                                                     for (String bankName : depositBoxNames) {
                                                                                       if (name.equalsIgnoreCase(bankName)) {
                                                                                         return true;
                                                                                       }
                                                                                     }
                                                                                     return false;
                                                                                   }
                                                                                 };

  public final Filter<RSNPC>    NPC_BANKERS                                      = new Filter<RSNPC>() {
                                                                                   private final String[] bankerNames = { om.Server.toString("bankBanker") };

                                                                                   @Override
                                                                                   public boolean accept(final RSNPC rsNPC) {
                                                                                     final String name = rsNPC != null ? rsNPC.getName() : null;
                                                                                     for (String bankName : bankerNames) {
                                                                                       if (name.equalsIgnoreCase(bankName)) {
                                                                                         return true;
                                                                                       }
                                                                                     }
                                                                                     return false;
                                                                                   }
                                                                                 };

  /**
   * Closes the bank interface. Supports deposit boxes.
   * 
   * @return <tt>true</tt> if the bank interface is no longer open.
   */
  public boolean close() {
    if (isOpen()) {
      methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_CLOSE.getID()).doClick();
      sleep(random(500, 600));
      return !isOpen();
    }
    if (isDepositOpen()) {
      methods.interfaces.getComponent(IBank.DEPOSIT_BOX.getID(), IBank.DEPOSIT_BOX_BUTTON_CLOSE.getID()).doClick();
      sleep(random(500, 600));
      return !isDepositOpen();
    }
    return false;
  }

  /**
   * If bank is open, deposits specified amount of an item into the bank. Supports deposit boxes.
   * 
   * @param itemID The ID of the item.
   * @param number The amount to deposit. 0 deposits All. 1,5,10 deposit corresponding amount while other numbers deposit X.
   * @return <tt>true</tt> if successful; otherwise <tt>false</tt>.
   */
  public boolean deposit(final int itemID, final int number) {
    if (isOpen() || isDepositOpen()) {
      if (number < 0) {
        throw new IllegalArgumentException(om.OS.toString("number < 0 (" + number + ")"));
      }
      RSComponent item = null;
      int itemCount = 0;
      final int invCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
      if (!isOpen()) {
        boolean match = false;
        for (int i = 0; i < 28; i++) {
          final RSComponent comp = methods.interfaces.get(11).getComponent(17).getComponent(i);
          if (comp.getComponentID() == itemID) {
            itemCount += comp.getComponentStackSize();
            if (!match) {
              item = comp;
              match = true;
            }
          }
          if (itemCount > 1) {
            break;
          }
        }
      } else {
        item = methods.inventory.getItem(itemID).getComponent();
        itemCount = methods.inventory.getCount(true, itemID);
      }
      if (item == null) {
        return true;
      }
      switch (number) {
        case 0:
          item.interact(itemCount > 1 ? om.Server.toString("bankDepositAll") : om.Server.toString("bankDeposit"));
          break;
        case 1:
          item.interact(om.Server.toString("bankDeposit"));
          break;
        case 5:
          item.interact(om.Server.toString("bankDeposit-") + number);
          break;
        default:
          if (!item.interact(om.Server.toString("bankDeposit-") + number)) {
            if (item.interact(om.Server.toString("bankDepositX"))) {
              sleep(random(1000, 1300));
              methods.inputManager.sendKeys(String.valueOf(number), true);
            }
          }
          break;
      }
      sleep(300);
      final int cInvCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
      return cInvCount < invCount || cInvCount == 0;
    }
    return false;
  }

  /**
   * Deposits all items in methods.inventory. Supports deposit boxes.
   * 
   * @return <tt>true</tt> on success.
   */
  public boolean depositAll() {
    if (isOpen())
      return methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_DEPOSIT_CARRIED_ITEMS.getID()).doClick();
    return isDepositOpen() && methods.interfaces.getComponent(IBank.DEPOSIT_BOX.getID(), IBank.DEPOSIT_BUTTON_DEPOSIT_CARRIED_ITEMS.getID()).doClick();
  }

  /**
   * Deposits all items in inventory except for the given IDs. Supports deposit boxes.
   * 
   * @param items The items not to deposit.
   * @return true on success.
   */
  public boolean depositAllExcept(final int... items) {
    if (isOpen() || isDepositOpen()) {
      boolean deposit = true;
      int invCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
      outer:
      for (int i = 0; i < 28; i++) {
        final RSComponent item = isOpen() ? methods.inventory.getItemAt(i).getComponent() : methods.interfaces.get(11).getComponent(17).getComponent(i);
        if (item != null && item.getComponentID() != -1) {
          for (final int id : items) {
            if (item.getComponentID() == id) {
              continue outer;
            }
          }
          for (int tries = 0; tries < 5; tries++) {
            deposit(item.getComponentID(), 0);
            sleep(random(600, 900));
            final int cInvCount = isOpen() ? methods.inventory.getCount(true) : getBoxCount();
            if (cInvCount < invCount) {
              invCount = cInvCount;
              continue outer;
            }
          }
          deposit = false;
        }
      }
      return deposit;
    }
    return false;
  }

  /**
   * Deposit everything your player has equipped. Supports deposit boxes.
   * 
   * @return <tt>true</tt> on success.
   * @since 6 March 2009.
   */
  public boolean depositAllEquipped() {
    if (isOpen())
      return methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_DEPOSIT_WORN_ITEMS.getID()).doClick();
    return isDepositOpen() && methods.interfaces.getComponent(IBank.DEPOSIT_BOX.getID(), IBank.DEPOSIT_BUTTON_DEPOSIT_WORN_ITEMS.getID()).doClick();
  }

  /**
   * Deposits everything your familiar is carrying. Supports deposit boxes.
   * 
   * @return <tt>true</tt> on success
   * @since 6 March 2009.
   */
  public boolean depositAllFamiliar() {
    if (isOpen()) {
      return methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_DEPOSIT_BEAST_INVENTORY.getID()).doClick();
    }
    return isDepositOpen() && methods.interfaces.getComponent(IBank.DEPOSIT_BOX.getID(), IBank.DEPOSIT_BUTTON_DEPOSIT_BEAST_INVENTORY.getID()).doClick();
  }

  /**
   * Returns the sum of the count of the given items in the bank.
   * 
   * @param items The array of items.
   * @return The sum of the stacks of the items.
   */
  public int getCount(final int... items) {
    int itemCount = 0;
    final RSItem[] inventoryArray = getItems();
    for (final RSItem item : inventoryArray) {
      for (final int id : items) {
        if (item.getID() == id) {
          itemCount += item.getStackSize();
        }
      }
    }
    return itemCount;
  }

  /**
   * Get current tab open in the bank.
   * 
   * @return int of tab (0-8), or -1 if none are selected (bank is not open).
   */
  public int getCurrentTab() {
    for (int i = 0; i < IBank.TAB.getIDs().length; i++) {
      if (methods.interfaces.get(IBank.BANK.getID()).getComponent(IBank.TAB.getIDs()[i] - 1).getBackgroundColor() == 1419)
        return i;
    }
    return -1;
  }

  /**
   * Gets the bank interface.
   * 
   * @return The bank <code>RSInterface</code>.
   */
  public RSInterface getInterface() {
    return methods.interfaces.get(IBank.BANK.getID());
  }

  /**
   * Gets the deposit box interface.
   * 
   * @return The deposit box <code>RSInterface</code>.
   */
  public RSInterface getBoxInterface() {
    return methods.interfaces.get(IBank.BANK.getID());
  }

  /**
   * Gets the <code>RSComponent</code> of the given item at the specified index.
   * 
   * @param index The index of the item.
   * @return <code>RSComponent</code> if item is found at index; otherwise null.
   */
  public RSItem getItemAt(final int index) {
    final RSItem[] items = getItems();
    if (items != null) {
      for (final RSItem item : items) {
        if (item.getComponent().getComponentIndex() == index) {
          return item;
        }
      }
    }

    return null;
  }

  /**
   * Gets the first item with the provided ID in the bank.
   * 
   * @param id ID of the item to get.
   * @return The component of the item; otherwise null.
   */
  public RSItem getItem(final int id) {
    final RSItem[] items = getItems();
    if (items != null) {
      for (final RSItem item : items) {
        if (item.getID() == id) {
          return item;
        }
      }
    }
    return null;
  }

  /**
   * Gets the point on the screen for a given item. Numbered left to right then top to bottom.
   * 
   * @param slot The index of the item.
   * @return The point of the item or new Point(-1, -1) if null.
   */
  public Point getItemPoint(final int slot) {
    if (slot < 0) {
      throw new IllegalArgumentException(om.Server.toString("slot < 0 " + slot));
    }
    final RSItem item = getItemAt(slot);
    if (item != null) {
      return item.getComponent().getLocation();
    }
    return new Point(-1, -1);
  }

  /**
   * Gets all the items in the bank's inventory.
   * 
   * @return an <code>RSItem</code> array of the bank's inventory interface.
   */
  public RSItem[] getItems() {
    if (getInterface() == null || getInterface().getComponent(IBank.INVENTORY.getID()) == null)
      return new RSItem[0];
    final RSComponent[] components = getInterface().getComponent(IBank.INVENTORY.getID()).getComponents();
    final RSItem[] items = new RSItem[components.length];
    for (int i = 0; i < items.length; ++i)
      items[i] = new RSItem(methods, components[i]);
    return items;
  }

  /**
   * Checks whether or not the bank is open.
   * 
   * @return <tt>true</tt> if the bank interface is open; otherwise <tt>false</tt>.
   */
  public boolean isOpen() {
    return getInterface().isValid();
  }

  /**
   * Checks whether or not the deposit box is open.
   * 
   * @return <tt>true</tt> if the deposit box interface is open; otherwise <tt>false</tt>.
   */
  public boolean isDepositOpen() {
    return methods.interfaces.get(IBank.DEPOSIT_BOX.getID()).isValid();
  }

  /**
   * Opens one of the supported banker NPCs, booths, or chests nearby. If they are not nearby, and they are not null, it will automatically walk to the closest one.
   * 
   * @return <tt>true</tt> if the bank was opened; otherwise <tt>false</tt>.
   */
  public boolean open() {
    if (isOpen()) {
      return true;
    }
    try {
      if (methods.menu.isOpen()) {
        methods.mouse.moveSlightly();
        sleep(random(20, 30));
      }
      RSObject bankBooth = methods.objects.getNearest(OBJECT_BANKS);
      RSNPC banker = methods.npcs.getNearest(NPC_BANKERS);
      /* Find closest one, others are set to null. Remember distance and tile. */
      int lowestDist = Integer.MAX_VALUE;
      RSTile tile = null;
      if (bankBooth != null) {
        tile = bankBooth.getLocation();
        lowestDist = methods.calc.distanceTo(tile);
      }
      if (banker != null && methods.calc.distanceTo(banker) < lowestDist) {
        tile = banker.getLocation();
        lowestDist = methods.calc.distanceTo(tile);
        bankBooth = null;
      }
      /* Open closest one, if any found */
      if (lowestDist < 5 && methods.calc.tileOnMap(tile) && methods.calc.canReach(tile, true)) {
        boolean didAction = false;
        if (bankBooth != null) {
          didAction = bankBooth.interact(om.Server.toString("bankUseQuickly")) || bankBooth.interact(om.Server.toString("bankOpen"), om.Server.toString("bankShantay"))
              || bankBooth.interact(om.Server.toString("bankUse"), om.Server.toString("bankChest"));
        } else if (banker != null) {
          didAction = banker.interact(om.Server.toString("bankBank"), om.Server.toString("bankBanker"))
              || banker.interact(om.Server.toString("bankBank"), om.Server.toString("bankFremennik"))
              || banker.interact(om.Server.toString("bankBank"), om.Server.toString("bankEmerald"));
        }
        if (didAction) {
          int count = 0;
          while (!isOpen() && ++count < 10) {
            sleep(random(200, 400));
            if (methods.players.getMyPlayer().isMoving()) {
              count = 0;
            }
          }
        } else {
          methods.camera.turnTo(tile);
        }
      } else if (tile != null) {
        methods.walking.walkTileMM(tile);
      }
      return isOpen();
    } catch (final Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Opens one of the supported deposit boxes nearby. If they are not nearby, and they are not null, it will automatically walk to the closest one.
   * 
   * @return <tt>true</tt> if the deposit box was opened; otherwise <tt>false</tt>.
   */
  public boolean openDepositBox() {
    try {
      if (!isDepositOpen()) {
        if (methods.menu.isOpen()) {
          methods.mouse.moveSlightly();
          sleep(random(20, 30));
        }
        final RSObject depositBox = methods.objects.getNearest(OBJECT_DEPOSIT_BOX);
        if (depositBox != null && methods.calc.distanceTo(depositBox) < 8 && methods.calc.tileOnMap(depositBox.getLocation())
            && methods.calc.canReach(depositBox.getLocation(), true)) {
          if (depositBox.interact(om.Server.toString("bankDeposit"))) {
            int count = 0;
            while (!isDepositOpen() && ++count < 10) {
              sleep(random(200, 400));
              if (methods.players.getMyPlayer().isMoving()) {
                count = 0;
              }
            }
          } else {
            methods.camera.turnTo(depositBox, 20);
          }
        } else {
          if (depositBox != null) {
            methods.walking.walkTo(depositBox.getLocation());
          }
        }
      }
      return isDepositOpen();
    } catch (final Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Opens the bank tab.
   * 
   * @param tabNumber The tab number - e.g. view all is 1.
   * @return <tt>true</tt> on success.
   */
  public boolean openTab(final int tabNumber) {
    return isOpen() && methods.interfaces.getComponent(IBank.BANK.getID(), IBank.TAB.getIDs()[tabNumber - 1]).doClick();
  }

  /**
   * @return <tt>true</tt> if currently searching the bank.
   */
  public boolean isSearchOpen() {
    // Setting 1248 is -2147483648 when search is enabled and -2013265920
    return methods.settings.getSetting(1248) == -2147483648;
  }

  /**
   * Searches for an item in the bank. Returns true if succeeded (does not necessarily mean it was found).
   * 
   * @param itemName The item name to find.
   * @return <tt>true</tt> on success.
   */
  public boolean searchItem(final String itemName) {
    if (!isOpen()) {
      return false;
    }
    methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_SEARCH.getID()).interact(om.Server.toString("Search"));
    sleep(random(1000, 1500));
    if (!isSearchOpen())
      sleep(500);
    if (isOpen() && isSearchOpen()) {
      methods.inputManager.sendKeys(itemName, false);
      sleep(random(300, 700));
      return true;
    }
    return false;
  }

  /**
   * Sets the bank rearrange mode to insert.
   * 
   * @return <tt>true</tt> on success.
   */
  public boolean setRearrangeModeToInsert() {
    if (!isOpen()) {
      return false;
    }
    if (methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_REARRANGE_MODE) != 1) {
      methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_INSERT.getID()).doClick();
      sleep(random(500, 700));
    }
    return methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_REARRANGE_MODE) == 1;
  }

  /**
   * Sets the bank rearrange mode to swap.
   * 
   * @return <tt>true</tt> on success.
   */
  public boolean setRearrangeModeToSwap() {
    if (!isOpen()) {
      return false;
    }
    if (methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_REARRANGE_MODE) != 0) {
      methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_SWAP.getID()).doClick();
      sleep(random(500, 700));
    }
    return methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_REARRANGE_MODE) == 0;
  }

  /**
   * Sets the bank withdraw mode to item.
   * 
   * @return <tt>true</tt> on success.
   */
  public boolean setWithdrawModeToItem() {
    if (!isOpen()) {
      return false;
    }
    if (methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) != 0) {
      methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_ITEM.getID()).doClick();
      sleep(random(500, 700));
    }
    return methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) == 0;
  }

  /**
   * Sets the bank withdraw mode to note.
   * 
   * @return <tt>true</tt> on success.
   */
  public boolean setWithdrawModeToNote() {
    if (!isOpen()) {
      return false;
    }
    if (methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) != 1) {
      methods.interfaces.getComponent(IBank.BANK.getID(), IBank.BUTTON_NOTE.getID()).doClick();
      sleep(random(500, 700));
    }
    return methods.settings.getSetting(Settings.SETTING_BANK_TOGGLE_WITHDRAW_MODE) == 1;
  }

  /**
   * Tries to withdraw an item. 0 is All. -1 is All but one, 1, 5, 10 use Withdraw 1, 5, 10 while other numbers Withdraw X.
   * 
   * @param itemID The ID of the item.
   * @param count The number to withdraw.
   * @return <tt>true</tt> on success.
   */
  public boolean withdraw(final int itemID, final int count) {
    if (!isOpen()) {
      return false;
    }
    if (count < -1) {
      throw new IllegalArgumentException(om.OS.toString("count (" + count + ") < -1"));
    }
    final RSItem rsi = getItem(itemID);
    if (rsi == null || rsi.getID() == -1) {
      return false;
    }
    final RSComponent item = rsi.getComponent();
    if (item == null) {
      return false;
    }
    int t = 0;
    while (item.getRelativeX() == 0 && methods.bank.getCurrentTab() != 0 && t < 5) {
      if (methods.interfaces.getComponent(IBank.BANK.getID(), IBank.TAB.getIDs()[0]).doClick())
        sleep(random(800, 1300));
      t++;
    }
    if (!methods.interfaces.scrollTo(item, (IBank.BANK.getID() << 16) + IBank.SCROLLBAR.getID())) {
      return false;
    }
    final int invCount = methods.inventory.getCount(true);
    item.doClick(count == 1 ? true : false);
    final String defaultAction = om.Server.toString("bankWithdraw-") + count;
    String action = null;
    switch (count) {
      case 0:
        action = om.Server.toString("bankWithdrawAll");
        break;
      case -1:
        action = om.Server.toString("bankWithdrawAllButOne");
        break;
      case 1:
        break;
      case 5:
        action = defaultAction;
        break;
      case 10:
        action = defaultAction;
        break;
      default:
        int i = -1;
        try {
          i = Integer.parseInt(item.getActions()[3].toLowerCase().trim().replaceAll("\\D", ""));
        } catch (final Exception e) {
          e.printStackTrace();
        }
        if (i == count) {
          action = defaultAction;
        } else if (item.interact(om.Server.toString("bankWithdrawX"))) {
          sleep(random(1000, 1300));
          methods.keyboard.sendText(String.valueOf(count), true);
        }
    }
    if (action != null && item.interact(action)) {
      sleep(random(1000, 1300));
    }
    final int newInvCount = methods.inventory.getCount(true);
    return newInvCount > invCount;
  }

  /**
   * Gets the count of all the items in the inventory with the any of the specified IDs while deposit box is open.
   * 
   * @param ids the item IDs to include
   * @return The count.
   */
  public int getBoxCount(final int... ids) {
    if (!isDepositOpen()) {
      return -1;
    }
    int count = 0;
    for (int i = 0; i < 28; ++i) {
      for (final int id : ids) {
        if (methods.interfaces.get(11).getComponent(17).isValid() && methods.interfaces.get(11).getComponent(17).getComponent(i).getComponentID() == id) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Gets the count of all items in your inventory ignoring stack sizes while deposit box is open.
   * 
   * @return The count.
   */
  public int getBoxCount() {
    if (!isDepositOpen()) {
      return -1;
    }
    int count = 0;
    for (int i = 0; i < 28; i++) {
      if (methods.interfaces.get(11).getComponent(17).isValid() && methods.interfaces.get(11).getComponent(17).getComponent(i).getComponentID() != -1) {
        count++;
      }
    }
    return count;
  }

  /**
   * Gets the equipment items from the bank interface.
   * 
   * @return All equipment items that are being worn.
   * @author LastCoder
   */
  public RSItem[] getEquipmentItems() {
    if (methods.interfaces.get(IBank.EQUIPMENT.getID()).getComponent(IBank.EQUIPMENT_COMPONENT.getID()).isValid()) {
      return new RSItem[0];
    }
    final RSComponent[] components = methods.interfaces.get(IBank.EQUIPMENT.getID()).getComponent(IBank.EQUIPMENT_COMPONENT.getID()).getComponents();
    final RSItem[] items = new RSItem[components.length];
    for (int i = 0; i < items.length; i++)
      items[i] = new RSItem(methods, components[i]);
    return items;
  }

  /**
   * Gets a equipment item from the bank interface.
   * 
   * @param id ID of the item.
   * @return RSItem
   */
  public RSItem getEquipmentItem(final int id) {
    final RSItem[] items = getEquipmentItems();
    if (items != null) {
      for (final RSItem item : items) {
        if (item.getID() == id) {
          return item;
        }
      }
    }
    return null;
  }

  /**
   * Gets the ID of a equipment item based on name.
   * 
   * @param name Name of the item.
   * @return -1 if item is not found.
   */
  public int getEquipmentItemID(final String name) {
    final RSItem[] items = getEquipmentItems();
    if (items != null) {
      for (final RSItem item : items) {
        if (item.getName().contains(name)) {
          return item.getID();
        }
      }
    }
    return -1;
  }

  /**
   * Opens the equipment interface.
   * 
   * @return <tt>true</tt> if opened.
   */
  public boolean openEquipment() {
    return getInterface().getComponent(IBank.BUTTON_OPEN_EQUIP.getID()).isValid() && getInterface().getComponent(IBank.BUTTON_OPEN_EQUIP.getID()).doClick();
  }

  /**
   * Gets the item ID of a item side the bank.
   * 
   * @param name Name of the item.
   * @return -1 if item is not found.
   */
  public int getItemID(final String name) {
    final RSItem[] items = getItems();
    if (items != null) {
      for (final RSItem item : items) {
        if (item.getName().toLowerCase().equals(name.toLowerCase())) {
          return item.getID();
        }
      }
      for (final RSItem item : items) {
        if (item.getName().toLowerCase().contains(name.toLowerCase())) {
          return item.getID();
        }
      }
    }
    return -1;
  }
}