package com.tanrong.wifi;
import java.io.BufferedReader;
import java.util.StringTokenizer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
public class MainActivity extends Activity {
    private Context mContext;
    private Button openWifi,closeWifi,addWifi,capture;
    private WifiAdmin wifiAdmin;
    private RadioGroup radioGroup;
    private EditText edtSSID,edtPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext=MainActivity.this;
        openWifi= (Button) findViewById(R.id.openWifi);
        openWifi.setText("打开wifi");
        closeWifi= (Button) findViewById(R.id.closeWifi);
        closeWifi.setText("关闭wifi");
        addWifi= (Button) findViewById(R.id.addWifi);
        addWifi.setText("添加一个wifi");
        capture= (Button) findViewById(R.id.capture);
        capture.setText("扫描二维码");

         radioGroup= (RadioGroup) findViewById(R.id.group);
        edtSSID= (EditText) findViewById(R.id.ssid);
        edtPassword= (EditText) findViewById(R.id.pwd);

        wifiAdmin =new WifiAdmin(mContext);

        openWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                wifiAdmin.openWifi();
            }
        });
        closeWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wifiAdmin.closeWifi();
            }
        });

        addWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ssid=edtSSID.getText().toString().trim();
                String pwd=edtPassword.getText().toString().trim();
                int type=0;
                
                switch (radioGroup.getCheckedRadioButtonId()){
                    case R.id.nonePwd:
                        type= WifiAdmin.TYPE_NO_PASSWD;
                        break;
                    case R.id.WPA:
                        type= WifiAdmin.TYPE_WPA;
                        break;
                    case R.id.WEP:
                        type= WifiAdmin.TYPE_WEP;
                        break;
                }
                wifiAdmin.addNetwork(ssid, pwd, type);
            }
        });
        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext,CaptureActivity.class));
            }
        });
   }
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	
    	try{
    		Intent it=getIntent();
        	String text=it.getStringExtra("wifi");
        	if(text!=null){
    	    	Log.i("text--->",text);
    	    	 //StringTokenizer st = new StringTokenizer(text, "\n");
                String[] colums=text.split("\n");
                if(colums.length<2){
                	Toast.makeText(MainActivity.this, "二维码格式错误", Toast.LENGTH_SHORT).show();
                }else if(colums.length==2){
                	String ssid=colums[0].substring(5);
                	String pwd=colums[1].substring(9);
        	    	this.edtSSID.setText(ssid);
        	    	this.edtPassword.setText(pwd);
                }else if(colums.length==3){
                	String ssid=colums[0].substring(5);
                	String pwd=colums[1].substring(9);
                	String security=colums[2].substring(8);
        	    	this.edtSSID.setText(ssid);
        	    	this.edtPassword.setText(pwd);
        	    	if(security.trim().endsWith("null")){
        	    		radioGroup.check(R.id.nonePwd);
        	    	}else if(security.trim().equals("wpa")){
        	    		radioGroup.check(R.id.WPA);
        	    	}else if(security.trim().equals("wep")){
        	    		radioGroup.check(R.id.WEP);
        	    	}
                }    	    	
        	}else{
        		Log.i("text--->","null");
        	}
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	
    	
    }
}
