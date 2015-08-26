package zbynek.remotehouseholdcontrol;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;
import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;

public class ControlFragment extends Fragment {

	private ConnectionCredentialsManager cm;
	
	@Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  
            Bundle savedInstanceState) {  
     	    View v = inflater.inflate(R.layout.control_fragment_layout, container, false);
     	    
    	    ImageButton garageBt = (ImageButton)v.findViewById(R.id.imageButtonVrata);
    	    garageBt.setOnClickListener(listener);

    	    ImageButton brankaBt = (ImageButton)v.findViewById(R.id.imageButtonBranka);
    	    brankaBt.setOnClickListener(listener);

    	    ImageButton garazBt = (ImageButton)v.findViewById(R.id.imageButtonGaraz);
    	    garazBt.setOnClickListener(listener);
		    return v;
		    }
	
	ImageButton.OnClickListener listener = new ImageButton.OnClickListener()
	{
		@Override

    public void onClick(View v) {
      cm = new ConnectionCredentialsManager(getActivity());
			int rele_id=0;
			switch (v.getId()) {
			case  R.id.imageButtonVrata: 	{ rele_id=R.integer.rele_vrata; 	break;}
			case  R.id.imageButtonBranka: 	{ rele_id=R.integer.rele_branka; 	break;}
			case  R.id.imageButtonGaraz: 	{ rele_id=R.integer.rele_garaz; 	break;}
			}
			
			//Toast.makeText(getActivity(),rele_id, Toast.LENGTH_LONG).show();
			
			AsyncTask<Boolean, Void, Boolean> runPulseTask = new AsyncTask<Boolean, Void, Boolean>() {
				@Override
				protected Boolean doInBackground(Boolean ... params) {
					boolean isChecked = params[0];
					CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
			//	Toast.makeText(getActivity(),"Point 2", Toast.LENGTH_LONG).show();	
					try {
						return scriptCaller.callCGIScriptPulse(1);
						//success = scriptCaller.callCGIScriptAndSetValue(isChecked);
						//if (!success) {throw new IOException("Script failed.");}
					} catch (IOException e) {
											return false;
											}
				}
			};
			

			
			boolean result;
			try {
			//	Toast.makeText(getActivity(),"Point 1", Toast.LENGTH_LONG).show();	
				
				result = runPulseTask.execute(true).get();
        Toast.makeText(getActivity(), "CgiScriptCaller result: " + result,
          Toast.LENGTH_LONG).show();
			} catch (Exception e) { //collect all possible exceptions
				Toast.makeText(getActivity(),"Exception PulseTask", Toast.LENGTH_LONG).show();					
				result = false;
			}
				
			}
		
	};	
	
}