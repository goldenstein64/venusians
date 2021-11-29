package venusians.data.board;

/**
 * A class responsible for representing a 2D integer point in space
 */
public class IntPoint extends Point {

  public final int x;
  public final int y;

  public IntPoint(int x, int y) {
    super(x, y);
    this.x = x;
    this.y = y;
  }

  public IntPoint plus(IntPoint other) {
    return new IntPoint(this.x + other.x, this.y + other.y);
  }

  public IntPoint minus(IntPoint other) {
    return new IntPoint(this.x - other.x, this.y - other.y);
  }

  @Override
  public IntPoint negative() {
    return new IntPoint(-this.x, -this.y);
  }

  public IntPoint times(IntPoint other) {
    return new IntPoint(this.x * other.x, this.y * other.y);
  }

  public IntPoint times(int scalar) {
    return new IntPoint(this.x * scalar, this.y * scalar);
  }

  public IntPoint over(IntPoint other) {
    return new IntPoint(this.x / other.x, this.y / other.y);
  }

  public IntPoint mod(IntPoint other) {
    return new IntPoint(this.x % other.x, this.y % other.y);
  }

  @Override
  public String toString() {
    return String.format("(%d, %d)", x, y);
  }
}
