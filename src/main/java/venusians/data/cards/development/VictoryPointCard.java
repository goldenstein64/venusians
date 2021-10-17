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

  public void apply() {
    // add a victory point to the player's total score
  }
}
