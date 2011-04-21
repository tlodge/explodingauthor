package uk.ac.nottingham.horizon.exploding.author.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class GameListEntry extends BaseModel{
	
	
	private static final long serialVersionUID = 8999244053150339016L;

	
	public String getID() {
		return get("ID");
	}

	public String getName() {
		return get("name");
	}

	
	public String getLocation() {
		return get("location");
	}


	public String getStartYear() {
		return get("startYear");
	}

	public String getEndYear() {
		return get("endYear");
	}


	public GameListEntry(String ID,String name, String location,String startYear, String endYear){
		set("ID", ID);
		set("name",  name);
		set("location",  location);
		set("startYear", startYear);
		set("endYear",  endYear);
	}
	
}
