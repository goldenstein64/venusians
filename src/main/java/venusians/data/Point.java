package venusians.data;

/**
 * A class responsible for representing a point in 2D space.
 */
public class Point {

  public final int x;
  public final int y;

  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public Point plus(Point other) {
    return new Point(this.x + other.x, this.y + other.y);
  }

  public Point minus(Point other) {
    return new Point(this.x - other.x, this.y - other.y);
  }

  public Point negative() {
    return new Point(-this.x, -this.y);
  }

  public Point times(Point other) {
    return new Point(this.x * other.x, this.y * other.y);
  }

  public Point times(int scalar) {
    return new Point(this.x * scalar, this.y * scalar);
  }
}
