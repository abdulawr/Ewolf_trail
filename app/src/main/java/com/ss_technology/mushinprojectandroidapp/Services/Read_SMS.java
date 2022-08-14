package com.ss_technology.mushinprojectandroidapp.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.ss_technology.mushinprojectandroidapp.Activities.Chats;
import com.ss_technology.mushinprojectandroidapp.Config.ApiCall;
import com.ss_technology.mushinprojectandroidapp.Config.HelperFunctions;
import com.ss_technology.mushinprojectandroidapp.Config.KeepMeLogin;
import com.ss_technology.mushinprojectandroidapp.Config.VolleyCallback;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

public class Read_SMS extends Service {

    Runnable mHandlerTask;
    Handler handler;
    private final static int INTERVAL = 1000 * 15;

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mHandlerTask = new Runnable()
        {
            @Override
            public void run() {
                KeepMeLogin keepMeLogin=new KeepMeLogin(getApplicationContext());
                ApiCall apiCall=new ApiCall(getApplicationContext());
                try{
                    JSONObject object=new JSONObject(keepMeLogin.getData());
                    HashMap<String,String> mp=new HashMap<>();
                    mp.put("id",object.getString("id"));
                    apiCall.Insert2(mp, "check_Msg.php", new VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {
                            if(result.trim().equals("1")){
                                HelperFunctions.ShowNotification(getApplicationContext(),"You have new messages","Check chat section for more details", Chats.class);
                            }
                        }
                    });
                }
                catch (Exception e){

                }
                handler.postDelayed(mHandlerTask, INTERVAL);
            }
        };
        startRepeatingTask();
        return START_STICKY;
    }

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        handler.removeCallbacks(mHandlerTask);
    }

    @Override
    public void onDestroy() {
        Log.e("TAG","stoppedd");
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
