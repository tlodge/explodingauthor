package uk.ac.nottingham.horizon.exploding.author.client.presenter;

import java.util.ArrayList;



import uk.ac.nottingham.horizon.exploding.author.client.datastore.Datastore;
import uk.ac.nottingham.horizon.exploding.author.client.event.GameStateLoadEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.GameStateLoadEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneCreatedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneCreatedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneDeletedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneDeletedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneMapCenterEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneMapCenterEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntrySelectedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntrySelectedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineEntry;

import uk.ac.nottingham.horizon.exploding.author.client.model.Zone;
import uk.ac.nottingham.horizon.exploding.author.client.util.ColourMap;

//import uk.ac.horizon.exploding.author.shared.Polygon;


import com.allen_sauer.gwt.log.client.Log;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.maps.client.MapWidget;
import com.google.gwt.maps.client.event.MapClickHandler;
import com.google.gwt.maps.client.event.PolygonCancelLineHandler;
import com.google.gwt.maps.client.event.PolygonClickHandler;
import com.google.gwt.maps.client.event.PolygonEndLineHandler;
import com.google.gwt.maps.client.event.PolygonLineUpdatedHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ZoneMapPresenter implements Presenter{

	private ArrayList<Polygon> zones = new ArrayList<Polygon>();
	
	private final HandlerManager eventBus;
	private final Display display; 
	private double opacity = 1.0;
	private int weight = 1;
	private boolean fillFlag = true;
	private Polygon currentPolygon = null;
	
	public ZoneMapPresenter(HandlerManager eb, Display d){
		this.display = d;
		this.eventBus = eb;
	}

	
	
	public interface Display{
		
		HasClickHandlers getDeleteZoneButton();
		HasClickHandlers getNewZoneButton();
		// TODO Auto-generated method stub
		void enableCreateZoneButton();
		void disableCreateZoneButton();
		MapWidget getMap();
		Widget asWidget();
	}

	private void bind(){
		
		
	
		display.getDeleteZoneButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
			
			}
			
			
		});
		
		display.getNewZoneButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				createPolygon();
			}
		});
		

		display.getMap().addMapClickHandler(new MapClickHandler() {
			public void onClick(MapClickEvent e) {
				
			}
		});
		
	}
	
	public void deletePolygons(){
		ArrayList<Zone> zones = Datastore.getZones();
		if (zones.size() > 2){
			for (int i = 2; i < zones.size(); i++){
				Zone z = zones.get(i);
				display.getMap().removeOverlay(z.getPolygon());
			}
			ColourMap.reset();
		}
	}
	
	public void deletePolygon(){
	  if (currentPolygon != null)
			  display.getMap().removeOverlay(currentPolygon);
	  currentPolygon = null;
	}
	
	private void deleteZone(String zoneid){
	
		Zone z = Datastore.getZone(zoneid, TimeLineEntry.DEFAULT);
		currentPolygon = z.getPolygon();
		deletePolygon();
		/*
		 * can now delete zone details..nasty
		 */
		Datastore.removeTimeLineEntry(zoneid);
	}
	
	private void addOverlay(String zoneid){
		
		Zone z = Datastore.getZone(zoneid, TimeLineEntry.DEFAULT);
		 if (z != null){
			 currentPolygon = z.getPolygon();
			 
			 if (currentPolygon != null){
				 deselectAll();
				 display.getMap().addOverlay(currentPolygon); 
			 }
		 }
	}
	
	private void subscribe(){
		
		eventBus.addHandler(GameStateLoadEvent.TYPE,
				new GameStateLoadEventHandler(){

					@Override
					public void onGameStateLoad(GameStateLoadEvent event) {
						deletePolygons();
					}});
		
		eventBus.addHandler(ZoneDeletedEvent.TYPE,
				 new ZoneDeletedEventHandler() {
				          public void onZoneDeleted(ZoneDeletedEvent event) {
					            deleteZone(event.getId());
					      }
			    });
		
		eventBus.addHandler(ZoneCreatedEvent.TYPE,
				 new ZoneCreatedEventHandler() {
				          public void onZoneCreated(ZoneCreatedEvent event) {
					           
					            if (!event.fromMap()){
					            	addOverlay(event.getId());
					            }
					      }
			    });
		
		eventBus.addHandler(ZoneMapCenterEvent.TYPE,
				 new ZoneMapCenterEventHandler() {
				          public void onCenterRequest(ZoneMapCenterEvent event) {
					       
					         display.getMap().setCenter(event.getPoint());
					        
					      }
			    });
		
		eventBus.addHandler(TimeLineEntrySelectedEvent.TYPE, new TimeLineEntrySelectedEventHandler(){

			@Override
			public void onTimeLineEntrySelected(TimeLineEntrySelectedEvent event) {
				deselectAll();		
			}
			
		});
	}
	
	
	private void deselectAll(){
		if (zones!=null && zones.size() > 0){
			for (Polygon p : zones){
				if (p!=null){
					p.setEditingEnabled(false);
				}
			}
		}
	}
	
	
	
	public void createPolygon() {
		display.disableCreateZoneButton();
		deselectAll();
	    
		final String colour = ColourMap.getNextColour();
	    
	    PolyStyleOptions style = PolyStyleOptions.newInstance(colour, weight,
	        opacity);
	
	    
	    final Polygon poly = new Polygon(new LatLng[0], colour, weight, opacity,
	        colour, fillFlag ? .7 : 0.0);
	    
	    currentPolygon = poly;
	    
	    display.getMap().addOverlay(poly);
	    
	    poly.setDrawingEnabled();
	   
	    poly.setStrokeStyle(style);
	   
	    poly.addPolygonClickHandler(new PolygonClickHandler(){

			@Override
			public void onClick(PolygonClickEvent event) {
				
				deselectAll();
				poly.setEditingEnabled(true);
				currentPolygon = poly;
			}
		});
	    
	  
	    poly.addPolygonLineUpdatedHandler(new PolygonLineUpdatedHandler() {

	      public void onUpdate(PolygonLineUpdatedEvent event) {
	    	  
	      }
	    });

	    poly.addPolygonCancelLineHandler(new PolygonCancelLineHandler() {

	      public void onCancel(PolygonCancelLineEvent event) {
	      
	      }
	    });

	    poly.addPolygonEndLineHandler(new PolygonEndLineHandler() {

	      public void onEnd(PolygonEndLineEvent event) {
	    	 
	    	  addNewZone(poly, colour);
	    	  display.enableCreateZoneButton();
	      }
	    });
	  }
	
	
	
	
	private void addNewZone(Polygon poly, String colour){
	  zones.add(poly);
	  Datastore.addZone(poly, colour);
	}
	
	
	@Override
	public void go(HasWidgets container) {
		display.asWidget().setVisible(true);
		bind();
		subscribe();
		
	}
}
