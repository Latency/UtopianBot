package org.rsbot.script.randoms;

import java.awt.Point;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.util.Timer;

@ScriptManifest(authors = "endoskeleton", name = "CapnArnav", version = 1)
public class CapnArnav extends Random {
  public CapnArnav(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager  om                     = methods.locale;

  private static final int[]   ARNAV_CHEST            = { 42337, 42338 };
  private static final int     ARNAV_ID               = 2308;
  private static final int     EXIT_PORTAL            = 11369;
  private static final int[][] INTERFACE_SOLVE_IDS    = { { 7, 14, 21 }, // BOWL
      { 5, 12, 19 }, // RING
      { 6, 13, 20 }, // COIN
      { 8, 15, 22 }                                  // BAR
                                                      };
  private static final int[][] ARROWS                 = { { 2, 3 }, { 9, 10 }, { 16, 17 } };
  private static final int     TALK_INTERFACE         = 228;
  private static final int     CHEST_INTERFACE_PARENT = 185;
  private static final int     CHEST_INTERFACE_UNLOCK = 28;
  private static final int     CHEST_INTERFACE_CENTER = 23;

  private static enum STATE {
    OPEN_CHEST, SOLVE, TALK, EXIT
  }

  private int index = -1;

  @Override
  public boolean activateCondition() {
    final RSNPC captain = methods.npcs.getNearest(ARNAV_ID);

    if (captain != null) {
      sleep(random(1500, 1600));
      final RSObject portal = methods.objects.getNearest(EXIT_PORTAL);

      return portal != null;

    }

    return false;
  }

  @Override
  public void onFinish() {
    index = -1;
  }

  private STATE getState() {
    if (methods.objects.getNearest(ARNAV_CHEST[1]) != null) {
      return STATE.EXIT;
    } else if (methods.interfaces.canContinue()
        || methods.interfaces.get(TALK_INTERFACE) != null
        && methods.interfaces.get(TALK_INTERFACE).isValid()) {
      return STATE.TALK;
    } else if (methods.interfaces.get(CHEST_INTERFACE_PARENT) == null
        || !methods.interfaces.get(CHEST_INTERFACE_PARENT).isValid()) {
      return STATE.OPEN_CHEST;
    } else {
      return STATE.SOLVE;
    }
  }

  @Override
  public int loop() {
    if (methods.bank.isDepositOpen() || methods.bank.isOpen()) {
      methods.bank.close();
    }

    if (!activateCondition()) {
      return -1;
    }

    if (methods.players.getMyPlayer().isMoving()) {
      return random(1000, 2000);
    }

    switch (getState()) {
      case EXIT:
        final RSObject portal = methods.objects.getNearest(EXIT_PORTAL);
        if (portal != null) {
          if (!portal.isOnScreen()) {
            methods.camera.turnTo(portal);
          }
          if (portal.interact(om.Server.toString("srEnter"))) {
            return random(3000, 3500);
          }
        }
        break;

      case OPEN_CHEST:
        final RSObject chest = methods.objects.getNearest(ARNAV_CHEST);
        if (chest != null) {
          if (chest.doClick()) {
            return random(1000, 1300);
          }
        }
        break;

      case TALK:
        if (methods.interfaces.canContinue()) {
          methods.interfaces.clickContinue();
          return random(1500, 2000);
        }
        final RSComponent okay = methods.interfaces.getComponent(TALK_INTERFACE, 3);
        if (okay != null && okay.isValid()) {
          okay.doClick();
        }
        return random(1500, 2000);

      case SOLVE:
        final RSInterface solver = methods.interfaces.get(CHEST_INTERFACE_PARENT);
        if (solver != null && solver.isValid()) {

          final String s = solver.getComponent(32).getText();
          if (s.contains(om.Server.toString("capnBowl"))) {
            index = 0;
          } else if (s.contains("capnRing")) {
            index = 1;
          } else if (s.contains("capnCoin")) {
            index = 2;
          } else if (s.contains("capnBar")) {
            index = 3;
          }

          if (solved()) {
            solver.getComponent(CHEST_INTERFACE_UNLOCK).doClick();
            return random(600, 900);
          }

          final RSComponent container = solver
              .getComponent(CHEST_INTERFACE_CENTER);
          for (int i = 0; i < 3; i++) {
            int rand = random(0, 100);
            if (rand < 50) {
              rand = 0;
            } else if (rand >= 50) {
              rand = 1;
            }
            final RSComponent target = solver
                .getComponent(INTERFACE_SOLVE_IDS[index][i]);
            final RSComponent arrow = solver.getComponent(ARROWS[i][rand]);
            while (container.isValid()
                && target.isValid()
                && !container.getArea().contains(
                    new Point(target.getCenter().x + 15, target
                        .getCenter().y)) && arrow.isValid()
                && new Timer(10000).isRunning()) {
              arrow.doClick();
              sleep(random(1000, 1200));
            }
          }

        }
    }
    return random(500, 800);
  }

  private boolean solved() {
    if (index == -1) {
      return false;
    }
    final RSInterface solver = methods.interfaces.get(CHEST_INTERFACE_PARENT);
    if (solver != null && solver.isValid()) {
      final RSComponent container = solver.getComponent(CHEST_INTERFACE_CENTER);

      final Point p1 = solver.getComponent(INTERFACE_SOLVE_IDS[index][0])
          .getCenter();
      p1.setLocation(p1.x + 15, p1.y);
      final Point p2 = solver.getComponent(INTERFACE_SOLVE_IDS[index][1])
          .getCenter();
      p2.setLocation(p2.x + 15, p1.y);
      final Point p3 = solver.getComponent(INTERFACE_SOLVE_IDS[index][2])
          .getCenter();
      p3.setLocation(p3.x + 15, p1.y);
      return container.getArea().contains(p1)
          && container.getArea().contains(p2) && container.getArea()
              .contains(p3);
    }
    return false;
  }

}