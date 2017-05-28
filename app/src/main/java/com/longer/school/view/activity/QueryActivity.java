package com.longer.school.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.Bind;
import butterknife.ButterKnife;

public class QueryActivity extends AppCompatActivity {
    @Bind(R.id.tv_que_1)
    TextView tvQue1;
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
    private ImageView iv_query;
    private ImageView iv_clear;
    private EditText et_username;
    private Context context;

    private TextView tv_money;
    private TextView tv_name;
    private TextView tv_sex;
    private TextView tv_yuanxi;
    private TextView tv_banji;
    private TextView tv_tel;
    private TextView tv_zhuanye;
    private TextView tv_birthday;
    private TextView tv_place;

    private static String username;
    private final int error_money = 1;// 获取一卡通余额失败
    private final int error_infor = 2;// 获取学生信息失败
    private final int set_money = 3;// 设置一卡通余额
    private final int set_infor = 4;// 设置学生信息

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case error_money:
                    Toast.makeText(context, "获取余额失败", Toast.LENGTH_SHORT).show();
                    break;
                case error_infor:
                    Toast.makeText(context, "获取信息失败", Toast.LENGTH_SHORT).show();
                    break;
                case set_money:
                    String money = (String) msg.obj;
                    tv_money.setText(money);
                    break;
                case set_infor:
                    Map<String, String> map = (Map<String, String>) msg.obj;
                    if (("null".equals(map.get("place").toString()))) {
                        Toast.makeText(context, "没有该学生号！", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    setdata(map);
                    break;
            }

        }
    };

    /**
     * 设置数据
     *
     * @param map
     */
    private void setdata(Map<String, String> map) {
        tv_banji.setText(map.get("banji").toString());
        tv_name.setText(map.get("name").toString());
        tv_sex.setText(map.get("sex").toString());
        tv_yuanxi.setText(map.get("yuanxi").toString());
        // 电话号码隐藏
        String tel = "null";
        try {
            tel = map.get("tel").toString();
            tel = tel.substring(0, 7) + "不给看";
        } catch (Exception e) {
            tel = "null";
        }
        tv_tel.setText(tel);
        tv_zhuanye.setText(map.get("zhuanye").toString());
        tv_birthday.setText(map.get("birthday").toString());
        tv_place.setText(map.get("place").toString());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_query);
        ButterKnife.bind(this);
        inti();
        showhistory();
    }

    public void inti() {
        cardQue1.setVisibility(View.GONE);
        cardQue2.setVisibility(View.GONE);
        cardQue3.setVisibility(View.GONE);
        iv_query = (ImageView) findViewById(R.id.iv_que_serch);
        et_username = (EditText) findViewById(R.id.frag_query_et);
        tv_banji = (TextView) findViewById(R.id.query_tv_banji);
        tv_birthday = (TextView) findViewById(R.id.query_tv_birthday);
        tv_money = (TextView) findViewById(R.id.query_tv_money);
        tv_name = (TextView) findViewById(R.id.query_tv_name);
        tv_place = (TextView) findViewById(R.id.query_tv_palce);
        tv_sex = (TextView) findViewById(R.id.query_tv_sex);
        tv_tel = (TextView) findViewById(R.id.query_tv_tel);
        tv_yuanxi = (TextView) findViewById(R.id.query_tv_yuanxi);
        tv_zhuanye = (TextView) findViewById(R.id.query_tv_zhuanye);
        iv_clear = (ImageView) findViewById(R.id.iv_que_clear);
        iv_clear.setVisibility(View.GONE);

        iv_clear.setOnClickListener(v->
                et_username.setText("");
           );
        findViewById(R.id.real_query).setBackgroundColor(Color.parseColor(FileTools.getshareString("refreshcolor")));
        findViewById(R.id.iv_que_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        context = QueryActivity.this;
        iv_query.setOnClickListener(new QueryListener());
        et_username.addTextChangedListener(watcher);
    }


    /**
     * 文字变化监听
     */
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count,
                                      int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() > 0) {
                iv_clear.setVisibility(View.VISIBLE);
            }
        }
    };

    /**
     * 显示搜索历史，我们将搜索历史储存在share里面
     */
    private void showhistory() {
        final String str1 = FileTools.getshareString("query1");
        final String str2 = FileTools.getshareString("query2");
        final String str3 = FileTools.getshareString("query3");
        if (!"".equals(str1)) {
            cardQue1.setVisibility(View.VISIBLE);
            tvQue1.setText(str1);
            cardQue1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    et_username.setText(str1);
                    et_username.setSelection(str1.length());
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
                    et_username.setText(str2);
                    et_username.setSelection(str2.length());
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
                    et_username.setText(str3);
                    et_username.setSelection(str3.length());
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

    class QueryListener implements View.OnClickListener {
        public void onClick(View v) {
            username = et_username.getText().toString().trim();
            Pattern p = Pattern.compile("[0-9]*");
            Matcher m = p.matcher(username);
            if (username.length() != 10 || !m.matches()) {
                Toast.makeText(context, "请输入正确的学生号", Toast.LENGTH_SHORT).show();
                return;
            }
            cardQue1.setVisibility(View.GONE);
            cardQue2.setVisibility(View.GONE);
            cardQue3.setVisibility(View.GONE);
            // 点击查询之后隐藏输入法
            InputMethodManager imm = (InputMethodManager) context.getApplicationContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(et_username.getWindowToken(), 0);
            if ("2014513211".equals(username) || "2014513210".equals(username)) {
                Toast.makeText(context, "此人已帅到无法显示", Toast.LENGTH_SHORT).show();
                et_username.setText("");
                return;
            }
            getInfor();
        }
    }

//    String str;
//
//    // 获取学生信息（已经没用了）
//    class English implements View.OnClickListener {
//        String COOKIE2 = FileTools.getshare(context, "COOKIE2");
//        public void onClick(View v) {
//            // 线程1
//            new Thread() {
//                public void run() {
//                    String username;
//                    for (int i = 130000; i < 150000; i++) {
//                        username = "1601" + i;
//                        System.out.println("线程1-->" + username);
//                        str = LoginService.getatudent2(COOKIE2, username);
//                        if (str != null) {
//                            try {
//                                File file = new File(context.getFilesDir(), "stu16_01_15.txt");
//                                FileOutputStream fos = new FileOutputStream(file, true);
//                                fos.write(str.getBytes());
//                                fos.close();
//                            } catch (Exception e) {
//                                System.out.println("保存问件出错");
//                            }
//                        }
//                    }
//                }
//            }.start();
//            // 线程2
//            new Thread() {
//                public void run() {
//                    String username;
//                    for (int i = 150000; i < 170000; i++) {
//                        username = "1601" + i;
//                        System.out.println("线程2------->" + username);
//                        str = LoginService.getatudent2(COOKIE2, username);
//                        if (str != null) {
//                            try {
//                                File file = new File(context.getFilesDir(), "stu16_01_17.txt");
//                                FileOutputStream fos = new FileOutputStream(file, true);
//                                fos.write(str.getBytes());
//                                fos.close();
//                            } catch (Exception e) {
//                                System.out.println("保存问件出错");
//                            }
//                        }
//                    }
//                }
//            }.start();
//            // 线程3
//            new Thread() {
//                public void run() {
//                    String username;
//                    for (int i = 170000; i < 190000; i++) {
//                        username = "1601" + i;
//                        System.out.println("线程3---------------->：" + username);
//                        str = LoginService.getatudent2(COOKIE2, username);
//                        if (str != null) {
//                            try {
//                                File file = new File(context.getFilesDir(), "stu16_01_19.txt");
//                                FileOutputStream fos = new FileOutputStream(file, true);
//                                fos.write(str.getBytes());
//                                fos.close();
//                            } catch (Exception e) {
//                                System.out.println("保存问件出错");
//                            }
//                        }
//                    }
//                }
//            }.start();
//            // 线程4
//            new Thread() {
//                public void run() {
//                    String username;
//                    for (int i = 190000; i < 210000; i++) {
//                        username = "1601" + i;
//                        System.out.println("线程4---------------->：" + username);
//                        str = LoginService.getatudent2(COOKIE2, username);
//                        if (str != null) {
//                            try {
//                                File file = new File(context.getFilesDir(), "stu16_01_21.txt");
//                                FileOutputStream fos = new FileOutputStream(file, true);
//                                fos.write(str.getBytes());
//                                fos.close();
//                            } catch (Exception e) {
//                                System.out.println("保存问件出错");
//                            }
//                        }
//                    }
//                }
//            }.start();
//        }
//    }

    /**
     * 获取学生信息
     */
    public void getInfor() {
        new Thread() {
            public void run() {
                // Log.d("学生信息:", LoginService.getatudent2(context,username));
                Map<String, String> map = LoginService.query_student(context, username);
                Message mes = new Message();
                if (map == null) {
                    // 获取学生信息失败
                    mes.what = error_infor;
                } else {
                    mes.what = set_infor;
                    mes.obj = map;
                }
                handler.sendMessage(mes);
                savequery();
            }
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
        }.start();
    }


    /**
     * 不管成功还是失败都保存查询的帐号
     */
    private void savequery() {
        String str1 = FileTools.getshareString("query1");
        String str2 = FileTools.getshareString("query2");
        String str3 = FileTools.getshareString("query3");
        if ("".equals(str1)) {
            FileTools.saveshareString("query1", username);
        } else if (username.equals(str1) || username.equals(str2) || username.equals(str3)) {
            //如果之前查询过也不用保存
        } else if ("".equals(str2)) {
            FileTools.saveshareString("query2", username);
        } else if ("".equals(str3)) {
            FileTools.saveshareString("query3", username);
        } else {
            FileTools.saveshareString("query1", username);
            FileTools.saveshareString("query2", str1);
            FileTools.saveshareString("query3", str2);
        }
    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "学生信息查询");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
