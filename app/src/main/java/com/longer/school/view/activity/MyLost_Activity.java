package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.longer.school.Application;
import com.longer.school.modle.bean.Lost;
import com.longer.school.modle.bean.User;
import com.longer.school.R;
import com.longer.school.adapter.LostAdapter;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

/**
 * Created by Axu on 2016/9/19.
 */
public class MyLost_Activity extends AppCompatActivity {
    public Context context;
    public LostAdapter lostAdapter;
    private Boolean atbottom = true;
    public static RecyclerView rv_main;
    private int i = 6;
    public static List<Lost> my_losts = null;
    public static LinearLayoutManager linearLayoutManager_lost;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_mylost);
        context = MyLost_Activity.this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveColor(Color.parseColor(FileTools.getshareString("refreshcolor")));
        mWaveSwipeRefreshLayout.setMaxDropHeight(1350);
        mWaveSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        rv_main = (RecyclerView) findViewById(R.id.rv_lost_main);
        linearLayoutManager_lost = new LinearLayoutManager(context);
        rv_main.setItemAnimator(new DefaultItemAnimator());
        rv_main.setLayoutManager(linearLayoutManager_lost);
        rv_main.addOnScrollListener(onScrollListener);

        toolbar.setTitle("个人中心");
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        setData();
    }


    /**
     * 设置滑动监听，实现上拉加载更多
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView_goods, int dx, int dy) {
            super.onScrolled(recyclerView_goods, dx, dy);
            int visible = linearLayoutManager_lost.getChildCount();
            int total = linearLayoutManager_lost.getItemCount();
            int past = linearLayoutManager_lost.findFirstCompletelyVisibleItemPosition();
            if ((visible + past) >= total) {
                if (atbottom) {
                    atbottom = false;
                }
            }
        }
    };


    /**
     * 查询数据
     */
    public void setData() {
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        query.addWhereEqualTo("user", user);
        query.order("-updatedAt");
        query.setLimit(6);
        i = 6;
        query.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                if (e != null) {
                    Toast.show("查询失物招领出错：" + e.toString());
                    Log.d("查询失物招领出错", e.toString());
                    return;
                }
                my_losts = list;
                Log.d("设置适配器", "lostadapter");
                lostAdapter = new LostAdapter(my_losts, context,"");
                rv_main.setAdapter(lostAdapter);
                lostAdapter.setOnItemclicklister(itemclick);
                lostAdapter.notifyDataSetChanged();
                atbottom = true;

            }
        });
    }

    /**
     * 每行的单击事件
     */
    LostAdapter.Itemclick itemclick = new LostAdapter.Itemclick() {

        public void OnItemclick(View v, int position) {
            Log.d("###", Integer.toString(position));
            Lost lost = my_losts.get(position);
            Intent intent = new Intent(context, Lost_Activity.class);
            intent.putExtra("Lost", lost);
            startActivity(intent);
        }
    };
    /**
     * 下拉刷新
     */
    WaveSwipeRefreshLayout.OnRefreshListener onRefreshListener = new WaveSwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    setData();
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                }
            }, 800);
        }
    };
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "我的失物招领");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

}
