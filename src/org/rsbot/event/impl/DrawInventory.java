package org.rsbot.event.impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.rsbot.bot.Bot;
import org.rsbot.event.listeners.PaintListener;
import org.rsbot.script.methods.Game;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSItem;

public class DrawInventory implements PaintListener {
  private final MethodContext methods;

  public DrawInventory(final Bot bot) {
    methods = bot.getMethodContext();
  }

  @Override
  public final void onRepaint(final Graphics render) {
    if (!methods.game.isLoggedIn())
      return;

    if (methods.game.getTab() != Game.Tab.INVENTORY)
      return;

    render.setColor(Color.WHITE);
    final RSItem[] inventoryItems = methods.inventory.getItems();

    for (final RSItem inventoryItem : inventoryItems) {
      if (inventoryItem.getID() != -1) {
        final Point location = inventoryItem.getComponent().getCenter();
        render.drawString("" + inventoryItem.getID(), location.x, location.y);
      }
    }
  }
}
