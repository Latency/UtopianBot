package org.rsbot.event.impl;

import java.awt.Graphics;

import org.rsbot.bot.Bot;
import org.rsbot.event.listeners.TextPaintListener;
import org.rsbot.script.methods.Menu;
import org.rsbot.util.StringUtil;

public class TMenuActions implements TextPaintListener {
  private final Menu         menu;

  public TMenuActions(final Bot bot) {
    menu = bot.getMethodContext().menu;
  }

  @Override
  public final int drawLine(final Graphics render, int idx) {
    final String[] actions = menu.getActions();
    final String[] options = menu.getOptions();
    if (actions.length != options.length)
      return idx;
    for (int i = 0; i < actions.length; i++)
      StringUtil.drawLine(render, idx++, i + ": [red]" + actions[i] + " " + options[i]);
    return idx;
  }
}