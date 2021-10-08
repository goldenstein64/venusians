package venusians.data;

public class IntPoint extends Point {

  public final int x;
  public final int y;

  public IntPoint(int x, int y) {
    super(x, y);
    this.x = x;
    this.y = y;
  }

  public boolean equals(Point other) {
    return this.x == other.x && this.y == other.y;
  }

  public IntPoint plus(IntPoint other) {
    return new IntPoint(this.x + other.x, this.y + other.y);
  }

  public IntPoint minus(IntPoint other) {
    return new IntPoint(this.x - other.x, this.y - other.y);
  }

  public IntPoint negative() {
    return new IntPoint(-this.x, -this.y);
  }

  public IntPoint time(IntPoint other) {
    return new IntPoint(this.x * other.x, this.y * other.y);
  }

  public IntPoint times(int scalar) {
    return new IntPoint(this.x * scalar, this.y * scalar);
  }
}
