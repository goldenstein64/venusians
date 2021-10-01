package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;

public class YearOfPlentyCard extends DevelopmentCard {

  private static final String name = "";
  private static final String description = "";
  private static final Image cardImage = new Image("");
  private Player owner;

  public YearOfPlentyCard(Player owner) {
    this.owner = owner;
  }

  public void apply() {
    // take 2 of any resource.
    // it can be 2 of the same or 2 different resources
  }
}
