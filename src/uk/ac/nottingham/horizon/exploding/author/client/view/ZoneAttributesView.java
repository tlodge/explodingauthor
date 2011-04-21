package uk.ac.nottingham.horizon.exploding.author.client.view;

import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Widget;
//import com.google.gwt.widgetideas.client.SliderBar;

import uk.ac.nottingham.horizon.exploding.author.client.presenter.ZoneAttributesPresenter;

public class ZoneAttributesView extends DialogBox implements ZoneAttributesPresenter.Display{

	private Button cancel;
	private Button create;
	private TextBox zoneName;
	
	public ZoneAttributesView() {
		
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setSize("289px", "207px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("214px", "21px");
		
		Label lblZoneName = new Label("Zone Name");
		horizontalPanel.add(lblZoneName);
		horizontalPanel.setCellVerticalAlignment(lblZoneName, HasVerticalAlignment.ALIGN_MIDDLE);
		lblZoneName.setWidth("102px");
		
		zoneName = new TextBox();
		horizontalPanel.add(zoneName);
		
		VerticalPanel verticalPanel_1 = new VerticalPanel();
		verticalPanel_1.setSize("289px", "134px");
		verticalPanel.add(verticalPanel_1);
		
		
		HorizontalPanel horizontalPanel_1 = new HorizontalPanel();
		horizontalPanel_1.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_1.setSize("120px", "31px");
		verticalPanel.add(horizontalPanel_1);
		verticalPanel.setCellHorizontalAlignment(horizontalPanel_1, HasHorizontalAlignment.ALIGN_CENTER);
		
		//SliderBar slider = new SliderBar(2.0, 100.0);
		//verticalPanel.add(slider);
		
		create = new Button("Create");
		horizontalPanel_1.add(create);
		horizontalPanel_1.setCellHorizontalAlignment(create, HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_1.setCellVerticalAlignment(create, HasVerticalAlignment.ALIGN_MIDDLE);
		
		cancel = new Button("Cancel");
		horizontalPanel_1.add(cancel);
		horizontalPanel_1.setCellHorizontalAlignment(cancel, HasHorizontalAlignment.ALIGN_CENTER);
		horizontalPanel_1.setCellVerticalAlignment(cancel, HasVerticalAlignment.ALIGN_MIDDLE);
		setWidget(verticalPanel);
	}

	public HasClickHandlers getCancelButton(){
		return cancel;
	}
	
	public  HasClickHandlers getCreateButton(){
		return create;
	}
	
	public String getZoneName(){
		return zoneName.getText();
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

}
