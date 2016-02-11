package org.rsbot.script.wrappers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.NoSuchElementException;

import javax.vecmath.Point3d;

import org.rsbot.script.methods.Calculations;
import org.rsbot.script.provider.MethodContext;
import org.rsbot.script.provider.MethodProvider;
import org.rsbot.script.util.Pair;

/**
 * Represents a path to walk along in game.
 * 
 * @author Latency
 */
public class RSPath extends MethodProvider {
  private LinkedList<Pair<RSTile, Integer>>         tiles = new LinkedList<Pair<RSTile, Integer>>();
  private final ListIterator<Pair<RSTile, Integer>> itr;

  /**
   * Defines the path traversal options.
   */
  public static enum TraversalOption {
    HANDLE_RUN,
    SPACE_ACTIONS
  }

  /**
   * 
   * @param ctx
   * @param destination
   * @throws Exception
   */
  public RSPath(final MethodContext ctx, final RSTile destination) {
    this(ctx, new RSTile[] { destination });
  }

  /**
   * 
   * @param _ctx
   * @param tiles
   * @throws Exception
   */
  public RSPath(final MethodContext ctx, final RSTile... tiles) {
    super(ctx);
    this.tiles = toLinked(tiles);
    this.itr = this.tiles.listIterator();
  }

  /**
   * @returns The length of the path.
   */
  public final int length() {
    return this.tiles.size();
  }

  /**
   * Checks whether or not this path can be traversed by the player. This will be the case provided that the player near to one of its vertices, but not already standing on the end vertex.
   * 
   * @return <tt>true</tt> if the player can walk along this path; otherwise <tt>false</tt>.
   */
  public boolean isValid() {
    return !tiles.isEmpty() && !methods.players.getMyPlayer().getLocation().equals(end());
  }

  /**
   * If this path is equal to another.
   * 
   * @param obj
   * @return
   */
  @Override
  public final boolean equals(final Object obj) {
    if (obj == this)
      return true;
    if (obj instanceof RSPath) {
      final RSPath path = (RSPath) obj;
      if (path.length() != this.tiles.size())
        return false;
      final List<RSTile> ary = Arrays.asList(path.toArray());
      for (int x = 0; x < path.length(); x++) {
        if (!this.tiles.get(x).equals(ary.get(x)))
          return false;
      }
      return true;
    }
    return false;
  }

  /**
   * Takes a step along this path if appropriate. Specifies both TraversalOption.SPACE_ACTIONS and TraversalOption.HANDLE_RUN.
   * 
   * @return <tt>true</tt> if this path is currently valid for the player; otherwise <tt>false</tt>.
   * @see #traverse(EnumSet)
   * @throws NullPointerException
   */
  public boolean traverse() {
    return traverse(EnumSet.of(TraversalOption.HANDLE_RUN, TraversalOption.SPACE_ACTIONS));
  }

  /**
   * Takes a step along this path if appropriate. If the path cannot be traversed due to the player being too far from its vertices or already at the end vertex, <tt>false</tt> will be returned. In all other cases, <tt>true</tt> will be returned, but an action will not necessarily be performed (based on the given options).
   * 
   * @param options Walking style options.
   * @return <tt>true</tt> if this path is currently valid for the player; otherwise <tt>false</tt>.
   * @throws <code>NullPointerException</code> if there is no valid next tile
   * @throws NullPointerException
   */
  public boolean traverse(final EnumSet<TraversalOption> options) {
    if (!itr.hasNext())
      return false;

    final Pair<RSTile, Integer> next = this.itr.next();
    if (methods.calc.distanceTo(next.left) <= 1 || !this.itr.hasNext() && methods.players.getMyPlayer().isMoving() || next.equals(methods.walking.getDestination()))
      return false;

    for (final TraversalOption opts : options) {
      switch (opts) {
        case HANDLE_RUN:
          if (!methods.walking.isRunEnabled() && methods.walking.getEnergy() > 50) {
            methods.walking.setRun(true);
            sleep(300);
          }
          break;
        case SPACE_ACTIONS:
          final RSTile dest = methods.walking.getDestination();
          if (dest != null && methods.players.getMyPlayer().isMoving() && methods.calc.distanceTo(dest) > 5 && Calculations.distanceBetween(next.left, dest) < 7)
            return true;
          break;
      }
    }
    return methods.walking.walkTileMM(next.left, 0, 0);
  }

  /**
   * Gets the start tile of this path.
   * 
   * @return The start <code>Tile</code>.
   */
  public final RSTile start() {
    try {
      return this.tiles.getFirst().left;
    } catch (NoSuchElementException ex) {
      return null;
    }
  }

  /**
   * Gets the next immediately available vertex in this path.
   * 
   * @return The next walkable <code>RSTile</code>.
   */
  public final RSTile next() {
    try {
      return this.tiles.listIterator().next().left;
    } catch (NoSuchElementException ex) {
      return null;
    }
  }

  /**
   * Gets the end tile of this path.
   * 
   * @return The end <code>Tile</code>.
   */
  public final RSTile end() {
    try {
      return this.tiles.getLast().left;
    } catch (NoSuchElementException ex) {
      return null;
    }
  }

  /**
   * Gets the iterator of this path.
   * 
   * @return The iterator <code>ListIterator</code>.
   */
  public final Iterator<Pair<RSTile, Integer>> iterator() {
    return this.tiles.iterator();
  }

  /**
   * Randomize this path. The original path is stored so this method may be called multiple times without the waypoints drifting far from their original locations.
   * 
   * @param maxX The max deviation on the X axis
   * @param maxY The max deviation on the Y axis
   * @return This path.
   */
  public RSPath randomize() {
    final List<RSTile> list = Arrays.asList(toArray());
    Collections.shuffle(list);
    this.tiles = toLinked(list);
    return this;
  }

  /**
   * Reverses this path.
   * 
   * @return This path.
   */
  public RSPath reverse() {
    final List<RSTile> list = Arrays.asList(toArray());
    Collections.reverse(list);
    this.tiles = toLinked(list);
    return this;
  }

  // --------------------------------------------------------------------------------------------------------

  @SuppressWarnings("unused")
  private static double dist(final Point3d start, final Point3d end) {
    return ((start.x != end.x && start.y != end.y) ? 1.41421356 : 1.0);
  }

  protected static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(final Map<K, V> map) {
    final List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      @Override
      public final int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
        return (o1.getValue()).compareTo(o2.getValue());
      }
    });

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list)
      result.put(entry.getKey(), entry.getValue());
    return result;
  }

  protected static <V, K extends Comparable<? super K>> Map<K, V> sortByKey(final Map<K, V> map) {
    final List<Map.Entry<K, V>> list = new LinkedList<Map.Entry<K, V>>(map.entrySet());
    Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
      @Override
      public final int compare(final Map.Entry<K, V> o1, final Map.Entry<K, V> o2) {
        return (o1.getKey()).compareTo(o2.getKey());
      }
    });

    Map<K, V> result = new LinkedHashMap<K, V>();
    for (Map.Entry<K, V> entry : list)
      result.put(entry.getKey(), entry.getValue());
    return result;
  }

  /**
   * Returns an array containing all of the vertices in the collection specified.
   * 
   * @return A <code>RSTile</code> array.
   */
  public final static <T> T[] toArray(Collection<T> coll, T... ts) {
    return coll.toArray(ts);
  }

  /**
   * Returns an array containing all of the vertices in this path.
   * 
   * @return A <code>RSTile</code> array.
   */
  public final RSTile[] toArray() {
    final RSTile[] list = new RSTile[this.tiles.size()];
    int x = 0;
    for (final Pair<RSTile, Integer> pair : this.tiles)
      list[x++] = pair.left;
    return list;
  }

  /**
   * Returns an ArrayList pair containing all of the vertices in this path, and the distance to the Tile.
   * 
   * @return an array pair containing all of the vertices in this path, and the distance to its corresponding tile.
   */
  protected ArrayList<Pair<RSTile, Integer>> toArray(final RSTile... tiles) {
    ArrayList<Pair<RSTile, Integer>> list = new ArrayList<Pair<RSTile, Integer>>();
    for (final RSTile t : tiles)
      if (t != null)
        list.add(new Pair<RSTile, Integer>(t, methods.calc.distanceTo(t)));
    return list;
  }

  /**
   * Returns an ArrayList pair containing all of the vertices in this path, and the distance to the Tile.
   * 
   * @return an array pair containing all of the vertices in this path, and the distance to its corresponding tile.
   */
  protected LinkedList<Pair<RSTile, Integer>> toLinked(final RSTile... tiles) {
    LinkedList<Pair<RSTile, Integer>> list = new LinkedList<Pair<RSTile, Integer>>();
    for (final RSTile t : tiles)
      synchronized (t) {
        list.add(new Pair<RSTile, Integer>(t, methods.calc.distanceTo(t)));
      }
    return list;
  }

  /**
   * Returns an ArrayList pair containing all of the vertices in this path, and the distance to the Tile.
   * 
   * @return an array pair containing all of the vertices in this path, and the distance to its corresponding tile.
   */
  protected static ArrayList<Pair<RSTile, Integer>> toArray(final List<RSTile> tiles) {
    return toArray(tiles);
  }

  /**
   * Returns an ArrayList pair containing all of the vertices in this path, and the distance to the Tile.
   * 
   * @return an array pair containing all of the vertices in this path, and the distance to its corresponding tile.
   */
  protected static LinkedList<Pair<RSTile, Integer>> toLinked(final List<RSTile> tiles) {
    return toLinked(tiles);
  }

}
