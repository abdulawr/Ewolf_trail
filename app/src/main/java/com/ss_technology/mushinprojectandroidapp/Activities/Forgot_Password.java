package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONObject;

import java.util.HashMap;

public class Forgot_Password extends AppCompatActivity {

    Button sendcodeBtn,ver_sendcodeBtn,new_password_btn;
    TextInputEditText email;
    TextInputEditText verf_code,newPassword;
    ApiCall apiCall;
    String userID,code;
    LinearLayout forgot_layout,verification_layout,new_password_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Forgot Password");

        sendcodeBtn = findViewById(R.id.sendcodeBtn);
        new_password_btn = findViewById(R.id.new_password_btn);
        newPassword = findViewById(R.id.newPassword);
        new_password_layout = findViewById(R.id.new_password_layout);
        email = findViewById(R.id.email);
        ver_sendcodeBtn = findViewById(R.id.ver_sendcodeBtn);
        verf_code = findViewById(R.id.verf_code);
        forgot_layout = findViewById(R.id.forgot_layout);
        verification_layout = findViewById(R.id.verification_layout);
        apiCall = new ApiCall(this);

        sendcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String em = email.getText().toString();

                if(HelperFunctions.verify(em)){
                    HashMap<String,String> mp = new HashMap<>();
                    mp.put("email",em);
                    mp.put("type","sendVerificationCode");
                    apiCall.Insert(mp, "forgotPass.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            try {
                                JSONObject object = new JSONObject(result);
                                if(object.getString("status").trim().equals("1")){
                                   email.getText().clear();
                                   userID = object.getString("userID");
                                   code = object.getString("code");
                                   forgot_layout.setVisibility(View.GONE);
                                   verification_layout.setVisibility(View.VISIBLE);
                                }
                                Toast.makeText(Forgot_Password.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){
                                Toast.makeText(Forgot_Password.this, "Something went wrong try again", Toast.LENGTH_SHORT).show();
                                Log.e("Basit",e.getMessage());
                            }

                        }
                    });
                }
                else{
                    Toast.makeText(Forgot_Password.this, "Enter email first", Toast.LENGTH_SHORT).show();
                }

            }
        });


        ver_sendcodeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otp = verf_code.getText().toString();
                if(HelperFunctions.verify(otp)){
                  if(otp.trim().equals(code)){
                      new_password_layout.setVisibility(View.VISIBLE);
                      verification_layout.setVisibility(View.GONE);
                  }
                  else{
                      Toast.makeText(Forgot_Password.this, "Invalid OTP", Toast.LENGTH_SHORT).show();
                  }
                }
                else{
                    Toast.makeText(Forgot_Password.this, "Enter OTP first", Toast.LENGTH_SHORT).show();
                }
            }
        });

        new_password_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass = newPassword.getText().toString();
                if(HelperFunctions.verify(pass)){
                    HashMap<String,String> mp = new HashMap<>();
                    mp.put("password",pass);
                    mp.put("type","changePassword");
                    mp.put("userID",userID);
                    apiCall.Insert(mp, "forgotPass.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                          try {
                             JSONObject ob = new JSONObject(result);
                             if(ob.getString("status").trim().equals("1")){
                                 startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                 finish();
                             }
                              Toast.makeText(Forgot_Password.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                          }
                          catch (Exception e){
                              Toast.makeText(Forgot_Password.this, "Error occurred in json parsing", Toast.LENGTH_SHORT).show();
                          }
                        }
                    });
                }
                else{
                    Toast.makeText(Forgot_Password.this, "Enter password first", Toast.LENGTH_SHORT).show();
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
}