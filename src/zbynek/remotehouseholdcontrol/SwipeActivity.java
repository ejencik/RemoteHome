package zbynek.remotehouseholdcontrol;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

public class SwipeActivity extends FragmentActivity {

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager viewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_swipe);
	
		Data.download(this);

		// Create the adapter that will return a fragment for the overview or control
		OverviewAndControlAdapter fragmentPagerAdapter = 
		new OverviewAndControlAdapter(getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		viewPager = (ViewPager) findViewById(R.id.pager);
		viewPager.setAdapter(fragmentPagerAdapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_swipe, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    // Handle item selection
	    switch (item.getItemId()) {
	        case R.id.menu_settings:
	            Intent openSettingsIntent = new Intent(this, SettingsActivity.class);
	            startActivity(openSettingsIntent);
	            return true;
	        case R.id.menu_refresh:
	        	finish();
	        	startActivity(getIntent());	        	
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class OverviewAndControlAdapter extends FragmentPagerAdapter {

		public OverviewAndControlAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new TemperatureFragment();			
			case 1:
				return new StatusFragment();
			case 2:
				return new  ControlFragment();			
			case 3:
				return new SaunaFragment();			
			case 4:
				return new ZaluzieFragment();			
			case 5:
				return new GraphFragment();			
			case 6:
				return new DataFragment();			
			case 7:
				return new ListFragment();			

			}
			return null;

		}

		@Override
		public int getCount() {
			// Show total pages.
			return 8;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return getString(R.string.title_temperature);

			case 1:
				return getString(R.string.title_status);	

			case 2:
				return getString(R.string.title_control);

			case 3:
				return getString(R.string.title_sauna);

			case 4:
				return getString(R.string.title_zaluzie);	
			
			case 5:
				return getString(R.string.title_graph);

			case 6:
				return getString(R.string.title_data);

			case 7:
				return getString(R.string.title_list);

			}
			return null;
		}
	}


}
