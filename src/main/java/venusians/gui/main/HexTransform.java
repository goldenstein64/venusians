package venusians.gui.main;

import venusians.data.board.IntPoint;
import venusians.data.board.Point;

public class HexTransform {

  private static final double X_ANGLE = Math.cos(Math.PI / 3);
  private static final double Y_ANGLE = Math.sin(Math.PI / 3);
  private static final IntPoint GUI_OFFSET = new IntPoint(180, 100);
  private static final double GUI_SCALE = 50;

  private static final IntPoint CORNER_CASE_SIZE = new IntPoint(1, 3);

  /**
   * Takes a position in hexagonal space and converts it to GUI space.
   * @param position
   * @return
   */
  public static Point toGuiPosition(Point position) {
    return new Point(position.x + position.y * X_ANGLE, position.y * Y_ANGLE)
      .times(GUI_SCALE)
      .plus(GUI_OFFSET);
  }

  public static Point toHexPosition(Point position) {
    Point scaledPosition = position.minus(GUI_OFFSET).over(GUI_SCALE);
    double newX =
      (Y_ANGLE * scaledPosition.x - X_ANGLE * scaledPosition.y) / Y_ANGLE;
    double newY = scaledPosition.y / Y_ANGLE;
    return new Point(newX, newY);
  }

  public static Point getClosestCorner(Point position) {
    return toGuiPosition(getClosestHexCorner(toHexPosition(position)));
  }

  public static IntPoint getClosestHexCorner(Point position) {
    IntPoint intPosition = new IntPoint(
      (int) Math.floor(position.x), 
      (int) Math.floor(position.y)
    );
    
    int xOffset = (-intPosition.x + 2) % 3;

    IntPoint positionOffset = new IntPoint(
      intPosition.x, 
      3 * (int) Math.floor((intPosition.y + xOffset) / 3.0) - xOffset
    );

    Point positionCase = position.minus(positionOffset);

    IntPoint result = getCornerByCase(positionCase).plus(positionOffset);

    return result;
  }

  private static IntPoint getCornerByCase(Point position) {
    double x = position.x;
    double y = position.y;

    if (y < x) {
      return new IntPoint(1, 0);
    } else if (y < (3 - x) / 2) {
      return new IntPoint(0, 1);
    } else if (y < -2 * x + 3) {
      return new IntPoint(0, 2);
    } else if (y < -x / 2 + 3) {
      return new IntPoint(1, 2);
    } else {
      return new IntPoint(1, 3);
    }
  }
}
