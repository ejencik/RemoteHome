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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

public class ZaluzieFragment extends Fragment {

	private ConnectionCredentialsManager cm;
//	private ToggleButton heatingButton;
	private Switch heatingButton;
	private Switch SprinklerButton;
	private Button PulseButton;
	
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
		
		SprinklerButton = new Switch(getActivity());
		TextView SprinklerLabel = new TextView(getActivity());
		SprinklerLabel.setText("Sprinkler");
		layoutForHeating.addView(SprinklerButton);
		layoutForHeating.addView(SprinklerLabel);
		
		verticalLayout.addView(layoutForHeating);
		return verticalLayout;
	}

		private OnCheckedChangeListener heatingButtonListener = new OnCheckedChangeListener() {
		@Override

		public void onCheckedChanged(final CompoundButton button, boolean isChecked) {

			CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
			boolean success;
			
		/*		try {
					success = scriptCaller.pokus(isChecked);
					if (!success) {throw new IOException("Script failed.");}
				} catch (IOException e) {
					return false;
										}
				return true;
			
	
	*/		AsyncTask<Boolean, Void, Boolean> setHeatingStatusTask = new AsyncTask<Boolean, Void, Boolean>() {
				@Override
				protected Boolean doInBackground(Boolean ... params) {
					boolean isChecked = params[0];
					CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
					boolean success;
					try {
						success = scriptCaller.callCGIScriptAndSetValue(isChecked);
						if (!success) {throw new IOException("Script failed.");}
					} catch (IOException e) {
						return false;
											}
					return true;
				}
			};
			boolean result;
			try {
				result = setHeatingStatusTask.execute(isChecked).get();
			} catch (Exception e) { //collect all possible exceptions
				Toast.makeText(getActivity(),"Exception setHeating", Toast.LENGTH_LONG).show();					
				result = false;
			}
			if (!result) {
				showError("chybka 1");
				button.setChecked(!isChecked); //set previous state
			}
			
		}
	};

	@Override
	public void onResume() {
		super.onResume();
		// Toast.makeText(getActivity(), "ControlFragment is opening", Toast.LENGTH_LONG).show();
		//load status of Heating
		AsyncTask<Void, Void, Boolean> getHeatingStatusTask = new AsyncTask<Void, Void, Boolean>() {
			@Override
			protected Boolean doInBackground(Void ... params) {
				CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
				try {
					return scriptCaller.callCGIScriptAndGetStatus();
				} catch (IOException e) {
					Toast.makeText(getActivity(),"Chyba heating", Toast.LENGTH_LONG).show();					
					return null;
					}
			}
		};
		try { 
			Boolean status = getHeatingStatusTask.execute().get();
			if (status == null) {
				showError("chyba 2");

			} else {
				heatingButton.setOnCheckedChangeListener(null);
				heatingButton.setChecked(status);  // nastavi ziskany status
				heatingButton.setOnCheckedChangeListener(heatingButtonListener);
			}
		} catch (Exception e) { //collect all possible exceptions
			showError("chyba 3");
		}
	}    //public void onResume() 
	
	private void showError( String chyba) {
		AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
		alertDialog.setMessage("Problem... showError" + chyba);
		alertDialog.setButton("OK",new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				//do nothing
			}
		});
		alertDialog.show();		
	}
	
	
}	

 