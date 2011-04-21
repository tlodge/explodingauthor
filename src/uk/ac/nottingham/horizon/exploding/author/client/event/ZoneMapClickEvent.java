package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.maps.client.geom.LatLng;


public class ZoneMapClickEvent extends GwtEvent<ZoneMapClickedEventHandler>{
	  
	  public static Type<ZoneMapClickedEventHandler> TYPE = new Type<ZoneMapClickedEventHandler>();
	  
	  LatLng point;
	  
	  public ZoneMapClickEvent(LatLng pointclicked) {
	    this.point = pointclicked;
	  }
	  
	  public LatLng getPoint() { return point; }
	  

	  @Override
	  public Type<ZoneMapClickedEventHandler> getAssociatedType() {
	    return TYPE;
	  }

	  @Override
	  protected void dispatch(ZoneMapClickedEventHandler handler) {
	    handler.onContactUpdated(this);
	  }
}
