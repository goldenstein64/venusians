package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;
import venusians.util.Images;

public class MonopolyCard extends DevelopmentCard {

  private static final String name = "Monopoly";
  private static final String description =
    "Get 2 Cards of 1 type from everyone else.";
  private static final Image cardImage = Images.load(
    MonopolyCard.class,
    "monopolyChip.png"
  );

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
}
