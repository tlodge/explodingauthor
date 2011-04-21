package uk.ac.nottingham.horizon.exploding.author.client.view;


import java.util.ArrayList;


import uk.ac.nottingham.horizon.exploding.author.client.datastore.Datastore;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineModel;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.GridEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;

import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.extjs.gxt.ui.client.widget.grid.EditorGrid;
import com.extjs.gxt.ui.client.widget.layout.FlowLayout;
import com.google.gwt.user.client.Element;

public class TimeLineGrid extends LayoutContainer {
 private EditorGrid<TimeLineModel> grid;
 //private ContentPanel cp;


 private ArrayList<ColumnConfig> configs;
 
 public TimeLineGrid(){
	 Datastore.getStore().setMonitorChanges(true);
	 Log.debug("rendering....");
	   setLayout(new FlowLayout(5));
	  configs = new ArrayList<ColumnConfig>();

	   
	   ColumnConfig column = new ColumnConfig();
	   TextField<String> text = new TextField<String>();  
	   column.setId("name0");
	   column.setHeader("zone");
	   column.setWidth(40);
	   text.setAllowBlank(false);  
	   
	
	  /* text.addListener(Events.Change, new Listener<GridEvent<TimeLineModel>>(){

		@Override
		public void handleEvent(GridEvent<TimeLineModel> be) {
			// TODO Auto-generated method stub
			Log.debug("row updated!!!" );
		}

		/*@Override
		public void handleEvent(BaseEvent be) {
			// TODO Auto-generated method stub
			Log.debug("row updated!!!" + be.getSource().);
		}
		
		});*/
	   
	   
	   column.setEditor(new CellEditor(text));  
	   
	   configs.add(column);  
	   
	  /* for (int i = 1; i < 2; i++){
		   column = new ColumnConfig();
		   column.setId("name"+i);
		   column.setHeader(String.valueOf(i));
		   column.setWidth(35);
		   configs.add(column);
	   }*/
	   
	   ColumnModel cm = new ColumnModel(configs);
	   grid = new EditorGrid<TimeLineModel>(Datastore.getStore(), cm);
	   grid.setAutoHeight(true);
	   grid.setHeight(5);
	   grid.setBorders(true);
	   grid.setStripeRows(true);
	   grid.enableEvents(true);
	   add(grid);
 }
 
 
 
 public EditorGrid<TimeLineModel> getGrid(){
	 return grid;
 } 
 
 public ArrayList<ColumnConfig> getColumnConfigs(){
	 return configs;
 }
 
 @Override
 protected void onRender(Element parent, int index) {
   super.onRender(parent, index);
  
 }
}
