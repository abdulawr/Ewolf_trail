package com.ss_technology.mushinprojectandroidapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.material.textfield.TextInputEditText;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONObject;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    TextInputEditText email,pass;
    ApiCall apiCall;
    KeepMeLogin keepMeLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        email=findViewById(R.id.email);
        pass=findViewById(R.id.pass);
        apiCall=new ApiCall(this);
        keepMeLogin=new KeepMeLogin(this);

    }

    public void SignUp(View view) {
        startActivity(new Intent(getApplicationContext(),SignUp.class));
    }

    public void Forgot_Password(View view) {
        startActivity(new Intent(getApplicationContext(),Forgot_Password.class));
    }


    public void Login(View view) {
        String em=email.getText().toString();
        String pa=pass.getText().toString();
        if (HelperFunctions.verify(em) && HelperFunctions.verify(pa)){
            HashMap<String,String> map=new HashMap<>();
            map.put("email",em);
            map.put("pass",pa);
            apiCall.Insert(map, "userLogin.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                   try {
                       JSONObject object=new JSONObject(result);
                       if(object.getString("status").trim().equals("1")){
                           JSONObject data=object.getJSONObject("data");
                           keepMeLogin.setData(data.toString());
                           startActivity(new Intent(getApplicationContext(),Main_Home.class));
                           finish();
                       }
                       else {
                           Toast.makeText(MainActivity.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                       }
                   }
                   catch (Exception e){
                       Toast.makeText(MainActivity.this, "Error occurred in Json Parsing!", Toast.LENGTH_SHORT).show();
                   }
                }
            });
        }
        else {
            Toast.makeText(this, "Fill the values correctly!", Toast.LENGTH_SHORT).show();
        }
    }
}