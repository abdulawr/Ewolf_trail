package com.ss_technology.mushinprojectandroidapp.Activities;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Adapter.Train_Gif_Adapter;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Train_Gif_Container;
import com.ss_technology.mushinprojectandroidapp.R;
import com.ss_technology.mushinprojectandroidapp.Services.AlarmReceiver;
import com.ss_technology.mushinprojectandroidapp.Services.Read_SMS;
import com.ss_technology.mushinprojectandroidapp.Services.SendingAppoimentMessage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    KeepMeLogin keepMeLogin;
    NavigationView navigationView;
    DrawerLayout drawer;
    ApiCall apiCall;
    Spinner spinner;
    HashMap<String,String> train_cat;
    ArrayList<String> train_list;
    RecyclerView rec;
    ArrayList<Train_Gif_Container> train_gif_list;
    Train_Gif_Adapter adapter;
    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;
    boolean ch=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        keepMeLogin=new KeepMeLogin(this);

        apiCall=new ApiCall(this);
        train_list=new ArrayList<>();
        train_gif_list=new ArrayList<>();
        rec=findViewById(R.id.rec);
        rec.setHasFixedSize(true);
        train_cat=new HashMap<>();
        spinner=findViewById(R.id.spinner);
         drawer = findViewById(R.id.drawer_layout);
         navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
            toggle.syncState();
        drawer.addDrawerListener(toggle);
        navigationView.setNavigationItemSelectedListener(this);

        //start sms service
        startService(new Intent(getApplicationContext(), Read_SMS.class));

        // set background
        LinearLayout background_layout=findViewById(R.id.background_layout);
        Bitmap bitmap=BitmapFactory.decodeResource(Home.this.getResources(),R.drawable.back_image);
        Bitmap blur=blur(this,bitmap);
        BitmapDrawable ob = new BitmapDrawable(getResources(), blur);
        background_layout.setBackground(ob);

        try {
            JSONObject object=new JSONObject(keepMeLogin.getData());
            View headerItem=navigationView.getHeaderView(0);
            TextView name=headerItem.findViewById(R.id.name);
            TextView email=headerItem.findViewById(R.id.email);
            name.setText(object.getString("name"));
            email.setText(object.getString("email"));
            ImageView profile_image=headerItem.findViewById(R.id.profile_image);
            Picasso.get().load(BaseURL.userImage()+object.getString("image")).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(profile_image);
        }
        catch (Exception e){
            Toast.makeText(this, "Error occured in Json Parsing", Toast.LENGTH_SHORT).show();
            Log.e("TAG",e.getMessage());
        }

        apiCall.getWithOutAlert("getTrainCatList.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
             try {
                 JSONArray jsonArray=new JSONArray(result);
                 for(int i=0; i<jsonArray.length(); i++){
                     JSONObject item=jsonArray.getJSONObject(i);
                     train_cat.put(item.getString("name").trim(),item.getString("id"));
                     train_list.add(item.getString("name"));
                 }
                 if(!train_list.isEmpty()){
                     ArrayAdapter<String> list=new ArrayAdapter<String>(Home.this, android.R.layout.simple_list_item_1,train_list);
                     spinner.setAdapter(list);
                 }
             }
             catch (Exception e){
                 Toast.makeText(Home.this, "Can`t load the training category!", Toast.LENGTH_SHORT).show();
             }
            }
        });


        apiCall.get("getTrainGif.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try {
                    JSONArray arr=new JSONArray(result);
                    for (int i=0; i<arr.length(); i++){
                        JSONObject item=arr.getJSONObject(i);
                        Train_Gif_Container con=new Train_Gif_Container();
                        con.setId(item.getString("id"));
                        con.setName(item.getString("name"));
                        con.setLevel_id(item.getString("level_id"));
                        con.setDes(item.getString("des"));
                        con.setGif_path(item.getString("gif_path"));
                        con.setType(item.getString("type"));
                        train_gif_list.add(con);
                        if(!train_gif_list.isEmpty()){
                            adapter=new Train_Gif_Adapter(Home.this, train_gif_list, new Train_Gif_Adapter.ClickGIFlistner() {
                                @Override
                                public void GIF_url(String url) {

                                    View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_image_full_screen,null);
                                    AlertDialog.Builder al=new AlertDialog.Builder(Home.this);
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
                                    ImageView full_image=view.findViewById(R.id.img);
                                    Glide.with(Home.this).load(BaseURL.GifUrl()+url).listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            Log.e("TAG",e.getMessage());
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                            Log.e("TAG","Image loaded");
                                            return false;
                                        }
                                    }).into(full_image);
                                    dialog.show();                                }
                            });
                            rec.setLayoutManager(new LinearLayoutManager(Home.this));
                            rec.setAdapter(adapter);
                            ch=true;
                        }
                        else{
                            Toast.makeText(Home.this, "Nothing To Show!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                catch (Exception e){
                    Toast.makeText(Home.this, "Error occurred while loading data from json!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(ch){
                    String selectedItem = parent.getItemAtPosition(position).toString();
                    String cat_id=train_cat.get(selectedItem);
                    HashMap<String,String> mm=new HashMap<>();
                    mm.put("cat_id",cat_id);
                    apiCall.Insert(mm, "getTrainGif.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if (!result.trim().equals("") && !result.trim().equals("[]")) {
                                try {
                                    train_gif_list.clear();
                                    JSONArray arr=new JSONArray(result);
                                    for (int i=0; i<arr.length(); i++){
                                        JSONObject item=arr.getJSONObject(i);
                                        Train_Gif_Container con=new Train_Gif_Container();
                                        con.setId(item.getString("id"));
                                        con.setName(item.getString("name"));
                                        con.setLevel_id(item.getString("level_id"));
                                        con.setDes(item.getString("des"));
                                        con.setGif_path(item.getString("gif_path"));
                                        con.setType(item.getString("type"));
                                        train_gif_list.add(con);
                                        if(!train_gif_list.isEmpty()){
                                            adapter.notifyDataSetChanged();
                                        }
                                        else{
                                            Toast.makeText(Home.this, "Nothing To Show!", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                catch (Exception e){
                                    Toast.makeText(Home.this, "Error occurred while loading data from json!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else {
                                Toast.makeText(Home.this, "Nothing To Show!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {

            }
        });


        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        boolean firstStart = prefs.getBoolean("firstStart", true);
        if (firstStart) {
            alarmMgr = (AlarmManager) Home.this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(Home.this, AlarmReceiver.class);
            alarmIntent = PendingIntent.getBroadcast(Home.this, 0, intent, 0);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(System.currentTimeMillis());
            calendar.set(Calendar.HOUR_OF_DAY, 11);
            alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);

            SharedPreferences prefss = getSharedPreferences("prefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = prefss.edit();
            editor.putBoolean("firstStart", false);
            editor.apply();
        }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.signout){
            keepMeLogin.Clear();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
       int id=item.getItemId();
       if (id==R.id.contact){
           startActivity(new Intent(getApplicationContext(),Contact_Us.class));
       }
        else if (id==R.id.your_profile){
            startActivity(new Intent(getApplicationContext(),Profile.class));
        }
       else if (id==R.id.ask_question){
           startActivity(new Intent(getApplicationContext(),Website.class));
       }
       else if (id==R.id.diet_planner){
           startActivity(new Intent(getApplicationContext(),Diet_chart.class));
       }
       else if (id==R.id.sell_pets){
           startActivity(new Intent(getApplicationContext(),Sell_Pets.class));
       }
       else if (id==R.id.your_sell_pets){
           startActivity(new Intent(getApplicationContext(),Your_Sell_Pets_Post.class));
       }
       else if (id==R.id.buy_pets){
           startActivity(new Intent(getApplicationContext(),Buy_Pets.class));
       }
       else if (id==R.id.chats){
           startActivity(new Intent(getApplicationContext(),Chats.class));
       }
       else if (id==R.id.add_pets){
           startActivity(new Intent(getApplicationContext(),Add_Your_Pet.class));
       }
        else if (id==R.id.your_pets_list){
            startActivity(new Intent(getApplicationContext(),Your_Pets_List.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}