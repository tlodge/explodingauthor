package uk.ac.nottingham.horizon.exploding.author.client.model;


import java.util.HashMap;

import uk.ac.nottingham.horizon.exploding.author.shared.XMLUtil;

import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.Polygon;
import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;
import com.google.gwt.xml.client.XMLParser;

public class Zone {

	private String id;
	private String name;
	private Polygon polygon;
	private String colour;
	private HashMap<String, Integer> attributes = new HashMap<String, Integer>();
	private HashMap<String, String> attributeOps = new HashMap<String, String>();
	
	public Zone(String id){
		this.id = id;
		setDefaults();
	}
	
	public String getColour() {
		return colour;
	}
	
	public void setAttribute(String name, Integer value){
		attributes.put(name, value);
	}
	
	public void setAttributeOp(String name, String value){
		attributeOps.put(name, value);
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public Zone(String id, String name, Polygon polygon, String colour){
		this.id = id;
		this.name = name;	
		this.polygon = polygon;
		this.colour = colour;
		setDefaults();
	}
	
	private void setDefaults(){
		attributes.put("health", 0);
		attributes.put("wealth", 0);
		attributes.put("brains", 0);
		attributes.put("action", 0);
		attributes.put("instant", 1);
		attributes.put("flags", 15);
		attributes.put("absolute", 1);
		
		attributeOps.put("healthOp", "attributeOp");
		attributeOps.put("wealthOp", "attributeOp");
		attributeOps.put("brainsOp", "attributeOp");
		attributeOps.put("actionOp", "attributeOp");
	}
	
	
	public Polygon getPolygon() {
		return polygon;
	}

	public void setPolygon(Polygon polygon) {
		this.polygon = polygon;
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
	
	/*
	 * Can't find any xml serialization libraries for JSON....
	 */
	public Element toXML(){
		
		Document xmlDoc = XMLParser.createDocument();
		
        Element ezone = xmlDoc.createElement("zone");
        ezone.setAttribute("ref", name+id);
      
        XMLUtil.addElement(ezone, xmlDoc, "id", id);
        XMLUtil.addElement(ezone, xmlDoc, "name", name);
              
        Element efield = xmlDoc.createElement("field");
        efield.setAttribute("name", "attributes");
        efield.setAttribute("type", "attributeSet");
        
        Element eattributeSet = xmlDoc.createElement("attributeSet");
        eattributeSet.setAttribute("ref", "170" + id); //??
       
        for (String attrname : attributes.keySet()){
        	Element e = xmlDoc.createElement(attrname);
        	Text t = xmlDoc.createTextNode(String.valueOf(attributes.get(attrname)));
        	e.appendChild(t);
        	eattributeSet.appendChild(e);
        }
        
        for (String attrname : attributeOps.keySet()){
        	Element e = xmlDoc.createElement(attrname);
        	e.setAttribute("name", attrname);
            e.setAttribute("type", String.valueOf(attributeOps.get(attrname)));
            eattributeSet.appendChild(e);
        }
        
        efield.appendChild(eattributeSet);
        ezone.appendChild(efield);
        
        if (polygon != null){
        	XMLUtil.addElement(ezone, xmlDoc, "polygon", "1");
        	XMLUtil.addElement(ezone, xmlDoc, "radius", "0.000000000000");
        	Element ecoords = xmlDoc.createElement("field");
        	ecoords.setAttribute("name", "coords");
        	Text coordsText = xmlDoc.createTextNode(getPolyCoords());
        	ecoords.appendChild(coordsText);
        	ezone.appendChild(ecoords);
        }
        return ezone;	
	}
	
	
	
	private String getPolyCoords(){
		String coords = "";
		for (int i =0; i < polygon.getVertexCount(); i++){
			LatLng l = polygon.getVertex(i);
			coords += l.getLongitude() +"," + l.getLatitude() + ",0 ";
		}
		return coords;
	}
}
