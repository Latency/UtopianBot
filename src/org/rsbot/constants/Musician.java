package org.rsbot.constants;

import org.rsbot.script.wrappers.RSTile;

// ///////////////////////////////////////////////////////////////////////
// Musicians
// ///////////////////////////////////////////////////////////////////////
public enum Musician {
  Lumbridge               (  29, new RSTile(-1, -1)),
  Draynor_Village         (  30, new RSTile(-1, -1)),
  Clan_Camp               (5439, new RSTile(-1, -1)),
  Kingdom_of_Asgarnia     (5442, new RSTile(-1, -1)),
  Port_Sarim              (6667, new RSTile(-1, -1)),
  Monastery               (8698, new RSTile(-1, -1)),
  West_Varrock            (8699, new RSTile(3153, 3422)),
  East_Varrock            (8700, new RSTile(-1, -1)),
  South_East_Varrock      (8701, new RSTile(-1, -1)),
  North_AlKharid          (8707, new RSTile(-1, -1)),
  South_AlKharid          (8708, new RSTile(-1, -1));

  int         id;
  RSTile      tile;

  // Constructor
  private Musician(final int id, final RSTile tile) {
    this.id   = id;
    this.tile = tile;
  }

  public final int getID() {
    return id;
  }

  public final RSTile getTile() {
    return tile;
  }
}