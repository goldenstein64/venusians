package venusians.data.board.buildable;

import java.util.HashMap;
import javafx.scene.image.Image;
import venusians.data.IntPoint;
import venusians.data.board.Board;
import venusians.data.board.Board.PositionType;
import venusians.data.board.tiles.MapSlot;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.players.Player;

public class Settlement implements Buildable {

  private static final Image image = new Image("");
  public static final HashMap<ResourceCard, Integer> blueprint = new HashMap<ResourceCard, Integer>();

  {
    blueprint.put(ResourceCard.BRICK, 1);
    blueprint.put(ResourceCard.WOOD, 1);
    blueprint.put(ResourceCard.WHEAT, 1);
    blueprint.put(ResourceCard.WOOL, 1);
  }

  private IntPoint position;
  private PositionType positionType;
  private Player owner;

  public Settlement(Player owner, IntPoint position) {
    this.owner = owner;
    this.position = position;
    this.positionType = PositionType.valueOf(position);
    if (positionType == PositionType.TILE) {
      throw new IllegalArgumentException("This position is of type TILE");
    }
  }

  @Override
  public IntPoint getPosition() {
    return position;
  }

  public static HashMap<ResourceCard, Integer> getBlueprint() {
    return blueprint;
  }

  @Override
  public Image getImage() {
    return image;
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  public HashMap<ResourceCard, Integer> getResources() {
    HashMap<ResourceCard, Integer> result = new HashMap<ResourceCard, Integer>();
    MapSlot[][] map = Board.getMap();
    for (
      int i = positionType == PositionType.EVEN_CORNER ? 0 : 1;
      i < Board.firstOrderOffsets.length;
      i += 2
    ) {
      IntPoint offset = Board.firstOrderOffsets[i];
      IntPoint newPosition = position.plus(offset);
      MapSlot tile = map[newPosition.x][newPosition.y];
      if (tile.kind instanceof ResourceCard) {
        ResourceCard resource = (ResourceCard) tile.kind;
        int oldValue = result.getOrDefault(resource, 0);
        result.put(resource, oldValue);
      }
    }
    return result;
  }
}
