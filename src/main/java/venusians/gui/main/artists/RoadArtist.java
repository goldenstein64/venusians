package venusians.gui.main.artists;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.buildable.Road;
import venusians.gui.main.HexTransform;

public class RoadArtist {

  public static final double ROAD_WIDTH = 10;

  public static Line render(Road subject) {
    IntPoint position1 = subject.getPosition1();
    IntPoint position2 = subject.getPosition2();

    Point guiPosition1 = HexTransform.hexToGuiSpace(position1);
    Point guiPosition2 = HexTransform.hexToGuiSpace(position2);
    Color color = subject.getOwner().getColor();

    return render(guiPosition1, guiPosition2, color);
  }

  public static Line render(Point position1, Point position2, Paint color) {
    Line result = new Line(position1.x, position1.y, position2.x, position2.y);
    result.setStrokeWidth(ROAD_WIDTH);
    result.setStrokeLineCap(StrokeLineCap.ROUND);
    result.setStroke(color);

    return result;
  }

  public static void reRender(Line line, Point position1, Point position2) {
    line.setStartX(position1.x);
    line.setStartY(position1.y);
    line.setEndX(position2.x);
    line.setEndY(position2.y);
  }
}
