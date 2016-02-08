package zbynek.remotehouseholdcontrol;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.util.SimpleArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.widget.LinearLayout;
import java.io.IOException;
import java.util.Calendar;

import zbynek.remotehouseholdcontrol.nettools.CgiScriptCaller;
import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;

public class SaunaFragment extends Fragment {

	private ConnectionCredentialsManager cm;
  //  private int rele_id = getResources().getInteger(R.integer.rele_sauna);	

	public boolean GetRelayStatus(int device_id) {
		return  true;
	}
    
	@Override 
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {  
     	    View v = inflater.inflate(R.layout.sauna_fragment_layout, container, false);
     	    SimpleArrayMap<String, String> m = Data.get();     	    
     	    
// initial Button settings     	    
     	    int rele_id = getResources().getInteger(R.integer.rele_sauna);
      	    boolean rele_status = true;
    	    if (m != null) {
    	    	String IOport = rele_id < 9 	? m.get(getString(R.string.P3))
    	    									: m.get(getString(R.string.P5));
    	    	int port_result = (IOport!=null) ? Integer.parseInt((IOport.trim()).toString()):1;
    			rele_status = (port_result & rele_id) > 0 ? true : false;
    		}
    	    ToggleButton saunaBt = (ToggleButton)v.findViewById(R.id.toggleButtonSauna);
    	    saunaBt.setOnClickListener(listener);
    	    saunaBt.setChecked(rele_status);
    	    
// show temperature
     	    if (m != null) {
     	    String val = m.get(getString(R.string.temp_sauna));
     	     	      if (val != null) {
     	     	    	 TextView g = (TextView) v.findViewById(R.id.textSaunaTemperature);
     	     	        g.setText(val); 
     	     	      }
     	     	    }
    	    
            LinearLayout ll = (LinearLayout)v.findViewById(R.id.my_tarif_graf);
            int tarif_width=480;
       //     tarif_width=ll.getWidth();
            float tarif_part=tarif_width/23;
            
            Paint paint = new Paint();
            Bitmap bg = Bitmap.createBitmap(tarif_width , 150, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bg); 

            int belt_y1	=25;
            int belt_y2	=55;
            
            paint.setColor(Color.parseColor("#99FFCC"));
            canvas.drawRect(0, 0, tarif_width,100, paint); 

            paint.setColor(Color.parseColor("#21283D"));
            canvas.drawRect(0, belt_y1, tarif_width,belt_y2, paint); 

// draw HDO intervals
	     	String string_HDO = m.get(getString(R.string.HDO));
            String[] interval_HDO = string_HDO.split("#");
     
            for (int i=0; i <interval_HDO.length; i++) {
            //	System.out.println("interval_HDO[i] "+interval_HDO[i]);
               	String[] time_HDO = interval_HDO[i].split("-");	
            	Float begin_HDO	= Float.parseFloat(time_HDO[0].trim())*tarif_part;	
            	Float end_HDO	= Float.parseFloat(time_HDO[1].trim())*tarif_part;	
            	paint.setColor(Color.parseColor("#cceae7"));
            	canvas.drawRect(begin_HDO, belt_y1, end_HDO, belt_y2, paint); 
            }
            
// draw actual time
            Calendar cal = Calendar.getInstance(); 
            float minute = cal.get(Calendar.MINUTE);
            float hourofday = cal.get(Calendar.HOUR_OF_DAY);
            float xpos= ((hourofday) + minute/60 )*tarif_part ; 
            paint.setColor(Color.parseColor("#ff0000"));
            canvas.drawRect(xpos-1, belt_y1-3, xpos+1,belt_y2+3, paint); 

// draw hours labels
            for (int i=1; i < 24; i++) {
                paint.setColor(Color.parseColor("#FFFFF0"));
                canvas.drawCircle((tarif_part)*i, belt_y1+12, 3, paint); 
                String formatted = String.format("%02d", i);
                paint.setColor(Color.parseColor("#751947"));
                canvas.drawText(formatted  ,(tarif_part)*i-7, belt_y1+50, paint);
            }

            ll.setBackground(new BitmapDrawable(getResources(), bg));
 		    return v;
		    }

	ToggleButton.OnClickListener listener = new ToggleButton.OnClickListener()
	{		@Override
	public void onClick(View v) {
      cm = new ConnectionCredentialsManager(getActivity());
//TZE je to spravne?  
	    ToggleButton saunaBt = (ToggleButton)v.findViewById(R.id.toggleButtonSauna);
	    Boolean isChecked = saunaBt.isChecked();
	    
	   AsyncTask< Boolean, Void, Boolean> setSwitchStatusTask = new AsyncTask< Boolean, Void, Boolean>() {
				@Override
				protected Boolean doInBackground( Boolean ... params) {
					Boolean isChecked = params[0];
					   int device = getResources().getInteger(R.integer.rele_sauna); 
					CgiScriptCaller scriptCaller = new CgiScriptCaller(cm);
					try {
						return scriptCaller.callCGIScriptAndSetValue(isChecked,device);
					} catch (IOException e) {return false;
											}
				}
			};
			boolean result;
			try {
				result = setSwitchStatusTask.execute(isChecked).get();
				if (!result) {   
	//			Toast.makeText(getActivity(), "CgiScriptCaller result: " + result, Toast.LENGTH_LONG).show();
	   			}
			} catch (Exception e) { //collect all possible exceptions
				Toast.makeText(getActivity(),"Exception PulseTask", Toast.LENGTH_LONG).show();					
				result = false;
   			  }
			}
		};	
}