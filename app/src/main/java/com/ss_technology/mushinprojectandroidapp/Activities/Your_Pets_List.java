package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ss_technology.mushinprojectandroidapp.Adapter.Your_Sell_Pets_Post_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Sell_Pets_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Your_Pets_List extends AppCompatActivity {

    RecyclerView rec;
    ApiCall apiCall;
    KeepMeLogin keepMeLogin;
    ArrayList<Sell_Pets_Container> list;
    Your_Sell_Pets_Post_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_pets_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        rec=findViewById(R.id.rec);
        keepMeLogin=new KeepMeLogin(this);
        apiCall=new ApiCall(this);
        list=new ArrayList<>();

        HashMap<String,String> mp=new HashMap<>();
        try{
            JSONObject ob=new JSONObject(keepMeLogin.getData());
            mp.put("user_id",ob.getString("id"));
            apiCall.Insert(mp, "getPet_List.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONArray ar=new JSONArray(result);
                        for(int i=0; i<ar.length(); i++){
                            JSONObject item=ar.getJSONObject(i);
                            Sell_Pets_Container con=new Sell_Pets_Container();
                            con.setId(item.getString("id"));
                            con.setName(item.getString("name"));
                            con.setBread(item.getString("bread"));
                            con.setAdded_date(item.getString("dob"));
                            con.setImage(item.getString("image"));
                            con.setGender(item.getString("gender"));
                            list.add(con);
                        }
                        if(!list.isEmpty()){
                            rec.setLayoutManager(new LinearLayoutManager(Your_Pets_List.this));
                             adapter= new Your_Sell_Pets_Post_Adapter(Your_Pets_List.this,false,list, new Your_Sell_Pets_Post_Adapter.Delete_Click() {
                                @Override
                                public void onclick(String post_id, int position) {
                                    HashMap<String,String> ms=new HashMap<>();
                                    ms.put("post_id",post_id);
                                    apiCall.Insert(ms, "delete_Your_Pets.php", new VolleyCallback() {
                                        @Override
                                        public void onSuccess(String result) {
                                           if (result.trim().equals("1")){
                                               list.remove(position);
                                               adapter.notifyDataSetChanged();
                                           }
                                           else{
                                               Toast.makeText(Your_Pets_List.this, "Can`t delet it try again!", Toast.LENGTH_SHORT).show();
                                           }
                                        }
                                    });
                                }
                            });
                            rec.setAdapter(adapter);
                        }
                        else {
                            Toast.makeText(Your_Pets_List.this, "Nothing to show!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(Your_Pets_List.this, "Error occured try again", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, "Error occurred in Json parsing!", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}