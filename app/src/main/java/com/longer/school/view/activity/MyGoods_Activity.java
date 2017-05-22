package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.longer.school.Application;
import com.longer.school.modle.bean.Good;
import com.longer.school.modle.bean.Lost;
import com.longer.school.modle.bean.User;
import com.longer.school.R;
import com.longer.school.adapter.GoodsAdapter;
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
 * Created by Axu on 2016/9/24.
 */
public class MyGoods_Activity extends AppCompatActivity {
    private Context context;
    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    private RecyclerView recyclerView;
    private Boolean atbottom;
    private int i = 6;
    public GoodsAdapter goodsAdapter;
    public static List<Good> my_goods = null;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_mygoods);

        context = MyGoods_Activity.this;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) findViewById(R.id.swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveColor(Color.parseColor(FileTools.getshareString("refreshcolor")));
        mWaveSwipeRefreshLayout.setMaxDropHeight(1350);
        mWaveSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
        recyclerView = (RecyclerView) findViewById(R.id.rv_goods_main);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        gridLayoutManager = new GridLayoutManager(context, 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addOnScrollListener(onScrollListener);

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
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visible = gridLayoutManager.getChildCount();
            int total = gridLayoutManager.getItemCount();
            int past = gridLayoutManager.findFirstCompletelyVisibleItemPosition();
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
        if (!mWaveSwipeRefreshLayout.isRefreshing()) {
            mWaveSwipeRefreshLayout.setRefreshing(true);
        }
        User user = BmobUser.getCurrentUser(User.class);
        BmobQuery<Good> query = new BmobQuery<Good>();
        query.addWhereEqualTo("seller", user);
        query.order("-updatedAt");
        query.include("seller");
        query.setLimit(6);
        i = 6;
        boolean isCache = query.hasCachedResult(Good.class);
        query.findObjects(new FindListener<Good>() {
            @Override
            public void done(List<Good> list, BmobException e) {
                if (mWaveSwipeRefreshLayout.isRefreshing()) {
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                }
                if (e != null) {
                    Toast.show("查询失物招领出错：" + e.toString());
                    Log.d("查询失物招领出错", e.toString());
                    return;
                }
                my_goods = list;
                goodsAdapter = new GoodsAdapter(my_goods, context, "");
                goodsAdapter.setOnItemclicklister(itemclick);
                recyclerView.setAdapter(goodsAdapter);
                goodsAdapter.notifyDataSetChanged();
                atbottom = true;
            }
        });
    }

    /**
     * 每行的单击事件
     */
    GoodsAdapter.Itemclick itemclick = new GoodsAdapter.Itemclick() {

        public void OnItemclick(View v, int position) {
            Good good = my_goods.get(position);
            Intent intent = new Intent(context, Goods_Activity.class);
            intent.putExtra("Good", good);
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
        MiStatInterface.recordPageStart(this, "我的跳蚤市场");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }


}
