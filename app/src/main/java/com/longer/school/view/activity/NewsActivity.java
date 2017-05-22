package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.longer.school.Application;
import com.longer.school.modle.bean.Lost;
import com.longer.school.modle.bean.News;
import com.longer.school.R;
import com.longer.school.adapter.NewsAdapter;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private Context context;
    private SwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RecyclerView recycle;
    public static LinearLayoutManager linearLayoutManager_lost;
    public static List<News> news = null;
    public NewsAdapter newsAdapter;
    private final int getdate_success = 1;// 获取数据成功，之后解析数据
    private final int getdate_fall = 2;// 刷新失败,

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            showrefresh(false);
            switch (msg.what) {
                case getdate_success:
                    Toast.show("刷新成功");
                    file_newsinfo();
                    break;
                case getdate_fall:
                    Toast.show("获取新闻失败了");
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_news);

        init();
    }

    public void init() {
        context = NewsActivity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("校园公告");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        recycle = (RecyclerView) findViewById(R.id.rv_new);
        linearLayoutManager_lost = new LinearLayoutManager(context);
        recycle.setItemAnimator(new DefaultItemAnimator());
        recycle.setLayoutManager(linearLayoutManager_lost);
        setSwipe();

        setdate();
    }

    public void setdate() {
        // 如果News.txt 文件存在，直接从文件中读取
        String txt_news = FileTools.getFile(context, "News.txt");
        showrefresh(true);
        if (txt_news != null) {
            Log.d("tip", "有新闻缓存数据");
            file_newsinfo();
            getdate();
        } else {//没有缓存重新爬数据
            getdate();
        }
    }

    /**
     * 爬取数据
     */
    public void getdate() {
        new Thread() {
            public void run() {
                Message mes = new Message();
                try {
                    String url = "http://www.cdtu.edu.cn/lanmuliebiao.jsp?urltype=tree.TreeTempUrl&wbtreeid=1012";
                    Document doc1 = Jsoup.connect(url).timeout(5000).post();
                    Elements element1 = doc1.select("ul.indexlist");

                    // 将截取的网页信息保存到news.txt文件中
                    String text = element1.toString();
                    FileTools.saveFile(context, "News.txt", text);
                    System.out.println("保存新闻成功");

                    mes.what = getdate_success;
                    handler.sendMessage(mes);

                } catch (Exception e) {
                    mes.what = getdate_fall;
                    handler.sendMessage(mes);
                    System.out.println("下拉刷新出错了");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 解析数据放进类
     */
    public void file_newsinfo() {
        String html = FileTools.getFile(context, "News.txt");
        Document doc2 = Jsoup.parse(html);
        Elements element2 = doc2.getElementsByTag("li");

        news = new ArrayList<News>();

        News ne;
        for (Element links : element2) {
            ne = new News();
            ne.setTime(links.select("span.date").text());
            ne.setTitle(links.select("span.name").text());
            Elements element3 = links.getElementsByTag("a");
            ne.setLink("http://www.cdtu.edu.cn" + element3.attr("href"));
            news.add(ne);
            ne = null;
        }
        Log.d("tip", "一共有多少条：" + news.size());
        newsAdapter = new NewsAdapter(news);
        recycle.setAdapter(newsAdapter);
        newsAdapter.setOnItemclicklister(itemclick);
        newsAdapter.notifyDataSetChanged();
    }

    /**
     * 每一行的单击事件
     */
    NewsAdapter.Itemclick itemclick = new NewsAdapter.Itemclick() {
        public void OnItemclick(View v, int position) {
            Log.d("tip", "点击了");
            Intent intent = new Intent(context, News_bodyActivity.class);
            intent.putExtra("title", news.get(position).getTitle());
            intent.putExtra("time", news.get(position).getTime());
            intent.putExtra("link", news.get(position).getLink());
            startActivity(intent);
        }
    };

    /**
     * 显示wsiperefresh
     *
     * @param show true 为显示
     */
    private void showrefresh(final boolean show) {
        mWaveSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (show) {
                    if (!mWaveSwipeRefreshLayout.isRefreshing()) {
                        mWaveSwipeRefreshLayout.setRefreshing(true);
                    } else {
                    }
                } else {
                    if (mWaveSwipeRefreshLayout.isRefreshing()) {
                        mWaveSwipeRefreshLayout.setRefreshing(false);
                    } else {
                    }
                }
            }
        });
    }

    private void setSwipe() {
        mWaveSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.new_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF4081"));
        mWaveSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Thread() {
                public void run() {
                    Message mes = new Message();
                    try {
                        sleep(800);
                        String url = "http://www.cdtu.edu.cn/lanmuliebiao.jsp?urltype=tree.TreeTempUrl&wbtreeid=1012";
                        Document doc1 = Jsoup.connect(url).timeout(5000).post();
                        Elements element1 = doc1.select("ul.indexlist");

                        // 将截取的网页信息保存到news.txt文件中
                        String text = element1.toString();
                        FileTools.saveFile(context, "News.txt", text);
                        System.out.println("保存新闻成功");

                        mes.what = getdate_success;
                        handler.sendMessage(mes);

                    } catch (Exception e) {
                        mes.what = getdate_fall;
                        handler.sendMessage(mes);
                        System.out.println("下拉刷新出错了");
                        e.printStackTrace();
                    }
                }
            }.start();
        }
    };
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "校园公告");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
