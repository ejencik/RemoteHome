package zbynek.remotehouseholdcontrol;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.xmlpull.v1.XmlPullParserException;

import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;
import zbynek.remotehouseholdcontrol.nettools.StatusesXmlParser;
import zbynek.remotehouseholdcontrol.nettools.UrlReader;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class WaterLevelFragment extends Fragment {

	private static final String XML_STATES_FILE = "stavsenzoru.xml";
	
	private TextView textView; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		return textView;
	}
	
	@Override
	public void onResume() {
		super.onResume();

		displayActualState();
	}

	private void displayActualState() {
		String url = constructUrl();
		new DownloadStatesTask().execute(url);
	}

	private String constructUrl() {
		ConnectionCredentialsManager cm = new ConnectionCredentialsManager(getActivity());
		return cm.constructUrl(XML_STATES_FILE);
	}

	private class DownloadStatesTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... args) {
			String url = args[0];

			try {
				ConnectionCredentialsManager cm = new ConnectionCredentialsManager(getActivity());
				String xml = UrlReader.readOutputFromUrl(cm, url);
				LinkedHashMap<String, String> statuses = StatusesXmlParser.parseStatusesFromXml(xml);
				return formatOutput(statuses);
			} catch (IOException e) {
				return "Neprectu XML.\n Check you internet connection and settings.";

			} catch (XmlPullParserException e) {
				return "An error occured while parsing the answer from server.";
			}
		}

		private String formatOutput(LinkedHashMap<String, String> statuses) {
			StringBuilder sb = new StringBuilder();
			for (Entry<String, String> entry : statuses.entrySet()) {
				sb.append(entry.getKey());
				sb.append(" : ");
				sb.append(entry.getValue());
				sb.append("\n");
			}
			return sb.toString();
		}

		@Override
		protected void onPostExecute(String result) {
			textView.setText(result);
			textView.setTextColor(0xff000000);
			textView.setBackgroundColor(0xffcccccc);
			textView.setTextSize(12);
		}


	}	
}