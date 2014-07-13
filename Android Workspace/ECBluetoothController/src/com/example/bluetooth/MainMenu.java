package com.example.bluetooth;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//**********************************************************************
//* MainMenu
//*
//*		Identifies if bluetooth is enabled on the device, if not, it 
//* 	displays a button that turns bluetooth on, else it provides the
//*		option to got to the device list activity
//*
//**********************************************************************

public class MainMenu extends Activity {
	Button btnDevices;
	Button btnEnableBT;
	TextView BTWarning;
	static final int ENABLE_BLUETOOTH = 1;

	//**********************************************************************
	//* OnCreate
	//*
	//* 	Assigns all variables to the on screen buttons and labels setup in 
	//*		the controller layout. Then checks android device to see if bluetooth
	//* 	is turned on.
	//*
	//**********************************************************************
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_menu);
		
		Initialise();
		
		final BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        
		if (!mBluetoothAdapter.isEnabled()){
			btnDevices.setEnabled(false);
			btnEnableBT.setVisibility(View.VISIBLE);
			BTWarning.setVisibility(View.VISIBLE);
		}
	}
	
	//**********************************************************************
	//* Initialise
	//*
	//* 	Attaches controller layout buttons and textboxes to variables for use
	//* 	in other functions.
	//*
	//**********************************************************************
	
	private void Initialise(){
		BTWarning = (TextView)findViewById(R.id.txtWarning);
		btnDevices = (Button) findViewById(R.id.btnDevices);
		btnEnableBT = (Button)findViewById(R.id.btnBTEnable);
		
		btnDevices.setOnClickListener(searchDevicesListener);
		btnEnableBT.setOnClickListener(enableBT);
	}
	
	//**********************************************************************
	//* onClickListeners
	//*
	//* 	These functions listen for button presses and execute the function
	//*		associated with the button.
	//*
	//**********************************************************************
	
	private Button.OnClickListener searchDevicesListener = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			DeviceScreen();
		}
		
	};
	
	private Button.OnClickListener enableBT = new Button.OnClickListener(){

		@Override
		public void onClick(View v) {
			
			final BluetoothAdapter btAdapter = BluetoothAdapter.getDefaultAdapter();
			
			if (btAdapter == null) {
				BTWarning.setVisibility(View.VISIBLE);
				btnEnableBT.setVisibility(View.INVISIBLE);
				BTWarning.setText("This Android device does not support Bluetooth");
			}else{
				if (!btAdapter.isEnabled()){
					Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
					startActivityForResult(enableBtIntent, ENABLE_BLUETOOTH);	
				}
			}
		}
		
	};
	
	//**********************************************************************
	//* onActivityResult
	//*
	//*		Displays a request to the user to enable bluetooth 
	//*
	//**********************************************************************
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == ENABLE_BLUETOOTH) {
	        if (resultCode == RESULT_OK) {
	        	btnDevices.setEnabled(true);
				btnEnableBT.setVisibility(View.INVISIBLE);
				BTWarning.setVisibility(View.INVISIBLE);
	        }
	    }
	}
	
	//**********************************************************************
	//* DeviceScreen
	//*
	//*		Starts Device List activity 
	//*
	//**********************************************************************
	
	private void DeviceScreen(){
		startActivity(new Intent(this,DeviceList.class));
	}
	
	//**********************************************************************
	//* onBackPressed
	//*
	//* 	Executes when the phones built in back button is pressed, it is used
	//*		to close the software.
	//*
	//**********************************************************************
	
	public void onBackPressed() {
		finish();
	}

}
