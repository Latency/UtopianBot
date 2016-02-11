package org.rsbot.script.randoms;

import java.awt.Rectangle;

import org.rsbot.gui.AccountManager;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSItem;

@ScriptManifest(authors = { "Fred", "Arbiter" }, name = "Improved Rewards Box", version = 1.3)
public class ImprovedRewardsBox extends Random {
  public ImprovedRewardsBox(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om                = methods.locale;

  private static final int    BOOK_KNOWLEDGE_ID = 11640;
  private static final int    LAMP_ID           = 2528;
  private static final int    MYSTERY_BOX_ID    = 6199;
  private static final int    BOX_ID            = 14664;
  private static final int    BOX_IF            = 202;
  private static final int    BOX_CONFIRM_IF    = 28;
  private static final int    BOX_SELECTION_IF  = 15;
  private static final int    BOX_SCROLLBAR_IF  = 24;
  private static final int    XP_IF             = 134;

  private static final int    ATT_ID            = 4;
  private static final int    AGILITY_ID        = 5;
  private static final int    HERBLORE_ID       = 6;
  private static final int    FISHING_ID        = 7;
  private static final int    THIEVING_ID       = 8;
  private static final int    RUNECRAFTING_ID   = 9;
  private static final int    SLAYER_ID         = 10;
  private static final int    FARMING_ID        = 11;
  private static final int    MINING_ID         = 12;
  private static final int    SMITHING_ID       = 13;
  private static final int    HUNTER_ID         = 14;
  private static final int    COOKING_ID        = 15;
  private static final int    FIREMAKING_ID     = 16;
  private static final int    WOODCUTTING_ID    = 17;
  private static final int    FLETCHING_ID      = 18;
  private static final int    CONSTRUCTION_ID   = 19;
  private static final int    SUMMONING_ID      = 20;
  private static final int    STRENGTH_ID       = 21;
  private static final int    RANGED_ID         = 22;
  private static final int    MAGIC_ID          = 23;
  private static final int    DEFENCE_ID        = 24;
  private static final int    HITPOINTS_ID      = 25;
  private static final int    CRAFTING_ID       = 26;
  private static final int    PRAYER_ID         = 27;
  private static final int    DUNGEONEERING_ID  = 28;
  private static final int    CONFIRM_ID        = 2;

  private int                 scrollbarTopLength;
  private int                 scrollbarTotalLength;
  private int                 hiddenScreenHeight;
  private int                 viewableScreenHeight;
  private int                 endofselection    = 0;
  private int                 XPSelection;

  public Random               Rand;

  @Override
  public boolean activateCondition() {
    return methods.game.isLoggedIn() && !methods.players.getMyPlayer().isInCombat() && !methods.bank.isOpen()
        && cachedInventoryContainedOneOf(BOX_ID, BOOK_KNOWLEDGE_ID, LAMP_ID, MYSTERY_BOX_ID);
  }

  private boolean cachedInventoryContainedOneOf(final int... ids) {
    for (final RSItem item : methods.inventory.getItems(true)) {
      for (final int id : ids) {
        if (item.getID() == id) {
          return true;
        }
      }
    }
    return false;
  }

  public int getActualY(final RSComponent Component) {
    int boxYPos;
    final RSComponent[] selection = methods.interfaces.get(202).getComponent(15)
        .getComponents();
    for (int end = 0; end < selection.length; end++) {
      if (selection[end].containsText(":")) {
        endofselection = end - 6;
      }
      if (selection[end].containsText(om.Server.toString("rewardEmote"))) {
        endofselection = end - 6;
      }
      if (selection[end].containsText(om.Server.toString("rewardCostume"))) {
        endofselection = end - 6;
      }
    }
    viewableScreenHeight = methods.interfaces.get(202).getComponent(15).getHeight() - 11;
    final int totalScreenHeight = selection[endofselection].getAbsoluteY()
        + selection[endofselection].getHeight() - selection[0]
            .getAbsoluteY();
    hiddenScreenHeight = totalScreenHeight - viewableScreenHeight;
    if (hiddenScreenHeight > 0) {
      final RSComponent[] scrollbar = methods.interfaces.get(202)
          .getComponent(24).getComponents();
      scrollbarTopLength = scrollbar[1].getAbsoluteY() - scrollbar[0]
          .getAbsoluteY();
      final int scrollbarBottomLength = scrollbar[5].getAbsoluteY()
          - scrollbar[3].getAbsoluteY() + scrollbar[3].getHeight() - 6;
      scrollbarTotalLength = scrollbarTopLength + scrollbarBottomLength;
      final double difference = Double.parseDouble(Integer.toString(scrollbarTopLength))
          / Double.parseDouble(Integer.toString(scrollbarTotalLength)) * Double
              .parseDouble(Integer.toString(hiddenScreenHeight));
      boxYPos = Component.getAbsoluteY() - (int) difference;
    } else {
      boxYPos = Component.getAbsoluteY();
    }
    return boxYPos;
  }

  public Rectangle getBoxArea(final RSComponent Component) {
    return new Rectangle(Component.getAbsoluteX(), getActualY(Component),
        Component.getWidth(), Component.getHeight());
  }

  @Override
  public int loop() {
    if (methods.players.getMyPlayer().isInCombat()) {
      return -1;
    }
    final String[] choices = getChoices();
    if (methods.interfaces.get(BOX_IF).isValid()) {
      for (final RSComponent child : methods.interfaces.get(137).getComponents()) {
        if (choices[choices.length - 1].equals(om.Server.toString("rewardEEmote"))) {
          break;
        }
        if (child.containsText(om.Server.toString("rewardAlreadyUnlocked"))
            && child.containsText(om.Server.toString("rewardEmotes"))
            && !child.containsText("<col=0000ff>")) {
          for (int i = 0; i < choices.length; i++) {
            if (choices[i].contains(om.Server.toString("rewardEEmote"))) {
              System.arraycopy(choices, i + 1, choices, i, choices.length - 1 - i);
              choices[choices.length - 1] = om.Server.toString("rewardEEmote");
              break;
            }
          }
        }
      }
      RSComponent[] selection = methods.interfaces.get(BOX_IF).getComponent(BOX_SELECTION_IF).getComponents();
      int optionSelected = 999;
      for (final String choice : choices) {
        for (int i = 0; i < selection.length; i++) {
          if (selection[i].getText().toLowerCase()
              .contains(choice.toLowerCase())) {
            optionSelected = i - 6;
            break;
          }
        }
        if (optionSelected != 999) {
          break;
        }
      }
      if (optionSelected == 999) {
        optionSelected = 0;
      }
      final RSComponent[] scrollbar = methods.interfaces.get(BOX_IF).getComponent(BOX_SCROLLBAR_IF).getComponents();
      if (scrollbarTopLength > 0) {
        methods.mouse.move(scrollbar[1].getAbsoluteX() + random(1, 7),
            scrollbar[1].getAbsoluteY() + random(0, 20));
        methods.mouse.drag((int) methods.mouse.getLocation().getX(),
            (int) methods.mouse.getLocation().getY() - scrollbarTopLength);
      }
      if (getBoxArea(selection[optionSelected]).y > 278) {
        methods.mouse.move(scrollbar[1].getAbsoluteX() + random(1, 7),
            scrollbar[1].getAbsoluteY() + random(20, 30));
        int toDragtoY = (int) (methods.mouse.getLocation().getY() + Double
            .parseDouble(Integer
                .toString((getBoxArea(selection[optionSelected]).y
                    + getBoxArea(selection[optionSelected]).height
                    - selection[0].getAbsoluteY() - viewableScreenHeight)))
            / Double.parseDouble(Integer
                .toString(hiddenScreenHeight)) * Double
                .parseDouble(Integer.toString(scrollbarTotalLength)));
        if (toDragtoY - (int) methods.mouse.getLocation().getY() > scrollbar[5]
            .getAbsoluteY()
            - scrollbar[3].getAbsoluteY()
            + scrollbar[3].getHeight() - 6) {
          toDragtoY = (int) methods.mouse.getLocation().getY()
              + scrollbar[5].getAbsoluteY()
              - scrollbar[3].getAbsoluteY()
              + scrollbar[3].getHeight() - 6;
        }
        methods.mouse.drag((int) methods.mouse.getLocation().getX(), toDragtoY);
      }
      sleep(random(3000, 4000));
      selection = methods.interfaces.get(BOX_IF).getComponent(BOX_SELECTION_IF).getComponents();
      if (selection.length > optionSelected) {
        final int boxX = getBoxArea(selection[optionSelected]).x + 15;
        final int boxY = getBoxArea(selection[optionSelected]).y + 15;
        final int boxWidth = getBoxArea(selection[optionSelected]).width - 30;
        final int boxHeight = getBoxArea(selection[optionSelected]).height - 30;
        methods.mouse.move(random(boxX, boxX + boxWidth),
            random(boxY, boxY + boxHeight));
        methods.mouse.click(true);
        methods.interfaces.get(BOX_IF).getComponent(BOX_CONFIRM_IF).doClick();
      }
      return random(3000, 4000);
    }
    if (methods.interfaces.get(XP_IF).isValid()) {
      methods.interfaces.get(XP_IF).getComponent(XPSelection).doClick();
      methods.interfaces.get(XP_IF).getComponent(CONFIRM_ID).doClick();
      return random(3000, 4000);
    }
    if (methods.inventory.contains(BOX_ID)) {
      methods.inventory.getItem(BOX_ID).interact(om.Server.toString("prisonOpen"));
      return random(3000, 4000);
    }
    if (methods.inventory.contains(BOOK_KNOWLEDGE_ID)) {
      methods.inventory.getItem(BOOK_KNOWLEDGE_ID).interact(om.Server.toString("graveRead"));
      return random(3000, 4000);
    }
    if (methods.inventory.contains(LAMP_ID)) {
      methods.inventory.getItem(LAMP_ID).interact(om.Server.toString("rewardRub"));
      return random(3000, 4000);
    }
    if (methods.inventory.contains(MYSTERY_BOX_ID)) {
      methods.inventory.getItem(MYSTERY_BOX_ID).interact(om.Server.toString("prisonOpen"));
      return random(3000, 4000);
    }
    return -1;
  }

  private String[] getChoices() {
    final String[] choices = new String[2];
    choices[0] = om.Server.toString("rewardXPItem");
    choices[1] = om.Server.toString("rewardCash");

    final String a = methods.account.getName() == null ? null : AccountManager.getReward(methods.account.getName());
    if (a.equals(om.Server.toString("skillAttack"))) {
      XPSelection = ATT_ID;
    } else if (a.equals(om.Server.toString("skillStrength"))) {
      XPSelection = STRENGTH_ID;
    } else if (a.equals(om.Server.toString("skillDefence"))) {
      XPSelection = DEFENCE_ID;
    } else if (a.equals(om.Server.toString("skillRange"))) {
      XPSelection = RANGED_ID;
    } else if (a.equals(om.Server.toString("skillPrayer"))) {
      XPSelection = PRAYER_ID;
    } else if (a.equals(om.Server.toString("skillMagic"))) {
      XPSelection = MAGIC_ID;
    } else if (a.equals(om.Server.toString("skillRunecrafting"))) {
      XPSelection = RUNECRAFTING_ID;
    } else if (a.equals(om.Server.toString("skillConstruction"))) {
      XPSelection = CONSTRUCTION_ID;
    } else if (a.equals(om.Server.toString("skillHitpoints"))) {
      XPSelection = HITPOINTS_ID;
    } else if (a.equals(om.Server.toString("skillAgility"))) {
      XPSelection = AGILITY_ID;
    } else if (a.equals(om.Server.toString("skillHerblore"))) {
      XPSelection = HERBLORE_ID;
    } else if (a.equals(om.Server.toString("skillThieving"))) {
      XPSelection = THIEVING_ID;
    } else if (a.equals(om.Server.toString("skillCrafting"))) {
      XPSelection = CRAFTING_ID;
    } else if (a.equals(om.Server.toString("skillFletching"))) {
      XPSelection = FLETCHING_ID;
    } else if (a.equals(om.Server.toString("skillSlayer"))) {
      XPSelection = SLAYER_ID;
    } else if (a.equals(om.Server.toString("skillHunter"))) {
      XPSelection = HUNTER_ID;
    } else if (a.equals(om.Server.toString("skillMining"))) {
      XPSelection = MINING_ID;
    } else if (a.equals(om.Server.toString("skillSmithing"))) {
      XPSelection = SMITHING_ID;
    } else if (a.equals(om.Server.toString("skillFishing"))) {
      XPSelection = FISHING_ID;
    } else if (a.equals(om.Server.toString("skillCooking"))) {
      XPSelection = COOKING_ID;
    } else if (a.equals(om.Server.toString("skillFiremaking"))) {
      XPSelection = FIREMAKING_ID;
    } else if (a.equals(om.Server.toString("skillWoodcutting"))) {
      XPSelection = WOODCUTTING_ID;
    } else if (a.equals(om.Server.toString("skillFarming"))) {
      XPSelection = FARMING_ID;
    } else if (a.equals(om.Server.toString("skillSummoning"))) {
      XPSelection = SUMMONING_ID;
    } else if (a.equals(om.Server.toString("skillDungeoneering"))) {
      XPSelection = DUNGEONEERING_ID;
    } else {
      XPSelection = WOODCUTTING_ID;
      choices[0] = methods.account.getName() == null ? null : AccountManager.getReward(methods.account.getName());
    }
    return choices;
  }

}