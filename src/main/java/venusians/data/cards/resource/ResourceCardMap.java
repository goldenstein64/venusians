package venusians.data.cards.resource;

import java.util.HashMap;

public class ResourceCardMap extends HashMap<ResourceCard, Integer> {

  public ResourceCardMap() {
    super();
    for (ResourceCard card : ResourceCard.values()) {
      super.put(card, 0);
    }
  }

  public void add(ResourceCard card) {
    add(card, 1);
  }

  public void add(ResourceCard card, int count) {
    int sum = super.get(card) + count;
    super.put(card, sum);
  }

  public void add(ResourceCardMap blueprint) {
    for (Entry<ResourceCard, Integer> entry : super.entrySet()) {
      int addend = blueprint.get(entry.getKey());
      int sum = entry.getValue() + addend;
      entry.setValue(sum);
    }
  }

  public void remove(ResourceCardMap blueprint) {
    for (Entry<ResourceCard, Integer> entry : super.entrySet()) {
      int subtrahend = blueprint.get(entry.getKey());
      int difference = entry.getValue() - subtrahend;
      entry.setValue(difference);
    }
  }

  public void remove(ResourceCard card) {
    remove(card, 1);
  }

  public void remove(ResourceCard card, int count) {
    int difference = super.get(card) - count;
    super.put(card, difference);
  }

  public boolean contains(ResourceCardMap blueprint) {
    for (Entry<ResourceCard, Integer> entry : super.entrySet()) {
      int expected = blueprint.get(entry.getKey());
      int actual = entry.getValue();
      if (actual < expected) return false;
    }

    return true;
  }

  public boolean contains(ResourceCard card, int count) {
    int actual = super.get(card);
    return actual >= count;
  }

  public ResourceCardList toList() {
    ResourceCardList result = new ResourceCardList();
    for (Entry<ResourceCard, Integer> entry : super.entrySet()) {
      ResourceCard card = entry.getKey();
      int count = entry.getValue();
      for (int i = 0; i < count; i++) {
        result.add(card);
      }
    }

    return result;
  }

  public int size() {
    int total = 0;

    for (Integer count : super.values()) {
      total += count;
    }

    return total;
  }
}
