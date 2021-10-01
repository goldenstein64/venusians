package venusians.data.board.buildable;

import java.util.HashMap;
import javafx.scene.image.Image;
import venusians.data.Point;
import venusians.data.cards.resource.ResourceCard;

public class Road implements Buildable {

  private static final Image image = new Image("");
  private static final HashMap<ResourceCard, Integer> blueprint = new HashMap<ResourceCard, Integer>();

  {
    blueprint.put(ResourceCard.WOOD, 1);
    blueprint.put(ResourceCard.BRICK, 1);
  }

  private Point position1;
  private Point position2;

  public Road(Point position1, Point position2) {
    this.position1 = position1;
    this.position2 = position2;
  }

  public static HashMap<ResourceCard, Integer> getBlueprint() {
    return blueprint;
  }

  @Override
  public Point getPosition() {
    return position1;
  }

  @Override
  public Image getImage() {
    return image;
  }
}
