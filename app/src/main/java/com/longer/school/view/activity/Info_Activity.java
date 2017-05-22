package com.longer.school.view.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.longer.school.Application;
import com.longer.school.modle.bean.Appinfor;
import com.longer.school.Config;
import com.longer.school.R;
import com.longer.school.utils.ali.OrderInfoUtil2_0;
import com.longer.school.utils.ali.PayResult;
import com.longer.school.utils.PublicTools;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.io.File;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by longer
 */
public class Info_Activity extends AppCompatActivity {

    private Context context;


    /**
     *  重要说明:
     *
     *  这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
     *  真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
     *  防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
     */

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "";


    /** 商户私钥，pkcs8格式 */
    /** 如下私钥，RSA2_PRIVATE 或者 RSA_PRIVATE 只需要填入一个 */
    /** 如果商户两个都设置了，优先使用 RSA2_PRIVATE */
    /** RSA2_PRIVATE 可以保证商户交易在更加安全的环境下进行，建议使用 RSA2_PRIVATE */
    /** 获取 RSA2_PRIVATE，建议使用支付宝提供的公私钥生成工具生成， */
    /**
     * 工具地址：https://doc.open.alipay.com/docs/doc.htm?treeId=291&articleId=106097&docType=1
     */
    public static final String RSA2_PRIVATE = "";
    public static final String RSA_PRIVATE = "";

    private static final int SDK_PAY_FLAG = 1;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
//                        Toast.makeText(PayDemoActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        Toast.show("支付成功...");
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.show("支付失败...");
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /////////////////////////////////////////////////
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_info);

        context = Info_Activity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("关于APP");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色

        TextView tv_version = (TextView) findViewById(R.id.tv_info_version);
        tv_version.setText("成功助手V" + Config.version);

        findViewById(R.id.tv_info_qq).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicTools.joinQQGroup(context, "aDdrhfP93WZiEsR2Wr6AUgMDHFZWklgd");
            }
        });

        findViewById(R.id.tv_info_qqnum).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PublicTools.copy("1114070859");
            }
        });
        CardView card = (CardView) findViewById(R.id.card_info_gx);
        card.setOnClickListener(onClickListener);

        /////////////////////////////////////////////////////////////////////
//        findViewById(R.id.tv_info_support).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                payV2();
//            }
//        });
    }
    //////////////////////////////////////////////////////////////////
    /**
     * 支付宝支付业务
     */
    public void payV2() {
        if (TextUtils.isEmpty(APPID) || (TextUtils.isEmpty(RSA2_PRIVATE) && TextUtils.isEmpty(RSA_PRIVATE))) {
            new AlertDialog.Builder(this).setTitle("警告").setMessage("需要配置APPID | RSA_PRIVATE")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialoginterface, int i) {
                            //
                            finish();
                        }
                    }).show();
            return;
        }

        /**
         * 这里只是为了方便直接向商户展示支付宝的整个支付流程；所以Demo中加签过程直接放在客户端完成；
         * 真实App里，privateKey等数据严禁放在客户端，加签过程务必要放在服务端完成；
         * 防止商户私密数据泄露，造成不必要的资金损失，及面临各种安全风险；
         *
         * orderInfo的获取必须来自服务端；
         */
        boolean rsa2 = (RSA2_PRIVATE.length() > 0);
        Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(APPID, rsa2);
        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);

        String privateKey = rsa2 ? RSA2_PRIVATE : RSA_PRIVATE;
        String sign = OrderInfoUtil2_0.getSign(params, privateKey, rsa2);
        final String orderInfo = orderParam + "&" + sign;

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                PayTask alipay = new PayTask(Info_Activity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Log.i("msp", result.toString());

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }


    ////////////////////////////////////////////////////////////////


    /**
     * 检查更新
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final ProgressDialog pg = new ProgressDialog(context);
            pg.setMessage("正在检查新版本...");
            pg.setCanceledOnTouchOutside(true);// 点击对话框以外是否消失
            pg.show();
            BmobQuery<Appinfor> bmobQuery = new BmobQuery<Appinfor>();
            bmobQuery.order("-updatedAt");
            bmobQuery.addWhereGreaterThan("version", Config.version);//大于当前版本
            bmobQuery.findObjects(new FindListener<Appinfor>() {
                @Override
                public void done(List<Appinfor> list, BmobException e) {
                    if (e == null && list != null) {
                        pg.dismiss();
                        Log.d("1", "发现新版本");
                        updata(list.get(0));
                    } else {
                        Log.d("info_activity", "没有发现新版本或者出异常了");
                        pg.dismiss();
                        Toast.show("没有发现新版本");
                    }
                }
            });
        }
    };

    /**
     * 有可用更新
     */
    public void updata(final Appinfor appinfor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发现新版本");
        builder.setCancelable(true);
        builder.setMessage(appinfor.getInfor());
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                download(appinfor);
            }
        });

        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.show();

    }

    /**
     * 下载最新文件
     */
    public void download(Appinfor appinfor) {
        final ProgressDialog downloadDialog = new ProgressDialog(context);
        downloadDialog.setTitle("下载中");
        downloadDialog.setMax(100);//设置最大值
        downloadDialog.setProgress(0);//进度归零
        downloadDialog.setCancelable(true);//按返回键取消
        downloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//设置风格为长方形
        downloadDialog.setCanceledOnTouchOutside(false);//点击外部取消
        downloadDialog.setIndeterminate(false);//设置进度是否明确
        downloadDialog.show();

        File path = new File(Environment.getExternalStorageDirectory() + "/" + appinfor.getApp().getFilename());
        final BmobFile bmobfile = new BmobFile("cdtu.apk", "", appinfor.getApp().getUrl());
        final File saveFile = new File(path, bmobfile.getFilename());
        bmobfile.download(saveFile, new DownloadFileListener() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.show("下载成功,保存路径:" + s);
                    if (s != null) {
                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_VIEW);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.setDataAndType(Uri.fromFile(new File(s)), "application/vnd.android.package-archive");
                        startActivity(i);
                    }
                } else {
                    Toast.show("下载失败：" + s);
                }
            }

            @Override
            public void onProgress(Integer integer, long l) {
                downloadDialog.setProgress(integer);
            }

            @Override
            public void onStart() {
                super.onStart();
                Toast.show("开始下载...");
            }
        });

    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "关于app");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

}
