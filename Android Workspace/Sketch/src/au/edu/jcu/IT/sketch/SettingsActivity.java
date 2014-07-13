package au.edu.jcu.IT.sketch;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	TextView state;
	SeekBar seekBar;
	RadioButton radioRed, radioGreen, radioBlue;
	int colour;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		state = (TextView) findViewById(R.id.stateTxt);
		seekBar = (SeekBar) findViewById(R.id.seekBar1);
		radioRed = (RadioButton) findViewById(R.id.radio0);
		radioGreen = (RadioButton) findViewById(R.id.radio1);
		radioBlue = (RadioButton) findViewById(R.id.radio2);
		
		
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		String message = bundle.getString("message");
		state.setText(message);
	}
	
	public void okayPressed(View view) {
		if (radioRed.isChecked()) {
			colour = Color.RED;
		} else if (radioGreen.isChecked()) {
			colour = Color.GREEN;
		} else {
			colour = Color.BLUE;
		}
			
		
		Intent data = new Intent();
		data.putExtra("number", seekBar.getProgress());
		data.putExtra("colour", colour);
		setResult(RESULT_OK, data);
		
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

}
