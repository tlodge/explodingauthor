package uk.ac.nottingham.horizon.exploding.author.client.datastore;

import uk.ac.nottingham.horizon.exploding.author.client.model.InstanceListEntry;

import com.allen_sauer.gwt.log.client.Log;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;

public class InstanceDataStore {
	private static ListStore<InstanceListEntry> instancestore = new ListStore<InstanceListEntry>();

	public static ListStore<InstanceListEntry> getInstanceStore(){
		return instancestore;
	}

	public static void listInstancesDEBUG(){
		String json = "{\"instances\":[{\"id\":\"anid1\", \"image\":\"animage\", \"status\":\"a status\", \"dns\":\"aws.compute.aservice\"},{\"id\":\"anid2\", \"image\":\"animage2\", \"status\":\"a status2\", \"dns\":\"2aws.compute.aservice\"}]}";
		loadInstanceList(json);
	}

	public static void listInstances(){

		String url = GWT.getHostPageBaseURL().replace("explodingauthor/", "");
		url += "listinstances.php";

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
						loadInstanceList(response.getText());
					}else{ 
						Log.debug("got following " + response.getText());
					}
				}

			});

		}catch(Exception e){
			Log.debug("error " + e.getMessage());
		}
	}

	static void loadInstanceList(String json){
		ListStore<InstanceListEntry> tmpstore = new ListStore<InstanceListEntry>();
		
		try{

			JSONValue value = JSONParser.parse(json);	
			JSONObject obj = value.isObject();
			JSONArray jarray = obj.get("instances").isArray();

			for (int i = 0; i < jarray.size(); i++){

				try{
					JSONObject iiobject = jarray.get(i).isObject();
					uk.ac.nottingham.horizon.exploding.author.client.jsmodel.InstanceItem ii = buildInstanceItem(iiobject.toString());
					Log.debug(ii.getId() + "  "  + ii.getImage() + "  " + ii.getStatus() + " " +  ii.getDns());
					tmpstore.add(new InstanceListEntry(ii.getId(),ii.getImage(),ii.getStatus(), ii.getDns()));
				}catch(Exception e){
					Log.debug(e.getMessage());
				}
			}
		}catch(Exception e){
			Log.debug("error...." + e.getMessage());
		}
		
		/*
		 * check to see if any updates
		 */
		if (tmpstore.getCount() != instancestore.getCount()){
			Log.debug("UPDATING...");
			instancestore.removeAll();
			instancestore.add(tmpstore.getModels());
			
			return;
		}
		
		for (int i = 0; i < tmpstore.getCount(); i++){
			if (!(tmpstore.getAt(i).equals(instancestore.getAt(i)))){
				Log.debug("UPDATING...");
				instancestore.removeAll();
				instancestore.add(tmpstore.getModels());
				return;
			}
		}
	}

	public static void callAWS(String url){
	

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
					}else{ 
						Log.debug("got following " + response.getText());
					}
				}

			});

		}catch(Exception e){
			Log.debug("error " + e.getMessage());
		}
	
	}
	
	public static void stopInstance(String instanceid){
		String url = GWT.getHostPageBaseURL().replace("explodingauthor/", "");
		url += "stopinstance.php?instanceid="+instanceid;
		Log.debug("calling url " + url);
		callAWS(url);
	}
	
	public static void startInstance(String instanceid){
		String url = GWT.getHostPageBaseURL().replace("explodingauthor/", "");
		url += "startinstance.php?instanceid="+instanceid;
		Log.debug("calling url " + url);
		callAWS(url);
	}
	
	public static final native uk.ac.nottingham.horizon.exploding.author.client.jsmodel.InstanceItem buildInstanceItem(String json) /*-{
		return eval('(' + json + ')');
	}-*/;

}
