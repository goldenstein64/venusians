package venusians.data;

import java.util.Random;
import venusians.data.lifecycle.GameOptions;
import venusians.data.lifecycle.GameResults;
import venusians.data.players.Player;
import venusians.data.players.Players;

/**
 * Class responsible for implementing game logic
 */
public class Game {

  private static GameOptions gameOptions;
  private static GameResults gameResults;

  public static GameOptions getGameOptions() {
    return gameOptions;
  }

  public static GameResults getGameResults() {
    return gameResults;
  }

  public static void setUp() {
    Players.setUp();
  }

  public static void resetData() {
    gameOptions = new GameOptions();
    gameResults = new GameResults();
  }
}
