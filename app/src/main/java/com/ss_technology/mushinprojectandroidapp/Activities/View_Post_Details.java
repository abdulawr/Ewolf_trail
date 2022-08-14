package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Adapter.Images_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class View_Post_Details extends AppCompatActivity {

    ApiCall apiCall;
    RecyclerView rec;
    ArrayList<String> list;
    TextView name,dob,color,bread,coat,age,gender,confirm_stud,confirm_breeder,dog_type,predigree_link,price,contact;
    KeepMeLogin keepMeLogin;
    HashMap<String,String> chatMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_post_details);

        getSupportActionBar().setTitle(getIntent().getStringExtra("name"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        String post_id=getIntent().getStringExtra("id");
        apiCall=new ApiCall(this);
        rec=findViewById(R.id.rec);
        rec.setHasFixedSize(true);
        list=new ArrayList<>();
        chatMap=new HashMap<>();
        keepMeLogin=new KeepMeLogin(this);

        try {
           JSONObject ob=new JSONObject(keepMeLogin.getData());
           chatMap.put("sender_id",ob.getString("id"));
        }
        catch (Exception e){
            Toast.makeText(this, "Error occurred while getting data from Json", Toast.LENGTH_SHORT).show();
        }

        name=findViewById(R.id.name);
        contact=findViewById(R.id.contact);
        dob=findViewById(R.id.dob);
        color=findViewById(R.id.color);
        bread=findViewById(R.id.bread);
        price=findViewById(R.id.price);
        coat=findViewById(R.id.coat);
        age=findViewById(R.id.age);
        gender=findViewById(R.id.gender);
        confirm_stud=findViewById(R.id.confirm_stud);
        confirm_breeder=findViewById(R.id.confirm_breeder);
        dog_type=findViewById(R.id.dog_type);
        predigree_link=findViewById(R.id.predigree_link);

        HashMap<String,String> mp=new HashMap<>();
        mp.put("post_id",post_id);
        apiCall.Insert(mp, "getPostDetails.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject object=new JSONObject(result);
                    // contain post related information
                    JSONObject post=object.getJSONObject("data");
                    name.setText(post.getString("name"));
                    dob.setText(post.getString("dob"));
                    color.setText(post.getString("color"));
                    bread.setText(post.getString("bread"));
                    coat.setText(post.getString("coat"));
                    age.setText(post.getString("age"));
                    gender.setText(post.getString("gender"));
                    price.setText(post.getString("price")+"  PKR");
                    confirm_stud.setText(post.getString("conform_stud"));
                    confirm_breeder.setText(post.getString("conform_breeder"));
                    dog_type.setText(post.getString("dog_type"));
                    predigree_link.setText(post.getString("predigree_li"));

                    // contain user related information
                    JSONObject user=object.getJSONObject("user");
                    chatMap.put("receiver_id",user.getString("id"));
                    contact.setText("0"+user.getString("mobile"));

                    JSONArray img_array=object.getJSONArray("images");
                    for (int i=0; i<img_array.length(); i++){
                        JSONObject img=img_array.getJSONObject(i);
                        list.add(img.getString("image"));
                    }
                    Images_Adapter adapter=new Images_Adapter(list, new Images_Adapter.OnImgClick() {
                        @Override
                        public void getUrl(String url) {
                            View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_post_image_full_screen,null);
                            AlertDialog.Builder al=new AlertDialog.Builder(View_Post_Details.this);
                            al.setView(view);
                            Dialog dialog=al.create();
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            ImageView close_image=view.findViewById(R.id.close_icon);
                            close_image.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                            ImageView full_image=view.findViewById(R.id.full_image);
                            Picasso.get().load(BaseURL.PostUrl()+url).into(full_image);
                            dialog.show();
                        }
                    });
                    rec.setLayoutManager(new LinearLayoutManager(View_Post_Details.this,LinearLayoutManager.HORIZONTAL, false));
                    rec.setAdapter(adapter);
                }
                catch (Exception e){
                    Toast.makeText(View_Post_Details.this, "Error occurred in Json parsing!", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public void Chat_Now(View view) {
        apiCall.Insert(chatMap, "insert_into_chatList.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
             if(result.trim().equals("1")){
                 startActivity(new Intent(View_Post_Details.this,Chats.class));
             }
               else if(result.trim().equals("2")){
                    startActivity(new Intent(View_Post_Details.this,Chats.class));
               }
             else{
                 Toast.makeText(View_Post_Details.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                 Log.e("TAG",result);
             }
            }
        });
    }
}
