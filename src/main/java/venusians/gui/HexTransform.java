package venusians.gui;

import venusians.data.Point;

public class HexTransform {

  private static double xAngle = Math.cos(Math.PI / 6);
  private static double yAngle = Math.sin(Math.PI / 6);

  public static Point toGuiPosition(Point position) {
    int xPosition = (int) (50 * (position.x - position.y * xAngle));
    int yPosition = (int) (50 * position.y * yAngle);
    return new Point(xPosition, yPosition);
  }
}
