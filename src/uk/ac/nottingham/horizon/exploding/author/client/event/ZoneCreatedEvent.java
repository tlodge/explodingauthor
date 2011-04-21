package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ZoneCreatedEvent extends GwtEvent<ZoneCreatedEventHandler> {
	  public static Type<ZoneCreatedEventHandler> TYPE = new Type<ZoneCreatedEventHandler>();
	  private static String id;
	  private static boolean fromMap;
	  private static int type;
	  
	  public ZoneCreatedEvent(String identity, int t, boolean fM) {
		    id = identity;
		    fromMap = fM;
		    setType(t);
	  }
		  
	  public String getId() { return id; }
	  public boolean fromMap() {return fromMap;}
	  
	  
	  @Override
	  public Type<ZoneCreatedEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	  @Override
	  protected void dispatch(ZoneCreatedEventHandler handler) {
	    handler.onZoneCreated(this);
	  }

	public static void setType(int type) {
		ZoneCreatedEvent.type = type;
	}

	public static int getType() {
		return type;
	}
	
}
