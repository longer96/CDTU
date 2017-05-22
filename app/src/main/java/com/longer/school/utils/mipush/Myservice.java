package com.longer.school.utils.mipush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.longer.school.modle.bean.CardClass;
import com.longer.school.R;
import com.longer.school.view.activity.MainActivity;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.TimeTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Axu on 2016/9/29.
 */

public class Myservice extends Service {
    private Thread thread;
    private List<CardClass> cards = new ArrayList<CardClass>();
    private List<CardClass> cards2 = new ArrayList<CardClass>();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        thread = new Thread() {
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    String text = FileTools.getFile(Myservice.this, "card.txt");
                    if (!text.equals("")) {

                        cards = readData();//读取刷新前的缓存数据
                        LoginService.refresh_card(Myservice.this, "20");
                        String str1 = cards.get(0).getTime();
                        cards2 = readData();
                        String str2 = cards2.get(0).getTime();
//                        Log.d("22222", str2);
                        if (!str1.equals(str2)) {
                            int t = 0;//记录变化数量
                            for (int x = 0; x < 20; x++) {
                                if (str1.equals(cards2.get(x).getTime())) {
                                    t = x;
                                }
                            }
                            //如果大于10条不提示
                            if(t < 10) {
                                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                                        Myservice.this)
                                        .setSmallIcon(R.drawable.school_ico)
                                        .setContentTitle("消费记录" + Integer.toString(t) + "条")
                                        .setContentText(cards2.get(0).getWhere() + ":" + cards2.get(0).getMoney() + "元")
                                        .setDefaults(Notification.DEFAULT_ALL);//设置提示方式为全部默认
                                mBuilder.setTicker("New message");//第一次提示消息的时候显示在通知栏上
                                mBuilder.setNumber(t);//第几条
                                mBuilder.setAutoCancel(true);//自己维护通知的消失
                                Intent resultIntent = new Intent(Myservice.this, MainActivity.class);
                                resultIntent.putExtra("it", 1);
                                PendingIntent resultPendingIntent = PendingIntent.getActivity(Myservice.this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                                mBuilder.setContentIntent(resultPendingIntent);
                                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0, mBuilder.build());
                            }
                        }

//                        Log.d("run", "###");
                    }
                    synchronized (thread) {//使线程释放并不消耗CPU
                        try {
                            thread.wait(180000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    synchronized (thread) {//激活线程
                        thread.notify();
                    }
                }

            }

            ;
        };
        thread.start();
        flags = START_STICKY;//据说当内存充足时，服务会再次启动
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 读取缓存的记录时间
     *
     * @return
     */
    public List<CardClass> readData() {
        List<CardClass> list = new ArrayList<CardClass>();
        String text = FileTools.getFile(Myservice.this, "card.txt");
        try {
            JSONArray js = new JSONArray(text);
            int lo = js.length();
            System.out.println("一共有多少条记录" + lo);
            CardClass card = null;
            for (int i = 0; i < lo; i++) {
                JSONObject jo = (JSONObject) js.opt(i);
                card = new CardClass();
                card.setDay(TimeTools.xinqi(jo.getString("FLW_CONTIME")));
                card.setMoney(jo.getString("FLW_AMOUNT"));
                card.setTime(jo.getString("FLW_CONTIME"));
                card.setWhere(jo.getString("TRD_CNAME"));
                list.add(card);
                card = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("数据读取产生异常");
        }
        return list;
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onDestroy() {
        Intent localIntent = new Intent();
        localIntent.setClass(this, Myservice.class);
        this.startService(localIntent);
        super.onDestroy();
    }
}
