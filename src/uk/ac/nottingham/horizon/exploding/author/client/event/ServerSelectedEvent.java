package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ServerSelectedEvent extends GwtEvent<ServerSelectedEventHandler>{
	
	public static Type<ServerSelectedEventHandler> TYPE = new Type<ServerSelectedEventHandler>();
	private String  serverName;
	
	public ServerSelectedEvent(String sn){
		serverName = sn;
	}
	
	public String getServerName(){
		return serverName;
	}
	
	@Override
	public Type<ServerSelectedEventHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ServerSelectedEventHandler handler) {
		// TODO Auto-generated method stub
		handler.onServerSelected(this);
	}
}