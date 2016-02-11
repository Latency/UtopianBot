package org.rsbot.script.util;

/**
 * Represents a <code>Pair</code> container to emulate C++/C#/Apache Lang3 bug fix.
 * 
 * @author Latency
 */
public class Pair<A, B> {
  public A left;
  public B right;

  public Pair(A left, B right) {
    this.left = left;
    this.right = right;
  }

  @Override
  public int hashCode() {
    int hashleft = left != null ? left.hashCode() : 0;
    int hashright = right != null ? right.hashCode() : 0;
    return (hashleft + hashright) * hashright + hashleft;
  }

  @Override
  public boolean equals(Object other) {
    if (other instanceof Pair) {
      Pair<?, ?> otherPair = (Pair<?, ?>) other;
      return ((this.left == otherPair.left || (this.left != null && otherPair.left != null && this.left.equals(otherPair.left))) && (this.right == otherPair.right || (this.right != null && otherPair.right != null && this.right.equals(otherPair.right))));
    }
    return false;
  }

  @Override
  public String toString()
  {
    return "(" + left + ", " + right + ")";
  }
}