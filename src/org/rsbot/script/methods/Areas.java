package org.rsbot.script.methods;

import static org.rsbot.constants.Areas.Area_BlackArmGangEntrance;
import static org.rsbot.constants.Areas.Area_Fally;
import static org.rsbot.constants.Areas.Area_Guild;
import static org.rsbot.constants.Areas.Area_GuildStairsBottom;
import static org.rsbot.constants.Areas.Area_GuildStairsTop;
import static org.rsbot.constants.Areas.Area_PhoenixGangEntrance;
import static org.rsbot.constants.Areas.bankArea_EastVarrock;
import static org.rsbot.constants.Areas.bankArea_WestVarrock;

import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.util.Filter;
import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSNPC;
import org.rsbot.script.wrappers.RSObject;
import org.rsbot.script.wrappers.RSTile;

public class Areas extends MethodProvider {
  public Areas(final MethodContext ctx) {
    super(ctx);
  }

  public final RSArea getAreaNPC(final int id) {
    final RSNPC npc = methods.npcs.getNearest(id);
    if (npc == null)
      return null;
    final RSTile tile = npc.getLocation();
    return new RSArea(tile.getX() - 3, tile.getY() - 3, tile.getX() + 3, tile.getY() + 3);
  }

  public final RSArea getAreaObject(final int id) {
    Filter<RSObject> objFilter = new Filter<RSObject>() {
      @Override
      public boolean accept(RSObject t) {
        return (t != null) && (id == t.getID());
      }
    };
    final RSObject[] obj = methods.objects.getAll(objFilter);
    if ((obj == null) || (obj[0] == null))
      return null;
    RSTile tile = obj[0].getLocation();
    return new RSArea(tile.getX() - 3, tile.getY() - 3, tile.getX() + 3, tile.getY() + 3);
  }

  public static final RSArea getAreaTile(final RSTile tile, int dev) {
    return new RSArea(tile.getX() - dev, tile.getY() - dev, tile.getX() + dev, tile.getY() + dev);
  }

  // ////////////////////////////////////////////////
  // //////// Areas /////////////////////////////////
  // ////////////////////////////////////////////////

  public final static boolean atGuildBank() {
    return false;
    // return (bankArea_WestFalador.contains(getMyPlayer().getLocation()));
  }

  public final boolean aboveGuildStairs() {
    return (Area_GuildStairsTop.contains(methods.players.getMyPlayer().getLocation()));
  }

  public final boolean underGuildStairs() {
    return (Area_GuildStairsBottom.contains(methods.players.getMyPlayer().getLocation()));
  }

  public final boolean inGuild() {
    return (Area_Guild.contains(methods.players.getMyPlayer().getLocation()));
  }

  public final boolean atPhoenixGangHideout() {
    return (Area_PhoenixGangEntrance.contains(methods.players.getMyPlayer().getLocation()));
  }

  public final boolean atBlackArmGangHideout() {
    return (Area_BlackArmGangEntrance.contains(methods.players.getMyPlayer().getLocation()));
  }

  public final boolean atFally() {
    return (Area_Fally.contains(methods.players.getMyPlayer().getLocation()));
  }

  public final boolean inVarrock() {
    RSArea VarrockBottom = new RSArea(3182, 3382, 3263, 3407), VarrockEast = new RSArea(3174, 3399, 3182, 3431), VarrockMiddle = new RSArea(3196, 3408, 3272, 3435), VarrockAltar = new RSArea(3253, 3380, 3263, 3395), // Top
                                                                                                                                                                                                                        // 1
    VarrockNGate = new RSArea(3235, 3462, 3250, 3500), // Top 2
    VarrockNGate2 = new RSArea(3250, 3462, 3261, 3492), // Top 3
    area = VarrockBottom.union(VarrockEast).union(VarrockMiddle).union(VarrockAltar).union(VarrockNGate).union(VarrockNGate2);
    return (area.contains(methods.players.getMyPlayer().getLocation()) || atPhoenixGangHideout());
  }

  // //////////////////////////
  // ////////ADD BANKS HERE////
  // //////////////////////////
  public static boolean atBank() {
    return (bankArea_WestVarrock.contains() || bankArea_EastVarrock.contains() /* || banks.???? */);
  }

  // /////////////////////////
  // ////////AZURRE'S SHOP////
  // /////////////////////////
  public final boolean inAzurresShop() {
    RSArea ShopX1 = new RSArea(3250, 3401, 3255, 3401), // Left to Right = Make Cross
    ShopX2 = new RSArea(3252, 3399, 3253, 3404), // Bottom to Top = Make Cross
    area;
    RSTile Extra = new RSTile(3254, 3402); // Extra tile in room.

    area = ShopX1.union(ShopX2).union(Extra);
    return area.contains(methods.players.getMyPlayer().getLocation());
  }

  public final boolean atAzurresShop() {
    RSArea ShopX1 = new RSArea(3248, 3396, 3259, 3398), // bottom
    ShopX2 = new RSArea(3254, 3399, 3259, 3400), // east
    ShopX3 = new RSArea(3248, 3399, 3250, 3402), // west
    area;
    RSTile Extra = new RSTile(3250, 3399); // west corner tile

    area = ShopX1.union(ShopX2).union(ShopX3).union(Extra);
    return area.contains(methods.players.getMyPlayer().getLocation());
  }
}