package org.rsbot.script.methods;

import java.util.ArrayList;

import org.rsbot.client.ServerData;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.methods.Game.Tab;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSWorld;

public class Worlds extends MethodProvider {

  public static final int INTERFACE_LOBBY       = 906;
  public static final int INTERFACE_WORLDS      = 910;
  public static final int COMPONENT_WORLDSELECT = 12;

  private final OutputManager om = methods.locale;

  public Worlds(final MethodContext ctx) {
    super(ctx);
  }
  
  /**
   * Gets an RSWorld for the specified number.
   * 
   * @param worldNum The RSWorld's world number.
   * @return An RSWorld for the specified number.
   */
  public RSWorld getWorld(final int worldNum) {
    RSWorld world = new RSWorld(methods, worldNum);
    return world.getNumber() != -1 ? world : null;
  }

  /**
   * Gets every valid RSWorld. (F2P and P2P). This may take a couple seconds.
   * 
   * @return An array of every valid RSWorld.
   * @throws NullPointerException If you are not inside the lobby.
   */
  public RSWorld[] getAllValidWorlds() throws NullPointerException {
    return getAllValidWorlds(true, false);
  }

  /**
   * Gets every valid RSWorld. This may take a couple seconds.
   * 
   * @param members Whether or not to include Runescape Members worlds.
   * @param only To exclude F2P worlds (if members was chosen).
   * @return An array of all the valid RSWorlds.
   * @throws NullPointerException If you are not inside the lobby.
   */
  public RSWorld[] getAllValidWorlds(final boolean members, final boolean only) throws NullPointerException {
    if (!methods.lobby.inLobby())
      throw new NullPointerException(om.OS.toString("MustBeInLobby"));
    if (!methods.interfaces.get(Lobby.WORLD_SELECT_INTERFACE).isValid() || methods.lobby.getCurrentTab() != Lobby.TAB_WORLD_SELECT) {
      methods.lobby.open(Lobby.TAB_WORLD_SELECT);
      sleep(500);
    }
    ArrayList<RSWorld> worlds = new ArrayList<RSWorld>();
    for (int i = 0; i < methods.interfaces.getComponent(Lobby.WORLD_SELECT_INTERFACE, Lobby.WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents().length; i++) {
      final String number = methods.interfaces.getComponent(Lobby.WORLD_SELECT_INTERFACE, Lobby.WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents()[i].getText();
      final int worldNum = Integer.parseInt(number);
      if (worldNum != -1) {
        RSWorld world = getWorld(worldNum);
        if (world != null) {
          boolean mems = world.isMembers();
          if ((!members && mems) || (members && !mems && only)) {
            continue;
          }
          worlds.add(world);
        }
      }
    }
    return worlds.toArray(new RSWorld[worlds.size()]);
  }

  /**
   * Gets the current world that the player is logged into, or the selected world if the player is in the lobby.
   * 
   * @return The current world that the player is logged into.
   */
  public RSWorld getCurrentWorld() {
    if (methods.game.isLoggedIn()) {
      ServerData worldData = methods.client.getWorldData();
      if (worldData != null) {
        int worldNum = worldData.getWorldID();
        if (worldNum != -1) {
          return getWorld(worldNum);
        }
      }
      // If hooks for ServerData FAILS, resort to interfaces
      if (methods.game.getTab() != Tab.FRIENDS) {
        methods.game.openTab(Tab.FRIENDS);
        for (int i = 0; i < 25 && methods.game.getTab() != Tab.FRIENDS; i++) {
          sleep(random(70, 80));
        }
      }
      int world = -1;
      try {
        world = Integer.parseInt(methods.interfaces.getComponent(550, 19).getText().replaceAll(om.Server.toString("worldsFriendsList") + "<br>RuneScape ", ""));
      } catch (Exception ignored) {
      }
      return getWorld(world);
    } else if (methods.lobby.inLobby()) {
      return getWorld(methods.lobby.getSelectedWorld());
    }
    return null;
  }

  /**
   * Hops to a world chosen by given worlds numbers.
   * 
   * @param lowestPing To choose the world to hop to based on its ping.
   * @param worlds The world number(s) to choose from.
   * @return <tt>true</tt> if the hop was successful, otherwise <tt>false</tt>.
   */
  public boolean hopTo(final boolean lowestPing, final int[] worlds) {
    ArrayList<RSWorld> worldList = new ArrayList<RSWorld>();
    for (final int i : worlds) {
      if (i != -1) {
        RSWorld world = getWorld(i);
        if (world != null) {
          worldList.add(world);
        }
      }
    }
    return hopTo(lowestPing, worldList.toArray(new RSWorld[worldList.size()]));
  }

  /**
   * Hops to a world chosen by given worlds.
   * 
   * @param lowestPing To choose the world to hop to based on its ping.
   * @param worlds The world(s) to choose from.
   * @return <tt>true</tt> if the hop was successful, otherwise <tt>false</tt>.
   */
  public boolean hopTo(final boolean lowestPing, final RSWorld[] worlds) {
    for (int j = 0; j < 3; j++) {
      if (lowestPing) {
        long lowest = Integer.MAX_VALUE;
        RSWorld lowestWorld = null;
        for (final RSWorld currWorld : worlds) {
          long ping = currWorld.getPing();
          if (ping != -1 && ping < lowest) {
            lowest = ping;
            lowestWorld = currWorld;
          }
        }
        if (lowestWorld == null) {
          return false;
        } else if (hopTo(lowestWorld)) {
          return true;
        }
      } else {
        RSWorld choose = worlds[random(0, worlds.length)];
        if (choose != null && hopTo(choose)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Hops to a chosen world based on its number.
   * 
   * @param world The world number.
   * @return <tt>true</tt> if the hop was successful, otherwise <tt>false</tt>.
   */
  public boolean hopTo(final int world) {
    RSWorld rsworld = getWorld(world);
    return rsworld != null && hopTo(rsworld);
  }

  /**
   * Hops to a chosen world based on its number.
   * 
   * @param world The world.
   * @return <tt>true</tt> if the hop was successful, otherwise <tt>false</tt>.
   */
  public boolean hopTo(final RSWorld world) {
    methods.env.disableRandom(om.Server.toString("gameLogin"));
    if (methods.game.isLoggedIn()) {
      if (methods.game.logout(true)) {
        for (int i = 0; i < 60; i++) {
          sleep(random(95, 110));
          if (methods.interfaces.get(INTERFACE_LOBBY).isValid() && methods.game.getClientState() <= Game.INDEX_LOBBY_SCREEN) {
            break;
          }
        }
      }
    }
    sleep(random(100, 600));
    if (!methods.interfaces.get(INTERFACE_LOBBY).isValid()) {
      methods.env.enableRandom(om.Server.toString("gameLogin"));
      return false;
    }
    if (!methods.interfaces.get(INTERFACE_WORLDS).isValid()) {
      RSComponent worldComp = methods.interfaces.getComponent(INTERFACE_LOBBY, COMPONENT_WORLDSELECT);
      if (!worldComp.doClick(true)) {
        methods.env.enableRandom(om.Server.toString("gameLogin"));
        return false;
      }
      sleep(random(900, 1200));
    }
    sleep(random(100, 600));
    try {
      if (!world.isValid()) {
        return false;
      }
    } catch (Exception e) {
      return false;
    }
    if (methods.lobby.switchWorlds(world.getNumber())) {
      sleep(random(1200, 2500));
      methods.env.enableRandom(om.Server.toString("gameLogin"));
      return true;
    }
    methods.env.enableRandom(om.Server.toString("gameLogin"));
    return false;
  }
}