package venusians.data;

import venusians.data.board.Board;
import venusians.data.lifecycle.GameOptions;
import venusians.data.lifecycle.GameResults;
import venusians.data.lifecycle.PlayerProfile;
import venusians.data.players.Players;

/**
 * Class responsible for implementing game logic
 */
public class Game {

  private static GameOptions gameOptions = new GameOptions();
  private static GameResults gameResults = new GameResults();

  public static GameOptions getGameOptions() {
    return gameOptions;
  }

  public static GameResults getGameResults() {
    return gameResults;
  }

  public static void addPlayerProfile(PlayerProfile profile) {
    gameOptions.profiles.add(profile);
  }

  public static void removePlayerProfile(PlayerProfile profile) {
    gameOptions.profiles.remove(profile);
  }

  public static void startGame() {
    Players.startGame();
    Board.startGame();
  }

  public static void resetData() {
    gameOptions = new GameOptions();
    gameResults = new GameResults();
  }
}
