package venusians.data.cards.development;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.util.ArrayList;

import javafx.scene.image.Image;
import venusians.data.players.Player;

public abstract class DevelopmentCard {

  private static SecureRandom rng = new SecureRandom();
  
  // TODO: find a better way to define the card pool other than their classes?
  private static ArrayList<Class<? extends DevelopmentCard>> cardPool = new ArrayList<Class<? extends DevelopmentCard>>();
  static {
    // fill the cardPool
    for (int i = 0; i < 14; i++) {
      cardPool.add(KnightCard.class);
    }

    for (int i = 0; i < 2; i++) {
      cardPool.add(MonopolyCard.class);
    }

    for (int i = 0; i < 2; i++) {
      cardPool.add(RoadBuildingCard.class);
    }

    for (int i = 0; i < 5; i++) {
      cardPool.add(VictoryPointCard.class);
    }

    for (int i = 0; i < 2; i++) {
      cardPool.add(YearOfPlentyCard.class);
    }
  }

  public static DevelopmentCard pickRandomCard(Player owner) {
    int choice = rng.nextInt(cardPool.size());
    Class<? extends DevelopmentCard> cardClass = cardPool.remove(choice);
    Constructor<? extends DevelopmentCard> constructor;
    try {
      constructor = cardClass.getConstructor(Player.class);
    } catch (NoSuchMethodException | SecurityException e) {
      throw new RuntimeException(e);
    }

    try {
      return constructor.newInstance(owner);
    } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
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
