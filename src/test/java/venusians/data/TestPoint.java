package venusians.data;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestPoint {

  @Test
  void makePoint() {
    Point some = new Point(0, 0);
    assertTrue(some instanceof Point);
  }

  @Test
  void addPoint() {
    Point some = new Point(1, 2);
    Point other = new Point(3, 4);

    Point result = some.plus(other);

    assertEquals(result.x, 4);
    assertEquals(result.y, 6);
  }

  @Test
  void addMultiplePoints() {
    Point some = new Point(1, 2);
    Point other1 = new Point(3, 4);
    Point other2 = new Point(5, 6);
    Point other3 = new Point(7, 8);

    Point result = some.plus(other1).plus(other2).plus(other3);

    assertEquals(result.x, 16);
    assertEquals(result.y, 20);
  }

  @Test
  void subtractAPoint() {
    Point some = new Point(2, 3);
    Point other = new Point(1, 2);

    Point result = some.minus(other);

    assertEquals(result.x, 1);
    assertEquals(result.y, 1);
  }

  @Test
  void makePointNegative() {
    Point some = new Point(-3, 7);

    Point result = some.negative();

    assertEquals(result.x, 3);
    assertEquals(result.y, -7);
  }

  @Test
  void multiplyPointByPoint() {
    Point some = new Point(2, 5);
    Point other = new Point(3, 6);

    Point result = some.times(other);

    assertEquals(result.x, 6);
    assertEquals(result.y, 30);
  }

  @Test
  void multiplyPointByScalar() {
    Point some = new Point(8, 5);

    Point result = some.times(3);

    assertEquals(result.x, 24);
    assertEquals(result.y, 15);
  }

  @Test
  void multiplyPointByMultiplePoints() {
    Point some = new Point(2, 3);
    Point other1 = new Point(3, 5);
    Point other2 = new Point(5, 8);
    Point other3 = new Point(8, 13);

    Point result = some.times(other1).times(other2).times(other3);

    assertEquals(result.x, 240);
    assertEquals(result.y, 1560);
  }
}
