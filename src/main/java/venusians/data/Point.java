package venusians.data;

/**
 * A class responsible for representing a point in 2D space.
 */
public class Point {

  public final double x;
  public final double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public boolean equals(Point other) {
    return this.x == other.x && this.y == other.y;
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

  public Point times(double scalar) {
    return new Point(this.x * scalar, this.y * scalar);
  }

  public Point over(Point other) {
    return new Point(this.x / other.x, this.y / other.y);
  }

  public Point over(double scalar) {
    return new Point(this.x / scalar, this.y / scalar);
  }

  public IntPoint asInt() {
    return new IntPoint((int) x, (int) y);
  }
}
