package zbynek.remotehouseholdcontrol;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class WaterLevelFragment extends Fragment {
	private TextView textView; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		textView = new TextView(getActivity());
		textView.setGravity(Gravity.CENTER);
		return textView;
	}
	
	
}