package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.GwtEvent;


public class TimeLineEntryChangedEvent extends GwtEvent<TimeLineEntryChangedEventHandler>{
	
	public static Type<TimeLineEntryChangedEventHandler> TYPE = new Type<TimeLineEntryChangedEventHandler>();

	@Override
	protected void dispatch(TimeLineEntryChangedEventHandler handler) {
		handler.onEntryChanged(this);
	}

	@Override
	public Type<TimeLineEntryChangedEventHandler> getAssociatedType() {
		return TYPE;
	}

}
