package venusians.data.cards.development;

import java.util.Random;
import javafx.scene.image.Image;
import venusians.data.players.Player;

public class VictoryPointCard extends DevelopmentCard {

  private Random rng = new Random();
  private final String name;
  private static final String description = "";
  private final Image cardImage;
  private Player owner;

  public VictoryPointCard(Player owner) {
    this.owner = owner;

    // TODO: randomize name and image
    this.name = "Library";
    this.cardImage = new Image("");
  }

  public void apply() {
    // add a victory point to the player's total score
  }
}
