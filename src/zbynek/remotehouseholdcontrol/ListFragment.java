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
import android.widget.ArrayAdapter;  
import android.widget.ListView;  
import java.lang.ref.WeakReference;
import java.util.ArrayList;  
import java.util.Arrays;

//import com.windrealm.android.R;  
 
public class ListFragment extends Fragment {

//	private TableLayout table;
	private LayoutInflater inflater;
//	private HandleDownload h = new HandleDownload(this);
	
	  private ListView mainListView ;  
	  private ArrayAdapter<String> listAdapter ;  

	@Override
	public View onCreateView(LayoutInflater i, ViewGroup container,Bundle savedInstanceState) {
		 // Find the ListView resource.   
		
	  View v = i.inflate(R.layout.table, container, false);
	    mainListView = (ListView)v.findViewById(R.id.mainListView );  
	  
	    // Create and populate a List of planet names.  
	    String[] planets = new String[] { "Mercury", "Venus", "Earth", "Mars",  
	                                      "Jupiter", "Saturn", "Uranus", "Neptune"};    
	    ArrayList<String> planetList = new ArrayList<String>();  
	    planetList.addAll( Arrays.asList(planets) );  
    //	System.out.println("planets "+planets);
    //	System.out.println("planetsList "+planetList);
        	
	    // Create ArrayAdapter using the planet list.  
	    listAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.simplerow, planetList);  
	      
	// Add more planets. If you passed a String[] instead of a List<String>  into the ArrayAdapter constructor, 
	 // you must not add more items. Otherwise an exception will occur.  
	/*    listAdapter.add( "Ceres" );  
	    listAdapter.add( "Pluto" );  
	    listAdapter.add( "Haumea" );  
	    listAdapter.add( "Makemake" );  
	    listAdapter.add( "Eris" );  
	    */  
	    // Set the ArrayAdapter as the ListView's adapter.  
	  //  mainListView.setAdapter( listAdapter ); 	  
	    inflater = i;
	    return v;
	}

	@Override
	public void onResume() {
		super.onResume();
	//	Data.setOnDownload(h);
	}
}