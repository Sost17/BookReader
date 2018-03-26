package com.lib.book.bookreader.Activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.lib.book.bookreader.Adapter.BookAdapter;
import com.lib.book.bookreader.ClickListener.NoDoubleClickListener;
import com.lib.book.bookreader.R;
import com.lib.book.bookreader.UserActivity.Loginactivity;
import com.lib.book.bookreader.Util.DBoperate;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private Button search_icon;
    ListView simpleList;
    BottomNavigationView navigation;
    BookAdapter adapter;
    String user = null;
    String getbookname = null;
    public HashMap<Object, String> selectInfo = new HashMap<Object, String>();
    int selectNavId = -1;

    @SuppressLint("HandlerLeak")
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
         * 判断用户是否已登录
         * */
        isuserlogin();

        /**
         * 按钮点击事件
         * */
        clicklistener();

    }

    private void init(){
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_main);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        simpleList = findViewById(R.id.array_booklistview);
        search_icon = findViewById(R.id.title_search);
    }

    private void getuser(){
        Intent getintent = getIntent();
        user = getintent.getStringExtra("user");
        getbookname = getintent.getStringExtra("bookname");
    }

    private void isuserlogin(){
        if (user != null) {
            navigation.getMenu().findItem(R.id.navigation_mybook).setChecked(true);
            navigation.getMenu().findItem(R.id.navigation_mybook).setTitle(user);
            selectNavId = 1;
            if(getbookname == null){
                newthread("getuserbook","");
            } else {
                navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                navigation.getMenu().findItem(R.id.navigation_mybook).setTitle(user);
                selectNavId = 0;
                ArrayList<HashMap<String,Object>> book_list = new ArrayList<HashMap<String,Object>>();
                HashMap<String,Object> mapbook = new HashMap<String,Object>();
                mapbook.put("name",getbookname);
                book_list.add(mapbook);
                adapter = new BookAdapter(MainActivity.this, book_list);
                simpleList.setAdapter(adapter);
            }
        } else {
            selectNavId = 0;
            newthread("getonlinebook","");
        }
    }

    private void clicklistener(){

        /**
         * 搜索按钮点击事件
         * */
        search_icon.setOnClickListener(new NoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View view) {
                if(user != null){
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, SearchActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    finish();
                } else{
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Loginactivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        /**
         * 导航栏点击事件
         * */
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /**
         * 长按ListView点击事件
         * */
        simpleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                /**
                 * 记录当前导航栏选择的标签
                 * */
                selectInfo = (HashMap<Object, String>) parent.getItemAtPosition(position);
                return false;
            }
        });

        /**
         * 注册一个上下文菜单
         * */
        registerForContextMenu(simpleList);

        /**
         * ListView点击事件
         * */
        simpleList.setOnItemClickListener(new NoDoubleClickListener.NoDoubleItemClickListener() {
            @Override
            public void onNoDoubleClick(AdapterView<?> parent, View view, int position, long id) {
                if(selectNavId == 1){
                    HashMap<Object, String> selectuserInfo = (HashMap<Object, String>) parent.getItemAtPosition(position);
                    String bookname = selectuserInfo.get("name").toString();
                    newthread("getlink",bookname);
                } else if(selectNavId == 0){
                    readbook(parent,position);
                }

            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectNavId = 0;
                    newthread("getonlinebook","");
                    return true;
                case R.id.navigation_mybook:
                    if (user == null) {
                        Intent intent = new Intent();
                        intent.setClass(MainActivity.this, Loginactivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        selectNavId = 1;
                        newthread("getuserbook","");
                    }
                    return true;
            }
            return false;
        }
    };

    /**
     * 创建系统菜单
     * */
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.logout, menu);
        if (user == null) {
            menu.findItem(R.id.menu_logout).setTitle("用户登录");
        }
        return true;
    }

    /**
     * 系统菜单点击事件
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.menu_logout:
                if (user == null) {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Loginactivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "退出成功", Toast.LENGTH_LONG).show();
                    navigation.getMenu().findItem(R.id.navigation_home).setChecked(true);
                    navigation.getMenu().findItem(R.id.navigation_mybook).setTitle("我的书架");
                    selectNavId = 0;
                    newthread("getonlinebook","");
                    user = null;
                    item.setTitle("用户登录");
                }
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 创建上下文菜单
     * */
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(selectNavId == 1){
            if(user != null){
                menu.removeItem(1);
                menu.add(0, 2, 0, "删除");
            }
        }
        if (selectNavId == 0) {
            if (user != null) {
                menu.removeItem(2);
                menu.add(0, 1, 0, "添加到我的书架");
            } else {
                menu.add(0, 1, 0, "用户登录");
            }
        }
    }

    /**
     * 上下文菜单点击事件
     * */
    public boolean onContextItemSelected(MenuItem item) {
        String bookname = selectInfo.get("name").toString();
        switch (item.getItemId()) {
            case 1:
                if (user != null) {

                    newthread("addbook",bookname);
                } else {
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, Loginactivity.class);
                    startActivity(intent);
                    finish();
                }
                break;
            case 2:
                newthread("deletebook",bookname);
                break;
            default:
                break;
        }
        return super.onContextItemSelected(item);
    }

    /**
     * ListView点击View
     * */
    public void readbook(AdapterView<?> parent ,int clickID){
        HashMap<Object, String> selectuserInfo = (HashMap<Object, String>) parent.getItemAtPosition(clickID);

        new AlertDialog.Builder(MainActivity.this)

                .setTitle("图书信息")

                .setMessage("名称："+ selectuserInfo.get("name").toString())

                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                    }

                })

                .show();

    }

    /**
     * 线程操作
     * */
    private void newthread(final String threadtag, final String threadinfo){
        new Thread(new Runnable() {
            final mhandler hd = new mhandler();

            @Override
            public void run() {
                DBoperate.getConn();
                Message message = new Message();

                if(threadtag.equals("getlink")){
                    message.what = 5;

                    /**
                     * 获取图书在线链接
                     */
                    message.obj = DBoperate.onQuerybooklink(threadinfo);

                } else if(threadtag.equals("getonlinebook")){
                    message.what = 1;

                    /**
                     * 获得在线书库图书
                     */
                    message.obj = DBoperate.onQuery("book_name", "onlinelibrary");

                } else if(threadtag.equals("getuserbook")){
                    message.what = 1;

                    /**
                     * 获得我的书架的图书
                     */
                    message.obj = DBoperate.onQueryUserbook("book_name", "userlibrary", user);

                } else if(threadtag.equals("addbook")){

                    String havebook = null;

                    /**
                     * 添加图书到我的书架，更新图书火热指数
                     * */
                    int hot = DBoperate.onQueryhot(threadinfo) + 1;
                    havebook = DBoperate.onQueryInsertUserbook(threadinfo, user);
                    if (havebook == null) {
                        DBoperate.onInsertUserbook(user, threadinfo);
                        DBoperate.onupdatehot(hot, threadinfo);
                        message.what = 2;
                    } else {
                        message.what = 3;
                    }
                } else if(threadtag.equals("deletebook")){

                    /**
                     * 从我的书架删除图书
                     * */
                    DBoperate.ondeleteuserbook(user,threadinfo);
                    message.what = 4;
                    message.obj = DBoperate.onQueryUserbook("book_name", "userlibrary", user);
                }

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
                case 1:
                    adapter = new BookAdapter(MainActivity.this, (ArrayList<HashMap<String, Object>>) msg.obj);
                    simpleList.setAdapter(adapter);
                    break;
                case 2:
                    Toast.makeText(MainActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(MainActivity.this, "本书已经在书架上了", Toast.LENGTH_LONG).show();
                    break;
                case 4:
                    adapter = new BookAdapter(MainActivity.this, (ArrayList<HashMap<String, Object>>) msg.obj);
                    simpleList.setAdapter(adapter);
                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Intent intent = new Intent();
                    intent.setClass(MainActivity.this, RemotePDFActivity.class);
                    intent.putExtra("url", (String) msg.obj);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }
    }
}
