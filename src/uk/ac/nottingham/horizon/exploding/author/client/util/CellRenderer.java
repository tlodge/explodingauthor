package uk.ac.nottingham.horizon.exploding.author.client.util;

import java.util.ArrayList;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineEntry;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineModel;
import uk.ac.nottingham.horizon.exploding.author.client.model.Zone;

import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.grid.ColumnData;
import com.extjs.gxt.ui.client.widget.grid.Grid;
import com.extjs.gxt.ui.client.widget.grid.GridCellRenderer;

public class CellRenderer {

	public static GridCellRenderer<TimeLineModel> zonerenderer = new GridCellRenderer<TimeLineModel>() {  

		public String render(TimeLineModel model, String property, ColumnData config, int rowIndex, int colIndex,  
				ListStore<TimeLineModel> store, Grid<TimeLineModel> grid) {  
			Zone z = model.getZone(); 
			if (z != null){
				String style = z.getColour(); 
				return "<span style='background:" + style + "'>" + model.get(property) + "</span>";
			}
			return property;
		}
	};	
	
	public static GridCellRenderer<TimeLineModel> tlerenderer = new GridCellRenderer<TimeLineModel>() {  

		public String render(TimeLineModel model, String property, ColumnData config, int rowIndex, int colIndex,  
				ListStore<TimeLineModel> store, Grid<TimeLineModel> grid) {  

			ArrayList<TimeLineEntry> e  = model.get("name"+colIndex);
			
			String s ="";
			if (e != null){
				for (TimeLineEntry t: e){
					s += t.getName() + " ";
				}
			}

			return (s);
		}
	};	
}
