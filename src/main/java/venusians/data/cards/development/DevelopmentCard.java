package venusians.data.cards.development;

import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javafx.scene.image.Image;
import venusians.data.cards.HasCardImage;
import venusians.data.players.Player;

public abstract class DevelopmentCard implements HasCardImage {

  private static SecureRandom rng = new SecureRandom();

  private static enum CardEnum {
    KNIGHT, MONOPOLY, ROAD_BUILDING, VICTORY_POINT, YEAR_OF_PLENTY;

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
      }else if (card instanceof VictoryPointCard) {
        return VICTORY_POINT;
      } else if (card instanceof YearOfPlentyCard) {
        return YEAR_OF_PLENTY;
      } else {
        throw new RuntimeException("Card class is invalid");
      }
    }
  }
  
  // TODO: find a better way to define the card pool other than their classes?
  private static ArrayList<CardEnum> livePool = new ArrayList<CardEnum>();
  static {
    // fill the cardPool
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

  private static ArrayList<CardEnum> deadPool = new ArrayList<CardEnum>();

  public static DevelopmentCard pickCard(Player owner) {
    if (livePool.size() == 0) {
      reshuffleDeadPile();
    }

    int choice = rng.nextInt(livePool.size());
    CardEnum cardEnum = livePool.remove(choice);
    return cardEnum.asInstance(owner);
  }

  public static void returnCard(DevelopmentCard card) {
    CardEnum cardEnum = CardEnum.valueOf(card);
    deadPool.add(cardEnum);
  }

  private static void reshuffleDeadPile() {
    livePool.addAll(deadPool);
    deadPool.clear();
  }

  protected static Image loadImage(String filename) {
    Image tempCardImage = null;
    try {
      tempCardImage =
        new Image(
          DevelopmentCard.class.getResource(filename).toURI().toString()
        );
    } catch (URISyntaxException e) {
      e.printStackTrace();
    }
    return tempCardImage;
  }

  public abstract String getName();

  public abstract String getDescription();

  public abstract Image getCardImage();

  public abstract void apply();
}
