package venusians.gui.main;

import venusians.data.board.Board;
import venusians.data.board.Board.PositionType;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;

public class HexTransform {

  private static final double X_ANGLE = Math.cos(Math.PI / 3);
  private static final double Y_ANGLE = Math.sin(Math.PI / 3);
  private static final IntPoint GUI_OFFSET = new IntPoint(180, 100);
  private static final double GUI_SCALE = 50;

  /**
   * Takes a position in hexagonal space and converts it to GUI space.
   * @param hexPosition
   * @return
   */
  public static Point hexToGuiSpace(Point hexPosition) {
    return hexToNormalSpace(hexPosition).times(GUI_SCALE).plus(GUI_OFFSET);
  }

  public static Point hexToNormalSpace(Point hexPosition) {
    return new Point(
      hexPosition.x + hexPosition.y * X_ANGLE,
      hexPosition.y * Y_ANGLE
    );
  }

  public static Point normalToHexSpace(Point normalPosition) {
    return new Point(
      (Y_ANGLE * normalPosition.x - X_ANGLE * normalPosition.y) / Y_ANGLE,
      normalPosition.y / Y_ANGLE
    );
  }

  public static Point guiToHexSpace(Point guiPosition) {
    Point scaledPosition = (guiPosition.minus(GUI_OFFSET)).over(GUI_SCALE);
    return normalToHexSpace(scaledPosition);
  }

  /**
   * Takes a position in GUI space and returns the closest tile corner in GUI space.
   * @param guiPosition
   * @return The closest corner
   */
  public static Point guiToGuiCorner(Point guiPosition) {
    return hexToGuiSpace(hexToHexCorner(guiToHexSpace(guiPosition)));
  }

  public static IntPoint guiToHexCorner(Point guiPosition) {
    return hexToHexCorner(guiToHexSpace(guiPosition));
  }

  public static IntPoint hexEstimateHexCorner(
    IntPoint hexStart,
    Point hexGoal
  ) {
    Point normalStart = HexTransform.hexToNormalSpace(hexStart);
    Point normalGoal = HexTransform.hexToNormalSpace(hexGoal);

    Point delta = normalGoal.minus(normalStart);
    Point direction = delta.unit();

    double angle = Math.atan2(direction.y, direction.x);
    if (angle < 0) {
      angle += 2 * Math.PI;
    }

    PositionType startType = PositionType.valueOf(hexStart);

    IntPoint offset;
    if (startType == PositionType.ODD_CORNER) {
      offset = getOddCornerFromAngle(angle);
    } else if (startType == PositionType.EVEN_CORNER) {
      offset = getEvenCornerFromAngle(angle);
    } else {
      throw new RuntimeException("Unknown angle for position type of 'slot'");
    }

    return hexStart.plus(offset);
  }

  private static IntPoint getOddCornerFromAngle(double angle) {
    if (3 * angle < Math.PI) {
      return Board.FIRST_ORDER_OFFSETS[1];
    } else if (angle < Math.PI) {
      return Board.FIRST_ORDER_OFFSETS[3];
    } else if (3 * angle < 5 * Math.PI) {
      return Board.FIRST_ORDER_OFFSETS[5];
    } else {
      return Board.FIRST_ORDER_OFFSETS[1];
    }
  }

  private static IntPoint getEvenCornerFromAngle(double angle) {
    if (3 * angle < 2 * Math.PI) {
      return Board.FIRST_ORDER_OFFSETS[2];
    } else if (3 * angle < 4 * Math.PI) {
      return Board.FIRST_ORDER_OFFSETS[4];
    } else {
      return Board.FIRST_ORDER_OFFSETS[0];
    }
  }

  /**
   * Takes a position in Hexagonal space and returns the closest tile corner in Hexagonal space.
   * @param hexPosition
   * @return The closest corner
   */
  public static IntPoint hexToHexCorner(Point hexPosition) {
    IntPoint positionOffset = getPositionOffset(hexPosition);

    Point positionCase = hexPosition.minus(positionOffset);

    IntPoint result = getCornerByCase(positionCase).plus(positionOffset);

    return result;
  }

  public static IntPoint hexToHexSlot(Point hexPosition) {
    IntPoint positionOffset = getPositionOffset(hexPosition);

    Point positionCase = hexPosition.minus(positionOffset);

    IntPoint result = getSlotByCase(positionCase).plus(positionOffset);

    return result;
  }

  public static IntPoint guiToHexSlot(Point guiPosition) {
    return hexToHexSlot(guiToHexSpace(guiPosition));
  }

  private static IntPoint getPositionOffset(Point hexPosition) {
    IntPoint intPosition = new IntPoint(
      (int) Math.floor(hexPosition.x),
      (int) Math.floor(hexPosition.y)
    );

    int xOffset = (-intPosition.x) % 3;

    return new IntPoint(
      intPosition.x,
      3 * (int) Math.floor((intPosition.y + xOffset) / 3.0) - xOffset
    );
  }

  private static IntPoint getCornerByCase(Point hexPosition) {
    double x = hexPosition.x;
    double y = hexPosition.y;

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

  private static IntPoint getSlotByCase(Point hexPosition) {
    double x = hexPosition.x;
    double y = hexPosition.y;

    if (y < -x + 1) {
      return new IntPoint(0, 0);
    } else if (y < 2) {
      return new IntPoint(1, 1);
    } else {
      return new IntPoint(0, 3);
    }
  }
}
