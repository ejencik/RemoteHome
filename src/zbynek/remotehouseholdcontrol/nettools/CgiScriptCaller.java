package zbynek.remotehouseholdcontrol.nettools;

import java.io.IOException;

import android.os.AsyncTask;

import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;
import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;

public class CgiScriptCaller {

	
	ConnectionCredentialsManager cm;
	String scriptName;	
	String scriptNamePulse;
	String scriptNameSwitch;
	
	public CgiScriptCaller(ConnectionCredentialsManager cm) {
		this.cm = cm;
	//	this.scriptName = scriptName;
		this.scriptName = "heating.cgi";
		this.scriptNamePulse = "pulse.cgi";
		this.scriptNamePulse = "switch.cgi";
	}
	
	
	

	/**
	 * Calls a CGI script pulse and returns true if it finished successfully (returns "0")
	 */
	public boolean callCGIScriptPulse(int Device) throws IOException {
		String urlString = cm.constructUrl(scriptNamePulse) + "?device=" + Device;	
		String scriptOutput = UrlReader.readOutputFromUrl(cm, urlString);
		return "OK".equals(scriptOutput.trim());
	}
	/**
	 * Calls a CGI script and returns true if it finished successfully (returns "0")
	 * @param setOn set the script to ON or OFF status.
	 */

	public boolean callCGIScriptAndSetValue(boolean setOn) throws IOException {
		String value = setOn ? "on" : "off";
		String urlString = cm.constructUrl(scriptName) + "?value=" + value;	
		String scriptOutput = UrlReader.readOutputFromUrl(cm, urlString);
		return "OK".equals(scriptOutput.trim());
	}
	
	/**
	 * Calls a CGI script and returns actual status - ON (true) / OFF (false)
	 */
	public boolean callCGIScriptAndGetStatus() throws IOException {
		String urlString = cm.constructUrl(scriptName) + "?value=status";	
		String scriptOutput = UrlReader.readOutputFromUrl(cm, urlString);
		if ("1".equals(scriptOutput.trim())) {
			return true;
		} else if ("0".equals(scriptOutput.trim())) {
			return false;
		} else {
			throw new IOException("Script returned an invalid status.");
		}
	}
	
	public boolean pokus(boolean isChecked) throws IOException {
		AsyncTask<Boolean, Void, Boolean> setHeatingStatusTask = new AsyncTask<Boolean, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Boolean ... params) {
				boolean isChecked = params[0];
				CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
				boolean success;
				try {
					success = scriptCaller.callCGIScriptAndSetValue(isChecked);
					if (!success) {throw new IOException("Script failed.");}
				} catch (IOException e) {
					return false;
										}
				return true;
			}
		};
		boolean result;
		try {
			result = setHeatingStatusTask.execute(isChecked).get();
		} catch (Exception e) { //collect all possible exceptions
//			Toast.makeText(getActivity(),"Exception setHeating", Toast.LENGTH_LONG).show();					
			result = false;
		}
		if (!result) {
		//	showError("chybka 1");
		//	button.setChecked(!isChecked); //set previous state
		}
		return result;
	}
	

}
