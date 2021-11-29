package venusians.data.players;

import java.util.Map.Entry;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.scene.paint.Color;
import venusians.data.board.RoadRuler;
import venusians.data.board.buildable.City;
import venusians.data.board.buildable.Road;
import venusians.data.board.buildable.Settlement;
import venusians.data.cards.TradeRequest;
import venusians.data.cards.development.DevelopmentCard;
import venusians.data.cards.development.DevelopmentCardList;
import venusians.data.cards.development.KnightCard;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.cards.special.LargestArmyCard;
import venusians.data.chat.Chat;
import venusians.data.chat.HasName;
import venusians.data.chat.Message;

/**
 * Describes the data and possible actions a participant can do while playing the
 * game.
 *
 * Building functions are only describes in the BuildModeController.
 */
public class Player implements HasName {

  private DevelopmentCardList developmentHand = new DevelopmentCardList();
  private String name;
  private Color color;
  private int roadPool = 15;
  private int settlementPool = 5;
  private int cityPool = 4;
  private PlayerResources resources = new PlayerResources();
  private int victoryPoints = 0;
  private int knightsPlayed = 0;
  private ReadOnlyIntegerWrapper victoryPointsWrapper;

  public Player(String name, Color color) {
    this.name = name;
    this.color = color;
    this.victoryPointsWrapper = new ReadOnlyIntegerWrapper();
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public DevelopmentCardList getDevelopmentHand() {
    return developmentHand;
  }

  public ResourceCardMap getResourceHand() {
    return resources.getResourceHand();
  }

  public int getVictoryPoints() {
    return victoryPoints;
  }

  public void setVictoryPoints(int victoryPoints) {
    this.victoryPoints = victoryPoints;
    victoryPointsWrapper.set(victoryPoints);
  }

  public void addVictoryPoints(int addedPoints) {
    victoryPoints += addedPoints;
    victoryPointsWrapper.set(victoryPoints);
  }

  public ReadOnlyIntegerProperty victoryPointsProperty() {
    return victoryPointsWrapper.getReadOnlyProperty();
  }

  public void tradeWith(Player other, TradeRequest tradeRequest) {
    if (!this.resources.contains(tradeRequest.necessaryResources)) {
      throw new RuntimeException(
        "This player does not have necessary resources for this trade."
      );
    } else if (!other.resources.contains(tradeRequest.requestedResources)) {
      throw new RuntimeException(
        "Other player does not have requested resources for this trade."
      );
    }

    this.transferResources(other, tradeRequest.necessaryResources);
    other.transferResources(this, tradeRequest.requestedResources);
  }

  public void say(String text) {
    Message message = new Message(this, text);
    Chat.add(message);
  }

  private void transferResources(Player to, ResourceCardMap request) {
    for (Entry<ResourceCard, Integer> entry : request.entrySet()) {
      ResourceCard card = entry.getKey();
      int value = entry.getValue();

      this.resources.remove(card, value);
      to.resources.add(card, value);
    }
  }

  public boolean hasResourcesForRoad() {
    return roadPool > 0 && resources.contains(Road.BLUEPRINT);
  }

  public boolean hasResourcesForSettlement() {
    return settlementPool > 0 && resources.contains(Settlement.BLUEPRINT);
  }

  public boolean hasResourcesForCity() {
    return cityPool > 0 && resources.contains(City.BLUEPRINT);
  }

  public boolean hasResourcesForDevelopmentCard() {
    return (
      DevelopmentCard.hasCards() &&
      resources.contains(DevelopmentCard.BLUEPRINT)
    );
  }

  public DevelopmentCard pickDevelopmentCard() {
    DevelopmentCard card = DevelopmentCard.pickCard(this);
    resources.remove(DevelopmentCard.BLUEPRINT);
    developmentHand.add(card);
    return card;
  }

  public void useDevelopmentCard(DevelopmentCard card) {
    developmentHand.remove(card);
    DevelopmentCard.returnCard(card);

    if (card instanceof KnightCard) {
      incrementKnightsPlayed();
    }
  }

  public int getKnightsPlayed() {
    return knightsPlayed;
  }

  public int getLongestRoadLength() {
    return RoadRuler.getLongestRoadLengthFor(this);
  }

  private void incrementKnightsPlayed() {
    knightsPlayed++;
    if (
      knightsPlayed >= 3 &&
      knightsPlayed > LargestArmyCard.getMostKnightsPlayed()
    ) {
      LargestArmyCard.setCardOwner(this);
    }
  }
}
