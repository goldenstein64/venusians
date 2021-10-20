package venusians.data.board.buildable;

import javafx.scene.image.Image;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.Board.PositionType;
import venusians.data.board.tiles.MapSlot;
import venusians.data.board.tiles.TileSlot;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public class Settlement implements Building {

  private static final Image image = new Image("");
  public static final ResourceCardMap blueprint = new ResourceCardMap();

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

  public static ResourceCardMap getBlueprint() {
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
  
  public ResourceCardMap getResources() {
    ResourceCardMap result = new ResourceCardMap();
    MapSlot[][] map = Board.getMap();
    for (
      int i = positionType == PositionType.EVEN_CORNER ? 0 : 1;
      i < Board.firstOrderOffsets.length;
      i += 2
    ) {
      IntPoint offset = Board.firstOrderOffsets[i];
      IntPoint newPosition = position.plus(offset);
      MapSlot mapSlot = map[newPosition.x][newPosition.y];
      if (!(mapSlot instanceof TileSlot))
        continue;
      TileSlot tile = (TileSlot) mapSlot;
      if (tile.kind instanceof ResourceCard && tile.rollValue != -1) {
        ResourceCard resource = (ResourceCard) tile.kind;
        int oldValue = result.getOrDefault(resource, 0);
        result.put(resource, oldValue);
      }
    }
    
    return result;
  }
}
