package venusians.data.board;

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

  public Point mod(Point other) {
    return new Point(this.x % other.x, this.y % other.y);
  }

  public double distanceFrom(Point other) {
    return Math.sqrt(distanceSquaredFrom(other));
  }

  public double distanceSquaredFrom(Point other) {
    double diffX = other.x - this.x;
    double diffY = other.y - this.y;
    return diffX * diffX + diffY * diffY;
  }

  public double magnitudeSquared() {
    return this.x * this.x + this.y * this.y;
  }

  public double magnitude() {
    return Math.sqrt(this.magnitudeSquared());
  }

  public Point unit() {
    return this.over(this.magnitude());
  }

  public IntPoint asInt() {
    return new IntPoint((int) Math.round(this.x), (int) Math.round(this.y));
  }

  public String toString() {
    return String.format("(%.2f, %.2f)", this.x, this.y);
  }
}
