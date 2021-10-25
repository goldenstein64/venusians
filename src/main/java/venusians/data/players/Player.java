package venusians.data.players;

import java.util.Map.Entry;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.scene.paint.Color;
import venusians.data.board.Board;
import venusians.data.board.IntPoint;
import venusians.data.board.buildable.Buildable;
import venusians.data.board.buildable.City;
import venusians.data.board.buildable.Road;
import venusians.data.board.buildable.Settlement;
import venusians.data.cards.TradeRequest;
import venusians.data.cards.development.DevelopmentCard;
import venusians.data.cards.development.DevelopmentCardList;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.chat.Chat;
import venusians.data.chat.Message;

/**
 * Describes the data and possible actions a participant has while playing the
 * game.
 */
public class Player {

  private DevelopmentCardList developmentHand = new DevelopmentCardList();
  private String name;
  private Color color;
  private int roadPool = 15;
  private int settlementPool = 5;
  private int cityPool = 4;
  private PlayerResources resources = new PlayerResources();
  private int victoryPoints = 0;
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

  public void receiveResource(ResourceCard resourceCard) {
    resources.addResources(resourceCard, 1);
  }

  public void receiveResources(ResourceCard resourceCard, int count) {
    resources.addResources(resourceCard, count);
  }

  public void receiveResources(ResourceCardMap blueprint) {
    resources.getResourceHand().add(blueprint);
  }

  public void removeResources(ResourceCardMap blueprint) {
    resources.getResourceHand().remove(blueprint);
  }

  public ResourceCardMap getResourceHand() {
    return this.resources.getResourceHand();
  }

  public int getVictoryPoints() {
    return victoryPoints;
  }

  public void setVictoryPoints(int victoryPoints) {
    this.victoryPoints = victoryPoints;
    victoryPointsWrapper.set(victoryPoints);
  }

  public void incrementVictoryPoints() {
    victoryPoints += 1;
    victoryPointsWrapper.set(victoryPoints);
  }

  public ReadOnlyIntegerProperty victoryPointsProperty() {
    return victoryPointsWrapper.getReadOnlyProperty();
  }

  public void endTurn() {
    Players.nextTurn();
  }

  public void tradeWith(Player other, TradeRequest tradeRequest) {
    if (!hasResources(tradeRequest.necessaryResources)) {
      throw new RuntimeException(
        "This player does not have necessary resources for this trade."
      );
    } else if (!other.hasResources(tradeRequest.requestedResources)) {
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

  public Road buildRoad(IntPoint position1, IntPoint position2) {
    if (roadPool <= 0) {
      throw new RuntimeException("Road pool is empty");
    }
    roadPool -= 1;

    this.resources.removeResources(Road.getBlueprint());
    Road newRoad = new Road(this, position1, position2);
    Board.addBuildable(newRoad);

    return newRoad;
  }

  public Settlement buildSettlement(IntPoint position) {
    throwIfPositionIsntConnectedByRoad(position);

    if (settlementPool <= 0) {
      throw new RuntimeException("Settlment pool is empty");
    }
    settlementPool -= 1;

    this.resources.removeResources(Settlement.getBlueprint());
    return buildStartingSettlement(position);
  }

  public Settlement buildStartingSettlement(IntPoint position) {
    Settlement newSettlement = new Settlement(this, position);
    Board.addBuildable(newSettlement);
    setVictoryPoints(victoryPoints + 1);

    return newSettlement;
  }

  public City upgradeSettlement(Settlement settlement) {
    throwIfSettlementIsntOnBoard(settlement);
    if (cityPool <= 0) {
      throw new RuntimeException("City pool is empty");
    }
    cityPool -= 1;
    settlementPool += 1;

    this.resources.removeResources(City.getBlueprint());
    City newCity = new City(this, settlement.getPosition());
    Board.upgradeBuildable(settlement, newCity);
    setVictoryPoints(victoryPoints + 1);

    return newCity;
  }

  private void throwIfPositionIsntConnectedByRoad(IntPoint position) {
    boolean hasConnection = false;
    for (Buildable buildable : Board.getBuildables()) {
      if (buildable.getOwner() == this && buildable instanceof Road) {
        Road road = (Road) buildable;
        if (
          road.getPosition1().equals(position) ||
          road.getPosition2().equals(position)
        ) {
          hasConnection = true;
          break;
        }
      }
    }
    if (!hasConnection) {
      throw new IllegalArgumentException("This position is not connected by a road.");
    }
  }

  private void throwIfSettlementIsntOnBoard(Settlement settlement) {
    if (!Board.hasBuildable(settlement)) {
      throw new IllegalArgumentException("This settlement is not on the board");
    }
  }

  private void transferResources(
    Player to,
    ResourceCardMap request
  ) {
    for (Entry<ResourceCard, Integer> entry : request.entrySet()) {
      ResourceCard card = entry.getKey();
      int value = entry.getValue();

      this.resources.removeResources(card, value);
      to.resources.addResources(card, value);
    }
  }

  public boolean hasResources(ResourceCardMap request) {
    return this.resources.hasResources(request);
  }

  public void pickDevelopmentCard() {
    DevelopmentCard card = DevelopmentCard.pickCard(this);
    developmentHand.add(card);
  }

  public void useDevelopmentCard(DevelopmentCard card) {
    card.apply();
    developmentHand.remove(card);
    DevelopmentCard.returnCard(card);
  }
}
