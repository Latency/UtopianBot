package org.rsbot.script.wrappers;

import org.rsbot.gui.AccountManager;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.methods.Lobby;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;

public class RSWorld extends MethodProvider {
  private final int       world;
  private boolean         checked                            = false, isMembers = false;
  public static final int MEMBERS_TEXTURE                    = 1531;
  public static final int WORLD_SELECT_INTERFACE_WORLD_STARS = 70;

  private final OutputManager om = methods.locale;

  public RSWorld(final MethodContext ctx, final int world) {
    super(ctx);
    this.world = world;
  }

  public boolean isMembers() throws NullPointerException {
    if (!methods.lobby.inLobby()) {
      throw new NullPointerException(om.OS.toString("MustBeInLobby"));
    }
    if (!methods.interfaces.get(Lobby.WORLD_SELECT_INTERFACE).isValid()) {
      methods.lobby.open(Lobby.TAB_WORLD_SELECT);
      sleep(random(200, 500));
    }
    sleep(random(100, 200));
    if (!checked) {
      int index = -1;
      for (int i = 0; i < methods.interfaces.getComponent(Lobby.WORLD_SELECT_INTERFACE, Lobby.WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents().length; i++) {
        final RSComponent comp = methods.interfaces.getComponent(Lobby.WORLD_SELECT_INTERFACE, Lobby.WORLD_SELECT_INTERFACE_WORLD_NAME).getComponents()[i];
        if (comp != null) {
          final String number = comp.getText();
          if (Integer.parseInt(number) == world) {
            index = i;
            break;
          }
        }
      }
      if (index != -1) {
        try {
          final RSComponent star = methods.interfaces.getComponent(Lobby.WORLD_SELECT_INTERFACE, WORLD_SELECT_INTERFACE_WORLD_STARS).getComponents()[index];
          if (star.getBackgroundColor() == MEMBERS_TEXTURE)
            isMembers = true;
          checked = true;
        } catch (ArrayIndexOutOfBoundsException aioe) {
          return false;
        }
      }
    }
    return isMembers;
  }

  public boolean isValid() throws NullPointerException {
    return world != -1 && (AccountManager.isMember(methods.bot.getAccount()) || !isMembers()) && getPing() != -1;
  }

  public int getNumber() {
    return world;
  }
  
  public long getPing() {
    long finalpings = 0;
    for (int i = 0; i < 3; i++) { // Ping the world 3 times, otherwise ping may not be accurate.
      long ping = ping();
      if (ping == -1) {
        return -1;
      }
      finalpings += ping;
    }
    return finalpings / 3;
  }

  private long ping() {
    try {
      if (world == -1) {
        return -1;
      }
      final String host = "world" + Integer.toString(world) + ".runescape.com";
      java.net.SocketAddress addr = new java.net.InetSocketAddress(java.net.InetAddress.getByName(host), 80);
      java.net.Socket sock = new java.net.Socket();
      long start = System.currentTimeMillis();
      sock.connect(addr);
      boolean connect = sock.isConnected();
      sock.close();
      return !connect ? -1 : System.currentTimeMillis() - start;
    } catch (Exception e) {
      return -1;
    }
  }

}