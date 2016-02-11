package org.rsbot.event.impl;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;

import org.rsbot.bot.Bot;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.script.wrappers.RSTile;

public class DrawObjects implements PaintListener {

  private final MethodContext methods;

  public DrawObjects(final Bot bot) {
    methods = bot.getMethodContext();
  }

  private static final HashMap<RSObject.Type, Color> color_map = new HashMap<RSObject.Type, Color>();

  static {
    color_map.put(RSObject.Type.BOUNDARY, Color.BLACK);
    color_map.put(RSObject.Type.FLOOR_DECORATION, Color.YELLOW);
    color_map.put(RSObject.Type.INTERACTABLE, Color.WHITE);
    color_map.put(RSObject.Type.WALL_DECORATION, Color.GRAY);
  }

  @Override
  public final void onRepaint(final Graphics render) {
    if (!methods.game.isLoggedIn())
      return;
    final RSPlayer player = methods.players.getMyPlayer();
    if (player == null)
      return;
    final FontMetrics metrics = render.getFontMetrics();
    final RSTile location = player.getLocation();
    final int locX = location.getX();
    final int locY = location.getY();
    final int tHeight = metrics.getHeight();
    for (int x = locX - 25; x < locX + 25; x++) {
      for (int y = locY - 25; y < locY + 25; y++) {
        final RSTile tile = new RSTile(x, y);
        final Point screen = methods.calc.tileToScreen(tile);
        if (!methods.calc.pointOnScreen(screen))
          continue;
        final RSObject[] objects = methods.objects.getAllAt(tile);
        int i = 0;
        for (final RSObject object : objects) {
          final Point real = methods.calc.tileToScreen(object.getLocation());
          if (!methods.calc.pointOnScreen(real))
            continue;
          if (screen.x > -1) {
            render.setColor(Color.GREEN);
            render.fillRect(screen.x - 1, screen.y - 1, 2, 2);
            render.setColor(Color.RED);
            render.drawLine(screen.x, screen.y, real.x, real.y);
          }
          final String s = new Integer(object.getID()).toString();
          final int ty = real.y - tHeight / 2 - i++ * 15;
          final int tx = real.x - metrics.stringWidth(s) / 2;
          render.setColor(color_map.get(object.getType()));
          render.drawString(s, tx, ty);
        }
      }
    }
  }
}
