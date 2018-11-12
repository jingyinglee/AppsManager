package com.android.launcher3.widget;

import android.os.CountDownTimer;

/**
 * Created by wzz on 2017/8/28.
 * 定时器控件
 */
public class Timercontrol {
    //定义一个定时器
    private CountDownTimer mTimer;

    private static class LazyHolder {
        private static final Timercontrol INSTANCE = new Timercontrol();
    }

    //单例模式
    public static Timercontrol getInstance() {
        return LazyHolder.INSTANCE;
    }

    public Timercontrol() {
        //以毫秒为单位，第一个参数是指从开始调用start()方法到倒计时完成的时候onFinish()方法被调用这段时间的毫秒数，也就是倒计时总的时间；
        // 第二个参数表示间隔多少毫秒调用一次 onTick方法，例如间隔1000毫秒。
        mTimer = new CountDownTimer(30000, 30000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //ToastUtil.showToast(BaseApplication.getInstance(),"倒计时30秒后关闭VPN");
            }

            @Override
            public void onFinish() {

            }
        };
    }
    /**获取当前定时器对象*/
    public CountDownTimer getTimer() {
        return mTimer;
    }
}
