package com.longer.school.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longer.school.modle.bean.Yellow;
import com.longer.school.R;
import com.longer.school.adapter.YellowAdapter;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Sqlite_selectActivity extends Activity {

    private EditText ed_select;
    private Context context;
    private LinearLayout ll;
    private TextView tv_tip;
    public static YellowAdapter yrAdapter;
    private static List<Yellow> yellows = null;
    public static LinearLayoutManager linearLayoutManager;
    private RecyclerView lv;
    public boolean atbottom = true;//到达底部，没有加载数据之前不会重复执行
    public static int i = 12;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sqlite_select);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        i = 12;
        context = Sqlite_selectActivity.this;
        ed_select = (EditText) findViewById(R.id.sqlite_select_et);
        lv = (RecyclerView) findViewById(R.id.recycle_yellow);
        ll = (LinearLayout) findViewById(R.id.sql_ll);
        tv_tip = (TextView) findViewById(R.id.sql_tv_tip);
        linearLayoutManager = new LinearLayoutManager(context);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.setLayoutManager(linearLayoutManager);
        lv.addOnScrollListener(onScrollListener);

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
                if (atbottom) {
                    atbottom = false;
                    getnewdatd();
                }
            }
        }
    };


    // 搜索
    public void onclick_select(View view) {
        if ("".equals(ed_select.getText().toString().trim())) {
            return;
        }
        lv.setVisibility(View.VISIBLE);
        ll.setVisibility(View.GONE);
        tv_tip.setVisibility(View.GONE);
        setdata();
    }
    /**
     * 每行单击事件
     */
    YellowAdapter.Itemclick clike = new YellowAdapter.Itemclick() {
        @Override
        public void OnItemclick(View view, int position) {
            Log.d("Click","PPPP");
            Yellow yellow = yellows.get(position);
            Intent intent = new Intent(context, Yellow_Activity.class);
            intent.putExtra("Yellow", yellow);
            startActivity(intent);
        }
    };
    public void setdata() {
        i = 12;
        BmobQuery<Yellow> eq1 = new BmobQuery<Yellow>();
        eq1.addWhereContains("tag", ed_select.getText().toString().trim());
        eq1.order("-updatedAt");
        eq1.setLimit(12);

        BmobQuery<Yellow> eq2 = new BmobQuery<Yellow>();
        eq2.addWhereContains("type", ed_select.getText().toString().trim());
        eq2.order("-updatedAt");
        eq2.setLimit(12);

        List<BmobQuery<Yellow>> andQuerys = new ArrayList<BmobQuery<Yellow>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);

        BmobQuery<Yellow> bmobQuery = new BmobQuery<Yellow>();
        bmobQuery.or(andQuerys);
        bmobQuery.findObjects(new FindListener<Yellow>() {
            @Override
            public void done(List<Yellow> list, BmobException e) {
                if (list != null) {
                    yellows = list;

//                    com.longer.school.utils.Toast.show("没有找到相关信息:" + list.size());
                    yrAdapter = new YellowAdapter(list, context);
                    lv.setAdapter(yrAdapter);
                    yrAdapter.setOnItemclicklister(clike);
                } else {
                    com.longer.school.utils.Toast.show("没有找到相关信息");
                }
            }
        });
    }



    public void getnewdatd() {
        BmobQuery<Yellow> bmobQuery = new BmobQuery<Yellow>();
        bmobQuery.addWhereContains("tag", ed_select.getText().toString().trim());
        bmobQuery.setLimit(10);
        Log.d("num", i + "#");
        bmobQuery.setSkip(i); // 忽略前12条数据（即第一页数据结果）
        bmobQuery.order("-updatedAt");
        bmobQuery.findObjects(new FindListener<Yellow>() {
            @Override
            public void done(List<Yellow> list, BmobException e) {
                if (list != null) {
                    if (list.size() == 0) {
                        com.longer.school.utils.Toast.show("没有更多的内容啦~");
                        return;
                    }
                    yrAdapter.notifyDataSetChanged(list);
                    for (Yellow yellow : list) {
                        yellows.add(yellow);
                    }
                    atbottom = true;
                    i = i + 10;
                } else {
                    com.longer.school.utils.Toast.show("加载失败~");
                }
            }
        });

    }

    public void onclick_sql1(View view) {
        ed_select.setText("计算机工程学院");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql2(View view) {
        ed_select.setText("老师");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql3(View view) {
        ed_select.setText("水厂");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql4(View view) {
        ed_select.setText("舍值班室");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql5(View view) {
        ed_select.setText("计算机");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql6(View view) {
        ed_select.setText("后勤部");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql7(View view) {
        ed_select.setText("保卫处");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql8(View view) {
        ed_select.setText("形势与政策");
        ed_select.setSelection(ed_select.getText().length());
    }

    public void onclick_sql9(View view) {
        ed_select.setText("体育");
        ed_select.setSelection(ed_select.getText().length());
    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "学校黄页");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
