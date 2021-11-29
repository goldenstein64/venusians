package venusians.data.board.buildable;

import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public class Road implements Buildable {

  public static final ResourceCardMap BLUEPRINT = new ResourceCardMap();

  static {
    BLUEPRINT.put(ResourceCard.WOOD, 1);
    BLUEPRINT.put(ResourceCard.BRICK, 1);
  }

  private IntPoint position1;
  private IntPoint position2;
  private Player owner;

  public Road(Player owner, IntPoint position1, IntPoint position2) {
    this.owner = owner;
    this.position1 = position1;
    this.position2 = position2;
  }

  @Override
  public ResourceCardMap getBlueprint() {
    return BLUEPRINT;
  }

  public IntPoint[] getPositions() {
    return new IntPoint[] { position1, position2 };
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
