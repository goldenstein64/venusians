package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;
import venusians.util.Images;

public class MonopolyCard extends DevelopmentCard {

  private static final String name = "";
  private static final String description = "";
  private static final Image cardImage = Images.load(MonopolyCard.class, "monopolyChip.png");

  public MonopolyCard(Player owner) {
    super(owner);
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getDescription() {
    return description;
  }

  @Override
  public Image getCardImage() {
    return cardImage;
  }

  @Override
  public void apply() {
    // Declare a resource
    // every other player must give 2 of that resource
    // take all of that resource if they have less than 2
  }
}
