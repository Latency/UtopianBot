package org.rsbot.script.wrappers;

import java.awt.Point;
import java.util.ArrayList;

import javax.vecmath.Point3d;

import org.rsbot.script.provider.MethodContext;

/**
 * A tile at an absolute location in the game world.
 * 
 * @author Latency
 */
public class RSTile extends Point3d {
  /**
   * 
   */
  private static final long serialVersionUID = -2739885686884225619L;
  private int               flags;
  private final ArrayList<RSTile> adj_tiles        = new ArrayList<RSTile>();

  protected static interface Flags {
    public static final int W_NW = (1 << 1),
                                 W_N = (1 << 2),
                                 W_NE = (1 << 3),
                                 W_E = (1 << 4),
                                 W_SE = (1 << 5),
                                 W_S = (1 << 6),
                                 W_SW = (1 << 7),
                                 W_W = (1 << 8),
                                 BLOCKED = (1 << 9),
                                 UNKNOWN_1 = (1 << 20),
                                 UNKNOWN_2 = (1 << 22),
                                 UNKNOWN_3 = (1 << 25),
                                 WATER = BLOCKED | UNKNOWN_1 | UNKNOWN_2 | UNKNOWN_3; // 0x1280100;
  }

  /**
   * @param x the x axis of the Tile
   * @param y the y axis of the Tile
   */
  public RSTile(final int x, final int y) {
    this(x, y, 0);
  }

  public RSTile(final double x, final double y) {
    this((int) x, (int) y, 0);
  }

  public RSTile(final double x, final double y, final double z) {
    this((int) x, (int) y, (int) z);
  }

  public RSTile(final Point p) {
    this(p.x, p.y, 0);
  }

  public RSTile(final Point3d p) {
    this(p.x, p.y, p.z);
  }

  /**
   * @param x the x axel of the Tile
   * @param y the y axel of the Tile
   * @param z the z axel of the Tile( the floor)
   */
  public RSTile(final int x, final int y, final int z) {
    super(x, y, z);
  }

  public boolean isQuestionable() {
    return containsFlag(Flags.W_NW | Flags.W_N | Flags.W_NE | Flags.W_E | Flags.W_SE | Flags.W_S | Flags.W_SW | Flags.W_W);
  }

  public static boolean isWalkable() {
    return !containsFlag(Flags.BLOCKED, Flags.WATER);
  }

  public boolean isSpecial() {
    return !isSet(Flags.BLOCKED) && isSet(Flags.WATER);
  }

  public boolean isSet(final int flag) {
    return (this.flags & flag) == flag;
  }

  public boolean containsFlag(final int bits) {
    return (this.flags & bits) != 0;
  }

  public static boolean isSet(final int val, final int flag) {
    return (val & flag) == flag;
  }

  public static boolean containsFlag(final int val, final int bits) {
    return (val & bits) != 0;
  }

  public final int getX() {
    return (int) this.x;
  }

  public final int getY() {
    return (int) this.y;
  }

  public final int getZ() {
    return (int) this.z;
  }

  public final int getFlags() {
    return this.flags;
  }

  public final RSTile[] getAdjacentTiles() {
    return this.adj_tiles.toArray(new RSTile[this.adj_tiles.size()]);
  }

  public final Point3d toPoint() {
    return new Point3d(getX(), getY(), getZ());
  }

  /**
   * Randomizes this tile.
   * 
   * @param maxXDeviation Max X distance from tile x.
   * @param maxYDeviation Max Y distance from tile y.
   * @return The randomized tile
   */
  public final RSTile randomize(final int maxXDeviation, final int maxYDeviation) {
    final int x = (int) (getX() + (maxXDeviation > 0 ? (Math.random() * 2 - 1.0) * maxXDeviation : 0));
    final int y = (int) (getY() + (maxYDeviation > 0 ? (Math.random() * 2 - 1.0) * maxYDeviation : 0));
    return new RSTile(x, y);
  }

  public void updateTileData(final MethodContext methods) {
    if (methods != null && methods.game != null && methods.game.isLoggedIn()) {
      final int[][] flags = methods.walking.getCollisionFlags((int) this.z);
      if (flags != null) {
        final Point p = new Point((int) (this.x - methods.game.getMapBase().getX()), (int) (this.y - methods.game.getMapBase().getY()));
        this.flags = flags[p.x][p.y];
        final int upper = flags.length - 1;
        this.adj_tiles.clear();
        if (p.y > 0 && !containsFlag(Flags.W_S) && !containsFlag(flags[p.x][p.y - 1], Flags.BLOCKED))
          this.adj_tiles.add(new RSTile(this.x, this.y - 1));
        if (p.x > 0 && !containsFlag(Flags.W_W) && !containsFlag(flags[p.x - 1][p.y], Flags.BLOCKED))
          this.adj_tiles.add(new RSTile(this.x - 1, this.y));
        if (p.y < upper && !containsFlag(Flags.W_N) && !containsFlag(flags[p.x][p.y + 1], Flags.BLOCKED))
          this.adj_tiles.add(new RSTile(this.x, this.y + 1));
        if (p.x < upper && !containsFlag(Flags.W_E) && !containsFlag(flags[p.x + 1][p.y], Flags.BLOCKED))
          this.adj_tiles.add(new RSTile(this.x + 1, this.y));
        if (p.x > 0 && p.y > 0 && !containsFlag(Flags.W_SW | Flags.W_S | Flags.W_W) && !containsFlag(flags[p.x - 1][p.y - 1], Flags.BLOCKED) && !containsFlag(flags[p.x][p.y - 1], Flags.BLOCKED | Flags.W_W) && !containsFlag(flags[p.x - 1][p.y], Flags.BLOCKED | Flags.W_S))
          this.adj_tiles.add(new RSTile(this.x - 1, this.y - 1));
        if (p.x > 0 && p.y < upper && !containsFlag(Flags.W_NW | Flags.W_N | Flags.W_W) && !containsFlag(flags[p.x - 1][p.y + 1], Flags.BLOCKED) && !containsFlag(flags[p.x][p.y + 1], Flags.BLOCKED | Flags.W_W) && !containsFlag(flags[p.x - 1][p.y], Flags.BLOCKED | Flags.W_N))
          this.adj_tiles.add(new RSTile(this.x - 1, this.y + 1));
        if (p.x < upper && p.y > 0 && !containsFlag(Flags.W_SE | Flags.W_S | Flags.W_E) && !containsFlag(flags[p.x + 1][p.y - 1], Flags.BLOCKED) && !containsFlag(flags[p.x][p.y - 1], Flags.BLOCKED | Flags.W_E) && !containsFlag(flags[p.x + 1][p.y], Flags.BLOCKED | Flags.W_S))
          this.adj_tiles.add(new RSTile(this.x + 1, this.y - 1));
        if (p.x > 0 && p.y < upper && !containsFlag(Flags.W_NE | Flags.W_N | Flags.W_E) && !containsFlag(flags[p.x + 1][p.y + 1], Flags.BLOCKED) && !containsFlag(flags[p.x][p.y + 1], Flags.BLOCKED | Flags.W_E) && !containsFlag(flags[p.x + 1][p.y], Flags.BLOCKED | Flags.W_N))
          this.adj_tiles.add(new RSTile(this.x + 1, this.y + 1));
      }
    }
  }

  @Override
  public final boolean equals(final Object obj) {
    if (obj == this)
      return true;
    if (obj instanceof RSTile) {
      final RSTile tile = (RSTile) obj;
      return tile.getX() == getX() && tile.getY() == getY() && tile.getZ() == getZ();
    }
    return false;
  }

  @Override
  public final String toString() {
    return "(X: " + getX() + ", Y:" + getY() + ", Z:" + getZ() + ")";
  }

  /**
   * @param wKey If parameter is true, send with flags, otherwise, (x, y, z)
   * @return A 3D string represented vector + key.
   * @author Latency
   */
  public final String toString(final boolean wKey) {
    return (wKey ? toString(this.flags) : toString());
  }

  /**
   * @param key
   * @return A 3D string represented vector + key.
   * @author Latency
   */
  public final String toString(final int key) {
    return this.toString().replaceFirst(")", " " + key + ")");
  }
}
