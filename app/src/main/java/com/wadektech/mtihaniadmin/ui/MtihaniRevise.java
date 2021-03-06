package com.wadektech.mtihaniadmin.ui;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import timber.log.Timber;

public class MtihaniRevise extends Application {
    private static MtihaniRevise mRevise;
    @Override
    public void onCreate() {
        super.onCreate ();

        mRevise = this ;

        Timber.plant(new Timber.DebugTree());

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder (this);
        builder.downloader (new OkHttpDownloader (this,Integer.MAX_VALUE));
        Picasso built = builder.build ();
        built.setIndicatorsEnabled (true);
        built.setLoggingEnabled (true);
        Picasso.setSingletonInstance (built);
    }

    public static MtihaniRevise getApp() {
        return mRevise;
    }
}
