package org.rsbot.script.randoms;

import org.rsbot.gui.AccountManager;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;

@ScriptManifest(authors = { "Holo", "Gnarly", "Salty_Fish", "Pervy Shuya", "Doout" }, name = "BankPin", version = 3.0)
public class BankPins extends Random {
  public BankPins(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om = methods.locale;

  @Override
  public boolean activateCondition() {
    return methods.interfaces.get(13).isValid() || methods.interfaces.get(14).isValid();
  }

  void enterCode(final String pin) {
    if (!methods.interfaces.get(13).isValid())
      return;

    final RSComponent[] children = methods.interfaces.get(13).getComponents();
    int state = 0;
    for (int i = 1; i < 5; i++) {
      if (children[i].containsText("?")) {
        state++;
      }
    }
    state = 4 - state;
    if (!methods.interfaces.get(759).isValid()) {
      return;
    }
    final RSComponent[] bankPin = methods.interfaces.get(759).getComponents();
    for (final RSComponent aBankPin : bankPin) {
      if (aBankPin.containsText(pin.substring(state, state + 1))) {
        aBankPin.doClick(true);
        sleep(random(500, 1000));
        break;
      }
    }
  }

  @Override
  public int loop() {
    if (methods.interfaces.get(14).isValid()) {
      methods.interfaces.getComponent(14, 33).doClick();
      sleep(300);
    } else {
      final String pin = AccountManager.getPin(methods.account.getName());
      if (pin == null || pin.length() != 4) {
        log.severe(om.OS.toString("pinAddPin"));
        stopScript(false);
      }
      if (methods.interfaces.get(14).isValid() || !methods.interfaces.get(13).isValid()) {
        methods.interfaces.get(14).getComponent(3).doClick();
        return -1;
      }
      enterCode(pin);
      if (methods.interfaces.get(211).isValid())
        methods.interfaces.get(211).getComponent(3).doClick();
      else if (methods.interfaces.get(217).isValid())
        sleep(random(10500, 12000));
    }
    return 500;
  }
}