package venusians.data.board.buildable;

import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public class Road implements Buildable {

  private static final ResourceCardMap blueprint = new ResourceCardMap();

  static {
    blueprint.put(ResourceCard.WOOD, 1);
    blueprint.put(ResourceCard.BRICK, 1);
  }

  private IntPoint position1;
  private IntPoint position2;
  private Player owner;

  public Road(Player owner, IntPoint position1, IntPoint position2) {
    this.owner = owner;
    this.position1 = position1;
    this.position2 = position2;
  }

  public static ResourceCardMap getBlueprint() {
    return blueprint;
  }

  @Override
  public IntPoint getPosition() {
    return position1;
  }

  public IntPoint getPosition1() {
    return position1;
  }

  public IntPoint getPosition2() {
    return position2;
  }

  public Player getOwner() {
    return owner;
  }
}
