package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ZoneSelectedEvent extends GwtEvent<ZoneSelectedEventHandler> {
	  public static Type<ZoneSelectedEventHandler> TYPE = new Type<ZoneSelectedEventHandler>();
	  private static String id;
	  
	  public ZoneSelectedEvent(String i) {
		    id = i;
	  }
		  
	  public String getId() { return id; }
	  
	  @Override
	  public Type<ZoneSelectedEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	  @Override
	  protected void dispatch(ZoneSelectedEventHandler handler) {
	    handler.onZoneSelected(this);
	  }
	
}
