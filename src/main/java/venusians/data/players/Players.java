package venusians.data.players;

import java.security.SecureRandom;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.paint.Color;
import venusians.data.Game;
import venusians.data.lifecycle.GameOptions;
import venusians.data.lifecycle.PlayerProfile;

public final class Players {

  private Players() {}

  private static SecureRandom rng = new SecureRandom();
  private static Player[] allPlayers;
  private static Player currentPlayer;
  private static int currentIndex;
  private static ReadOnlyObjectWrapper<Player> currentPlayerWrapper = new ReadOnlyObjectWrapper<Player>(
    Players.class,
    "currentPlayer"
  );

  /**
   * Assuming the game's GameOptions has been finalized, it initializes all
   * Player objects as well as the current player.
   */
  public static void startGame() {
    GameOptions gameOptions = Game.getGameOptions();

    createPlayers(gameOptions);
    shufflePlayers();
    setFirstPlayer();
  }

  private static void createPlayers(GameOptions gameOptions) {
    allPlayers = new Player[gameOptions.profiles.size()];
    for (int i = 0; i < allPlayers.length; i++) {
      PlayerProfile profile = gameOptions.profiles.get(i);
      allPlayers[i] = new Player(profile.name, profile.color);
    }
  }

  private static void shufflePlayers() {
    for (int i = 0; i < allPlayers.length; i++) {
      int choice = rng.nextInt(allPlayers.length);

      Player temp = allPlayers[i];
      allPlayers[i] = allPlayers[choice];
      allPlayers[choice] = temp;
    }
  }

  public static Player[] getPlayers() {
    return allPlayers;
  }

  public static int getPlayerCount() {
    return allPlayers.length;
  }

  public static Player getCurrentPlayer() {
    return currentPlayer;
  }

  public static void setCurrentPlayer(Player currentPlayer) {
    currentPlayerWrapper.set(currentPlayer);
    Players.currentPlayer = currentPlayer;
  }

  public static ReadOnlyObjectProperty<Player> currentPlayerProperty() {
    return currentPlayerWrapper.getReadOnlyProperty();
  }

  /**
   * Looks through each player and returns the one with the given color.
   * @param color The Player color to look for
   * @return The Player with that color.
   */
  public static Player getPlayerFromColor(Color color) {
    for (Player player : allPlayers) {
      if (player != null && player.getColor().equals(color)) {
        return player;
      }
    }

    throw new RuntimeException("Player not found");
  }

  /** Advances to the next player's turn, setting the new current player. */
  public static void nextTurn() {
    currentIndex = (currentIndex + 1) % allPlayers.length;
    setCurrentPlayer(allPlayers[currentIndex]);
  }

  /** Randomly selects which player goes first. */
  private static void setFirstPlayer() {
    currentIndex = rng.nextInt(allPlayers.length);
    setCurrentPlayer(allPlayers[currentIndex]);
  }
}
