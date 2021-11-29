package venusians.data;

import venusians.data.board.Board;
import venusians.data.cards.development.DevelopmentCard;
import venusians.data.cards.special.LargestArmyCard;
import venusians.data.cards.special.LongestRoadCard;
import venusians.data.lifecycle.GameOptions;
import venusians.data.lifecycle.GameResults;
import venusians.data.players.Players;

/**
 * Class responsible for implementing game logic
 */
public final class Game {

  private Game() {}

  private static GameOptions gameOptions = new GameOptions();
  private static GameResults gameResults = new GameResults();

  /**
   * Returns the game's active GameOptions object. This becomes a dead
   * reference if Game.resetData() is called.
   * @return The active GameOptions object.
   */
  public static GameOptions getGameOptions() {
    return gameOptions;
  }

  /**
   * Returns the game's active GameResults object. This becomes a dead
   * reference if Game.resetData() is called.
   * @return The active GameResults object.
   */
  public static GameResults getGameResults() {
    return gameResults;
  }

  /**
   * Assuming GameOptions has been finalized, it initializes the data in each
   * of the game's sub-singletons.
   */
  public static void startGame() {
    Players.startGame();
    Board.startGame();
    DevelopmentCard.startGame();
    LargestArmyCard.startGame();
    LongestRoadCard.startGame();
  }

  /**
   * Resets the GameOptions and GameResults objects.
   */
  public static void resetData() {
    gameOptions = new GameOptions();
    gameResults = new GameResults();
  }
}
