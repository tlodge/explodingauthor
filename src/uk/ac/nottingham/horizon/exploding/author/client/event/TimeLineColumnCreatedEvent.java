package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class TimeLineColumnCreatedEvent extends GwtEvent<TimeLineColumnCreatedEventHandler>{

	public static Type<TimeLineColumnCreatedEventHandler> TYPE = new Type<TimeLineColumnCreatedEventHandler>();
	
	//private int column;
	
	public TimeLineColumnCreatedEvent(){//int column){
		//this.column = column;
	}
	
	//public int getColumn(){
		//return column;
	//}
	
	@Override
	protected void dispatch(TimeLineColumnCreatedEventHandler handler) {
		 handler.onColumnCreated(this);
	}

	@Override
	public Type<TimeLineColumnCreatedEventHandler> getAssociatedType() {
		// TODO Auto-generated method stub
		return TYPE;
	} 
}
