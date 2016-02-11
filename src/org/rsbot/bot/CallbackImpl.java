package org.rsbot.bot;

import java.util.Map;

import org.rsbot.client.Callback;
import org.rsbot.client.Render;
import org.rsbot.client.RenderData;
import org.rsbot.event.events.CharacterMovedEvent;
import org.rsbot.event.events.MessageEvent;
import org.rsbot.script.Script;
import org.rsbot.script.provider.MethodContext;

public class CallbackImpl implements Callback {

  private final Bot bot;

  public CallbackImpl(final Bot bot) {
    this.bot = bot;
  }

  @Override
  public Bot getBot() {
    return bot;
  }

  @Override
  public void notifyMessage(final int id, final String sender, final String msg) {
    final MessageEvent m = new MessageEvent(sender, id, msg);
    bot.getEventManager().dispatchEvent(m);
  }

  @Override
  public void rsCharacterMoved(final org.rsbot.client.RSCharacter c, final int i) {
    final CharacterMovedEvent e = new CharacterMovedEvent(bot.getScriptHandler().getMethodContext(), c, i);
    bot.getEventManager().dispatchEvent(e);
  }

  @Override
  public void updateRenderInfo(final Render r, final RenderData rd) {
    final Map<Integer, Script> scripts = bot.getScriptHandler().getRunningScripts();
    for (final Script s : scripts.values()) {
      if (s.getMethodContext().bot == bot) {
        if (bot.getScriptHandler() != null) {
          final MethodContext ctx = bot.getScriptHandler().getMethodContext();
          if (ctx != null)
            ctx.calc.updateRenderInfo(r, rd);
        }
        break;
      }
    }
  }
}
