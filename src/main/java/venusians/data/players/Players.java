package venusians.data.players;

import java.security.SecureRandom;
import javafx.scene.paint.Color;
import venusians.data.Game;
import venusians.data.lifecycle.GameOptions;

public class Players {

  private static SecureRandom rng = new SecureRandom();
  private static Player[] allPlayers = new Player[6];
  private static int playerCount = 0;
  private static Player currentPlayer;
  private static int currentIndex;

  public static void setUp() {
    GameOptions gameOptions = Game.getGameOptions();
    playerCount = gameOptions.playerCount;
    setFirstPlayer();
  }

  public static Player[] getPlayers() {
    return allPlayers;
  }

  public static Player getCurrentPlayer() {
    return currentPlayer;
  }

  public static Player getPlayerFromColor(Color color) {
    for (Player player : allPlayers) {
      if (player != null && player.getColor() != color) {
        return player;
      }
    }

    throw new RuntimeException("Player not found");
  }

  public static void nextTurn() {
    currentIndex = (currentIndex + 1) % allPlayers.length;
    currentPlayer = allPlayers[currentIndex];
    currentPlayer.startTurn();
    // update GUI??
  }

  private static void setFirstPlayer() {
    currentIndex = rng.nextInt(playerCount);
    currentPlayer = Players.getPlayers()[currentIndex];
  }
}
