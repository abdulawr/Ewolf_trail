package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ss_technology.mushinprojectandroidapp.Adapter.Buy_Pets_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Sell_Pets_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Buy_Pets extends AppCompatActivity {

    ApiCall apiCall;
    ArrayList<Sell_Pets_Container> list;
    RecyclerView rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_pets);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        HashMap<String,String> mp=new HashMap<>();
        KeepMeLogin keepMeLogin=new KeepMeLogin(this);
        try {
          JSONObject object=new JSONObject(keepMeLogin.getData());
            mp.put("user_id",object.getString("id"));

            apiCall.Insert(mp,"get_Buy_Pets_List.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {

                    try {
                        JSONArray arr = new JSONArray(result);
                        for (int i = 0; i < arr.length(); i++) {
                            JSONObject item = arr.getJSONObject(i);
                            Sell_Pets_Container dd = new Sell_Pets_Container();
                            dd.setId(item.getString("id"));
                            dd.setName(item.getString("name"));
                            dd.setPrice(item.getString("price"));
                            dd.setDob(item.getString("dob"));
                            dd.setAge(item.getString("age"));
                            dd.setGender(item.getString("gender"));
                            dd.setDog_type(item.getString("dog_type"));
                            dd.setStatus(item.getString("status"));
                            dd.setAdded_date(item.getString("added_data"));
                            dd.setBread(item.getString("bread"));
                            dd.setImage(item.getString("image"));
                            list.add(dd);
                        }
                        Buy_Pets_Adapter adapter=new Buy_Pets_Adapter(Buy_Pets.this,list);
                        rec.setHasFixedSize(true);
                        rec.setLayoutManager(new LinearLayoutManager(Buy_Pets.this));
                        rec.setAdapter(adapter);
                    }
                    catch (Exception e){
                        Log.e("Basit",e.getMessage());
                        Toast.makeText(Buy_Pets.this, "Error occured in Json parsing", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        catch (Exception e){
            Toast.makeText(this, "Error occured in json parsing!", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){
        apiCall=new ApiCall(this);
        list=new ArrayList<>();
        rec=findViewById(R.id.rec);
        rec.setHasFixedSize(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}