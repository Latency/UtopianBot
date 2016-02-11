package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

/*
 * Certer Random event solver
 * Coding by joku.rules, Fixed by FuglyNerd
 * Interface data taken from Certer solver by Nightmares18
 */
@ScriptManifest(authors = { "joku.rules" }, name = "Certer", version = 1.0)
public class Certer extends Random {
  public Certer(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om           = methods.locale;

  private final int[]         MODEL_IDS    = { 2807, 8828, 8829, 8832, 8833, 8834, 8835,
                                           8836, 8837 };
  private final int[]         bookPiles    = { 42352, 42354 };
  private final String[]      ITEM_NAMES   = om.Server.toString(new String[] { "certBowl", "certBattleAxe", "certFish",
                                           "certShield", "certHelmet", "certRing", "certShears", "certSword", "certSpade" });

  private boolean             readyToLeave = false;
  private int                 failCount    = 0;

  @Override
  public void onFinish() {
    failCount = 0;
    readyToLeave = false;
  }

  @Override
  public boolean activateCondition() {
    return methods.game.isLoggedIn() && methods.objects.getNearest(bookPiles) != null;
  }

  @Override
  public int loop() {
    if (!activateCondition() && readyToLeave) {
      readyToLeave = false;
      failCount = 0;
      return -1;
    }
    if (methods.players.getMyPlayer().getAnimation() != -1 || methods.players.getMyPlayer().isMoving()) {
      return random(500, 1000);
    }
    if (methods.interfaces.getComponent(241, 4).containsText(om.Server.toString("certAhem") + ", ")) {

      readyToLeave = false;
    }

    if (methods.interfaces.getComponent(241, 4).containsText(om.Server.toString("certCorrect"))
        || methods.interfaces.getComponent(241, 4).containsText(om.Server.toString("certYouGo"))) {
      readyToLeave = true;
    }

    if (readyToLeave) {
      final int PORTAL_ID = 11368;
      final RSObject portal = methods.objects.getNearest(PORTAL_ID);
      if (portal != null) {
        final RSTile portalLocation = portal.getLocation();
        if (portal.isOnScreen()) {
          portal.interact(om.Server.toString("srEnter"));
          return random(3000, 4000);
        }
        methods.walking.walkTileMM(new RSTile(portalLocation.getX() - 1, portalLocation.getY()).randomize(1, 1));
        return random(1500, 2000);
      }
    }

    if (methods.interfaces.getComponent(184, 0).isValid()) {
      final int modelID = methods.interfaces.getComponent(184, 8).getComponents()[3]
          .getModelID();
      String itemName = null;
      for (int i = 0; i < MODEL_IDS.length; i++) {
        if (MODEL_IDS[i] == modelID) {
          itemName = ITEM_NAMES[i];
        }
      }

      if (itemName == null) {
        log(om.Server.toString("certObjectID") + ": " + modelID);
        failCount++;
        if (failCount > 10) {
          stopScript(false);
          return -1;
        }
        return random(1000, 2000);
      }

      for (int j = 0; j < 3; j++) {
        final RSComponent iface = methods.interfaces.getComponent(184, 8)
            .getComponents()[j];
        if (iface.containsText(itemName)) {
          iface.doClick();
          return random(1000, 1200);
        }
      }
    }

    if (methods.interfaces.canContinue()) {
      methods.interfaces.clickContinue();
      return random(1000, 1200);
    }

    final RSNPC certer = methods.npcs.getNearest(om.Server.toString("certNiles"), om.Server.toString("certMiles"), om.Server.toString("certGiles"));
    if (certer != null) {
      if (methods.calc.distanceTo(certer) < 4) {
        certer.interact(om.Server.toString("srTalkTo"));
        return random(2500, 3000);
      }
      final RSTile certerLocation = certer.getLocation();
      methods.walking.walkTileMM(new RSTile(certerLocation.getX() + 2, certerLocation.getY()).randomize(1, 1));
      return random(3000, 3500);
    }

    failCount++;
    if (failCount > 10) {
      stopScript(false);
      return -1;
    }
    return random(400, 600);
  }
}