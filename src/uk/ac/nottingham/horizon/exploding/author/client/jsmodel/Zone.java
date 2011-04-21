package uk.ac.nottingham.horizon.exploding.author.client.jsmodel;

import com.google.gwt.core.client.JavaScriptObject;

public class Zone extends JavaScriptObject{
	
	protected Zone(){}
	
	public final native String getID( ) /*-{return this.ID; }-*/;
	public final native void setID(String value) /*-{this.ID = value; }-*/;
	public final native String getContentGroupID() /*-{return this.contentGroupID; }-*/;
	public final native void setContentGroupID(String value) /*-{this.contentGroupID = value; }-*/;
	public final native String getOrgId() /*-{return this.orgId; }-*/;
	public final native String getName() /*-{return this.name; }-*/;
	public final native String getRef() /*-{return this.ref; }-*/;
	public final native String getRadius() /*-{return this.radius; }-*/;
	
	//public final native JsArray<JsArray<LongLatElevation>> getCoordinates() /*-{return this.coordinates;}-*/;
	//public final native void setCoordinates(JsArray<JsArray<LongLatElevation>> coordinates) /*-{this.coordinates = coordinates; }-*/;
	
	
}
