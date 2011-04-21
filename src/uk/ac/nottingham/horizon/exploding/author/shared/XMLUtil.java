package uk.ac.nottingham.horizon.exploding.author.shared;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.Text;

public class XMLUtil {
	

		public static void addElement(Element root, Document xmlDoc, String name, String value){
			Element e = xmlDoc.createElement(name);
			Text eText = xmlDoc.createTextNode(String.valueOf(value));
			e.appendChild(eText);
			root.appendChild(e);
		}
	
}
