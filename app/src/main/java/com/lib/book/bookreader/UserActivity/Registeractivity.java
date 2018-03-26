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
import android.widget.Toast;

import com.lib.book.bookreader.Activity.MainActivity;
import com.lib.book.bookreader.ClickListener.NoDoubleClickListener;
import com.lib.book.bookreader.R;
import com.lib.book.bookreader.Util.DBoperate;

import java.util.HashMap;

/**
 * Created by commo on 2017/3/17.
 */

public class Registeractivity extends Activity {

    private EditText user_editText;
    private EditText pwd_editText;
    private EditText again_pwd_editText;
    private Button success_buttun;
    private TextView textView;

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
        setContentView(R.layout.activity_register);

        user_editText = (EditText) findViewById(R.id.editText4);
        pwd_editText = (EditText) findViewById(R.id.editText3);
        again_pwd_editText = (EditText) findViewById(R.id.editText5);
        success_buttun = (Button) findViewById(R.id.button4);
        textView = (TextView) findViewById(R.id.textView11);
    }

    public void clicklistener(){

        /**
         * 注册
         * */
        success_buttun.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String user_string = user_editText.getText().toString();
                String pwd_string = pwd_editText.getText().toString();
                String againpwd_string = again_pwd_editText.getText().toString();

                if (pwd_string.equals(againpwd_string) != true) {
                    textView.setText("两次密码不一致！！！");
                } else if (user_string.equals("")) {
                    textView.setText("请输入用户名！！！");
                } else {
                    newthread(user_string,pwd_string);
                }
            }
        });
    }

    public void newthread(final String user_string, final String pwd_string){
        new Thread(new Runnable() {
            final mhandler hd = new mhandler();
            String user = null;
            @Override
            public void run() {
                Message message = new Message();
                DBoperate.getConn();

                /**
                 * 获取用户名和密码
                 * */
                HashMap<String,Object> map = DBoperate.onQueryUser(user_string, "usermanager");
                user = map.get("username").toString();


                if( user_string.equals(user) == true){
                    message.what = 0;
                }else {

                    /**
                     * 用户注册
                     * */
                    DBoperate.onInsertUser(user_string,pwd_string);

                    Intent intent = new Intent();
                    intent.setClass(Registeractivity.this, MainActivity.class);
                    intent.putExtra("user", user_string);
                    startActivity(intent);
                    finish();

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
                    textView.setText("该用户已存在！！");
                    break;
                case 1:
                    Toast.makeText(Registeractivity.this,"注册成功！！",Toast.LENGTH_SHORT).show();
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
        intent.setClass(Registeractivity.this,Loginactivity.class);
        startActivity(intent);
        finish();
    }
}
