package com.lib.book.bookreader.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lib.book.bookreader.ClickListener.NoDoubleClickListener;
import com.lib.book.bookreader.R;
import com.lib.book.bookreader.Util.DBoperate;

import java.util.ArrayList;
import java.util.HashMap;

import me.gujun.android.taggroup.TagGroup;

public class SearchActivity extends Activity {


    private TagGroup taggroup;
    private ImageView search_back;
    private EditText search_input;
    private ImageView search_delete,search_button;
    String user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 参数初始化
         * */
        init();

        /**
         * 获得页面跳转传过来的user
         * */
        getuser();

        /**
         * 获得火热图书
         * */
        getTagGroup();

        /**
         * 按钮点击事件
         * */
        clicklistener();
    }

    private void init(){
        setContentView(R.layout.activity_search);

        taggroup = findViewById(R.id.search_tg_hot);
        search_back = findViewById(R.id.search_iv_back);
        search_input = findViewById(R.id.search_et_input);
        search_delete = findViewById(R.id.search_iv_delete);
        search_button = findViewById(R.id.search_iv_search);
    }

    private void getuser(){
        Intent getintent = getIntent();

        user = getintent.getStringExtra("user");
    }

    private void getTagGroup(){
        new Thread(new Runnable() {
            final mhandler hd = new mhandler();

            @Override
            public void run() {
                DBoperate.getConn();
                Message message = new Message();
                ArrayList<HashMap<String,Object>> hotnum,book1,book2,book3;
                message.what = 0;

                /**
                 * 获得排名前二图书火热指数
                 * */
                hotnum = DBoperate.onQueryhotnum();

                /**
                 * 获得火热图书名称
                 * */
                book1 = DBoperate.onQueryhotbook((Integer) hotnum.get(0).get("num"));
                book2 = DBoperate.onQueryhotbook((Integer) hotnum.get(1).get("num"));

                String[] hotbook = new String[50];
                int j = 0;

                for(int i = 0 ; i < book1.size() ; i ++){
                    hotbook[i] = book1.get(i).get("bookname").toString();
                    j = i + 1 ;
                }
                for(int i = 0 ; i < book2.size() ; i ++,j ++){
                    hotbook[j] = book2.get(i).get("bookname").toString();
                }

                String[] sendhotbook = new String[j];
                for(int m = 0 ; m < sendhotbook.length ; m ++){
                    sendhotbook[m] = hotbook[m];
                }
                message.obj = sendhotbook;

                /**
                 * 发送消息
                 * */
                hd.sendMessage(message);

                DBoperate.onClose();
            }
        }).start();
    }

    private void clicklistener(){

        /**
         * 返回
         * */
        search_back.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this, MainActivity.class);
                intent.putExtra("user",user);
                startActivity(intent);
                finish();
            }
        });

        /**
         * 删除
         * */
        search_delete.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                search_input.setText("");
            }
        });

        /**
         * 搜索
         * */
        search_button.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                String seach_info = search_input.getText().toString();
                if(!seach_info.equals("")){
                    newthread(seach_info);
                }else{
                    Toast.makeText(SearchActivity.this,"请输入书名",Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    private TagGroup.OnTagClickListener mTagClickListener = new TagGroup.OnTagClickListener() {
        @Override
        public void onTagClick(String tag) {
            search_input.setText(tag);
        }
    };

    private void newthread(final String search_info){
        new Thread(new Runnable() {
            final mhandler hd = new mhandler();

            @Override
            public void run() {
                DBoperate.getConn();
                Message message = new Message();
                String bookname = null;

                /**
                 * 搜索获取图书名称
                 * */
                bookname = DBoperate.onQuerybook(search_info);

                if(bookname == null){
                    message.what = 1;
                } else{
                    message.what = 2;
                    message.obj = bookname;
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
    private class mhandler extends Handler {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    taggroup.setTags((String[]) msg.obj);
                    taggroup.setOnTagClickListener(mTagClickListener);
                    break;
                case 1:
                    Toast.makeText(SearchActivity.this,"书库未收录此书",Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Intent intent = new Intent();
                    intent.setClass(SearchActivity.this, MainActivity.class);
                    intent.putExtra("bookname",(String) msg.obj);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
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
        intent.setClass(SearchActivity.this, MainActivity.class);
        intent.putExtra("user",user);
        startActivity(intent);
        finish();
    }
}
