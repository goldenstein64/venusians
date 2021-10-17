package venusians.gui.main;

import venusians.data.board.IntPoint;
import venusians.data.board.Point;

public class HexTransform {

  private static final double xAngle = Math.cos(Math.PI / 3);
  private static final double yAngle = Math.sin(Math.PI / 3);
  private static final IntPoint offset = new IntPoint(180, 100);
  private static final double scale = 50;

  /**
   * Takes a position in hexagonal space and converts it to GUI space.
   * @param position
   * @return
   */
  public static Point toGuiPosition(Point position) {
    return new Point(position.x + position.y * xAngle, position.y * yAngle)
      .times(scale)
      .plus(offset);
  }

  public static Point toHexPosition(Point position) {
    Point scaledPosition = position.minus(offset).over(scale);
    double newX =
      (yAngle * scaledPosition.x - xAngle * scaledPosition.y) / yAngle;
    double newY = scaledPosition.y / yAngle;
    return new Point(newX, newY);
  }

  public static IntPoint getClosestCorner(Point position) {
    return getClosestHexCorner(toHexPosition(position));
  }

  public static IntPoint getClosestHexCorner(Point position) {
    IntPoint positionOffset = getPositionOffset(position);

    Point positionCase = position.minus(positionOffset);
    return getCornerByCase(positionCase).plus(positionOffset);
  }

  private static IntPoint getPositionOffset(Point position) {
    int offsetX = (int) Math.floor(position.x);
    int offsetY = 3 * (int) Math.floor(position.y / 3);
    IntPoint positionOffset = new IntPoint(offsetX, offsetY);
    return positionOffset;
  }

  private static IntPoint getCornerByCase(Point position) {
    double x = position.x;
    double y = position.y;

    if (y < 2 * x - 3) {
      return new IntPoint(1, 0);
    } else if (y < (x - 3) / 2) {
      return new IntPoint(0, 0);
    } else if (y < -x) {
      return new IntPoint(0, 1);
    } else if (y < x / 2) {
      return new IntPoint(1, 2);
    } else if (y < 2 * x) {
      return new IntPoint(1, 3);
    } else {
      return new IntPoint(0, 3);
    }
  }
}
