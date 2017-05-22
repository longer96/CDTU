package com.longer.school.view.activity.zfxt;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.modle.bean.ScoreClass;
import com.longer.school.R;
import com.longer.school.adapter.Score_zxcjAdapter;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.StreamTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score_zxcjActivity extends AppCompatActivity {

    ExpandableListView mainlistview = null;
    List<String> parent = null;
    List<String> parent_score = null;
    Map<String, List<String>> map = null;
    private Context context;
    private ProgressDialog pg;
    private TextView tv;
    private final int success = 1;// 成功
    private final int fish = 2;// 失败
    private List<ScoreClass> scores;
    private BottomSheetDialog mBottomSheetDialog;
    private String[] xn;//动态获取的学年
    private RecyclerView recyclerView;
    private boolean first;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if(pg.isShowing()){
                pg.dismiss();
            }
            switch (msg.what) {
                case success:
                    if(first){
                        Toast.makeText(context, "列表课展开查看详细哦~", Toast.LENGTH_SHORT).show();
                        first = false;
                    }
                    initdata();
                    setdialogdata();
                    break;
                case fish:
                    Toast.makeText(context, "咦，获取成绩失败鸟~", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_score_zxcj);

        mainlistview = (ExpandableListView) this.findViewById(R.id.score_expandablelist);
        tv = (TextView) this.findViewById(R.id.score_zxcj_tv_xq);
        tv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showdialog();
            }
        });

        context = Score_zxcjActivity.this;
        createBottomSheetDialog();
        init();

    }


    // 初始化数据
    public void init() {
        first = true;//是否是刚进入界面
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("在校成绩");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        // 先一键登录正方系统
        pg = new ProgressDialog(context);
        pg.setMessage("正在获取成绩...");
        pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
        pg.show();
        new Thread() {
            public void run() {
                Message mes = new Message();
                String str = LoginService.get_score_all(context);
                if (str != null) {
                    scores = StreamTools.getscore(str);
                    xn = StreamTools.getscorexn(str);
                    mes.what = success;
                } else {
                    mes.what = fish;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }

    private void showdialog() {
        if (mBottomSheetDialog.isShowing()) {
            mBottomSheetDialog.dismiss();
        } else {
            mBottomSheetDialog.show();
        }
    }

    public void createBottomSheetDialog() {
        mBottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottomsheet_zxcj, null, false);
        mBottomSheetDialog.setContentView(view);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_zxcj);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setBehaviorCallback();
    }

    /**
     * 设置学年的数据
     */
    public void setdialogdata(){
        List<String> list = new ArrayList<>();

        list.add("历年成绩");
        for(int i=0; i<xn.length; i++){
            list.add(xn[i] + "  第2学期");
            list.add(xn[i] + "  第1学期");
        }

        Score_zxcjAdapter adapter = new Score_zxcjAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemclicklister(new Score_zxcjAdapter.Itemclick() {
            @Override
            public void Onclick(View view, String xn) {
                showdialog();
                tv.setText(xn);
                if("历年成绩".equals(xn)) {
                    init();
                    return;
                }

                pg = new ProgressDialog(context);
                pg.setMessage("正在获取成绩...");
                pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
                pg.show();
//                Log.d("tip","学年："  + xn.split("  ")[0].toString() + "  学期：" + xn.split("第")[1].split("学")[0].toString());
                get_score(xn.split("  ")[0].toString(), xn.split("第")[1].split("学")[0].toString());

            }
        });
    }

    private void setBehaviorCallback() {
        View view = mBottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetDialog.dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    //适配数据
    private void initdata() {
        // Log.d("链表长度：", scores.size() + "");
        parent = new ArrayList<String>();
        parent_score = new ArrayList<String>();
        for (int i = 0; i < scores.size(); i++) {
            parent.add(scores.get(i).getName());
            parent_score.add(scores.get(i).getCj_zp());
        }

        // Log.d("链表长度2：", parent.size() + "");

        map = new HashMap<String, List<String>>();

        List<String> list = null;
        for (int i = 0; i < scores.size(); i++) {
            list = new ArrayList<String>();

            list.add("学年学期：" + scores.get(i).getXn() + "第" + scores.get(i).getXq() + "学期");
            list.add("课程性质：" + scores.get(i).getSx());
            list.add("开课学院：" + scores.get(i).getXy());
            list.add("学分：" + scores.get(i).getXf());
            list.add("绩点：" + scores.get(i).getJd());

            list.add("     平时成绩：" + scores.get(i).getCj_ps());
            list.add("     期中成绩：" + scores.get(i).getCj_qz());
            list.add("     期末成绩：" + scores.get(i).getCj_qm());
            list.add("     实验成绩：" + scores.get(i).getCj_sy());
            list.add("     总评成绩：" + scores.get(i).getCj_zp());

            if ("重修".equals(scores.get(i).getCx())) {
                list.add("\t\t\t\t\t\t备注： 重修");
            }

            map.put(scores.get(i).getName(), list);
            list = null;
        }
        mainlistview.setAdapter(new MyAdapter());
    }


    class MyAdapter extends BaseExpandableListAdapter {

        // 得到子item需要关联的数据
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            String key = parent.get(groupPosition);
            return (map.get(key).get(childPosition));
        }

        // 得到子item的ID
        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        // 设置子item的组件
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView,
                                 ViewGroup parent2) {
            String key = parent.get(groupPosition);
            String info = map.get(key).get(childPosition);
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_score_zxcj_children, null);
            }
            TextView tv = (TextView) convertView.findViewById(R.id.second_textview);
            tv.setText(info);
            return tv;
        }

        // 获取当前父item下的子item的个数
        @Override
        public int getChildrenCount(int groupPosition) {
            String key = parent.get(groupPosition);
            int size = map.get(key).size();
            return size;
        }

        // 获取当前父item的数据
        @Override
        public Object getGroup(int groupPosition) {
            return parent.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return parent.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        // 设置父item组件
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent2) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.layout_score_zxcj_parent, null);
            }
            RelativeLayout ly = (RelativeLayout) convertView.findViewById(R.id.score_zxcj_linear);

            TextView tv1 = (TextView) convertView.findViewById(R.id.score_parent_tv1);
            TextView tv2 = (TextView) convertView.findViewById(R.id.score_parent_tv2);
            tv1.setText(parent.get(groupPosition));
            tv2.setText(parent_score.get(groupPosition));
            return ly;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    }

    // 查询成绩
    public void get_score(final String xn, final String xq) {
        new Thread() {
            public void run() {
                Message mes = new Message();
                String str = LoginService.get_score_xq(context, xn, xq);
                if (str != null) {
                    scores = StreamTools.getscore(str);
                    mes.what = success;
                } else {
                    mes.what = fish;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }
//
//    @Override
//    public void onClick(View v) {
//        mPopWindows.dismiss();
//        int id = v.getId();
//        String xn = null;// 学期
//        String xq = null;// 学年
//        switch (id) {
//            case R.id.score_xq1: {
//                if (!tv.getText().equals("2014-2015秋季学期")) {
//                    tv.setText("2014-2015秋季学期");
//                    xn = "2014-2015";
//                    xq = "1";
//                }
//                break;
//            }
//            case R.id.score_xq2: {
//                if (!tv.getText().equals("2014-2015春季学期")) {
//                    tv.setText("2014-2015春季学期");
//                    xn = "2014-2015";
//                    xq = "2";
//                }
//                break;
//            }
//            case R.id.score_xq3: {
//                if (!tv.getText().equals("2015-2016秋季学期")) {
//                    tv.setText("2015-2016秋季学期");
//                    xn = "2015-2016";
//                    xq = "1";
//                }
//                break;
//            }
//            case R.id.score_xq4: {
//                if (!tv.getText().equals("2015-2016春季学期")) {
//                    tv.setText("2015-2016春季学期");
//                    xn = "2015-2016";
//                    xq = "2";
//                }
//                break;
//            }
//            case R.id.score_xq5: {
//                if (!tv.getText().equals("历年成绩")) {
//                    tv.setText("历年成绩");
//                    init();
//                }
//                break;
//            }
//        }
//        if (xn != null) {
//            pg = new ProgressDialog(context);
//            pg.setMessage("正在获取成绩...");
//            pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
//            pg.show();
//            get_score(xn, xq);
//        }
//    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "正方系统_在校成绩");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}