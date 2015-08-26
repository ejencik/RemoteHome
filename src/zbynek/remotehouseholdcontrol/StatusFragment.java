package zbynek.remotehouseholdcontrol;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.lang.ref.WeakReference;

public class StatusFragment extends Fragment {

  private LinearLayout layout;
  private LayoutInflater inflater;
  private OnDownload onDownload = new OnDownload(this);

  @Override
  public View onCreateView(LayoutInflater i, ViewGroup container,
    Bundle savedInstanceState) {
    View v = i.inflate(R.layout.status_fragment_layout, container, false);
    layout = (LinearLayout) v.findViewById(R.id.status_layout);
    inflater = i;
    Data.setOnDownload(onDownload);
    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
    Data.setOnDownload(onDownload);
  }

  protected void updateView() {
    View frag = inflater.inflate(R.layout.status_fragment_layout, null);
    SimpleArrayMap<String, String> m = Data.get();
    if (m != null) {
      String v = m.get(getString(R.string.env_garaz_vrata));
      if (v != null) {
        Toast.makeText(getActivity(), v, Toast.LENGTH_LONG).show();
        ImageView g = (ImageView) frag.findViewById(R.id.garageView);
        g.setImageResource(v.trim().equals(getString(R.string.status_close))
          ? R.drawable.garage_closed
          : R.drawable.garage_open);
      }

      v = m.get(getString(R.string.env_boiler));
      if (v != null) {
        ImageView img = (ImageView) frag.findViewById(R.id.boiler_View);
        img.setImageResource(v.trim().equals(getString(R.string.status_off))
          ? R.drawable.boiler_off
          : R.drawable.boiler_on);
      }
    }
    layout.removeAllViewsInLayout();
    layout.addView(frag);
  }

  private static class OnDownload extends Handler {
    private final WeakReference<StatusFragment> frag;

    private OnDownload(StatusFragment frag) {
      this.frag = new WeakReference<StatusFragment>(frag);
    }

    @Override
    public void handleMessage(Message msg) {
      if (msg.what == Data.MSG_DOWNLOADED) {
        StatusFragment f = frag.get();
        if (f != null)
          f.updateView();
      }
    }
  }

}
