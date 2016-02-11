package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;

@ScriptManifest(name = "LeaveSafeArea", authors = "Taha", version = 1.0)
public class LeaveSafeArea extends Random {
  public LeaveSafeArea(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om = methods.locale;

  @Override
  public boolean activateCondition() {
    return methods.interfaces.getComponent(212, 2).containsText(om.Server.toString("safeThings")) &&
        methods.interfaces.getComponent(212, 2).getAbsoluteY() > 380 &&
        methods.interfaces.getComponent(212, 2).getAbsoluteY() < 410 ||
        methods.interfaces.getComponent(236, 1).containsText(om.Server.toString("safeStart")) &&
        methods.interfaces.getComponent(236, 1).getAbsoluteY() > 390 &&
        methods.interfaces.getComponent(236, 1).getAbsoluteY() < 415;
  }

  @Override
  public int loop() {
    if (methods.interfaces.canContinue()) {
      methods.interfaces.clickContinue();
      sleep(random(1000, 1200));
    }
    methods.interfaces.getComponent(236, 1).doClick();
    return -1;
  }

}
