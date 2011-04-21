package uk.ac.nottingham.horizon.exploding.author.client.datastore;

import java.util.ArrayList;
import java.util.HashMap;
import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.TextField;
import com.extjs.gxt.ui.client.widget.grid.CellEditor;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;
import com.extjs.gxt.ui.client.widget.grid.ColumnModel;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.maps.client.event.PolygonClickHandler;
import com.google.gwt.maps.client.geom.LatLng;
import com.google.gwt.maps.client.overlay.PolyStyleOptions;
import com.google.gwt.maps.client.overlay.Polygon;

import com.google.gwt.xml.client.Document;
import com.google.gwt.xml.client.Element;
import com.google.gwt.xml.client.XMLParser;


import uk.ac.nottingham.horizon.exploding.author.client.event.GameStateLoadedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneCreatedEvent;
import uk.ac.nottingham.horizon.exploding.author.client.event.ZoneMapCenterEvent;
import uk.ac.nottingham.horizon.exploding.author.client.jsmodel.LatLngElevation;
import uk.ac.nottingham.horizon.exploding.author.client.model.GameListEntry;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineEntry;
import uk.ac.nottingham.horizon.exploding.author.client.model.TimeLineModel;
import uk.ac.nottingham.horizon.exploding.author.client.model.Zone;
import uk.ac.nottingham.horizon.exploding.author.client.util.ColourMap;
import uk.ac.nottingham.horizon.exploding.author.client.util.TimeConverter;
import uk.ac.nottingham.horizon.exploding.author.client.util.TimeLineIdGenerator;
import uk.ac.nottingham.horizon.exploding.author.shared.XMLUtil;
import uk.ac.nottingham.horizon.exploding.author.client.util.CellRenderer;
import uk.ac.nottingham.horizon.exploding.author.client.view.HelpDialog;

public class Datastore {

	private static HashMap<String,Zone> zones = new HashMap<String, Zone>();
	private static ListStore<TimeLineModel> timelinestore = new ListStore<TimeLineModel>(); 
	private static ListStore<GameListEntry> liststore = new ListStore<GameListEntry>();
	private static ArrayList<ColumnConfig> configs = new ArrayList<ColumnConfig>();
	private static ColumnModel cm;

	private static HandlerManager eventBus;

	private static String BASEURL = "";
	//private static String POST_PROXY = "http://localhost/aws/postproxy.php";
	//private static String GET_PROXY = "http://localhost/aws/getproxy.php";

	private static int zoneIndex = 0;



	public static void init(HandlerManager e){
		eventBus = e;
		subscribe();
	}

	private static void subscribe(){
	}

	private static void showMessage(String title, String message){
		HelpDialog h = new HelpDialog(title, message);
		h.show();
	}


	public static void export(String name, String location, String address){
		Log.debug("exporting with location " + location);
		
		if (name == null || location == null || address == null){
			showMessage("cannot upload game", "please make sure you specify a game name, location and server address");
			return;
		}
		
		if (location.trim().equals("") || name.trim().equals("") || address.trim().equals("")){
			showMessage("cannot upload game", "please make sure you specify a game name, location and server address");
			return;
		}
		String s = getXML(location).toString();		

		Log.debug(s);
		
		if (s != null){
			upload(s, name, address);
		}else{
			showMessage("cannot upload game", "Error creating the game file");
		}
	}

	private static void upload(String xml, String name, String address){

		if (address == null || address.trim().equals("")){
			address = BASEURL;
		}



		String url = address + "author/gameupload.html";

		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
		builder.setHeader("Content-type", "application/x-www-form-urlencoded");

		builder.setRequestData("gamestate=" + xml + "&gamename=" + name + "&url=" + url); 
		Log.debug(xml);
		builder.setCallback(new RequestCallback(){

			@Override
			public void onError(Request request, Throwable exception) {
				// TODO Auto-generated method stub
				Log.debug("error " + exception.getMessage());
			}

			@Override
			public void onResponseReceived(Request request,
					Response response) {
				if (200 == response.getStatusCode()){
					Log.debug(response.getText());
				}else{ 
					Log.debug("ERROR " + response.getText());
				}
			}
		});

		try{
			builder.send();
		}catch(Exception e){
			Log.debug("error " + e.getMessage());
		}
	}

	public static void getGameList(String address){
		listGames(address);
	}

	/*
	 * Can only add 'default' zones
	 */

	public static void addZone(Polygon poly, String colour){
		Zone z = new Zone(String.valueOf(zoneIndex), "zone" + zoneIndex, poly, colour);
		String key = getKey(z.getId(), TimeLineEntry.DEFAULT);
		zones.put(key, z);
		eventBus.fireEvent(new ZoneCreatedEvent(String.valueOf(zoneIndex), TimeLineEntry.DEFAULT, true));
		zoneIndex +=1;
	}


	private static String getKey(String zoneid, int type){
		String s = "";

		if (type == TimeLineEntry.GLOBAL)
			s = ":" + TimeLineEntry.GLOBAL;

		if (type == TimeLineEntry.TIMELINE)
			s = ":" + TimeLineEntry.TIMELINE;

		return zoneid + s;
	}


	private static void addZone(Zone zone, int type){
		String key = getKey(zone.getId(), type);
		zones.put(key, zone);
		eventBus.fireEvent(new ZoneCreatedEvent(zone.getId(), type, false));
		zoneIndex +=1;
	}

	public static void deleteZone(String zoneId, int type){
		String key = getKey(zoneId, type);
		zones.remove(key);
	}

	public static Zone getZone(String zoneId, int type){
		String key = getKey(zoneId, type);
		return zones.get(key);
	}

	public static ArrayList<Zone> getZones(){
		ArrayList<Zone> tmpzones = new ArrayList<Zone>();
		for (Zone z: zones.values()){
			tmpzones.add(z);
		}
		return tmpzones;
	}


	public static ListStore<TimeLineModel> getStore(){
		return timelinestore;
	}

	private static String getStartYear(){

		if (timelinestore == null)
			return "";

		try{
			TimeLineModel m = timelinestore.getAt(0);

			if ( (m==null) || m.get("name1") == null){	
				return "";
			}
			else{
				ArrayList<TimeLineEntry> list = m.get("name1");
				if (list != null && list.size() > 0)
					return list.get(0).getName();
			}
			return "";
		}catch(Exception e){
			return "";
		}
	}

	private static String getEndYear(){
		if (timelinestore == null)
			return "";

		try{
			TimeLineModel m = timelinestore.getAt(0);

			if (m == null || m.get("name1") == null){
				return "";
			}

			if (configs == null){
				return "";
			}

			for (int i = configs.size()-1; i >= 0; i--){
				ArrayList<TimeLineEntry> list;
				if ((list = m.get("name" + i)) != null){
					if (list.size() > 0){
						if (!list.get(0).getName().equals(""))
							return list.get(0).getName();

					}

				}
			}
			return "";
		}catch(Exception e){
			return "";
		}
	}

	private static boolean checkYears(String year1, String year2){


		if (year1.trim() == ""){
			showMessage("cannot upload game", "please check that you have a valid start year in the timeline");
			return false;
		}
		if (year2.trim() == ""){
			showMessage("cannot upload game", "please check that you have a valid end year in the timeline");
			return false;
		}
		try{
			Integer.valueOf(year1);
		}catch(NumberFormatException n){
			showMessage("cannot upload game", "please check that you have a valid start year (i.e a number) in the timeline");
			return false;
		}

		try{
			Integer.valueOf(year2);
		}catch(NumberFormatException n){
			showMessage("cannot upload game", "please check that you have a valid end year (i.e a number) in the timeline");
			return false;
		}

		return true;
	}

	public static Element getXML(String location){

		Document xmlDoc = XMLParser.createDocument();
		Element gameState = xmlDoc.createElement("gameState");
		gameState.setAttribute("version", "1.0");

		if (location.trim().equals(""))
			location = "Unknown";

		XMLUtil.addElement(gameState, xmlDoc, "location", location);

		String startYear = getStartYear();
		String endYear = getEndYear();

		if (!checkYears(startYear, endYear))
			return null;


		XMLUtil.addElement(gameState, xmlDoc, "startYear", startYear);
		XMLUtil.addElement(gameState, xmlDoc, "endYear", endYear);

		//timeEvents
		try{
			Element timeEvents = xmlDoc.createElement("timeEvents");


			for (int i = 0; i < timelinestore.getCount(); i++){


				TimeLineModel m = timelinestore.getAt(i);

				for (String key : m.getProperties().keySet()){
					try{

						if (!key.equals("name0")){ //special case
							ArrayList<TimeLineEntry> entries = m.get(key);

							if (entries != null){

								for (TimeLineEntry tle : entries){
									if (tle!= null){

										if (tle.getName() != null){

											if (!(tle.getName().equals(""))){

												timeEvents.appendChild(tle.toXML());
											}
										}
									}
								}
							}
						}
					}catch(Exception e){
						Log.debug("---------------->>>>>>>>  error exporting ");
					}
				}
			}

			gameState.appendChild(timeEvents);

			//zones
			Element ezones = xmlDoc.createElement("zones");

			for (Zone z : zones.values()){
				if (Integer.valueOf(z.getId())!= 0){
					ezones.appendChild(z.toXML());
				}
			}

			gameState.appendChild(ezones);

		}catch(Exception e){
			Log.debug("ERRROR EXPORTING ---> " + e.getMessage());
		}
		return gameState;
	}


	public static ListStore<GameListEntry> getListStore(){

		return liststore;
	}

	public static void listGames(String address){


		RequestBuilder builder = null;
		if (address == null || address.trim().equals("")){
			address = BASEURL;
		}

		String url = address + "author/list.html";

		//String url = GET_PROXY + "?url=" + address + "author/list.html";

		Log.debug("sending to " + url);

		builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

		try{
			Request request = builder.sendRequest(null, new RequestCallback(){

				@Override
				public void onError(Request request, Throwable exception) {
					// TODO Auto-generated method stub
					Log.debug("error " + exception.getMessage());
				}

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()){
						Log.debug(response.getText());
						generateJSONListObjects(response.getText());
					}else{ 
						Log.debug("got following " + response.getText());
					}
				}
			});

		}catch(Exception e){
			Log.debug("error " + e.getMessage());
		}
	}



	public static void loadNewGame(String gameId, String address){

		//	String json = "{\"zones\":[{\"zone\":{\"ID\":\"ZO500\",\"contentGroupID\":\"CG500\",\"orgId\":\"1",\"name\":\"Main\",\"ref\":\"215G6G\",\"polygon\":1,\"radius\":0,\"coordinates\":[{\"position\":[{\"latitude\":52.964072,\"longitude\":-1.166954,\"elevation\":0},{\"latitude\":52.966502,\"longitude\":-1.153522,\"elevation\":0},{\"latitude\":52.958308,\"longitude\":-1.145926,\"elevation\":0},{\"latitude\":52.955335,\"longitude\":-1.142063,\"elevation\":0},{\"latitude\":52.953421,\"longitude\":-1.145453,\"elevation\":0},{\"latitude\":52.95112,\"longitude\":-1.151891,\"elevation\":0},{\"latitude\":52.950706,\"longitude\":-1.154294,\"elevation\":0}]}]}},{\"zone\":{\"ID\":\"ZO501\",\"contentGroupID\":\"CG500\",\"orgId\":2,\"name\":\"centre\",\"ref\":\"215B54\",\"polygon\":1,\"radius\":0,\"coordinates\":[{\"position\":[{\"latitude\":52.956033,\"longitude\":-1.156611,\"elevation\":0},{\"latitude\":52.955929,\"longitude\":-1.152492,\"elevation\":0},{\"latitude\":52.956136,\"longitude\":-1.151204,\"elevation\":0},{\"latitude\":52.957816,\"longitude\":-1.151075,\"elevation\":0},{\"latitude\":52.958928,\"longitude\":-1.146612,\"elevation\":0},{\"latitude\":52.956265,\"longitude\":-1.143351,\"elevation\":0},{\"latitude\":52.95536,\"longitude\":-1.143565,\"elevation\":0},{\"latitude\":52.955102,\"longitude\":-1.14305,\"elevation\":0},{\"latitude\":52.95368,\"longitude\":-1.141462,\"elevation\":0},{\"latitude\":52.953214,\"longitude\":-1.145368,\"elevation\":0},{\"latitude\":52.95218,\"longitude\":-1.149316,\"elevation\":0},{\"latitude\":52.951301,\"longitude\":-1.151934,\"elevation\":0},{\"latitude\":52.952749,\"longitude\":-1.15335,\"elevation\":0}]}]}},{\"zone\":{\"ID\":\"ZO502\",\"contentGroupID\":\"CG500\",\"orgId\":3,\"name\":\"arsenal\",\"ref\":\"215FZC\",\"polygon\":1,\"radius\":0,\"coordinates\":[{\"position\":[{\"latitude\":52.964279,\"longitude\":-1.155753,\"elevation\":0},{\"latitude\":52.964925,\"longitude\":-1.152577,\"elevation\":0},{\"latitude\":52.959006,\"longitude\":-1.146698,\"elevation\":0},{\"latitude\":52.957868,\"longitude\":-1.151247,\"elevation\":0},{\"latitude\":52.956214,\"longitude\":-1.151376,\"elevation\":0},{\"latitude\":52.956007,\"longitude\":-1.152492,\"elevation\":0},{\"latitude\":52.956136,\"longitude\":-1.156611,\"elevation\":0},{\"latitude\":52.959083,\"longitude\":-1.158242,\"elevation\":0},{\"latitude\":52.960944,\"longitude\":-1.155367,\"elevation\":0}]}]}},{\"zone\":{\"ID\":\"ZO503\",\"contentGroupID\":\"CG500\",\"orgId\":4,\"name\":\"dusthole\",\"ref\":\"215JD4\",\"polygon\":1,\"radius\":0,\"coordinates\":[{\"position\":[{\"latitude\":52.963219,\"longitude\":-1.155624,\"elevation\":0},{\"latitude\":52.960453,\"longitude\":-1.153479,\"elevation\":0},{\"latitude\":52.959962,\"longitude\":-1.155281,\"elevation\":0},{\"latitude\":52.957661,\"longitude\":-1.157556,\"elevation\":0},{\"latitude\":52.956162,\"longitude\":-1.156783,\"elevation\":0},{\"latitude\":52.956084,\"longitude\":-1.161633,\"elevation\":0},{\"latitude\":52.956576,\"longitude\":-1.162791,\"elevation\":0},{\"latitude\":52.958153,\"longitude\":-1.16056,\"elevation\":0},{\"latitude\":52.959755,\"longitude\":-1.158199,\"elevation\":0},{\"latitude\":52.961513,\"longitude\":-1.160173,\"elevation\":0},{\"latitude\":52.963219,\"longitude\":-1.155624,\"elevation\":0}]}]}},{\"zone\":{\"ID\":\"ZO504\",\"contentGroupID\":\"CG500\",\"orgId\":5,\"name\":\"charlton\",\"ref\":\"215D1K\",\"polygon\":1,\"radius\":0,\"coordinates\":[{\"position\":[{\"latitude\":52.956007,\"longitude\":-1.16159,\"elevation\":0},{\"latitude\":52.956058,\"longitude\":-1.156783,\"elevation\":0},{\"latitude\":52.952335,\"longitude\":-1.15365,\"elevation\":0},{\"latitude\":52.951379,\"longitude\":-1.152363,\"elevation\":0},{\"latitude\":52.950758,\"longitude\":-1.151977,\"elevation\":0},{\"latitude\":52.950551,\"longitude\":-1.153221,\"elevation\":0},{\"latitude\":52.950474,\"longitude\":-1.153908,\"elevation\":0}]}]}},{\"zone\":{\"ID\":\"ZO505\",\"contentGroupID\":\"CG500\",\"orgId\":6,\"name\":\"marshes\",\"ref\":\"215NL4\",\"polygon\":1,\"radius\":0,\"coordinates\":[{\"position\":[{\"latitude\":52.964176,\"longitude\":-1.155624,\"elevation\":0},{\"latitude\":52.964641,\"longitude\":-1.153436,\"elevation\":0},{\"latitude\":52.960893,\"longitude\":-1.150603,\"elevation\":0},{\"latitude\":52.95792,\"longitude\":-1.151204,\"elevation\":0},{\"latitude\":52.956886,\"longitude\":-1.151462,\"elevation\":0},{\"latitude\":52.955128,\"longitude\":-1.151032,\"elevation\":0},{\"latitude\":52.955386,\"longitude\":-1.156182,\"elevation\":0},{\"latitude\":52.957713,\"longitude\":-1.157727,\"elevation\":0}]}]}}],\"timeevents\":[{\"timelineevent\":{\"ID\":\"TE500\",\"orgId\":1,\"contentGroupID\":\"CG500\",\"name\":1900,\"description\":\"The year 1900\",\"zoneId\":0,\"enabled\":1,\"startTime\":0,\"endTime\":100,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE501\",\"orgId\":2,\"contentGroupID\":\"CG500\",\"name\":1901,\"description\":\"The year 1901\",\"zoneId\":0,\"enabled\":1,\"startTime\":100,\"endTime\":200,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE502\",\"orgId\":3,\"contentGroupID\":\"CG500\",\"name\":1902,\"description\":\"The year 1902\",\"zoneId\":0,\"enabled\":1,\"startTime\":200,\"endTime\":300,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE503\",\"orgId\":4,\"contentGroupID\":\"CG500\",\"name\":1903,\"description\":\"The year 1903\",\"zoneId\":0,\"enabled\":1,\"startTime\":300,\"endTime\":400,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE504\",\"orgId\":5,\"contentGroupID\":\"CG500\",\"name\":1904,\"description\":\"The year 1904\",\"zoneId\":0,\"enabled\":1,\"startTime\":400,\"endTime\":500,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE505\",\"orgId\":6,\"contentGroupID\":\"CG500\",\"name\":1905,\"description\":\"The year 1905\",\"zoneId\":0,\"enabled\":1,\"startTime\":500,\"endTime\":600,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE506\",\"orgId\":7,\"contentGroupID\":\"CG500\",\"name\":1906,\"description\":\"The year 1906\",\"zoneId\":0,\"enabled\":1,\"startTime\":600,\"endTime\":700,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE507\",\"orgId\":8,\"contentGroupID\":\"CG500\",\"name\":1907,\"description\":\"The year 1907\",\"zoneId\":0,\"enabled\":1,\"startTime\":700,\"endTime\":800,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE508\",\"orgId\":9,\"contentGroupID\":\"CG500\",\"name\":1908,\"description\":\"The year 1908\",\"zoneId\":0,\"enabled\":1,\"startTime\":800,\"endTime\":900,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE509\",\"orgId\":10,\"contentGroupID\":\"CG500\",\"name\":1909,\"description\":\"The year 1909\",\"zoneId\":0,\"enabled\":1,\"startTime\":900,\"endTime\":1000,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE510\",\"orgId\":11,\"contentGroupID\":\"CG500\",\"name\":1910,\"description\":\"The year 1910\",\"zoneId\":0,\"enabled\":1,\"startTime\":1000,\"endTime\":1100,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE511\",\"orgId\":12,\"contentGroupID\":\"CG500\",\"name\":1911,\"description\":\"The year 1911\",\"zoneId\":0,\"enabled\":1,\"startTime\":1100,\"endTime\":1200,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE512\",\"orgId\":13,\"contentGroupID\":\"CG500\",\"name\":1912,\"description\":\"The year 1912\",\"zoneId\":0,\"enabled\":1,\"startTime\":1200,\"endTime\":1300,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE513\",\"orgId\":14,\"contentGroupID\":\"CG500\",\"name\":1913,\"description\":\"The year 1913\",\"zoneId\":0,\"enabled\":1,\"startTime\":1300,\"endTime\":1400,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE514\",\"orgId\":15,\"contentGroupID\":\"CG500\",\"name\":1914,\"description\":\"The year 1914\",\"zoneId\":0,\"enabled\":1,\"startTime\":1400,\"endTime\":1500,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE515\",\"orgId\":16,\"contentGroupID\":\"CG500\",\"name\":1915,\"description\":\"The year 1915\",\"zoneId\":0,\"enabled\":1,\"startTime\":1500,\"endTime\":1600,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE516\",\"orgId\":17,\"contentGroupID\":\"CG500\",\"name\":1916,\"description\":\"The year 1916\",\"zoneId\":0,\"enabled\":1,\"startTime\":1600,\"endTime\":1700,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE517\",\"orgId\":18,\"contentGroupID\":\"CG500\",\"name\":1917,\"description\":\"The year 1917\",\"zoneId\":0,\"enabled\":1,\"startTime\":1700,\"endTime\":1800,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE518\",\"orgId\":19,\"contentGroupID\":\"CG500\",\"name\":1918,\"description\":\"The year 1918\",\"zoneId\":0,\"enabled\":1,\"startTime\":1800,\"endTime\":1900,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE519\",\"orgId\":20,\"contentGroupID\":\"CG500\",\"name\":1919,\"description\":\"The year 1919\",\"zoneId\":0,\"enabled\":1,\"startTime\":1900,\"endTime\":2000,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE520\",\"orgId\":21,\"contentGroupID\":\"CG500\",\"name\":1920,\"description\":\"The year 1920\",\"zoneId\":0,\"enabled\":1,\"startTime\":2000,\"endTime\":2100,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE521\",\"orgId\":22,\"contentGroupID\":\"CG500\",\"name\":1921,\"description\":\"The year 1921\",\"zoneId\":0,\"enabled\":1,\"startTime\":2100,\"endTime\":2200,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE522\",\"orgId\":23,\"contentGroupID\":\"CG500\",\"name\":1922,\"description\":\"The year 1922\",\"zoneId\":0,\"enabled\":1,\"startTime\":2200,\"endTime\":2300,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE523\",\"orgId\":24,\"contentGroupID\":\"CG500\",\"name\":1923,\"description\":\"The year 1923\",\"zoneId\":0,\"enabled\":1,\"startTime\":2300,\"endTime\":2400,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE524\",\"orgId\":25,\"contentGroupID\":\"CG500\",\"name\":1924,\"description\":\"The year 1924\",\"zoneId\":0,\"enabled\":1,\"startTime\":2400,\"endTime\":2500,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE525\",\"orgId\":26,\"contentGroupID\":\"CG500\",\"name\":1925,\"description\":\"The year 1925\",\"zoneId\":0,\"enabled\":1,\"startTime\":2500,\"endTime\":2600,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE526\",\"orgId\":27,\"contentGroupID\":\"CG500\",\"name\":1926,\"description\":\"The year 1926\",\"zoneId\":0,\"enabled\":1,\"startTime\":2600,\"endTime\":2700,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE527\",\"orgId\":28,\"contentGroupID\":\"CG500\",\"name\":1927,\"description\":\"The year 1927\",\"zoneId\":0,\"enabled\":1,\"startTime\":2700,\"endTime\":2800,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE528\",\"orgId\":29,\"contentGroupID\":\"CG500\",\"name\":1928,\"description\":\"The year 1928\",\"zoneId\":0,\"enabled\":1,\"startTime\":2800,\"endTime\":2900,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE529\",\"orgId\":30,\"contentGroupID\":\"CG500\",\"name\":1929,\"description\":\"The year 1929\",\"zoneId\":0,\"enabled\":1,\"startTime\":2900,\"endTime\":3000,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE530\",\"orgId\":31,\"contentGroupID\":\"CG500\",\"name\":1930,\"description\":\"The year 1930\",\"zoneId\":0,\"enabled\":1,\"startTime\":3000,\"endTime\":3100,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE531\",\"orgId\":32,\"contentGroupID\":\"CG500\",\"name\":1931,\"description\":\"The year 1931\",\"zoneId\":0,\"enabled\":1,\"startTime\":3100,\"endTime\":3200,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE532\",\"orgId\":33,\"contentGroupID\":\"CG500\",\"name\":1932,\"description\":\"The year 1932\",\"zoneId\":0,\"enabled\":1,\"startTime\":3200,\"endTime\":3300,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE533\",\"orgId\":34,\"contentGroupID\":\"CG500\",\"name\":1933,\"description\":\"The year 1933\",\"zoneId\":0,\"enabled\":1,\"startTime\":3300,\"endTime\":3400,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE534\",\"orgId\":35,\"contentGroupID\":\"CG500\",\"name\":1934,\"description\":\"The year 1934\",\"zoneId\":0,\"enabled\":1,\"startTime\":3400,\"endTime\":3500,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE535\",\"orgId\":36,\"contentGroupID\":\"CG500\",\"name\":1935,\"description\":\"The year 1935\",\"zoneId\":0,\"enabled\":1,\"startTime\":3500,\"endTime\":3600,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE536\",\"orgId\":37,\"contentGroupID\":\"CG500\",\"name\":1936,\"description\":\"The year 1936\",\"zoneId\":0,\"enabled\":1,\"startTime\":3600,\"endTime\":3700,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE537\",\"orgId\":38,\"contentGroupID\":\"CG500\",\"name\":1937,\"description\":\"The year 1937\",\"zoneId\":0,\"enabled\":1,\"startTime\":3700,\"endTime\":3800,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE538\",\"orgId\":39,\"contentGroupID\":\"CG500\",\"name\":1938,\"description\":\"The year 1938\",\"zoneId\":0,\"enabled\":1,\"startTime\":3800,\"endTime\":3900,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE539\",\"orgId\":40,\"contentGroupID\":\"CG500\",\"name\":1939,\"description\":\"The year 1939\",\"zoneId\":0,\"enabled\":1,\"startTime\":3900,\"endTime\":4000,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE540\",\"orgId\":41,\"contentGroupID\":\"CG500\",\"name\":1940,\"description\":\"The year 1940\",\"zoneId\":0,\"enabled\":1,\"startTime\":4000,\"endTime\":4100,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE541\",\"orgId\":42,\"contentGroupID\":\"CG500\",\"name\":1941,\"description\":\"The year 1941\",\"zoneId\":0,\"enabled\":1,\"startTime\":4100,\"endTime\":4200,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE542\",\"orgId\":43,\"contentGroupID\":\"CG500\",\"name\":1942,\"description\":\"The year 1942\",\"zoneId\":0,\"enabled\":1,\"startTime\":4200,\"endTime\":4300,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE543\",\"orgId\":44,\"contentGroupID\":\"CG500\",\"name\":1943,\"description\":\"The year 1943\",\"zoneId\":0,\"enabled\":1,\"startTime\":4300,\"endTime\":4400,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE544\",\"orgId\":45,\"contentGroupID\":\"CG500\",\"name\":1944,\"description\":\"The year 1944\",\"zoneId\":0,\"enabled\":1,\"startTime\":4400,\"endTime\":4500,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE545\",\"orgId\":46,\"contentGroupID\":\"CG500\",\"name\":1945,\"description\":\"The year 1945\",\"zoneId\":0,\"enabled\":1,\"startTime\":4500,\"endTime\":4600,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE546\",\"orgId\":47,\"contentGroupID\":\"CG500\",\"name\":1946,\"description\":\"The year 1946\",\"zoneId\":0,\"enabled\":1,\"startTime\":4600,\"endTime\":4700,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE547\",\"orgId\":48,\"contentGroupID\":\"CG500\",\"name\":1947,\"description\":\"The year 1947\",\"zoneId\":0,\"enabled\":1,\"startTime\":4700,\"endTime\":4800,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE548\",\"orgId\":49,\"contentGroupID\":\"CG500\",\"name\":1948,\"description\":\"The year 1948\",\"zoneId\":0,\"enabled\":1,\"startTime\":4800,\"endTime\":4900,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE549\",\"orgId\":50,\"contentGroupID\":\"CG500\",\"name\":1949,\"description\":\"The year 1949\",\"zoneId\":0,\"enabled\":1,\"startTime\":4900,\"endTime\":5000,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE550\",\"orgId\":51,\"contentGroupID\":\"CG500\",\"name\":1950,\"description\":\"The year 1950\",\"zoneId\":0,\"enabled\":1,\"startTime\":5000,\"endTime\":5100,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE551\",\"orgId\":52,\"contentGroupID\":\"CG500\",\"name\":1951,\"description\":\"The year 1951\",\"zoneId\":0,\"enabled\":1,\"startTime\":5100,\"endTime\":5200,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE552\",\"orgId\":53,\"contentGroupID\":\"CG500\",\"name\":1952,\"description\":\"The year 1952\",\"zoneId\":0,\"enabled\":1,\"startTime\":5200,\"endTime\":5300,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE553\",\"orgId\":54,\"contentGroupID\":\"CG500\",\"name\":1953,\"description\":\"The year 1953\",\"zoneId\":0,\"enabled\":1,\"startTime\":5300,\"endTime\":5400,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE554\",\"orgId\":55,\"contentGroupID\":\"CG500\",\"name\":1954,\"description\":\"The year 1954\",\"zoneId\":0,\"enabled\":1,\"startTime\":5400,\"endTime\":5500,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE555\",\"orgId\":56,\"contentGroupID\":\"CG500\",\"name\":1955,\"description\":\"The year 1955\",\"zoneId\":0,\"enabled\":1,\"startTime\":5500,\"endTime\":5600,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE556\",\"orgId\":57,\"contentGroupID\":\"CG500\",\"name\":1956,\"description\":\"The year 1956\",\"zoneId\":0,\"enabled\":1,\"startTime\":5600,\"endTime\":5700,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE557\",\"orgId\":58,\"contentGroupID\":\"CG500\",\"name\":1957,\"description\":\"The year 1957\",\"zoneId\":0,\"enabled\":1,\"startTime\":5700,\"endTime\":5800,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE558\",\"orgId\":59,\"contentGroupID\":\"CG500\",\"name\":1958,\"description\":\"The year 1958\",\"zoneId\":0,\"enabled\":1,\"startTime\":5800,\"endTime\":5900,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE559\",\"orgId\":60,\"contentGroupID\":\"CG500\",\"name\":1959,\"description\":\"The year 1959\",\"zoneId\":0,\"enabled\":1,\"startTime\":5900,\"endTime\":6000,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE560\",\"orgId\":61,\"contentGroupID\":\"CG500\",\"name\":1960,\"description\":\"The year 1960\",\"zoneId\":0,\"enabled\":1,\"startTime\":6000,\"endTime\":6100,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE561\",\"orgId\":62,\"contentGroupID\":\"CG500\",\"name\":1961,\"description\":\"The year 1961\",\"zoneId\":0,\"enabled\":1,\"startTime\":6100,\"endTime\":6200,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE562\",\"orgId\":63,\"contentGroupID\":\"CG500\",\"name\":1962,\"description\":\"The year 1962\",\"zoneId\":0,\"enabled\":1,\"startTime\":6200,\"endTime\":6300,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE563\",\"orgId\":64,\"contentGroupID\":\"CG500\",\"name\":1963,\"description\":\"The year 1963\",\"zoneId\":0,\"enabled\":1,\"startTime\":6300,\"endTime\":6400,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE564\",\"orgId\":65,\"contentGroupID\":\"CG500\",\"name\":1964,\"description\":\"The year 1964\",\"zoneId\":0,\"enabled\":1,\"startTime\":6400,\"endTime\":6500,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE565\",\"orgId\":66,\"contentGroupID\":\"CG500\",\"name\":1965,\"description\":\"The year 1965\",\"zoneId\":0,\"enabled\":1,\"startTime\":6500,\"endTime\":6600,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE566\",\"orgId\":67,\"contentGroupID\":\"CG500\",\"name\":1966,\"description\":\"The year 1966\",\"zoneId\":0,\"enabled\":1,\"startTime\":6600,\"endTime\":6700,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE567\",\"orgId\":68,\"contentGroupID\":\"CG500\",\"name\":1967,\"description\":\"The year 1967\",\"zoneId\":0,\"enabled\":1,\"startTime\":6700,\"endTime\":6800,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE568\",\"orgId\":69,\"contentGroupID\":\"CG500\",\"name\":1968,\"description\":\"The year 1968\",\"zoneId\":0,\"enabled\":1,\"startTime\":6800,\"endTime\":6900,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE569\",\"orgId\":70,\"contentGroupID\":\"CG500\",\"name\":1969,\"description\":\"The year 1969\",\"zoneId\":0,\"enabled\":1,\"startTime\":6900,\"endTime\":7000,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE570\",\"orgId\":71,\"contentGroupID\":\"CG500\",\"name\":1970,\"description\":\"The year 1970\",\"zoneId\":0,\"enabled\":1,\"startTime\":7000,\"endTime\":7100,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE571\",\"orgId\":72,\"contentGroupID\":\"CG500\",\"name\":1971,\"description\":\"The year 1971\",\"zoneId\":0,\"enabled\":1,\"startTime\":7100,\"endTime\":7200,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE572\",\"orgId\":73,\"contentGroupID\":\"CG500\",\"name\":1972,\"description\":\"The year 1972\",\"zoneId\":0,\"enabled\":1,\"startTime\":7200,\"endTime\":7300,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE573\",\"orgId\":74,\"contentGroupID\":\"CG500\",\"name\":1973,\"description\":\"The year 1973\",\"zoneId\":0,\"enabled\":1,\"startTime\":7300,\"endTime\":7400,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE574\",\"orgId\":75,\"contentGroupID\":\"CG500\",\"name\":1974,\"description\":\"The year 1974\",\"zoneId\":0,\"enabled\":1,\"startTime\":7400,\"endTime\":7500,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE575\",\"orgId\":76,\"contentGroupID\":\"CG500\",\"name\":1975,\"description\":\"The year 1975\",\"zoneId\":0,\"enabled\":1,\"startTime\":7500,\"endTime\":7600,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE576\",\"orgId\":77,\"contentGroupID\":\"CG500\",\"name\":1976,\"description\":\"The year 1976\",\"zoneId\":0,\"enabled\":1,\"startTime\":7600,\"endTime\":7700,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE577\",\"orgId\":78,\"contentGroupID\":\"CG500\",\"name\":1977,\"description\":\"The year 1977\",\"zoneId\":0,\"enabled\":1,\"startTime\":7700,\"endTime\":7800,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE578\",\"orgId\":79,\"contentGroupID\":\"CG500\",\"name\":1978,\"description\":\"The year 1978\",\"zoneId\":0,\"enabled\":1,\"startTime\":7800,\"endTime\":7900,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE579\",\"orgId\":80,\"contentGroupID\":\"CG500\",\"name\":1979,\"description\":\"The year 1979\",\"zoneId\":0,\"enabled\":1,\"startTime\":7900,\"endTime\":8000,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE580\",\"orgId\":81,\"contentGroupID\":\"CG500\",\"name\":1980,\"description\":\"The year 1980\",\"zoneId\":0,\"enabled\":1,\"startTime\":8000,\"endTime\":8100,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE581\",\"orgId\":82,\"contentGroupID\":\"CG500\",\"name\":1981,\"description\":\"The year 1981\",\"zoneId\":0,\"enabled\":1,\"startTime\":8100,\"endTime\":8200,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE582\",\"orgId\":83,\"contentGroupID\":\"CG500\",\"name\":1982,\"description\":\"The year 1982\",\"zoneId\":0,\"enabled\":1,\"startTime\":8200,\"endTime\":8300,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE583\",\"orgId\":84,\"contentGroupID\":\"CG500\",\"name\":1983,\"description\":\"The year 1983\",\"zoneId\":0,\"enabled\":1,\"startTime\":8300,\"endTime\":8400,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE584\",\"orgId\":85,\"contentGroupID\":\"CG500\",\"name\":1984,\"description\":\"The year 1984\",\"zoneId\":0,\"enabled\":1,\"startTime\":8400,\"endTime\":8500,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE585\",\"orgId\":86,\"contentGroupID\":\"CG500\",\"name\":1985,\"description\":\"The year 1985\",\"zoneId\":0,\"enabled\":1,\"startTime\":8500,\"endTime\":8600,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE586\",\"orgId\":87,\"contentGroupID\":\"CG500\",\"name\":1986,\"description\":\"The year 1986\",\"zoneId\":0,\"enabled\":1,\"startTime\":8600,\"endTime\":8700,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE587\",\"orgId\":88,\"contentGroupID\":\"CG500\",\"name\":1987,\"description\":\"The year 1987\",\"zoneId\":0,\"enabled\":1,\"startTime\":8700,\"endTime\":8800,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE588\",\"orgId\":89,\"contentGroupID\":\"CG500\",\"name\":1988,\"description\":\"The year 1988\",\"zoneId\":0,\"enabled\":1,\"startTime\":8800,\"endTime\":8900,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE589\",\"orgId\":90,\"contentGroupID\":\"CG500\",\"name\":1989,\"description\":\"The year 1989\",\"zoneId\":0,\"enabled\":1,\"startTime\":8900,\"endTime\":9000,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE590\",\"orgId\":91,\"contentGroupID\":\"CG500\",\"name\":1990,\"description\":\"The year 1990\",\"zoneId\":0,\"enabled\":1,\"startTime\":9000,\"endTime\":9100,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE591\",\"orgId\":92,\"contentGroupID\":\"CG500\",\"name\":1991,\"description\":\"The year 1991\",\"zoneId\":0,\"enabled\":1,\"startTime\":9100,\"endTime\":9200,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE592\",\"orgId\":93,\"contentGroupID\":\"CG500\",\"name\":1992,\"description\":\"The year 1992\",\"zoneId\":0,\"enabled\":1,\"startTime\":9200,\"endTime\":9300,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE593\",\"orgId\":94,\"contentGroupID\":\"CG500\",\"name\":1993,\"description\":\"The year 1993\",\"zoneId\":0,\"enabled\":1,\"startTime\":9300,\"endTime\":9400,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE594\",\"orgId\":95,\"contentGroupID\":\"CG500\",\"name\":1994,\"description\":\"The year 1994\",\"zoneId\":0,\"enabled\":1,\"startTime\":9400,\"endTime\":9500,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE595\",\"orgId\":96,\"contentGroupID\":\"CG500\",\"name\":1995,\"description\":\"The year 1995\",\"zoneId\":0,\"enabled\":1,\"startTime\":9500,\"endTime\":9600,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE596\",\"orgId\":97,\"contentGroupID\":\"CG500\",\"name\":1996,\"description\":\"The year 1996\",\"zoneId\":0,\"enabled\":1,\"startTime\":9600,\"endTime\":9700,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE597\",\"orgId\":98,\"contentGroupID\":\"CG500\",\"name\":1997,\"description\":\"The year 1997\",\"zoneId\":0,\"enabled\":1,\"startTime\":9700,\"endTime\":9800,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE598\",\"orgId\":99,\"contentGroupID\":\"CG500\",\"name\":1998,\"description\":\"The year 1998\",\"zoneId\":0,\"enabled\":1,\"startTime\":9800,\"endTime\":9900,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE599\",\"orgId\":100,\"contentGroupID\":\"CG500\",\"name\":1999,\"description\":\"The year 1999\",\"zoneId\":0,\"enabled\":1,\"startTime\":9900,\"endTime\":10000,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE600\",\"orgId\":101,\"contentGroupID\":\"CG500\",\"name\":2000,\"description\":\"The year 2000\",\"zoneId\":0,\"enabled\":1,\"startTime\":10000,\"endTime\":10100,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE601\",\"orgId\":102,\"contentGroupID\":\"CG500\",\"name\":2001,\"description\":\"The year 2001\",\"zoneId\":0,\"enabled\":1,\"startTime\":10100,\"endTime\":10200,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE602\",\"orgId\":103,\"contentGroupID\":\"CG500\",\"name\":2002,\"description\":\"The year 2002\",\"zoneId\":0,\"enabled\":1,\"startTime\":10200,\"endTime\":10300,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE603\",\"orgId\":104,\"contentGroupID\":\"CG500\",\"name\":2003,\"description\":\"The year 2003\",\"zoneId\":0,\"enabled\":1,\"startTime\":10300,\"endTime\":10400,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE604\",\"orgId\":105,\"contentGroupID\":\"CG500\",\"name\":2004,\"description\":\"The year 2004\",\"zoneId\":0,\"enabled\":1,\"startTime\":10400,\"endTime\":10500,\"track\":0,\"rgb\":255,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE605\",\"orgId\":106,\"contentGroupID\":\"CG500\",\"name\":2005,\"description\":\"The year 2005\",\"zoneId\":0,\"enabled\":1,\"startTime\":10500,\"endTime\":10600,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE606\",\"orgId\":107,\"contentGroupID\":\"CG500\",\"name\":2006,\"description\":\"The year 2006\",\"zoneId\":0,\"enabled\":1,\"startTime\":10600,\"endTime\":10700,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE607\",\"orgId\":108,\"contentGroupID\":\"CG500\",\"name\":2007,\"description\":\"The year 2007\",\"zoneId\":0,\"enabled\":1,\"startTime\":10700,\"endTime\":10800,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE608\",\"orgId\":109,\"contentGroupID\":\"CG500\",\"name\":2008,\"description\":\"The year 2008\",\"zoneId\":0,\"enabled\":1,\"startTime\":10800,\"endTime\":10900,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE609\",\"orgId\":110,\"contentGroupID\":\"CG500\",\"name\":2009,\"description\":\"The year 2009\",\"zoneId\":0,\"enabled\":1,\"startTime\":10900,\"endTime\":11000,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE610\",\"orgId\":111,\"contentGroupID\":\"CG500\",\"name\":2010,\"description\":\"The year 2010\",\"zoneId\":0,\"enabled\":1,\"startTime\":11000,\"endTime\":11100,\"track\":0,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE611\",\"orgId\":112,\"contentGroupID\":\"CG500\",\"name\":2011,\"description\":\"The year 2011\",\"zoneId\":0,\"enabled\":1,\"startTime\":11100,\"endTime\":11200,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE612\",\"orgId\":113,\"contentGroupID\":\"CG500\",\"name\":2012,\"description\":\"The year 2012\",\"zoneId\":0,\"enabled\":1,\"startTime\":11200,\"endTime\":11300,\"track\":0,\"rgb\":65280,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE613\",\"orgId\":114,\"contentGroupID\":\"CG500\",\"name\":2013,\"description\":\"The year 2013\",\"zoneId\":0,\"enabled\":1,\"startTime\":11300,\"endTime\":11400,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE614\",\"orgId\":115,\"contentGroupID\":\"CG500\",\"name\":2014,\"description\":\"The year 2014\",\"zoneId\":0,\"enabled\":1,\"startTime\":11400,\"endTime\":11500,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE615\",\"orgId\":116,\"contentGroupID\":\"CG500\",\"name\":2015,\"description\":\"The year 2015\",\"zoneId\":0,\"enabled\":1,\"startTime\":11500,\"endTime\":11600,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE616\",\"orgId\":117,\"contentGroupID\":\"CG500\",\"name\":2016,\"description\":\"The year 2016\",\"zoneId\":0,\"enabled\":1,\"startTime\":11600,\"endTime\":11700,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE617\",\"orgId\":118,\"contentGroupID\":\"CG500\",\"name\":2017,\"description\":\"The year 2017\",\"zoneId\":0,\"enabled\":1,\"startTime\":11700,\"endTime\":11800,\"track\":0,\"rgb\":65535,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE618\",\"orgId\":119,\"contentGroupID\":\"CG500\",\"name\":2018,\"description\":\"The year 2018\",\"zoneId\":0,\"enabled\":1,\"startTime\":11800,\"endTime\":11900,\"track\":0,\"rgb\":16711680,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE619\",\"orgId\":120,\"contentGroupID\":\"CG500\",\"name\":2019,\"description\":\"The year 2019\",\"zoneId\":0,\"enabled\":1,\"startTime\":11900,\"endTime\":12000,\"track\":0,\"rgb\":16744512,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE620\",\"orgId\":121,\"contentGroupID\":\"CG500\",\"name\":2020,\"description\":\"The year 2020\",\"zoneId\":0,\"enabled\":1,\"startTime\":12000,\"endTime\":12100,\"track\":0,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE621\",\"orgId\":122,\"contentGroupID\":\"CG500\",\"name\":\"Wedding\",\"description\":\"The first wedding in your community took place on Sunday Afternoon.Both bride and groom came to Woolwich over 3 years ago.After the ceremony the bridal party returned to their home, were breakfast and dinner were served and dancing engaged in.\",\"zoneId\":2,\"enabled\":1,\"startTime\":504,\"endTime\":554,\"track\":1,\"rgb\":16744512,\"health\":4,\"wealth\":5,\"brains\":4,\"action\":5,\"instant\":1,\"flags\":15,\"absolute\":0}},{\"timelineevent\":{\"ID\":\"TE622\",\"orgId\":123,\"contentGroupID\":\"CG500\",\"name\":\"Arrival\",\"description\":\"You have arrived in the town of Woolwich, on the outskirts of London, England. It has been a long journey and you are far from home. You need to find a place to stay and settle for awhile in this new place.\",\"zoneId\":1,\"enabled\":1,\"startTime\":55,\"endTime\":105,\"track\":1,\"rgb\":9319919,\"health\":-4,\"wealth\":-4,\"brains\":4,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":0}},{\"timelineevent\":{\"ID\":\"TE623\",\"orgId\":125,\"contentGroupID\":\"CG500\",\"name\":\"Arsenal Football team moves\",\"description\":\"Now a professional football team, the Arsenal team started by workers at the Gunners factory at the Aresnal moves to North London.\",\"zoneId\":3,\"enabled\":1,\"startTime\":1293,\"endTime\":1343,\"track\":1,\"rgb\":9319919,\"health\":0,\"wealth\":-5,\"brains\":-5,\"action\":-5,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE624\",\"orgId\":126,\"contentGroupID\":\"CG500\",\"name\":\"Haleys Comet\",\"description\":\"The Great Daylight Comet is sighted in the skies over London. Warning to the Inhabitants of the City: Close your windows and keep indoors for the Earth will soon pass through the Tail of the terrible Comet and its poisonous gases will fill the heavens! \",\"zoneId\":1,\"enabled\":1,\"startTime\":1022,\"endTime\":1072,\"track\":1,\"rgb\":16776960,\"health\":-5,\"wealth\":0,\"brains\":-5,\"action\":5,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE625\",\"orgId\":127,\"contentGroupID\":\"CG500\",\"name\":\"World War One\",\"description\":\"WW1 has been declared\",\"zoneId\":0,\"enabled\":1,\"startTime\":1217,\"endTime\":1267,\"track\":1,\"rgb\":16711680,\"health\":-5,\"wealth\":5,\"brains\":5,\"action\":5,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE626\",\"orgId\":128,\"contentGroupID\":\"CG500\",\"name\":\"Jobs For Women at the Arsenal!\",\"description\":\"Since 1872 women had been banned from working in the Arsenal, but with the start of WW1 all our boys are going to war. For the duration of the war women munitions workers are required.\",\"zoneId\":3,\"enabled\":1,\"startTime\":1349,\"endTime\":1399,\"track\":1,\"rgb\":16776960,\"health\":-1,\"wealth\":4,\"brains\":5,\"action\":5,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE627\",\"orgId\":135,\"contentGroupID\":\"CG500\",\"name\":\"Houses for 6 pounds a week\",\"description\":\"Cheap housing is available for newcomers to Woolwich, some local shops, some rundown cottages near Dairy Lane and Frances Street. Take care of the vagrants and overcrowding in this area.\",\"zoneId\":5,\"enabled\":1,\"startTime\":44,\"endTime\":94,\"track\":2,\"rgb\":16711680,\"health\":-5,\"wealth\":-5,\"brains\":-5,\"action\":5,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE628\",\"orgId\":136,\"contentGroupID\":\"CG500\",\"name\":\"Wellington Cottages\",\"description\":\"The local policemen have moved into this area and it is therefore regarded as a safe place to live. \",\"zoneId\":6,\"enabled\":1,\"startTime\":99,\"endTime\":149,\"track\":2,\"rgb\":65280,\"health\":0,\"wealth\":5,\"brains\":0,\"action\":5,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE629\",\"orgId\":137,\"contentGroupID\":\"CG500\",\"name\":\"Jobs Available in the Arsenal\",\"description\":\"Jobs are available at the Arsenal for the men in your community. Houses are available for the Arsenal workers in Plumstead and near the common. Beware temptation in the many public houses. It is an eight hour day and overtime to be had.\",\"zoneId\":3,\"enabled\":1,\"startTime\":102,\"endTime\":152,\"track\":4,\"rgb\":16776960,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE630\",\"orgId\":138,\"contentGroupID\":\"CG500\",\"name\":\"Jobs Available In The Arsenal\",\"description\":\"Jobs are available at the Arsenal for the men in your community. Houses are available for the Arsenal workers in Plumstead and near the common. Beware temptation in the many public houses. It is an eight hour day and overtime to be had.\",\"zoneId\":3,\"enabled\":1,\"startTime\":53,\"endTime\":103,\"track\":3,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}},{\"timelineevent\":{\"ID\":\"TE631\",\"orgId\":123,\"contentGroupID\":\"CG500\",\"name\":\"unnamed123\",\"zoneId\":0,\"enabled\":1,\"startTime\":912,\"endTime\":962,\"track\":4,\"rgb\":9319919,\"health\":0,\"wealth\":0,\"brains\":0,\"action\":0,\"instant\":1,\"flags\":15,\"absolute\":1}}]}";
		//generateJSONGameObjects(json);

		if (address == null || address.trim().equals("")){
			address = BASEURL;
		}

		String url = address + "author/getcontent.html?contentGroupId=" + gameId;

		//String url = GET_PROXY + "?url=" + address + "author/getcontent.html?contentGroupId=" + gameId;

		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, URL.encode(url));

		try{
			Request request = builder.sendRequest(null, new RequestCallback(){

				@Override
				public void onError(Request request, Throwable exception) {
					// TODO Auto-generated method stub
					Log.debug("error " + exception.getMessage());
				}

				@Override
				public void onResponseReceived(Request request,
						Response response) {
					if (200 == response.getStatusCode()){
						Log.debug(response.getText());
						generateJSONGameObjects(response.getText());


					}else{ 
						Log.debug("got following " + response.getText());
					}
				}

			});

		}catch(Exception e){
			Log.debug("error " + e.getMessage());
		}
	}


	public static void addRootZones(){
		ColourMap.reset();
		zoneIndex = 0;

		Zone timelinezone = new Zone(String.valueOf(zoneIndex), "time", null, ColourMap.getNextColour());
		String key = getKey("0", TimeLineEntry.TIMELINE);
		zones.put(key, timelinezone);
		eventBus.fireEvent(new ZoneCreatedEvent("0", TimeLineEntry.TIMELINE, false));

		Zone globalzone = new Zone(String.valueOf(zoneIndex), "global", null, ColourMap.getNextColour());
		key = getKey("0", TimeLineEntry.GLOBAL);
		zones.put(key, globalzone);
		eventBus.fireEvent(new ZoneCreatedEvent("0", TimeLineEntry.GLOBAL, false));

		zoneIndex+=1;
	}

	static void generateJSONListObjects(String json){
		try{

			liststore.removeAll();

			JSONValue value = JSONParser.parse(json);	
			JSONObject obj = value.isObject();
			JSONArray jarray = obj.get("contentgroups").isArray();

			for (int i = 0; i < jarray.size(); i++){

				try{
					JSONObject cgobject = jarray.get(i).isObject();
					uk.ac.nottingham.horizon.exploding.author.client.jsmodel.ContentGroup cg = buildContentGroup(cgobject.toString());
					liststore.add(new GameListEntry(cg.getID(),cg.getName(),cg.getLocation(), String.valueOf(cg.getStartYear()), String.valueOf(cg.getEndYear())));
				}catch(Exception e){
					Log.debug(e.getMessage());
				}
			}
		}catch(Exception e){
			Log.debug("error...." + e.getMessage());
		}
	}


	static	void generateJSONGameObjects(String json){


		timelinestore.removeAll();
		zones.clear();
		zoneIndex = 0;
		addRootZones();
		//addGlobalZone();

		try{
			JSONValue value = JSONParser.parse(json);
			JSONObject obj = value.isObject();
			JSONArray jarray = obj.get("zones").isArray();

			for (int i = 0; i < jarray.size(); i++){
				try{
					JSONObject jobj = jarray.get(i).isObject();

					JSONObject zoneobject = jobj.get("zone").isObject();
					uk.ac.nottingham.horizon.exploding.author.client.jsmodel.Zone z = buildZone(zoneobject.toString());
					Log.debug("zone id is " + z.getID());

					JSONArray coords = zoneobject.get("coordinates").isArray();
					//Log.debug(coords.toString());

					for (int x = 0; x < coords.size(); x++){
						JSONObject position = coords.get(x).isObject();
						JSONArray positionarray = position.get("position").isArray();
						JsArray<LatLngElevation> llelist = getPosition(positionarray.toString());
						LatLng[] latLngArray = new LatLng[llelist.length()];
						for (int y = 0; y < llelist.length(); y++){
							LatLngElevation lle = llelist.get(y);
							latLngArray[y] = LatLng.newInstance(lle.getLatitude(),lle.getLongitude());

							//if this is the first point we've seen, center the map on it
							if (i==0 && x == 0 && y==0){
								Log.debug("centering map on first point");
								eventBus.fireEvent(new ZoneMapCenterEvent(latLngArray[x]));
							}
						}
						final String colour = ColourMap.getNextColour();
						Polygon poly = createPolygon(latLngArray, colour);
						Zone azone = new Zone(String.valueOf(z.getOrgId()), z.getName(), poly, colour);
						addZone(azone, TimeLineEntry.DEFAULT);
					}

				}catch(Exception e){
					Log.debug(e.getMessage());
				}
			}

			jarray = obj.get("timeevents").isArray();

			ArrayList<TimeLineEntry> entries = new ArrayList<TimeLineEntry>();
			int max = 0;

			for (int i = 0; i < jarray.size(); i++){
				try{
					JSONObject jobj = jarray.get(i).isObject();
					JSONObject tleobject = jobj.get("timelineevent").isObject();
					uk.ac.nottingham.horizon.exploding.author.client.jsmodel.TimeLineEvent t = buildTimeLineEvent(tleobject.toString());
					int column = TimeConverter.getColumnFromStartTime((int)t.getStartTime());
					int sequence = TimeConverter.getSequenceFromStartTime((int)t.getStartTime());



					max = Math.max(max, column);
					HashMap<String, Integer> attributes = new HashMap<String, Integer>();
					attributes.put("health", t.getHealth());
					attributes.put("wealth", t.getWealth());
					attributes.put("brains", t.getBrains());
					attributes.put("action", t.getAction());
					attributes.put("instant", t.getInstant());
					attributes.put("flags", t.getFlags());
					attributes.put("absolute", t.getAbsolute());

					TimeLineEntry tle = new TimeLineEntry(String.valueOf(i), String.valueOf(t.getZoneId()), t.getName(), t.getDescription(), column, sequence, attributes); 

					if (String.valueOf(t.getZoneId()).equals("0")){
						if (t.getTrack() == 0)
							tle.setType(TimeLineEntry.TIMELINE);
						else
							tle.setType(TimeLineEntry.GLOBAL);
					}
					tle.setTrack(t.getTrack());
					entries.add(tle);

				}catch(Exception e){
					Log.debug("timeline parse exception " + e.getMessage());
				}
			}


			prepareGrid(0, max);

			for (TimeLineEntry t: entries)
				updateTimeLineEntry(t);

			eventBus.fireEvent(new GameStateLoadedEvent());

		}catch(Exception e){
			Log.debug("ERROR " + e.getMessage());
		}
	}


	private static Polygon createPolygon(LatLng[] latLngArray, String colour){

		double opacity = 1.0;
		int weight = 1;

		PolyStyleOptions style = PolyStyleOptions.newInstance(colour, weight,
				opacity);

		final Polygon poly = new Polygon(latLngArray, colour, weight, opacity,
				colour, .7);

		poly.addPolygonClickHandler(new PolygonClickHandler(){

			@Override
			public void onClick(PolygonClickEvent event) {

				if (zones.size() > 0){

					for (String key : zones.keySet()){
						Polygon p = zones.get(key).getPolygon();
						if (p!=null){
							p.setEditingEnabled(false);
						}
					}
				}
				poly.setEditingEnabled(true);
			}
		});

		poly.setStrokeStyle(style);
		return poly;
	}




	/*
	 * Timeline entry stuff
	 */

	/*
	 * Add a new time line (timeline model) 
	 */

	public static void insertTimeLine(String zoneid, int type){
		TimeLineModel m = getNewTimeLine(zoneid, type);
		timelinestore.add(m);
	}

	/*
	 * Return the time line entry corresponding to the the zone, column and sequencenumber, generate it if it doesn't exist.
	 */

	public static TimeLineEntry getTimeLineEntry(String zone, int type, int column, int sequence ){//, boolean generate){

		//Log.debug("zone is " + zone + " type is " + type + " column is " + column  + " sequence is " + sequence + " datastore size is " +  timelinestore.getCount());
		for (int i = 0; i < timelinestore.getCount(); i++){

			TimeLineModel m = timelinestore.getAt(i);

			//horrible here...

			if ( (m.getZone().getId().equals(zone) && !zone.equals("0"))   ||  ( zone.equals("0") && i==type ) ){


				ArrayList<TimeLineEntry> entries;

				if ((entries = m.get("name"+column)) == null){

					entries = new ArrayList<TimeLineEntry>();
					TimeLineEntry t = new TimeLineEntry(TimeLineIdGenerator.getID(), zone, "", "", column, sequence, null);

					t.setType(type);
					if (zone.equals("0") && i == 0){
						t.setTrack(0);

					}
					entries.add(t);
					m.set("name"+column, entries);
					return t;
				}

				for (int x =0; x < entries.size(); x++){
					if (entries.get(x).getSequence() == sequence){

						return entries.get(x);
					}
				}

				TimeLineEntry t = new TimeLineEntry(TimeLineIdGenerator.getID(), zone, "", "", column, sequence, null);
				t.setType(type);

				if (zone.equals("0") && i == 0){

					t.setTrack(0);
				}

				entries.add(t);
				return t;
			}
		}
		Log.debug("returning NULL!!!");
		return null;
	}


	public static ArrayList<TimeLineEntry> getTimeLineEntries(String zone, int row, int column){

		if (column <= 0)
			return null;


		for (int i = 0; i < Datastore.getStore().getCount(); i++){
			TimeLineModel m = Datastore.getStore().getAt(i);
			if (m.getZone().getId().equals(zone)){

				ArrayList<TimeLineEntry> entries;

				if ((entries = m.get("name"+column)) == null){
					entries = new ArrayList<TimeLineEntry>();
					TimeLineEntry tle = new TimeLineEntry(TimeLineIdGenerator.getID(),zone);
					if (row == 0){
						tle.setType(TimeLineEntry.TIMELINE);
						tle.setTrack(0);
					}
					if (row == 1){
						tle.setType(TimeLineEntry.GLOBAL);
						tle.setTrack(1);
					}
					entries.add(tle);
					m.set("name"+column, entries);
				}
				return entries;
			}	
		}	
		return null;
	}

	public static void updateTimeLineEntry(TimeLineEntry tle){

		/*
		 * if it's track 0, it's a timeline event!
		 */
		if (tle.getType() == TimeLineEntry.TIMELINE){
			TimeLineModel m = timelinestore.getAt(0);

			ArrayList<TimeLineEntry> entries = m.get("name"+tle.getColumn());

			if (entries==null){
				entries = new ArrayList<TimeLineEntry>();
			}
			addEntry(entries,tle);
			m.set("name"+tle.getColumn(), entries);
			return;
		}

		/*
		 * special case, global event i.e track is not 0, but zone is.
		 */
		if (tle.getType() == TimeLineEntry.GLOBAL){
			TimeLineModel m = timelinestore.getAt(1);
			ArrayList<TimeLineEntry> entries = m.get("name"+tle.getColumn());
			if (entries==null){
				entries = new ArrayList<TimeLineEntry>();
			}
			addEntry(entries,tle);
			m.set("name"+tle.getColumn(), entries);
			return;
		}


		for (int i = 0; i < timelinestore.getCount(); i++){

			TimeLineModel m = timelinestore.getAt(i);

			if (m.getZone().getId().equals(tle.getZoneid())){

				ArrayList<TimeLineEntry> entries = m.get("name"+tle.getColumn());

				if (entries==null){
					entries = new ArrayList<TimeLineEntry>();
				}
				addEntry(entries,tle);
				m.set("name"+tle.getColumn(), entries);
			}
		}
	}

	public static void addEntry(ArrayList<TimeLineEntry> entries, TimeLineEntry t){
		for (int i = 0; i < entries.size(); i++){
			TimeLineEntry tle = entries.get(i);
			if (tle.getId()==t.getId()){
				entries.set(i, t);
				return;
			}
		}
		entries.add(t);
	}

	public static void addRenderers(){
		configs.get(0).setRenderer(CellRenderer.zonerenderer);
	}

	private static void prepareGrid(int from, int to){

		configs = new ArrayList<ColumnConfig>();

		ColumnConfig column = new ColumnConfig();
		TextField<String> text = new TextField<String>();  
		column.setId("name0");
		column.setHeader("zone");
		column.setWidth(40);
		text.setAllowBlank(false);  
		column.setEditor(new CellEditor(text));  
		configs.add(column);  

		for (int i =(from-from)+1; i < (to-from)+1; i++){
			ColumnConfig c = new ColumnConfig("name"+i, String.valueOf(i), 35);
			c.setRenderer(CellRenderer.tlerenderer);
			configs.add(c);

		}

		cm = new ColumnModel(configs);
		configs.get(0).setRenderer(CellRenderer.zonerenderer);
	}

	private static TimeLineModel getNewTimeLine(String zoneid, int type){
		HashMap<String, Object> m = new HashMap<String, Object>();
		Zone z = Datastore.getZone(zoneid,type);
		m.put("name0", z.getName());
		TimeLineModel model = new TimeLineModel(z,m);
		return model;
	}

	public static void setColumnConfigs(ArrayList<ColumnConfig> cc){
		configs = cc; 
	}

	public static ColumnModel getColumnModel(){
		return cm;
	}

	public static void addNewColumn(){

		int column = configs.size();
		ColumnConfig c = new ColumnConfig("name"+column, String.valueOf(column), 35);
		c.setRenderer(CellRenderer.tlerenderer);
		configs.add(c); 

		cm = new ColumnModel(configs);

		int count = 0;
		for (TimeLineModel m : timelinestore.getModels()){
			ArrayList<TimeLineEntry> entries = new ArrayList<TimeLineEntry>();
			TimeLineEntry t = new TimeLineEntry(TimeLineIdGenerator.getID(), m.getZone().getId(), "", "", column, 0, null);
			if (count==0){
				t.setTrack(0);
				t.setType(TimeLineEntry.TIMELINE);
			}
			else if (count == 1){
				t.setTrack(1);
				t.setType(TimeLineEntry.GLOBAL);
			}
			entries.add(t);
			m.set("name" + column , entries);	
			count +=1;
		}
	}

	/*
	 * Javascript overlay types conversion stuff
	 */

	public static final native uk.ac.nottingham.horizon.exploding.author.client.jsmodel.TimeLineEvent buildTimeLineEvent(String json) /*-{
		return eval('(' + json + ')');
	}-*/;

	public static final native uk.ac.nottingham.horizon.exploding.author.client.jsmodel.Zone buildZone(String json) /*-{
		return eval('(' + json + ')');
	}-*/;

	public static final native uk.ac.nottingham.horizon.exploding.author.client.jsmodel.ContentGroup buildContentGroup(String json) /*-{
		return eval('(' + json + ')');
	}-*/;

	public static final native JsArray<LatLngElevation> getPosition(String json)/*-{
		return eval('(' + json + ')');
	}-*/;

	public static final native uk.ac.nottingham.horizon.exploding.author.client.jsmodel.LatLngElevation buildLongLatElevation(String json) /*-{
		return eval('(' + json + ')');
	}-*/;
}
