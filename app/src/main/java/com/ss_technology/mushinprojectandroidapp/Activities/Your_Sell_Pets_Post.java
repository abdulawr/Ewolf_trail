package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.ss_technology.mushinprojectandroidapp.Adapter.Your_Sell_Pets_Post_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Sell_Pets_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Your_Sell_Pets_Post extends AppCompatActivity {

    RecyclerView rec;
    KeepMeLogin keepMeLogin;
    ApiCall apiCall;
    ArrayList<Sell_Pets_Container> list;
    Your_Sell_Pets_Post_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_sell_pets_post);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        try {
            JSONObject object=new JSONObject(keepMeLogin.getData());
            HashMap<String,String> mp=new HashMap<>();
            mp.put("user_id",object.getString("id"));
            apiCall.Insert(mp, "getUserPost.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try{
                        JSONArray arr=new JSONArray(result);
                        for (int i=0; i<arr.length(); i++){
                            JSONObject item=arr.getJSONObject(i);
                            Sell_Pets_Container dd=new Sell_Pets_Container();
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

                        if (!list.isEmpty()){
                            adapter=new Your_Sell_Pets_Post_Adapter(Your_Sell_Pets_Post.this,true, list, new Your_Sell_Pets_Post_Adapter.Delete_Click() {
                                @Override
                                public void onclick(String post_id, int position) {
                                    HashMap<String,String> ms=new HashMap<>();
                                    ms.put("post_id",post_id);
                                    apiCall.Insert(ms, "delete_sell_pets_post.php", new VolleyCallback() {
                                        @Override
                                        public void onSuccess(String result) {
                                            if(result.trim().equals("1")){
                                                Toast.makeText(Your_Sell_Pets_Post.this, "Successfully deleted!", Toast.LENGTH_SHORT).show();
                                                list.remove(position);
                                                adapter.notifyDataSetChanged();
                                            }
                                            else{
                                                Toast.makeText(Your_Sell_Pets_Post.this, "Error occured try again!", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });

                            rec.setHasFixedSize(true);
                            rec.setLayoutManager(new LinearLayoutManager(Your_Sell_Pets_Post.this));
                            rec.setAdapter(adapter);
                        }
                        else {
                            Toast.makeText(Your_Sell_Pets_Post.this, "Nothing to show!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(Your_Sell_Pets_Post.this, "Error occured in Json Parsing", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, "Error occurred in Json parsing?", Toast.LENGTH_SHORT).show();
        }
    }

    private void init(){

        rec=findViewById(R.id.rec);
        list=new ArrayList<>();
        keepMeLogin=new KeepMeLogin(this);
        apiCall=new ApiCall(this);

        FrameLayout background_layout=findViewById(R.id.background_layout);
        Bitmap bitmap= BitmapFactory.decodeResource(Your_Sell_Pets_Post.this.getResources(),R.drawable.back_image);
        Bitmap blur=blur(this,bitmap);
        BitmapDrawable ob = new BitmapDrawable(getResources(), blur);
        background_layout.setBackground(ob);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

    public Bitmap blur(Context context, Bitmap image) {
        final float BITMAP_SCALE = 0.4f;
        final float BLUR_RADIUS = 7.5f;
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(BLUR_RADIUS);
        theIntrinsic.setInput(tmpIn);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}