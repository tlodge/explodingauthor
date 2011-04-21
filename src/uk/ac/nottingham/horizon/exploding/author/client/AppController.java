package uk.ac.nottingham.horizon.exploding.author.client;


import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneCreatedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneCreatedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.AttributesPresenter;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.Presenter;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.ServerManagerPresenter;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.TimeLinePresenter;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.FileManagerPresenter;
import uk.ac.nottingham.horizon.exploding.author.client.view.FileManagerView;
import uk.ac.nottingham.horizon.exploding.author.client.view.ServerManagerView;
import uk.ac.nottingham.horizon.exploding.author.client.view.TimeLineView;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.ZoneMapPresenter;
import uk.ac.nottingham.horizon.exploding.author.client.view.AttributesView;
import uk.ac.nottingham.horizon.exploding.author.client.view.ZoneMapView;

import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.HasWidgets;


public class AppController implements Presenter, ValueChangeHandler<String> {

	private final HandlerManager eventBus;
	private HasWidgets container;

	public AppController(HandlerManager eb){
		this.eventBus = eb;
		bind();
		
	}

	private void bind() {
		History.addValueChangeHandler(this);

	    eventBus.addHandler(ZoneCreatedEvent.TYPE,
	        new ZoneCreatedEventHandler() {
	          public void onZoneCreated(ZoneCreatedEvent event) {
	            createZoneAttributes();
	          }
	        });  
	}

	public void go(final HasWidgets container) {
		this.container = container;
		
		Presenter fmp = new FileManagerPresenter(eventBus, new FileManagerView());
		fmp.go(container);
		
		Presenter zmp = new ZoneMapPresenter(eventBus, new ZoneMapView());
		zmp.go(container);
		
		Presenter tlv = new TimeLinePresenter(eventBus, new TimeLineView());
		tlv.go(container);
		
		Presenter aps = new AttributesPresenter(eventBus, new AttributesView());
		aps.go(container);
		
		/*
		Presenter smp = new ServerManagerPresenter(eventBus, new ServerManagerView());
		smp.go(container);
		*/
	}
	
	private void createZoneAttributes(){
		History.newItem("zone attributes");
	}

	public void onValueChange(ValueChangeEvent<String> event) {
		/*String token = event.getValue();
		
		if (token != null) {
			Presenter presenter = null;
			
			
			if (token.equals("zone")) {
				presenter = new ZoneMapPresenter(eventBus, new ZoneMapView());
			}

			if (token.equals("zone attributes")){
				presenter = new ZoneAttributesPresenter(eventBus, new ZoneAttributesView());
			}

			if (presenter != null){
				presenter.go(container);
			}
		}*/
	}

}
