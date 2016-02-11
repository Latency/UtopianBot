package org.rsbot.constants;

import org.rsbot.script.wrappers.RSArea;
import org.rsbot.script.wrappers.RSTile;

public enum Rune {
  // (Rune ID, Talisman ID, Tiara ID, Altar ID, Portal ID, Ruins ID, Ruins Tile, Altar Area)
  FIRE   ( 554, 1442, 5337, 2482, 2469, 2456, new RSTile(-1, -1),     new RSArea(2574, 4830, 2594, 4851)),
  WATER  ( 555, 1444, 5331, 2480, 2467, 2454, new RSTile(-1, -1),     new RSArea(3474, 4822, 3499, 4845)),
  AIR    ( 556, 1438, 5527, 2478, 2465, 2452, new RSTile(3127, 3405), new RSArea(2837, 4826, 2850, 4841)),
  EARTH  ( 557, 1440, 5535, 2481, 2468, 2455, new RSTile(-1, -1),     new RSArea(2631, 4816, 2682, 4854)),
  MIND   ( 558, 1448, 5529, 2479, 2466, 2453, new RSTile(-1, -1),     new RSArea(2761, 4819, 2802, 4854)),
  BODY   ( 559, 1446, 5533, 2483, 2470, 2457, new RSTile(-1, -1),     new RSArea(2507, 4831, 2533, 4849)),
  DEATH  ( 560,   -1,   -1,   -1,   -1,   -1,       null,             null),
  CHAOS  ( 562,   -1,   -1,   -1,   -1,   -1,       null,             null),
  LAW    ( 563,   -1,   -1,   -1,   -1,   -1,       null,             null),
  COSMIC ( 564,   -1,   -1,   -1,   -1,   -1,       null,             null),
  ESSENCE(1436,   -1,   -1,   -1,   -1,   -1,       null,             null);

  int    id_item,
         id_talisman,
         id_tiara,
         id_altar,
         id_portal,
         id_ruins;
  RSTile tile_ruins;
  RSArea area_altar;

  // Constructor
  private Rune(final int id_item, final int id_talisman, final int id_tiara, final int id_altar, final int id_portal, final int id_ruins, final RSTile tile_ruins, final RSArea area_altar) {
    this.id_item = id_item;
    this.id_talisman = id_talisman;
    this.id_tiara = id_tiara;
    this.id_altar = id_altar;
    this.id_portal = id_portal;
    this.id_ruins = id_ruins;
    this.tile_ruins = tile_ruins;
    this.area_altar = area_altar;
  }

  public final int getItemID() {
    return id_item;
  }

  public final int getTalismanID() {
    return id_talisman;
  }

  public final int getTiaraID() {
    return id_tiara;
  }

  public final int getAltarID() {
    return id_altar;
  }

  public final int getPortalID() {
    return id_portal;
  }

  public final int getRuinsID() {
    return id_ruins;
  }

  public final RSTile getRuinsTile() {
    return tile_ruins;
  }

  public final RSArea getAltarArea() {
    return area_altar;
  }

  public final boolean isElemental() {
    return this == AIR || this == WATER || this == FIRE || this == EARTH;
  }
}