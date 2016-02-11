package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSInterface;

@ScriptManifest(authors = { "ToshiXZ" }, name = "TeleotherCloser", version = 1.0)
public class TeleotherCloser extends Random {
  public TeleotherCloser(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om = methods.locale;

  @Override
  public boolean activateCondition() {
    final RSInterface iface = methods.interfaces.get(326);
    return iface.isValid() && iface.getComponent(2).getText().contains(om.Server.toString("wantsToTeleport"));
  }

  @Override
  public int loop() {
    methods.interfaces.get(326).getComponent(8).doClick();
    sleep(random(500, 750));
    log.info(om.OS.toString("disableAA"));
    methods.game.disableAid();
    return -1;
  }
}
