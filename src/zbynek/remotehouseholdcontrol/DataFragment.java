package zbynek.remotehouseholdcontrol;

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

import java.lang.ref.WeakReference;

public class DataFragment extends Fragment {

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
      for (int i = 0; i < m.size(); i++)
        {
          View row = inflater.inflate(R.layout.table_row_status, null);
         ((TextView)row.findViewById(R.id.row_key)).setText(m.keyAt(i));
          ((TextView)row.findViewById(R.id.row_val)).setText(m.valueAt(i));
          table.addView(row);
        }
    table.setBackgroundColor(0xff000000);
  }

	private static class HandleDownload extends Handler {
		private final WeakReference<DataFragment> frag;
		public HandleDownload(DataFragment frag) {
			this.frag = new WeakReference<DataFragment>(frag);
		}

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == Data.MSG_DOWNLOADED) {
				SimpleArrayMap<String, String> m = Data.get();
				DataFragment f = frag.get();
				if (m != null && f != null)
					f.displayData(m);
			}
		}
	}
}