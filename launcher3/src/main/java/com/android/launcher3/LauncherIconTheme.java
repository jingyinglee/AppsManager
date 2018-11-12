package com.android.launcher3;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

/**
 * Created by abylee on 2018/1/12.
 */

public final class LauncherIconTheme {
    //下载
    private static int DOWNLOAD = R.drawable.ic_allapps_pressed;
    //google地图
    private static int GOOGLE_MAPS = R.drawable.ic_allapps_pressed;
    //自定义的应用资源id

    private static String TAG = "LauncherIconTheme";

    //根据包名、类名获取Bitmap
    public static Bitmap getIconBitmap(Context context, String packageName, String className) {
        Resources resources = context.getResources();
        int iconId = getIconId(packageName, className);
        if (iconId != -1) {
            return BitmapFactory.decodeResource(resources, iconId);
        }
        return null;
    }

    //根据包名、类名获取Drawable   要用到的就是这个方法
    public static Drawable getIconDrawable(Context context, String packageName, String className) {
        Resources resources = context.getResources();
        int iconId = getIconId(packageName, className);
        if (iconId != -1) {
            return resources.getDrawable(iconId);
        }
        return null;
    }

    //根据包名、类名获取资源定义的图标资源id
    private static int getIconId(String packageName, String className) {
//        if ("com.android.providers.downloads.ui".equals(packageName)
//                && "com.android.providers.downloads.ui.DownloadList".equals(className)) {
//            return DOWNLOAD;
//
//        } else if ("com.google.android.apps.maps".equals(packageName)
//                && "com.google.android.maps.MapsActivity".equals(className)) {
//            return GOOGLE_MAPS;
//
//        } else{
//            return -1;
//        }
        return -1;
    }
}