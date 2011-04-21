package uk.ac.nottingham.horizon.exploding.author.client.event;
import com.google.gwt.event.shared.EventHandler;

public interface ServerSelectedEventHandler extends EventHandler {
	void onServerSelected(ServerSelectedEvent event);
}