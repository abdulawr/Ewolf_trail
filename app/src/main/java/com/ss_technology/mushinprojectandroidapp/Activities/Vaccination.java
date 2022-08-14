package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.ss_technology.mushinprojectandroidapp.Adapter.Your_Sell_Pets_Post_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Sell_Pets_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Vaccination extends AppCompatActivity {

    ApiCall apiCall;
    KeepMeLogin keepMeLogin;
    RecyclerView rec;
    ArrayList<Sell_Pets_Container> list;
    Your_Sell_Pets_Post_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccination);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        list=new ArrayList<>();
        rec=findViewById(R.id.rec);
        apiCall=new ApiCall(this);
        keepMeLogin=new KeepMeLogin(this);
        HashMap<String,String> map=new HashMap<>();


        try {
            JSONObject object=new JSONObject(keepMeLogin.getData());
            map.put("userID",object.getString("id"));
            apiCall.Insert(map, "getVaccinationData.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONArray array = new JSONArray(result);
                        for(int i=0; i<array.length(); i++){
                            JSONObject pet=array.getJSONObject(i);
                            int age_day= HelperFunctions.getAge(pet.getString("dob"),"-");
                            Log.e("Basit",String.valueOf(age_day));
                            boolean status = false;
                            String message="";
                            if (age_day == 43){
                                message ="It is to inform you that after 2 day, It time for the first shot of vaccination";
                                status = true;
                            }else if(age_day == 64){
                                message ="It is to inform you that after 2 day, It time for the second shot of vaccination";
                                status = true;
                            }else if(age_day == 82){
                                message ="It is to inform you that after 2 day, It time for the third shot of vaccination";
                                status = true;
                            }else if(age_day == 100){
                                message ="It is to inform you that after 2 day, It time for the last shot of vaccination";
                                status = true;
                            }

                            if (status){
                                Sell_Pets_Container con=new Sell_Pets_Container();
                                con.setId(pet.getString("id"));
                                con.setName(pet.getString("name"));
                                con.setBread(pet.getString("bread"));
                                con.setAdded_date(pet.getString("dob"));
                                con.setImage(pet.getString("image"));
                                con.setGender(pet.getString("gender"));
                                con.setMessage(message);
                                list.add(con);
                            }
                        }

                        rec.setHasFixedSize(true);
                        rec.setLayoutManager(new LinearLayoutManager(Vaccination.this));
                        adapter= new Your_Sell_Pets_Post_Adapter(Vaccination.this,true,list,null);
                        rec.setAdapter(adapter);
                    }
                    catch (Exception e){
                        Log.e("Basit",e.getMessage());
                    }

                }
            });
        }
        catch (Exception e){
            Log.e("TAG",e.getMessage());
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }
}