package com.example.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//**********************************************************************
//* Controller Activity
//*
//* The purpose of this activity is to set up the controller parameters
//*	and to connect the controller via Bluetooth to the car.
//*
//**********************************************************************
public class Controller extends Activity implements SensorEventListener {
	public int dutyCycle = 0x00;
	public int steer = 0x50;
	String address;
	BluetoothAdapter btAdapter;
	BluetoothSocket btSocket = null;
	OutputStream outStream = null;
	TextView connectStatus;
	TextView dutyCycleText;
	TextView steeringPostionText;
	Handler mHandler;
	SensorManager mSensorManager;
	Sensor mSensor;
	
	
	Button countRight;
	Button countLeft;
	Button countUp;
	Button reverse;
	
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	//**********************************************************************
	//* OnCreate
	//*
	//* Assigns all variables to the on screen buttons and labels setup in 
	//*	the controller layout. Then Sets up a BT connection to the device 
	//* chosen in the device list activity.
	//*
	//**********************************************************************
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_controller);
		
		Initialise();
		BTconnect();
	}
	
	//**********************************************************************
	//* onPause
	//*
	//* Closes bluetooth connection and returns to device list activity
	//*	when android device is locked or goes to sleep while connected, 
	//* This prevents the device from trying to establish a new connection
	//* to the bluetooth module on wake up, while still connected.
	//*
	//**********************************************************************
	
	
	@Override
	protected void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this);
		try {
		      btSocket.close();
		      connectStatus.setText("Connection Closed!!");
		      connectStatus.setTextColor(Color.CYAN);
		   } catch (IOException e) {
			  connectStatus.setText("Close Failed");
			  connectStatus.setTextColor(Color.RED);
			}
		finish();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
	}
	
	//**********************************************************************
	//* onBackPressed
	//*
	//* Executes when the phones built in back button is pressed, it is used
	//*	to close the BT connection, and go back to the device list activity
	//*
	//**********************************************************************
	
	@Override
	public void onBackPressed() {
		try {
		      btSocket.close();
		      connectStatus.setText("Connection Closed!!");
		      connectStatus.setTextColor(Color.CYAN);
		   } catch (IOException e) {
			  connectStatus.setText("Close Failed");
			  connectStatus.setTextColor(Color.RED);
			}
		finish();
	}

	//**********************************************************************
	//* createBluetoothSocket
	//*
	//* Returns a new BT socket created assigned with the UUID for SSP
	//* (serial port profile).	
	//*
	//**********************************************************************
	
	private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
	      if(Build.VERSION.SDK_INT >= 10){
	          try {
	              final Method  m = device.getClass().getMethod("createInsecureRfcommSocketToServiceRecord", new Class[] { UUID.class });
	              return (BluetoothSocket) m.invoke(device, MY_UUID);
	          } catch (Exception e) {
	        	  connectStatus.setText("Could not create a Secure Connection");
	        	  connectStatus.setTextColor(Color.RED);
	          }
	      }
	      return  device.createRfcommSocketToServiceRecord(MY_UUID);
	}
	
	//**********************************************************************
	//* Initialise
	//*
	//* Attaches controller layout buttons and textboxes to variables for use
	//* in other functions.
	//*
	//**********************************************************************
	
	private void Initialise(){
		Bundle getAddress = getIntent().getExtras();			// 
		address = getAddress.getString("deviceAddress");
		
		countUp = (Button)findViewById(R.id.btnIncrease);
        countLeft = (Button)findViewById(R.id.btnLEFT);
        countRight = (Button)findViewById(R.id.btnRIGHT);
        reverse = (Button)findViewById(R.id.btnREV);
		
		btAdapter = BluetoothAdapter.getDefaultAdapter();
		connectStatus = (TextView) findViewById(R.id.connectStatus);
		dutyCycleText = (TextView)findViewById(R.id.txtDuty);
		steeringPostionText = (TextView)findViewById(R.id.txtSteer);
		
		mHandler = new Handler();
		mHandler.post(mUpdate);
		
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		
		mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
		
		
		countUp.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					dutyCycle = 0x05;
					dutyCycleText.setText("Forward");
					reverse.setEnabled(false);
				} else if (action == MotionEvent.ACTION_UP) {
					dutyCycle = 0x00;
					dutyCycleText.setText("Stop");
					reverse.setEnabled(true);
				}
				return false;
			}
		});
		
		reverse.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					dutyCycle = 0x0B;
					dutyCycleText.setText("Reverse Mode");
					countUp.setEnabled(false);
				} else if (action == MotionEvent.ACTION_UP){
					dutyCycle = 0x00;
					dutyCycleText.setText("Stop");
					countUp.setEnabled(true);
				}
				return false;
			}
		});
		
		countLeft.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					steer = 0x40;
					steeringPostionText.setText("Left");
					countRight.setEnabled(false);
				} else if (action == MotionEvent.ACTION_UP){
					steer = 0x50;
					steeringPostionText.setText("Center");
					countRight.setEnabled(true);
				}
				return false;
			}
		});
		
		countRight.setOnTouchListener(new View.OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if (action == MotionEvent.ACTION_DOWN) {
					steer = 0x60;
					steeringPostionText.setText("Right");
					countLeft.setEnabled(false);
				} else if (action == MotionEvent.ACTION_UP){
					steer = 0x50;
					steeringPostionText.setText("Center");
					countLeft.setEnabled(true);
				}
				return false;
			}
		});
	}
	
	private Runnable mUpdate = new Runnable() {

		@Override
		public void run() {
			sendData(steer + dutyCycle);
			mHandler.postDelayed(this, 200);
		}
	};

	//**********************************************************************
	//* BT connect
	//*
	//*	  Establishes a connection to the chosen Bluetooth Module
	//*   Two things are needed to make a connection:
	//*   A MAC address, which was obtained in the DeviceList Activity 
	//*   A Service ID or UUID.  In this case we are using the
	//*   UUID for SPP.
	//*
	//**********************************************************************
	
	private void BTconnect() {
		   // Set up a pointer to the remote node using it's address.
		   BluetoothDevice device = btAdapter.getRemoteDevice(address);
		    
		   try {
		       btSocket = createBluetoothSocket(device);
		   } catch (IOException e) {
			   connectStatus.setText("Creating Connection Failed");
			   connectStatus.setTextColor(Color.RED);
		   }
		        
		   // Establish the connection.  This will block until it connects.
		   try {
		      btSocket.connect();
		      connectStatus.setText("Connection OK!!");
		      connectStatus.setTextColor(Color.GREEN);
		   } catch (IOException e) {
			  connectStatus.setText("Connection Failed");
			  connectStatus.setTextColor(Color.RED);
		   }
		   // Establish the out flow data stream
		   try {
		      outStream = btSocket.getOutputStream();
		   } catch (IOException e) {
		      connectStatus.setText("Output Stream Failed");
		      connectStatus.setTextColor(Color.RED);
		   }
	}
	
	//**********************************************************************
	//* sendData
	//*
	//*	  writes the message to the bluetooth out flow data stream
	//*
	//**********************************************************************
	
	private void sendData(Integer message) {
		  
	    try {
	      outStream.write(message);
	    } catch (IOException e) {
	    }
	 }

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		float x = event.values[0];
		float y = event.values[1];
		
		if (y < -2) {
			steer = 0x40;
			steeringPostionText.setText("Left");
			countRight.setEnabled(false);
		} else if ( y > 2) {
			steer = 0x60;
			steeringPostionText.setText("Right");
			countLeft.setEnabled(false);
		} else {
			steer = 0x50;
			steeringPostionText.setText("Center");
			countLeft.setEnabled(true);
			countRight.setEnabled(true);
		}
	}

}
