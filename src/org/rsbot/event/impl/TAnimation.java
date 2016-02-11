package org.rsbot.event.impl;

import java.awt.Graphics;

import org.rsbot.bot.Bot;
import org.rsbot.event.listeners.TextPaintListener;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.util.StringUtil;

public class TAnimation implements TextPaintListener {

  private final MethodContext methods;

  public TAnimation(final Bot bot) {
    methods = bot.getMethodContext();
  }

  @Override
  public final int drawLine(final Graphics render, int idx) {
    int animation;
    if (methods.game.isLoggedIn())
      animation = methods.players.getMyPlayer().getAnimation();
    else
      animation = -1;
    StringUtil.drawLine(render, idx++, "Animation " + animation);
    return idx;
  }

}
