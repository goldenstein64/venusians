package venusians.data.cards.development;

import java.net.URISyntaxException;
import java.util.Random;
import javafx.scene.image.Image;
import venusians.data.players.Player;

public class KnightCard extends DevelopmentCard {

  private static final String name = "Knight";
  private static final String description = "Move the Robber.";
  private static final Image cardImage = loadImage("knightChip.png");
  private Player owner;

  public KnightCard(Player owner) {
    this.owner = owner;
  }

  public Image getCardImage() {
    return cardImage;
  }

  public void apply() {
    // choose a place to move the robber.
    // if the robber lands next to a settlement, choose one and steal a resource from its owner.
  }
}
