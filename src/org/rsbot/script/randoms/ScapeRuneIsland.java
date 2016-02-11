package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

/*
 * Updated by Arbiter (Oct 19, 2010)
 * Updated by Jacmob (Oct 22, 2010)
 * Updated by Jacmob (Oct 24, 2010)
 */
@ScriptManifest(authors = "Arbiter", name = "ScapeRuneIsland", version = 2.2)
public class ScapeRuneIsland extends Random {
  public ScapeRuneIsland(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om          = methods.locale;

  public final int[]          STATUE_IDS  = { 8992, 8993, 8990, 8991 };
  public final RSTile         CENTER_TILE = new RSTile(3421, 4777);

  public RSObject             direction;
  public boolean              finished;
  public boolean              fishing;
  public boolean              forceTalk;

  @Override
  public boolean activateCondition() {
    return methods.calc.distanceTo(CENTER_TILE) < 50;
  }

  @Override
  public void onFinish() {
    direction = null;
    finished = false;
    fishing = false;
    forceTalk = false;
  }

  @Override
  public int loop() {
    if (!activateCondition()) {
      return -1;
    }
    final RSObject statue1 = methods.objects.getNearest(STATUE_IDS[0]);
    final RSObject statue2 = methods.objects.getNearest(STATUE_IDS[1]);
    final RSObject statue3 = methods.objects.getNearest(STATUE_IDS[2]);
    final RSObject statue4 = methods.objects.getNearest(STATUE_IDS[3]);
    if (methods.players.getMyPlayer().isMoving() || methods.players.getMyPlayer().getAnimation() != -1) {
      return random(550, 700);
    }
    if (methods.interfaces.get(241).getComponent(4).isValid() && methods.interfaces.get(241).getComponent(4).getText().contains(om.Server.toString("srCatnap"))) {
      finished = true;
    }
    if (methods.interfaces.get(64).getComponent(4).isValid() && methods.interfaces.get(64).getComponent(4).getText().contains(om.Server.toString("srFallenAsleep"))) {
      finished = true;
    }
    if (methods.interfaces.get(242).getComponent(4).isValid() && methods.interfaces.get(242).getComponent(4).getText().contains(om.Server.toString("srWaitBefore"))) {
      forceTalk = true;
    }
    if (methods.interfaces.canContinue()) {
      if (methods.interfaces.clickContinue()) {
        return random(500, 1000);
      }
    }
    if (forceTalk) {
      RSNPC servant = methods.npcs.getNearest(2481);
      if (servant != null && direction == null && methods.settings.getSetting(344) == 0) {
        if (!methods.calc.tileOnScreen(servant.getLocation())) {
          methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(servant.getLocation()));
          return 700;
        }
        if (servant.interact(om.Server.toString("srTalkTo"))) {
          forceTalk = false;
        }
        return random(1000, 2000);
      }
      if (servant == null) {
        servant = methods.npcs.getNearest(2481);
        if (servant == null) {
          methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(CENTER_TILE));
          return random(1000, 2000);
        }
        return random(50, 100);
      }
    }
    if (finished) {
      final RSObject portal = methods.objects.getNearest(8987);
      if (portal != null) {
        if (!methods.calc.tileOnScreen(portal.getLocation())) {
          methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(portal.getLocation()));
          return random(500, 1000);
        }
        if (portal.interact(om.Server.toString("srEnter"))) {
          return random(6000, 7000);
        }
        return random(500, 1000);
      }
      methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(CENTER_TILE));
    }
    if (methods.bank.isDepositOpen() && methods.bank.getBoxCount() - methods.bank.getBoxCount(6209, 6202, 6200) >= 27) {
      final RSComponent randomItem = methods.interfaces.get(11).getComponent(17).getComponent(random(16, 26));
      final int randomID = randomItem.getComponentID();
      if (randomID < 0) {
        return random(50, 100);
      }
      log(om.OS.toString("srItemWithID") + " " + randomID + " " + "srWasDeposited");
      if (methods.interfaces.get(11).getComponent(17).getComponent(random(16, 26)).interact(om.Server.toString("srDep"))) {
        return random(500, 1000);
      }
      return random(50, 100);
    }
    if (methods.bank.isDepositOpen() && methods.bank.getBoxCount() - methods.bank.getBoxCount(6209, 6202, 6200) < 27) {
      methods.bank.close();
      return random(500, 1000);
    }
    if (methods.inventory.getCountExcept(6209, 6202, 6200) >= 27) {
      final RSObject box = methods.objects.getNearest(32930);
      if (!methods.calc.tileOnScreen(box.getLocation())) {
        methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(box.getLocation()));
        return random(1000, 2000);
      }
      log(om.OS.toString("srDepositing"));
      box.interact(om.Server.toString("bankDeposit"));
      return random(500, 1000);
    }
    if (methods.inventory.getCount(6202) > 0) {
      final RSObject pot = methods.objects.getNearest(8985);
      if (pot != null) {
        if (!methods.calc.tileOnScreen(pot.getLocation())) {
          methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(pot.getLocation()));
          return random(400, 800);
        }
        methods.inventory.getItem(6202).interact(om.Server.toString("inventoryUse"));
        sleep(random(800, 1000));
        if (pot.interact(om.Server.toString("inventoryUse"))) {
          sleep(1000);
        }
        return random(2000, 2400);
      }
      methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(CENTER_TILE));
    }
    if (fishing && methods.inventory.getCount(6209) == 0) {
      final RSGroundItem net = methods.groundItems.getNearest(6209);
      if (net != null) {
        if (!methods.calc.tileOnScreen(net.getLocation())) {
          methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(net.getLocation()));
          return random(800, 1000);
        }
        methods.tiles.interact(net.getLocation(), om.Server.toString("srTake"));
        return random(800, 1000);
      }
      methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(CENTER_TILE));
    }
    if (methods.interfaces.get(246).getComponent(5).containsText(om.Server.toString("srContains")) && methods.settings.getSetting(334) == 1 && direction == null) {
      sleep(2000);
      if (methods.calc.tileOnScreen(statue1.getLocation())) {
        direction = statue1;
        fishing = true;
      }
      if (methods.calc.tileOnScreen(statue2.getLocation())) {
        direction = statue2;
        fishing = true;
      }
      if (methods.calc.tileOnScreen(statue3.getLocation())) {
        direction = statue3;
        fishing = true;
      }
      if (methods.calc.tileOnScreen(statue4.getLocation())) {
        direction = statue4;
        fishing = true;
      }
      log(om.OS.toString("srCheckingDirection"));
      return random(2000, 3000);
    }
    if (direction != null && methods.inventory.getCount(6200) < 1) {
      sleep(random(1000, 1200));
      if (!methods.calc.tileOnScreen(direction.getLocation())) {
        methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(direction.getLocation()));
        return random(400, 600);
      }
      final RSObject spot = methods.objects.getNearest(8986);
      if (spot != null) {
        if (!methods.calc.tileOnScreen(spot.getLocation())) {
          methods.camera.turnTo(spot.getLocation());
        }
        if (!methods.calc.tileOnScreen(spot.getLocation()) && methods.walking.walkTileMM(spot.getLocation())) {
          sleep(random(1000, 2000));
          if (!methods.calc.tileOnScreen(spot.getLocation())) {
            sleep(1000);
          }
        }
        methods.tiles.interact(spot.getLocation(), om.Server.toString("srNet"));
        return random(2000, 2500);
      }
      methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(CENTER_TILE));
    }
    if (methods.inventory.getCount(6200) > 0) {
      final RSNPC cat = methods.npcs.getNearest(2479);
      if (cat != null) {
        if (!methods.calc.tileOnScreen(cat.getLocation())) {
          methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(cat.getLocation()));
        }
        methods.inventory.getItem(6200).interact(om.Server.toString("inventoryUse"));
        sleep(random(500, 1000));
        cat.interact(om.Server.toString("srInteract"));
      } else {
        methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(CENTER_TILE));
      }
      return random(1900, 2200);
    }
    RSNPC servant = methods.npcs.getNearest(2481);
    if (servant != null && direction == null && methods.settings.getSetting(344) == 0) {
      if (!methods.calc.tileOnScreen(servant.getLocation())) {
        methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(servant.getLocation()));
        return 700;
      }
      servant.interact(om.Server.toString("srTalkTo"));
      return random(1000, 2000);
    }
    if (servant == null) {
      servant = methods.npcs.getNearest(2481);
      if (servant == null) {
        methods.walking.walkTileMM(methods.walking.getClosestTileOnMap(CENTER_TILE));
        return random(1000, 2000);
      }
      return random(50, 100);
    }
    log("srSetting" + " " + methods.settings.getSetting(344));
    return random(800, 1200);
  }
}