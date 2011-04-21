package uk.ac.nottingham.horizon.exploding.author.client.jsmodel;

import com.google.gwt.core.client.JavaScriptObject;


public class ContentGroup extends JavaScriptObject{
	protected ContentGroup(){}
	public final native String getID() /*-{return this.ID; }-*/;
	public final native String getName() /*-{return this.name; }-*/;
	public final native String getLocation() /*-{return this.location; }-*/;
	public final native int getStartYear() /*-{return this.startYear; }-*/;
	public final native int getEndYear() /*-{return this.endYear; }-*/;
	
}
