package org.rsbot.script.randoms;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import org.rsbot.gui.AccountManager;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.Random;
import org.rsbot.script.ScriptManifest;
import org.rsbot.script.methods.Lobby;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;

/**
 * @author Iscream
 * @author Pervy
 * @author Timer
 * @author icnhzabot
 */
@ScriptManifest(authors = { "Iscream", "Pervy Shuya", "Timer", "icnhzabot" }, name = "Login", version = 2.2)
public class LoginBot extends Random {
  public LoginBot(MethodContext ctx) {
    super(ctx);
  }

  private final OutputManager om                                = methods.locale;

  private static final int    INTERFACE_MAIN                    = 905;
  private static final int    INTERFACE_MAIN_CHILD              = 59;
  private static final int    INTERFACE_MAIN_CHILD_COMPONENT_ID = 4;

  private static final int    INTERFACE_WELCOME                 = 906;
  private static final int    USERINFO_TAB                      = 204;
  private static final int    WELCOME_RETURN_TEXT               = 224;
  private static final int    BACK_BUTTON                       = 234;
  private static final int    HIGHRISK_WORLD_RETURN_TEXT        = 87;
  private static final int    HIGHRISK_WORLD_GOBACK             = 92;
  private static final int    MEMBERS_GOBACK                    = 231;

  private static final int    INTERFACE_LOGIN                   = 596;
  private static final int    LOGIN_RETURN_TEXT                 = 14;
  private static final int    LOGIN_BACK_BUTTON                 = 66;
  private static final int    USERNAME_CLICKAREA                = 40;
  private static final int    USERNAME                          = 73;
  private static final int    PASSWORD_CLICKAREA                = 42;
  private static final int    PASSWORD                          = 79;
  private static final int    LOGIN_BUTTON                      = 57;

  private static final int    INTERFACE_GRAPHICS_NOTICE         = 976;
  private static final int    INTERFACE_GRAPHICS_LEAVE_ALONE    = 6;
  private static final int    INDEX_LOGGED_OUT                  = 3;
  private static final int    INDEX_LOBBY                       = 7;
  private int                 invalidCount, worldFullCount, mouseStartSpeed = 5;

  /*
   * Just some relatively low population worlds, so it doesn't take a minute for world hopping private static final int[] F2P_WORLDS = {49, 108, 120, 123, 165, 19, 50, 153, 161}; private static final int[] MEMBERS_WORLDS = {112, 12, 15, 138, 96, 137, 131, 121, 56};
   */

  @Override
  public void onStart() {
    invalidCount = worldFullCount = 0;
    mouseStartSpeed = methods.mouse.getSpeed();
    methods.mouse.setSpeed(random(9, 11));
  }

  @Override
  public void onFinish() {
    methods.mouse.setSpeed(mouseStartSpeed);
  }

  @Override
  public boolean activateCondition() {
    final int idx = methods.game.getClientState();
    return !methods.bot.disableAutoLogin && idx <= INDEX_LOBBY && idx >= INDEX_LOGGED_OUT && !isWorldHopping() && methods.account.getName() != null;
  }

  @Override
  public int loop() {
    methods.mouse.setSpeed(random(9, 11));
    if (methods.game.getClientState() != INDEX_LOGGED_OUT) {
      if (!methods.game.isWelcomeScreen()) {
        sleep(random(1000, 2000));
      }
      final RSInterface welcome = methods.interfaces.get(INTERFACE_WELCOME);
      if (methods.game.getClientState() == INDEX_LOBBY && welcome.isValid()) {
        final RSComponent tab = welcome.getComponent(USERINFO_TAB);
        if (methods.lobby.getCurrentTab() == Lobby.TAB_OPTIONS && tab.isValid()) {
          methods.lobby.open(Lobby.TAB_PLAYER_INFO);
        }
        if (!methods.lobby.clickPlay()) {
          return -1;
        }
        for (int i = 0; i < 4 && methods.game.getClientState() == 6; i++) {
          sleep(random(450, 530));
        }
        final String logintext = welcome.getComponent(WELCOME_RETURN_TEXT).getText().toLowerCase().trim();
        if (logintext.contains(om.Server.toString("loginTotal"))) {
          log(om.OS.toString("LogBackIn") + " " + (AccountManager.isMember(methods.account.getName()) ? "1500+" : "1000+"));
          welcome.getComponent(BACK_BUTTON).doClick(true);
          stopScript(false);
        } else if (logintext.contains(om.Server.toString("loginExceed"))) {
          if (welcome.getComponent(BACK_BUTTON).isValid()) {
            welcome.getComponent(BACK_BUTTON).doClick(true);
          }
          if (invalidCount >= 7) {
            log.severe(om.OS.toString("loginUnable"));
            stopScript(false);
          }
          invalidCount++;
        } else if (logintext.contains(om.Server.toString("loginNotOut"))) {
          if (welcome.getComponent(BACK_BUTTON).isValid()) {
            welcome.getComponent(BACK_BUTTON).doClick(true);
          }
          if (invalidCount >= 7) {
            log.severe(om.OS.toString("loginUnable"));
            stopScript(false);
          }
          invalidCount++;
        } else if (logintext.contains(om.Server.toString("loginMember"))) {
          log.severe(om.OS.toString("loginUnableMembers"));
          final RSComponent back1 = welcome.getComponent(MEMBERS_GOBACK);
          final RSComponent back2 = welcome.getComponent(BACK_BUTTON);
          methods.mouse.click(back1.getAbsoluteX(), back1.getAbsoluteY(), back2.getAbsoluteX() + back2.getWidth() - back1.getAbsoluteX(), back1.getHeight(), true);
          stopScript(false);
        } else if (welcome.getComponent(HIGHRISK_WORLD_RETURN_TEXT).isValid()
            && welcome.getComponent(HIGHRISK_WORLD_RETURN_TEXT).getText().toLowerCase().trim().contains(om.Server.toString("loginWilderness"))) {
          if (welcome.getComponent(HIGHRISK_WORLD_GOBACK).isValid()) {
            welcome.getComponent(HIGHRISK_WORLD_GOBACK).doClick();
            sleep(random(300, 700));
          }
          log.severe(om.OS.toString("loginUnableHighrisk"));
          stopScript(false);
        } else if (logintext.contains(om.Server.toString("loginFull"))) {
          if (welcome.getComponent(BACK_BUTTON).isValid()) {
            welcome.getComponent(BACK_BUTTON).doClick(true);
            sleep(random(300, 700));
          }
          if (worldFullCount >= 7) {
            log(om.OS.toString("loginWorldFull"));
            stopScript(false);
          } else {
            worldFullCount++;
          }
        }
      }
    }
    final RSInterface login = methods.interfaces.get(INTERFACE_LOGIN);
    if (!methods.game.isLoggedIn() && login.isValid()) {
      final String logintext = login.getComponent(LOGIN_RETURN_TEXT).getText().toLowerCase().trim();
      if (login.getComponent(LOGIN_BACK_BUTTON).isValid()) {
        login.getComponent(LOGIN_BACK_BUTTON).doClick(true);
      }
      if (logintext.contains(om.Server.toString("loginNoReply"))) {
        if (invalidCount >= 7) {
          log.severe(om.OS.toString("loginNoReply"));
          stopScript(false);
        }
        invalidCount++;
      } else if (logintext.contains(om.Server.toString("loginUpdate"))) {
        log.severe(om.OS.toString("loginUpdated"));
        stopScript(false);
      } else if (logintext.contains(om.Server.toString("loginNotLoggedOut"))) {
        if (invalidCount >= 7) {
          log.severe(om.OS.toString("loginAlreadyLogged"));
          stopScript(false);
        }
        invalidCount++;
      } else if (logintext.contains(om.Server.toString("loginDisable"))) {
        log.severe(om.OS.toString("loginBanned"));
        stopScript(false);
      } else if (logintext.contains(om.Server.toString("loginInvalid")) || logintext.contains(om.Server.toString("loginIncorrect"))) {
        if (invalidCount >= 7) {
          log.severe(om.OS.toString("loginStuck"));
          stopScript(false);
        }
        invalidCount++;
      } else if (logintext.contains(om.Server.toString("loginError"))) {
        if (invalidCount >= 7) {
          log.severe(om.OS.toString("loginConnectError"));
          stopScript(false);
        }
        invalidCount++;
      } else if (logintext.contains(om.Server.toString("loginExceed"))) {
        if (invalidCount >= 7) {
          log.severe(om.OS.toString("loginLimitExceed"));
          stopScript(false);
        }
      }
    }
    if (methods.game.getClientState() == INDEX_LOGGED_OUT) {
      if (methods.interfaces.getComponent(INTERFACE_GRAPHICS_NOTICE, INTERFACE_GRAPHICS_LEAVE_ALONE).isValid()) {
        methods.interfaces.getComponent(INTERFACE_GRAPHICS_NOTICE, INTERFACE_GRAPHICS_LEAVE_ALONE).interact("");
        sleep(random(300, 500));
        if (methods.interfaces.getComponent(LOGIN_BACK_BUTTON).isValid()) {
          methods.interfaces.getComponent(LOGIN_BACK_BUTTON).doClick(true);
        }
        return random(500, 600);
      }
      final RSInterface interface_login = methods.interfaces.get(INTERFACE_LOGIN);
      if (!interface_login.isValid()) {
        methods.interfaces.getComponent(INTERFACE_MAIN, INTERFACE_MAIN_CHILD).getComponent(INTERFACE_MAIN_CHILD_COMPONENT_ID).interact("");
        return random(500, 600);
      }
      if (isUsernameFilled() && isPasswordFilled()) {
        if (random(0, 2) == 0) {
          methods.keyboard.pressKey((char) KeyEvent.VK_ENTER);
        } else {
          methods.interfaces.getComponent(INTERFACE_LOGIN, LOGIN_BUTTON).doClick(true);
        }
      }
      if (!isUsernameFilled()) {
        atLoginInterface(methods.interfaces.get(INTERFACE_LOGIN).getComponent(USERNAME_CLICKAREA));
        sleep(random(500, 700));
        int times = methods.interfaces.get(INTERFACE_LOGIN).getComponent(USERNAME).getText().trim().length();
        for (int i = 0; i <= times + random(1, 5); i++) {
          methods.keyboard.sendText("\b", false);
          if (random(0, 2) == 0) {
            sleep(random(25, 110));
          }
        }
        if (methods.interfaces.get(INTERFACE_LOGIN).getComponent(USERNAME).getText().trim().length() == 0) {
          sleep(random(200, 700));
          methods.keyboard.sendText(methods.account.getName().toLowerCase().trim(), false);
        }
      }
      if (isUsernameFilled() && !isPasswordFilled()) {
        atLoginInterface(methods.interfaces.get(INTERFACE_LOGIN).getComponent(PASSWORD_CLICKAREA));
        sleep(random(500, 700));
        int times = methods.interfaces.get(INTERFACE_LOGIN).getComponent(PASSWORD).getText().trim().length();
        for (int i = 0; i <= times + random(1, 5); i++) {
          methods.keyboard.sendText("\b", false);
          if (random(0, 2) == 0) {
            sleep(random(25, 110));
          }
        }
        if (methods.interfaces.getComponent(INTERFACE_LOGIN, PASSWORD).getText().trim().length() == 0) {
          sleep(random(200, 700));
          methods.keyboard.sendText(methods.account.getPassword().trim(), false);
        }
      }
    }
    return activateCondition() ? random(950, 1500) : -1;
  }

  private boolean isUsernameFilled() {
    final String username = methods.account.getName().toLowerCase().trim();
    return methods.interfaces.get(INTERFACE_LOGIN).getComponent(USERNAME).getText().toLowerCase().equalsIgnoreCase(username);
  }

  private boolean isPasswordFilled() {
    final String passWord = AccountManager.getPassword(methods.account.getName()).trim();
    return methods.interfaces.get(INTERFACE_LOGIN).getComponent(PASSWORD).getText().toLowerCase().length() == (passWord == null ? 0 : passWord.length());
  }

  private int minX(final RSComponent a) {
    int x = 0;
    final Rectangle pos = a.getArea();
    final int dx = (int) (pos.getWidth() - 4) / 2;
    final int midx = (int) (pos.getMinX() + pos.getWidth() / 2);
    if (pos.x == -1 || pos.y == -1 || pos.width == -1 || pos.height == -1) {
      return 0;
    }
    for (int i = 0; i < methods.interfaces.get(INTERFACE_LOGIN).getComponent(PASSWORD).getText().length(); i++) {
      x += 11;
    }
    if (x > 44)
      return (int) (pos.getMinX() + x + 15);
    return midx + random(-dx, dx);
  }

  private boolean atLoginInterface(final RSComponent i) {
    if (!i.isValid()) {
      return false;
    }
    final Rectangle pos = i.getArea();
    if (pos.x == -1 || pos.y == -1 || pos.width == -1 || pos.height == -1) {
      return false;
    }
    final int dy = (int) (pos.getHeight() - 4) / 2;
    final int maxRandomX = (int) (pos.getMaxX() - pos.getCenterX());
    final int midx = (int) pos.getCenterX();
    final int midy = (int) (pos.getMinY() + pos.getHeight() / 2);
    if (i.getIndex() == PASSWORD_CLICKAREA) {
      methods.mouse.click(minX(i), midy + random(-dy, dy), true);
    } else {
      methods.mouse.click(midx + random(1, maxRandomX), midy + random(-dy, dy), true);
    }
    return true;
  }

  private boolean isWorldHopping() {
    return methods.interfaces.getComponent(INTERFACE_WELCOME, WELCOME_RETURN_TEXT).isValid()
        && methods.interfaces.getComponent(INTERFACE_WELCOME, WELCOME_RETURN_TEXT).getText().toLowerCase().trim().contains("just left another world");
  }
}