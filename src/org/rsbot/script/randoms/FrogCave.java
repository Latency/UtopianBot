package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSNPC;

/**
 * Updated by Iscream Feb 22,10 Updated by Parameter Jan 1, 11
 */
@ScriptManifest(authors = { "Nightmares18", "joku.rules", "Taha", "Fred" }, name = "FrogCave", version = 2.3)
public class FrogCave extends Random {
  public FrogCave(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om = methods.locale;
  private RSNPC               frog;
  private boolean             talkedToHerald, talkedToFrog;
  private int                 tries;

  @Override
  public boolean activateCondition() {
    if (!methods.game.isLoggedIn()) {
      return false;
    } else if (methods.npcs.getNearest(om.Server.toString("frogHerald")) != null && methods.objects.getNearest(5917) != null) {
      sleep(random(2000, 3000));
      return methods.npcs.getNearest(om.Server.toString("frogHerald")) != null && methods.objects.getNearest(5917) != null;
    }
    return false;
  }

  private RSNPC findFrog() {
    return methods.npcs.getNearest(new Filter<RSNPC>() {
      @Override
      public boolean accept(final RSNPC npc) {
        return !npc.isMoving() && npc.getHeight() == -278;
      }
    });
  }

  private boolean canContinue() {
    return methods.interfaces.canContinue() || methods.interfaces.getComponent(65, 6).isValid();
  }

  @Override
  public void onFinish() {
    talkedToHerald = false;
    frog = null;
    tries = 0;
  }

  @Override
  public int loop() {
    try {
      if (!activateCondition()) {
        return -1;
      }
      if (canContinue()) {
        // log("can continue...");
        if (!talkedToHerald) {
          final RSComponent heraldTalkComp = methods.interfaces.getComponent(242, 4);
          talkedToHerald = heraldTalkComp.isValid()
              && (heraldTalkComp.containsText(om.Server.toString("frogCrown")) || heraldTalkComp.containsText(om.Server.toString("frogWaiting")));
        }
        if (!methods.interfaces.clickContinue()) {
          methods.interfaces.getComponent(65, 6).doClick();
        }
        return random(600, 800);
      }
      if (methods.players.getMyPlayer().isMoving()) {
        return random(600, 800);
      }
      if (!talkedToHerald) {
        final RSNPC herald = methods.npcs.getNearest(om.Server.toString("frogHerald"));
        if (methods.calc.distanceTo(herald) < 5) {
          if (!methods.calc.tileOnScreen(herald.getLocation())) {
            methods.camera.turnTo(herald);
          }
          herald.interact(om.Server.toString("srTalkTo"));
          return random(500, 1000);
        }
        methods.walking.walkTileMM(herald.getLocation());
        return random(500, 700);
      }
      if (frog == null) {
        frog = findFrog();
        if (frog != null) {
          log(om.OS.toString("frogPrincessFound") + " " + frog.getID());
        }
      }
      if (frog != null && frog.getLocation() != null && (!talkedToFrog || !canContinue())) {
        if (methods.calc.distanceTo(frog) < 5) {
          if (!methods.calc.tileOnScreen(frog.getLocation())) {
            methods.camera.turnTo(frog);
          }
          if (frog.interact(om.Server.toString("frogTalkToFrog"))) {
            sleep(750, 1250);
            talkedToFrog = canContinue();
          }
          return random(900, 1000);
        }
        methods.walking.walkTileMM(frog.getLocation());
        return random(500, 700);
      }
      tries++;
      if (tries > 200) {
        // log("tries > 200");
        tries = 0;
        talkedToHerald = false;
      }
      return random(200, 400);
    } catch (final Exception e) {
      e.printStackTrace();
    }
    return random(200, 400);
  }
}