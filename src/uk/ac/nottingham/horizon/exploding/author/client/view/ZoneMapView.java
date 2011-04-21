package uk.ac.nottingham.horizon.exploding.author.client.view;


import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.maps.client.MapWidget;


import com.google.gwt.maps.client.geom.LatLng;


import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockPanel;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

//import com.smartgwt.client.widgets.Canvas;

import uk.ac.nottingham.horizon.exploding.author.client.presenter.ZoneMapPresenter;

public class ZoneMapView extends Window implements ZoneMapPresenter.Display{
	private MapWidget map;
	private Button deletePolygonButton = new Button("Delete Last Zone");
	private Button addPolygonButton = new Button("Add new Zone");
	
	public ZoneMapView(){
		VerticalPanel vp = new VerticalPanel();
		map = new MapWidget(LatLng.newInstance(48.859068, 2.344894), 13);
		map.setSize("500px", "300px");
		map.setUIToDefault();
		
		vp.add(makeToolbar());
		vp.add(map);
		setHeading("Zone Editor");
		setLayout(new RowLayout(Orientation.VERTICAL));
		setClosable(false);
		setCollapsible(true);
		setWidth("500px");
		add(vp);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public MapWidget getMap() {
		return map;
	}

	/**
	 * Create the toolbar above the map. Note that the map must be initialized
	 * before this method is called.
	 */
	private Widget makeToolbar() {
		DockPanel p = new DockPanel();
		p.setWidth("100%");
		HorizontalPanel buttonPanel = new HorizontalPanel();
		buttonPanel.setCellHorizontalAlignment(buttonPanel, HorizontalPanel.ALIGN_CENTER);
		buttonPanel.add(addPolygonButton);
		//buttonPanel.add(deletePolygonButton);
		p.add(buttonPanel, DockPanel.EAST);
		return p;
	}


	@Override
	public HasClickHandlers getDeleteZoneButton() {
		return deletePolygonButton;
	}

	@Override
	public HasClickHandlers getNewZoneButton() {
		return addPolygonButton;
	}

	@Override
	public void disableCreateZoneButton() {
		// TODO Auto-generated method stub
		addPolygonButton.setEnabled(false);
	}

	@Override
	public void enableCreateZoneButton() {
		// TODO Auto-generated method stub
		addPolygonButton.setEnabled(true);
	}

}
