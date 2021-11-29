package venusians.data.board;

import java.util.ArrayDeque;
import java.util.HashSet;
import venusians.data.board.buildable.Building;
import venusians.data.board.buildable.Road;
import venusians.data.board.buildable.SuggestedBuildable;
import venusians.data.players.Player;
import venusians.util.Matrix;

/**
 * A class for checking whether suggested buildables affected by a removed road
 * would be unanchored from any real buildables.
 */
public class AnchorChecker {

  private Player owner;
  private Matrix<SuggestedBuildable<? extends Building>> suggestedBuildingMap;
  private Matrix<HashSet<SuggestedBuildable<Road>>> suggestedRoadMap;

  /**
   * Answers whether affected buildables would stay anchored without the passed in road.
   *
   * @param removingRoad The road that would be removed
   * @return whether affected buildables would stay anchored
   */
  public boolean buildablesStayAnchoredWithout(Road removingRoad) {
    return (
      buildingsStayAnchoredWithout(removingRoad) &&
      roadsStayAnchoredWithout(removingRoad)
    );
  }

  private boolean buildingsStayAnchoredWithout(Road removingRoad) {
    return (
      buildingsAtPositionStayAnchoredWithout(
        removingRoad,
        removingRoad.getPosition1()
      ) &&
      buildingsAtPositionStayAnchoredWithout(
        removingRoad,
        removingRoad.getPosition2()
      )
    );
  }

  private boolean buildingsAtPositionStayAnchoredWithout(
    Road removingRoad,
    IntPoint position
  ) {
    SuggestedBuildable<? extends Building> suggestedBuilding = suggestedBuildingMap.get(
      position
    );
    if (suggestedBuilding == null) return true;

    HashSet<Road> allRoads = new HashSet<>();
    HashSet<Road> suggestedRoads = toRealRoads(suggestedRoadMap.get(position));
    HashSet<Road> existingRoads = Board.getRoadsAt(position);

    for (Road road : existingRoads) {
      if (road.getOwner() != owner) {
        existingRoads.remove(road);
      }
    }

    allRoads.addAll(suggestedRoads);
    allRoads.addAll(existingRoads);
    allRoads.remove(removingRoad);

    return !allRoads.isEmpty();
  }

  private boolean roadsStayAnchoredWithout(Road removingRoad) {
    return (
      roadsAtPositionStayAnchoredWithout(
        removingRoad,
        removingRoad.getPosition1()
      ) &&
      roadsAtPositionStayAnchoredWithout(
        removingRoad,
        removingRoad.getPosition2()
      )
    );
  }

  private boolean roadsAtPositionStayAnchoredWithout(
    Road removingRoad,
    IntPoint position
  ) {
    HashSet<Road> knownRoads = new HashSet<>();
    knownRoads.add(removingRoad);

    ArrayDeque<IntPoint> openPoints = new ArrayDeque<>();
    openPoints.addLast(position);

    boolean foundNewSuggestedRoads = false;
    while (!openPoints.isEmpty()) {
      IntPoint currentPosition = openPoints.removeLast();
      if (Board.positionHasRoad(owner, currentPosition)) {
        return true;
      }

      for (SuggestedBuildable<Road> suggestedRoad : suggestedRoadMap.get(
        currentPosition
      )) {
        Road road = suggestedRoad.getBuildable();
        if (knownRoads.contains(road)) continue;

        IntPoint position1 = road.getPosition1();
        IntPoint position2 = road.getPosition2();
        IntPoint oppositePosition = currentPosition.equals(position1)
          ? position2
          : position1;

        foundNewSuggestedRoads = true;
        openPoints.addLast(oppositePosition);
      }
    }

    return !foundNewSuggestedRoads;
  }

  private HashSet<Road> toRealRoads(
    HashSet<SuggestedBuildable<Road>> suggestedRoads
  ) {
    HashSet<Road> result = new HashSet<>();
    for (SuggestedBuildable<Road> suggestedRoad : suggestedRoads) {
      result.add(suggestedRoad.getBuildable());
    }

    return result;
  }

  public void setSuggestedBuildingMap(
    Matrix<SuggestedBuildable<? extends Building>> suggestedBuildingMap
  ) {
    this.suggestedBuildingMap = suggestedBuildingMap;
  }

  public void setSuggestedRoadMap(
    Matrix<HashSet<SuggestedBuildable<Road>>> suggestedRoadMap
  ) {
    this.suggestedRoadMap = suggestedRoadMap;
  }

  public void setOwner(Player owner) {
    this.owner = owner;
  }
}
