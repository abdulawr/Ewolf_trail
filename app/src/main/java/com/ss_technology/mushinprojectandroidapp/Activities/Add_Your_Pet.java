package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Add_Your_Pet extends AppCompatActivity {

    ApiCall apiCall;
    Spinner dog_bread_spinner;
    ArrayList<String> dog_bread_list;
    HashMap<String,String> dog_bread_map;
    Bitmap bitmap=null;
    DatePicker datepicker;
    KeepMeLogin keepMeLogin;
    final  int REQUEST_CODE=111;
    final int REQUST_CODE_IMAGE=222;
    TextInputEditText name,color,coat;
    Spinner dog_gender,dog_type_spinners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_your_pet);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        keepMeLogin=new KeepMeLogin(this);
        apiCall=new ApiCall(this);
        datepicker=findViewById(R.id.datepicker);
        coat=findViewById(R.id.coat);
        dog_gender=findViewById(R.id.dog_gender);
        dog_type_spinners=findViewById(R.id.dog_type_spinners);
        dog_bread_spinner=findViewById(R.id.dog_bread_spinner);
        dog_bread_map=new HashMap<>();
        dog_bread_list=new ArrayList<>();
        name=findViewById(R.id.name);
        color=findViewById(R.id.color);


        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Add_Your_Pet.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }

        apiCall.get("getDogBreads.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                try{
                    JSONArray array=new JSONArray(result);
                    for(int i=0; i<array.length(); i++){
                        JSONObject item=array.getJSONObject(i);
                        dog_bread_map.put(item.getString("name").trim(),item.getString("id"));
                        dog_bread_list.add(item.getString("name").trim());
                    }
                    ArrayAdapter<String> ad=new ArrayAdapter<>(Add_Your_Pet.this, android.R.layout.simple_list_item_1,dog_bread_list);
                    dog_bread_spinner.setAdapter(ad);
                }
                catch (Exception e){
                    Toast.makeText(Add_Your_Pet.this, "Something went wrong in downloading dog bread information from server try again!", Toast.LENGTH_SHORT).show();
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

    public void Choose_Images(View view) {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Add_Your_Pet.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else
        {
            SelectImage();
        }
    }

    public void SelectImage()
    {
        Intent intent=new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        if(intent.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUST_CODE_IMAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == REQUEST_CODE && permissions.length >0)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                //SelectImage();
            }
            else {
                ActivityCompat.requestPermissions(Add_Your_Pet.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUST_CODE_IMAGE && resultCode == Activity.RESULT_OK)
        {
            if(data != null)
            {
                Uri uri=data.getData();
                if (uri != null)
                {
                    try {
                        InputStream stream=getApplicationContext().getContentResolver().openInputStream(uri);
                        Bitmap original = BitmapFactory.decodeStream(stream);
                        bitmap=original;
                    } catch (Exception e) {
                        Toast.makeText(this, "Some thing went wrong try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }


    public void Submit_Dog_Details(View view) {
        if(bitmap != null){
            String na=name.getText().toString();
            Date d=new Date();
            d.setDate(datepicker.getDayOfMonth());
            d.setMonth(datepicker.getMonth());
            d.setYear(datepicker.getYear()-1900);
            SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
            String dog_DOB = df.format(d);

         String col=color.getText().toString();
         String coa=coat.getText().toString();
         String dog_bread=dog_bread_map.get(dog_bread_spinner.getSelectedItem());
         String gender=dog_gender.getSelectedItem().toString();
         String dog_type=dog_type_spinners.getSelectedItem().toString();
         String image=HelperFunctions.BitmapToString(bitmap,90);
         HashMap<String,String> mp=new HashMap<>();
         if (HelperFunctions.verify(na) && HelperFunctions.verify(col)){
          try{
              if(!HelperFunctions.verify(coa)){
                  coa="none";
              }
              
             JSONObject ob=new JSONObject(keepMeLogin.getData());
             mp.put("user_id",ob.getString("id"));
             mp.put("dob",dog_DOB);
              mp.put("name",na);
              mp.put("coat",coa);
              mp.put("color",col);
              mp.put("bread",dog_bread);
              mp.put("gender",gender);
              mp.put("dog_type",dog_type);
              mp.put("image",image);

                apiCall.Insert(mp, "Add_Pets.php", new VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                       try{
                         JSONObject object=new JSONObject(result);
                         if(object.getString("status").trim().equals("1")){

                             if(HelperFunctions.getAge(dog_DOB,"/") == 45){
                                 String go_Message="Your pet '"+na+"', It`s time for shot of vaccination today. We recommended you to give first shot of vaccination";
                                 HelperFunctions.ShowNotification(Add_Your_Pet.this,"Vaccination Notification",go_Message,null);
                             }
                             else if(HelperFunctions.getAge(dog_DOB,"/") == 44){
                                 String go_Message="Your pet '"+na+"', age will be 45 after one days. We recommended you to give first shot of vaccination after 1 days.";
                                 HelperFunctions.ShowNotification(Add_Your_Pet.this,"Vaccination Notification",go_Message,null);
                             }
                             else if(HelperFunctions.getAge(dog_DOB,"/") == 43){
                                 String go_Message="Your pet '"+na+"', age will be 45 after tne days. We recommended you to give first shot of vaccination after 2 days.";
                                 HelperFunctions.ShowNotification(Add_Your_Pet.this,"Vaccination Notification",go_Message,null);
                             }
                             startActivity(new Intent(getApplicationContext(),Home.class));
                             finish();
                             Toast.makeText(Add_Your_Pet.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                         }
                         else{
                             Toast.makeText(Add_Your_Pet.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                         }
                       }
                       catch (Exception e){
                           Log.e("TAG",e.getMessage());
                           Toast.makeText(Add_Your_Pet.this, "Something went wrong try again!", Toast.LENGTH_SHORT).show();
                       }
                    }
                });

          }
          catch (Exception e){
              Toast.makeText(this, "Something went wrong while getting user data json!", Toast.LENGTH_SHORT).show();
          }
         }
         else{
             Toast.makeText(this, "Fill the form correctly!", Toast.LENGTH_SHORT).show();
         }
        }
        else{
            Toast.makeText(this, "Select Image First!", Toast.LENGTH_SHORT).show();
        }
    }
}