package com.longer.school.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.longer.school.Config;
import com.longer.school.utils.FileTools;


public class AppSplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //如果share 里的版本号不等于当前的版本号就会显示引导页(我们暂时不使用引导页，感觉不实用)
                String share_version = FileTools.getshareString("updatenow");
                if ((Config.version + "").equals(share_version)) {//表示用户已经更新了，我们可以清楚强制更新的缓存
                    //2.1版本，让之前的用户重新登录
//                    FileTools.saveshareString("login", "false");
                    FileTools.saveshareString("updatenow", "");
                }
                startActivity(new Intent(AppSplashActivity.this, MainActivity.class));
                finish();
            }
        }, 400);
    }
}
