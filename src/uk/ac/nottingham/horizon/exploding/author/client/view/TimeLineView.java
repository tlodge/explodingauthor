package uk.ac.nottingham.horizon.exploding.author.client.view;

import java.util.ArrayList;

import com.extjs.gxt.ui.client.Style.HorizontalAlignment;
import com.extjs.gxt.ui.client.Style.Orientation;

import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineModel;
import uk.ac.nottingham.horizon.exploding.author.client.presenter.TimeLinePresenter;

public class TimeLineView extends Window implements TimeLinePresenter.Display{


	private TimeLineGrid table;
	private Button delete;
	private Button addColumn;
	private ContentPanel cp;
	private ScrollPanel sp;

	
	public TimeLineView(){
		setWidth(800);
		cp = new ContentPanel();
		cp.setFrame(false);
		   //cp.setIcon(Resources.ICONS.table());
		cp.setWidth(780);
		cp.setHeight(200);
		
		cp.setLayout(new FitLayout());
		cp.setButtonAlign(HorizontalAlignment.CENTER);  
		
		delete = new Button("Delete");
		cp.addButton(delete);
		
		addColumn = new Button("add column");
		cp.addButton(addColumn);
		
		sp = new ScrollPanel();
		//sp.setHeight("300px");
		table = new TimeLineGrid();
		table.setAutoHeight(true);
		
	
		setHeading("Timeline Editor");
		setLayout(new RowLayout(Orientation.VERTICAL));
		sp.add(table);
		cp.add(sp);
		
		add(cp);	
	}

	public Button getDeleteButton(){
		return delete;
	}
	
	public Button getAddColumnButton(){
		return addColumn;
	}
	
	
	public EditorGrid<TimeLineModel> getTableView(){
		return table.getGrid();
	}
	
	@Override
	public Widget asWidget() {
		return this;
	}

	@Override
	public ArrayList<ColumnConfig> getColumnConfigs() {
		return table.getColumnConfigs();
	}
	
	@Override
	public void resizeit(){
		sp.setHeight(table.getHeight() + "10px");
		cp.setHeight(table.getHeight() + 115);
	}

}
