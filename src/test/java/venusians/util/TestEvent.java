package venusians.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

public class TestEvent {

  @Test
  void eventGetsConstructed() {
    Event<String> stringEvent = new Event<String>();
    assertNotNull(stringEvent);
  }

  @Test
  void eventCanFire() {
    Event<String> stringEvent = new Event<String>();
    stringEvent.fire("Hello!");
  }

  @Test
  void eventCanBeListenedTo() {
    Event<String> stringEvent = new Event<String>();
    Event.Listener<String> connection = stringEvent.connect(
      (String value) -> {
        System.out.printf("RECEIVED: %s%n", value);
      }
    );

    assertNotNull(connection);
  }

  @Test
  void eventCanBeListenedToTwiceAndFired() {
    Event<Integer> intEvent = new Event<Integer>();

    int[] obj = new int[] { -1 };

    intEvent.connect(
      (Integer value) -> {
        obj[0] += value;
      }
    );

    intEvent.connect(
      (Integer value) -> {
        obj[0] += value;
      }
    );

    intEvent.fire(3);

    assertEquals(obj[0], 5);
  }

  void eventCanCheckWhetherListenerIsConnected() {
    Event<String> stringEvent = new Event<String>();
    Event.Listener<String> connection = stringEvent.connect(
      (String value) -> {
        System.out.printf("RECEIVED: %s%n", value);
      }
    );

    assertTrue(stringEvent.isConnected(connection));
  }

  @Test
  void eventCanBeDisconnected() {
    Event<String> stringEvent = new Event<String>();
    Event.Listener<String> connection = stringEvent.connect(
      (String value) -> {
        System.out.printf("RECEIVED: %s%n", value);
      }
    );

    assertTrue(stringEvent.isConnected(connection));

    stringEvent.disconnect(connection);

    assertFalse(stringEvent.isConnected(connection));
  }

  @Test
  void eventCanBeListenedToAndFired() {
    Event<Integer> intEvent = new Event<Integer>();

    int[] obj = new int[] { -1 };

    Event.Listener<Integer> connection = intEvent.connect(
      (Integer value) -> {
        obj[0] = value;
      }
    );

    intEvent.fire(5);

    assertEquals(obj[0], 5);
  }

  @Test
  void eventConnectedThenDisconnected() {
    Event<Integer> intEvent = new Event<Integer>();

    int[] obj = new int[] { -1 };

    Event.Listener<Integer> connection = intEvent.connect(
      (Integer value) -> {
        obj[0] = value;
      }
    );

    intEvent.fire(1);

    intEvent.disconnect(connection);

    intEvent.fire(3);

    assertEquals(obj[0], 1);
  }
}
