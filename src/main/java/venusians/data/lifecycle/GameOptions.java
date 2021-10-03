package venusians.data.lifecycle;

import java.util.ArrayList;
import java.util.List;
import venusians.data.extensions.Extension;

public class GameOptions {

  public List<PlayerProfile> profiles = new ArrayList<PlayerProfile>();
  public List<Extension> extensions = new ArrayList<Extension>();
  public boolean randomizeTilePositions = false;
  public boolean randomizeRollValues = false;
}
