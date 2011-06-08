package uk.ac.nottingham.horizon.exploding.author.client.presenter;


import java.util.ArrayList;
import com.allen_sauer.gwt.log.client.Log;
import uk.ac.nottingham.horizon.exploding.author.client.datastore.Datastore;
import uk.ac.nottingham.horizon.exploding.author.client.event.GameStateLoadedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.GameStateLoadedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineColumnCreatedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineColumnCreatedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntryChangedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntryChangedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntrySelectedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneCreatedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneCreatedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneDeletedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneDeletedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineEntry;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineModel;
import uk.ac.nottingham.horizon.exploding.author.client.model.Zone;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;


public class TimeLinePresenter implements Presenter{

	private final Display display;
	private final HandlerManager eventBus;
	private String currentZone = null;
	
	
	public interface Display{
		Widget asWidget();
		EditorGrid<TimeLineModel> getTableView();
		ArrayList<ColumnConfig> getColumnConfigs();
		Button getDeleteButton();
		Button getAddColumnButton();
		void resizeit();
	}

	public TimeLinePresenter(HandlerManager eb, Display d){
		this.display = d;
		this.eventBus = eb;
		Datastore.setColumnConfigs(display.getColumnConfigs());
	}

	private void addRow(String zoneid, int type){
		Datastore.insertTimeLine(zoneid, type);
		display.getTableView().setHeight(display.getTableView().getHeight() + 42);
	}

	private void bind(){
		
		display.getTableView().addListener(Events.AfterEdit, new Listener<GridEvent<TimeLineModel>>(){

			@Override
			public void handleEvent(GridEvent<TimeLineModel> be) {
				String zoneName = be.getModel().get("name0");
				Zone zone = be.getModel().getZone();
				zone.setName(zoneName);
			}
		
		});

		display.getTableView().addListener(Events.CellClick, new Listener<GridEvent<TimeLineModel>>(){

			@Override
			public void handleEvent(GridEvent<TimeLineModel> be) {
				if (be.getRowIndex() <= 1){
					display.getDeleteButton().disable();
				}else{
					display.getDeleteButton().enable();
				}
				TimeLineModel model = be.getModel();
				currentZone = model.getZone().getId();
				//eventBus.fireEvent(new TimeLineEntrySelectedEvent(currentZone, be.getRowIndex(), be.getColIndex()))
				if (be.getColIndex() > 0){ //don't do anything if teh first zone name column is clicked			
					int type = TimeLineEntry.DEFAULT;
					
					if (be.getRowIndex() == 0) 
						type = TimeLineEntry.TIMELINE;
					else if (be.getRowIndex() == 1)
						type = TimeLineEntry.GLOBAL;
					
					eventBus.fireEvent(new TimeLineEntrySelectedEvent(Datastore.getTimeLineEntry(currentZone, type, be.getColIndex(), 0)));
				}
				
			}
		});

		
		display.getDeleteButton().addSelectionListener(new SelectionListener<ButtonEvent>() {  

			@Override  
			public void componentSelected(ButtonEvent ce) { 
				if (currentZone != null){
					eventBus.fireEvent(new ZoneDeletedEvent(currentZone));
				}
			}  
		});

		display.getAddColumnButton().addSelectionListener(new SelectionListener<ButtonEvent>() {  

			@Override  
			public void componentSelected(ButtonEvent ce) { 
				addColumn();
			}
		});
	}

	private void subscribe(){
		
		eventBus.addHandler(TimeLineEntryChangedEvent.TYPE, new TimeLineEntryChangedEventHandler(){

			@Override
			public void onEntryChanged(TimeLineEntryChangedEvent event) {
				// TODO Auto-generated method stub
				display.getTableView().getView().refresh(true);
			}
			
		});
		
		eventBus.addHandler(ZoneCreatedEvent.TYPE,
				new ZoneCreatedEventHandler() {
			public void onZoneCreated(ZoneCreatedEvent event) {
				//Log.debug("---------------> a new zone has been added!!" + event.getId());
				addZone(event.getId(), event.getType());
			}
		});

		eventBus.addHandler(ZoneDeletedEvent.TYPE,
				new ZoneDeletedEventHandler() {
			public void onZoneDeleted(ZoneDeletedEvent event) {
				deleteZone(event.getId());
			}
		});

		eventBus.addHandler(TimeLineColumnCreatedEvent.TYPE, new TimeLineColumnCreatedEventHandler(){

			@Override
			public void onColumnCreated(TimeLineColumnCreatedEvent event) {
				addColumn();
			}

		});

		eventBus.addHandler(GameStateLoadedEvent.TYPE, new GameStateLoadedEventHandler(){
			
			@Override
			public void onGameStateLoaded(GameStateLoadedEvent event) {
				display.getTableView().reconfigure(Datastore.getStore(), Datastore.getColumnModel());
			}
		});
	}

	private void addColumn(){
		Datastore.addNewColumn();
		display.getTableView().reconfigure(Datastore.getStore(), Datastore.getColumnModel());
	}

	private void deleteZone(String zoneid){
		
		display.resizeit();
	}

	
	private void addZone(String zoneid, int type){
		addRow(zoneid, type);
		display.resizeit();
	}

	public Element getTimeEvents(String zoneid){
		Document xmlDoc = XMLParser.createDocument();
		Element ezone = xmlDoc.createElement("zone");
		return ezone;
	}

	@Override
	public void go(HasWidgets container) {
		display.asWidget().setVisible(true);
		subscribe();
		bind();
		Datastore.addRenderers();
		Datastore.addRootZones();
	}

}
