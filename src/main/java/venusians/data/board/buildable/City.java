package venusians.data.board.buildable;

import java.util.HashMap;
import java.util.Map.Entry;
import venusians.data.Point;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.players.Player;

public class City extends Settlement {

  private static final HashMap<ResourceCard, Integer> blueprint = new HashMap<ResourceCard, Integer>();

  {
    blueprint.put(ResourceCard.ORE, 3);
    blueprint.put(ResourceCard.WHEAT, 2);
  }

  public static HashMap<ResourceCard, Integer> getBlueprint() {
    return blueprint;
  }

  public City(Player owner, Point position) {
    super(owner, position);
  }

  @Override
  public HashMap<ResourceCard, Integer> getResources() {
    HashMap<ResourceCard, Integer> result = super.getResources();
    for (Entry<ResourceCard, Integer> entry : result.entrySet()) {
      result.put(entry.getKey(), 2 * entry.getValue());
    }
    return result;
  }
}
