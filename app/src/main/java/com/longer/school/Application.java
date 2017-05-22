package com.longer.school;


import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import com.blankj.utilcode.utils.Utils;
import com.longer.school.modle.bean.User;
import com.longer.school.view.activity.MainActivity;
import com.longer.school.utils.FileTools;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;


/**
 * Created by longer on 2016/7/28.
 */
public class Application extends android.app.Application {

    private static Application INSTANCE;

    public static Application getINSTANCE() {
        return INSTANCE;
    }

    private void setInstance(Application app) {
        setBmobIMApplication(app);
    }

    private static void setBmobIMApplication(Application a) {
        Application.INSTANCE = a;
    }

    public static int theme;//得到用户选择的主题颜色

    public static User my = null;

    public static void setuser(User user) {
        my = user;
    }

    //----小米推送  beging------------------
    private static final String APP_ID = Config.APP_ID;
    private static final String APP_KEY = Config.APP_KEY;
    public static final String TAG = "com.longer.school";
    //    private static DemoHandler sHandler = null;
    private static MainActivity sMainActivity = null;
    //----小米推送  end------------------

    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        setTheme(R.style.AppTheme);
        gettheme();
        init();//初始化数据，比如主题，颜色等等
        Utils.init(this);
        // 初始化NoHttp
//        NoHttp.init(INSTANCE);
        // 打开NoHttp的调试模式
//        Logger.setDebug(true);
        // 设置NoHttp的日志的tag
//        Logger.setTag("NoHttpTest");


        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
        BmobConfig config = new BmobConfig.Builder(INSTANCE)
                //设置appkey
                .setApplicationId(Config.ApplicationKey)
                //请求超时时间（单位为秒）：默认15s
                .setConnectTimeout(10)
                //文件分片上传时每片的大小（单位字节），默认512*1024
                .setUploadBlockSize(512 * 1024)
                //文件的过期时间(单位为秒)：默认1800s
                .setFileExpiration(5000)
                .build();
        Bmob.initialize(config);


        // 注册push服务，注册成功后会向DemoMessageReceiver发送广播(小米推送)
        // 可以从DemoMessageReceiver的onCommandResult方法中MiPushCommandMessage对象参数中获取注册信息
        if (shouldInit()) {
            MiPushClient.registerPush(this, APP_ID, APP_KEY);
        }
        //小米数据统计
        MiStatInterface.initialize(this.getApplicationContext(), APP_ID, APP_KEY,
                "default channel");
        MiStatInterface.setUploadPolicy(
                MiStatInterface.UPLOAD_POLICY_REALTIME, 0);
        MiStatInterface.enableExceptionCatcher(true);// 崩溃日志收集
//        URLStatsRecorder.enableAutoRecord();//实时网络监控
    }

    //初始化一个颜色，用来设置下拉菜单
    public void init() {
        String str = FileTools.getshareString("refreshcolor");
        if ("".equals(str)) {
            FileTools.saveshareInt("theme", R.style.AppTheme_NoActionBar);
            FileTools.saveshareString("refreshcolor", "#3F51B5");//保存一个颜色，用来设置下拉刷新的颜色
        }
    }

    /**
     * 得到保存的主题
     */
    private void gettheme() {
        theme = FileTools.getshareInt("theme");
        theme = theme == 404 ? R.style.AppTheme_NoActionBar : theme;
    }

    public static void settheme(int mtheme) {
        theme = mtheme;
    }

    //----小米推送  beging------------------
    private boolean shouldInit() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    //----小米推送  end------------------

}
