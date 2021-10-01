package venusians.data.board.buildable;

import java.util.HashMap;
import javafx.scene.image.Image;
import venusians.data.Point;
import venusians.data.cards.resource.ResourceCard;

public interface Buildable {
  public static HashMap<ResourceCard, Integer> getBlueprint() {
    return null;
  }

  public Point getPosition();

  public Image getImage();
}
