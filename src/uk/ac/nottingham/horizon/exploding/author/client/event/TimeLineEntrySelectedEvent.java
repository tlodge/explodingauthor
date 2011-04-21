package uk.ac.nottingham.horizon.exploding.author.client.event;

//import java.util.ArrayList;

import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineEntry;

import com.google.gwt.event.shared.GwtEvent;


public class TimeLineEntrySelectedEvent  extends GwtEvent<TimeLineEntrySelectedEventHandler> {

	public static Type<TimeLineEntrySelectedEventHandler> TYPE = new Type<TimeLineEntrySelectedEventHandler>();
	//private ArrayList<TimeLineEntry> entries;
	private TimeLineEntry timeLineEntry;
	
	public TimeLineEntrySelectedEvent(TimeLineEntry t){//ArrayList<TimeLineEntry> e){ //String z, int r, int c){
		//entries = e;
		setTimeLineEntry(t);
	}
	
	//public ArrayList<TimeLineEntry> getTimeLineEntries(){
	//	return entries;//timeLineEntry;
	//}

	@Override
	protected void dispatch(TimeLineEntrySelectedEventHandler handler) {
		 handler.onTimeLineEntrySelected(this);
		
	}

	@Override
	public Type<TimeLineEntrySelectedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	}

	public void setTimeLineEntry(TimeLineEntry timeLineEntry) {
		this.timeLineEntry = timeLineEntry;
	}

	public TimeLineEntry getTimeLineEntry() {
		return timeLineEntry;
	}

}
