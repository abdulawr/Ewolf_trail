package com.ss_technology.mushinprojectandroidapp.Config;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.util.Base64;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ss_technology.mushinprojectandroidapp.R;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class HelperFunctions {

    public static String currentDate()
    {
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd", Locale.getDefault());
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public static String BitmapToString(Bitmap bi,int quality)
    {
        ByteArrayOutputStream outputStream=new ByteArrayOutputStream();
        bi.compress(Bitmap.CompressFormat.JPEG,quality,outputStream);
        byte[] imageByte=outputStream.toByteArray();
        String encodeimage= Base64.encodeToString(imageByte, Base64.DEFAULT);
        return  encodeimage;

    }

    public static String getCurrentTime()
    {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT-4:00"));
        Date currentLocalTime = cal.getTime();
        DateFormat date = new SimpleDateFormat("KK:mm:ss");
        date.setTimeZone(TimeZone.getTimeZone("GMT-4:00"));
        String localTime = date.format(currentLocalTime);
        return localTime;
    }

    public static  boolean verify(String value){
        return (value.trim().length() > 0 && !value.equals(""));
    }

    public static int getAge(String dobString,String sep){
        int age_year=0,age_month=0,age_day=0;
        String[] dob_arr=dobString.split(sep);
        int dob_year=Integer.parseInt(dob_arr[0]);
        int dob_month=Integer.parseInt(dob_arr[1]);
        int dob_day=Integer.parseInt(dob_arr[2]);

        String[] current_arr=currentDate().split("/");
        int cur_year=Integer.parseInt(current_arr[0]);
        int cur_month=Integer.parseInt(current_arr[1]);
        int cur_day=Integer.parseInt(current_arr[2]);

        // minus dob from the curr date
        if(cur_month > dob_month && cur_day > dob_day){
          age_year=cur_year-dob_year;
          age_month=cur_month-dob_month;
          age_day=cur_day - dob_day;
        }
        else if(cur_day == dob_day && cur_month > dob_month){
          age_day=cur_day-dob_day;
          age_month=cur_month-dob_month;
          age_year=cur_year-dob_year;
        }
        else if(cur_day == dob_day && cur_month < dob_month){
            age_day=cur_day-dob_day;
            cur_month+=12;
            cur_year-=1;
            age_month=cur_month-dob_month;
            age_year=cur_year-dob_year;
        }
        else if(cur_month == dob_month && cur_day < dob_day){
            cur_day+=getMonthlength(cur_month);
            age_day=cur_day-dob_day;
            cur_month-=1;
            if(cur_month > dob_month){
                age_month=cur_month-dob_month;
            }
            else{
                cur_year-=1;
                cur_month+=12;
                age_month=cur_month-dob_month;
            }
            age_year=cur_year-dob_year;

        }
        else if(cur_month == dob_month && cur_day > dob_day){
            age_day=cur_day-dob_day;
            age_month=cur_month-dob_month;
            age_year=cur_year-dob_year;
        }
        else if(cur_month == dob_month && cur_day == dob_day){
            age_year=0;
            age_month=0;
            age_day=0;
        }
        else if(cur_day > dob_day && cur_month < dob_month){
            age_day=cur_day-dob_day;
            age_year=cur_year-dob_year;
            cur_year=cur_year-1;
            cur_month+=12;
            age_month=cur_month-dob_month;
        }
        else if(cur_day < dob_day && cur_month > dob_month){
            cur_day+=getMonthlength(cur_month);
            age_day=cur_day - dob_day;
            cur_month-=1;
            if(cur_month > dob_month){
               age_month=cur_month-dob_month;
            }
            else{
               cur_year-=1;
               cur_month+=12;
               age_month=cur_month-dob_month;
            }
            age_year=cur_year-dob_year;
        }
        else if(cur_month < dob_month && cur_day < dob_day){
           cur_day+=getMonthlength(cur_month);
           age_day=cur_day-dob_day;
           cur_month-=1;
           cur_month+=12;
           age_month=cur_month-dob_month;
           cur_year--;
           age_year=cur_year-dob_year;
        }

        float total= (float) (age_month * 30.417);
        total+=age_day;
        total=Math.round(total);
        int total_age= (int) total;
       return total_age;
    }

    public static int getMonthlength(int index){
        ArrayList<Integer> lis=new ArrayList<>();
        lis.add(31);
        lis.add(28);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        lis.add(30);
        lis.add(31);
        return lis.get(index);
    }

    public static void ShowNotification(Context context,String title,String des, Class names)
    {
        try
        {
            int NOTIFICATION_ID = 234;
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String CHANNEL_ID = "show_camp_request";
                CharSequence name = title;
                String Description = des;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "show_camp_request")
                    .setSmallIcon(R.drawable.not_logo)
                    .setContentTitle(title)
                    .setContentText(des)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.not_logo);
            Bitmap bitmap= BitmapFactory.decodeResource(Resources.getSystem(),R.drawable.logo);
            builder.setLargeIcon(bitmap);

            if(names != null)
            {
                Intent intent = new Intent(context, names);
              /*  intent.putExtra("userID",uID);
                intent.putExtra("driverID",dID);
                intent.putExtra("campID",cID);*/
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                builder.setContentIntent(pendingIntent);
            }

            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
        catch (Exception e)
        {
            Log.e("TAG",e.getMessage());
        }

    }

}
