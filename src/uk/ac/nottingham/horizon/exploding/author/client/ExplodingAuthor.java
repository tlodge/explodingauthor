package uk.ac.nottingham.horizon.exploding.author.client;


import uk.ac.nottingham.horizon.exploding.author.client.datastore.Datastore;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.Maps;
import com.google.gwt.user.client.ui.RootPanel;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class ExplodingAuthor implements EntryPoint {
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Maps.loadMapsApi("ABQIAAAAzHoqwRb2SWt_tEEAD7NVdRQIFrPB_ghrNi1FkUc2OFWkddXcQBT0x4bIjOist0pY-vrAiyL9V3TjpA", "2", false, new Runnable() {
		      public void run() {
		    	  load();
		      }
	   });
		
		/*
		Maps.loadMapsApi("ABQIAAAAzHoqwRb2SWt_tEEAD7NVdRTZBdsmAwqv6TAVg88pgnojzAobnhQjzdIPTQFtDqCtKrYZa8DjkVT0_g", "2", false, new Runnable() {
		      public void run() {
		    	  load();
		      }
	   });*/
		
	}
	
	private void load(){
		
		HandlerManager eventBus = new HandlerManager(null);
		Datastore.init(eventBus);
		AppController appViewer = new AppController(eventBus);
		appViewer.go(RootPanel.get());
	}
}
