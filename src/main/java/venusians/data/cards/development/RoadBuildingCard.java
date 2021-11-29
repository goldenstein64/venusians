package venusians.data.cards.development;

import javafx.scene.image.Image;
import venusians.data.players.Player;
import venusians.util.Images;

public class RoadBuildingCard extends DevelopmentCard {

  private static final String name = "Road Building";
  private static final String description = "Build 2 roads.";
  private static final Image cardImage = Images.load(
    RoadBuildingCard.class,
    "roadBuildingChip.png"
  );

  public RoadBuildingCard(Player owner) {
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
}
