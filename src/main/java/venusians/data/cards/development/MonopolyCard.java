package venusians.data.cards.development;

import java.net.URISyntaxException;
import javafx.scene.image.Image;
import venusians.data.players.Player;

public class MonopolyCard extends DevelopmentCard {

  private static final String name = "";
  private static final String description = "";
  private static final Image cardImage = loadImage("monopolyChip.png");
  private Player owner;

  public MonopolyCard(Player owner) {
    this.owner = owner;
  }

  public void apply() {
    // Declare a resource
    // every other player must give 2 of that resource
    // take all of that resource if they have less than 2
  }
}
