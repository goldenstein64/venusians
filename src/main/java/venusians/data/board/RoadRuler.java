package venusians.data.board;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import javafx.util.Pair;
import venusians.data.board.buildable.Road;
import venusians.data.players.Player;

public class RoadRuler {

  private static class RulerState {

    public Player player;
    public HashSet<Road> knownRoads;

    public RulerState(Player currentPlayer, HashSet<Road> knownRoads) {
      this.player = currentPlayer;
      this.knownRoads = knownRoads;
    }

    public boolean roadIsKnownOrNotOwned(Road road) {
      return knownRoads.contains(road) || road.getOwner() != player;
    }
  }

  public static int getLongestRoadLengthFor(Player player) {
    HashSet<Road> allPlayerRoads = new HashSet<>(Board.getRoads());
    for (Iterator<Road> iter = allPlayerRoads.iterator(); iter.hasNext();) {
      Road road = iter.next();
      if (road.getOwner() != player) {
        iter.remove();
      }
    }

    if (allPlayerRoads.isEmpty()) return 0;

    int maxPathLength = 0;
    HashSet<Road> knownRoads = new HashSet<>();
    RulerState state = new RulerState(player, knownRoads);
    while (!allPlayerRoads.isEmpty()) {
      Road firstRoad = allPlayerRoads.iterator().next();

      knownRoads.add(firstRoad);

      int backLength = getLongestHalfLength(firstRoad.getPosition1(), state);
      int frontLength = getLongestHalfLength(firstRoad.getPosition2(), state);
      int pathLength = backLength + frontLength + 1;
      if (pathLength > maxPathLength) {
        maxPathLength = pathLength;
      }

      allPlayerRoads.removeAll(knownRoads);
    }

    return maxPathLength;
  }

  private static int getLongestHalfLength(
    IntPoint firstPosition,
    RulerState state
  ) {
    ArrayDeque<Pair<IntPoint, Integer>> openPoints = new ArrayDeque<>();
    openPoints.addLast(new Pair<>(firstPosition, 0));
    int result = 0;
    while (!openPoints.isEmpty()) {
      Pair<IntPoint, Integer> currentPair = openPoints.removeLast();
      IntPoint currentPosition = currentPair.getKey();
      int currentDistance = currentPair.getValue();

      for (Road road : Board.getRoadsAt(currentPosition)) {
        if (state.roadIsKnownOrNotOwned(road)) continue;

        state.knownRoads.add(road);
        IntPoint position1 = road.getPosition1();
        IntPoint position2 = road.getPosition2();
        IntPoint oppositePosition = currentPosition.equals(position1)
          ? position2
          : position1;
        openPoints.addLast(new Pair<>(oppositePosition, currentDistance + 1));
        if (result < currentDistance + 1) {
          result = currentDistance + 1;
        }
      }
    }

    return result;
  }
}
