package venusians.data.lifecycle;

import javafx.scene.paint.Color;

/**
 * Contains data pertaining to how to set up each player from setup
 */
public class PlayerProfile {

  private static final Color[] defaultColors = new Color[] {
    Color.RED,
    Color.WHITE,
    Color.BLUE,
    Color.ORANGE,
    Color.GREEN,
    Color.BROWN,
  };

  public String name;
  public Color color;

  public PlayerProfile() {
    this("", 0);
  }

  public PlayerProfile(int order) {
    this("", order);
  }

  public PlayerProfile(String name, int order) {
    this.name = name;
    this.color = defaultColors[order];
  }

  public PlayerProfile(Color color) {
    this("", color);
  }

  public PlayerProfile(String name, Color color) {
    this.name = name;
    this.color = color;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Color getColor() {
    return color;
  }

  public void setColor(Color color) {
    this.color = color;
  }
}
