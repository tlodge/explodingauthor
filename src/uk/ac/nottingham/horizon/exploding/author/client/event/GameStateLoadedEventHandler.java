package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface GameStateLoadedEventHandler extends EventHandler{

	 void onGameStateLoaded(GameStateLoadedEvent event);
} 