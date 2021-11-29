package venusians.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public abstract class DynamicUpdater<T> {

	public class DeltaSet<V> {
		public Set<V> missing;
		public Set<V> extra;

		public DeltaSet(Set<V> missing, Set<V> extra) {
			this.missing = missing;
			this.extra = extra;
		}
	}

	private Set<T> currentSet = new HashSet<>();

	protected abstract void onMissing(T value);
	protected abstract void onExtra(T value);

	public DeltaSet<T> getDeltaSet(Collection<T> newSet) {
		HashSet<T> missingSet = new HashSet<>(newSet);
		HashSet<T> extraSet = new HashSet<>(currentSet);

		missingSet.removeAll(currentSet);
		extraSet.removeAll(newSet);

		return new DeltaSet<T>(missingSet, extraSet);
	}

	public void update(Collection<T> newSet) {
		DeltaSet<T> deltaSet = getDeltaSet(newSet);

		for (T value : deltaSet.missing) {
			onMissing(value);
			currentSet.add(value);
		}

		for (T value : deltaSet.extra) {
			onExtra(value);
			currentSet.remove(value);
		}
	}

	public Set<T> getCurrentSet() {
		return currentSet;
	}
}
