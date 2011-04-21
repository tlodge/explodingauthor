package uk.ac.nottingham.horizon.exploding.author.client.jsmodel;

import com.google.gwt.core.client.JavaScriptObject;

public class LatLngElevation extends JavaScriptObject{
	
	protected LatLngElevation(){}
	
	public final native double getLatitude() /*-{return this.latitude; }-*/;
	public final native double getLongitude() /*-{return this.longitude; }-*/;
	public final native double getElevation() /*-{return this.elevation; }-*/;

}
