package org.rsbot.event.impl;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;

import org.rsbot.bot.Bot;
import org.rsbot.client.Node;
import org.rsbot.client.RSNPCNode;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.methods.Nodes;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSNPC;

public class DrawNPCs implements PaintListener {

  private final MethodContext methods;

  public DrawNPCs(final Bot bot) {
    methods = bot.getMethodContext();
  }

  @Override
  public void onRepaint(final Graphics render) {
    if (!methods.game.isLoggedIn())
      return;
    final FontMetrics metrics = render.getFontMetrics();
    for (final int element : methods.client.getRSNPCIndexArray()) {
      final Node node = Nodes.lookup(methods.bot.getClient().getRSNPCNC(), element);
      if (node == null || !(node instanceof RSNPCNode))
        continue;
      final RSNPC npc = new RSNPC(methods, ((RSNPCNode) node).getRSNPC());
      final Point location = methods.calc.tileToScreen(npc.getLocation(), npc.getHeight() / 2);
      if (!methods.calc.pointOnScreen(location))
        continue;
      render.setColor(Color.RED);
      render.fillRect((int) location.getX() - 1, (int) location.getY() - 1, 2, 2);
      String s = npc.getID() + (npc.getLevel() > 0 ? " (" + npc.getLevel() + ")" : "");
      render.setColor(npc.isInCombat() ? (npc.isDead() ? Color.gray : Color.red) : npc.isMoving() ? Color.green : Color.WHITE);
      render.drawString(s, location.x - metrics.stringWidth(s) / 2, location.y - metrics.getHeight() / 2);
      if (npc.getAnimation() != -1 || npc.getGraphic() > 0) {
        s = "(A: " + npc.getAnimation() + " | G: " + npc.getGraphic() + ")";
        render.drawString(s, location.x - metrics.stringWidth(s) / 2, location.y - metrics.getHeight() * 3 / 2);
      }
    }
  }
}