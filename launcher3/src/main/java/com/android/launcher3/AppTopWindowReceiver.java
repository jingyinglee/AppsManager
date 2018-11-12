package com.android.launcher3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


/**
 * Created by abylee on 2017/12/19.
 */

public class AppTopWindowReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //获取前台应用的包名（模拟器那边定的）
        Bundle bundle = intent.getExtras();
        String pkgName = bundle.getString("mFocusedWindow");

        //...
    }
}
