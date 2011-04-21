package uk.ac.nottingham.horizon.exploding.author.client.event;

import com.google.gwt.event.shared.EventHandler;

public interface TimeLineEntrySelectedEventHandler extends EventHandler {
	 void onTimeLineEntrySelected(TimeLineEntrySelectedEvent event);
}
