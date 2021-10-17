package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;

public class RoadBuildingCard extends DevelopmentCard {

  private static final String name = "";
  private static final String description = "";
  private static final Image cardImage = loadImage("roadBuildingChip.png");
  private Player owner;

  public RoadBuildingCard(Player owner) {
    this.owner = owner;
  }

  public void apply() {
    // place two roads, like they were just built
  }
}
