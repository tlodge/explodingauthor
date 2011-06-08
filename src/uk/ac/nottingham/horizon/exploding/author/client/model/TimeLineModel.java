package uk.ac.nottingham.horizon.exploding.author.client.model;

import java.util.HashMap;

import com.extjs.gxt.ui.client.data.BaseModel;

public class TimeLineModel extends BaseModel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3433460473145014210L;
	private Zone zone;
	//private HashMap<String,Object> entries;
	
	public TimeLineModel(Zone zone,HashMap<String,Object> m){		
		super(m);
		this.zone = zone;
		//this.entries = m;
	}
	
	public void setZone(Zone zone){
		this.zone = zone;
	}
	
	public Zone getZone(){
		return zone;
	}
	
}
