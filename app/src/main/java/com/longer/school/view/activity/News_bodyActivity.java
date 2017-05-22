package com.longer.school.view.activity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.PublicTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class News_bodyActivity extends AppCompatActivity {

    private TextView tv_title;
    private TextView tv_body;
    private TextView tv_time;
    private static String link;
    private Context context;

    private final int success = 1;// 获取数据成功
    private final int fall = 2;// 获取数据失败

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case success:
                    String body = (String) msg.obj;
                    tv_body.setText(body);
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
        setContentView(R.layout.layout_news_body);
        inti();
    }

    public void inti() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("校园公告");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    finish();
                }
            });
        }

        tv_title = (TextView) findViewById(R.id.news_body_tv_title);
        tv_body = (TextView) findViewById(R.id.news_body_tv_body);
        tv_time = (TextView) findViewById(R.id.news_body_tv_time);

        context = News_bodyActivity.this;
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String time = intent.getStringExtra("time");
        link = intent.getStringExtra("link");

        tv_title.setText(title);
        tv_time.setText(time);

    }

    @Override
    protected void onStart() {
        super.onStart();

        get_body();
    }

    public void get_body() {
        new Thread() {
            public void run() {
                Message mes = new Message();
                try {
                    Document doc1 = Jsoup.connect(link).timeout(5000).post();
                    Elements element1 = doc1.select("div#vsb_newscontent");
                    Document doc2 = Jsoup.parse(element1.toString());
                    Elements element2 = doc1.select("font");

                    StringBuffer body = new StringBuffer();
                    for (Element links : element2) {
                        String tex = links.text() + "\n\n";
                        body.append(tex);
                    }
                    mes = new Message();
                    mes.what = success;
                    mes.obj = body.toString();
                    handler.sendMessage(mes);

                } catch (Exception e) {
                    mes = new Message();
                    mes.what = fall;
                    handler.sendMessage(mes);
                    System.out.println("获取内容出错了");
                }
            }

            ;
        }.start();
    }

    public void click_open(View view) {
        PublicTools.openhtml(context,link);
    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "校园新闻详细");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
