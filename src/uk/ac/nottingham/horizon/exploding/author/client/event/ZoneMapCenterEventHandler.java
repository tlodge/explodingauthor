package uk.ac.nottingham.horizon.exploding.author.client.event;


import com.google.gwt.event.shared.EventHandler;

public interface ZoneMapCenterEventHandler extends EventHandler{
  void onCenterRequest(ZoneMapCenterEvent event);
}
