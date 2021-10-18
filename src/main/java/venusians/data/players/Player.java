package venusians.data.players;

import java.util.ArrayList;
import java.util.HashMap;
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
import venusians.data.cards.resource.ResourceCard;
import venusians.data.chat.Chat;
import venusians.data.chat.Message;

/**
 * Describes the data and possible actions a participant has while playing the
 * game.
 */
public class Player {

  private ArrayList<DevelopmentCard> developmentHand = new ArrayList<DevelopmentCard>();
  private HashMap<ResourceCard, Integer> resourceHand = new HashMap<ResourceCard, Integer>();
  private String name;
  private Color color;
  private int victoryPoints = 0;
  private ReadOnlyIntegerWrapper victoryPointsWrapper;

  public Player(String name, Color color) {
    this.name = name;
    this.color = color;
    for (ResourceCard card : ResourceCard.values()) {
      resourceHand.put(card, 0);
    }
    this.victoryPointsWrapper =
      new ReadOnlyIntegerWrapper(this, "victoryPoints");
  }

  public String getName() {
    return name;
  }

  public Color getColor() {
    return color;
  }

  public DevelopmentCard[] getDevelopmentHand() {
    return (DevelopmentCard[]) developmentHand.toArray();
  }

  public ResourceCard[] getResourceHand() {
    ArrayList<ResourceCard> result = new ArrayList<ResourceCard>();
    for (Entry<ResourceCard, Integer> pair : resourceHand.entrySet()) {
      ResourceCard card = pair.getKey();
      int count = pair.getValue();
      for (int i = 0; i < count; i++) {
        result.add(card);
      }
    }

    return (ResourceCard[]) result.toArray();
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

  public void startTurn() {
    // roll dice

    // put gui in active state (for this player)

    // the gui should be the part that calls endTurn()
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

  public void buildRoad(IntPoint position1, IntPoint position2) {
    useResources(Road.getBlueprint());
    Road newRoad = new Road(this, position1, position2);
    Board.addBuildable(newRoad);
  }

  public void buildSettlement(IntPoint position) {
    throwIfPositionIsntConnectedByRoad(position);

    useResources(Settlement.getBlueprint());
    buildStartingSettlement(position);
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
      throw new IllegalArgumentException(
        "This position is not connected by a road."
      );
    }
  }

  public void buildStartingSettlement(IntPoint position) {
    Settlement newSettlement = new Settlement(this, position);
    Board.addBuildable(newSettlement);
    setVictoryPoints(victoryPoints + 1);
  }

  public void upgradeSettlement(Settlement settlement) {
    throwIfSettlementIsntOnBoard(settlement);

    useResources(City.getBlueprint());
    City newCity = new City(this, settlement.getPosition());
    Board.upgradeBuildable(settlement, newCity);
    setVictoryPoints(victoryPoints + 1);
  }

  private void throwIfSettlementIsntOnBoard(Settlement settlement) {
    if (!Board.hasBuildable(settlement)) {
      throw new IllegalArgumentException("This settlement is not on the board");
    }
  }

  private void transferResources(
    Player to,
    HashMap<ResourceCard, Integer> resources
  ) {
    for (Entry<ResourceCard, Integer> entry : resources.entrySet()) {
      ResourceCard key = entry.getKey();
      int value = entry.getValue();

      int oldOwnerValue = resourceHand.get(key);
      resourceHand.put(key, oldOwnerValue - value);

      int oldOtherValue = to.resourceHand.get(key);
      to.resourceHand.put(key, oldOtherValue + value);
    }
  }

  private void useResources(HashMap<ResourceCard, Integer> resources) {
    throwIfCantUseResources(resources);
    removeResourcesFromHand(resources);
  }

  private void removeResourcesFromHand(
    HashMap<ResourceCard, Integer> resources
  ) {
    for (Entry<ResourceCard, Integer> entry : resources.entrySet()) {
      ResourceCard key = entry.getKey();
      int oldValue = resourceHand.get(key);
      resourceHand.put(key, oldValue - entry.getValue());
    }
  }

  private void throwIfCantUseResources(
    HashMap<ResourceCard, Integer> resources
  ) {
    if (!hasResources(resources)) {
      throw new RuntimeException(
        "This Buildable does not have sufficient resources."
      );
    }
  }

  public boolean hasResources(HashMap<ResourceCard, Integer> resources) {
    for (Entry<ResourceCard, Integer> entry : resources.entrySet()) {
      if (hasResourcesFromEntry(entry)) {
        return false;
      }
    }
    return true;
  }

  private boolean hasResourcesFromEntry(Entry<ResourceCard, Integer> entry) {
    return resourceHand.get(entry.getKey()) < entry.getValue();
  }

  public void pickDevelopmentCard() {
    DevelopmentCard card = DevelopmentCard.pickRandomCard(this);
    developmentHand.add(card);
  }

  public void useDevelopmentCard(DevelopmentCard card) {
    card.apply();
    developmentHand.remove(card);
    // place the used card in the dead pool
  }
}
