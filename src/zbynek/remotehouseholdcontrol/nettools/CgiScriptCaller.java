package zbynek.remotehouseholdcontrol.nettools;

import java.io.IOException;
import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;
import android.support.v4.util.SimpleArrayMap;
import zbynek.remotehouseholdcontrol.Data;

import 	java.util.Random;

import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;

public class CgiScriptCaller {

	ConnectionCredentialsManager cm;
	String scriptNamePulse;
	String scriptNameSwitch;
	String scriptNameStatus;	
	String scriptNameGraph;	
	
	public CgiScriptCaller(ConnectionCredentialsManager cm) {
		this.cm = cm;
		this.scriptNamePulse 	= "pulse.cgi";
		this.scriptNameSwitch 	= "switch.cgi";
		this.scriptNameStatus 	= "status.cgi";
		this.scriptNameGraph 	= "graph.cgi";
	}
	
	public boolean GetRelayStatus(int rele_id) {

	    SimpleArrayMap<String, String> m = Data.get();
	    if (m != null) {
/*	    	String IOport = rele_id < 9 	? m.get(getString(R.string.P3))
	    									: m.get(getString(R.string.P5));
	    	int port_result = (IOport!=null) ? Integer.parseInt((IOport.trim()).toString()):1;
			boolean rele_status = (port_result & rele_id) > 0 ? true : false;		
	*/    }
			return true; 
		}
	/**	 * refresh data with result of each CGI script 
	 * @param device_id */
	public static boolean refreshData(String statusesIO) 
			throws IOException {
		String valueParser 		= "/";
		String valueDivider 	= ";";
		String val_Status 		= "";
		
		SimpleArrayMap<String, String> m = new SimpleArrayMap<String, String>();
		String[] values=statusesIO.split(valueParser);
		for (int i = 0; i < values.length; i++){		
			String[]  elements = values[i].split(valueDivider);		
			if (elements[0].contains("Status"))   	val_Status 	= elements[1];
			 if (elements[0] !=null && elements[1] !=null)	m.put(elements[0], elements[1]);
		}
		if (val_Status.contains("OK")) {
			Data.setMap(m);
			return true;} 
		else 	
			return false;
	}

// randoms v url jsou pridany kvuli proxy sixx.org
	/**	 * Calls a CGI script pulse and returns true if it finished successfully (returns "0") */
	public boolean callCGIScriptPulse(int device_id) throws IOException {
		Random r = new Random();
		String urlString = cm.constructUrl(scriptNamePulse) + "?device=" + device_id+ "?random=" + r.nextInt(1000000);	
		String scriptOutput = UrlReader.readOutputFromUrl(cm, urlString);
	return	refreshData(scriptOutput);		
  	}

	/**	 * Calls a CGI script and returns true if it finished successfully (returns "0")	 * @param setOn set the script to ON or OFF status. */
	public boolean callCGIScriptAndSetValue(boolean setOn, int device_id) throws IOException {
		Random r = new Random();
		String value = setOn ? "1" : "0";
		String urlString = cm.constructUrl(scriptNameSwitch) + "?value=" + value+ "?device=" + device_id + "?random=" + r.nextInt(1000000);	
		String scriptOutput = UrlReader.readOutputFromUrl(cm, urlString);
		return	refreshData(scriptOutput);		
  	}
	
	/**	 * Calls a CGI script and returns actual status - ON (true) / OFF (false)*/
	public boolean callCGIScriptAndGetStatus(int device_id) throws IOException {
		Random r = new Random();
		String urlString = cm.constructUrl(scriptNameStatus+ "?random=" + r.nextInt(1000000));	
		String scriptOutput = UrlReader.readOutputFromUrl(cm, urlString);
		refreshData(scriptOutput);
		return refreshData(scriptOutput);
	}

	/**	 * Calls a CGI script and returns data for graph*/
	public String callCGIScriptAndGetGraphData(int sensor_id) throws IOException {
		Random r = new Random();
		String urlString = cm.constructUrl(scriptNameGraph) + "?device=" + sensor_id+ "?random=" + r.nextInt(1000000);		
		String scriptOutput = UrlReader.readOutputFromUrl(cm, urlString);
		return scriptOutput;
	}
}