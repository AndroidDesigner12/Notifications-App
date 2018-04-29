package com.example.william.notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.william.notifications.Model.Sch;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.Trigger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


   private Button notify,notifyOne,notifyTwo;
   private SwitchCompat setAlarm;
   private NotificationManager manager;
   private int ids = 0;
   private String endDate = "03:25";
   private String startDate = "07:38";
   private String newDate = "07:40";

   private RadioButton five,ten,fifteen;
   private int beforMinute = 2;
   private FirebaseJobDispatcher firebaseJobDispatcher;

   private ArrayList<Sch> arrayList = new ArrayList<>();


   public int getHour(String date){
       final int hour = Integer.parseInt(date.substring(0,2));
    return hour;
   }

   public int getMin(String date){
       final int minute = Integer.parseInt(date.substring(3,5));
       return minute;
   }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        five = findViewById(R.id.five);
        ten = findViewById(R.id.ten);
        fifteen = findViewById(R.id.fifteen);

        notify = findViewById(R.id.notify);
        notifyOne = findViewById(R.id.notify_one);
        notifyTwo = findViewById(R.id.notify_two);
        setAlarm = findViewById(R.id.set_alarm);

        final int periodicity = (int) TimeUnit.HOURS.toSeconds(12); // Every 12 hours periodicity expressed as seconds
        final int toleranceInterval = (int)TimeUnit.HOURS.toSeconds(1); // a small(ish) window of time when triggering is OK


        firebaseJobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(getApplicationContext()));
        Job myJob = firebaseJobDispatcher.newJobBuilder()
                .setService(MyJobService.class)
                .setTag("Hello")
                .setConstraints(Constraint.ON_ANY_NETWORK)
                .setTrigger(Trigger.executionWindow(periodicity,periodicity+toleranceInterval))
                .setLifetime(Lifetime.FOREVER)
                .setRecurring(true)
                .setReplaceCurrent(true)
                .build();

        firebaseJobDispatcher.mustSchedule(myJob);


        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEEE",Locale.US);
        String day = simpleDateFormat.format(calendar.getTime());

       // updateScheduleTable(day,"05:08");
        arrayList = getSchedules(day);
        Log.d("cal", "onCreate: "+arrayList.get(0).getStart_date());



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel =
                    new NotificationChannel("channel", "name", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.enableVibration(true);
            notificationChannel.setLightColor(Color.GRAY);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            getManager().createNotificationChannel(notificationChannel);

        }else {

        }


        notify.setOnClickListener(this);

        setAlarm.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){






                    for (int i=0;i<arrayList.size();i++){

                        setAlarms(getHour(arrayList.get(i).getStart_date()),getMin(arrayList.get(i).getStart_date()));

                    }

                    Toast.makeText(getApplicationContext(),"notification Enabled",Toast.LENGTH_SHORT).show();
                }else {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    Intent intent = new Intent(getApplicationContext(),Alarm.class);
                    final int _id = (int) System.currentTimeMillis();
                    PendingIntent pendingIntent = PendingIntent.
                            getBroadcast(getApplicationContext(),_id,intent,PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(pendingIntent);
                    Toast.makeText(getApplicationContext(),"Notification Disabled",Toast.LENGTH_SHORT).show();
                }
            }
        });



        notifyOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "BC App";
                String text = "Your Class is Starting soon";

                Intent intent = new Intent(MainActivity.this,Nav.class);


                NotificationCompat.Builder builder = initNotificBuilder(title,text,intent);
                getManager().notify(2,builder.build());

            }
        });
        notifyTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++ids;
                String title = "BC App";
                String text = "Save Your Attendance";

                Intent intent = new Intent(MainActivity.this,Nav.class);


                NotificationCompat.Builder builder = initNotificBuilder(title,text,intent);
                getManager().notify(3,builder.build());

            }
        });

    }


    public ArrayList<Sch>  getSchedules(String day){
       ArrayList<Sch> arrayList = new ArrayList<>();

        String date = new StringBuilder()
                .append("\'")
                .append(day)
                .append("\'")
                .toString();

       String rawQuery = "Select start_date,days\n" +
               "   \t\tFrom schedules\n" +
               "   \t\twhere days = "+date;

       DbHelper dbHelper = new DbHelper(this);
       SQLiteDatabase sqLiteDatabase = dbHelper.getReadableDatabase();
       Cursor cursor = sqLiteDatabase.rawQuery(rawQuery,null);

        if (cursor != null) {
            String days = "", start_date = "";

            while (cursor.moveToNext()) {
                days = cursor.getString(cursor.getColumnIndex(DbContract.ScheduleEntry.DAYS));
                start_date = cursor.getString(cursor.getColumnIndex(DbContract.ScheduleEntry.START_DATE));

                Sch schedules = new Sch(start_date, day);
                arrayList.add(schedules);
            }



        }

        return arrayList;
    }


    public void updateScheduleTable(String day,String newDate){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DbContract.ScheduleEntry.START_DATE,newDate);

        String [] whereArgs = {day};


        getContentResolver().update(DbContract.ScheduleEntry.SCHEDULE_URI,contentValues,
                DbContract.ScheduleEntry.DAYS +" =?",whereArgs);

    }

    public void setAlarms(int hour, int min) {
        TimeZone timeZone = TimeZone.getTimeZone("GMT+05:45");
        Calendar calendar = Calendar.getInstance(timeZone);
        calendar.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        calendar.set(Calendar.HOUR, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        calendar.add(Calendar.MINUTE, -beforMinute);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Log.d("cal", "setAlarms: " + calendar.getTime());
        Intent intent = new Intent(this, Alarm.class);
        final int _id = (int) System.currentTimeMillis();
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(this, _id, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        if (calendar.getTimeInMillis() - System.currentTimeMillis() < 0) {
            //if its in past, add one day
            Log.d("cal", "setAlarms: " + String.valueOf(calendar.getTimeInMillis() - System.currentTimeMillis()));
        } else {



             }
    }



    public void setAlarams(long time){
        Log.d("call", "setAlarams: "+SystemClock.elapsedRealtime());
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this,Alarm.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(this,1001,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                time,pendingIntent);

    }

    private NotificationCompat.Builder initNotificBuilder(String title, String text, Intent intent) {
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,"channel")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle(title)
                .setVibrate(new long[]{500,1000,1000,1000})
                .setSound(alarmSound)
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setAutoCancel(true)
                .setOngoing(true)
                .setContentText(text);

        if (intent != null){
            PendingIntent pendingIntent =  PendingIntent.getActivity(this,0,intent,0);
            builder.setContentIntent(pendingIntent);
        }

        return builder;
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.five:
                if (checked)
                    beforMinute = 5;
                    Toast.makeText(MainActivity.this,five.getText(),Toast.LENGTH_SHORT).show();

                break;

            case R.id.ten:
                if (checked)
                    beforMinute = 10;
                    Toast.makeText(MainActivity.this,ten.getText(),Toast.LENGTH_SHORT).show();

                break;

            case R.id.fifteen:
                if (checked)
                    beforMinute = 15;
                    Toast.makeText(MainActivity.this,fifteen.getText(),Toast.LENGTH_SHORT).show();

                break;
        }
    }

    @Override
    public void onClick(View v) {

        String title = "BC App";
        String text = "Your Class is over. Please give your feedback";

        Intent intent = new Intent(this,Nav.class);

        NotificationCompat.Builder builder = initNotificBuilder(title,text,intent);
        getManager().notify(1,builder.build());

    }

    public NotificationManager getManager() {

        if (manager == null)
            manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        return manager;

    }
}
