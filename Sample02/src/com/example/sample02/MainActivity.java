package com.example.sample02;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.widget.EditText;
import android.util.Log;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ScrollView;
import java.io.*;
import android.hardware.usb.*;
import android.widget.LinearLayout;
import com.hoho.android.usbserial.driver.*;

public class MainActivity extends Activity {

	private UsbSerialDriver mSerialDevice;
	private UsbManager mUsbManager;
	//private TextView sysout;
	private TextView mTextSnd;
	private TextView mTextRcv;
	private EditText mEditCmd; 
	private Button mBtnSnd;
	private Button mBtnOpen;
	private ScrollView mScvSnd;
	private ScrollView mScvRcv;
	private LinearLayout mLly1;
	
	private static final String TAG = "arduino";
	private static Handler mHandler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		mHandler = new Handler();
		mLly1    = (LinearLayout) findViewById(R.layout.activity_main);
		mTextSnd = (TextView) findViewById(R.id.TextSnd);
		mTextRcv = (TextView) findViewById(R.id.TextRcv);
		//mScvSnd  = (ScrollView) findViewById(R.id.ScrollViewSnd);
		//mScvRcv  = (ScrollView) findViewById(R.id.ScrollViewRcv);
		mEditCmd = (EditText) findViewById(R.id.etCmd);
		//mScvSnd.addView(mLly1);
		//mScvRcv.addView(mLly1);
		mBtnSnd = (Button)findViewById(R.id.btnSnd);
		mBtnSnd.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogOut("Button was clicked.[OK]");
				mTextSnd.append( mEditCmd.getText().toString()+"\n" );
				mTextRcv.append( mEditCmd.getText().toString()+"\n" );

				//try{
					//mSerialDevice.write(mEditCmd.toString().getBytes("UTF-8"),
					//					mEditCmd.toString().length());
				//}
				//catch(IOException e){
				//	e.printStackTrace();
				//}
			}
		});

		mBtnOpen = (Button)findViewById(R.id.btnOpen);
		mBtnOpen.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				LogOut("Button was clicked.[Open]");
				mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
				mSerialDevice = UsbSerialProber.acquire(mUsbManager);
				if (mSerialDevice != null) {
					LogOut("UsbSerialProber.acquire [Success]");
					//try{
						//mSerialDevice.open();
						//start_read_thread();
					//}catch(IOException e){
					//	LogOut("UsbSerialProber.acquire [Success]");
					//	e.printStackTrace();
					//}
				}else{
					LogOut("mSerialDevice.acquire [Fail!]");
				}
			}
		});
	}

	private void LogOut( String Msg ) {
		//System.out.println(Msg);
		Log.v(TAG,Msg);
	}

	private byte mBuf[];
	private int mNum;
	public void start_read_thread(){
		  new Thread(new Runnable(){
			public void run(){
			  try{
				mBuf = new byte[256];
				while(true){
//  	          final byte buf[] = new byte[256];
//  	          final int num = mSerialDevice.read(buf, buf.length);
//  	          mBuf = new byte[256];
				  mNum = mSerialDevice.read(mBuf, mBuf.length);
				  if(mNum > 0){
//    	        	  Log.v("arduino", new String(buf, 0, num)); // Arduino窶堋ｩ窶堙ｧﾅｽﾃｳﾂ信窶堋ｵ窶堋ｽ窶冤窶堙ｰlogcatﾂ出窶氾�//    	        	  System.out.println(new String(buf, 0, num));
					  mHandler.post(new Runnable() {
						//run()窶堙娯�窶��ﾃ個祥��ﾂ昶�ﾃ哉陳�辰ﾆ停�ﾆ湛ﾆ椎槌鍛ﾆ檀窶堙��ﾂｮﾂ催ｬ窶堋ｳ窶堙ｪ窶堙懌�ﾂｷﾂ。
						  public void run() {
							  //System.out.println(new String(mBuf, 0, mNum));
							  mTextRcv.append(new String(mBuf, 0, mNum));
//    	    	        	  System.out.println("Kita!!");
						  }
					  });
				  }
				  Thread.sleep(10);
				}
			  }
			  catch(IOException e){
				e.printStackTrace();
			  }
			  catch (InterruptedException e) {
				e.printStackTrace();
			  }
			}
		  }).start();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void opDestroy() {
		super.onDestroy();
		try{
			mSerialDevice.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}

