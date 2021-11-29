package venusians.data.cards.development;

import java.security.SecureRandom;
import java.util.ArrayList;
import javafx.scene.image.Image;
import venusians.data.cards.HasCard;
import venusians.data.cards.resource.ResourceCard;
import venusians.data.cards.resource.ResourceCardMap;
import venusians.data.players.Player;

public abstract class DevelopmentCard implements HasCard {

  public static final ResourceCardMap BLUEPRINT = new ResourceCardMap();

  static {
    BLUEPRINT.add(ResourceCard.WHEAT);
    BLUEPRINT.add(ResourceCard.WOOL);
    BLUEPRINT.add(ResourceCard.ORE);
  }

  private static SecureRandom rng = new SecureRandom();

  private static enum CardEnum {
    KNIGHT,
    MONOPOLY,
    ROAD_BUILDING,
    VICTORY_POINT,
    YEAR_OF_PLENTY;

    public DevelopmentCard asInstance(Player owner) {
      switch (this) {
        case KNIGHT:
          return new KnightCard(owner);
        case MONOPOLY:
          return new MonopolyCard(owner);
        case ROAD_BUILDING:
          return new RoadBuildingCard(owner);
        case VICTORY_POINT:
          return new VictoryPointCard(owner);
        case YEAR_OF_PLENTY:
          return new YearOfPlentyCard(owner);
        default:
          throw new RuntimeException("Enum is invalid");
      }
    }

    public static CardEnum valueOf(DevelopmentCard card) {
      if (card instanceof KnightCard) {
        return KNIGHT;
      } else if (card instanceof MonopolyCard) {
        return MONOPOLY;
      } else if (card instanceof RoadBuildingCard) {
        return ROAD_BUILDING;
      } else if (card instanceof VictoryPointCard) {
        return VICTORY_POINT;
      } else if (card instanceof YearOfPlentyCard) {
        return YEAR_OF_PLENTY;
      } else {
        throw new RuntimeException("Card class is invalid");
      }
    }
  }

  private static ArrayList<CardEnum> livePool = new ArrayList<CardEnum>();
  private static ArrayList<CardEnum> deadPool = new ArrayList<CardEnum>();

  public static void startGame() {
    deadPool.clear();
    livePool.clear();
    for (int i = 0; i < 14; i++) {
      livePool.add(CardEnum.KNIGHT);
    }

    for (int i = 0; i < 2; i++) {
      livePool.add(CardEnum.MONOPOLY);
    }

    for (int i = 0; i < 2; i++) {
      livePool.add(CardEnum.ROAD_BUILDING);
    }

    for (int i = 0; i < 5; i++) {
      livePool.add(CardEnum.VICTORY_POINT);
    }

    for (int i = 0; i < 2; i++) {
      livePool.add(CardEnum.YEAR_OF_PLENTY);
    }
  }

  public static DevelopmentCard pickCard(Player owner) {
    if (livePool.isEmpty()) {
      reshuffleDeadPool();
    }

    int choice = rng.nextInt(livePool.size());
    CardEnum cardEnum = livePool.remove(choice);
    return cardEnum.asInstance(owner);
  }

  public static void returnCard(DevelopmentCard card) {
    CardEnum cardEnum = CardEnum.valueOf(card);
    deadPool.add(cardEnum);
  }

  public static void returnCards(DevelopmentCard... cards) {
    for (DevelopmentCard card : cards) {
      returnCard(card);
    }
  }

  private static void reshuffleDeadPool() {
    livePool.addAll(deadPool);
    deadPool.clear();
  }

  public static boolean hasCards() {
    return livePool.size() > 0 || deadPool.size() > 0;
  }

  protected Player owner;

  public DevelopmentCard(Player owner) {
    this.owner = owner;
  }

  public Player getOwner() {
    return owner;
  }

  public abstract String getName();

  public abstract String getDescription();

  @Override
  public abstract Image getCardImage();
}
