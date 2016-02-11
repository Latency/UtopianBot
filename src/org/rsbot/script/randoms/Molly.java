package org.rsbot.script.randoms;

import java.util.ArrayList;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

//Checked 3/7/10

/**
 * Updated by aman (Nov 14, 09) Updated by Equilibrium (Dec 13, 09) Updated by Fred (Dec 25, 09) Updated by Iscream (Jan 31, 10) Updated by Iscream (Feb 20, 10) Updated by Jacmob (Oct 24, 10) Updated by Arbiter (Nov 11, 10)
 * 
 * @author PwnZ
 */
@ScriptManifest(authors = { "PwnZ" }, name = "Molly", version = 1.9)
public class Molly extends Random {
  public Molly(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om                           = methods.locale;

  static final int            CLAW_ID                      = 14976;
  static final int            CONTROL_PANEL_ID             = 14978;
  static final int            DOOR_ID                      = 14982;
  static final int            MOLLY_CHATBOX_INTERFACEGROUP = 228;
  static final int            MOLLY_CHATBOX_NOTHANKS       = 3;
  static final int            MOLLY_CHATBOX_SKIP           = 2;
  static final int            CONTROL_INTERFACEGROUP       = 240;
  static final int            CONTROLS_GRAB                = 28;
  static final int            CONTROLS_UP                  = 29;
  static final int            CONTROLS_DOWN                = 30;
  static final int            CONTROLS_LEFT                = 31;
  static final int            CONTROLS_RIGHT               = 32;
  static final int            CONTROLS_EXIT                = 33;

  private RSNPC               molly;
  private RSObject            controlPanel;
  private int                 mollyID                      = -1;
  private boolean             cameraSet;
  private boolean             talkedToMolly;
  private boolean             finished;
  private long                delayTime;

  @Override
  public void onFinish() {
    mollyID = -1;
    cameraSet = false;
    talkedToMolly = false;
    finished = false;
    delayTime = -1;
  }

  @Override
  public boolean activateCondition() {
    molly = methods.npcs.getNearest(om.Server.toString("mollyMolly"));
    controlPanel = methods.objects.getNearest(Molly.CONTROL_PANEL_ID);
    return molly != null && molly.isInteractingWithLocalPlayer()
        || controlPanel != null;
  }

  private boolean inControlInterface() {
    final RSInterface i = methods.interfaces.get(Molly.CONTROL_INTERFACEGROUP);
    return i != null && i.isValid();
  }

  private boolean inControlRoom() {
    final RSObject o = methods.objects.getNearest(DOOR_ID);
    return o != null
        && methods.players.getMyPlayer().getLocation().getX() > o.getLocation().getX();
  }

  @Override
  public int loop() {
    if (!activateCondition()) {
      log(om.OS.toString("mollyFinished"));
      sleep(500);
      if (!activateCondition()) {
        return -1;
      }
    }
    controlPanel = methods.objects.getNearest(Molly.CONTROL_PANEL_ID);
    while (methods.players.getMyPlayer().isMoving() || methods.players.getMyPlayer().getAnimation() != -1) {
      return random(800, 1300);
    }
    if (methods.interfaces.canContinue()) {
      methods.interfaces.clickContinue();
      return random(250, 750);
    }
    if (mollyID == -1) {
      mollyID = molly.getID();
      // log("Molly ID: " + Integer.toString(mollyID));
      // log("Evil Twin ID:" + Integer.toString(mollyID - 40));
    }
    if (methods.interfaces.canContinue()) {
      setCamera();
      methods.interfaces.clickContinue();
      return random(500, 800);
    }
    final RSComponent skipInterface = methods.interfaces.get(
        Molly.MOLLY_CHATBOX_INTERFACEGROUP).getComponent(
        Molly.MOLLY_CHATBOX_SKIP);
    if (skipInterface != null && skipInterface.isValid()
        && skipInterface.getAbsoluteY() > 5
        && skipInterface.containsText(om.Server.toString("mollyYes"))) {
      skipInterface.doClick();
      return random(600, 1000);
    }
    final RSComponent noThanksInterface = methods.interfaces.get(
        Molly.MOLLY_CHATBOX_INTERFACEGROUP).getComponent(
        Molly.MOLLY_CHATBOX_NOTHANKS);
    if (noThanksInterface != null && noThanksInterface.isValid()
        && noThanksInterface.getAbsoluteY() > 5) {
      setCamera();
      sleep(random(800, 1200));
      noThanksInterface.doClick();
      talkedToMolly = true;
      return random(600, 1000);
    }
    if (!cameraSet) {
      methods.camera.setPitch(true);
      cameraSet = true;
      return random(300, 500);
    }
    if (finished && !inControlRoom()) {
      if (!methods.calc.tileOnScreen(molly.getLocation())) {
        methods.walking.walkTileOnScreen(molly.getLocation());
        return random(1000, 2000);
      }
      molly.interact(om.Server.toString("slTalk"));
      return random(1000, 1200);
    }
    if (finished && inControlRoom()) {
      if (!openDoor()) {
        return random(1000, 1500);
      }
      return random(400, 600);
    }
    if (!inControlRoom()) {
      if (talkedToMolly
          && !finished
          && (methods.interfaces.get(Molly.MOLLY_CHATBOX_INTERFACEGROUP) == null || methods.interfaces
              .get(Molly.MOLLY_CHATBOX_INTERFACEGROUP)
              .getComponent(0).getAbsoluteY() < 2)
          && (methods.interfaces.get(Molly.MOLLY_CHATBOX_NOTHANKS) == null || methods.interfaces
              .get(Molly.MOLLY_CHATBOX_NOTHANKS).getComponent(0)
              .getAbsoluteY() < 2)) {
        openDoor();
        sleep(random(800, 1200));
      } else {
        molly.interact(om.Server.toString("slTalk"));
        talkedToMolly = true;
        return random(1000, 2000);
      }
    } else {
      if (methods.npcs.getNearest(om.Server.toString("mollyMolly")) != null) {
        finished = true;
        sleep(random(800, 1200));
      } else {
        if (!inControlInterface()) {
          if (methods.calc.tileOnScreen(controlPanel.getLocation())) {
            controlPanel.interact(om.Server.toString("inventoryUse"));
            sleep(random(1200, 2000));
          } else {
            methods.walking.walkTileOnScreen(controlPanel.getLocation());
            methods.camera.setPitch(true);
            methods.camera.turnTo(controlPanel);
          }
        } else {
          navigateClaw();
          delayTime = System.currentTimeMillis();
          while (!methods.interfaces.canContinue()
              && System.currentTimeMillis() - delayTime < 15000) {
          }
          if (methods.interfaces.canContinue()) {
            methods.interfaces.clickContinue();
          }
          sleep(random(300, 400));
        }
      }
    }
    return random(200, 400);
  }

  private void navigateClaw() {
    if (!inControlInterface() || mollyID < 1) {
      return;
    }
    RSObject claw;
    RSNPC suspect;
    while ((claw = methods.objects.getNearest(Molly.CLAW_ID)) != null
        && (suspect = methods.npcs.getNearest(mollyID - 40)) != null) {
      final RSTile clawLoc = claw.getLocation();
      final RSTile susLoc = suspect.getLocation();
      final ArrayList<Integer> options = new ArrayList<Integer>();
      if (susLoc.getX() > clawLoc.getX()) {
        options.add(Molly.CONTROLS_LEFT);
      }
      if (susLoc.getX() < clawLoc.getX()) {
        options.add(Molly.CONTROLS_RIGHT);
      }
      if (susLoc.getY() > clawLoc.getY()) {
        options.add(Molly.CONTROLS_DOWN);
      }
      if (susLoc.getY() < clawLoc.getY()) {
        options.add(Molly.CONTROLS_UP);
      }
      if (options.isEmpty()) {
        options.add(Molly.CONTROLS_GRAB);
      }
      final RSInterface i = methods.interfaces.get(Molly.CONTROL_INTERFACEGROUP);
      if (i != null && i.isValid()) {
        i.getComponent(options.get(random(0, options.size())))
            .doClick();
      }
      delayTime = System.currentTimeMillis();
      while (!hasClawMoved(clawLoc)
          && System.currentTimeMillis() - delayTime < 3500) {
        sleep(10);
      }
    }
  }

  private boolean hasClawMoved(final RSTile prevClawLoc) {
    final RSObject claw = methods.objects.getNearest(Molly.CLAW_ID);
    if (claw == null) {
      return false;
    }
    final RSTile currentClawLoc = claw.getLocation();
    return currentClawLoc.getX() - prevClawLoc.getX() != 0
        || currentClawLoc.getY() - prevClawLoc.getY() != 0;
  }

  private boolean openDoor() {
    final RSObject door = methods.objects.getNearest(Molly.DOOR_ID);
    if (door == null) {
      return false;
    }
    if (!methods.calc.tileOnScreen(door.getLocation())) {
      methods.walking.walkTileOnScreen(door.getLocation());
      sleep(1000, 2000);
      return false;
    }
    door.interact(om.Server.toString("examOpen"));
    return false;
  }

  private void setCamera() {
    if (random(0, 6) == 3 && !cameraSet) {
      methods.camera.setPitch(true);
      cameraSet = true;
    }
  }
}