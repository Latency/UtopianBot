package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Keilgo" }, name = "DrillDemon", version = 0.2)
public class DrillDemon extends Random {
  public DrillDemon(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om      = methods.locale;

  public final int            demonID = 2790;
  public int                  sign1;
  public int                  sign2;
  public int                  sign3;
  public int                  sign4;

  @Override
  public void onFinish() {
    sign1 = -1;
    sign2 = -1;
    sign3 = -1;
    sign4 = -1;
  }

  @Override
  public boolean activateCondition() {
    return playerInArea(3167, 4822, 3159, 4818);
  }

  @Override
  public int loop() {
    methods.camera.setPitch(true);
    methods.camera.setCompass('N');

    if (methods.players.getMyPlayer().isMoving() || methods.players.getMyPlayer().getAnimation() != -1) {
      return random(1900, 2400);
    }

    final RSNPC demon = methods.npcs.getNearest(demonID);
    final RSObject mat1 = methods.objects.getNearest(10076);
    final RSObject mat2 = methods.objects.getNearest(10077);
    final RSObject mat3 = methods.objects.getNearest(10078);
    final RSObject mat4 = methods.objects.getNearest(10079);

    if (demon == null) {
      return -1;
    }

    myClickContinue();
    sleep(random(750, 1000));

    if (methods.interfaces.get(148).isValid()) {
      switch (methods.settings.getSetting(531)) {
        case 668:
          sign1 = 1;
          sign2 = 2;
          sign3 = 3;
          sign4 = 4;
          break;
        case 675:
          sign1 = 2;
          sign2 = 1;
          sign3 = 3;
          sign4 = 4;
          break;
        case 724:
          sign1 = 1;
          sign2 = 3;
          sign3 = 2;
          sign4 = 4;
          break;
        case 738:
          sign1 = 3;
          sign2 = 1;
          sign3 = 2;
          sign4 = 4;
          break;
        case 787:
          sign1 = 2;
          sign2 = 3;
          sign3 = 1;
          sign4 = 4;
          break;
        case 794:
          sign1 = 3;
          sign2 = 2;
          sign3 = 1;
          sign4 = 4;
          break;
        case 1116:
          sign1 = 1;
          sign2 = 2;
          sign3 = 4;
          sign4 = 3;
          break;
        case 1123:
          sign1 = 2;
          sign2 = 1;
          sign3 = 4;
          sign4 = 3;
          break;
        case 1228:
          sign1 = 1;
          sign2 = 4;
          sign3 = 2;
          sign4 = 3;
          break;
        case 1249:
          sign1 = 4;
          sign2 = 1;
          sign3 = 2;
          sign4 = 3;
          break;
        case 1291:
          sign1 = 2;
          sign2 = 4;
          sign3 = 1;
          sign4 = 3;
          break;
        case 1305:
          sign1 = 4;
          sign2 = 2;
          sign3 = 1;
          sign4 = 3;
          break;
        case 1620:
          sign1 = 1;
          sign2 = 3;
          sign3 = 4;
          sign4 = 2;
          break;
        case 1634:
          sign1 = 3;
          sign2 = 1;
          sign3 = 4;
          sign4 = 2;
          break;
        case 1676:
          sign1 = 1;
          sign2 = 4;
          sign3 = 3;
          sign4 = 2;
          break;
        case 1697:
          sign1 = 4;
          sign2 = 1;
          sign3 = 3;
          sign4 = 2;
          break;
        case 1802:
          sign1 = 3;
          sign2 = 4;
          sign3 = 1;
          sign4 = 2;
          break;
        case 1809:
          sign1 = 4;
          sign2 = 3;
          sign3 = 1;
          sign4 = 2;
          break;
        case 2131:
          sign1 = 2;
          sign2 = 3;
          sign3 = 4;
          sign4 = 1;
          break;
        case 2138:
          sign1 = 3;
          sign2 = 2;
          sign3 = 4;
          sign4 = 1;
          break;
        case 2187:
          sign1 = 2;
          sign2 = 4;
          sign3 = 3;
          sign4 = 1;
          break;
        case 2201:
          sign1 = 4;
          sign2 = 2;
          sign3 = 3;
          sign4 = 1;
          break;
        case 2250:
          sign1 = 3;
          sign2 = 4;
          sign3 = 2;
          sign4 = 1;
          break;
        case 2257:
          sign1 = 4;
          sign2 = 3;
          sign3 = 2;
          sign4 = 1;
          break;
      }
    }

    if (methods.interfaces.getComponent(148, 1).getText().contains(om.Server.toString("drillJumps"))) {
      if (sign1 == 1) {
        if (methods.calc.distanceTo(new RSTile(3167, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3160, 4820));
          mat1.interact(om.Server.toString("inventoryUse"));
        } else {
          mat1.interact(om.Server.toString("inventoryUse"));
        }
        return random(2000, 2500);
      } else if (sign2 == 1) {
        mat2.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign3 == 1) {
        mat3.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign4 == 1) {
        if (methods.calc.distanceTo(new RSTile(3159, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3166, 4820));
          mat4.interact(om.Server.toString("inventoryUse"));
        } else {
          mat4.interact(om.Server.toString("inventoryUse"));
        }
        return random(2000, 2500);
      }
    } else if (methods.interfaces.getComponent(148, 1).getText()
        .contains(om.Server.toString("drillPushUps"))) {
      if (sign1 == 2) {
        if (methods.calc.distanceTo(new RSTile(3167, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3160, 4820));
          mat1.interact(om.Server.toString("inventoryUse"));
        } else {
          mat1.interact(om.Server.toString("inventoryUse"));
        }
        return random(2000, 2500);
      } else if (sign2 == 2) {
        mat2.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign3 == 2) {
        mat3.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign4 == 2) {
        if (methods.calc.distanceTo(new RSTile(3159, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3166, 4820));
          mat4.interact(om.Server.toString("inventoryUse"));
        } else {
          mat4.interact(om.Server.toString("inventoryUse"));
        }
        return random(2000, 2500);
      }
    } else if (methods.interfaces.getComponent(148, 1).getText()
        .contains(om.Server.toString("drillSitUps"))) {
      if (sign1 == 3) {
        if (methods.calc.distanceTo(new RSTile(3167, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3160, 4820));
          mat1.interact(om.Server.toString("inventoryUse"));
        } else {
          mat1.interact(om.Server.toString("inventoryUse"));
        }
        return random(1000, 1500);
      } else if (sign2 == 3) {
        mat2.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign3 == 3) {
        mat3.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign4 == 3) {
        if (methods.calc.distanceTo(new RSTile(3159, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3166, 4820));
          mat4.interact(om.Server.toString("inventoryUse"));
        } else {
          mat4.interact(om.Server.toString("inventoryUse"));
        }
        return random(2000, 2500);
      }
    } else if (methods.interfaces.getComponent(148, 1).getText().contains(om.Server.toString("drillJogOn"))) {
      if (sign1 == 4) {
        if (methods.calc.distanceTo(new RSTile(3167, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3160, 4820));
          mat1.interact(om.Server.toString("inventoryUse"));
        } else {
          mat1.interact(om.Server.toString("inventoryUse"));
        }
        return random(2000, 2500);
      } else if (sign2 == 4) {
        mat2.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign3 == 4) {
        mat3.interact(om.Server.toString("inventoryUse"));
        return random(2000, 2500);
      } else if (sign4 == 4) {
        if (methods.calc.distanceTo(new RSTile(3159, 4820)) < 2) {
          methods.walking.walkTileMM(new RSTile(3166, 4820));
          mat4.interact(om.Server.toString("inventoryUse"));
        } else {
          mat4.interact(om.Server.toString("inventoryUse"));
        }
        return random(2000, 2500);
      }
    }

    if (!myClickContinue() && methods.players.getMyPlayer().getAnimation() == -1) {
      demon.interact(om.Server.toString("srTalkTo"));
    }

    return random(2000, 2500);
  }

  public boolean myClickContinue() {
    sleep(random(800, 1000));
    return methods.interfaces.getComponent(243, 7).doClick()
        || methods.interfaces.getComponent(241, 5).doClick()
        || methods.interfaces.getComponent(242, 6).doClick()
        || methods.interfaces.getComponent(244, 8).doClick()
        || methods.interfaces.getComponent(64, 5).doClick();
  }

  public boolean playerInArea(final int maxX, final int maxY, final int minX,
      final int minY) {
    final int x = methods.players.getMyPlayer().getLocation().getX();
    final int y = methods.players.getMyPlayer().getLocation().getY();
    return x >= minX && x <= maxX && y >= minY && y <= maxY;
  }
}