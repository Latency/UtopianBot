package org.rsbot.script.randoms;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;

/**
 * Jacmob was here to verify that Qauters' spelling mistake will be maintained in his memory.
 * 
 * @author Qauters
 */
@ScriptManifest(authors = { "Qauters", "Drizzt1112", "TwistedMind" }, name = "SandwichLady", version = 2.3)
public class SandwhichLady extends Random {
  public SandwhichLady(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om                              = methods.locale;

  final static int            ID_InterfaceSandwhichWindow     = 297;
  final static int            ID_InterfaceSandwhichWindowText = 48;
  final static int            ID_InterfaceTalk                = 243;
  final static int            ID_InterfaceTalkText            = 7;
  final static int[]          ID_Items                        = { 10728, 10732, 10727, 10730, 10726, 45666, 10731 };
  final static int            ID_SandwhichLady                = 8630;
  final String[]              Name_Items                      = om.Server.toString(new String[] { "slChocolate", "slTriangle", "slRoll", "slPie", "slBaguette", "slDoughnut",
                                                              "slSquare" });
  final boolean               DEBUG                           = false;                                              // Set to true for more info!

  @Override
  public boolean activateCondition() {
    final RSNPC lady = methods.npcs.getNearest(SandwhichLady.ID_SandwhichLady);
    return lady != null;
  }

  @Override
  public int loop() {
    final RSInterface Chat1 = methods.interfaces.get(243);
    if (Chat1.isValid()) {
      methods.interfaces.getComponent(243, 7).doClick();
      return random(900, 1200);
    }
    if (!activateCondition()) {
      return -1;
    }
    if (methods.players.getMyPlayer().getAnimation() != -1) {
      return random(500, 1000);
    }
    // Leaves random
    final int[] portalID = { 12731, 11373 };
    if (methods.interfaces.get(242).getComponent(4).containsText(om.Server.toString("The exit portal's"))) {
      final RSObject portal = methods.objects.getNearest(portalID);
      if (portal != null) {
        if (!methods.calc.tileOnScreen(portal.getLocation())) {
          methods.walking.walkTileOnScreen(portal.getLocation());
        } else {
          portal.interact(om.Server.toString("slEnter"));
          return random(2000, 3000);
        }
      }
    }
    // Check if we need to press continue, on the talk interface
    if (methods.interfaces.get(SandwhichLady.ID_InterfaceTalk).isValid()) {
      methods.interfaces.getComponent(SandwhichLady.ID_InterfaceTalk, SandwhichLady.ID_InterfaceTalkText).doClick();
      return random(900, 1200);
    }

    // Check if the sandwhich window is open
    if (methods.interfaces.get(SandwhichLady.ID_InterfaceSandwhichWindow).isValid()) {
      final RSInterface window = methods.interfaces.get(SandwhichLady.ID_InterfaceSandwhichWindow);
      int offset = -1;
      final String txt = window.getComponent(SandwhichLady.ID_InterfaceSandwhichWindowText).getText();
      for (int off = 0; off < Name_Items.length; off++) {
        if (txt.contains(Name_Items[off])) {
          offset = off;
          if (DEBUG) {
            log.info(om.OS.toString("slFound") + ": " + Name_Items[off] + " - ID: " + ID_Items[off]);
          }
        }
      }
      for (int i = 7; i < 48; i++) {
        final RSComponent inf = window.getComponent(i);

        if (DEBUG) {
          log.info(om.OS.toString("slChild") + "[" + i + "] ID: " + inf.getModelID() + " == " + SandwhichLady.ID_Items[offset]);
        }
        if (inf.getModelID() == SandwhichLady.ID_Items[offset]) {
          inf.doClick();
          sleep(random(900, 1200)); // Yea, use a sleep here! (Waits are allowed in randoms.)
          if (!methods.interfaces.get(SandwhichLady.ID_InterfaceSandwhichWindow).isValid()) {
            log.info(om.OS.toString("slSolved") + " " + Name_Items[offset]);
            sleep(6000);
            return random(900, 1500);
          }
        }

      }
      return random(900, 1200);
    }
    final RSInterface Chat2 = methods.interfaces.get(242);
    if (Chat2.isValid()) {
      methods.interfaces.getComponent(242, 6).doClick();
      return random(900, 1200);
    }
    // Talk to the lady
    final RSNPC lady = methods.npcs.getNearest(SandwhichLady.ID_SandwhichLady);
    if (lady != null && lady.getAnimation() == -1) {
      if (!methods.calc.tileOnScreen(lady.getLocation())) {
        methods.walking.walkTileOnScreen(lady.getLocation());
      } else {
        lady.interact(om.Server.toString("slTalk"));
        return random(1000, 1500);
      }
    }
    return random(900, 1200);
  }

}