package com.longer.school.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.longer.school.modle.bean.Lost;
import com.longer.school.R;
import com.longer.school.view.activity.Lost_Activity;
import com.longer.school.view.activity.MainActivity;
import com.longer.school.adapter.LostAdapter;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Axu on 2016/9/17.
 */
public class Fragment_Lost extends Fragment {
    public static RecyclerView rv_main;
    public Context context;
    public LostAdapter lostAdapter;
    public static LinearLayoutManager linearLayoutManager_lost;
    private View view;
    private static int i;//初始查询8条数据
    private Boolean atbottom = true;
    public static List<Lost> losts = null;
    private SwipeRefreshLayout mWaveSwipeRefreshLayout;
    public String select_name = "";//查询  字符串
//    public Integer type = 0;// 筛选   0：全部  1:失物   2：招领

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lost, null);
        context = this.getActivity();
        rv_main = (RecyclerView) view.findViewById(R.id.rv_lost_main);
        linearLayoutManager_lost = new LinearLayoutManager(context);
        rv_main.setItemAnimator(new DefaultItemAnimator());
        rv_main.setLayoutManager(linearLayoutManager_lost);
        rv_main.addOnScrollListener(onScrollListener);
        MainActivity.setToolBarVisibale(true);
        i = 8;
        setSwipe();
        setDataCache();
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        setinter();
    }

    public void onResume() {
        super.onResume();
        // 在fragment中的调用用于记录页面访问信息
        MiStatInterface.recordPageStart(getActivity(), "失物招领");
    }

    public void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    /**
     * 继承接口
     */
    private void setinter() {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setFabClickedListener_cx(new MainActivity.OnFabClickedListener() {
            @Override
            public void onclicked(int num) {
                if (num == 1)
                    select();
            }
        });
//        mainActivity.setFabClickedListener_sx(new MainActivity.OnFabClickedListener2() {
//            @Override
//            public void onclicked(int num) {
//                if (num == 1)
//                    sx();
//            }
//        });
    }

//    /**
//     * 初始化bmob云
//     */
//    private void initBmob() {
//        BmobConfig config = new BmobConfig.Builder(context)
//                .setApplicationId(Config.ApplicationKey)
//                .setConnectTimeout(10)
//                .setUploadBlockSize(512 * 1024)
//                .setFileExpiration(5000)
//                .build();
//        Bmob.initialize(config);
//    }

    /**
     * 每一行的单击事件
     */
    LostAdapter.Itemclick itemclick = new LostAdapter.Itemclick() {

        public void OnItemclick(View v, int position) {
            Lost lost = losts.get(position);
            Intent intent = new Intent(context, Lost_Activity.class);
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            v.findViewById(R.id.iv_lost_pic), getString(R.string.transition_lost_img));
            intent.putExtra("Lost", lost);
            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    };


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
                    setnewData();
                    atbottom = false;
                }
            }
        }
    };

    /**
     * 查询数据
     */
    public void setData() {
        showrefresh(true);
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        query.order("-createdAt");
        query.setLimit(8);
        i = 8;
        if (!select_name.isEmpty()) {//如果通过查询添加限制
            query.addWhereContains("title", select_name);
        }
//        if (type == 1) {
//            query.addWhereEqualTo("type", false);
//        } else if (type == 2) {
//            query.addWhereEqualTo("type", true);
//        }
        query.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                showrefresh(false);
                if (e != null) {
                    Toast.show("查询失物招领出错：" + e.toString());
                    Log.d("查询失物招领出错", e.toString());
                    return;
                }
                losts = list;
                lostAdapter = new LostAdapter(losts, context, select_name);
                rv_main.setAdapter(lostAdapter);
                lostAdapter.setOnItemclicklister(itemclick);
                lostAdapter.notifyDataSetChanged();
                atbottom = true;
            }
        });
    }


    /**
     * 查询数据,缓存加载，是在第一次进入界面使用
     */
    public void setDataCache() {
        showrefresh(true);
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        query.order("-createdAt");
        query.setLimit(8);
        //判断是否有缓存，该方法必须放在查询条件（如果有的话）都设置完之后再来调用才有效，就像这里一样。
//        boolean isCache = query.hasCachedResult(Lost.class);
//        if (isCache) {//此为举个例子，并不一定按这种方式来设置缓存策略
//            query.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);    // 如果有缓存的话，则设置策略为CACHE_ELSE_NETWORK
//            Log.d("tip", "有缓存");
//        } else {
//            query.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ONLY);    // 如果没有缓存的话，则设置策略为NETWORK_ELSE_CACHE
//            Log.d("tip", "无缓存");
//        }
        query.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                showrefresh(false);
                if (e != null) {
                    Log.d("查询失物招领出错", e.toString());
                        Toast.show("查询出错(" + e.getErrorCode() + ")");
                    return;
                }
                losts = list;
                lostAdapter = new LostAdapter(losts, context, select_name);
                rv_main.setAdapter(lostAdapter);
                lostAdapter.setOnItemclicklister(itemclick);
                lostAdapter.notifyDataSetChanged();
                atbottom = true;
            }
        });
    }

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


    /**
     * 查询更多数据
     */
    public void setnewData() {
        showrefresh(true);
        BmobQuery<Lost> bmobQuery = new BmobQuery<Lost>();
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(8);
        bmobQuery.setSkip(i);
        if (!select_name.isEmpty()) {//如果通过查询添加限制
            bmobQuery.addWhereContains("title", select_name);
        }
//        if (type == 1) {
//            bmobQuery.addWhereEqualTo("type", false);
//        } else if (type == 2) {
//            bmobQuery.addWhereEqualTo("type", true);
//        }
        bmobQuery.findObjects(new FindListener<Lost>() {
            @Override
            public void done(List<Lost> list, BmobException e) {
                showrefresh(false);
                if (e != null) {
                    Toast.show("查询更多失物招领出错：" + e.toString());
                    Log.d("查询更多失物招领出错", e.toString());
                    return;
                }
                if (list.size() == 0) {
                    Toast.show("没有更多的内容啦~");
                    return;
                }
                Log.d("tip","losts长度2："  + losts.size());
                lostAdapter.notifyDataSetChanged(list);
                //我们发现执行notifydataserchanged之后自动完成了下面的事，真奇怪
//                list.clear();
//                for (Lost lost : list) {
//                    losts.add(lost);
//                    Log.d("tip","1111111111");
//                }
                Log.d("tip","losts长度："  + losts.size());
                atbottom = true;
                i = i + 8;
            }
        });
    }

    private void setSwipe() {
        mWaveSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.parseColor("#FF4081"));
        mWaveSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    setData();
                }
            }, 1200);
        }
    };

    /**
     * 筛选
     */
//    private void sx() {
//        final View layout = View.inflate(context, R.layout.dialog_lost,
//                null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("筛选");
//        builder.setView(layout, 100, 30, 0, 20);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                RadioButton radioButton1 = (RadioButton) layout.findViewById(R.id.dialog_rb_all);
//                RadioButton radioButton2 = (RadioButton) layout.findViewById(R.id.dialog_rb_sw);
//                RadioButton radioButton3 = (RadioButton) layout.findViewById(R.id.dialog_rb_zl);
//                if (radioButton1.isChecked()) {
//                    Toast.show("1");
//                    type = 0;
//                    setData();
//                } else if (radioButton2.isChecked()) {
//                    type = 1;
//                    setData();
//                } else if (radioButton3.isChecked()) {
//                    type = 2;
//                    setData();
//                }
//            }
//        });
//        builder.create().show();
//    }

    /**
     * 搜索
     */
    private void select() {
        final EditText editText = new EditText(context);
        editText.setHint("全部");
        editText.setText(select_name);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("搜索");
        builder.setView(editText, 80, 0, 80, 0);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_name = editText.getText().toString().trim();
                setData();
            }
        });
        builder.create().show();
    }
}
