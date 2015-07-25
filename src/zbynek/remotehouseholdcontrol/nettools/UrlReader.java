package zbynek.remotehouseholdcontrol.nettools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.HttpURLConnection;
import java.net.PasswordAuthentication;
import java.net.URL;


public class UrlReader {

	public static String readOutputFromUrl(final ConnectionCredentialsManager cm, String urlString) throws IOException {
		BufferedReader br = null;
		try {
			URL url = new URL(urlString);
			Authenticator.setDefault(new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(cm.getUsername(), cm.getPassword().toCharArray());
				}
			});
			HttpURLConnection.setFollowRedirects(true);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(10000 /* milliseconds */);
			conn.setConnectTimeout(15000 /* milliseconds */);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);
			conn.setUseCaches(false);
			conn.connect();
			int response = conn.getResponseCode(); // should be 200
			if (response != 200) {
				throw new IOException("Response from the server: "+response);
			}
			br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		} finally {
			if (br != null) {
				br.close();
			}
		}
	}	
	
}
