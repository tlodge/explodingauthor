package uk.ac.nottingham.horizon.exploding.author.client.presenter;

import uk.ac.nottingham.horizon.exploding.author.client.datastore.Datastore;
import uk.ac.nottingham.horizon.exploding.author.client.event.GameStateLoadEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ServerSelectedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ServerSelectedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.model.GameListEntry;

import com.allen_sauer.gwt.log.client.Log;

import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
 

public class FileManagerPresenter implements Presenter{

	private final Display display;
	private final HandlerManager eventBus;
	private String currentGameID = null;
	private String currentLocation = "";
	
	public interface Display{
		Widget asWidget();
		HasClickHandlers getExportButton();
		HasClickHandlers getImportButton();
		HasClickHandlers getRefreshButton();
		Grid<GameListEntry> getGrid();
		String getGameName();
		String getServerAddress();
		String getLocation();
		
		void setServerAddress(String address);
		void setLocation(String location);
	}
	
	
	public FileManagerPresenter(HandlerManager eb, Display d){
		this.display = d;
		this.eventBus = eb;
		String url = GWT.getHostPageBaseURL().replace("explodingauthor/", "");
		//display.setServerAddress(url + "makefest/");
		display.setServerAddress(url + "exploding/");
	}
	
	private void init(){	    
	   //Datastore.getGameList(display.getServerAddress());
		Timer t = new Timer() {
		    public void run() {
		    	getGameList();
		    }
		};
		t.scheduleRepeating(20000);
	}
	
	private void getGameList(){
		if (!display.getServerAddress().trim().equals(""))
			Datastore.getGameList(display.getServerAddress());
	}
	private void bind(){
		display.getExportButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				
				Datastore.export(display.getGameName(), display.getLocation() ,display.getServerAddress());
				
				//Log.debug("firing a new game export event..name = " + display.getGameName());
				//eventBus.fireEvent(new GameStateExportEvent(display.getGameName()));
				
			}});
		
		display.getImportButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (currentGameID != null){
					Log.debug("loading " + currentGameID);
					eventBus.fireEvent(new GameStateLoadEvent());
					Datastore.loadNewGame(currentGameID, display.getServerAddress());
					display.setLocation(currentLocation);
				}
			}});
		
		
		display.getGrid().addListener(Events.CellClick, new Listener<GridEvent<GameListEntry>>(){

			@Override
			public void handleEvent(GridEvent<GameListEntry> be) {
				Log.debug("set current game id to " + be.getModel().getID());
				currentGameID = be.getModel().getID();
				currentLocation = be.getModel().getLocation();
				Log.debug("set current location to " + be.getModel().getLocation());
			}
		});
		
		display.getRefreshButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				Log.debug("getting game list");
				Datastore.getGameList(display.getServerAddress());
				
			}});
		
		
	}
	
	private void subscribe(){
		eventBus.addHandler(ServerSelectedEvent.TYPE,
				 new ServerSelectedEventHandler() {

					@Override
					public void onServerSelected(ServerSelectedEvent event) {							
							
						display.setServerAddress(event.getServerName());
						Datastore.getGameList(display.getServerAddress());
					}
			    });
	}
	
	@Override
	public void go(HasWidgets container) {
		// TODO Auto-generated method stub
		display.asWidget().setVisible(true);
		subscribe();
		bind();
		init();
	}

}
