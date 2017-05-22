package com.longer.school.view.activity.library;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.LoginService;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.ButterKnife;

public class Library_select_bodyActivity extends AppCompatActivity {

    private TextView tv_book;
    private TextView tv_author;
    private TextView tv_where;
    private TextView tv_time;
    private TextView tv_class;
    private TextView tv_num;
    private TextView tv_place;
    private TextView tv_tsg;
    private TextView tv_text;
    private TextView tv_menu;

    private static String link;
    private Context context;
    private static String text;

    private final int success = 1;// 获取数据成功
    private final int fall = 2;// 获取数据失败

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case success:
                    setdate();
                    break;
                case fall:
                    Toast.makeText(context, "获取数据出错", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.layout_select_body);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("图书详细");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
    }


    public void inti() {
        tv_book = (TextView) findViewById(R.id.library_select_tv_book);
        tv_author = (TextView) findViewById(R.id.library_select_tv_author);
        tv_where = (TextView) findViewById(R.id.library_select_tv_where);
        tv_time = (TextView) findViewById(R.id.library_select_tv_time);
        tv_class = (TextView) findViewById(R.id.library_select_tv_class);
        tv_num = (TextView) findViewById(R.id.library_select_tv_num);
        tv_place = (TextView) findViewById(R.id.library_select_tv_place);
        tv_tsg = (TextView) findViewById(R.id.library_select_tv_tsg);
        tv_text = (TextView) findViewById(R.id.library_select_tv_text);
        // tv_menu = (TextView) findViewById(R.id.library_select_tv_menu);

        context = Library_select_bodyActivity.this;
        Intent intent = getIntent();
        String title = intent.getStringExtra("name");
        link = intent.getStringExtra("link");

        tv_book.setText(title);

    }

    @Override
    protected void onStart() {
        super.onStart();
        get_body();
    }

    /**
     * 自动登录书籍
     */
    public void get_body() {
        new Thread() {
            public void run() {
                Message mes = new Message();
                try {
                    text = LoginService.get_library_select_body(context, link);
                    mes.what = success;
                    handler.sendMessage(mes);
                } catch (Exception e) {
                    mes = new Message();
                    mes.what = fall;
                    handler.sendMessage(mes);
                    System.out.println("获取书籍内容出错了");
                }
            }

            ;
        }.start();
    }

    /**
     * 解析内容
     */
    public void setdate() {
        try {
            Document doc = Jsoup.parse(text);
            Elements ele1 = doc.select("table[id=Table2] tbody tr");
            if (ele1.size() == 0) {
                ele1 = doc.select("table[id=Table1] tbody tr td");
                Log.d("tip", "为空" + ele1.toString());
                tv_text.setText(ele1.text().toString());
                com.longer.school.utils.Toast.show("该书的信息有点少哦");
                return;
            }

//            Log.d("tip", "长度：" + ele1.size() + "\n内容：" + ele1.toString());
//            Log.d("tip","   长度：" + ele1.get(1).select("td").size() + "\n内容：" + ele1.get(1).select("td").toString());
            tv_author.setText(ele1.get(1).select("td").get(1).text());
            tv_where.setText(ele1.get(2).select("td").get(1).text());
            tv_time.setText(ele1.get(3).select("td").get(1).text());

            tv_text.setText(ele1.text().toString());
            // tv_menu.setText(ele1.get(lo - 1).select("td").get(1).text());


            Elements ele2 = doc.select("table[id=Table1] tbody tr");
            String str;
//            Log.d("tip", "长度：" + ele2.size() + "\n内容：" + ele2.toString());
            if (ele2.size() <= 2) {
                str = ele2.get(1).text();
            } else {
                str = ele2.get(ele2.size() - 1).text();
            }
            tv_class.setText(str.split("索书号:")[0]);
            tv_num.setText(str.split("册数:")[1]);
            tv_tsg.setText(str.split("图书馆:")[1].split("册数:")[0]);
            Elements ele3 = doc.select("table[id=DataGrid1] tbody tr");
            tv_place.setText(ele3.get(1).select("td").get(2).text());

        } catch (Exception e) {
            e.printStackTrace();
            com.longer.school.utils.Toast.show("获取图书信息失败咯");
        }
    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "图书查询详细界面");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
