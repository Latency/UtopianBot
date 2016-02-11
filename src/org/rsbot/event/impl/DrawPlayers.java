package org.rsbot.event.impl;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import org.rsbot.bot.Bot;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSPlayer;

public class DrawPlayers implements PaintListener {

  private final MethodContext methods;
  
  public DrawPlayers(final Bot bot) {
    methods = bot.getMethodContext();
  }

  @Override
  public final void onRepaint(final Graphics render) {
    if (!methods.game.isLoggedIn())
      return;
    final org.rsbot.client.RSPlayer[] players = methods.client.getRSPlayerArray();
    if (players == null)
      return;
    final FontMetrics metrics = render.getFontMetrics();
    for (final org.rsbot.client.RSPlayer element : players) {
      if (element == null)
        continue;
      final RSPlayer player = new RSPlayer(methods, element);
      final Point location = methods.calc.tileToScreen(player.getLocation(), player.getHeight() / 2);
      if (!methods.calc.pointOnScreen(location))
        continue;
      render.setColor(Color.RED);
      render.fillRect((int) location.getX() - 1, (int) location.getY() - 1, 2, 2);
      String s = player.getName() + " (" + player.getCombatLevel() + ")";
      render.setColor(player.isInCombat() ? (player.isDead() ? Color.GRAY : Color.RED) : player.isMoving() ? Color.GREEN : Color.WHITE);
      render.drawString(s, location.x - metrics.stringWidth(s) / 2, location.y - metrics.getHeight() / 2);
      final String msg = player.getMessage();
      boolean raised = false;
      if (player.getAnimation() != -1 || player.getGraphic() > 0 || player.getNPCID() != -1) {
        if (player.getNPCID() != -1)
          s = "(NPC: " + player.getNPCID() + " | L: " + player.getLevel() + " | A: " + player.getAnimation() + " | G: " + player.getGraphic() + ")";
        else
          s = "(A: " + player.getAnimation() + " | G: " + player.getGraphic() + ")";
        render.drawString(s, location.x - metrics.stringWidth(s) / 2, location.y - metrics.getHeight() * 3 / 2);
        raised = true;
      }
      if (msg != null) {
        render.setColor(Color.ORANGE);
        render.drawString(msg, location.x - metrics.stringWidth(msg) / 2, location.y - metrics.getHeight() * (raised ? 5 : 3) / 2);
      }
    }
  }
}