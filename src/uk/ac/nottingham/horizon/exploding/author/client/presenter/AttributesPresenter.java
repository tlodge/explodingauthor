package uk.ac.nottingham.horizon.exploding.author.client.presenter;

import java.util.ArrayList;

import uk.ac.nottingham.horizon.exploding.author.client.datastore.Datastore;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntryChangedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntrySelectedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.TimeLineEntrySelectedEventHandler;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineEntry;


import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.event.SliderEvent;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.Slider;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class AttributesPresenter implements Presenter {
	
	private final Display display;
	private final HandlerManager eventBus;
	private final String[] attributeLabels = {"health", "wealth", "brains", "action"};
	private TimeLineEntry timeLineEntry;
	
	public interface Display{
		Widget asWidget();
	
		void setTitle(String text);		
		void setDescription(String text);
		void moveToFront();
		
		String getTitle();
		String getDescription();
		TextArea getTextAreaDescription();
		TextBox getTextBoxTitle();
		Slider getSlider1();
		Slider getSlider2();
		Slider getSlider3();
		Slider getSlider4();
		Button getSequence1();
		Button getSequence2();
		Button getSequence3();
		Button getSequence4();
		void setSelected(int x);
	}
	
	public AttributesPresenter(HandlerManager eb, Display d){
		this.display = d;
		this.eventBus = eb;
	}

	private void bind(){
		
	
		display.getSequence1().addSelectionListener(new SelectionListener<ButtonEvent>(){

			@Override
			public void componentSelected(ButtonEvent ce) {
				display.setSelected(1);
				timeLineEntry = Datastore.getTimeLineEntry(timeLineEntry.getZoneid(), timeLineEntry.getType(), timeLineEntry.getColumn(), 0);
				updateDisplay();
			}

		});
		
		display.getSequence2().addSelectionListener(new SelectionListener<ButtonEvent>(){

			@Override
			public void componentSelected(ButtonEvent ce) {
				display.setSelected(2);
				timeLineEntry = Datastore.getTimeLineEntry(timeLineEntry.getZoneid(), timeLineEntry.getType(), timeLineEntry.getColumn(), 1);
				updateDisplay();
			}

		});
		
		display.getSequence3().addSelectionListener(new SelectionListener<ButtonEvent>(){

			@Override
			public void componentSelected(ButtonEvent ce) {
				display.setSelected(3);
				timeLineEntry = Datastore.getTimeLineEntry(timeLineEntry.getZoneid(), timeLineEntry.getType(), timeLineEntry.getColumn(), 2);
				updateDisplay();
			}

		});
		
		display.getSequence4().addSelectionListener(new SelectionListener<ButtonEvent>(){

			@Override
			public void componentSelected(ButtonEvent ce) {
				display.setSelected(4);
				timeLineEntry = Datastore.getTimeLineEntry(timeLineEntry.getZoneid(), timeLineEntry.getType(), timeLineEntry.getColumn(), 3);
				updateDisplay();
			}

		});
		
		/*display.getTextAreaDescription().addKeyUpHandler(new KeyUpHandler(){
			@Override
			public void onKeyUp(KeyUpEvent event) {
				timeLineEntry.setDescription(display.getDescription());
			}
			
		});*/
		
		display.getTextAreaDescription().addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				timeLineEntry.setDescription(display.getDescription());
				
			}});
		display.getTextBoxTitle().addChangeHandler(new ChangeHandler(){

			@Override
			public void onChange(ChangeEvent event) {
				timeLineEntry.setName(display.getTitle());
				eventBus.fireEvent(new TimeLineEntryChangedEvent());
				
			}});
		
		/*display.getTextBoxTitle().addKeyUpHandler(new KeyUpHandler(){

			@Override
			public void onKeyUp(KeyUpEvent event) {
				timeLineEntry.setName(display.getTitle());
				eventBus.fireEvent(new TimeLineEntryChangedEvent());
				//Datastore.updateTimeLineEntry(timeLineEntry);
			}
			
		});*/
		
		display.getSlider1().addListener(Events.Change, new Listener<SliderEvent>(){

			@Override
			public void handleEvent(SliderEvent be) {
				
				timeLineEntry.getAttributes().put(attributeLabels[0], be.getNewValue());
			}});
		
		display.getSlider2().addListener(Events.Change, new Listener<SliderEvent>(){

			@Override
			public void handleEvent(SliderEvent be) {
				
				timeLineEntry.getAttributes().put(attributeLabels[1], be.getNewValue());
			}});
		
		display.getSlider3().addListener(Events.Change, new Listener<SliderEvent>(){

			@Override
			public void handleEvent(SliderEvent be) {
				
				timeLineEntry.getAttributes().put(attributeLabels[2], be.getNewValue());
			}});
		
		display.getSlider4().addListener(Events.Change, new Listener<SliderEvent>(){

			@Override
			public void handleEvent(SliderEvent be) {
				
				timeLineEntry.getAttributes().put(attributeLabels[3], be.getNewValue());
			}});
	}
	
	private void subscribe(){
		eventBus.addHandler(TimeLineEntrySelectedEvent.TYPE,
				 new TimeLineEntrySelectedEventHandler() {

					@Override
					public void onTimeLineEntrySelected(
							TimeLineEntrySelectedEvent event) {							
							timeLineSelected(event.getTimeLineEntry());//event.getZoneId(), event.getRow(), event.getColumn());

					}
			    });
	}
	
	private void updateDisplay(){
		display.moveToFront();
		
		if (timeLineEntry != null){
		
			display.setTitle(timeLineEntry.getName());
			display.setDescription(timeLineEntry.getDescription());
			display.getSlider1().setValue(timeLineEntry.getAttributes().get(attributeLabels[0]) == null ? 5 : timeLineEntry.getAttributes().get(attributeLabels[0]));
			display.getSlider2().setValue(timeLineEntry.getAttributes().get(attributeLabels[1]) == null ? 5 : timeLineEntry.getAttributes().get(attributeLabels[1]));
			display.getSlider3().setValue(timeLineEntry.getAttributes().get(attributeLabels[2]) == null ? 5 : timeLineEntry.getAttributes().get(attributeLabels[2]));
			display.getSlider4().setValue(timeLineEntry.getAttributes().get(attributeLabels[3]) == null ? 5 : timeLineEntry.getAttributes().get(attributeLabels[3]));
		}else{
			display.setTitle("");
			display.setDescription("");
			display.getSlider1().setValue(5);
			display.getSlider2().setValue(5);
			display.getSlider3().setValue(5);
			display.getSlider4().setValue(5);
			
		}
		
	}
	private void timeLineSelected(TimeLineEntry t){ //String zoneid, int row, int column){
		//entries = e;
		
		timeLineEntry = t;//e.get(0);
		display.setSelected(1);
		//timeLineEntry = Datastore.getTimeLineEntry(timeLineEntry.getZoneid(), timeLineEntry.getType(), timeLineEntry.getColumn(), 0);
		updateDisplay();
	}
	
	@Override
	public void go(HasWidgets container) {
	  
	  display.asWidget().setVisible(false);
	  bind();
	  subscribe();
	  
	}
	
}
