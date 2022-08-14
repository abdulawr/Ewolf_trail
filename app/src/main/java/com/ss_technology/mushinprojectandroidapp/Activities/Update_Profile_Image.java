package com.ss_technology.mushinprojectandroidapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;
import com.ss_technology.mushinprojectandroidapp.R;

import org.json.JSONObject;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class Update_Profile_Image extends AppCompatActivity {

    ImageView image;
    Button upload;
    final  int REQUEST_CODE=111;
    final int REQUST_CODE_IMAGE=222;
    Bitmap bitmap=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_profile_image);

        image=findViewById(R.id.image);
        upload=findViewById(R.id.upload);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED)
                {
                    ActivityCompat.requestPermissions(Update_Profile_Image.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
                }
                else
                {
                    SelectImage();
                }
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    KeepMeLogin keepMeLogin=new KeepMeLogin(getApplicationContext());
                    try{
                        JSONObject object=new JSONObject(keepMeLogin.getData());
                        HashMap<String,String> map=new HashMap<>();
                        map.put("id",object.getString("id"));
                        map.put("imagePath",object.getString("image"));
                        map.put("newImage", HelperFunctions.BitmapToString(bitmap,90));
                        ApiCall apiCall=new ApiCall(getApplicationContext());
                        apiCall.Insert(map, "updateProfileImage.php", new VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                             try {
                                JSONObject ob=new JSONObject(result);
                                if (ob.getString("status").trim().equals("1")){
                                    Toast.makeText(Update_Profile_Image.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(Update_Profile_Image.this, ob.getString("message"), Toast.LENGTH_SHORT).show();
                                }
                             }
                             catch (Exception e)
                             {
                                 Toast.makeText(Update_Profile_Image.this, "Error occured in Json parsing", Toast.LENGTH_SHORT).show();
                             }
                            }
                        });
                        ;                }
                    catch (Exception e){
                        Toast.makeText(Update_Profile_Image.this, "Error occured in Json parsing", Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e){
                    Toast.makeText(Update_Profile_Image.this, "Select Image First", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                SelectImage();
            }
            else {
                ActivityCompat.requestPermissions(Update_Profile_Image.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE);
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
                        image.setImageBitmap(original);
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        original.compress(Bitmap.CompressFormat.PNG, 90, out);
                        bitmap = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                        bitmap=Bitmap.createScaledBitmap(bitmap, 300, 300, false);
                    } catch (Exception e) {
                        Toast.makeText(this, "Some thing went wrong try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }

    }
}