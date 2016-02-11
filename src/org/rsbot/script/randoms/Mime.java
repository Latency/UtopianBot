package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSTile;

@ScriptManifest(authors = { "PwnZ", "Taha", "Zenzie" }, name = "Mime", version = 1.3)
public class Mime extends Random {
  public Mime(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om = methods.locale;

  private enum Stage {
    click, findMime, findAnimation, clickAnimation, wait
  }

  private int   animation;
  private RSNPC mime;

  @Override
  public void onFinish() {
    mime = null;
    animation = -1;
  }

  @Override
  public boolean activateCondition() {
    final RSNPC mime = methods.npcs.getNearest(1056);
    return mime != null && methods.calc.distanceTo(mime.getLocation()) < 15;
  }

  private boolean clickAnimation(final String find) {
    if (!methods.interfaces.get(188).isValid()) {
      return false;
    }
    for (int a = 0; a < methods.interfaces.get(188).getChildCount(); a++) {
      if (methods.interfaces.get(188).getComponent(a).getText().contains(find)) {
        log(om.OS.toString("mimeClickedOn") + ": " + find);
        sleep(random(500, 1000));
        methods.interfaces.get(188).getComponent(a).doClick();
        sleep(random(1000, 1200));
        return true;
      }
    }
    return false;
  }

  private RSNPC getNPCAt(final RSTile t) {
    for (final RSNPC npc : methods.npcs.getAll()) {
      if (npc.getLocation().equals(t)) {
        return npc;
      }
    }
    return null;
  }

  private Stage getStage() {
    if (methods.interfaces.canContinue() && methods.players.getMyPlayer().getLocation().equals(new RSTile(2008, 4764))) {
      return Stage.click;
    } else if (mime == null) {
      return Stage.findMime;
    } else if ((methods.interfaces.get(372).getComponent(2).getText().contains(om.Server.toString("mimeWatch")) || methods.interfaces.get(372).getComponent(
        3).getText().contains(om.Server.toString("mimeWatch"))) && mime.getAnimation() != -1 && mime.getAnimation() != 858) {
      return Stage.findAnimation;
    } else if (methods.interfaces.get(188).isValid()) {
      return Stage.clickAnimation;
    } else {
      return Stage.wait;
    }
  }

  @Override
  public int loop() {
    if (!activateCondition()) {
      return -1;
    }
    switch (getStage()) {
      case click:
        methods.interfaces.clickContinue();
        sleep(random(1500, 2000));
        return random(200, 400);

      case findMime:
        if ((mime = methods.npcs.getNearest(1056)) == null && (mime = getNPCAt(new RSTile(2011, 4762))) == null) {
          log.warning(om.OS.toString("mimeError"));
          return -1;
        }
        return random(200, 400);

      case findAnimation:
        animation = mime.getAnimation();
        log.info(om.OS.toString("mimeFound") + ": " + animation);
        sleep(1000);
        if (methods.interfaces.get(188).isValid()) {
          return random(400, 800);
        }
        final long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start >= 5000) {
          if (methods.interfaces.get(188).isValid()) {
            return random(1000, 1600);
          }
          sleep(random(1000, 1500));
        }
        return random(200, 400);

      case clickAnimation:
        // log.info("Clicking text according to animation: " + animation);
        if (animation != -1 && animation != 858) {
          switch (animation) {
            case 857:
              clickAnimation(om.Server.toString("mimeThink"));
              break;
            case 860:
              clickAnimation(om.Server.toString("mimeCry"));
              break;
            case 861:
              clickAnimation(om.Server.toString("mimeLaugh"));
              break;
            case 866:
              clickAnimation(om.Server.toString("mimeDance"));
              break;
            case 1128:
              clickAnimation(om.Server.toString("mimeGlassWall"));
              break;
            case 1129:
              clickAnimation(om.Server.toString("mimeLean"));
              break;
            case 1130:
              clickAnimation(om.Server.toString("mimeRope"));
              break;
            case 1131:
              clickAnimation(om.Server.toString("mimeGlassBox"));
              break;
            default:
              log.info(om.OS.toString("Unknown Animation") + ": " + animation + " " + om.OS.toString("Please inform a developer at UScripts.info!"));
              return random(2000, 3000);
          }
        }
      case wait:
        return random(200, 400);
    }
    return random(200, 400);
  }
}