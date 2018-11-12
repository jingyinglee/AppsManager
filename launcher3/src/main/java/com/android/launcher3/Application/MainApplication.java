package com.android.launcher3.Application;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.liulishuo.filedownloader.FileDownloader;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by abylee on 2017/12/14.
 */

public class MainApplication extends Application {

    private static MainApplication ourInstance = new MainApplication();
    private static Context mContext;

    public static MainApplication getInstance() {
        return ourInstance;
    }

    public static Context getContext() {
        return mContext;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ourInstance = this;
        mContext = getApplicationContext();

        Utils.init(this);

        FileDownloader.setup(mContext);

        Logger.addLogAdapter(new DiskLogAdapter());
    }
}