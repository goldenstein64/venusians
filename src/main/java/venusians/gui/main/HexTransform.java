package venusians.gui.main;

import venusians.data.Point;

public class HexTransform {

  private static double xAngle = Math.cos(Math.PI / 3);
  private static double yAngle = Math.sin(Math.PI / 3);
  private static Point offset = new Point(-250, -200);

  public static Point toGuiPosition(Point position) {
    return new Point(position.x + position.y * xAngle, position.y * yAngle)
      .times(50)
      .plus(offset);
  }
}
