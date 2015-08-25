package zbynek.remotehouseholdcontrol;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import org.xmlpull.v1.XmlPullParserException;

import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;
import zbynek.remotehouseholdcontrol.nettools.StatusesXmlParser;
import zbynek.remotehouseholdcontrol.nettools.UrlReader;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.util.SimpleArrayMap;

public class Data {
	private static final String XML_STATES_FILE = "stavsenzoru.xml";

	public static final int MSG_DOWNLOADED = 1;
	
	private static final AtomicReference<SimpleArrayMap<String, String>> d
	  = new AtomicReference<SimpleArrayMap<String,String>>();
	
	private static final SimpleArrayMap<Class, Handler> onDownload =
    new SimpleArrayMap<Class, Handler>();
	
	public static void setMap(SimpleArrayMap<String, String> m) {
		d.set(m);
    for (int i = 0; i < onDownload.size(); i++)
      onDownload.valueAt(i).sendEmptyMessage(MSG_DOWNLOADED);
	}
	
	public static SimpleArrayMap<String, String> get() {
		return d.get();
	}
	
	public static boolean isLoaded() {
		return d.get() != null;
	}
	
	public static void setOnDownload(Handler h) {
		onDownload.put(h.getClass(), h);
		if (isLoaded())
			h.sendEmptyMessage(MSG_DOWNLOADED);
	}
	
	public static void download(Context ctx) {
		new DownloadStatesTask().execute(ctx);
	}

	private static class DownloadStatesTask extends
	  AsyncTask<Context, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Context... args) {
			Context ctx = args[0];
			ConnectionCredentialsManager cm = new ConnectionCredentialsManager(ctx);
			String url = cm.constructUrl(XML_STATES_FILE);
			try {
				String xml = UrlReader.readOutputFromUrl(cm, url);
				setMap(StatusesXmlParser.parseXml(xml));
				return true;
			} catch (IOException e) {
				SimpleArrayMap<String, String> m = new SimpleArrayMap<String, String>();
				m.put("Error", "Neprectu XML.\n Zkontrolujte pripojeni a nastaveni.");
				setMap(m);
				return false;
			} catch (XmlPullParserException e) {
				SimpleArrayMap<String, String> m = new SimpleArrayMap<String, String>();
				setMap(m);
				return false;
			}
		}
	}
}
