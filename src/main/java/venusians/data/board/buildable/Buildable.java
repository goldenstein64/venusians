package venusians.data.board.buildable;

import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public interface Buildable {
  public ResourceCardMap getBlueprint();

  public Player getOwner();
}
