package uk.ac.nottingham.horizon.exploding.author.client.presenter;

import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

import com.google.gwt.user.client.ui.DialogBox;

public class ZoneAttributesPresenter implements Presenter{

	private final Display display;
	private final HandlerManager eventBus;
	
	public ZoneAttributesPresenter(HandlerManager eb, Display d){
		this.display = d;
		this.eventBus = eb;
	}
	
	public interface Display{
		Widget asWidget();
	}
	
	@Override
	public void go(HasWidgets container) {
	  container.clear();
	 //container.add(display.asWidget());	
	  ((DialogBox) display.asWidget()).center();
	}
	
	

}
