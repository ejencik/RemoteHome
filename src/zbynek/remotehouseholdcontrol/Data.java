package zbynek.remotehouseholdcontrol;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;
import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.util.SimpleArrayMap;

public class Data {

	public static final int MSG_DOWNLOADED = 1;
	
	private static final AtomicReference<SimpleArrayMap<String, String>> d= new AtomicReference<SimpleArrayMap<String,String>>();
	
	private static final SimpleArrayMap<Class, Handler> onDownload =new SimpleArrayMap<Class, Handler>();
	
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
			CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
			try {
				 scriptCaller.callCGIScriptAndGetStatus(1);
				return true;
			} catch (IOException e) {
			// TZ	SimpleArrayMap<String, String> m = new SimpleArrayMap<String, String>();
			// TZ	m.put("Error", "Neprectu XML.\n Zkontrolujte pripojeni a nastaveni.");
				// TZ	setMap(m);
				return false;
			} 
		}
	}
}