package uk.ac.nottingham.horizon.exploding.author.client.view;

import java.util.ArrayList;
import java.util.List;

import com.extjs.gxt.ui.client.Style.Orientation;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.ui.Widget;

import uk.ac.nottingham.horizon.exploding.author.client.presenter.ServerManagerPresenter;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.util.Margins;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.extjs.gxt.ui.client.widget.grid.Grid;

import uk.ac.nottingham.horizon.exploding.author.client.datastore.InstanceDataStore;
import uk.ac.nottingham.horizon.exploding.author.client.model.InstanceListEntry;

public class ServerManagerView extends Window implements ServerManagerPresenter.Display {

	private Button newServerButton;
	private Button startServerButton;
	private Button useServerButton;
	private Grid<InstanceListEntry> grid;
	
	public ServerManagerView() {
		setWidth(395);
		setHeading("AWS Server Manger");
		setLayout(new RowLayout(Orientation.VERTICAL));
		
		VerticalPanel verticalPanel = new VerticalPanel();
		add(verticalPanel, new RowData(390.0, 185.0, new Margins()));
		
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		
		columns.add(new ColumnConfig("id", "id", 65));  
		columns.add(new ColumnConfig("image", "image", 65));  
		columns.add(new ColumnConfig("status", "status", 65));  
		columns.add(new ColumnConfig("dns", "dns", 185));
		
		grid = new Grid<InstanceListEntry>(InstanceDataStore.getInstanceStore(), new ColumnModel(columns));  
		grid.setBorders(true);  
		grid.setHeight(200);
		verticalPanel.add(grid);
		grid.setSize("390px", "148px");
		
		HorizontalPanel horizontalPanel = new HorizontalPanel();
		horizontalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		verticalPanel.add(horizontalPanel);
		horizontalPanel.setSize("390px", "34px");
		
		newServerButton = new Button("start new");
		horizontalPanel.add(newServerButton);
		newServerButton.setWidth("101px");
		
		startServerButton = new Button("start selected");
		horizontalPanel.add(startServerButton);
		startServerButton.setWidth("104px");
		
		useServerButton = new Button("use selected");
		horizontalPanel.add(useServerButton);
	}

	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public Button getStartServerButton() {
		return startServerButton;
	}

	@Override
	public Button getNewServerButton() {
		return newServerButton;
	}

	@Override
	public Button getUseServerButton() {
		return useServerButton;
	}
	
	@Override
	public Grid<InstanceListEntry> getGrid() {
		return grid;
	}

}
