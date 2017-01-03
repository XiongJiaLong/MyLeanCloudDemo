package com.example.administrator.myleanclouddemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    ListView listView;
    List<String> list;
    ArrayAdapter<String> stringArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        listView = (ListView) findViewById(R.id.chat_list);
        list = new ArrayList<>();
        list.add("这是和tom的对话");
        stringArrayAdapter =
                new ArrayAdapter<>(ChatActivity.this, R.layout.support_simple_spinner_dropdown_item, list);
        listView.setAdapter(stringArrayAdapter);
    }

    /**
     *
     * 注册消息处理逻辑
     */
    private void getMessage(){
        AVIMMessageManager.registerMessageHandler(AVIMTextMessage.class,new AVIMMessageHandler(){
            @Override
            public void onMessage(AVIMMessage message, AVIMConversation conversation, AVIMClient client) {

                    Log.e("============","=========="+((AVIMTextMessage)message).getText());
                    list.add("from  Tom"+((AVIMTextMessage)message).getText());
                    stringArrayAdapter.notifyDataSetChanged();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE,0,0,"刷新");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getOrder()){
            case 0:
                Log.e("=========","=0==click=====");
                getMessage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
