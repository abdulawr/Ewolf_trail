package com.ss_technology.mushinprojectandroidapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.BaseURL;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONObject;

import java.util.HashMap;

public class Profile extends AppCompatActivity {

    ImageView profileImage;
    EditText name,email,mobile,address,password;
    Button update;
    ApiCall apiCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        init();

        KeepMeLogin keepMeLogin=new KeepMeLogin(getApplicationContext());
        try {
            JSONObject object=new JSONObject(keepMeLogin.getData());
            name.setText(object.getString("name"));
            email.setText(object.getString("email"));
            address.setText(object.getString("adds"));
            password.setText(object.getString("pass"));
            mobile.setText(object.getString("mobile"));

            Picasso.get().load(BaseURL.userImage()+object.getString("image")).networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE).into(profileImage);

            HashMap<String,String> map=new HashMap<>();
            map.put("id",object.getString("id"));
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(HelperFunctions.verify(email.getText().toString()) && HelperFunctions.verify(password.getText().toString())){
                        map.put("name",name.getText().toString());
                        map.put("email",email.getText().toString());
                        map.put("mobile",mobile.getText().toString());
                        map.put("password",password.getText().toString());
                        map.put("address",address.getText().toString());

                        apiCall.Insert(map, "updateProfile.php", new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {

                                try{
                                    JSONObject ob=new JSONObject(result);
                                    if (ob.getString("status").trim().equals("1")){
                                        JSONObject userOBj=ob.getJSONObject("data");
                                        keepMeLogin.setData(userOBj.toString());
                                        name.setText(userOBj.getString("name"));
                                        email.setText(userOBj.getString("email"));
                                        address.setText(userOBj.getString("adds"));
                                        password.setText(userOBj.getString("pass"));
                                        mobile.setText(userOBj.getString("mobile"));
                                        Toast.makeText(Profile.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        Toast.makeText(Profile.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception e){
                                    Log.e("TAG",e.getMessage());
                                    Toast.makeText(Profile.this, "Error occured in Json parsing", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                    else{
                        Toast.makeText(Profile.this, "Fill the form correctly!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            finish();
        return true;
    }

  private  void  init(){
      profileImage=findViewById(R.id.profileImage);
      name=findViewById(R.id.name);
      email=findViewById(R.id.email);
      password=findViewById(R.id.password);
      address=findViewById(R.id.address);
      mobile=findViewById(R.id.mobile);
      update=findViewById(R.id.update);
      apiCall=new ApiCall(getApplicationContext());
  }

    public void updateProfileImage(View view) {
     startActivity(new Intent(getApplicationContext(),Update_Profile_Image.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        super.recreate();
    }
}