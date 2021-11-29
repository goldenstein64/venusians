package venusians.data.cards;

import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public class TradeRequest {

  public ResourceCardMap requestedResources = new ResourceCardMap();
  public ResourceCardMap necessaryResources = new ResourceCardMap();
  public Player merchant;

  public TradeRequest(Player merchant) {
    this.merchant = merchant;
  }

  public void setRequestedResources(ResourceCardMap requestedResources) {
    this.requestedResources = requestedResources;
  }

  public void setNecessaryResources(ResourceCardMap necessaryResources) {
    this.necessaryResources = necessaryResources;
  }

  public ResourceCardMap getRequestedResources() {
    return requestedResources;
  }

  public ResourceCardMap getNecessaryResources() {
    return necessaryResources;
  }
}
