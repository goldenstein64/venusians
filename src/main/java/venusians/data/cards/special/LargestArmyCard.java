package venusians.data.cards.special;

import venusians.data.players.Player;

public final class LargestArmyCard {

  private LargestArmyCard() {}

  public static final int CARD_VALUE = 2;

  private static Player cardOwner = null;

  public static void startGame() {
    cardOwner = null;
  }

  public static int getMostKnightsPlayed() {
    if (cardOwner != null) {
      return cardOwner.getKnightsPlayed();
    } else {
      return 0;
    }
  }

  public static Player getCardOwner() {
    return cardOwner;
  }

  public static void setCardOwner(Player newOwner) {
    if (cardOwner != null) {
      cardOwner.addVictoryPoints(-CARD_VALUE);
    }
    newOwner.addVictoryPoints(CARD_VALUE);
    cardOwner = newOwner;
  }
}
