package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ss_technology.mushinprojectandroidapp.Adapter.Chat_List_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Chat_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import javax.security.auth.login.LoginException;

public class Chats extends AppCompatActivity {

    RecyclerView rec;
    ApiCall apiCall;
    KeepMeLogin keepMeLogin;
    ArrayList<Chat_Container> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rec=findViewById(R.id.rec);
        rec.setHasFixedSize(true);
        apiCall=new ApiCall(this);
        keepMeLogin=new KeepMeLogin(this);
        list=new ArrayList<>();

        try {
            JSONObject object=new JSONObject(keepMeLogin.getData());
            HashMap<String,String> send=new HashMap<>();
            send.put("sender_id",object.getString("id"));
            apiCall.Insert(send, "getChatList.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {

                    try {
                        JSONObject sender=new JSONObject(result);
                        JSONArray sender_array=sender.getJSONArray("sender");
                        for(int i=0; i<sender_array.length(); i++){
                            JSONObject item=sender_array.getJSONObject(i);
                            Chat_Container con=new Chat_Container();
                            con.setChat_id(item.getString("id"));
                            con.setSender_id(item.getString("sender"));
                            con.setReceiver_id(item.getString("receiver"));
                            con.setName(item.getString("name"));
                            con.setMobile(item.getString("mobile"));
                            con.setImage(item.getString("image"));
                            con.setStatus(item.getString("status"));
                            con.setSender_msg("1");
                            con.setReceiver_msg("0");
                            list.add(con);
                        }
                        JSONArray receiver_array=sender.getJSONArray("receiver");
                        for(int i=0; i<receiver_array.length(); i++){
                            JSONObject item=receiver_array.getJSONObject(i);
                            Chat_Container con=new Chat_Container();
                            con.setChat_id(item.getString("id"));
                            con.setSender_id(item.getString("receiver"));
                            con.setReceiver_id(item.getString("sender"));
                            con.setName(item.getString("name"));
                            con.setMobile(item.getString("mobile"));
                            con.setImage(item.getString("image"));
                            con.setStatus(item.getString("status"));
                            con.setSender_msg("0");
                            con.setReceiver_msg("1");
                            list.add(con);
                        }
                        Chat_List_Adapter adapter=new Chat_List_Adapter(list,Chats.this);
                        rec.setLayoutManager(new LinearLayoutManager(Chats.this));
                        rec.setAdapter(adapter);
                    }
                    catch (Exception e){

                        Toast.makeText(Chats.this, "Error occured while getting data from json", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e){
           finish();
            Toast.makeText(this, "Something went try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}