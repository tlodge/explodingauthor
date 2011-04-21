package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.maps.client.geom.LatLng;

public class ZoneMapCenterEvent extends GwtEvent<ZoneMapCenterEventHandler>{
	  
	  public static Type<ZoneMapCenterEventHandler> TYPE = new Type<ZoneMapCenterEventHandler>();
	  
	  LatLng point;
	  
	  public ZoneMapCenterEvent(LatLng point) {
	    this.point = point;
	  }
	  
	  public LatLng getPoint() { return point; }
	  

	  @Override
	  public Type<ZoneMapCenterEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	  @Override
	  protected void dispatch(ZoneMapCenterEventHandler handler) {
	    handler.onCenterRequest(this);
	  }
}

