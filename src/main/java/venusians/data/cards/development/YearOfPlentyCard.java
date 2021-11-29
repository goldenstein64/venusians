package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;
import venusians.util.Images;

public class YearOfPlentyCard extends DevelopmentCard {

  private static final String name = "Year of Plenty";
  private static final String description = "Gain 2 resources.";
  private static final Image cardImage = Images.load(
    YearOfPlentyCard.class,
    "yearOfPlentyChip.png"
  );

  public YearOfPlentyCard(Player owner) {
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
