package venusians.data.players;

import java.util.HashMap;
import java.util.Map.Entry;

import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardList;
import venusians.data.cards.resource.ResourceCardMap;

public class PlayerResources {
	private ResourceCardMap resourceHand = new ResourceCardMap();

  public PlayerResources() {
    for (ResourceCard card : ResourceCard.values()) {
      resourceHand.put(card, 0);
    }
  }

  public ResourceCardList getResourceHand() {
    ResourceCardList result = new ResourceCardList();
    for (Entry<ResourceCard, Integer> pair : resourceHand.entrySet()) {
      ResourceCard card = pair.getKey();
      int count = pair.getValue();
      for (int i = 0; i < count; i++) {
        result.add(card);
      }
    }

    return result;
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
    removeResourcesFromHand(resources);
  }

  public void removeResourcesFromHand(ResourceCardMap resources) {
    for (Entry<ResourceCard, Integer> entry : resources.entrySet()) {
      ResourceCard card = entry.getKey();
      int count = entry.getValue();
      int oldValue = resourceHand.get(card);
      resourceHand.put(card, oldValue - count);
    }
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
