package venusians.data.chat;

public enum GameBroadcaster implements HasName {
  INSTANCE;

  public static final String NAME = "Game";

  public String getName() {
    return NAME;
  }
}
