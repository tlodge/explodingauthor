package uk.ac.nottingham.horizon.exploding.author.client.jsmodel;

import com.google.gwt.core.client.JavaScriptObject;

public class InstanceItem extends JavaScriptObject{
	protected InstanceItem(){}
	public final native String getId() /*-{return this.id; }-*/;
	public final native String getImage() /*-{return this.image; }-*/;
	public final native String getStatus() /*-{return this.status; }-*/;
	public final native String getDns() /*-{return this.dns; }-*/;
}
