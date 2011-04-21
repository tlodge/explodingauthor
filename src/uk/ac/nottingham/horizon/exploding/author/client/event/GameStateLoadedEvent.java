package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class GameStateLoadedEvent extends GwtEvent<GameStateLoadedEventHandler>{
	
	public static Type<GameStateLoadedEventHandler> TYPE = new Type<GameStateLoadedEventHandler>();

	@Override
	protected void dispatch(GameStateLoadedEventHandler handler) {
		handler.onGameStateLoaded(this);
	}

	@Override
	public Type<GameStateLoadedEventHandler> getAssociatedType() {
		return TYPE;
	}

}
