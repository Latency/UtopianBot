package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "Taha" }, name = "FirstTimeDeath", version = 1.1)
public class FirstTimeDeath extends Random {
  public FirstTimeDeath(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om = methods.locale;
  private int                 step;
  private boolean             exit;
  private RSObject            reaperChair;

  @Override
  public boolean activateCondition() {
    return (reaperChair = methods.objects.getNearest(45802)) != null || (reaperChair = methods.objects.getNearest(45802)) != null;
  }

  @Override
  public int loop() {
    if (!activateCondition()) {
      return -1;
    }
    methods.camera.setPitch(true);
    if (methods.interfaces.canContinue() && !exit) {
      if (methods.interfaces.getComponent(241, 4).getText().contains(om.Server.toString("deathYes"))) {
        step++;
        exit = true;
        return random(200, 400);
      } else if (methods.interfaces.getComponent(242, 5).getText().contains(om.Server.toString("deathEnjoy"))) {
        step++;
        exit = true;
      } else if (methods.interfaces.getComponent(236, 2).getText().contains(om.Server.toString("deathNo")))
        methods.interfaces.getComponent(236, 2).doClick();
      methods.interfaces.clickContinue();
      return random(200, 400);
    }
    switch (step) {
      case 0:
        reaperChair.interact(om.Server.toString("srTalkTo"));
        sleep(random(1000, 1200));
        if (!methods.interfaces.canContinue()) {
          methods.walking.walkTileOnScreen(new RSTile(reaperChair.getLocation().getX() + 2, reaperChair.getLocation().getY() + 1));
          methods.camera.turnTo(reaperChair);
        }
        break;

      case 1:
        final int portalID = 45803;
        final RSObject portal = methods.objects.getNearest(portalID);
        final RSTile loc = methods.players.getMyPlayer().getLocation();
        portal.interact(om.Server.toString("srEnter"));
        sleep(random(1000, 1200));
        if (methods.calc.distanceTo(loc) < 10) {
          methods.camera.turnTo(portal);
          if (!methods.calc.tileOnScreen(portal.getLocation())) {
            methods.walking.walkTileOnScreen(portal.getLocation());
          }
        }
        break;
    }
    return random(200, 400);
  }

  @Override
  public void onFinish() {
    step = -1;
    exit = false;
    reaperChair = null;
  }
}
