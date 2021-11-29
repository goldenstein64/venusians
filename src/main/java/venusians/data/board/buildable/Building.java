package venusians.data.board.buildable;

import javafx.scene.image.Image;
import venusians.data.board.Board;
import venusians.data.board.Board.PositionType;
import venusians.data.board.IntPoint;
import venusians.data.board.tiles.MapSlot;
import venusians.data.board.tiles.TileSlot;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public abstract class Building implements Buildable {

  private IntPoint position;
  private Player owner;

  protected Building(Player owner, IntPoint position) {
    this.position = position;
    this.owner = owner;
  }

  public ResourceCardMap getResources() {
    ResourceCardMap result = new ResourceCardMap();
    PositionType positionType = PositionType.valueOf(position);
    int start = positionType == PositionType.EVEN_CORNER ? 1 : 0;
    for (int i = start; i < Board.FIRST_ORDER_OFFSETS.length; i += 2) {
      IntPoint offset = Board.FIRST_ORDER_OFFSETS[i];
      IntPoint newPosition = position.plus(offset);

      MapSlot mapSlot = Board.getSlotAt(newPosition);
      if (!(mapSlot instanceof TileSlot)) continue;
      TileSlot tile = (TileSlot) mapSlot;

      if (tile.kind instanceof ResourceCard && tile.rollValue != -1) {
        ResourceCard resource = (ResourceCard) tile.kind;
        result.add(resource, 1);
      }
    }

    return result;
  }

  public IntPoint getPosition() {
    return position;
  }

  @Override
  public Player getOwner() {
    return owner;
  }

  public abstract Image getImage();
}
