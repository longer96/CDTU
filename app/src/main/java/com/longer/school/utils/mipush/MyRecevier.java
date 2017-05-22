package com.longer.school.utils.mipush;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

/**
 * Created by Axu on 2016/9/30.
 */

public class MyRecevier extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("收到广播", "oooo");
//        if (!isServiceWork(context, "com.longer.school.utils.mipush.Myservice")) {
        Log.d("启动服务", "oooo");
        Intent startIntent = new Intent(context, Myservice.class);
        context.startService(startIntent);
//        }
    }

    /**
     * 判断服务是否已经在运行
     *
     * @param mContext
     * @param serviceName
     * @return
     */
    public boolean isServiceWork(Context mContext, String serviceName) {
        boolean isWork = false;
        ActivityManager myAM = (ActivityManager) mContext
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> myList = myAM.getRunningServices(40);
        if (myList.size() <= 0) {
            return false;
        }
        for (int i = 0; i < myList.size(); i++) {
            String mName = myList.get(i).service.getClassName().toString();
            if (mName.equals(serviceName)) {
                isWork = true;
                break;
            }
        }
        return isWork;
    }
}
