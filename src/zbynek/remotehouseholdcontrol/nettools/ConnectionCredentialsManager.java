package zbynek.remotehouseholdcontrol.nettools;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import zbynek.remotehouseholdcontrol.R;


public class ConnectionCredentialsManager {

	private static final String HTTP_PROTOCOL = "http";
	
	private static final String PASSWORD = "password";
	private static final String USERNAME = "username";
	private static final String DIR = "dir";
	private static final String PORT = "port";
	private static final String DOMAIN = "domain";
	private static final String AUTOCONFIG = "autoconfig";


	private SharedPreferences sharedPref;
  private final Context context;
	
	public ConnectionCredentialsManager(Context context) {
    this.context = context;
		sharedPref = context.getSharedPreferences("ConnectionPreferences", Context.MODE_PRIVATE);
	}
	
//	public void saveCredentials(String domain, String port, String dir, String username, 
	//		String password, Boolean autoconfig) 
	public void saveCredentials(String domain, String port, String dir, String username, String password) 
	{		
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putString(DOMAIN, domain);
		editor.putString(PORT, port);
		editor.putString(DIR, dir);
		editor.putString(USERNAME, username);
		editor.putString(PASSWORD, password);
		editor.putBoolean(AUTOCONFIG, true);
		editor.commit();	
	}
	
	public String getDomain() {
		return sharedPref.getString(DOMAIN, "192.168.111.1");		
	}
	
	public String getPort() {
		return sharedPref.getString(PORT, "8081");		
	}

	public String getDir() {
		return sharedPref.getString(DIR, "/intranet/xml");		
	}

	public String getUsername() {
		return sharedPref.getString(USERNAME, "");		
	}

	public String getPassword() {
		return sharedPref.getString(PASSWORD, "");		
	}
	
	public Boolean getAutoconfig() {
		return sharedPref.getBoolean(AUTOCONFIG, true);	
	}
	
	public String getWifiName() {
		String ssid = "none";
		WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (DetailedState.CONNECTED.equals(WifiInfo.getDetailedStateOf(
      wifiInfo.getSupplicantState())))
			ssid = wifiInfo.getSSID();
		return ssid;
	}

	// TZE  pokud jsem doma a telefon vidi domaci wifi kulisaci tak pouzit HTTP_PROTOCOL+"://192.168.111.1:8081/xml/,  
	//jinak pouzit konfiguraci podle nastaveni 
	
	public String constructUrl(String file) {
    if (context.getString(R.string.ssid_home).equals(getWifiName()))
      return context.getString(R.string.url_internal) + file;
		return HTTP_PROTOCOL+"://"+getDomain()+":"+getPort()+"/"+getDir()+"/"+file;
	}
}