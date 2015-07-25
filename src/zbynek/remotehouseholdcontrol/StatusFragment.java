package zbynek.remotehouseholdcontrol;

import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;
import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;
import zbynek.remotehouseholdcontrol.nettools.StatusesXmlParser;
import zbynek.remotehouseholdcontrol.nettools.UrlReader;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;

public class StatusFragment extends Fragment {

	private LinearLayout linlayout;
	private LayoutInflater inflater;
	ImageView image;

	@Override 
	public View onCreateView(LayoutInflater i, ViewGroup container, Bundle savedInstanceState) {
			  View v = i.inflate(R.layout.status_fragment_layout, container, false);
			  linlayout = (LinearLayout)v.findViewById(R.id.status_layout);
			  inflater = i;
			  return v;
			}
	
	@Override
	public void onResume() {
		super.onResume();
		
		View garage_image_view = inflater.inflate(R.layout.status_fragment_layout, null); 
		
		SimpleArrayMap<String, String> m = Data.get();
		if (m != null) {

 	//	    ((ImageView)garage_image_view.findViewById(R.id.boiler_View )).setImageResource(
		//    		"On".equals(m.get("ENV_Boiler")) ?	R.drawable.boiler_on : R.drawable.boiler_off);    	

 		   ((ImageView)garage_image_view.findViewById(R.id.garageView)).setImageResource(
		    		"Close".equals(m.get("ENV_Garáž_vrata")) ?	R.drawable.garage_open : R.drawable.garage_open);    	
 		  Toast.makeText(getActivity(),m.get("ENV_Garáž_vrata"), Toast.LENGTH_LONG).show();	
 		  
 /*		    ((ImageView)garage_image_view.findViewById(R.id.Heating_View)).setImageResource(
		    		"On".equals(m.get("ENV_Vìtrání")) ?	R.drawable.garage_open : R.drawable.garage_closed);    	
	*/	}
		
	//	((ImageView)garage_image_view.findViewById(R.id.garageView)).setImageResource(R.drawable.garage_open);    	
		linlayout.removeAllViewsInLayout();
		linlayout.addView(garage_image_view);

		//linlayout.setBackgroundColor(0xff000000);
	}

	

}
