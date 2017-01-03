package com.example.administrator.myleanclouddemo;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.SaveCallback;
import com.avos.avoscloud.im.v2.AVIMClient;
import com.avos.avoscloud.im.v2.AVIMConversation;
import com.avos.avoscloud.im.v2.AVIMException;
import com.avos.avoscloud.im.v2.AVIMMessage;
import com.avos.avoscloud.im.v2.AVIMMessageHandler;
import com.avos.avoscloud.im.v2.AVIMMessageManager;
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback;
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback;
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button btn_create;
    Button btn_send;
    Button btn_openConv;
    ListView listView;
    List<String> list;
    CoordinatorLayout layout;
    boolean isCreate = true;
    AVIMClient jerry;
    ArrayAdapter<String> stringArrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_create = (Button) findViewById(R.id.btn_create_conv);
        layout = (CoordinatorLayout) findViewById(R.id.container);
        btn_create.setOnClickListener(this);
        btn_send = (Button) findViewById(R.id.btn_to_jerry);
        btn_send.setOnClickListener(this);
        btn_openConv = (Button) findViewById(R.id.btn_open_conv);
        btn_openConv.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.main_list);
        list = new ArrayList<>();
        list.add("对话列表");
        stringArrayAdapter = new ArrayAdapter<>(MainActivity.this,
                R.layout.support_simple_spinner_dropdown_item, list);
        listView.setAdapter(stringArrayAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_conv:
                createConv();
                break;
            case R.id.btn_to_jerry:
                sendMessageToJerry();
                getMessage();
                break;
            case R.id.btn_open_conv:
                Intent intent = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(intent);
                break;
        }
    }

    private void createConv(){
        if (isCreate) {
            jerry = AVIMClient.getInstance("Jerry");
            jerry.open(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient avimClient, AVIMException e) {
                    if (e == null) {
                        //创建对话
                        avimClient.createConversation(Arrays.asList("Bob", "Harry", "William"),
                                "猫和老鼠", null, new AVIMConversationCreatedCallback() {
                                    @Override
                                    public void done(AVIMConversation avimConversation, AVIMException e) {
                                        if (e == null) {
                                            SnackbarUtil.createSnackBar(layout,"创建对话完成");
                                        }
                                    }
                                });
                    }
                }
            });
            isCreate = false;
        }else {
            SnackbarUtil.createSnackBar(layout,"对话已经创建");
        }
    }

    private void createSnackbar(String s){
        Snackbar.make(layout,s,Snackbar.LENGTH_SHORT)
                .setAction("确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Snackbar.make(layout,"处理完成",Snackbar.LENGTH_SHORT).show();
                    }
                }).show();
    }

    private void sendToServer(){
        AVObject testObject = new AVObject("TestObject");
        testObject.put("words","我是熊佳龙");
        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if(e == null){
                    Log.d("MainActivity","success");
                }
            }
        });
    }

    private void sendMessageToJerry() {
        AVIMClient tom = AVIMClient.getInstance("Tom");
        if (jerry != null) {
            // 与服务器连接
            tom.open(new AVIMClientCallback() {
                @Override
                public void done(AVIMClient client, AVIMException e) {
                    if (e == null) {
                        // 创建与Jerry之间的对话
                        client.createConversation(Arrays.asList("Jerry"), "Tom & Jerry", null,
                                new AVIMConversationCreatedCallback() {

                                    @Override
                                    public void done(AVIMConversation conversation, AVIMException e) {
                                        if (e == null) {
                                            AVIMTextMessage msg = new AVIMTextMessage();
                                            msg.setText("耗子，起床！");
                                            // 发送消息
                                            conversation.sendMessage(msg, new AVIMConversationCallback() {

                                                @Override
                                                public void done(AVIMException e) {
                                                    if (e == null) {
                                                        Snackbar.make(layout, "发送成功", Snackbar.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                    }
                                });
                    }
                }
            });
        }else {
            Snackbar.make(layout, "发送失败", Snackbar.LENGTH_SHORT).show();
        }
    }

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
}
