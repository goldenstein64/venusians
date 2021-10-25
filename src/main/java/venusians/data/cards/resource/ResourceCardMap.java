package venusians.data.cards.resource;

import java.util.HashMap;

public class ResourceCardMap extends HashMap<ResourceCard, Integer> {

	public ResourceCardMap() {
		super();
		for (ResourceCard card : ResourceCard.values()) {
			super.put(card, 0);
		}
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
}
