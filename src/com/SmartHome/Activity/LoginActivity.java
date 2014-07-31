package com.SmartHome.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.*;
import android.widget.EditText;
import android.widget.Toast;
import com.SmartHome.DataType.PublicState;
import com.SmartHome.R;
import com.SmartHome.DataType.RequestInfo;
import com.SmartHome.Util.ServiceRequest;
import com.SmartHome.Cription.security.Base64Demo;
import com.SmartHome.Cription.security.SecurityDemo;

import java.net.MalformedURLException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by p on 14-4-3.
 */
public class LoginActivity extends Activity implements Observer{

    ProgressDialog pro_dialog;
    String account="",password="",ip="",port="";
    EditText accountEditText,passwordEditText;
    boolean saved_info;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSavedInfo();

        setContentView(R.layout.login_layout);

        accountEditText = (EditText)findViewById(R.id.login_account);
        passwordEditText = (EditText)findViewById(R.id.login_password);

        if(saved_info){//有保存的信息
            accountEditText.setText(account);
            passwordEditText.setText(password);

            PublicState publicState = (PublicState)getApplication();
            publicState.net_ip = ip;
            publicState.net_port = port;
        }

    }
    public void getInputText(){//该函数获取当前activity上的EditText控件输入的文本
        account = String.valueOf(accountEditText.getText());
        PublicState.getInstance().user_act = account;
        password = String.valueOf(passwordEditText.getText());
        PublicState.getInstance().user_psw = password;
    }

    public void onNetConfigClicked(View v){
        Intent intent = new Intent(LoginActivity.this,NetConfigActivity.class);
        startActivity(intent);

    }
    public void onHeadClicked(View v){//测试加密用的函数
        try {
            testDecode();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void testDecode() throws Exception {
        //传递key接口：ip:port/wsnRest/key/:key
        Log.d("public_key","intotest");
        SecurityDemo securityDemo = new SecurityDemo();
        Log.d("public_key", Base64.encodeToString(securityDemo.getRSAPublicKey(),Base64.DEFAULT));
        byte[] deskey = securityDemo.getDESKey();
        Log.d("deskey",Base64Demo.encode(deskey));

        String decoded_deskey = securityDemo.getRSAEnceodeKey(deskey);
        Log.d("decoded_deskey",decoded_deskey);

        String str1 = "hello,world";
        String result = securityDemo.getEncodeData(str1);

        Log.d("decoded_data",result);
        String url = "http://192.168.1.101:80/wsnRest/checkPK";


         RequestInfo rf = new RequestInfo(url,decoded_deskey);

        ServiceRequest sr=new ServiceRequest("control");
        sr.execute(rf);
    }
    public void onLoginSubmitClicked(View v){
        PublicState ps = (PublicState)getApplication();

       // if(saved_info == false){//没有保存的信息，则在登录前检测需要的信息是否输入完全
            getInputText();

            if(account.length() == 0 || password.length() == 0){//账户信息有问题
                Toast toast = Toast.makeText(getApplicationContext(),"用户信息输入错误，请重新填写", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            if(ps.net_ip.length() == 0 || ps.net_port.length() == 0){//网关地址有问题
                Toast toast = Toast.makeText(getApplicationContext(),"网关信息有误，请重新填写", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
           ps.makeMd5();
       // }
        showLoginDialog();
        saveLoginInfo(ps);

        String key_url = null;
        try {
            key_url = "http://"+ps.getNetAddress()+"/wsnRest/getkey/";
        } catch (Exception e) {
            e.printStackTrace();
        }
        String deskey = null;
        try {
            //deskey = ps.securityDemo.getRSAEnceodeKey(ps.securityDemo.getDESKey());
//            deskey = new String(ps.securityDemo.getDESKey());
              deskey = Base64Demo.encode(ps.securityDemo.getDESKey());
              Log.d("decoded-deskey:",deskey);
//            Log.d("decoded-deskey:",Base64Demo.encode(new String("ssssssss").getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestInfo rf_key= null;
        Log.d("des-key:",deskey);
        try {
            rf_key = new RequestInfo(key_url,deskey);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Log.d("key_url:",key_url);
        ServiceRequest sr_key = new ServiceRequest("control");
        //sr_key.setObserverble();
        //sr_key.addObserver(LoginActivity.this);
        sr_key.execute(rf_key);


        String getmode_url="http://"+ps.getNetAddress();
        getmode_url+="/wsnRest/scene/"+account+"/"+ps.getMd5();
        RequestInfo rf2= null;
        try {
            rf2 = new RequestInfo(getmode_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServiceRequest sr2=new ServiceRequest("getmode");
        //sr1.setObserverble();
        //sr1.addObserver(LoginActivity.this);
        sr2.execute(rf2);

        String getrule_url="http://"+ps.getNetAddress();
        getrule_url+="/wsnRest/scheduler/"+account+"/"+ps.getMd5();
        RequestInfo rf1= null;
        try {
            rf1 = new RequestInfo(getrule_url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServiceRequest sr1=new ServiceRequest("getrule");
        //sr1.setObserverble();
        //sr1.addObserver(LoginActivity.this);
        sr1.execute(rf1);

        String url="http://"+ps.getNetAddress();
        url+="/wsnRest/checkLogin/username=" +account+
                "/isFirst=yes/"+ps.getMd5();
        //login_url+="/test.xml";
        Log.d("getDeviceUrl", url);
        RequestInfo rf= null;
        try {
            rf = new RequestInfo(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        ServiceRequest sr=new ServiceRequest("getDevice");
        sr.setObserverble();
        sr.addObserver(LoginActivity.this);
        sr.execute(rf);
    }
    public void saveLoginInfo(PublicState pstate){
        SharedPreferences ps = getSharedPreferences(getResources().getString(R.string.login_preference_name),MODE_PRIVATE);
        SharedPreferences.Editor editor = ps.edit();
        editor.clear();
        editor.putBoolean("SaveInfo",true);
        editor.putString("Account",account);
        editor.putString("Password",password);
        editor.putString("Ip",pstate.net_ip);
        editor.putString("Port",pstate.net_port);
        editor.commit();
    }
    public void getSavedInfo(){
        SharedPreferences preferences = getSharedPreferences(getResources().getString(R.string.login_preference_name),MODE_PRIVATE);
        saved_info = preferences.getBoolean("SaveInfo",false);
        account = preferences.getString("Account","");
        password = preferences.getString("Password","");
        ip = preferences.getString("Ip","");
        port = preferences.getString("Port","");
    }
    public void showLoginDialog(){//显示正在登录的对话框
        pro_dialog = new ProgressDialog(LoginActivity.this);
        pro_dialog.setMessage("正在登录，请稍候");
        pro_dialog.setIndeterminate(false);
        pro_dialog.setCancelable(false);
        pro_dialog.show();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            AlertDialog isExit = new AlertDialog.Builder(this)
                    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent = new Intent(LoginActivity.this,FinishActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .create();
            isExit.setTitle("系统提示");
            isExit.setMessage("确定要退出吗");

            isExit.show();

        }

        return false;

    }
    @Override
    /*
    * 由于使用了异步请求的方式访问网关，所以请求数据的线程和LoginActivity类
    * 使用观察模型作为同步的方式，请求数据的线程是 被观察者，本Activity是观察者，当被观察者完成操作后
    * 调用继承的notifyObservers方法使观察者获知已完成，然后观察者的update方法将被调用。
    * */
    public void update(Observable observable, Object o) {

        pro_dialog.dismiss();
        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}