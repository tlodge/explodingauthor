package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class GameStateLoadEvent extends GwtEvent<GameStateLoadEventHandler>{
	
	public static Type<GameStateLoadEventHandler> TYPE = new Type<GameStateLoadEventHandler>();

	@Override
	public Type<GameStateLoadEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(GameStateLoadEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onGameStateLoad(this);
	}

}
