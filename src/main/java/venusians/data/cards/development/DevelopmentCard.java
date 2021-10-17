package venusians.data.cards.development;

import java.net.URISyntaxException;
import javafx.scene.image.Image;

public abstract class DevelopmentCard {

  private static final String name = null;
  private static final String description = null;
  private static final Image cardImage = null;

  public abstract void apply();

  protected static Image loadImage(String filename) {
    Image tempCardImage = null;
    try {
      tempCardImage =
        new Image(
          MonopolyCard.class.getResource("monopolyChip.png").toURI().toString()
        );
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return tempCardImage;
  }

  public String getName() {
    return name;
  }

  public String getDescription() {
    return description;
  }

  public Image getCardImage() {
    return cardImage;
  }
}
