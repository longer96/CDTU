package com.longer.school.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardToolsActivity extends AppCompatActivity {
    @Bind(R.id.tv_cardtools_name)
    TextView tvCardtoolsName;
    @Bind(R.id.tv_cardtools_id)
    TextView tvCardtoolsId;
    @Bind(R.id.btn_cardtool_gs)
    Button btnCardtoolGs;
    @Bind(R.id.btn_cardtool_gm)
    Button btnCardtoolGm;
    private Context context;
    private ProgressDialog pg;
    private final int success = 1;
    private final int fish = 2;
    private final int gs_success = 3;
    private final int gs_fish = 4;
    private String jieguo = null;

    // 主线程创建消息处理器
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            pg.dismiss();// 对话框消失
            switch (msg.what) {
                case success:
                    Toast.makeText(context, "登录成功", Toast.LENGTH_SHORT).show();
                    setdata();
                    break;
                case fish:
                    btnCardtoolGm.setEnabled(false);
                    btnCardtoolGs.setEnabled(false);
                    Toast.makeText(context, "登录失败", Toast.LENGTH_SHORT).show();
                    break;
                case gs_success:
                    Toast.makeText(context, jieguo, Toast.LENGTH_SHORT).show();
//                    tvCardtoolsId.setText("用户编号：" + jieguo);
                    break;
                case gs_fish:
                    Toast.makeText(context, "失败", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    /**
     * 解析数据，显示数据
     */
    private void setdata() {
        String name = FileTools.getshareString("name");
        String username = FileTools.getshareString("username");
        tvCardtoolsName.setText("姓名：" + name);
        tvCardtoolsId.setText("用户编号：" + username);

        //得到一卡通状态
//        new Thread() {
//            public void run() {
//                zt = LoginService.getcardzt();
//            }
//        }.start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_cardtools);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("一卡通工具");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
    }

    public void inti() {
        context = CardToolsActivity.this;
        login_card();
    }

    /**
     * 登录一卡通工具
     */
    private void login_card() {
        pg = new ProgressDialog(context);
        pg.setMessage("正在登录一卡通服务中心...");
        pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
        pg.show();

        new Thread() {
            public void run() {
                String result = LoginService.logincard();
                Message mes = new Message();
                if (result != null) {
                    mes.what = success;
                } else {
                    // 登录失败
                    mes.what = fish;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }


    @OnClick({R.id.btn_cardtool_gs, R.id.btn_cardtool_gm})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cardtool_gs:
                gs();
                break;
            case R.id.btn_cardtool_gm:
                gm();
                break;
        }
    }

    /**
     * 挂失一卡通,弹出窗口
     */
    private void gs() {
//        if (zt[0] == null) {
//            com.longer.school.utils.Toast.show("正在获取卡状态");
//            return;
//        }
        final EditText editText = new EditText(context);
        editText.setHint("一卡通密码");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("挂失一卡通");
        builder.setView(editText, 80, 20, 80, 0);
        builder.setPositiveButton("挂失", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                lostcard(editText.getText().toString().trim());
            }
        });
        builder.create().show();
    }

    /**
     * 挂失一卡通
     */
    private void lostcard(final String mm) {
        pg = new ProgressDialog(context);
        pg.setMessage("正在挂失一卡通...");
        pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
        pg.show();
        new Thread() {
            public void run() {
                jieguo = LoginService.lostcard(mm);
                Message mes = new Message();
                if (jieguo != null) {
                    mes.what = gs_success;
                } else {
                    mes.what = gs_fish;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }

    /**
     * 修改一卡通密码,弹出窗口
     */
    private void gm() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_cardtools, null);
        final EditText editTextold = (EditText) textEntryView.findViewById(R.id.editTextName);
        final EditText editTextnew = (EditText) textEntryView.findViewById(R.id.editTextNum);
        AlertDialog.Builder ad1 = new AlertDialog.Builder(context);
        ad1.setTitle("修改密码:");
        ad1.setView(textEntryView, 80, 30, 80, 0);
        ad1.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
                builder.setTitle("提示");
                builder.setCancelable(true);
                builder.setMessage("确定将密码修改为：" + editTextnew.getText().toString().trim());
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editcard(editTextold.getText().toString().trim(), editTextnew.getText().toString().trim());
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });
        ad1.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int i) {

            }
        });
        ad1.show();// 显示对话框
    }

    /**
     * 修改一卡通密码
     */
    private void editcard(final String old, final String xing) {
        pg = new ProgressDialog(context);
        pg.setMessage("正在修改卡密码...");
        pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
        pg.show();
        new Thread() {
            public void run() {
                jieguo = LoginService.editcard(old, xing);
                Message mes = new Message();
                if (jieguo != null) {
                    mes.what = gs_success;
                } else {
                    mes.what = gs_fish;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "一卡通工具");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
