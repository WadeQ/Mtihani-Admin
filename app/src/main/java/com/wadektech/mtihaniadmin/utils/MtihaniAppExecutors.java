package com.wadektech.mtihaniadmin.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Dennis Tsuma on 3/11/18.
 */

public class MtihaniAppExecutors {
    private static final Object LOCK = new Object();
    private static MtihaniAppExecutors sInstance;
    private final Executor diskIO;


    public MtihaniAppExecutors(Executor diskIO) {
        this.diskIO = diskIO;

    }

    public static MtihaniAppExecutors getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new MtihaniAppExecutors(Executors.newSingleThreadExecutor());
                }
            }
        }
        return sInstance;
    }

    public Executor getDiskIO() {
        return diskIO;
    }

}
