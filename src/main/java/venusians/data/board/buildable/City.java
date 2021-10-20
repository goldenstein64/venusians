package venusians.data.board.buildable;

import java.util.Map.Entry;

import venusians.data.board.IntPoint;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public class City extends Settlement {

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
}
