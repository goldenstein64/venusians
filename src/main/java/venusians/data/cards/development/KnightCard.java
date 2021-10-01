package venusians.data.cards.development;

import java.util.Random;
import javafx.scene.image.Image;
import venusians.data.players.Player;

public class KnightCard extends DevelopmentCard {

  private Random rng = new Random();
  private static final String name = "";
  private static final String description = "";
  private final Image cardImage;
  private Player owner;

  public KnightCard(Player owner) {
    this.owner = owner;
    int chosenImage = rng.nextInt(5) + 1;
    String chosenImagePath = String.format("/knight%d.png", chosenImage);
    cardImage = new Image(chosenImagePath);
  }

  public void apply() {
    // move the robber.
    // if the robber lands next to a settlement, choose one and steal a resource from its owner.
  }
}
