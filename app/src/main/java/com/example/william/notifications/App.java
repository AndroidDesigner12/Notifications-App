package com.example.william.notifications;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by william on 3/4/18.
 */

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();


        Stetho.initializeWithDefaults(this);


    }


}
