package com.example.william.notifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MyJobService extends JobService{


    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d("cal", "onStartJob: "+job.getTag());



        jobFinished(job,false);
        return false;
    }


    @Override
    public boolean onStopJob(JobParameters job) {
        Log.d("cal", "onStopJob: "+job.getTag());

        return false;
    }
}
