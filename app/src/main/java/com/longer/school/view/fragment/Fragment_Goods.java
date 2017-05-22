package com.longer.school.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.adapter.GoodsAdapter;
import com.longer.school.modle.bean.Good;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.activity.Goods_Activity;
import com.longer.school.view.activity.MainActivity;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Axu on 2016/9/21.
 */
public class Fragment_Goods extends Fragment {
    private View view;
    public Context context;
    public static String select_name = "";//查询  字符串
    public TabLayout tabLayout;
    public Integer type = 0;// 筛选   0：全部  1:出售   2：求购

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_goods, null);
        context = getActivity();
        MainActivity.setToolBarVisibale(false);
        select_name = "";

        /////////////////////////
        mSectionsPagerAdapter = new SectionsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setBackgroundColor(Color.parseColor(FileTools.getshareString("refreshcolor")));

        ////////////////////////////

        return view;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private String[] kings = new String[]{"全部", "学习相关", "生活用品", "其它"};

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return PlaceholderFragment.newInstance(kings[position]);
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return kings[position];
        }
    }

    /*
     * 设置viewpage 数据
     */
    public static class PlaceholderFragment extends Fragment {

        private static final String Kinds = "section_kings";
        RecyclerView recyclerView;
        SwipeRefreshLayout mWaveSwipeRefreshLayout;
        GridLayoutManager gridLayoutManager;
        GoodsAdapter goodsAdapter;
        Boolean atbottom = true;//判断是否已经到达底部，防止多次得到新数据
        Context context = Application.getINSTANCE();
        List<Good> goods;
        String kind_name;
        int i;//初始查询8条数据

        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(String kinds_name) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putString(Kinds, kinds_name);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            i = 8;
            kind_name = getArguments().getString(Kinds);
            View rootView = inflater.inflate(R.layout.fragment_ff_goods, container, false);

            recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_goods_main);
            mWaveSwipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            gridLayoutManager = new GridLayoutManager(context, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.addOnScrollListener(onScrollListener);

            setDataCache();
            setSwipe();

            return rootView;
        }

        /**
         * 设置刷新动画
         */
        private void setSwipe() {
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
         * 查询数据,缓存加载，是在第一次进入界面使用
         */
        public void setDataCache() {
            showrefresh(true);
            Log.d("tip","select_name" + select_name);
            BmobQuery<Good> query = new BmobQuery<Good>();
            query.order("-createdAt");
            if (!"全部".equals(kind_name)) {
                query.addWhereEqualTo("kind", kind_name);
            }
            if (!select_name.isEmpty()) {//如果通过查询添加限制
                query.addWhereContains("title", select_name);
            }
            query.setLimit(8);
            query.findObjects(new FindListener<Good>() {
                @Override
                public void done(List<Good> list, BmobException e) {
                    showrefresh(false);
                    if (e != null) {
                        Log.d("查询失物招领出错", e.toString());
                        Toast.show("查询出错(" + e.getErrorCode() + ")");
                        return;
                    }
                    goods = list;
                    goodsAdapter = new GoodsAdapter(goods, context, "");
                    goodsAdapter.setOnItemclicklister(itemclick);
                    recyclerView.setAdapter(goodsAdapter);
                    goodsAdapter.notifyDataSetChanged();
                }
            });
        }

        /**
         * 查询数据或下拉刷新数据
         */
        public void setData() {
            showrefresh(true);
            BmobQuery<Good> query = new BmobQuery<Good>();
            query.order("-createdAt");
            if (!"全部".equals(kind_name)) {
                query.addWhereEqualTo("kind", kind_name);
            }
            query.setLimit(8);
            i = 8;
            if (!"".equals(select_name)) {//如果通过查询添加限制
                query.addWhereContains("title", select_name);
            }
            query.findObjects(new FindListener<Good>() {
                @Override
                public void done(List<Good> list, BmobException e) {
                    showrefresh(false);
                    if (e != null) {
                        Log.d("查询失物招领出错", e.toString());
                        Toast.show("查询出错(" + e.getErrorCode() + ")");
                        return;
                    }
                    goods = list;
                    goodsAdapter = new GoodsAdapter(goods, context, "");
                    goodsAdapter.setOnItemclicklister(itemclick);
                    recyclerView.setAdapter(goodsAdapter);
                    goodsAdapter.notifyDataSetChanged();
                    atbottom = true;
                }
            });
        }

        /**
         * 每一行的单击事件
         */
        GoodsAdapter.Itemclick itemclick = new GoodsAdapter.Itemclick() {
            public void OnItemclick(View v, int position) {
                Good good = goods.get(position);
                Intent intent = new Intent(context, Goods_Activity.class);
                ActivityOptionsCompat options =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                v.findViewById(R.id.iv_goods_show), getString(R.string.transition_goods_img));
                intent.putExtra("Good", good);
                ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
            }
        };
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
                        setnewData();
                        atbottom = false;
                    }
                }
            }
        };

        /**
         * 查询更多数据
         */
        public void setnewData() {
            Log.d("tip", "i == " + i);
            showrefresh(true);
            BmobQuery<Good> bmobQuery = new BmobQuery<Good>();
            if (!"全部".equals(kind_name)) {
                bmobQuery.addWhereEqualTo("kind", kind_name);
            }
            bmobQuery.order("-createdAt");
            bmobQuery.setLimit(8);
            bmobQuery.setSkip(i);
            if (!select_name.isEmpty()) {//如果通过查询添加限制
                bmobQuery.addWhereContains("title", select_name);
            }
            bmobQuery.include("seller");
            bmobQuery.findObjects(new FindListener<Good>() {
                @Override
                public void done(List<Good> list, BmobException e) {
                    showrefresh(false);
                    if (e != null) {
                        Log.d("查询失物招领出错", e.toString());
                        Toast.show("查询出错：(" + e.getErrorCode() + ")");
                        return;
                    }
                    if (list.size() == 0) {
                        Toast.show("没有更多的内容啦~");
                        return;
                    }
                    goodsAdapter.notifyDataSetChanged(list);//我们发现执行notifydataserchanged之后自动完成了下面的事，真奇怪
                    atbottom = true;
                    i = i + 8;
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
                        }
                    } else {
                        if (mWaveSwipeRefreshLayout.isRefreshing()) {
                            mWaveSwipeRefreshLayout.setRefreshing(false);
                        }
                    }
                }
            });
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    @Override
    public void onStart() {
        super.onStart();
        setinter();
    }

    public void onResume() {
        super.onResume();
        // 在fragment中的调用用于记录页面访问信息
        MiStatInterface.recordPageStart(getActivity(), "跳蚤市场");
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
                if (num == 0)
                    select();
            }
        });
    }


    /**
     * 筛选
     */
//    private void sx() {
//        final View layout = View.inflate(context, R.layout.dialog_goods,
//                null);
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setTitle("筛选");
//        builder.setView(layout, 100, 30, 0, 20);
//        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                RadioButton radioButton1 = (RadioButton) layout.findViewById(R.id.dialog_rb_all);
//                RadioButton radioButton2 = (RadioButton) layout.findViewById(R.id.dialog_rb_cs);
//                RadioButton radioButton3 = (RadioButton) layout.findViewById(R.id.dialog_rb_qg);
//                if (radioButton1.isChecked()) {
//                    Toast.show("1");
//                    type = 0;
//                } else if (radioButton2.isChecked()) {
//                    type = 1;
//                } else if (radioButton3.isChecked()) {
//                    type = 2;
//                }
////                setData();
//            }
//        });
//        builder.create().show();
//    }

    /**
     * 搜索商品
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
                if (!"".equals(select_name)) {
                    Toast.show("暂时不能查询");
                }
            }
        });
        builder.create().show();
    }

    /**
     * 查询数据
     */
//    public void setData() {
//        showrefresh(true);
//        BmobQuery<Good> query = new BmobQuery<Good>();
//        query.order("-createdAt");
//        query.setLimit(8);
////        i = 8;
//        if (!"".equals(select_name)) {//如果通过查询添加限制
//            query.addWhereContains("title", select_name);
//        }
//        if (type == 1) {
//            query.addWhereEqualTo("type", true);
//        } else if (type == 2) {
//            query.addWhereEqualTo("type", false);
//        }
//
//        query.findObjects(new FindListener<Good>() {
//            @Override
//            public void done(List<Good> list, BmobException e) {
//                showrefresh(false);
//                if (e != null) {
//                    Log.d("查询失物招领出错", e.toString());
//                    Toast.show("查询出错(" + e.getErrorCode() + ")");
//                    return;
//                }
//                goods = list;
//                goodsAdapter = new GoodsAdapter(goods, context, select_name);
////                goodsAdapter.setOnItemclicklister(itemclick);
//                recyclerView.setAdapter(goodsAdapter);
//                goodsAdapter.notifyDataSetChanged();
//                atbottom = true;
//            }
//        });
//    }

}
