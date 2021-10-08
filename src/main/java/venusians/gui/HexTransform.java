package venusians.gui;

import venusians.data.Point;

public class HexTransform {

  private static double xAngle = Math.cos(Math.PI / 3);
  private static double yAngle = Math.sin(Math.PI / 3);
  private static Point offset = new Point(0, -200);

  public static Point toGuiPosition(Point position) {
    double xPosition = 50 * (position.x - position.y * xAngle) + offset.x;
    double yPosition = 50 * position.y * yAngle + offset.y;
    return new Point(xPosition, yPosition);
  }
}
