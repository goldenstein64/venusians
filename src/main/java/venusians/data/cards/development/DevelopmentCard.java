package venusians.data.cards.development;

import java.net.URISyntaxException;
import javafx.scene.image.Image;

public abstract class DevelopmentCard {

  protected static Image loadImage(String filename) {
    Image tempCardImage = null;
    try {
      tempCardImage =
        new Image(
          DevelopmentCard.class.getResource(filename).toURI().toString()
        );
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return tempCardImage;
  }

  public abstract String getName();

  public abstract String getDescription();

  public abstract Image getCardImage();

  public abstract void apply();
}
