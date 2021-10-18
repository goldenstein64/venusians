package venusians.data.cards.development;

import java.util.Random;
import javafx.scene.image.Image;
import venusians.data.players.Player;

public class VictoryPointCard extends DevelopmentCard {

  private static final String name = "Victory Point";
  private static final String description = "";
  private static final Image cardImage = loadImage("victoryPointChip.png");
  private Player owner;

  public VictoryPointCard(Player owner) {
    this.owner = owner;
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
