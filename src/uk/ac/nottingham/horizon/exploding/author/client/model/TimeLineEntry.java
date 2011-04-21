package uk.ac.nottingham.horizon.exploding.author.client.model;

import java.util.HashMap;



import uk.ac.nottingham.horizon.exploding.author.client.util.TimeConverter;
import uk.ac.nottingham.horizon.exploding.author.shared.XMLUtil;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

public class TimeLineEntry {

	public static final int TIMELINE 	= 0;
	public static final int GLOBAL 		= 1;
	public static final int DEFAULT 	= -1;
	
	private String 	id = null;
	private String zoneid = null;
	private String 	name = null;
	private String 	description = null;
	private Integer startTime = 0;
	private Integer endTime = 0;
	private Integer track = 1;
	private int  rgb = 255;
	private boolean enabled = true;
	private boolean absolute = true;
	private Integer column = -1;
	private Integer type = DEFAULT;
	private Integer sequence = 0;
	
	private HashMap<String, Integer> attributes = new HashMap<String, Integer>();

	public TimeLineEntry(String id, String zoneid){
		this.zoneid = zoneid;
		this.id = id;
		setDefaults();
	}

	public TimeLineEntry(String id, String zoneid, String name, String description, Integer column, Integer sequence, HashMap<String, Integer> attr){
		this.id = id;
		this.zoneid = zoneid;
		this.description = description;
		this.name = name;
		
		this.setColumn(column);
		this.setSequence(sequence);
		
		if (attr == null)
			setDefaults();
		else
			this.attributes = attr;
	}

	public void setAttribute(String attribute, Integer value){
		attributes.put(attribute,value);
	}

	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public Integer getStartTime() {
		return startTime;
	}


	public void setStartTime(Integer startTime) {
		this.startTime = startTime;
	}


	public Integer getEndTime() {
		return endTime;
	}


	public void setEndTime(Integer endTime) {
		this.endTime = endTime;
	}


	public Integer getTrack() {
		return track;
	}


	public void setTrack(Integer track) {
		this.track = track;
	}


	public int getRgb() {
		return rgb;
	}


	public void setRgb(int rgb) {
		this.rgb = rgb;
	}


	public boolean isEnabled() {
		return enabled;
	}


	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}


	public boolean isAbsolute() {
		return absolute;
	}


	public void setAbsolute(boolean absolute) {
		this.absolute = absolute;
	}


	public HashMap<String, Integer> getAttributes() {
		return attributes;
	}


	public void setAttributes(HashMap<String, Integer> attributes) {
		this.attributes = attributes;
	}


	public String toString(){
		return name;
	}


	private void setDefaults(){
		if (attributes == null)
			attributes = new HashMap<String, Integer>();

		attributes.put("health", 0);
		attributes.put("wealth", 0);
		attributes.put("brains", 0);
		attributes.put("action", 0);
		attributes.put("instant", 1);
		attributes.put("flags", 15);
		attributes.put("absolute", 1);
	}



	public Element toXML(){
		Document xmlDoc = XMLParser.createDocument();
		
		
		//Log.debug("--* " + id + " " + name + " " + description + " " + zoneid);
		
		Element timeevent = xmlDoc.createElement("timeEvent");
		try{

			timeevent.setAttribute("ref", "TE"+id);
			XMLUtil.addElement(timeevent, xmlDoc, "id", String.valueOf(id));
			XMLUtil.addElement(timeevent, xmlDoc, "name", String.valueOf(name));
			XMLUtil.addElement(timeevent, xmlDoc, "description", description);
			XMLUtil.addElement(timeevent, xmlDoc, "zone", zoneid);
			XMLUtil.addElement(timeevent, xmlDoc, "enabled", enabled? "1" : "0");
			XMLUtil.addElement(timeevent, xmlDoc, "absolute", absolute? "1": "0");

			/*
			 * Generate the index list entries
			 */
			Element indexList = xmlDoc.createElement("indexList");
			indexList.setAttribute("ref", "IL"+id);
			Element attributeSet = xmlDoc.createElement("attributeSet");
			attributeSet.setAttribute("ref", "AS"+id);

			for (String attrname : attributes.keySet()){
				Element e = xmlDoc.createElement(attrname);
				Text t = xmlDoc.createTextNode(String.valueOf(attributes.get(attrname)));
				e.appendChild(t);
				attributeSet.appendChild(e);
			}

			indexList.appendChild(attributeSet);
			timeevent.appendChild(indexList);

			XMLUtil.addElement(timeevent, xmlDoc, "startTime", String.valueOf(TimeConverter.getStartTime(column, sequence)));
			XMLUtil.addElement(timeevent, xmlDoc, "endTime", String.valueOf(TimeConverter.getEndTime(column, sequence)));
			XMLUtil.addElement(timeevent, xmlDoc, "track", String.valueOf(track));
			XMLUtil.addElement(timeevent, xmlDoc, "rgb",  String.valueOf(rgb));
		}catch(Exception e){
			Log.debug("xml creation exception " + e.getMessage());
		}
		return timeevent;
	}

	public String getZoneid() {
		return zoneid;
	}

	public void setZoneid(String zoneid) {
		this.zoneid = zoneid;
	}

	public void setColumn(Integer column) {
		this.column = column;
	}

	public Integer getColumn() {
		return column;
	}

	public void setSequence(Integer sequence) {
		this.sequence = sequence;
	}

	public Integer getSequence() {
		return sequence;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getType() {
		return type;
	}
}
