package venusians.gui.main;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import venusians.data.board.IntPoint;
import venusians.data.board.Point;
import venusians.data.board.buildable.Buildable;
import venusians.data.board.buildable.Building;
import venusians.data.board.buildable.Road;

public class BuildableRenderer {
	public static final double BUILDING_SIZE = 35;
	private static final ColorAdjust MONOCHROME = new ColorAdjust(0, -1, 0, 0);

	private HashMap<Buildable, Node> buildableNodeMap = new HashMap<>();
	private Pane parentPane;

	public BuildableRenderer(Pane parentPane) {
		this.parentPane = parentPane;
	}

	public void updateGraphics(Collection<Buildable> newBuildables) {
		HashSet<Buildable> missingBuildables = new HashSet<>(newBuildables);
		HashSet<Buildable> extraBuildables = new HashSet<>(buildableNodeMap.keySet());

		missingBuildables.removeAll(buildableNodeMap.keySet());
		extraBuildables.removeAll(newBuildables);

		for (Buildable buildable : extraBuildables) {
			Node node = buildableNodeMap.get(buildable);
			buildableNodeMap.remove(buildable);
			parentPane.getChildren().remove(node);
		}

		for (Buildable buildable : missingBuildables) {
			if (buildable instanceof Building) {
				Building building = (Building) buildable;
				renderBuilding(building);

			} else if (buildable instanceof Road) {
				Road road = (Road) buildable;
				renderRoad(road);
			}
		}
	}
	
	private void renderBuilding(Building building) {
    Image mapGraphic = building.getImage();
    ImageView imageView = new ImageView(mapGraphic);
    imageView.setFitWidth(BUILDING_SIZE);
    imageView.setFitHeight(BUILDING_SIZE);

    ImageView clipView = new ImageView(mapGraphic);
    clipView.setFitWidth(BUILDING_SIZE);
    clipView.setFitHeight(BUILDING_SIZE);
    imageView.setClip(clipView);
    
    Point guiPosition = HexTransform.toGuiSpace(building.getPosition());
    imageView.setLayoutX(guiPosition.x - 17.5);
    imageView.setLayoutY(guiPosition.y - 17.5);

    imageView.setEffect(new Blend(BlendMode.MULTIPLY, MONOCHROME,
      new ColorInput(
        0, 0, // x, y
        mapGraphic.getWidth(), mapGraphic.getHeight(), // width, height
        building.getOwner().getColor() // color
      )
    ));

    buildableNodeMap.put(building, imageView);
    parentPane.getChildren().add(imageView);
  }

  private void renderRoad(Road road) {
    IntPoint position1 = road.getPosition1();
    IntPoint position2 = road.getPosition2();

    Point guiPosition1 = HexTransform.toGuiSpace(position1);
    Point guiPosition2 = HexTransform.toGuiSpace(position2);

    Point center = (guiPosition1.plus(guiPosition2)).over(2);
		Point delta = guiPosition2.minus(guiPosition1);
		double length = delta.magnitude();
		double angle = Math.atan2(delta.y, delta.x);
    
    Rectangle roadGraphic = new Rectangle(length, 10);
    roadGraphic.setFill(road.getOwner().getColor());
		roadGraphic.setLayoutX(center.x - length / 2);
		roadGraphic.setLayoutY(center.y - 5.0);
		roadGraphic.setRotate(Math.toDegrees(angle));

		buildableNodeMap.put(road, roadGraphic);
		parentPane.getChildren().add(roadGraphic);
  }
}
