package com.example.bluetooth;

import java.io.OutputStream;
import java.util.Set;

import android.os.Bundle;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

//**********************************************************************
//* DeviceList
//*
//*	  Populates a list with bluetooth devices paired to the phone,
//*	  then waits for the users selection and stores that selections
//*   MAC address in a bundle that is exported into the controller 
//*   activity.
//*
//**********************************************************************

public class DeviceList extends Activity {
	
	BluetoothAdapter btAdapter;
	ArrayAdapter<String> btArrayAdapter;
	ListView mArrayList;
	BluetoothSocket btSocket = null;
	OutputStream outStream = null;
	private static String address = "00:00:00:00:00:00";
	
	//**********************************************************************
	//* OnCreate
	//*
	//* Assigns all variables to the on screen events, sets up on click
	//* listener for listView
	//*
	//**********************************************************************
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_device_list);
		
		Initialise();
		getPairedDeviceList();
		
		// listens for an item selection and exports items MAC address to Controller activity
		mArrayList.setOnItemClickListener(new OnItemClickListener() {
			@Override
	        public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
				
				String selected = (((TextView)view).getText().toString());
				String[] Separated = selected.split("\n");
				address = Separated[1].trim();
				// set up export of MAC address to controller Activity
				Bundle deviceAddress = new Bundle();
				deviceAddress.putString("deviceAddress", address);
				Intent startController = new Intent(DeviceList.this, Controller.class);
				startController.putExtras(deviceAddress);
				startActivity(startController);
	        }
	    });
	}
	
	//**********************************************************************
	//* Initialise
	//*
	//*	  Assigns layout items and phone hardware to variables
	//*
	//**********************************************************************
	
	private void Initialise(){
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		
		mArrayList = (ListView)findViewById(R.id.deviceList);
        btArrayAdapter = new ArrayAdapter<String>(DeviceList.this, 
				android.R.layout.simple_list_item_1);
        mArrayList.setAdapter(btArrayAdapter);
	}
	
	//**********************************************************************
	//* getPairedDeviceList
	//*
	//*	  Retrieves the list of paired bluetooth devices on the phone and
	//*   displays that list in a listView.
	//*
	//**********************************************************************
	
	private void getPairedDeviceList(){
		Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
		// If there are paired devices
		if (pairedDevices.size() > 0) {
		    // Loop through paired devices
		    for (BluetoothDevice device : pairedDevices) {
		        // Add the name and address to an array adapter to show in a ListView
		    	btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
		    }
		}
	}
	
	//**********************************************************************
	//* onBackPressed
	//*
	//*	  Finishes current activity and returns to the main menu
	//*
	//**********************************************************************
	
	public void onBackPressed() {
		finish();
	}
}
