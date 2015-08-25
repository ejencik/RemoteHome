package zbynek.remotehouseholdcontrol;

import java.lang.ref.WeakReference;
import java.util.Map;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;



public class TemperatureFragment extends Fragment {
  private static final String[] SECTIONS = {"OUT","NP1","NP2","ENV"};

	private TableLayout table;
	private LayoutInflater inflater;
	private HandleDownload h = new HandleDownload(this);

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,
	  Bundle savedInstanceState) {
	  View v = i.inflate(R.layout.table, container, false);
	  table = (TableLayout)v.findViewById(R.id.table);
	  inflater = i;
	  Data.setOnDownload(h);
	  return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		Data.setOnDownload(h);
	}

	protected void displayData(SimpleArrayMap<String, String> m) {
    if (m.isEmpty())
      return;
    table.removeAllViews();
    String date = m.get("date");
    if (date != null) {
      View refresh_time = inflater.inflate(R.layout.table_refresh_date, null);
      ((TextView)refresh_time.findViewById(R.id.row_refresh_date))
        .setText("Last update " + date);
      table.addView(refresh_time);
    }
    for (String section : SECTIONS) {
      View head = inflater.inflate(R.layout.table_row_header, null);
      ((TextView)head.findViewById(R.id.row_key)).setText(section);
      table.addView(head);
      for (int i = 0; i < m.size(); i++)
        if (m.keyAt(i).startsWith(section)) {
          View row = inflater.inflate(R.layout.table_row_status, null);
          ((TextView)row.findViewById(R.id.row_key)).setText(m.keyAt(i)
            .substring(4));
          ((TextView)row.findViewById(R.id.row_val)).setText(m.valueAt(i));
          table.addView(row);
        }
    }
    table.setBackgroundColor(0xff000000);
  }

	private static class HandleDownload extends Handler {

		private final WeakReference<TemperatureFragment> frag;
		
		public HandleDownload(TemperatureFragment frag) {
			this.frag = new WeakReference<TemperatureFragment>(frag);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Data.MSG_DOWNLOADED) {
				SimpleArrayMap<String, String> m = Data.get();
				TemperatureFragment f = frag.get();
				if (m != null && f != null)
					f.displayData(m);
			}
		}
	}
}