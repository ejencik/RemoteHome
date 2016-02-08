package zbynek.remotehouseholdcontrol;

import java.io.IOException;

import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;
import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.Toast;

public class ZaluzieFragment extends Fragment {

	private ConnectionCredentialsManager cm;
	private Switch heatingButton;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		LinearLayout verticalLayout = new LinearLayout(getActivity());
		verticalLayout.setOrientation(LinearLayout.VERTICAL);

		cm = new ConnectionCredentialsManager(getActivity());
		
		LinearLayout layoutForHeating = new LinearLayout(getActivity());

		//Heating button
		heatingButton = new Switch(getActivity());
		TextView heatingLabel = new TextView(getActivity());
		heatingLabel.setText("Heating");
		layoutForHeating.addView(heatingButton);
		layoutForHeating.addView(heatingLabel);
		
		verticalLayout.addView(layoutForHeating);
		return verticalLayout;
	}

	@Override
	public void onResume() {
		super.onResume();		//load status of Heating
		int rele_id = getResources().getInteger(R.integer.rele_test);
		CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
		boolean success = scriptCaller.GetRelayStatus(rele_id);
	
	    SimpleArrayMap<String, String> m = Data.get();
	    if (m != null) {
	    	String IOport = rele_id < 9 	? m.get(getString(R.string.P3))
	    									: m.get(getString(R.string.P5));
	    	int port_result = (IOport!=null) ? Integer.parseInt((IOport.trim()).toString()):1;
			boolean rele_status = (port_result & rele_id) > 0 ? true : false;

			//	    	Toast.makeText(getActivity(), "rele status: " + rele_status, Toast.LENGTH_LONG).show();
			heatingButton.setOnCheckedChangeListener(null);
			heatingButton.setChecked(rele_status);  // nastavi ziskany status
			heatingButton.setOnCheckedChangeListener(heatingButtonListener);
	    }
	}    //public void onResume()
	
	private OnCheckedChangeListener heatingButtonListener = new OnCheckedChangeListener() {
		@Override

		public void onCheckedChanged(final CompoundButton button, boolean isChecked) {

	//		CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
	//		boolean success;
		AsyncTask<Boolean, Void, Boolean> setSwitchStatusTask = new AsyncTask<Boolean, Void, Boolean>() {
				@Override
		
				protected Boolean doInBackground(Boolean ... params) {
					boolean isChecked = params[0];
					int rele_id = getResources().getInteger(R.integer.rele_test);	
					CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
					boolean success;
					try {
						success = scriptCaller.callCGIScriptAndSetValue(isChecked,rele_id);
						if (!success) {throw new IOException("Script failed.");}
					} catch (IOException e) {	return false;
											}
					return true;
				}
			};
			boolean result;
			try {
				result = setSwitchStatusTask.execute(isChecked).get();
			} catch (Exception e) { //collect all possible exceptions
				Toast.makeText(getActivity(),"Exception setHeating", Toast.LENGTH_LONG).show();					
				result = false;
			}
			if (!result) {
				showError("switchCGI not returned OK");
				button.setChecked(!isChecked); //set previous state
			}
		}
	}; // OnCheckedChangeListener 
	
	private void showError( String chyba) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setMessage("Err:" + chyba);
		alertDialog.setButton("OK",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//do nothing
			}
		});
		alertDialog.show();		
	}
}	