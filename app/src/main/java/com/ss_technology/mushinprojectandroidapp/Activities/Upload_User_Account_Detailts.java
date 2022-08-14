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
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthCredential;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class Upload_User_Account_Detailts extends AppCompatActivity {

    TextInputEditText name,mobile,email,add,pass;
    Button choose_image,signUp;
    ApiCall apiCall;
    Bitmap bitmap=null;
    final  int REQUEST_CODE=111;
    final int REQUST_CODE_IMAGE=222;
    ImageView profileimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_upload__user__account__detailts);

        init();

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(Upload_User_Account_Detailts.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
        }

        choose_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Upload_User_Account_Detailts.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                }
                else
                {
                    SelectImage();
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nn=name.getText().toString();
                String mo=mobile.getText().toString();
                String em=email.getText().toString();
                String ad=add.getText().toString();
                String pa=pass.getText().toString();
                if (HelperFunctions.verify(nn) && HelperFunctions.verify(mo) && HelperFunctions.verify(em)
                        && HelperFunctions.verify(ad) && HelperFunctions.verify(pa)){
                    if (bitmap != null){
                        HashMap<String,String> map=new HashMap<>();
                        map.put("name",nn);
                        map.put("mobile",mo);
                        map.put("email",em);
                        map.put("add",ad);
                        map.put("pass",pa);
                        map.put("image",HelperFunctions.BitmapToString(bitmap,90));
                        apiCall.Insert(map, "registerUser.php", new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                Log.e("Basit",result);
                                try{
                                    JSONObject object=new JSONObject(result);
                                    if (object.getString("status").trim().equals("1")){
                                       startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                       finish();
                                        Toast.makeText(Upload_User_Account_Detailts.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Toast.makeText(Upload_User_Account_Detailts.this, object.getString("message"), Toast.LENGTH_SHORT).show();
                                    }
                                }
                                catch (Exception exception){
                                    Log.e("Basit",exception.getMessage());
                                    Toast.makeText(Upload_User_Account_Detailts.this, "Error occurred in Json Parsing!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else{
                        Toast.makeText(Upload_User_Account_Detailts.this, "Select Image first!", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(Upload_User_Account_Detailts.this, "Fill the form correctly!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void SelectImage()
    {
        profileimage.setVisibility(View.GONE);
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
                ActivityCompat.requestPermissions(Upload_User_Account_Detailts.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
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
                        profileimage.setVisibility(View.VISIBLE);
                        InputStream stream=getApplicationContext().getContentResolver().openInputStream(uri);
                        Bitmap original = BitmapFactory.decodeStream(stream);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        original.compress(Bitmap.CompressFormat.PNG, 80, out);
                        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        bitmap=Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                        profileimage.setImageBitmap(original);
                    } catch (Exception e) {
                        Toast.makeText(this, "Some thing went wrong try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }


    private void init(){
        name=findViewById(R.id.name);
        mobile=findViewById(R.id.mobile);
        email=findViewById(R.id.email);
        add=findViewById(R.id.add);
        profileimage=findViewById(R.id.profileimage);
        pass=findViewById(R.id.pass);
        choose_image=findViewById(R.id.choose_image);
        signUp=findViewById(R.id.signUp);
        apiCall=new ApiCall(this);

        try{
            if (!getIntent().getStringExtra("mobile").trim().equals("")){
                mobile.setText(getIntent().getStringExtra("mobile"));
                mobile.setEnabled(false);
                email.setEnabled(true);
            }
        }
        catch (Exception e){
        }

        try {
            if (!getIntent().getStringExtra("email").trim().equals("")){
                name.setText(getIntent().getStringExtra("name"));
                email.setText(getIntent().getStringExtra("email"));
                email.setEnabled(false);
                mobile.setEnabled(true);

            }
        }
        catch (Exception e){
        }

    }
}