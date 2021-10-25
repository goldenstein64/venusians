package venusians.data.board.buildable;

import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public interface Buildable {
  public static ResourceCardMap getBlueprint() {
    return null;
  }

  public IntPoint getPosition();

  public Player getOwner();
}
