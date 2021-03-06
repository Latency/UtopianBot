package org.rsbot.script.methods;

import java.util.ArrayList;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSItem;

/**
 * Store related operations.
 */
public class Store extends MethodProvider {
  public static final int INTERFACE_STORE              = 620;
  public static final int INTERFACE_STORE_BUTTON_CLOSE = 18;
  public static final int INTERFACE_STORE_ITEMS        = 25;

  private final OutputManager om = methods.locale;

  public Store(final MethodContext ctx) {
    super(ctx);
  }
  
  /**
   * Tries to buy an item. 0 is All. 1, 5 and 10 use buy 1/5/10 while the other numbers use buy x.
   * 
   * @param itemID The id of the item.
   * @param count The number to buy.
   * @return <tt>true</tt> on success
   */
  public boolean buy(final int itemID, final int count) {
    if (count < 0) {
      return false;
    }
    if (!isOpen()) {
      return false;
    }
    final int inventoryCount = methods.inventory.getCount(true);
    final RSItem item = getItem(itemID);
    if (item != null) {
      if (count >= 500) {
        if (item.interact(om.Server.toString("storeBuy") + " 500")) {
          sleep(random(500, 700));
          return buy(itemID, (count - 500));
        }
        return false;
      } else if (count >= 50 && count < 500) {
        if (item.interact(om.Server.toString("storeBuy") + " 50")) {
          sleep(random(500, 700));
          return buy(itemID, (count - 50));
        }
        return false;
      } else if (count >= 10 && count < 50) {
        if (item.interact(om.Server.toString("storeBuy") + " 10")) {
          sleep(random(500, 700));
          return buy(itemID, (count - 10));
        }
        return false;
      } else if (count >= 5 && count < 10) {
        if (item.interact(om.Server.toString("storeBuy") + " 5")) {
          sleep(random(500, 700));
          return buy(itemID, (count - 5));
        }
        return false;
      } else if (count >= 1 && count < 5) {
        if (item.interact(om.Server.toString("storeBuy") + " 1")) {
          sleep(random(500, 700));
          return buy(itemID, (count - 1));
        }
        return false;
      } else {
        return methods.inventory.getCount(true) > inventoryCount;
      }
    }
    return false;
  }

  /**
   * Closes the store interface.
   * 
   * @return <tt>true</tt> if the interface is no longer open
   */
  public boolean close() {
    if (!isOpen()) {
      return true;
    }
    if (methods.interfaces.getComponent(INTERFACE_STORE, INTERFACE_STORE_BUTTON_CLOSE).doClick()) {
      sleep(random(500, 600));
      return !isOpen();
    }
    return false;
  }

  /**
   * Gets the store interface.
   * 
   * @return the store <tt>RSInterface</tt>
   */
  public RSInterface getInterface() {
    return methods.interfaces.get(INTERFACE_STORE);
  }

  /**
   * Gets the item at a given component index.
   * 
   * @param index The index of the component based off of the components in the Store interface.
   * @return <tt>RSComponent</tt> for the item at the given index; otherwise null.
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
   * Gets the first item found with the given id.
   * 
   * @param id ID of the item to get
   * @return The <tt>RSComponent</tt> of the item; otherwise null.
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
   * Gets all the items in the store inventory.
   * 
   * @return An <tt>RSComponent</tt> array representing all of the components in the stores <tt>RSInterface</tt>.
   */
  public RSItem[] getItems() {
    if (getInterface() == null || getInterface().getComponent(INTERFACE_STORE_ITEMS) == null) {
      return null;
    }

    final ArrayList<RSItem> items = new ArrayList<RSItem>();
    final RSComponent[] components = getInterface().getComponent(INTERFACE_STORE_ITEMS).getComponents();

    for (final RSComponent component : components) {
      if (component != null && component.getComponentID() != -1) {
        items.add(new RSItem(methods, component));
      }
    }

    return items.toArray(new RSItem[items.size()]);
  }

  /**
   * Returns whether or not the store interface is open.
   * 
   * @return <tt>true</tt> if the store interface is open, otherwise <tt>false</tt>.
   */
  public boolean isOpen() {
    return getInterface().isValid();
  }

}
