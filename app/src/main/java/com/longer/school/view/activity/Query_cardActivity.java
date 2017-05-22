package com.longer.school.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Query_cardActivity extends AppCompatActivity {

    @Bind(R.id.card_que_1)
    CardView cardQue1;
    @Bind(R.id.tv_que_2)
    TextView tvQue2;
    @Bind(R.id.card_que_2)
    CardView cardQue2;
    @Bind(R.id.tv_que_3)
    TextView tvQue3;
    @Bind(R.id.card_que_3)
    CardView cardQue3;
    @Bind(R.id.tv_que_1)
    TextView tvQue1;
    private Button but_query;
    private EditText et_cardnum;
    private Context context;
    private LinearLayout query_2;
    private LinearLayout query_3;

    private TextView tv_username;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_banji;
    private TextView tv_money;
    private TextView tv_tel;
    private TextView tv_zhuanye;
    private TextView tv_birthday;
    private TextView tv_place;
    private TextView tv_more;

    private static String cardnum;
    private static String username;//学号
    private final int error_money = 1;// 获取一卡通余额失败
    private final int error_infor = 2;// 获取学生信息失败
    private final int set_money = 3;// 设置一卡通余额
    private final int set_infor = 4;// 设置学生基础信息
    private final int set_moreinfor = 5;// 设置学生更多信息

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case error_money:
                    Toast.makeText(context, "获取余额失败", Toast.LENGTH_SHORT).show();
                    break;
                case error_infor:
                    but_query.setText("查询");
                    but_query.setEnabled(true);
                    Toast.makeText(context, "获取信息失败，请检查卡号！", Toast.LENGTH_SHORT).show();
                    break;
                case set_money:
                    String money = (String) msg.obj;
                    tv_money.setText(money);
                    break;
                case set_infor:
                    but_query.setText("查询");
                    but_query.setEnabled(true);
                    tv_more.setVisibility(View.VISIBLE);
                    tv_more.setOnClickListener(new QueryMore());
                    String[] list = (String[]) msg.obj;
                    username = list[0];
                    tv_username.setText(list[0]);
                    tv_name.setText(list[1]);
                    tv_banji.setText(list[2]);
                    break;
                case set_moreinfor:
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    tv_sex.setText(map.get("sex").toString());
                    tv_tel.setText(map.get("tel").toString());
                    tv_zhuanye.setText(map.get("zhuanye").toString());
                    tv_birthday.setText(map.get("birthday").toString());
                    tv_place.setText(map.get("place").toString());
                    if (("null".equals(map.get("place").toString()))) {
                        Toast.makeText(context, "没有该学生号！", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_query_card);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("一卡通查询");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
        showhistory();
    }

    public void inti() {
        cardQue1.setVisibility(View.GONE);
        cardQue2.setVisibility(View.GONE);
        cardQue3.setVisibility(View.GONE);
        but_query = (Button) findViewById(R.id.frag_query_card_bu);
        et_cardnum = (EditText) findViewById(R.id.frag_query_cardnum);

        tv_username = (TextView) findViewById(R.id.query_card_username);
        tv_birthday = (TextView) findViewById(R.id.query_card_birthday);
        tv_money = (TextView) findViewById(R.id.query_card_money);
        tv_name = (TextView) findViewById(R.id.query_card_name);
        tv_place = (TextView) findViewById(R.id.query_card_palce);
        tv_sex = (TextView) findViewById(R.id.query_card_sex);
        tv_tel = (TextView) findViewById(R.id.query_card_tel);
        tv_banji = (TextView) findViewById(R.id.query_card_banji);
        tv_zhuanye = (TextView) findViewById(R.id.query_card_zhuanye);
        tv_more = (TextView) findViewById(R.id.query_card_more);

        query_2 = (LinearLayout) findViewById(R.id.query_2);
        query_3 = (LinearLayout) findViewById(R.id.query_3);
        query_2.setVisibility(View.INVISIBLE);
        query_3.setVisibility(View.INVISIBLE);
        tv_more.setVisibility(View.INVISIBLE);

        context = Query_cardActivity.this;
        but_query.setOnClickListener(new QueryListener());
    }

    class QueryListener implements View.OnClickListener {
        public void onClick(View v) {
            if (et_cardnum.getText().length() < 8) {
                Toast.makeText(context, "请输入正确的卡号", Toast.LENGTH_LONG).show();
                return;
            }
            cardQue1.setVisibility(View.GONE);
            cardQue2.setVisibility(View.GONE);
            cardQue3.setVisibility(View.GONE);
            but_query.setText("查询中");
            but_query.setEnabled(false);
            //点击查询之后隐藏输入法
            InputMethodManager imm = (InputMethodManager) context.getApplicationContext().
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_cardnum.getWindowToken(), 0);
            cardnum = et_cardnum.getText().toString().trim();
            getInfor();
        }
    }

    //查询更多
    class QueryMore implements View.OnClickListener {
        public void onClick(View v) {
            query_2.setVisibility(View.VISIBLE);
            query_3.setVisibility(View.VISIBLE);
            getmoreInfor();
        }
    }

    /**
     * 显示搜索历史，我们将搜索历史储存在share里面
     */
    private void showhistory() {
        final String str1 = FileTools.getshareString("querycard1");
        final String str2 = FileTools.getshareString("querycard2");
        final String str3 = FileTools.getshareString("querycard3");
        if (!"".equals(str1)) {
            cardQue1.setVisibility(View.VISIBLE);
            tvQue1.setText(str1);
            cardQue1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_cardnum.setText(str1);
                    et_cardnum.setSelection(str1.length());
                }
            });
            cardQue1.setTranslationY(-500);
            cardQue1.animate()
                    .translationY(0)
                    .setStartDelay(0)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(1000)
                    .start();
        }
        if (!"".equals(str2)) {
            cardQue2.setVisibility(View.VISIBLE);
            tvQue2.setText(str2);
            cardQue2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_cardnum.setText(str2);
                    et_cardnum.setSelection(str2.length());
                }
            });
            cardQue2.setTranslationY(-500);
            cardQue2.animate()
                    .translationY(0)
                    .setStartDelay(400)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(1000)
                    .start();
        }
        if (!"".equals(str3)) {
            cardQue3.setVisibility(View.VISIBLE);
            tvQue3.setText(str3);
            cardQue3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_cardnum.setText(str3);
                    et_cardnum.setSelection(str3.length());
                }
            });
            cardQue3.setTranslationY(-500);
            cardQue3.animate()
                    .translationY(0)
                    .setStartDelay(800)
                    .setInterpolator(new DecelerateInterpolator(1.f))
                    .setDuration(800)
                    .start();
        }
    }


    public void getInfor() {
        // 获取学生信息
        new Thread() {
            public void run() {
                String[] list = LoginService.get_card_infor(context, cardnum);
                Message mes = new Message();
                if (list == null) {
                    // 获取学生信息失败
                    mes.what = error_infor;
                } else {
                    mes.what = set_infor;
                    mes.obj = list;
                }
                handler.sendMessage(mes);
                savequery();
            }

            ;
        }.start();
    }

    public void getmoreInfor() {
        // 获取学生信息
        new Thread() {
            public void run() {
                Map<String, String> map = LoginService.query_student(context, username);
                Message mes = new Message();
                if (map == null) {
                    // 获取学生信息失败
                    mes.what = error_infor;
                } else {
                    mes.what = set_moreinfor;
                    mes.obj = map;
                }
                handler.sendMessage(mes);
            }

            ;
        }.start();
        // 获取余额信息
        new Thread() {
            public void run() {
                String money = LoginService.query_money(context, username);
                Message mes = new Message();
                if (money == null) {
                    // 获取余额失败
                    mes.what = error_money;
                } else {
                    mes.what = set_money;
                    mes.obj = money;
                }
                handler.sendMessage(mes);
            }

            ;
        }.start();
    }


    /**
     * 不管成功还是失败都保存查询的帐号
     */
    private void savequery() {
        String str1 = FileTools.getshareString("querycard1");
        String str2 = FileTools.getshareString("querycard2");
        String str3 = FileTools.getshareString("querycard3");
        if ("".equals(str1)) {
            FileTools.saveshareString("querycard1", cardnum);
        } else if (cardnum.equals(str1) || cardnum.equals(str2) || cardnum.equals(str3)) {
            //如果之前查询过也不用保存
        } else if ("".equals(str2)) {
            FileTools.saveshareString("querycard2", cardnum);
        } else if ("".equals(str3)) {
            FileTools.saveshareString("querycard3", cardnum);
        } else {
            FileTools.saveshareString("querycard1", cardnum);
            FileTools.saveshareString("querycard2", str1);
            FileTools.saveshareString("querycard3", str2);
        }
    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "一卡通查询");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
