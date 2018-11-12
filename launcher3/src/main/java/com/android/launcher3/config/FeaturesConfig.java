package com.android.launcher3.config;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * Created by abylee on 2017/12/28.
 */

public class FeaturesConfig {
    public static String channel = "";

    public static boolean sPreseAppFeature = false;

    //桌面忽略文件列表
    //ignore_show_appsps.xml

    public static void initValue(Context context){
        try {
            ApplicationInfo appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            String value = appInfo.metaData.getString("LAUNCHER_CHANNEL");
            channel = value;
            switch (value) {
                case "github":
                    break;
                default:
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //是否支持卸载应用
    public static boolean isSupportUnistApp(){
        if( FeaturesConfig.channel .equals("github") ){
            return false;
        }else{
            return true;
        }
    }

    //是否支持长按：出壁纸选项，小部分件界面
    public static boolean isSupportLongClickEmpty(){
        if( FeaturesConfig.channel .equals("github") ){
            return false;
        }else{
            return true;
        }
    }
}
