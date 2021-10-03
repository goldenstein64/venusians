package venusians.util;

import java.util.HashSet;

/**
 * A simple Event pattern implementation. Listeners are executed out of order.
 */
public class Event<A> {

  private HashSet<Listener<A>> listeners = new HashSet<Listener<A>>();

  public Listener<A> connect(Listener<A> listener) {
    listeners.add(listener);
    return listener;
  }

  public void disconnect(Listener<A> listener) {
    listeners.remove(listener);
  }

  public boolean isConnected(Listener<A> listener) {
    return listeners.contains(listener);
  }

  public void fire(A argument) {
    for (Listener<A> listener : listeners) {
      listener.onFired(argument);
    }
  }

  public interface Listener<A> {
    public void onFired(A argument);
  }
}
