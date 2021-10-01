package venusians.util;

import java.util.HashSet;

/**
 * A simple Event pattern implementation. Listeners are executed out of order.
 */
public class Event<A> {

  private HashSet<EventListener<A>> listeners = new HashSet<EventListener<A>>();

  public void connect(EventListener<A> listener) {
    listeners.add(listener);
  }

  public void disconnect(EventListener<A> listener) {
    listeners.remove(listener);
  }

  public boolean isConnected(EventListener<A> listener) {
    return listeners.contains(listener);
  }

  public void fire(A argument) {
    for (EventListener<A> listener : listeners) {
      listener.onFired(argument);
    }
  }

  public interface EventListener<A> {
    public void onFired(A argument);
  }
}
