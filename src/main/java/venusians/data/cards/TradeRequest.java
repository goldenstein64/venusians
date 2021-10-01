package venusians.data.cards;

import java.util.HashMap;
import venusians.data.cards.resource.ResourceCard;

public class TradeRequest {

  public HashMap<ResourceCard, Integer> requestedResources;
  public HashMap<ResourceCard, Integer> necessaryResources;
}
