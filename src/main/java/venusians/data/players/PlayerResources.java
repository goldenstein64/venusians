package venusians.data.players;

import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;

public class PlayerResources {

  private ResourceCardMap resourceHand = new ResourceCardMap();

  public ResourceCardMap getResourceHand() {
    return resourceHand;
  }

  public void add(ResourceCard card, int count) {
    resourceHand.add(card, count);
  }

  public void remove(ResourceCard card, int count) {
    int oldValue = resourceHand.get(card);
    resourceHand.put(card, oldValue - count);
  }

  public void remove(ResourceCardMap resources) {
    throwIfNotEnoughResources(resources);
    resourceHand.remove(resources);
  }

  private void throwIfNotEnoughResources(ResourceCardMap resources) {
    if (!resourceHand.contains(resources)) {
      throw new RuntimeException("Insufficient resources");
    }
  }

  public boolean contains(ResourceCardMap resources) {
    return resourceHand.contains(resources);
  }
}
