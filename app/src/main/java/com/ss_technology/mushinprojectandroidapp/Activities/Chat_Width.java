package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Adapter.Chat_Message_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Config.CheckInternetConnection;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Chat_Container;
import com.ss_technology.mushinprojectandroidapp.Container.Chat_Message_Container;
import com.ss_technology.mushinprojectandroidapp.R;
import com.ss_technology.mushinprojectandroidapp.Services.Read_SMS;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Chat_Width extends AppCompatActivity {

    Chat_Container data;
    RecyclerView rec;
    EditText message;
    ApiCall apiCall;
    KeepMeLogin keepMeLogin;
    ArrayList<Chat_Message_Container> list;
    Chat_Message_Adapter adapter;
    Runnable mHandlerTask;
    Handler handler;
    private final static int INTERVAL = 1000 * 10; //1 minutes

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_width);

        data= (Chat_Container) getIntent().getSerializableExtra("obj");
        Toolbar toolbar=findViewById(R.id.toolbar);
        list=new ArrayList<>();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView name=findViewById(R.id.name);
        apiCall=new ApiCall(this);
        keepMeLogin=new KeepMeLogin(this);
        name.setText(data.getName());
        message=findViewById(R.id.message);
        ImageView imageView=findViewById(R.id.image);
        Picasso.get().load(BaseURL.userImage()+data.getImage()).into(imageView);
        rec=findViewById(R.id.rec);

        HashMap<String,String> mm=new HashMap<>();
        mm.put("chat_id",data.getChat_id());
        mm.put("receiver_id",data.getSender_id());
        mm.put("type","all_list");
        apiCall.Insert(mm, "getMessages.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray arr=new JSONArray(result);
                    for(int i=0; i<arr.length(); i++){
                        JSONObject msg=arr.getJSONObject(i);
                        Chat_Message_Container con=new Chat_Message_Container();
                        con.setId(msg.getString("id"));
                        con.setMessage(msg.getString("message"));
                        con.setSender_msg(msg.getString("sender_msg"));
                        if(data.getSender_id().trim().equals(msg.getString("sender"))){
                            con.setSender_id("1");
                        }
                        else{
                            con.setSender_id("0");
                        }
                        list.add(con);
                    }
                    if (!list.isEmpty()){
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Chat_Width.this);
                        rec.setLayoutManager(linearLayoutManager);
                        adapter=new Chat_Message_Adapter(list);
                        rec.setAdapter(adapter);
                        rec.smoothScrollToPosition(list.size()-1);
                        startRepeatingTask();
                    }

                }
                catch (Exception e){
                    finish();
                    Toast.makeText(Chat_Width.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                }
            }
        });


        handler = new Handler();
        mHandlerTask = new Runnable()
        {
            @Override
            public void run() {
                apiCall.Insert2(mm, "getMessages.php", new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            if (!list.isEmpty()){
                                list.clear();
                            }
                            JSONArray arr=new JSONArray(result);
                            for(int i=0; i<arr.length(); i++){
                                JSONObject msg=arr.getJSONObject(i);
                                Chat_Message_Container con=new Chat_Message_Container();
                                con.setId(msg.getString("id"));
                                con.setMessage(msg.getString("message"));
                                con.setSender_msg(msg.getString("sender_msg"));
                                if(data.getSender_id().trim().equals(msg.getString("sender"))){
                                    con.setSender_id("1");
                                }
                                else{
                                    con.setSender_id("0");
                                }
                                list.add(con);
                            }
                            if (!list.isEmpty()){
                                adapter.notifyDataSetChanged();
                                rec.smoothScrollToPosition(list.size()-1);
                            }

                        }
                        catch (Exception e){
                            finish();
                            Toast.makeText(Chat_Width.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                handler.postDelayed(mHandlerTask, INTERVAL);
            }
        };



    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void Submit_Message(View view) {
        if(CheckInternetConnection.Connection(Chat_Width.this)) {
            String msg = message.getText().toString();
            if (HelperFunctions.verify(msg)) {
                HashMap<String, String> ms = new HashMap<>();
                ms.put("message", msg);
                ms.put("chat_id", data.getChat_id());
                ms.put("sender_id", data.getSender_id());
                ms.put("receiver_id", data.getReceiver_id());
                ms.put("sender_msg", data.getSender_msg());
                ms.put("receiver_msg", data.getReceiver_msg());
                apiCall.Insert(ms, "insert_message.php", new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                       try {
                           if (result.trim().equals("1")) {
                               Chat_Message_Container con=new Chat_Message_Container();
                               con.setMessage(msg);
                               con.setSender_id("1");
                               list.add(con);
                               adapter.notifyDataSetChanged();
                               rec.smoothScrollToPosition(list.size()-1);
                               message.getText().clear();
                           } else {
                               Toast.makeText(Chat_Width.this, "Message not send try again", Toast.LENGTH_SHORT).show();
                           }
                       }
                       catch (Exception e){
                            Log.e("Basit",result);
                       }

                    }
                });
            }
        }
        else{
            Toast.makeText(this, "Check you internet connection!", Toast.LENGTH_SHORT).show();
        }
    }

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        handler.removeCallbacks(mHandlerTask);
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopRepeatingTask();
    }
}