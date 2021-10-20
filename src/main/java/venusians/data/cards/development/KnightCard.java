package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;
import venusians.util.Images;

public class KnightCard extends DevelopmentCard {

  private static final String name = "Knight";
  private static final String description = "Move the Robber.";
  private static final Image cardImage = Images.load(KnightCard.class, "knightChip.png");

  public KnightCard(Player owner) {
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
    // choose a place to move the robber.
    // if the robber lands next to a settlement, choose one and steal a resource from its owner.
  }
}
