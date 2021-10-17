package venusians.data;

import java.net.URISyntaxException;
import java.security.SecureRandom;
import javafx.scene.image.Image;

/**
 * Defines the logical behavior for rolling a collection of dice.
 */
public class DiceRoll {

  private static SecureRandom rng = new SecureRandom();

  private static Image[] diceImages = new Image[] {
    loadImage("diceFace1.png"),
    loadImage("diceFace2.png"),
    loadImage("diceFace3.png"),
    loadImage("diceFace4.png"),
    loadImage("diceFace5.png"),
    loadImage("diceFace6.png"),
  };

  private static Image loadImage(String filename) {
    try {
      return new Image(DiceRoll.class.getResource(filename).toURI().toString());
    } catch (URISyntaxException e) {
      throw new RuntimeException(e.getReason(), e);
    }
  }

  public int[] values;
  public int totalValue = 0;

  public DiceRoll(int valueCount) {
    values = new int[valueCount];

    for (int i = 0; i < valueCount; i++) {
      int value = rng.nextInt(6) + 1;
      values[i] = value;
      totalValue += value;
    }
  }

  public static Image getImage(int value) {
    return diceImages[value - 1];
  }
}
