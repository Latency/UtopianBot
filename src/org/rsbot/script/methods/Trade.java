package org.rsbot.script.methods;

import java.util.ArrayList;
import java.util.List;

import org.rsbot.locale.OutputManager;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.wrappers.RSComponent;
import org.rsbot.script.wrappers.RSInterface;
import org.rsbot.script.wrappers.RSItem;
import org.rsbot.script.wrappers.RSPlayer;

/**
 * Trade handling.
 * 
 * @author Timer
 * @author kyleshay
 */
public class Trade extends MethodProvider {
  public static final int  INTERFACE_TRADE_MAIN           = 335;
  public static final int  INTERFACE_TRADE_SECOND         = 334;
  public static final int  INTERFACE_TRADE_MAIN_NAME      = 15;
  public static final int  INTERFACE_TRADE_SECOND_NAME    = 54;
  public static final int  INTERFACE_TRADE_MAIN_OUR       = 30;
  public static final int  INTERFACE_TRADE_MAIN_THEIR     = 33;
  public static final int  INTERFACE_TRADE_MAIN_ACCEPT    = 17;
  public static final int  INTERFACE_TRADE_MAIN_DECLINE   = 19;
  public static final int  INTERFACE_TRADE_SECOND_ACCEPT  = 36;
  public static final int  INTERFACE_TRADE_SECOND_DECLINE = 37;

  private final static int INTERFACE_TRADE_MAIN_INV_SLOTS = 21;

  public static final int  TRADE_TYPE_MAIN                = 0;
  public static final int  TRADE_TYPE_SECONDARY           = 1;
  public static final int  TRADE_TYPE_NONE                = 2;

  private final OutputManager om = methods.locale;

  public Trade(final MethodContext ctx) {
    super(ctx);
  }
  
  /**
   * Are we in the first stage of a trade?
   * 
   * @return <tt>true</tt> if in first stage.
   */
  public boolean inTradeMain() {
    final RSInterface tradeInterface = methods.interfaces.get(INTERFACE_TRADE_MAIN);
    return tradeInterface != null && tradeInterface.isValid();
  }

  /**
   * Are we in the second stage of a trade?
   * 
   * @return <tt>true</tt> if in second stage.
   */
  public boolean inTradeSecond() {
    final RSInterface tradeInterface = methods.interfaces.get(INTERFACE_TRADE_SECOND);
    return tradeInterface != null && tradeInterface.isValid();
  }

  /**
   * Checks if you're in a trade.
   * 
   * @return <tt>true</tt> if you're trading; otherwise <tt>false</tt>.
   */
  public boolean inTrade() {
    return inTradeMain() || inTradeSecond();
  }

  /**
   * Trades a player.
   * 
   * @param playerName The player's name.
   * @param tradeWait Timeout to wait for the trade.
   * @return <tt>true</tt> if traded.
   */
  public boolean tradePlayer(final String playerName, final int tradeWait) {
    if (!inTrade()) {
      final RSPlayer targetPlayer = methods.players.getNearest(playerName);
      if (targetPlayer != null) {
        return targetPlayer.interact(om.Server.toString("tradeTradeWith"), targetPlayer.getName()) && waitForTrade(TRADE_TYPE_MAIN, tradeWait);
      }
      return false;
    }
    return isTradingWith(playerName);
  }

  /**
   * Trades a player.
   * 
   * @param playerName The player's name.
   * @return <tt>true</tt> if traded.
   */
  public boolean tradePlayer(final String playerName) {
    return tradePlayer(playerName, 15000);
  }

  /**
   * Trades a player.
   * 
   * @param targetPlayer The player you wish to trade.
   * @param tradeWait The time out for the trade.
   * @return <tt>true</tt> if traded.
   */
  public boolean tradePlayer(final RSPlayer targetPlayer, final int tradeWait) {
    if (!inTrade()) {
      if (targetPlayer != null) {
        return targetPlayer.interact(om.Server.toString("tradeTradeWith"), targetPlayer.getName()) && waitForTrade(TRADE_TYPE_MAIN, tradeWait);
      }
      return false;
    }
    return isTradingWith(targetPlayer.getName());
  }

  /**
   * Trades a player.
   * 
   * @param targetPlayer The desired player.
   * @return <tt>true</tt> if traded.
   */
  public boolean tradePlayer(final RSPlayer targetPlayer) {
    return tradePlayer(targetPlayer, 15000);
  }

  /**
   * Accepts a trade
   * 
   * @return <tt>true</tt> on accept.
   */
  public boolean acceptTrade() {
    if (inTradeMain())
      return methods.interfaces.get(INTERFACE_TRADE_MAIN).getComponent(INTERFACE_TRADE_MAIN_ACCEPT).interact(om.Server.toString("tradeAccept"));
    return inTradeSecond() && methods.interfaces.get(INTERFACE_TRADE_SECOND).getComponent(INTERFACE_TRADE_SECOND_ACCEPT).interact(om.Server.toString("tradeAccept"));
  }

  /**
   * Declines a trade
   * 
   * @return <tt>true</tt> on decline
   */
  public boolean declineTrade() {
    if (inTradeMain())
      return methods.interfaces.get(INTERFACE_TRADE_MAIN).getComponent(INTERFACE_TRADE_MAIN_DECLINE).interact(om.Server.toString("tradeDecline"));
    return inTradeSecond() && methods.interfaces.get(INTERFACE_TRADE_SECOND).getComponent(INTERFACE_TRADE_SECOND_DECLINE).interact(om.Server.toString("tradeDecline"));
  }

  /**
   * Waits for trade type to be true.
   * 
   * @param tradeType The trade type.
   * @param timeOut Time out of waiting.
   * @return <tt>true</tt> if true, otherwise false.
   */
  public boolean waitForTrade(final int tradeType, final long timeOut) {
    final long timeCounter = System.currentTimeMillis() + timeOut;
    while (timeCounter - System.currentTimeMillis() > 0) {
      switch (tradeType) {
        case TRADE_TYPE_MAIN:
          if (inTradeMain()) {
            return true;
          }
          break;
        case TRADE_TYPE_SECONDARY:
          if (inTradeSecond()) {
            return true;
          }
          break;
        case TRADE_TYPE_NONE:
          if (!inTrade()) {
            return true;
          }
          break;
      }
      sleep(5);
    }
    return false;
  }

  /**
   * Gets who you're trading with.
   * 
   * @return The person's name you're trading with.
   */
  public String getTradingWith() {
    if (inTradeMain()) {
      final String name = methods.interfaces.getComponent(INTERFACE_TRADE_MAIN, INTERFACE_TRADE_MAIN_NAME).getText();
      return name.substring(name.indexOf(": ") + 2);
    } else if (inTradeSecond())
      return methods.interfaces.getComponent(INTERFACE_TRADE_SECOND, INTERFACE_TRADE_SECOND_NAME).getText();
    return null;
  }

  /**
   * Checks if you're trading with someone.
   * 
   * @param name The person's name.
   * @return <tt>true</tt> if true; otherwise <tt>false</tt>.
   */
  public boolean isTradingWith(final String name) {
    return getTradingWith().equals(name);
  }

  /**
   * Returns the total number of items offered by another player
   * 
   * @return The number of items offered.
   */
  public int getNumberOfItemsOffered() {
    int number = 0;
    for (int i = 0; i < 28; i++) {
      if (methods.interfaces.get(INTERFACE_TRADE_MAIN).getComponent(INTERFACE_TRADE_MAIN_THEIR).getComponent(i).getComponentStackSize() != 0) {
        ++number;
      }
    }
    return number;
  }

  /**
   * Returns the items offered by another player
   * 
   * @return The items offered.
   */
  public RSItem[] getItemsOffered() {
    List<RSItem> items = new ArrayList<RSItem>();
    for (int i = 0; i < 28; i++) {
      RSComponent component = methods.interfaces.get(INTERFACE_TRADE_MAIN).getComponent(INTERFACE_TRADE_MAIN_THEIR).getComponent(i);
      if (component != null && component.getComponentStackSize() != 0) {
        items.add(new RSItem(methods, component));
      }
    }
    return items.toArray(new RSItem[items.size()]);
  }

  /**
   * Returns the total number of free slots the other player has
   * 
   * @return The number of free slots.
   */
  public int getFreeSlots() {
    if (inTradeMain()) {
      String text = methods.interfaces.get(INTERFACE_TRADE_MAIN).getComponent(INTERFACE_TRADE_MAIN_INV_SLOTS).getText().substring(4, 6);
      text = text.trim();
      try {
        return Integer.parseInt(text);
      } catch (final Exception e) {}
    }
    return 0;
  }
}
