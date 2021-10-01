package venusians.data.cards.development;

import javafx.scene.image.Image;

public abstract class DevelopmentCard {

  private final String name = null;
  private final String description = null;
  private final Image cardImage = null;

  public abstract void apply();

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
