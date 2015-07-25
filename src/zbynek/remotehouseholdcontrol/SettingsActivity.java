package zbynek.remotehouseholdcontrol;

import zbynek.remotehouseholdcontrol.nettools.ConnectionCredentialsManager;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;

public class SettingsActivity extends Activity {

	private EditText domain;
	private EditText port;
	private EditText dir;
	private EditText username;
	private EditText password;
	private ConnectionCredentialsManager credManager;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		domain = ((EditText)findViewById(R.id.editText1));
		port = ((EditText)findViewById(R.id.editText2));
		dir = ((EditText)findViewById(R.id.editText3));
		username = ((EditText)findViewById(R.id.editText4));
		password = ((EditText)findViewById(R.id.editText5));
		credManager = new ConnectionCredentialsManager(this);

		findViewById(R.id.btn_Save).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        credManager.saveCredentials(domain.getText().toString(),
          port.getText().toString(), dir.getText().toString(), 
          username.getText().toString(), password.getText().toString());
        Toast.makeText(SettingsActivity.this, R.string.settings_saved,Toast.LENGTH_LONG).show();
        finish();
      }
    });
		
		findViewById(R.id.btn_Cancel).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_settings, menu);
		return true;
	}
	
	
	@Override
	protected void onResume() {
		super.onResume();
		domain.setText(credManager.getDomain());
		port.setText(credManager.getPort());
		dir.setText(credManager.getDir());
		username.setText(credManager.getUsername());
		password.setText(credManager.getPassword());
	}
}
