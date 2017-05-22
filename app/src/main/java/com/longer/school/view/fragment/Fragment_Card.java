package com.longer.school.view.fragment;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.longer.school.modle.bean.CardClass;
import com.longer.school.R;
import com.longer.school.adapter.CardAdapter;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.TimeTools;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import jp.co.recruit_lifestyle.android.widget.WaveSwipeRefreshLayout;

public class Fragment_Card extends Fragment {

    private WaveSwipeRefreshLayout mWaveSwipeRefreshLayout;
    public static CardAdapter cardAdapter;
    public static LinearLayoutManager linearLayoutManager;
    private RecyclerView lv;
    private Context context;
    private static int num = 20;
    public boolean atbottom = true;//到达底部，没有加载数据之前不会重复执行
    //    private static String num_refresh = "";

    private final int handler_fall = 1;// 刷新失败,告诉控件
    private final int handler_success = 2;// 刷新成功，之后适配数据到lisi
    private final int autorefresh_fall = 3;// 自动刷新失败
    private final int autorefresh_success = 4;// 自动刷新成功
    private final int autorefresh_null = 5;// 消费记录为空

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (mWaveSwipeRefreshLayout.isRefreshing()) {
                mWaveSwipeRefreshLayout.setRefreshing(false);
            }
            switch (msg.what) {
                case handler_fall:
                    Toast.makeText(context, "刷新失败", Toast.LENGTH_SHORT).show();
                    break;
                case handler_success:
                    file_cardinfo(true);// 再次解析数据
//                    Toast.makeText(context, "刷新成功", Toast.LENGTH_SHORT).show();
                    break;
                case autorefresh_fall:
                    Toast.makeText(context, "刷新失败", Toast.LENGTH_SHORT).show();
                    mWaveSwipeRefreshLayout.setRefreshing(false);
                    break;
                case autorefresh_success:
                    file_cardinfo(false);// 再次解析数据
                    Toast.makeText(context, "自动刷新成功", Toast.LENGTH_SHORT).show();
                    break;
                case autorefresh_null:
                    Toast.makeText(context, "哥是土豪，最近没有消费记录", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_card, null);
        init();
        return view;
    }

    public void init() {
        num = 20;
        linearLayoutManager = new LinearLayoutManager(context);
        lv = (RecyclerView) view.findViewById(R.id.frag_card_lv);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.setLayoutManager(linearLayoutManager);
        lv.addOnScrollListener(onScrollListener);//添加滑动到底部自动刷新的操作

        setSwipe();
        context = getActivity();
        // 从文件读取缓存的信息
        file_cardinfo(false);
    }

    private void setSwipe() {
        mWaveSwipeRefreshLayout = (WaveSwipeRefreshLayout) view.findViewById(R.id.main_swipe);
        mWaveSwipeRefreshLayout.setColorSchemeColors(Color.WHITE, Color.WHITE);
        mWaveSwipeRefreshLayout.setWaveColor(Color.parseColor(FileTools.getshareString("refreshcolor")));
        mWaveSwipeRefreshLayout.setMaxDropHeight(1350);
        mWaveSwipeRefreshLayout.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 下拉刷新
     */
    WaveSwipeRefreshLayout.OnRefreshListener onRefreshListener = new WaveSwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            num = 20;
            new Thread() {
                public void run() {
                    Message mes = new Message();
                    int num = LoginService.refresh_card(context, "20");
                    if (num == 0)// 刷新数据
                    {// 刷新成功
                        mes.what = handler_success;
                    } else if (num == 1) {
                        mes.what = handler_fall;
                    } else if (num == 2) {
                        mes.what = autorefresh_null;
                    }
                    handler.sendMessage(mes);

                }
            }.start();
        }
    };

    /**
     * 从文件读取缓存的信息
     * add true 就是添加新数据，  false 就是设置适配器
     */
    public void file_cardinfo(boolean add) {
        System.out.println("读取缓存数据，开始解析数据");
        // 从文件读取缓存的信息
        String text = FileTools.getFile(context, "card.txt");
        // System.out.println(text);
        if ("".equals(text)) {// 读取文件失败
            autoRefresh();
            Toast.makeText(context, "正在准备数据哦~", Toast.LENGTH_SHORT).show();
            return;
        } else {
            try {
                JSONArray js = new JSONArray(text);
                int lo = js.length();
                System.out.println("一共有多少条记录" + lo);

                CardClass card = null;
                List<CardClass> list = new ArrayList<CardClass>();
                for (int i = 0; i < lo; i++) {
                    JSONObject jo = (JSONObject) js.opt(i);
                    card = new CardClass();

                    card.setDay(TimeTools.xinqi(jo.getString("FLW_CONTIME")));
                    card.setMoney(jo.getString("FLW_AMOUNT"));
                    card.setTime(jo.getString("FLW_CONTIME"));
                    card.setWhere(jo.getString("TRD_CNAME"));

                    list.add(card);
                    card = null;
                }
                JSONObject jo = (JSONObject) js.opt(0);
                String money;
                money = jo.getString("FLW_BALANCE");

                // 保存当前余额到到shareprefence
                FileTools.saveshare(context, "money", money);
                //如果是添加新数据
                if (add) {
                    cardAdapter.notifyDataSetChanged(list);
                    return;
                }
                cardAdapter = new CardAdapter(list);
                lv.setAdapter(cardAdapter);

            } catch (Exception e) {
                Toast.makeText(context, "产生异常了", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
                System.out.println("数据读取产生异常");
            }
        }
    }

    /**
     * recycleview 的滑倒底部监听事件
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visible = linearLayoutManager.getChildCount();
            int total = linearLayoutManager.getItemCount();
            int past = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if ((visible + past) >= total) {
                //得到更多的数据
                if (atbottom) {
                    atbottom = false;
                    adddate();
                }
            }
        }
    };

    /**
     * 上拉加载更多数据
     */
    public void adddate() {
        num += 14;
        mWaveSwipeRefreshLayout.setRefreshing(true);
        new Thread() {
            public void run() {
                Message mes = new Message();
                ;
                int num = LoginService.refresh_card(context, "20");
                if (num == 0)// 刷新数据
                {// 刷新成功
                    mes.what = handler_success;
                } else if (num == 1) {
                    mes.what = handler_fall;
                } else if (num == 2) {
                    mes.what = autorefresh_null;
                }
                atbottom = true;
                handler.sendMessage(mes);
            }

            ;
        }.start();
    }

    public void autoRefresh() {
        new Thread() {
            public void run() {
                Message mes = new Message();
                int num = LoginService.refresh_card(context, "20");
                if (num == 0)// 刷新数据
                {// 自动刷新成功
                    mes.what = autorefresh_success;
                } else if (num == 1) {// 自动刷新失败
                    mes.what = autorefresh_fall;
                } else if (num == 2) {
                    mes.what = autorefresh_null;
                }
                handler.sendMessage(mes);
            }

            ;
        }.start();
    }
}
