package org.rsbot.event.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.rsbot.bot.Bot;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.script.wrappers.RSTile;

public class DrawGround implements PaintListener {

  private final MethodContext methods;

  public DrawGround(final Bot bot) {
    methods = bot.getMethodContext();
  }

  @Override
  public final void onRepaint(final Graphics render) {
    if (!methods.game.isLoggedIn())
      return;
    final RSPlayer player = methods.players.getMyPlayer();
    if (player == null)
      return;
    render.setColor(Color.WHITE);
    final RSTile location = player.getLocation();
    for (int x = location.getX() - 25; x < location.getX() + 25; x++) {
      for (int y = location.getY() - 25; y < location.getY() + 25; y++) {
        final RSGroundItem[] item = methods.groundItems.getAllAt(x, y);
        if (item == null || item.length == 0) {
          continue;
        }
        final Point screen = methods.calc.tileToScreen(item[0].getLocation());
        if (methods.calc.pointOnScreen(screen))
          render.drawString(new Integer(item[0].getItem().getID()).toString(), location.getX() - 10, location.getY());
      }
    }
  }
}
