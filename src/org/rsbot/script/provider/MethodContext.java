package org.rsbot.script.provider;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.rsbot.bot.Bot;
import org.rsbot.client.Client;
import org.rsbot.locale.OutputManager;
import org.rsbot.script.internal.InputManager;
import org.rsbot.script.internal.reflection.Reflection;
import org.rsbot.script.methods.Account;
import org.rsbot.script.methods.AntiIdle;
import org.rsbot.script.methods.Areas;
import org.rsbot.script.methods.Bank;
import org.rsbot.script.methods.Calculations;
import org.rsbot.script.methods.Camera;
import org.rsbot.script.methods.ClanChat;
import org.rsbot.script.methods.Combat;
import org.rsbot.script.methods.Environment;
import org.rsbot.script.methods.Equipment;
import org.rsbot.script.methods.FriendChat;
import org.rsbot.script.methods.Game;
import org.rsbot.script.methods.GameGUI;
import org.rsbot.script.methods.GrandExchange;
import org.rsbot.script.methods.GroundItems;
import org.rsbot.script.methods.HiScores;
import org.rsbot.script.methods.Interfaces;
import org.rsbot.script.methods.Inventory;
import org.rsbot.script.methods.Keyboard;
import org.rsbot.script.methods.Lobby;
import org.rsbot.script.methods.Magic;
import org.rsbot.script.methods.Menu;
import org.rsbot.script.methods.Mouse;
import org.rsbot.script.methods.NPCs;
import org.rsbot.script.methods.Nodes;
import org.rsbot.script.methods.Objects;
import org.rsbot.script.methods.Players;
import org.rsbot.script.methods.Prayer;
import org.rsbot.script.methods.Quests;
import org.rsbot.script.methods.Settings;
import org.rsbot.script.methods.Skills;
import org.rsbot.script.methods.Store;
import org.rsbot.script.methods.Summoning;
import org.rsbot.script.methods.Tiles;
import org.rsbot.script.methods.Trade;
import org.rsbot.script.methods.Walking;
import org.rsbot.script.methods.Worlds;

/**
 * For internal use to link MethodProviders.
 * 
 * @author Jacmob
 */
public class MethodContext {
  /**
   * The instance of {@link java.util.Random} for random number generation.
   */
  public final java.util.Random random        = new java.util.Random();

  /**
   * The AntiIdle
   */
  public final AntiIdle         antiIdle = new AntiIdle(this);

  /**
   * The singleton of areas
   */
  public final Areas            areas         = new Areas(this);

  /**
   * The singleton of skills
   */
  public final Skills           skills;

  /**
   * The singleton of Settings
   */
  public final Settings         settings      = new Settings(this);

  /**
   * The singleton of magic
   */
  public final Magic            magic         = new Magic(this);

  /**
   * The singleton of bank
   */
  public final Bank             bank;

  /**
   * The singleton of players
   */
  public final Players          players       = new Players(this);

  /**
   * The singleton of store
   */
  public final Store            store         = new Store(this);

  /**
   * The singleton of Grand Exchange
   */
  public final GrandExchange    grandExchange = new GrandExchange(this);

  /**
   * The singletion of Hiscores
   */
  public final HiScores         hiscores      = new HiScores(this);

  /**
   * The singleton of ClanChat
   */
  public final ClanChat         clanChat      = new ClanChat(this);

  /**
   * The singleton of Camera
   */
  public final Camera           camera        = new Camera(this);

  /**
   * The singleton of NPCs
   */
  public final NPCs             npcs          = new NPCs(this);

  /**
   * The singleton of GameScreen
   */
  public final Game             game          = new Game(this);

  /**
   * The singleton of Combat
   */
  public final Combat           combat        = new Combat(this);

  /**
   * The singleton of Interfaces
   */
  public final Interfaces       interfaces    = new Interfaces(this);

  /**
   * The singleton of Mouse
   */
  public final Mouse            mouse         = new Mouse(this);

  /**
   * The singleton of Keyboard
   */
  public final Keyboard         keyboard      = new Keyboard(this);

  /**
   * The singleton of Menu
   */
  public final Menu             menu          = new Menu(this);

  /**
   * The singleton of Tile
   */
  public final Tiles            tiles         = new Tiles(this);

  /**
   * The singleton of Objects
   */
  public final Objects          objects       = new Objects(this);

  /**
   * The singleton of Walking
   */
  public final Walking          walking       = new Walking(this);

  /**
   * The singleton of Calculations
   */
  public final Calculations     calc          = new Calculations(this);

  /**
   * The singleton of Inventory
   */
  public final Inventory        inventory     = new Inventory(this);

  /**
   * The singleton of Equipment
   */
  public final Equipment        equipment     = new Equipment(this);

  /**
   * The singleton of GroundItems
   */
  public final GroundItems      groundItems   = new GroundItems(this);

  /**
   * The singleton of DynamicConstants
   */
  public final GameGUI          gui           = new GameGUI(this);

  /**
   * The singleton of Nodes
   */
  public final Nodes            nodes         = new Nodes(this);

  /**
   * the singleton of Account
   */
  public final Account          account       = new Account(this);

  /**
   * The singleton of Summoning
   */
  public final Summoning        summoning     = new Summoning(this);
  
  /**
   * The singleton of Environment
   */
  public final Environment      env           = new Environment(this);

  /**
   * The singleton of Prayer
   */
  public final Prayer           prayer        = new Prayer(this);

  /**
   * The singleton of Quests
   */
  public final Quests           quests        = new Quests(this);

  /**
   * The singleton of Prayer
   */
  public final FriendChat       friendChat    = new FriendChat(this);

  /**
   * The singleton of Trade
   */
  public final Trade            trade         = new Trade(this);

  /**
   * The singleton of Lobby
   */
  public final Lobby            lobby         = new Lobby(this);

  /**
   * The Worlds
   */
  public final Worlds           worlds        = new Worlds(this);
  
  /**
   * The Bot's input manager
   */
  public final InputManager     inputManager;

  /**
   * The language parsing message output manager
   */
  public final OutputManager    locale;

  /**
   * The client
   */
  public final Client           client;

  /**
   * Reflection providers.
   */
  public final Reflection       reflection;

  /**
   * The ExecutorService
   */
  public final ExecutorService  service;

  /**
   * The Bot session.
   */
  public final Bot              bot;

  // ///////////////////////////////////////////////////////////////////

  public MethodContext(final Bot bot) {
    this.bot = bot;
    this.client = bot.getClient();
    this.locale = bot.getOutputManager();
    this.inputManager = bot.getInputManager();
    this.reflection = new Reflection(bot);
    this.bank = new Bank(this);
    this.skills = new Skills(this);
    service = Executors.newCachedThreadPool();
  }
}