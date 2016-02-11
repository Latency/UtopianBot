package org.rsbot.script.randoms;

import java.awt.Point;

import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSModel;
import org.rsbot.script.wrappers.RSObject;

@ScriptManifest(authors = { "Iscream", "Aelin", "LM3", "IceCandle", "Taha" }, name = "Pinball", version = 2.7)
public class Pinball extends Random {

  public Pinball(MethodContext ctx) {
    super(ctx);
  }

  private static final int[] OBJ_PILLARS       = { 15000, 15002, 15004, 15006, 15008 };

  private static final int[] OBJ_ACTIVATE      = { 15000, 15002, 15004, 15006, 15007, 15008 };

  private static final int   INTERFACE_PINBALL = 263;

  @Override
  public boolean activateCondition() {
    return methods.game.isLoggedIn() && methods.objects.getNearest(OBJ_ACTIVATE) != null;
  }

  private int getScore() {
    final RSComponent score = methods.interfaces.get(INTERFACE_PINBALL).getComponent(1);
    try {
      return Integer.parseInt(score.getText().split(" ")[1]);
    } catch (final java.lang.ArrayIndexOutOfBoundsException t) {
      return 10;
    }
  }

  @Override
  public int loop() {
    if (!activateCondition()) {
      return -1;
    }
    if (methods.players.getMyPlayer().isMoving() || methods.players.getMyPlayer().getAnimation() != -1) {
      return random(300, 500);
    }
    if (getScore() >= 10) {
      final int OBJ_EXIT = 15010;
      final RSObject exit = methods.objects.getNearest(OBJ_EXIT);
      if (exit != null) {
        if (methods.calc.tileOnScreen(exit.getLocation()) && exit.interact("Exit")) {
          sleep(random(2000, 2200));
          exit.interact("Exit");
          return random(2000, 2100);
        }
        methods.camera.setCompass('s');
        methods.walking.walkTileOnScreen(exit.getLocation());
        return random(1400, 1500);
      }
    }
    final RSObject pillar = methods.objects.getNearest(OBJ_PILLARS);
    if (pillar != null) {
      if (methods.calc.distanceTo(pillar) > 2 && !pillar.isOnScreen()) {
        methods.walking.walkTileOnScreen(pillar.getLocation());
        return random(500, 600);
      }
      if (pillar != null) {
        doClick(pillar);
      }
      final int before = getScore();
      for (int i = 0; i < 50; i++) {
        if (getScore() > before) {
          return random(50, 100);
        }
        sleep(100, 200);
      }
    }
    return random(50, 100);
  }

  private void doClick(final RSObject pillar) {
    final RSModel model = pillar.getModel();
    if (model != null) {
      final Point central = model.getCentralPoint();
      methods.mouse.click(central.x, central.y, 4, 4, true);
      return;
    }
    final Point p = methods.calc.tileToScreen(pillar.getLocation());
    if (methods.calc.pointOnScreen(p))
      methods.mouse.click(p.x, p.y - 25, 4, 20, true);
  }

}
