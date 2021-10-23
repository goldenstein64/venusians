package venusians.data.board.buildable;

import java.util.Map.Entry;

import javafx.scene.image.Image;
import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;
import venusians.util.Images;

public class City extends Settlement {

  private static final Image mapGraphic = Images.load(City.class, "city.png");
  private static final ResourceCardMap blueprint = new ResourceCardMap();

  {
    blueprint.put(ResourceCard.ORE, 3);
    blueprint.put(ResourceCard.WHEAT, 2);
  }

  public static ResourceCardMap getBlueprint() {
    return blueprint;
  }

  public City(Player owner, IntPoint position) {
    super(owner, position);
  }

  @Override
  public ResourceCardMap getResources() {
    return multiplyResourceCardMapByTwo(super.getResources());
  }

  private ResourceCardMap multiplyResourceCardMapByTwo(ResourceCardMap cardMap) {
    for (Entry<ResourceCard, Integer> entry : cardMap.entrySet()) {
      cardMap.put(entry.getKey(), 2 * entry.getValue());
    }
    return cardMap;
  }

  @Override
  public Image getMapGraphic() {
    return mapGraphic;
  }
}
