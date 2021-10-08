package venusians.data.board.buildable;

import java.util.HashMap;
import javafx.scene.image.Image;
import venusians.data.IntPoint;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.players.Player;

public class Road implements Buildable {

  private static final Image image = new Image("");
  private static final HashMap<ResourceCard, Integer> blueprint = new HashMap<ResourceCard, Integer>();

  {
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

  public static HashMap<ResourceCard, Integer> getBlueprint() {
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

  @Override
  public Image getImage() {
    return image;
  }

  public Player getOwner() {
    return owner;
  }
}
