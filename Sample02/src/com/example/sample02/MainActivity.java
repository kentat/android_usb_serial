package com.example.sample02;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.view.Menu;

import android.util.Log;
import android.content.Context;

import android.view.View;
import android.widget.Button;

import android.widget.TextView;

import java.io.*;
import android.hardware.usb.*;
import com.hoho.android.usbserial.driver.*;

public class MainActivity extends Activity {

    /**
     * The device currently in use, or {@code null}.
     */
    private UsbSerialDriver mSerialDevice;

    /**
     * The system's USB service.
     */
    private UsbManager mUsbManager;

    /** Logcat�̃^�O�Ɋ܂߂镶����. */
    private static final String TAG = "arduino";
    
    private static Handler mHandler;
    
    private TextView sysout;
    private TextView mTextSnd;
    private TextView mTextRcv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "�N���I�I");
		//System.out.println("Start!!");

		//�n���h���𐶐�
		mHandler = new Handler();

        // �W���o�͂�u�������i�W���o��(LogCat)�{�e�L�X�g�r���[�j
        ///*TextView*/sysout = (TextView) findViewById(R.id.sysout);
        mTextSnd = (TextView) findViewById(R.id.TextSnd);
        mTextRcv = (TextView) findViewById(R.id.TextRcv);
        //System.setOut(new TextViewPrintStream(System.out, mTextSnd));    // (1)
        //System.setErr(new TextViewPrintStream(System.err, mTextSnd));    // (2)
//        System.setOut(new TextViewPrintStream(System.out, sysout));    // (1)
        //        System.setErr(new TextViewPrintStream(System.err, sysout));    // (2)
        
//      mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//      mSerialDevice = UsbSerialProber.acquire(mUsbManager);
//      if (mSerialDevice != null) {
//        try{
//      	mSerialDevice.open();
//      	mSerialDevice.setBaudRate(9600);
//          start_read_thread(); // �V���A���ʐM��ǂރX���b�h���N��
//        }
//        catch(IOException e){
//          e.printStackTrace();
//        }
//      }

        Button btn = (Button)findViewById(R.id.btnON);
        btn.setOnClickListener(new View.OnClickListener() {
        	// ON�{�^�����N���b�N���ꂽ���̃n���h��
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		// �N���b�N���ꂽ���̏������L�q
        		Log.v(TAG,"Button was clicked.[OK]");
        		//System.out.println("Button was clicked.[OK]");
        		mTextSnd.append("On");
        		try{
                	mSerialDevice.write("ON".getBytes("UTF-8"),2);
                }
                catch(IOException e){
                	e.printStackTrace();
                }
        	}
        });
    
        btn = (Button)findViewById(R.id.btnTEST);
        btn.setOnClickListener(new View.OnClickListener() {
        	// TEST�{�^�����N���b�N���ꂽ���̃n���h��
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		// �N���b�N���ꂽ���̏������L�q
        		Log.v(TAG,"Button was clicked.[TEST]");
        		//System.out.println("Button was clicked.[TEST]");
        		// �o�͂���
//        		for (int i = -50; i < 50; i++) {
//              	System.out.printf("%3d|", i);    // (3)
//                    for (int j = 0; j < Math.abs(i); j++) {
//                    	System.out.print("*");
//                    }
//                    System.out.println();
//                }
        	}
        });

        btn = (Button)findViewById(R.id.btnOpen);
        btn.setOnClickListener(new View.OnClickListener() {
        	// Open�{�^�����N���b�N���ꂽ���̃n���h��
        	@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub
        		// �N���b�N���ꂽ���̏������L�q
        		Log.v(TAG,"Button was clicked.[Open]");
        		//System.out.println("Button was clicked.[Open]");

        		mUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
                mSerialDevice = UsbSerialProber.acquire(mUsbManager);
                if (mSerialDevice != null) {
                	//System.out.println("UsbSerialProber.acquire [Success]");
	                  try{
	                	mSerialDevice.open();
	                	mSerialDevice.setBaudRate(9600);
	                    start_read_thread(); // �V���A���ʐM��ǂރX���b�h���N��
	                  }
	                  catch(IOException e){
	                	  //System.out.println(e.getMessage());
	                    e.printStackTrace();
	                  }
                }else{
                	//System.out.println("mSerialDevice.acquire [Fail!]");
                }
        	}
        });
    
    }

 // Handler h = new Handler();
 // public void run() {
 // 	try{
//	        while (true) {
//	            Thread.sleep(1000);
//	            h.post(new Runnable() {
//	                public void run() {
//	                	sysout.setText("ABC");
//	                }
//	            });
//	        }
 //   	}
//		catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//    }

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
//    	        	  Log.v("arduino", new String(buf, 0, num)); // Arduino�����M�����l��logcat�o��
//    	        	  System.out.println(new String(buf, 0, num));
    	              mHandler.post(new Runnable() {
    	              	//run()�̒��̏����̓��C���X���b�h�œ��삳��܂��B
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

