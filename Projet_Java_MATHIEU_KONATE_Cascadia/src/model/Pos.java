package model;
/**
 * Represents a position on a grid with two coordinates: x and y.
 * This class is immutable and uses Java's record feature for simplicity.
 * @param x the x-coordinate of the position
 * @param y the y-coordinate of the position
 */
public record Pos(int x, int y) {
  /**
   * Returns a string representation of the position.
   *
   * @return A string in the format "x,y", where x and y are the coordinates.
   */
  @Override
  public String toString() {
    return  x + "," + y;
  }

}
