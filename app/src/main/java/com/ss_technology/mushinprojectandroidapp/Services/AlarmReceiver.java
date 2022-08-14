package com.ss_technology.mushinprojectandroidapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intents=new Intent(context,SendingAppoimentMessage.class);
        context.startService(intents);
    }
}
