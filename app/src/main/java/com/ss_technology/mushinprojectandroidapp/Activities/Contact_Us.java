package com.ss_technology.mushinprojectandroidapp.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONObject;

public class Contact_Us extends AppCompatActivity {

    TextView name,address,mobile,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact__us);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);

        ApiCall apiCall=new ApiCall(getApplicationContext());
        apiCall.get("getContact.php", new VolleyCallback() {
            @Override
            public void onSuccess(String result) {
            try {
                JSONObject object=new JSONObject(result);
                name.setText(object.getString("name"));
                address.setText(object.getString("address"));
                mobile.setText(object.getString("mobile"));
                email.setText(object.getString("email"));


            }
            catch (Exception e){
                Toast.makeText(Contact_Us.this, "Error occured in Json parsing", Toast.LENGTH_SHORT).show();
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