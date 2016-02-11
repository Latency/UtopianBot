package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.util.Timer;

/**
 * Advanced System Update script that will logout at a random time during a system update.
 * 
 * @author Gnarly
 */
@ScriptManifest(authors = { "Gnarly", "Pervy Shuya" }, name = "SystemUpdate", version = 1.5)
public class SystemUpdate extends Random {
  public SystemUpdate(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om         = methods.locale;

  @SuppressWarnings("unused")
  private int                 logoutSeconds;
  private final Timer         systemTime = new Timer(0L);

  @Override
  public boolean activateCondition() {
    if (methods.game.isLoggedIn() && methods.interfaces.getComponent(754, 5).getText().startsWith("<col=ffff00>" + om.Server.toString("systemUpdateIn")) && !methods.players.getMyPlayer().isInCombat())
      check();
    return false;
  }

  @Override
  public int loop() {
    return -1;
  }

  private void check() {
    final int logoutMinutes = random(1, getMinutes());
    // logoutSeconds = random(10, getSeconds());
    systemTime.setEndIn(logoutMinutes);
    while (!checkForLogout())
      sleep(1000);
    log.info(om.Server.toString("sysUpdateSoon" + "."));
  }

  private boolean checkForLogout() {
    // if ((getMinutes() < logoutMinutes) && (getSeconds() < logoutSeconds))
    // {
    if (!systemTime.isRunning()) {
      stopScript(false);
      return true;
    }
    return false;
  }

  private int getMinutes() {
    return Integer.parseInt(methods.interfaces.getComponent(754, 5).getText().substring(29).trim().split(":")[0]);
  }

  @SuppressWarnings("unused")
  private int getSeconds() {
    return Integer.parseInt(methods.interfaces.getComponent(754, 5).getText().substring(29).trim().split(":")[1]);
  }

}
