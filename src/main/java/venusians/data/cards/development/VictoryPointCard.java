package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;
import venusians.util.Images;

public class VictoryPointCard extends DevelopmentCard {

  private static final String name = "Victory Point";
  private static final String description = "";
  private static final Image cardImage = Images.load(VictoryPointCard.class, "victoryPointChip.png");

  public VictoryPointCard(Player owner) {
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
    owner.incrementVictoryPoints();
  }
}
