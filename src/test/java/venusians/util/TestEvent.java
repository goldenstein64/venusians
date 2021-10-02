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
    Event.EventListener<String> connection = stringEvent.connect(
      (String value) -> {
        System.out.printf("RECEIVED: %s%n", value);
      }
    );

    assertNotNull(connection);
  }

  void eventCanCheckWhetherListenerIsConnected() {
    Event<String> stringEvent = new Event<String>();
    Event.EventListener<String> connection = stringEvent.connect(
      (String value) -> {
        System.out.printf("RECEIVED: %s%n", value);
      }
    );

    assertTrue(stringEvent.isConnected(connection));
  }

  @Test
  void eventCanBeDisconnected() {
    Event<String> stringEvent = new Event<String>();
    Event.EventListener<String> connection = stringEvent.connect(
      (String value) -> {
        System.out.printf("RECEIVED: %s%n", value);
      }
    );

    stringEvent.disconnect(connection);

    assertFalse(stringEvent.isConnected(connection));
  }

  @Test
  void eventCanBeListenedToAndFired() {
    Event<Integer> stringEvent = new Event<Integer>();

    int[] obj = new int[] { -1 };

    Event.EventListener<Integer> connection = stringEvent.connect(
      (Integer value) -> {
        obj[0] = value;
      }
    );

    stringEvent.connect(connection);

    stringEvent.fire(5);

    assertEquals(obj[0], 5);
  }
}
