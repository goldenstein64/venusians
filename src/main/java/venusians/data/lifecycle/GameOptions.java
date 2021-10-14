package venusians.data.lifecycle;

import java.util.ArrayList;
import java.util.List;
import venusians.data.extensions.Extension;

/**
 * Contains all the information required to start a game of Venusians™
 */
public class GameOptions {

  /** Contains all participants of the next game. */
  public List<PlayerProfile> profiles = new ArrayList<PlayerProfile>();

  /** Contains all extensions used in the next game. */
  public List<Extension> extensions = new ArrayList<Extension>();

  /** Describes whether tile positions on the map should be randomized. */
  public boolean areTilePositionsRandomized = false;

  /** Describes whether roll values on top of the map should be randomized. */
  public boolean areRollValuesRandomized = false;
}
