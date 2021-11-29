package venusians.data.board.buildable;

import java.util.Map.Entry;
import javafx.scene.image.Image;
import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;
import venusians.util.Images;

public class City extends Building {

  public static final Image MAP_GRAPHIC = Images.load(City.class, "city.png");
  public static final ResourceCardMap BLUEPRINT = new ResourceCardMap();

  static {
    BLUEPRINT.put(ResourceCard.ORE, 3);
    BLUEPRINT.put(ResourceCard.WHEAT, 2);
  }

  public City(Player owner, IntPoint position) {
    super(owner, position);
  }

  @Override
  public ResourceCardMap getBlueprint() {
    return BLUEPRINT;
  }

  @Override
  public ResourceCardMap getResources() {
    return multiplyResourceCardMapByTwo(super.getResources());
  }

  private ResourceCardMap multiplyResourceCardMapByTwo(
    ResourceCardMap cardMap
  ) {
    for (Entry<ResourceCard, Integer> entry : cardMap.entrySet()) {
      entry.setValue(2 * entry.getValue());
    }
    return cardMap;
  }

  @Override
  public Image getImage() {
    return MAP_GRAPHIC;
  }
}
