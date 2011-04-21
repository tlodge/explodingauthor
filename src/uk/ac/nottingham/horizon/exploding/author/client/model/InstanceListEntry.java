package uk.ac.nottingham.horizon.exploding.author.client.model;

import com.extjs.gxt.ui.client.data.BaseModel;

public class InstanceListEntry extends BaseModel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5762679159897938610L; 
	
	public String getId(){
		return get("id");
	}
	
	public String getImage(){
		return get("image");
	}
	
	public String getStatus(){
		return get("status");
	}
	
	public String getDns(){
		return get("dns");
	}
	
	public boolean equals(InstanceListEntry e){
		return 	   this.getDns().equals(e.getDns())
				&& this.getStatus().equals(e.getStatus())
				&& this.getImage().equals(e.getImage())
				&& this.getId().equals(e.getId());
	}
	
	public InstanceListEntry(String id, String image, String status, String dns){
		set("id", id);
		set("image",  image);
		set("status",  status);
		set("dns", dns);
	}
}
