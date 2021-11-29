package venusians.data.board.buildable;

import javafx.scene.image.Image;
import venusians.data.board.Board.PositionType;
import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;
import venusians.util.Images;

public class Settlement extends Building {

  public static final Image MAP_GRAPHIC = Images.load(
    Settlement.class,
    "settlement.png"
  );
  public static final ResourceCardMap BLUEPRINT = new ResourceCardMap();

  static {
    BLUEPRINT.put(ResourceCard.BRICK, 1);
    BLUEPRINT.put(ResourceCard.WOOD, 1);
    BLUEPRINT.put(ResourceCard.WHEAT, 1);
    BLUEPRINT.put(ResourceCard.WOOL, 1);
  }

  private PositionType positionType;

  public Settlement(Player owner, IntPoint position) {
    super(owner, position);
    this.positionType = PositionType.valueOf(position);
    if (positionType == PositionType.TILE) {
      throw new IllegalArgumentException(
        String.format("This position %s is of type TILE", position)
      );
    }
  }

  @Override
  public ResourceCardMap getBlueprint() {
    return BLUEPRINT;
  }

  @Override
  public Image getImage() {
    return MAP_GRAPHIC;
  }
}
