package com.longer.school.view.activity.zfxt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.modle.bean.CourseClass;
import com.longer.school.R;
import com.longer.school.R.id;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.StreamTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Score_kbcxActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Context context;
    private Spinner spin_xn;// 学年 2015-2016
    private Spinner spin_xq;// 1.2 学期
    private Spinner spin_nj;// 2014 2015 年级
    private Spinner spin_xy;// 学院 计算机工程学院
    private Spinner spin_zy;// 专业 软件设计
    private Spinner spin_bj;// 班级 1405173

    private final int success = 1;// 成功
    private final int fish = 2;// 失败
    private final int course_success = 3;// 获取课表成功
    private final int course_fish = 4;// 获取课表失败
    private ProgressDialog pg;
    private RotateAnimation refreshingAnimation; // 均匀旋转动画
    private ImageView refreshingView;// 刷新图片
    private String content;//默认数据返回的网页源码

    private Map<String, String> map_xy = null;
    private Map<String, String> map_zy = null;
    private Map<String, String> map_bj = null;


    private String[] xn = null;
    private String[] xq = null;
    private String[] nj = null;

//    private boolean lister = false;//设置了监听将不会再次设置

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case success:
                    refresh(false);
                    setview();//解析数据，将其放入spiner
                    break;
                case fish:
                    refresh(false);
                    Toast.makeText(context, "啊呀，获取数据出问题鸟~", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_score_kbcx);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("课表查询");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        context = Score_kbcxActivity.this;

        spin_xn = (Spinner) findViewById(id.score_spinner_xn);
        spin_xq = (Spinner) findViewById(id.score_spinner_xq);
        spin_nj = (Spinner) findViewById(id.score_spinner_nj);
        spin_xy = (Spinner) findViewById(id.score_spinner_xy);
        spin_zy = (Spinner) findViewById(id.score_spinner_zy);
        spin_bj = (Spinner) findViewById(id.score_spinner_bj);
        Button btn = (Button) findViewById(id.btn_kbcx_cx);
        btn.setOnClickListener(onClickListener);

        refreshingView = (ImageView) findViewById(id.activity_course_refreshing_icon);

        initdata();// 初始化数据
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String bj = spin_bj.getSelectedItem().toString();
            if ("".equals(bj)) {
                com.longer.school.utils.Toast.show("没有相关班级信息");
                return;
            }
            // 主线程获取课表
            List<CourseClass> courses = StreamTools.getcourse(context, content);
            StringBuffer sb = new StringBuffer();
            for (CourseClass info : courses) {
                sb.append(info.getBg());
                sb.append("#");
                sb.append(info.getId());
                sb.append("#");
                sb.append(info.getName());
                sb.append("#");
                sb.append(info.getRoom());
                sb.append("#");
                sb.append(info.getTeacher());
                sb.append("#");
                sb.append(info.getZoushu());
                sb.append("@");
            }
            String text = sb.toString().substring(0, sb.length() - 1);
            Log.d("课程end", text);
            Intent intent = new Intent(context, Score_courseActivity.class);
            intent.putExtra("course", text);
            intent.putExtra("bj", bj);
            startActivity(intent);
        }
    };

    /**
     * 是否显示刷新图片，true 为显示，false为不显示
     *
     * @param show
     */
    public void refresh(boolean show) {
        if (show) {
            // 添加匀速转动动画
            refreshingAnimation = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.rotating);
            LinearInterpolator lir = new LinearInterpolator();
            refreshingAnimation.setInterpolator(lir);
            refreshingView.startAnimation(refreshingAnimation);
            refreshingView.setVisibility(View.VISIBLE);
        } else {
            refreshingView.clearAnimation();
            refreshingView.setVisibility(View.GONE);
        }
    }

    private void setview() {
        map_xy = new HashMap<String, String>();
        map_zy = new HashMap<String, String>();
        map_bj = new HashMap<String, String>();
        try {
            Document doc = Jsoup.parse(content);
            Elements eles = doc.select("table[id=Table1] tbody tr");
            int select = 0;//得到选中的行

            //解析学年
            Elements ele4 = eles.get(0).select("td").get(0).select("option");
            xn = new String[ele4.size()];
            for (int i = 0; i < ele4.size(); i++) {
                if (ele4.get(i).toString().contains("sele")) {
                    select = i;
                }
                xn[i] = ele4.get(i).text();
            }
            ArrayAdapter<String> adapter_xn = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, xn);
            adapter_xn.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_xn.setAdapter(adapter_xn);
            spin_xn.setSelection(select, true);


            //解析学期
            Elements ele5 = eles.get(0).select("td").get(1).select("option");
            xq = new String[ele5.size()];
            for (int i = 0; i < ele5.size(); i++) {
                if (ele5.get(i).toString().contains("sele")) {
                    select = i;
                }
                xq[i] = ele5.get(i).text();
            }
            ArrayAdapter<String> adapter_xq = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, xq);
            adapter_xq.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_xq.setAdapter(adapter_xq);
            spin_xq.setSelection(select, true);


            //解析年级
            Elements ele6 = eles.get(0).select("td").get(2).select("option");
            nj = new String[ele6.size()];
            for (int i = 0; i < ele6.size(); i++) {
                if (ele6.get(i).toString().contains("sele")) {
                    select = i;
                }
                nj[i] = ele6.get(i).text();
            }
            ArrayAdapter<String> adapter_nj = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, nj);
            adapter_nj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_nj.setAdapter(adapter_nj);
            spin_nj.setSelection(select, true);


            // 解析学院
            Elements ele = eles.get(1).select("td").get(0).select("option");
            String[] xy = new String[ele.size()];
            for (int i = 0; i < ele.size(); i++) {
                if (ele.get(i).toString().contains("sele")) {
                    select = i;
                }
                map_xy.put(ele.get(i).text(), ele.get(i).attr("value"));
                xy[i] = ele.get(i).text();
            }
            ArrayAdapter<String> adapter_xy = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, xy);
            adapter_xy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_xy.setAdapter(adapter_xy);
            spin_xy.setSelection(select, true);

            // 解析专业
            Elements ele2 = eles.get(1).select("td").get(1).select("option");
            String[] zy = new String[ele2.size()];
            for (int i = 0; i < ele2.size(); i++) {
                if (ele2.get(i).toString().contains("sele")) {
                    select = i;
                }
                map_zy.put(ele2.get(i).text(), ele2.get(i).attr("value"));
                zy[i] = ele2.get(i).text();
            }
            ArrayAdapter<String> adapter_zy = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, zy);
            adapter_zy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_zy.setAdapter(adapter_zy);
            spin_zy.setSelection(select, true);


            // 解析班级
            Elements ele3 = eles.get(1).select("td").get(2).select("option");
            String[] bj = new String[ele3.size()];
            for (int i = 0; i < ele3.size(); i++) {
                if (ele3.get(i).toString().contains("sele")) {
                    select = i;
                }
                map_bj.put(ele3.get(i).text(), ele3.get(i).attr("value"));
                bj[i] = ele3.get(i).text();
            }
            ArrayAdapter<String> adapter_bj = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, bj);
            adapter_bj.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_bj.setAdapter(adapter_bj);
            spin_bj.setSelection(select, true);

            spin_xn.setOnItemSelectedListener(this);
            spin_xq.setOnItemSelectedListener(this);
            spin_nj.setOnItemSelectedListener(this);
            spin_xy.setOnItemSelectedListener(this);
            spin_zy.setOnItemSelectedListener(this);
            spin_bj.setOnItemSelectedListener(this);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "啊呀，获取数据出问题鸟~", Toast.LENGTH_SHORT).show();
        }
    }

    public void initdata() {
        refresh(true);
        //获取数据
        new Thread() {
            @Override
            public void run() {
                super.run();
                content = LoginService.get_score_course_bj();
                Message mes = null;
                if (content != null) {
                    mes = new Message();
                    mes.what = success;
                } else {
                    mes = new Message();
                    mes.what = fish;
                }
                handler.sendMessage(mes);
            }
        }.start();


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d("tip", "改变了一次；");
        refresh(true);
        String type = null;
        switch (view.getId()) {
            case R.id.score_spinner_xn:
                type = "xn";
                break;
            case R.id.score_spinner_xq:
                type = "xq";
                break;
            case R.id.score_spinner_nj:
                type = "nj";
                break;
            case R.id.score_spinner_xy:
                type = "xy";
                break;
            case R.id.score_spinner_zy:
                type = "zy";
                break;
            case R.id.score_spinner_bj:
                type = "kb";
                break;
        }
        final String type2 = type;

        spin_xn.setOnItemSelectedListener(null);
        spin_xq.setOnItemSelectedListener(null);
        spin_nj.setOnItemSelectedListener(null);
        spin_xy.setOnItemSelectedListener(null);
        spin_zy.setOnItemSelectedListener(null);
        spin_bj.setOnItemSelectedListener(null);

        //获取数据
        new Thread() {
            @Override
            public void run() {
                super.run();
//                Log.d("选中", spin_xy.getSelectedItem().toString());
                String xy = map_xy.get(spin_xy.getSelectedItem().toString());
                String zy = map_zy.get(spin_zy.getSelectedItem().toString());
                String kb = map_bj.get(spin_bj.getSelectedItem().toString());
                Log.d("111", "xy:" + xy + "   zy:" + zy + "  bj:" + kb);
                //学年，学期，年级不用键值对
                content = LoginService.get_score_course_kb(type2, spin_xn.getSelectedItem().toString(), spin_xq.getSelectedItem().toString(),
                        spin_nj.getSelectedItem().toString(), xy, zy, kb);
                Message mes = null;
                if (content != null) {
                    mes = new Message();
                    mes.what = success;
                } else {
                    mes = new Message();
                    mes.what = fish;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "正方系统_课表查询");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
