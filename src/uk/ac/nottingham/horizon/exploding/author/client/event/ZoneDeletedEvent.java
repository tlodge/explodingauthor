package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class ZoneDeletedEvent  extends GwtEvent<ZoneDeletedEventHandler>{
	
	 public static Type<ZoneDeletedEventHandler> TYPE = new Type<ZoneDeletedEventHandler>();
	  private static String id;
	  
	  public ZoneDeletedEvent(String i) {
		    id = i;
	  }
		  
	  public String getId() { return id; }
	  
	  @Override
	  public Type<ZoneDeletedEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	  @Override
	  protected void dispatch(ZoneDeletedEventHandler handler) {
	    handler.onZoneDeleted(this);
	  }
}
