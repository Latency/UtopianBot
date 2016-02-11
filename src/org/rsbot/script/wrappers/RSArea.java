package org.rsbot.script.wrappers;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import javax.vecmath.Point3d;

import org.rsbot.script.ScriptManifest;

/**
 * Represents a shape made of RSTiles.
 * 
 * @author Latency
 */
@ScriptManifest(authors = { "Latency" }, name = "RSArea", version = 2.0, description = "Extends RSTiles to construct N-point polygons.")
public class RSArea {
  private ArrayList<RSTile> tiles = new ArrayList<RSTile>(); // Actual list of points.
  private final int         plane;
  private final Polygon     poly;

  /**
   * @param tiles An Array containing of <b>RSTiles</b> forming a polygon shape.
   * @param plane The plane of the <b>RSArea</b>.
   */
  public RSArea(final RSTile[] tiles, final int plane) {
    this.plane = plane;
    this.tiles = toArray(tiles);
    this.poly = tileArrayToPolygon(tiles);
  }

  public RSArea(final RSTile tile) {
    this.plane = tile.getZ();
    this.tiles.add(tile);
    this.poly = tileToPolygon(tile);
  }

  /**
   * @param tiles An array containing <b>RSTile</b>s forming a polygon shape.
   */
  public RSArea(final RSTile[] tiles) {
    this(tiles, 0);
  }

  /**
   * @param aryList An Array containing of <b>RSTiles</b> forming a polygon shape.
   */
  public RSArea(final ArrayList<RSTile> aryList) {
    this(toArray(aryList), 0);
  }

  /**
   * @param sw The <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param ne The <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param plane The plane of the <b>RSArea</b>.
   */
  public RSArea(final RSTile sw, final RSTile ne, final int plane) {
    this.plane = plane;
    this.poly = rectangleToPolygon(sw, ne);
    this.tiles = this.setTiles();
  }

  /**
   * @param sw The <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param ne The <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
   */
  public RSArea(final RSTile sw, final RSTile ne) {
    this(sw, ne, 0);
  }

  /**
   * @param swX The X axis of the <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param swY The Y axis of the <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param neX The X axis of the <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param neY The Y axis of the <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
   */
  public RSArea(final int swX, final int swY, final int neX, final int neY) {
    this(new RSTile(swX, swY), new RSTile(neX, neY), 0);
  }

  /**
   * @param swX The X axis of the <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param swY The Y axis of the <i>South West</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param neX The X axis of the <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param neY The Y axis of the <i>North East</i> <b>RSTile</b> of the <b>RSArea</b>
   * @param plane The Z axis of the <i>Up Down</i> <b>RSTile</b> of the <b>RSArea</b>
   */
  public RSArea(final int swX, final int swY, final int neX, final int neY, final int plane) {
    this(new RSTile(swX, swY), new RSTile(neX, neY), plane);
  }

  /**
   * @param x The x location of the <b>RSTile</b> that will be checked.
   * @param y The y location of the <b>RSTile</b> that will be checked.
   * @return True if the <b>RSArea</b> contains the given <b>RSTile</b>.
   */
  public final boolean contains(final int x, final int y) {
    return contains(new RSTile(x, y));
  }

  /**
   * @param x The x location of the <b>RSTile</b> that will be checked.
   * @param y The y location of the <b>RSTile</b> that will be checked.
   * @param z The z location of the <b>RSTile</b> that will be checked.
   * @return True if the <b>RSArea</b> contains the given <b>RSTile</b>.
   */
  public final boolean contains(final int x, final int y, final int z) {
    return contains(new RSTile(x, y, z));
  }

  /**
   * @param tiles The <b>RSTile(s)</b> that will be checked.
   * @return True if the <b>RSArea</b> contains the given <b>RSTile(s)</b>.
   */
  public final boolean contains(final RSTile... tiles) {
    for (final RSTile t : tiles) {
      if (this.tiles.contains(t))
        return true;
    }
    return false;
  }

  /**
   * @return The central <b>RSTile</b> of the <b>RSArea</b>.
   */
  public final RSTile getCentralTile() {
    if (poly.npoints < 1)
      return null;
    int totalX = 0, totalY = 0;
    for (int i = 0; i < poly.npoints; i++) {
      totalX += poly.xpoints[i];
      totalY += poly.ypoints[i];
    }
    return new RSTile(Math.round(totalX / poly.npoints), Math.round(totalY / poly.npoints));
  }

  /**
   * @param base The base tile to measure the closest tile off of.
   * @return The nearest <b>RSTile</b> in the <b>RSArea</b> to the given <b>RSTile</b>.
   */
  public final RSTile getNearestTile(final RSTile base) {
    RSTile cur = null;
    double dist = -1;
    for (final RSTile tile : this.toArray()) {
      final Point3d pTile = new Point3d(tile.getX(), tile.getY(), tile.getZ()),
                    pBase = new Point3d(base.getX(), base.getY(), base.getZ());
      final double distTmp = pTile.distance(pBase);
      if (cur == null) {
        dist = distTmp;
        cur = tile;
      } else {
        if (distTmp < dist) {
          dist = distTmp;
          cur = tile;
        }
      }
    }
    return cur;
  }

  /**
   * Determines if a point lies within a polygon.
   * 
   * @param test The point to consider.
   * @return True if point is within polygon. False otherwise.
   * @link http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
   */
  @SuppressWarnings("unused")
  private boolean pointInPolygon(final Point test) {
    final Polygon p = this.poly;
    boolean c = true;
    for (int i = 0, j = this.poly.npoints - 1; i < this.poly.npoints; j = i++) {
      if (((p.ypoints[i] > test.y) != (p.ypoints[j] > test.y)) && (test.x < (p.xpoints[j] - p.xpoints[i]) * (test.y - p.ypoints[i]) / (p.ypoints[j] - p.ypoints[i]) + p.xpoints[i]))
        c = !c;
    }
    return c;
  }

  /**
   * @return The <b>ArrayList<RSTile></b> representing all the tiles within the polygon.
   */
  private ArrayList<RSTile> setTiles() {
    Rectangle b = this.poly.getBounds();
    this.tiles.clear();
    for (int xDev = 0; xDev < b.width; xDev++) {
      for (int yDev = 0; yDev < b.height; yDev++)
        this.tiles.add(new RSTile(b.x + xDev, b.y + yDev, this.plane));
    }
    if (tiles.size() > 0)
      Collections.sort(tiles, this.compare);
    return this.tiles;
  }

  /**
   * @return The <b>RSTiles</b> the <b>RSArea</b> contains.
   */
  public RSTile[] getTileArray() {
    final ArrayList<RSTile> list = new ArrayList<RSTile>();
    for (int x = getX(); x <= getX() + getWidth(); x++) {
      for (int y = getY(); y <= getY() + getHeight(); y++) {
        if (contains(x, y))
          list.add(new RSTile(x, y));
      }
    }
    final RSTile[] tiles = new RSTile[list.size()];
    for (int i = 0; i < list.size(); i++)
      tiles[i] = list.get(i);
    return tiles;
  }

  /**
   * @return The <b>RSTiles</b> the <b>RSArea</b> contains.
   */
  public final RSTile[][] getTiles() {
    final RSTile[][] tiles = new RSTile[this.getWidth()][this.getHeight()];
    for (int i = 0; i < this.getWidth(); ++i)
      for (int j = 0; j < this.getHeight(); ++j)
        if (contains(this.getX() + i, this.getY() + j))
          tiles[i][j] = new RSTile(this.getX() + i, this.getY() + j);

    return tiles;
  }

  /**
   * @return The distance between the the <b>RSTile</b> that's most <i>East</i> and the <b>RSTile</b> that's most <i>West</i>.
   */
  public final int getWidth() {
    return this.poly.getBounds().width;
  }

  /**
   * @return The distance between the the <b>RSTile</b> that's most <i>South</i> and the <b>RSTile</b> that's most <i>North</i>.
   */
  public final int getHeight() {
    return this.poly.getBounds().height;
  }

  /**
   * @return The X-axis of the <b>RSTile</b> that is the farthest to the <i>West</i>.
   */
  public final int getX() {
    return this.poly.getBounds().x;
  }

  /**
   * @return The Y-axis of the <b>RSTile</b> that is the farthest to the <i>South</i>.
   */
  public final int getY() {
    return this.poly.getBounds().y;
  }

  /**
   * @return The plane of the <b>RSArea</b>.
   */
  public final int getPlane() {
    return this.plane;
  }

  /**
   * Gets the bounding box of an area.
   * 
   * @return The bounding box of the <b>RSArea</b>.
   */
  public final Rectangle getBounds() {
    return new Rectangle(this.getX() + 1, this.getY() + 1, this.getWidth(), this.getHeight());
  }

  /**
   * Creates a <b>Polygon</b> from an array of <b>RSTile</b>'s.
   * 
   * @param tiles The <b>RSTile</b> of the Polygon.
   * @return The Polygon of the <b>RSTile</b>.
   */
  private static final Polygon tileArrayToPolygon(final RSTile[] tiles) {
    final Polygon poly = new Polygon();
    for (final RSTile t : tiles)
      poly.addPoint(t.getX(), t.getY());
    return poly;
  }

  /**
   * Creates a <b>Polygon</b> from a <b>RSTile</b>.
   * 
   * @param t The <b>RSTile</b> of the Polygon.
   * @return The Polygon of the <b>RSTile</b>.
   */
  private static final Polygon tileToPolygon(final RSTile t) {
    final Polygon poly = new Polygon();
    for (int x = 0; x < 4; x++)
      poly.addPoint(t.getX(), t.getY());
    return poly;
  }

  /**
   * Creates a <b>Polygon</b> from a set of <b>RSTile</b>'s. Rectangle is constructed from the <b>first</b> point diagonal to the <b>second</b>. Supports translations.
   * 
   * @param tiles The <b>RSTile</b> of the Polygon.
   * @return The Polygon of the <b>RSTile</b>.
   * @param sw
   * @param ne
   */
  private static final Polygon rectangleToPolygon(final RSTile sw, final RSTile ne) {
    final Point southwest, southeast, northwest, northeast;
    // Absolute (assuming positive values) quadrant slice to start in.
    if (sw.getX() <= ne.getX() && sw.getY() <= ne.getY()) {
      // Quadrant 3 = sw-ne okay
      southwest = new Point(sw.getX(), sw.getY());
      southeast = new Point(ne.getX() + 1, sw.getY());
      northwest = new Point(sw.getX(), ne.getY() + 1);
      northeast = new Point(ne.getX() + 1, ne.getY() + 1);
    } else if (sw.getX() <= ne.getX() && sw.getY() > ne.getY()) {
      // Quadrant 4 = nw-se
      southwest = new Point(sw.getX(), ne.getY());
      southeast = new Point(ne.getX() + 1, ne.getY());
      northwest = new Point(sw.getX(), sw.getY() + 1);
      northeast = new Point(ne.getX() + 1, sw.getY() + 1);
    } else if (sw.getX() > ne.getX() && sw.getY() <= ne.getY()) {
      // Quadrant 2 = se-nw
      southwest = new Point(ne.getX(), sw.getY());
      southeast = new Point(sw.getX() + 1, sw.getY());
      northwest = new Point(ne.getX(), ne.getY() + 1);
      northeast = new Point(sw.getX() + 1, ne.getY() + 1);
    } else { // if (sw.getX() > ne.getX() && sw.getY() > ne.getY()) {
      // Quadrant 1 = ne-sw
      southwest = new Point(ne.getX(), ne.getY());
      southeast = new Point(sw.getX() + 1, ne.getY());
      northwest = new Point(ne.getX(), sw.getY() + 1);
      northeast = new Point(sw.getX() + 1, sw.getY() + 1);
    }
    final Polygon poly = new Polygon();
    poly.addPoint(southwest.x, southwest.y);
    poly.addPoint(southeast.x, southeast.y);
    poly.addPoint(northwest.x, northwest.y);
    poly.addPoint(northeast.x, northeast.y);
    return poly;
  }

  /**
   * Converts an ArrayList<RSTile> to RSTile[].
   * 
   * @param aryList The list to be converted.
   * @return The alternative form as RSTile[].
   */
  private static RSTile[] toArray(final ArrayList<RSTile> aryList) {
    final RSTile[] tiles = new RSTile[aryList.size()];
    int x = 0;
    for (final RSTile tile : aryList)
      tiles[x++] = tile;
    return tiles;
  }

  /**
   * Converts a <b>RSTile[]</b> array to an <b>ArrayList<RSTile></b> of tiles.
   * 
   * @param tiles The array to be converted.
   * @return The alternative form as ArrayList<RSTile>.
   * @author Latency
   */
  private static ArrayList<RSTile> toArray(final RSTile[] tiles) {
    final ArrayList<RSTile> aryList = new ArrayList<RSTile>();
    Collections.addAll(aryList, tiles);
    return aryList;
  }

  /**
   * @return The collection of <b>RSTile</b>'s.
   */
  public final RSTile[] toArray() {
    return toArray(this.tiles);
  }

  /**
   * Compares a <b>Point</b>.
   * 
   * @return is 0 if equivilent, -1 if less than, 1 if greater than.
   * @author Latency
   */
  private final Comparator<RSTile> compare = new Comparator<RSTile>() {
                                       @Override
                                      public int compare(final RSTile o1, final RSTile o2) {
                                         int retval = new Integer(o1.getX()).compareTo(o2.getX());
                                         if (retval == 0) // Match found... so compare planes.
                                           retval = new Integer(o1.getZ()).compareTo(o2.getZ());
                                         return retval;
                                       }
                                     };

  /**
   * Combines RSArea regions into one.
   * 
   * @param area The <b>RSArea</b> to be combined.
   * @return the union of both the first and the second RSArea.
   */
  public final RSArea union(final RSArea area) {
    Collections.addAll(this.tiles, area.toArray());
    return new RSArea(removeDuplicates(this.tiles));
  }

  /**
   * Combines RSTile tiles into one area region.
   * 
   * @param new_tile The <b>RSTile</b> to be combined.
   * @return the union of both the area and the new RSTile.
   */
  public final RSArea union(final RSTile new_tile) {
    this.tiles.add(new_tile);
    return new RSArea(removeDuplicates(this.tiles));
  }

  /**
   * Removes RSTile tile from an area region.
   * 
   * @param old_tile The <b>RSTile</b> to be removed.
   * @return the difference of both the area and the RSTile.
   * @param tile
   */
  public final RSArea minus(final RSTile tile) {
    final ArrayList<RSTile> tileAry = new ArrayList<RSTile>();
    tileAry.add(tile);
    this.tiles.removeAll(tileAry);
    return new RSArea(removeDuplicates(this.tiles));
  }

  /**
   * Removes RSArea area from this area region.
   * 
   * @param area The <b>RSArea</b> to be removed.
   * @return the difference of the original area and specified one.
   */
  public final RSArea minus(final RSArea area) {
    final ArrayList<RSTile> tileAry = new ArrayList<RSTile>();
    Collections.addAll(tileAry, area.toArray());
    this.tiles.removeAll(tileAry);
    return new RSArea(removeDuplicates(this.tiles));
  }

  /**
   * Intersects RSTile tile from an area region.
   * 
   * @param tile The <b>RSTile</b> to be intersected.
   * @return the tile as an <b>RSArea</b> found within the original area.
   */
  public final RSArea intersect(final RSTile tile) {
    return (contains(tile) ? new RSArea(tile) : null);
  }

  /**
   * Intersects RSArea area from this area region.
   * 
   * @param area The <b>RSArea</b> to be overlapped (if any).
   * @return The intersection of the original area and specified one.
   */
  public final RSArea intersect(final RSArea area) {
    final ArrayList<RSTile> tileAry = new ArrayList<RSTile>();
    for (final RSTile tile : area.toArray()) {
      // Assume this.tiles array is pre-sorted. assert(pre-sorted);
      if (Arrays.binarySearch(this.toArray(), tile, compare) != 0)
        tileAry.add(tile);
    }
    return new RSArea(removeDuplicates(tileAry));
  }

  /**
   * Removes duplicate RSTile's from an ArrayList.
   * 
   * @param arlList The <b>ArrayList<RSTile></b> to be iterated.
   * @return The ArrayList<RSTile> container free of duplicates.
   */
  private ArrayList<RSTile> removeDuplicates(final ArrayList<RSTile> arlList) {
    final Set<Point3d> set = new HashSet<Point3d>();
    final ArrayList<RSTile> aryList = new ArrayList<RSTile>();
    Collections.sort(arlList, this.compare);
    for (final RSTile tile : arlList) {
      if (set.add(new Point3d(tile.getX(), tile.getY(), tile.getZ())))
        aryList.add(tile);
    }
    return aryList;
  }

}