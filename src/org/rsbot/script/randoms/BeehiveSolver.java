package org.rsbot.script.randoms;

import java.awt.Point;
import java.awt.Rectangle;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSNPC;

/**
 * Update by Iscream (Apr 24,2010) Update by Iscream (Apr 15,2010) Update by Arbiter (Sep 25,2010)
 * 
 * @author Pwnaz0r & Velocity
 * @version 2.3 - 04/03/09
 */
@ScriptManifest(authors = { "Pwnaz0r", "Velocity" }, name = "BeeHive", version = 2.5)
public class BeehiveSolver extends Random {
  public BeehiveSolver(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om                       = methods.locale;

  RSNPC                       BeehiveKeeper;
  private static final int    BEEHIVE_KEEPER_ID        = 8649;
  private static final int[]  DEST_INTERFACE_IDS       = { 16, 17, 18, 19 };
  private static final int    ID_DOWN                  = 16034;
  private static final int    ID_MIDDOWN               = 16022;
  private static final int    ID_MIDUP                 = 16025;
  private static final int    ID_TOP                   = 16036;
  private static final int[]  BEEHIVE_ARRAYS           = { ID_TOP, ID_MIDUP, ID_MIDDOWN, ID_DOWN };
  private final String[]      MODEL_NAMES              = om.Server.toString(new String[] { "hiveTop", "hiveMiddleUp", "hiveMiddleDown", "hiveDown" });
  boolean                     solved;
  private static final int[]  START_INTERFACE_IDS      = { 12, 13, 14, 15 };
  private static final int    INTERFACE_BEEHIVE_WINDOW = 420;
  private static final int    BUILD_BEEHIVE            = 40;
  private static final int    CLOSE_WINDOW             = 38;

  @Override
  public void onFinish() {
    BeehiveKeeper = null;
    solved = false;
  }

  @Override
  public boolean activateCondition() {
    if (!methods.game.isLoggedIn()) {
      return false;
    }

    if (methods.npcs.getNearest(BEEHIVE_KEEPER_ID) != null && methods.objects.getNearest(16168) != null) {
      solved = false;
      return true;
    }

    /*
     * BeehiveKeeper = npcs.getNearest(BEEHIVE_KEEPER_ID); if ((BeehiveKeeper != null) || getBeehiveInterface().isValid()) { sleep(random(1000, 1500)); BeehiveKeeper = npcs.getNearest(BEEHIVE_KEEPER_ID); if ((BeehiveKeeper != null) || getBeehiveInterface().isValid()) { solved = false; sleep(random(1000, 1500)); return true; } }
     */
    return false;
  }

  public boolean dragmethods_interfaces(final RSComponent child1, final RSComponent child2) {
    final Point start = returnMidInterface(child1);
    final Point finish = returnMidInterface(child2);

    methods.mouse.move(start);
    methods.mouse.drag(finish);
    return true;
  }

  public RSInterface getBeehiveInterface() {
    return methods.interfaces.get(420);
  }

  @Override
  public int loop() {
    BeehiveKeeper = methods.npcs.getNearest(BEEHIVE_KEEPER_ID);
    if (BeehiveKeeper == null) {
      // log.severe("Beekeeper Random Finished Succesfully");
      return -1;
    }

    if (myClickContinue()) {
      return 200;
    }

    if (methods.interfaces.getComponent(236, 2).doClick()) {
      return random(800, 1200);
    }

    if (getBeehiveInterface().isValid()) {
      for (int i = 1; i < 5; i++) {
        log.info(om.OS.toString("beehiveCheckID") + ": " + i);
        final int id = returnIdAtSlot(i);
        dragmethods_interfaces(getBeehiveInterface().getComponent(START_INTERFACE_IDS[i - 1]),
            getBeehiveInterface().getComponent(returnDragTo(id)));
      }
      sleep(2000);
      // Wait is necessary for delay in the change of a setting.
      if (methods.settings.getSetting(805) == 109907968) {
        solved = true;
        log(om.OS.toString("beehiveFinished"));
      } else {
        methods.interfaces.getComponent(INTERFACE_BEEHIVE_WINDOW, CLOSE_WINDOW).doClick();
        return random(500, 1000);
      }
      if (solved && methods.interfaces.getComponent(INTERFACE_BEEHIVE_WINDOW, BUILD_BEEHIVE).doClick()) {
        return random(900, 1600);
      }
    } else {
      log.info(om.OS.toString("beehiveInvalid"));
    }

    if (methods.players.getMyPlayer().getInteracting() == null && !solved) {
      final RSNPC npc = methods.npcs.getNearest(BEEHIVE_KEEPER_ID);
      if (npc != null) {
        if (!npc.interact(om.Server.toString("srTalkTo"))) {
          methods.camera.setAngle(methods.camera.getAngle() + random(-30, 30));
        }
      }
    }

    return random(500, 1000);
  }

  public boolean myClickContinue() {
    sleep(random(800, 1000));
    return methods.interfaces.getComponent(243, 7).doClick() || methods.interfaces.getComponent(241,
        5).doClick() || methods.interfaces.getComponent(
        242, 6).doClick() || methods.interfaces.getComponent(244, 8).doClick() || methods.interfaces.getComponent(64,
        5).doClick();
  }

  public static int returnDragTo(final int Model) {
    switch (Model) {
      case 16036:
        return DEST_INTERFACE_IDS[0];
      case 16025:
        return DEST_INTERFACE_IDS[1];
      case 16022:
        return DEST_INTERFACE_IDS[2];
      case 16034:
        return DEST_INTERFACE_IDS[3];
      default:
        return -1;
    }
  }

  public int returnIdAtSlot(final int slot) {
    if (slot < 1 || slot > 4) {
      log.info(om.OS.toString("beehiveInvalidSlot"));
      methods.interfaces.getComponent(INTERFACE_BEEHIVE_WINDOW, CLOSE_WINDOW).doClick();
    }

    final int Model_ID = getBeehiveInterface().getComponent(returnSlotId(slot)).getModelID();

    if (Model_ID == -1) {
      log.info(om.OS.toString("beehiveRestart"));
      methods.interfaces.getComponent(INTERFACE_BEEHIVE_WINDOW, CLOSE_WINDOW).doClick();
    }

    for (int i = 0; i < BEEHIVE_ARRAYS.length; i++) {
      if (Model_ID == BEEHIVE_ARRAYS[i]) {
        log.info(om.OS.toString("beehiveSlot") + " " + slot + " " + om.OS.toString("beehiveSelection") + ": " + MODEL_NAMES[i]);
        return Model_ID;
      }
    }

    return -1;
  }

  public static Point returnMidInterface(final RSComponent child) {
    Point point = new Point(-1, -1);
    final Rectangle rect = child.getArea();
    if (rect != null) {
      point = new Point((int) rect.getCenterX(), (int) rect.getCenterY());
    }
    return point;
  }

  public int returnSlotId(final int slot) {
    switch (slot) {
      case 1:
        return 25;
      case 2:
        return 22;
      case 3:
        return 23;
      case 4:
        return 21;
      default:
        log.info(om.OS.toString("beehiveInvalidRestart"));
        methods.interfaces.getComponent(INTERFACE_BEEHIVE_WINDOW, CLOSE_WINDOW).doClick();
        break;
    }
    return -1;
  }
}