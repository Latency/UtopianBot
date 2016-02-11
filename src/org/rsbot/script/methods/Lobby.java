package org.rsbot.script.methods;

import java.util.ArrayList;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;

/**
 * Methods for lobby interface
 * 
 * @Author Debauchery
 */

public class Lobby extends MethodProvider {
  private static final int  SELECTED_TEXTURE                         = 4671;

  public final static int   TAB_PLAYER_INFO                          = 0;
  public final static int   TAB_WORLD_SELECT                         = 1;
  public final static int   TAB_FRIENDS                              = 2;
  public final static int   TAB_FRIENDS_CHAT                         = 3;
  public final static int   TAB_CLAN_CHAT                            = 4;
  public final static int   TAB_OPTIONS                              = 5;

  public final static int   PLAYER_INFO_INTERFACE                    = 906;
  public final static int   PLAYER_INFO_INTERFACE_PLAY_BUTTON_1      = 160;
  public final static int   PLAYER_INFO_INTERFACE_PLAY_BUTTON_2      = 171;
  public final static int   LOGOUT_COMPONENT                         = 195;

  public final static int   WORLD_SELECT_INTERFACE                   = 910;
  public final static int   WORLD_SELECT_INTERFACE_CURRENT_WORLD     = 11;
  public final static int   WORLD_SELECT_INTERFACE_WORLD_LIST        = 77;
  public final static int   WORLD_SELECT_INTERFACE_WORLD_NAME        = 69;
  public final static int   WORLD_SELECT_INTERFACE_AMOUNT_OF_PLAYERS = 71;
  public final static int   WORLD_SELECT_INTERFACE_WORLD_ACTIVITY    = 72;
  public final static int   WORLD_SELECT_INTERFACE_WORLD_TYPE        = 74;
  public final static int   WORLD_SELECT_INTERFACE_WORLD_PING        = 76;
  public final static int   WORLD_SELECT_INTERFACE_SCROLL_AREA       = 86;
  public final static int   WORLD_SELECT_INTERFACE_SCROLL_BAR        = 1;

  public final static int   FRIENDS_INTERFACE                        = 909;

  public final static int   FRIENDS_CHAT_INTERFACE                   = 589;

  public final static int   CLAN_CHAT_INTERFACE                      = 912;

  public final static int   OPTIONS_INTERFACE                        = 978;
  public final static int[] TABS_TEXTURE                             = new int[] { 204, 12, 11, 254, 10, 9 };

  private final OutputManager om = methods.locale;

  public Lobby(final MethodContext ctx) {
    super(ctx);
  }
  
  /**
   * Checks that current game is in lobby.
   * 
   * @return <tt>true</tt> if the tab is opened.
   */
  public boolean inLobby() {
    return methods.game.getClientState() == Game.INDEX_LOBBY_SCREEN;
  }

  /**
   * Gets the currently open tab.
   * 
   * @return The currently open tab or the logout tab by default.
   */
  public int getCurrentTab() {
    if (!inLobby()) {
      return -1;
    }
    for (int i = 0; i < TABS_TEXTURE.length; i++) {
      if (methods.interfaces.getComponent(PLAYER_INFO_INTERFACE, TABS_TEXTURE[i]).getBackgroundColor() == SELECTED_TEXTURE) {
        return i;
      }
    }
    return 1;
  }

  /**
   * Opens the specified tab at the specified index.
   * 
   * @param i The tab to open.
   * @return <tt>true</tt> if tab successfully selected; otherwise <tt>false</tt>.
   */
  public boolean open(final int i) {
    if (inLobby()) {
      if (i == getCurrentTab())
        return true;
      methods.interfaces.getComponent(PLAYER_INFO_INTERFACE, TABS_TEXTURE[i]).doClick();
      sleep(random(400, 700));
    }
    return i == getCurrentTab();
  }

  /**
   * Finds out which world is selected from the lobby interface.
   * 
   * @return The world number that is currently selected
   */
  public int getSelectedWorld() {
    if (!inLobby()) {
      return -1;
    }
    if (!methods.interfaces.get(WORLD_SELECT_INTERFACE).isValid() || getCurrentTab() != TAB_WORLD_SELECT) {
      open(TAB_WORLD_SELECT);
    }
    if (methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_CURRENT_WORLD).isValid()) {
      final String worldText = methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_CURRENT_WORLD).getText().trim()
          .substring(methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_CURRENT_WORLD).getText().trim().indexOf(om.Server.toString("lobbyWorld") + " ") + 6);

      try {
        int i = Integer.parseInt(worldText);
        if (i != -1) {
          return i;
        }
      } catch (Exception e) {
        return -1;
      }
    }
    return -1;
  }

  /**
   * Finds all available worlds if in lobby.
   * 
   * @param includingFull If true it will include all full worlds when returned
   * @return All available worlds as a String array
   */
  public String[] getAvailableWorlds(final boolean includingFull) {
    final ArrayList<String> tempList = new ArrayList<String>();
    if (!inLobby()) {
      return new String[0];
    }
    if (!methods.interfaces.get(WORLD_SELECT_INTERFACE).isValid() || getCurrentTab() != TAB_WORLD_SELECT) {
      open(TAB_WORLD_SELECT);
      sleep(500);
    }
    for (int i = 0; i < methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents().length; i++) {
      final String amount = methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_AMOUNT_OF_PLAYERS).getComponents()[i].getText();
      final String number = methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents()[i].getText();
      if (!amount.contains(om.Server.toString("lobbyOFFLINE")) && !amount.contains("0")) {
        if (!includingFull) {
          if (!amount.contains(om.Server.toString("lobbyFULL"))) {
            tempList.add(number);
          }
        } else {
          tempList.add(number);
        }
      }
    }
    return tempList.toArray(new String[tempList.size()]);
  }

  /**
   * Checks if the chosen world is open.
   * 
   * @param world
   * @param includeFull
   * @return <tt>true</tt> is available, else <tt>false</tt>
   */
  public boolean isAvailable(final int world, final boolean includeFull) {
    for (final String s : getAvailableWorlds(includeFull)) {
      if (Integer.parseInt(s) == world) {
        return true;
      }
    }
    return false;
  }

  /**
   * Enters a world from the lobby.
   * 
   * @param world
   * @return <tt>true</tt> If correctly entered the world else <tt>false</tt>
   * @see org.rsbot.script.methods.Worlds hopTo(int world)
   */
  public boolean switchWorlds(final int world) {
    for (int i = 0; i < 3; i++) {
      if (!inLobby() || world == -1) {
        return false;
      }
      if (getCurrentTab() != TAB_WORLD_SELECT) {
        if (!open(TAB_WORLD_SELECT)) {
          sleep(random(600, 800));
          if (!open(TAB_WORLD_SELECT)) {
            return false;
          }
        }
        sleep(random(800, 12000));
      }
      if (getSelectedWorld() == world) {
        sleep(random(500, 700));
        if (getSelectedWorld() == world && clickPlay()) {
          return true;
        }
      }
      if (isAvailable(world, true)) {
        final RSComponent comp = getWorldComponent(world);
        if (comp != null && comp.isValid()) {
          methods.interfaces.scrollTo(comp, methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_SCROLL_AREA));
          sleep(random(300, 1000));
          comp.doClick(true);
          sleep(random(400, 600));
          if (!clickPlay()) {
            return false;
          }
          sleep(random(800, 1000));
          return true;
        }
      }
    }
    return false;
  }

  public boolean clickPlay() {
    methods.interfaces.getComponent(PLAYER_INFO_INTERFACE, PLAYER_INFO_INTERFACE_PLAY_BUTTON_1);
    if (getCurrentTab() == TAB_OPTIONS) {
      open(TAB_PLAYER_INFO);
      if (getCurrentTab() == TAB_OPTIONS) {
        return false;
      }
    }
    final RSInterface welcome_screen = methods.interfaces.get(PLAYER_INFO_INTERFACE);
    final RSComponent welcome_screen_button_play_1 = welcome_screen.getComponent(PLAYER_INFO_INTERFACE_PLAY_BUTTON_1);
    final RSComponent welcome_screen_button_play_2 = welcome_screen.getComponent(PLAYER_INFO_INTERFACE_PLAY_BUTTON_2);
    methods.mouse.click(welcome_screen_button_play_1.getAbsoluteX(), welcome_screen_button_play_1.getAbsoluteY(), welcome_screen_button_play_2.getAbsoluteX()
        + welcome_screen_button_play_2.getWidth() - welcome_screen_button_play_1.getAbsoluteX(), welcome_screen_button_play_1.getHeight(), true);
    return true;
  }

  /**
   * Gets the component of any world on the lobby interface
   * 
   * @param world
   * @return The component corresponding to the world.
   */
  public RSComponent getWorldComponent(final int world) {
    if (!inLobby()) {
      return null;
    }
    if (!methods.interfaces.get(WORLD_SELECT_INTERFACE).isValid()) {
      open(TAB_WORLD_SELECT);
    }
    for (int i = 0; i < methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents().length; i++) {
      final RSComponent comp = methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents()[i];
      if (comp != null) {
        final String number = comp.getText();
        if (Integer.parseInt(number) == world) {
          return methods.interfaces.getComponent(WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_WORLD_LIST).getComponents()[i];
        }
      }
    }
    return null;
  }

  /**
   * Used for logging out if in lobby
   * 
   * @return <tt>true</tt> if correctly logged out else false
   */
  public boolean logout() {
    if (inLobby()) {
      methods.interfaces.getComponent(PLAYER_INFO_INTERFACE, LOGOUT_COMPONENT).doClick();
    }
    return !methods.game.isLoggedIn();
  }

}