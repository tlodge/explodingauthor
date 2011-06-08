package uk.ac.nottingham.horizon.exploding.author.client.presenter;



import uk.ac.nottingham.horizon.exploding.author.client.datastore.InstanceDataStore;
import uk.ac.nottingham.horizon.exploding.author.client.event.ServerSelectedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.model.InstanceListEntry;
import uk.ac.nottingham.horizon.exploding.author.client.view.HelpDialog;



import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;

import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class ServerManagerPresenter implements Presenter {

	private final Display display;
	private final HandlerManager eventBus;
	private static InstanceListEntry selectedEntry = null;
	private boolean started = false;
	private boolean pending = false;
	
	public ServerManagerPresenter(HandlerManager eb, Display d){
		this.display = d;
		this.eventBus = eb;	
	}
	
	public interface Display{
		Widget asWidget();
		Grid<InstanceListEntry> getGrid();
		Button getStartServerButton();
		Button getNewServerButton();
		Button getUseServerButton(); 
	}
	
	@Override
	public void go(HasWidgets container) {
		display.asWidget().setVisible(true);
		bind();
		InstanceDataStore.listInstances();
		
		display.getStartServerButton().setEnabled(false);
		//display.getNewServerButton().setEnabled(false);
		display.getUseServerButton().setEnabled(false);
		
		/*Timer t = new Timer() {
		    public void run() {
		    	InstanceDataStore.listInstances();
		    }
		};
		t.scheduleRepeating(10000);*/
	}

	
	
	void updateState(){
		
		if (selectedEntry == null){
			display.getStartServerButton().setEnabled(false);
			//display.getNewServerButton().setEnabled(true);
			display.getUseServerButton().setEnabled(false);
			started = false;
		}
		
		String status = selectedEntry.getStatus();
		
		if (status.equals("running")){
			display.getStartServerButton().setText("stop server");
			display.getStartServerButton().setEnabled(true);
			display.getUseServerButton().setEnabled(true);
			started = true;
			pending = false;
		}else if (status.equals("stopped")){
			display.getStartServerButton().setText("start server");
			display.getStartServerButton().setEnabled(true);
			display.getUseServerButton().setEnabled(false);
			started = false;
			pending = false;
		}else if (status.equals("stopping")){
			display.getStartServerButton().setEnabled(false);
			display.getUseServerButton().setEnabled(false);
			started = false;
			pending = true;
		}else if (status.equals("terminated")){
			display.getUseServerButton().setEnabled(false);
			display.getStartServerButton().setEnabled(false);
			started = false;
			pending = false;
		}else if (status.equals("pending")){
			display.getUseServerButton().setEnabled(false);
			display.getStartServerButton().setEnabled(false);
			started = false;
			pending = true;
		}
	}
	
	void bind(){
	
		
		display.getGrid().addListener(Events.CellClick, new Listener<GridEvent<InstanceListEntry>>(){

			@Override
			public void handleEvent(GridEvent<InstanceListEntry> ile) {
				selectedEntry = ile.getModel();
				updateState();
			}
		});
		
		display.getGrid().addListener(Events.Update,  new Listener<GridEvent<InstanceListEntry>>(){

			@Override
			public void handleEvent(GridEvent<InstanceListEntry> be) {
				Log.debug("datastore has changed!!!");
				selectedEntry = null;
			}
		});
		
		display.getUseServerButton().addClickHandler(new ClickHandler(){
			
			@Override
			public void onClick(ClickEvent event) {
				if (selectedEntry != null){
					String url = "http://"+selectedEntry.getDns()+"/makefest/";
					eventBus.fireEvent(new ServerSelectedEvent(url));
					HelpDialog d = new HelpDialog("Server changed","The url you need to type into the phone is " + url + "rpc/");
					d.show();
				}
			}
		});
		
		display.getStartServerButton().addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (!pending){
					if (!started){
						startSelected();
					}else{
						stopSelected();
					}
					display.getStartServerButton().setEnabled(false);
					display.getUseServerButton().setEnabled(false);
				}
			}
		});
	}
	
	private static void stopSelected(){
		
		if (selectedEntry != null){
			Log.debug("stopping instance " + selectedEntry.getId());
			InstanceDataStore.stopInstance(selectedEntry.getId());
		}
	}
	
	private static void startSelected(){
		if (selectedEntry != null){
			Log.debug("starting  instance " + selectedEntry.getId());
			InstanceDataStore.startInstance(selectedEntry.getId());
		}
	}
}
