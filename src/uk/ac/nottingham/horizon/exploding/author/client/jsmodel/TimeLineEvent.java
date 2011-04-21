package uk.ac.nottingham.horizon.exploding.author.client.jsmodel;

import com.google.gwt.core.client.JavaScriptObject;

public class TimeLineEvent extends JavaScriptObject{
	
	protected TimeLineEvent(){}
	
	public final native String getID( ) /*-{return this.ID; }-*/;
	public final native int getOrgId( ) /*-{return this.orgId; }-*/;
	public final native String getContentGroupID( ) /*-{return this.contentGroupID; }-*/;
	public final native String getName( ) /*-{return this.name; }-*/;
	public final native String getDescription( ) /*-{return this.description; }-*/;
	public final native String getZoneId( ) /*-{return this.zoneId; }-*/;
	public final native int getEnabled( ) /*-{return this.enabled; }-*/;
	public final native float getStartTime( ) /*-{return this.startTime; }-*/;
	public final native float getEndTime( ) /*-{return this.endTime; }-*/;
	public final native int getTrack( ) /*-{return this.track; }-*/;
	public final native int getRgb( ) /*-{return this.rgb; }-*/;
	public final native int getHealth( ) /*-{return this.health; }-*/;
	public final native int getWealth( ) /*-{return this.wealth; }-*/;
	public final native int getBrains( ) /*-{return this.brains; }-*/;
	public final native int getAction( ) /*-{return this.action; }-*/;
	public final native int getInstant( ) /*-{return this.instant; }-*/;
	public final native int getFlags( ) /*-{return this.flags; }-*/;
	public final native int getAbsolute( ) /*-{return this.absolute; }-*/;
	

}
