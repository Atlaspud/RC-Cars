package au.edu.jcu.IT.sketch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private static final int SETTINGS_ACTIVITY = 1;
	
	private static int radius;
	private static int colour;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		radius = 5;
		colour = Color.BLUE;
		setContentView(R.layout.activity_main);
	}
	
	public static int getRadius() {
		return radius;
	}
	
	public static int getColour() {
		return colour;
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		
		switch (item.getItemId()) {
		case R.id.action_settings:
			
			Intent intent = new Intent(this, SettingsActivity.class);
			intent.putExtra("message",  "Hello World");
			
//			startActivity(intent);
			startActivityForResult(intent, SETTINGS_ACTIVITY);
			
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		switch (requestCode) {
		case SETTINGS_ACTIVITY:
			Bundle bundle = data.getExtras();
			radius = bundle.getInt("number");
			colour = bundle.getInt("colour");
			System.out.println("radius: " + radius + "Color: " + colour);
			break;
		}
	}

}
