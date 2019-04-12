package io.github.takusan23.nicomendroid.Util;

import android.app.Activity;
import android.widget.Toast;

import io.github.takusan23.nicomendroid.LiveIDActivity;

public class UIThreadToast {

    /**
     * ToastをUIスレッド以外でも呼べるようにする
     */
    public static void showUIThreadToast(String text, Activity activity) {
        if (activity!=null){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(activity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
