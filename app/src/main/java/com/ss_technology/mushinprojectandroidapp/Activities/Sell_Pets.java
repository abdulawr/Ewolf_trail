package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.Loading_Dai;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.Container.Train_Gif_Container;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Sell_Pets extends AppCompatActivity {

    DatePicker datepicker;
    ApiCall apiCall;
    Spinner dog_bread_spinner,dog_type_spinners,dog_gender,confirm_breeder_spinner,confirm_stud_spinner;
    ArrayList<String> dog_bread_list;
    HashMap<String,String> dog_bread_map;
    TextInputLayout coat_german_layout,predigree_link_layout;
    TextInputEditText name,age,price,color,predigree_link,coat_german;
    final  int REQUEST_CODE=111;
    final int REQUST_CODE_IMAGE=222;
    Uri imageUri;
    LinearLayout confirm_stud_spinner_layout,confirm_breeder_spinner_layout;
    JSONArray bitmap_json_array=null;
    ArrayList<Bitmap> bitmapList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_pets);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        dog_bread_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
              String dog_br=parent.getItemAtPosition(position).toString();
                if (dog_br.trim().equals("German shepherd") ||
                        dog_br.trim().equals("german shepherd") ||
                        dog_br.trim().equals("German Shepherd") ||
                        dog_br.trim().equals("Germanshepherd") ||
                        dog_br.trim().equals("German_shepherd") ||
                        dog_br.trim().equals("german_shepherd")){
                    coat_german_layout.setVisibility(View.VISIBLE);
                }
                else{
                    coat_german_layout.setVisibility(View.GONE);
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });


        dog_type_spinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String dog_br=parent.getItemAtPosition(position).toString();
                if (dog_br.trim().equals("Pedigree") ||
                        dog_br.trim().equals("pedigree")){
                    predigree_link_layout.setVisibility(View.VISIBLE);
                }
                else{
                    predigree_link_layout.setVisibility(View.GONE);
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

        dog_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                String dog_br=parent.getItemAtPosition(position).toString();
                if (dog_br.trim().equals("Male") ||
                        dog_br.trim().equals("male")){
                    confirm_stud_spinner_layout.setVisibility(View.VISIBLE);
                    confirm_breeder_spinner_layout.setVisibility(View.GONE);
                }
                else{
                    confirm_stud_spinner_layout.setVisibility(View.GONE);
                    confirm_breeder_spinner_layout.setVisibility(View.VISIBLE);
                }

            } // to close the onItemSelected
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });

    }

    private void init(){
        datepicker=findViewById(R.id.datepicker);
        apiCall=new ApiCall(Sell_Pets.this);
        coat_german_layout=findViewById(R.id.coat_german_layout);
        dog_bread_spinner=findViewById(R.id.dog_bread_spinner);
        predigree_link_layout=findViewById(R.id.predigree_link_layout);
        dog_gender=findViewById(R.id.dog_gender);
        bitmapList=new ArrayList<>();
        confirm_breeder_spinner=findViewById(R.id.confirm_breeder_spinner);
        confirm_stud_spinner=findViewById(R.id.confirm_stud_spinner);
        confirm_stud_spinner_layout=findViewById(R.id.confirm_stud_spinner_layout);
        confirm_breeder_spinner_layout=findViewById(R.id.confirm_breeder_spinner_layout);
        dog_type_spinners=findViewById(R.id.dog_type_spinners);
        name=findViewById(R.id.name);
        age=findViewById(R.id.age);
        coat_german=findViewById(R.id.coat_german);
        price=findViewById(R.id.price);
        predigree_link=findViewById(R.id.predigree_link);
        color=findViewById(R.id.color);
        dog_bread_map=new HashMap<>();
        dog_bread_list=new ArrayList<>();

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Sell_Pets.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
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
                 ArrayAdapter<String> ad=new ArrayAdapter<>(Sell_Pets.this, android.R.layout.simple_list_item_1,dog_bread_list);
                 dog_bread_spinner.setAdapter(ad);

                 if (dog_bread_spinner.getSelectedItem().toString().trim().equals("German shepherd") ||
                         dog_bread_spinner.getSelectedItem().toString().trim().equals("german shepherd") ||
                         dog_bread_spinner.getSelectedItem().toString().trim().equals("German Shepherd") ||
                         dog_bread_spinner.getSelectedItem().toString().trim().equals("Germanshepherd") ||
                         dog_bread_spinner.getSelectedItem().toString().trim().equals("German_shepherd") ||
                         dog_bread_spinner.getSelectedItem().toString().trim().equals("german_shepherd")){
                     coat_german_layout.setVisibility(View.VISIBLE);
                 }
                 else{
                     coat_german_layout.setVisibility(View.GONE);
                 }
             }
             catch (Exception e){
                 Toast.makeText(Sell_Pets.this, "Something went wrong in downloading dog bread information from server try again!", Toast.LENGTH_SHORT).show();
             }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    public void Submit_Dog_Details(View view) {
        Date d=new Date();
        d.setDate(datepicker.getDayOfMonth());
        d.setMonth(datepicker.getMonth());
        d.setYear(datepicker.getYear()-1900);
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

        String dog_DOB = df.format(d);
        String dog_bread_type=dog_bread_map.get(dog_bread_spinner.getSelectedItem());
        String dog_name=name.getText().toString();
        String dog_age=age.getText().toString();
        String dog_price=price.getText().toString();
        String dog_color=color.getText().toString();
        String dog_gen=dog_gender.getSelectedItem().toString();
        String dog_type=dog_type_spinners.getSelectedItem().toString();
        String predigree_li=predigree_link.getText().toString();
        String coat=coat_german.getText().toString();
        String conform_stud=confirm_stud_spinner.getSelectedItem().toString();
        String conform_breeder=confirm_breeder_spinner.getSelectedItem().toString();
        boolean check=true;

        if(HelperFunctions.verify(dog_DOB) && HelperFunctions.verify(dog_bread_type) &&
           HelperFunctions.verify(dog_name) && HelperFunctions.verify(dog_age) &&
           HelperFunctions.verify(dog_price) && HelperFunctions.verify(dog_color) &&
           HelperFunctions.verify(dog_gen)){

            if (dog_gen.trim().equals("Male")){
                conform_breeder="none";
            }
            if (dog_gen.trim().equals("Female")){
                conform_stud="none";
            }

            if (dog_type.trim().equals("Pedigree") ||
                    dog_type.trim().equals("pedigree")){
                if (HelperFunctions.verify(predigree_li)){
                    check=true;
                }
                else{
                    check=false;
                    Toast.makeText(this, "Enter Value Pedigree Link", Toast.LENGTH_SHORT).show();
                }
            }

            if (dog_bread_spinner.getSelectedItem().toString().trim().equals("German shepherd") ||
                    dog_bread_spinner.getSelectedItem().toString().trim().equals("german shepherd") ||
                    dog_bread_spinner.getSelectedItem().toString().trim().equals("German Shepherd") ||
                    dog_bread_spinner.getSelectedItem().toString().trim().equals("Germanshepherd") ||
                    dog_bread_spinner.getSelectedItem().toString().trim().equals("German_shepherd") ||
                    dog_bread_spinner.getSelectedItem().toString().trim().equals("german_shepherd")){

                if (HelperFunctions.verify(coat)){
                 check=true;
                }
                else {
                    check=false;
                    Toast.makeText(this, "Enter value for German Shepherd coat", Toast.LENGTH_SHORT).show();
                }

            }

            if(!bitmapList.isEmpty()){
                check=true;
            }
            else{
                check=false;
                Toast.makeText(this, "Select Images First!", Toast.LENGTH_SHORT).show();
            }

            // When every thing goes well
            if (check){
                HashMap<String,String> mp=new HashMap<>();
                mp.put("images",bitmap_json_array.toString());
                mp.put("dob",dog_DOB);
                mp.put("dog_bread",dog_bread_type);
                mp.put("name",dog_name);
                mp.put("age",dog_age);
                mp.put("price",dog_price);
                mp.put("color",dog_color);
                mp.put("gender",dog_gen);
                mp.put("dog_type",dog_type);
                mp.put("predigree_li",predigree_li);
                mp.put("conform_stud",conform_stud);
                mp.put("conform_breeder",conform_breeder);
                mp.put("coat",coat);
                KeepMeLogin keepMeLogin=new KeepMeLogin(this);
                try{
                    JSONObject object=new JSONObject(keepMeLogin.getData());
                    mp.put("user_id",object.getString("id"));

                    apiCall.Insert(mp, "Sell_Pets.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                           try{
                               JSONObject object=new JSONObject(result);
                               if (object.getString("status").trim().equals("1")){
                                   startActivity(new Intent(getApplicationContext(),Home.class));
                                   finish();
                                   Toast.makeText(Sell_Pets.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                               }
                               else{
                                   Toast.makeText(Sell_Pets.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                               }
                           }
                           catch (Exception e){
                               Toast.makeText(Sell_Pets.this, "Error occurred in json parsing", Toast.LENGTH_SHORT).show();
                           }
                        }
                    });
                }
                catch (Exception e){
                    Toast.makeText(this, "Error occured while fetching user information from json.", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else {
            Toast.makeText(this, "Fill the values correctly!", Toast.LENGTH_SHORT).show();
        }
    }

    public void Choose_Images(View view) {
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Sell_Pets.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }
        else
        {
            SelectImage();
        }
    }

    public void SelectImage()
    {
        Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
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
                ActivityCompat.requestPermissions(Sell_Pets.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
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
                try{
                    if(!bitmapList.isEmpty())
                    {
                        bitmapList.clear();
                    }

                    if(data.getClipData() == null)
                    {
                        Uri u=data.getData();
                        InputStream stream=getApplicationContext().getContentResolver().openInputStream(u);
                        Bitmap bitmap= BitmapFactory.decodeStream(stream);
                        bitmapList.add(bitmap);
                    }
                    else {
                        int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                        for(int i = 0; i < count; i++)
                        {
                            imageUri = data.getClipData().getItemAt(i).getUri();
                            InputStream stream=getApplicationContext().getContentResolver().openInputStream(imageUri);
                            Bitmap bitmap= BitmapFactory.decodeStream(stream);
                            bitmapList.add(bitmap);
                        }
                    }
                    Loading_Dai loading_dai=new Loading_Dai(Sell_Pets.this);
                    loading_dai.Show();
                    bitmap_json_array=new JSONArray();
                    for (Bitmap bi : bitmapList){
                        JSONObject item=new JSONObject();
                        item.put("url", HelperFunctions.BitmapToString(bi,80));
                        bitmap_json_array.put(item);
                    }
                    loading_dai.Hide();
                }
                catch (Exception e)
                {
                    // Log.e("TAG",e.getMessage());
                    Toast.makeText(this, "Error occurred select images again", Toast.LENGTH_SHORT).show();
                }

                // Log.e("TAG",bitmapJsonObject.toString());
            }
        }

    }

}