package org.rsbot.script.randoms;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import org.rsbot.constants.IBank;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSCharacter;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;

/**
 * <p>
 * This short-sighted gravedigger has managed to put five coffins in the wrong graves. <br />
 * If he'd looked more closely at the headstones, he might have known where each one was supposed to go! <br />
 * Help him by matching the contents of each coffin with the headstones in the graveyard. Easy, huh?
 * </p>
 * <p/>
 * Last Update: 1.7 08/06/11 icnhzabot.
 * 
 * @author Qauters
 */
@ScriptManifest(authors = { "Qauters" }, name = "GraveDigger", version = 1.7)
public class GraveDigger extends Random {
  public GraveDigger(final MethodContext ctx) {
    super(ctx);
    groups.add(new Group(7614, new int[] { 7603, 7605, 7612 }));
    groups.add(new Group(7615, new int[] { 7600, 7601, 7604 }));
    groups.add(new Group(7616, new int[] { 7597, 7606, 7607 }));
    groups.add(new Group(7617, new int[] { 7602, 7609, 7610 }));
    groups.add(new Group(7618, new int[] { 7599, 7608, 7613 }));
  }

  private final OutputManager om = methods.locale;

  class Group {
    // IDs used later
    int         coffinID = -1;
    int         graveID  = -1;

    // General group data
    final int   graveStoneModelID;
    final int[] coffinModelIDs;

    public Group(final int graveStoneModelID, final int[] coffinModelIDs) {
      this.graveStoneModelID = graveStoneModelID;
      this.coffinModelIDs = coffinModelIDs;
    }

    public boolean isGroup(final int graveStoneModelID) {
      return this.graveStoneModelID == graveStoneModelID;
    }

    public boolean isGroup(final int[] coffinModelIDs) {
      for (final int modelID : this.coffinModelIDs) {
        boolean found = false;
        for (final int coffinModelID : coffinModelIDs) {
          if (modelID == coffinModelID) {
            found = true;
          }
        }
        if (!found) {
          return false;
        }
      }
      return true;
    }
  }

  private static final int[]     coffinIDs                       = { 7587, 7588, 7589, 7590, 7591 };
  private static final int[]     graveStoneIDs                   = { 12716, 12717, 12718, 12719,
                                                                 12720 };
  private static final int[]     filledGraveIDs                  = { 12721, 12722, 12723, 12724,
                                                                 12725 };
  private static final int[]     emptyGraveIDs                   = { 12726, 12727, 12728, 12729,
                                                                 12730 };

  private static final int       INTERFACE_READ_GRAVESTONE       = 143;
  private static final int       INTERFACE_READ_GRAVESTONE_MODEL = 2;
  private static final int       INTERFACE_READ_GRAVESTONE_CLOSE = 3;
  private static final int       INTERFACE_CHECK_COFFIN          = 141;
  private static final int       INTERFACE_CHECK_COFFIN_CLOSE    = 12;
  private static final int[]     INTERFACE_CHECK_COFFIN_ITEMS    = { 3, 4, 5, 6, 7,
                                                                 8, 9, 10, 11 };

  @SuppressWarnings("unused")
  private static final int[]     NOT_TO_DEPOSIT                  = { 1351, 1349, 1353, 1361, 1355,
                                                                 1357, 1359, 4031, 6739, 13470, 14108, 1265, 1267, 1269, 1296, 1273,
                                                                 1271, 1275, 15259, 303, 305, 307, 309, 311, 10129, 301, 13431, 313,
                                                                 314, 2347, 995, 10006, 10031, 10008, 10012, 11260, 10150, 10010,
                                                                 556, 558, 555, 557, 554, 559, 562, 560, 565, 8013, 4251, 8011,
                                                                 8010, 8009, 8008, 8007 };

  private final ArrayList<Group> groups                          = new ArrayList<Group>();

  private int                    tmpID                           = -1, tmpStatus = -1;              // used to store some data across

  // loops

  @Override
  public void onFinish() {
    tmpID = -1;
    tmpStatus = -1;
  }

  @Override
  public boolean activateCondition() {
    return methods.settings.getSetting(696) != 0 && methods.objects.getNearest(12731) != null;
  }

  @Override
  public int loop() {
    if (methods.npcs.getNearest(om.Server.toString("graveLeo")) == null) {
      return -1;
    }
    if (methods.inventory.getCountExcept(GraveDigger.coffinIDs) > 23) {
      if (methods.interfaces.canContinue()) {
        methods.interfaces.clickContinue();
        sleep(random(1500, 2000));
      }
      final RSObject depo = methods.objects.getNearest(12731);
      if (depo != null) {
        if (!methods.calc.tileOnScreen(depo.getLocation())) {
          methods.walking.walkTo(depo.getLocation());
          methods.camera.turnTo(depo);
        } else {
          depo.interact(om.Server.toString("bankDeposit"));
        }
      }
      if (methods.interfaces.get(IBank.DEPOSIT_BOX.getID()).isValid()) {
        sleep(random(700, 1200));
        methods.interfaces.get(11).getComponent(17).getComponent(27).interact(om.Server.toString("srDep"));
        sleep(random(700, 1200));
        methods.interfaces.get(11).getComponent(17).getComponent(26).interact(om.Server.toString("srDep"));
        sleep(random(700, 1200));
        methods.interfaces.get(11).getComponent(17).getComponent(25).interact(om.Server.toString("srDep"));
        sleep(random(700, 1200));
        methods.interfaces.get(11).getComponent(17).getComponent(24).interact(om.Server.toString("srDep"));
        sleep(random(700, 1200));
        methods.interfaces.get(11).getComponent(17).getComponent(23).interact(om.Server.toString("srDep"));
        sleep(random(700, 1200));
        methods.interfaces.getComponent(11, 15).doClick();
        return random(500, 700);
      }
      return random(2000, 3000);
    }
    if (methods.players.getMyPlayer().isMoving()) {
    } else if (methods.players.getMyPlayer().getAnimation() == 827) {
    } else if (methods.interfaces.get(242).isValid()) {
      // Check if we finished before
      if (methods.interfaces.get(242).containsText(om.Server.toString("graveLeave"))) {
        tmpStatus++;
      }
      methods.interfaces.getComponent(242, 6).doClick();
    } else if (methods.interfaces.get(64).isValid()) {
      methods.interfaces.getComponent(64, 5).doClick();
    } else if (methods.interfaces.get(241).isValid()) {
      methods.interfaces.getComponent(241, 5).doClick();
    } else if (methods.interfaces.get(243).isValid()) {
      methods.interfaces.getComponent(243, 7).doClick();
    } else if (methods.interfaces.get(220).isValid()) {
      methods.interfaces.getComponent(220, 16).doClick();
    } else if (methods.interfaces.get(236).isValid()) {
      if (methods.interfaces.get(236).containsText(om.Server.toString("graveLeave"))) {
        methods.interfaces.getComponent(236, 1).doClick();
      } else {
        methods.interfaces.getComponent(236, 2).doClick();
      }
    } else if (methods.interfaces.get(GraveDigger.INTERFACE_CHECK_COFFIN).isValid()) {
      if (tmpID >= 0) {
        final int[] items = new int[GraveDigger.INTERFACE_CHECK_COFFIN_ITEMS.length];
        final RSInterface inters = methods.interfaces.get(GraveDigger.INTERFACE_CHECK_COFFIN);
        for (int i = 0; i < GraveDigger.INTERFACE_CHECK_COFFIN_ITEMS.length; i++) {
          items[i] = inters.getComponent(GraveDigger.INTERFACE_CHECK_COFFIN_ITEMS[i]).getComponentID();
        }
        for (final Iterator<Group> it = groups.iterator(); it.hasNext() && tmpID >= 0;) {
          final Group g = it.next();
          if (g.isGroup(items)) {
            g.coffinID = tmpID;
            tmpID = -1;
          }
        }
      }
      atCloseInterface(GraveDigger.INTERFACE_CHECK_COFFIN, GraveDigger.INTERFACE_CHECK_COFFIN_CLOSE);
    } else if (methods.interfaces.get(GraveDigger.INTERFACE_READ_GRAVESTONE).isValid()) {
      final int modelID = methods.interfaces.get(GraveDigger.INTERFACE_READ_GRAVESTONE).getComponent(GraveDigger.INTERFACE_READ_GRAVESTONE_MODEL).getComponentID();
      for (final Group g : groups) {
        if (g.isGroup(modelID)) {
          g.graveID = tmpID;
        }
      }
      atCloseInterface(GraveDigger.INTERFACE_READ_GRAVESTONE, GraveDigger.INTERFACE_READ_GRAVESTONE_CLOSE);
    } else if (tmpStatus == 0 && tmpID != -1) {
      for (final Group g : groups) {
        if (g.graveID == tmpID) {
          final RSObject obj = methods.objects.getNearest(g.graveID);
          if (obj == null || !setObjectInScreen(obj)) {
            log.info(om.OS.toString("graveShutdown"));
            methods.game.logout(false);
            return -1;
          }
          if (!methods.inventory.selectItem(GraveDigger.coffinIDs[g.coffinID])) {
            sleep(random(500, 800));
            methods.inventory.selectItem(GraveDigger.coffinIDs[g.coffinID]);
          }
          if (!obj.interact(om.Server.toString("graveUseCoffin"))) {
            sleep(random(100, 500));
            if (!obj.interact(om.Server.toString("graveUseCoffin"))) {
              sleep(random(100, 500));
              obj.doClick(true);
            }
          }
          // Wait for about 10s to finish
          final long cTime = System.currentTimeMillis();
          while (System.currentTimeMillis() - cTime < 10000) {
            if (methods.inventory.getItem(GraveDigger.coffinIDs[g.coffinID]) == null) {
              break;
            }
            sleep(random(400, 700));
          }
          break;
        }
      }
      tmpID = -1;
    } else if (tmpStatus == -1 && methods.objects.getNearest(GraveDigger.filledGraveIDs) != null) {
      final RSObject obj = methods.objects.getNearest(GraveDigger.filledGraveIDs);
      if (obj == null || !setObjectInScreen(obj)) {
        log.severe(om.OS.toString("graveShutdown"));
        methods.game.logout(false);
        return -1;
      }
      obj.interact(om.Server.toString("graveTakeCoffin"));
    } else if (tmpStatus == 0 && methods.objects.getNearest(GraveDigger.emptyGraveIDs) != null) {
      final RSObject obj = methods.objects.getNearest(GraveDigger.emptyGraveIDs);
      final int id = obj.getID();
      for (int i = 0; i < GraveDigger.emptyGraveIDs.length; i++) {
        if (GraveDigger.emptyGraveIDs[i] == id) {
          final RSObject objGS = methods.objects.getNearest(GraveDigger.graveStoneIDs[i]);
          if (objGS == null || !setObjectInScreen(objGS)) {
            log.severe(om.OS.toString("graveStoneShutdown"));
            methods.game.logout(false);
            return -1;
          }
          tmpID = obj.getID();
          // if (Bot.getClient().isItemSelected() == 1) {
          // objects.atObject(objGS, "Use");
          // }
          objGS.interact(om.Server.toString("graveRead"));
        }
      }
    } else if (tmpStatus == -1) {
      final ArrayList<Integer> agc = new ArrayList<Integer>();
      for (int i = 0; i < GraveDigger.coffinIDs.length; i++) {
        agc.add(i);
      }
      for (final Group g : groups) {
        if (g.coffinID != -1) {
          agc.remove(new Integer(g.coffinID));
        }
      }
      if (tmpStatus == -1 && agc.size() == 0) {
        tmpStatus++;
      }
      while (tmpStatus == -1) {
        final int i = random(0, agc.size());
        if (methods.inventory.getCount(GraveDigger.coffinIDs[agc.get(i)]) > 0) {
          tmpID = agc.get(i);
          methods.inventory.getItem(GraveDigger.coffinIDs[agc.get(i)]).interact("Check");

          return random(1800, 2400); // We are looking at the model
        }
      }
    } else if (tmpStatus == 0) {
      // Done
      final RSNPC leo = methods.npcs.getNearest(om.Server.toString("graveLeo"));
      if (leo == null || !setCharacterInScreen(leo)) {
        log.severe(om.OS.toString("graveLeoShutdown"));
        methods.game.logout(false);
        return -1;
      }
      // Teleport Ani - 8939
      if (methods.players.getMyPlayer().getAnimation() == -1) {
        leo.interact(om.Server.toString("srTalkTo"));
      }
    }
    return random(1400, 1800);
  }

  public void atCloseInterface(final int parent, final int child) {
    final RSComponent i = methods.interfaces.getComponent(parent, child);
    if (!i.isValid()) {
      return;
    }
    final Rectangle pos = i.getArea();
    if (pos.x == -1 || pos.y == -1 || pos.width == -1 || pos.height == -1) {
      return;
    }
    final int dx = (int) (pos.getWidth() - 4) / 2;
    final int dy = (int) (pos.getHeight() - 4) / 2;
    final int midx = (int) (pos.getMinX() + pos.getWidth() / 2);
    final int midy = (int) (pos.getMinY() + pos.getHeight() / 2);
    methods.mouse.click(midx + random(-dx, dx) - 5, midy + random(-dy, dy), true);
  }

  public boolean setCharacterInScreen(final RSCharacter ch) {
    // Check if it's on screen, if not make it on screen.
    for (int i = 0; i < 3; i++) {
      final Point screenLocation = ch.getScreenLocation();
      if (!methods.calc.pointOnScreen(screenLocation)) {
        switch (i) {
          case 0:
            methods.camera.turnTo(ch);
            sleep(random(200, 500));
            break;
          case 1:
            methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(ch.getLocation().randomize(2, 2)));
            sleep(random(1800, 2000));
            while (methods.players.getMyPlayer().isMoving()) {
              sleep(random(200, 500));
            }
            break;
          default:
            return false;
        }
      }
    }
    return true;
  }

  public boolean setObjectInScreen(final RSObject obj) {
    // Check if it's on screen, if not make it on screen.
    for (int i = 0; i < 3; i++) {
      final Point screenLocation = methods.calc.tileToScreen(obj.getLocation());
      if (!methods.calc.pointOnScreen(screenLocation)) {
        switch (i) {
          case 0:
            methods.camera.turnTo(obj);
            sleep(random(200, 500));
            break;
          case 1:
            methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(obj.getLocation().randomize(2, 2)));
            sleep(random(1800, 2000));
            while (methods.players.getMyPlayer().isMoving()) {
              sleep(random(200, 500));
            }
            break;
          default:
            return false;
        }
      }
    }
    return true;
  }
}