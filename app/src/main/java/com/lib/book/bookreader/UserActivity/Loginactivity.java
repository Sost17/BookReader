package com.lib.book.bookreader.UserActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lib.book.bookreader.Activity.MainActivity;
import com.lib.book.bookreader.ClickListener.NoDoubleClickListener;
import com.lib.book.bookreader.R;
import com.lib.book.bookreader.Util.DBoperate;

import java.util.HashMap;

/**
 * Created by commo on 2017/3/17.
 */

public class Loginactivity extends Activity {

    private EditText user_editText;
    private EditText pwd_editText;
    private TextView textView;
    private Button log_buttun;
    private Button reg_buttun;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 参数初始化
         * */
        init();

        /**
         * 按钮点击事件
         * */
        clicklistener();
    }

    public void init(){
        setContentView(R.layout.activity_login);

        user_editText = (EditText) findViewById(R.id.editText2);
        pwd_editText = (EditText) findViewById(R.id.editText);
        log_buttun = (Button) findViewById(R.id.button2);
        reg_buttun = (Button) findViewById(R.id.button3);
        textView = (TextView) findViewById(R.id.textView12);
    }

    public void clicklistener(){

        /**
         * 登录
         * */
        log_buttun.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String user_string = user_editText.getText().toString();
                String pwd_string = pwd_editText.getText().toString();
                if (user_string.equals("") || pwd_string.equals("")) {
                    textView.setText("请输入用户名或密码！！！");
                } else {
                    newthread(user_string,pwd_string);
                }
            }
        });

        /**
         * 注册
         * */
        reg_buttun.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Loginactivity.this,Registeractivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void newthread(final String user_string, final String pwd_string){
        new Thread(new Runnable() {
            final mhandler hd = new mhandler();
            String user = null;
            String password = null;

            @Override
            public void run() {
                Message message = new Message();
                DBoperate.getConn();

                /**
                 * 获取用户名和密码
                 * */
                HashMap<String,Object> map = DBoperate.onQueryUser(user_string, "usermanager");
                user = map.get("username").toString();
                password = map.get("passwd").toString();

                if( user_string.equals(user) == true && pwd_string.equals(password) == true ){
                    Intent intent = new Intent();
                    intent.setClass(Loginactivity.this, MainActivity.class);
                    intent.putExtra("user",user_string);
                    startActivity(intent);
                    finish();
                }else if(user == ""){
                    message.what = 0;
                }else if(pwd_string.equals(password) == false){
                    message.what = 1;
                }

                /**
                 * 发送消息
                 * */
                hd.sendMessage(message);

                DBoperate.onClose();
            }
        }).start();
    }

    /**
     * 线程之间消息传递
     * */
    public class mhandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    textView.setText("无此用户！！");
                    break;
                case 1:
                    textView.setText("密码错误！！");
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * 设置返回键点击事件
     * */
    public void onBackPressed(){
        Intent intent = new Intent();
        intent.setClass(Loginactivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

}
