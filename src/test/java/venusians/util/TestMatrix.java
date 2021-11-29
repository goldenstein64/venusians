package venusians.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class TestMatrix {

  @Test
  void startsEmpty() {
    Matrix<String> matrix = new Matrix<>(10, 10);

    assertTrue(matrix.isEmpty());
  }

  @Test
  void notEmptyAfterElementIsAdded() {
    Matrix<String> matrix = new Matrix<>(10, 10);

    matrix.put(0, 0, "hello");

    assertFalse(matrix.isEmpty());
  }

  @Test
  void elementCanBePlacedInMaxBounds() {
    Matrix<String> matrix = new Matrix<>(10, 10);

    matrix.put(9, 9, "world");

    assertFalse(matrix.isEmpty());
  }

  @Test
  void elementCannotBePlacedOutOfMaxBounds() {
    Matrix<String> matrix = new Matrix<>(10, 10);

    assertThrows(
      IndexOutOfBoundsException.class,
      () -> matrix.put(10, 10, "world")
    );
  }

  @Test
  void elementCannotBePlacedOutOfMinBounds() {
    Matrix<String> matrix = new Matrix<>(10, 10);

    assertThrows(
      IndexOutOfBoundsException.class,
      () -> matrix.put(-1, -1, "world")
    );
  }

  @Test
  void elementCanBeRemovedFromMatrix() {
    Matrix<String> matrix = new Matrix<>(10, 10);

    assertEquals(matrix.get(6, 5), null);

    matrix.put(6, 5, "hello");

    assertEquals(matrix.get(6, 5), "hello");

    matrix.remove(6, 5);

    assertEquals(matrix.get(6, 5), null);
  }

  @Test
  void elementCanBeRetrievedFromMatrix() {
    Matrix<String> matrix = new Matrix<>(10, 10);

    matrix.put(6, 4, "foo");

    assertEquals(matrix.get(6, 4), "foo");
  }
}
