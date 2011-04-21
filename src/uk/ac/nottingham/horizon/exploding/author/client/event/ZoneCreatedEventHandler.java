package uk.ac.nottingham.horizon.exploding.author.client.event;


import com.google.gwt.event.shared.EventHandler;

public interface ZoneCreatedEventHandler extends EventHandler {
  void onZoneCreated(ZoneCreatedEvent event);
}
