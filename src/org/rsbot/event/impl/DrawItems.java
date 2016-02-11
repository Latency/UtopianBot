package org.rsbot.event.impl;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;

import org.rsbot.bot.Bot;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSGroundItem;
import org.rsbot.script.wrappers.RSModel;
import org.rsbot.script.wrappers.RSPlayer;
import org.rsbot.script.wrappers.RSTile;

public class DrawItems implements PaintListener {

  private final MethodContext methods;

  public DrawItems(final Bot bot) {
    methods = bot.getMethodContext();
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
        final Point screen = methods.calc.tileToScreen(new RSTile(x, y));
        if (!methods.calc.pointOnScreen(screen))
          continue;
        final RSGroundItem[] items = methods.groundItems.getAllAt(x, y);
        if (items.length > 0) {
          final RSModel model = items[0].getModel();
          if (model != null) {
            render.setColor(Color.BLUE);
            for (final Polygon polygon : model.getTriangles())
              render.drawPolygon(polygon);
          }
        }
        for (int i = 0; i < items.length; i++) {
          render.setColor(Color.RED);
          render.fillRect((int) screen.getX() - 1, (int) screen.getY() - 1, 2, 2);
          final String s = "" + items[i].getItem().getID();
          final int ty = screen.y - tHeight * (i + 1) + tHeight / 2;
          final int tx = screen.x - metrics.stringWidth(s) / 2;
          render.setColor(Color.green);
          render.drawString(s, tx, ty);
        }
      }
    }
  }
}
