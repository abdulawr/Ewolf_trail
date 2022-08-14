package com.ss_technology.mushinprojectandroidapp.Services;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import androidx.annotation.Nullable;

import com.ss_technology.mushinprojectandroidapp.Activities.Your_Pets_List;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class SendingAppoimentMessage extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ApiCall apiCall=new ApiCall(getApplicationContext());
        KeepMeLogin keepMeLogin=new KeepMeLogin(getApplicationContext());
        try{
            JSONObject object=new JSONObject(keepMeLogin.getData());
            HashMap<String,String> mp=new HashMap<>();
            mp.put("id",object.getString("id"));
            apiCall.Insert(mp, "getNotificationList.php", new VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                 try {
                     JSONArray array=new JSONArray(result);
                     for(int i=0; i<array.length(); i++){
                         JSONObject pet=array.getJSONObject(i);
                         int age_day= HelperFunctions.getAge(pet.getString("dob"),"-");
                         String title="Vaccination Notification";
                         String message="Your pet '"+pet.getString("name")+"' born on "+pet.getString("dob")+", ";
                         if (age_day == 43){
                             message+="It is to inform you that after 2 day, It time for the first shot of vaccination";
                          HelperFunctions.ShowNotification(getApplicationContext(),title,message, Your_Pets_List.class);
                         }else if(age_day == 64){
                             message+="It is to inform you that after 2 day, It time for the second shot of vaccination";
                             HelperFunctions.ShowNotification(getApplicationContext(),title,message, Your_Pets_List.class);
                         }else if(age_day == 82){
                             message+="It is to inform you that after 2 day, It time for the third shot of vaccination";
                             HelperFunctions.ShowNotification(getApplicationContext(),title,message, Your_Pets_List.class);
                         }else if(age_day == 100){
                             message+="It is to inform you that after 2 day, It time for the last shot of vaccination";
                             HelperFunctions.ShowNotification(getApplicationContext(),title,message, Your_Pets_List.class);
                             HashMap<String,String> ms=new HashMap<>();
                             ms.put("id",pet.getString("id"));
                             apiCall.Insert(ms, "ChangeVacinationStatus.php", new VolleyCallback() {
                                 @Override
                                 public void onSuccess(String result) {
                                 }
                             });
                         }

                     }
                 }
                 catch (Exception e){
                  Log.e("TAG",e.getMessage());
                 }
                }
            });
        }
        catch (Exception e){
            Log.e("TAG",e.getMessage());
        }
        stopService(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
