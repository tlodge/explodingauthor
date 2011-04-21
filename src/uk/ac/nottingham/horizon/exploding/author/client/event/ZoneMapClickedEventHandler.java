package uk.ac.nottingham.horizon.exploding.author.client.event;


import com.google.gwt.event.shared.EventHandler;

public interface ZoneMapClickedEventHandler extends EventHandler{
  void onContactUpdated(ZoneMapClickEvent event);
}
