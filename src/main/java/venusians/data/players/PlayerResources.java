package venusians.data.players;

import java.util.Map.Entry;

import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;

public class PlayerResources {
	private ResourceCardMap resourceHand = new ResourceCardMap();

  public ResourceCardMap getResourceHand() {
    return resourceHand;
  }

  public void addResources(ResourceCard card, int count) {
    int oldValue = resourceHand.get(card);
    resourceHand.put(card, oldValue + count);
  }

  public void removeResources(ResourceCard card, int count) {
    int oldValue = resourceHand.get(card);
    resourceHand.put(card, oldValue - count);
  }

  public void removeResources(ResourceCardMap resources) {
    throwIfNotEnoughResources(resources);
    resourceHand.remove(resources);
  }

  private void throwIfNotEnoughResources(ResourceCardMap resources) {
    if (!hasResources(resources)) {
      throw new RuntimeException("Insufficient resources");
    }
  }

  public boolean hasResources(ResourceCardMap resources) {
    for (Entry<ResourceCard, Integer> entry : resources.entrySet()) {
      if (!hasResource(entry))
        return false;
    }
    return true;
  }

  private boolean hasResource(Entry<ResourceCard, Integer> entry) {
    return resourceHand.get(entry.getKey()) >= entry.getValue();
  }
}
