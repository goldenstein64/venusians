package venusians.data.board.buildable;

/**
 * A special class for storing Buildables that aren't real yet, aka suggested.
 */
public class SuggestedBuildable<T extends Buildable> {

  private T buildable;

  public SuggestedBuildable(T buildable) {
    this.buildable = buildable;
  }

  public T getBuildable() {
    return buildable;
  }
}
