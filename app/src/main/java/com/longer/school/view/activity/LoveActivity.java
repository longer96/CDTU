package com.longer.school.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.longer.school.Application;
import com.longer.school.R;
import com.blankj.utilcode.utils.TimeUtils;
import com.longer.school.modle.bean.Appinfor;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.User;
import com.longer.school.modle.biz.UserBiz;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.Toast;
import com.shitou.googleplay.lib.randomlayout.StellarMap;
import com.shitou.googleplay.lib.randomlayout.StellarMap.Adapter;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class LoveActivity extends Activity {
    private StellarMap stellarMap;
    private ArrayList<String> list_name = new ArrayList<String>();//表白对象的名字
    private List<Love> list_object = null;//查询得到的表白对象
    private String color[] = {"#7dfaed", "#ffb8d5", "#ffda80", "#88adf9", "#ff00ff", "#e4007f", "#00CD66", "#A020F0", "#90EE90"};
    public static LoveActivity instance = null;//暴露给其他位置关闭主界面
    private String[] colors_pop = {"#01a3a1", "#91bbeb", "#ff01b1bf", "#ff9d62", "#2d3867", "#ee697e"};//颜色组
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_love);
        instance = LoveActivity.this;
        context = LoveActivity.this;

        findViewById(R.id.fab_love).setOnClickListener(listener_bb);
        getdata();
        //用来检查用户表的性别以及pwd是否为空（之后可以删除 2017-4-17）
        getsex();
    }

    private void getsex() {
        new Thread() {
            public void run() {
                try {
                    if (Application.my != null && Application.my.getSex() == null) {
                        LoginService.updatastudent(context, Application.my.getUsername());
                        new UserBiz().upDateUserInfor(Application.my.getObjectId());
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 得到数据
     */
    private void getdata() {
        list_name.clear();
        BmobQuery<Love> bmobQuery = new BmobQuery<Love>();
        bmobQuery.order("-createdAt");
        bmobQuery.setLimit(90);
        bmobQuery.findObjects(new FindListener<Love>() {
            @Override
            public void done(List<Love> list, BmobException e) {
                if (e != null) {
                    Toast.show("查询出错：" + e.toString());
                    Log.d("查询表白墙出错", e.toString());
                    return;
                }
//                Log.d("TAG", "done: " + list.size());
                list_object = list;
                for (Love love : list) {
                    list_name.add(love.getObject());
                }
                setmap();
            }
        });
    }

    /**
     * 设置数据
     */
    private void setmap() {
        stellarMap = new StellarMap(this);
        // 1.设置内部的TextView距离四周的内边距
        int padding = 15;
        stellarMap.setInnerPadding(padding, padding, padding, 240);
        stellarMap.setAdapter(new StellarMapAdapter());
        // 设置默认显示第几组的数据
        stellarMap.setGroup(0, true);// 这里默认显示第0组
        // 设置x和y方向上的显示的密度
        stellarMap.setRegularity(15, 15);// 如果值设置的过大，有可能造成子View摆放比较稀疏

        // 把fragment显示至界面,new出fragment对象
        FrameLayout fl = (FrameLayout) findViewById(R.id.fl);
        fl.addView(stellarMap);
    }


    /**
     * fab 的发布 点击事件
     */
    View.OnClickListener listener_bb = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            String str = FileTools.getshareString("login");
//            if ("true".equals(str)) {// 表示已经成功登录过{
//                startActivity(new Intent(LoveActivity.this, Add_loveActivity.class));
//            } else {
//                Toast.show("大兄弟，您还没有登录");
//            }
            //先可以进行匿名的表白
            startActivity(new Intent(LoveActivity.this, Add_loveActivity.class));
        }
    };


    class StellarMapAdapter implements Adapter {
        /**
         * 返回多少组数据
         */
        @Override
        public int getGroupCount() {
            if (list_name.size() <= 15) {
                return 1;
            } else {
                int group = list_name.size() / 15;
//                System.out.println(group + "组");
                return group;
            }
        }

        /**
         * 每组多少个数据
         */
        @Override
        public int getCount(int group) {
            return list_name.size() >= 15 ? 15 : list_name.size();
        }

        /**
         * group: 当前是第几组 position:是当前组的position
         */
        @Override
        public View getView(int group, final int position, View convertView) {
            final TextView textView = new TextView(LoveActivity.this);
            // 根据group和组中的position计算出对应的在list中的位置
            int listPosition = group * getCount(group) + position;
            textView.setText(list_name.get(listPosition));

            // 1.设置随机的字体大小(随机大小)
            Random random = new Random();
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,
                    random.nextInt(8) + 18);// 14-29
            textView.setTextColor(Color.parseColor(color[position % 8]));
            textView.setOnClickListener(onclicklistener(15 * group + position));
            return textView;
        }

        View.OnClickListener onclicklistener(final int position) {
            return new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Love love = list_object.get(position);
                    View contentView = LayoutInflater.from(LoveActivity.this).inflate(R.layout.popup_love, null);
                    final PopupWindow mPopWindows = new PopupWindow(contentView);
                    mPopWindows.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
                    mPopWindows.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                    mPopWindows.setFocusable(true);
                    mPopWindows.setBackgroundDrawable(new BitmapDrawable());

                    TextView tv_name = (TextView) contentView.findViewById(R.id.tv_love_user);
                    TextView tv_object = (TextView) contentView.findViewById(R.id.tv_love_object);
                    TextView tv_content = (TextView) contentView.findViewById(R.id.tv_love_content);
                    TextView tv_time = (TextView) contentView.findViewById(R.id.tv_love_time);
                    TextView tv_like = (TextView) contentView.findViewById(R.id.tv_loveone_like);
                    TextView tv_comment = (TextView) contentView.findViewById(R.id.tv_loveone_comment);
                    CardView cardView = (CardView) contentView.findViewById(R.id.card_poplove);
                    cardView.setBackgroundColor(Color.parseColor(colors_pop[position % 6]));

                    tv_name.setText(love.getName());
                    tv_object.setText(love.getObject());
                    tv_content.setText(love.getContent());
                    tv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(love.getCreatedAt()));
                    if (love.getLike() != null) {
                        tv_like.setText("赞" + love.getLike());
                    }
                    if (love.getCommentnum() != null && love.getCommentnum() != 0) {
                        tv_comment.setText("评论" + love.getCommentnum());
                    }
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toLoveoneActivity(love, mPopWindows, colors_pop[position % 6]);
                        }
                    });
                    contentView.findViewById(R.id.ll_poplove).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mPopWindows.dismiss();
                        }
                    });
                    contentView.findViewById(R.id.card_poplove2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toLoveoneActivity(love, mPopWindows, colors_pop[position % 6]);
                        }
                    });
                    contentView.findViewById(R.id.sb_poploveone_comment).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toLoveoneActivity(love, mPopWindows, colors_pop[position % 6]);
                        }
                    });
                    contentView.findViewById(R.id.sb_poploveone_like).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            toLoveoneActivity(love, mPopWindows, colors_pop[position % 6]);
                        }
                    });

                    mPopWindows.showAtLocation(contentView, Gravity.CENTER, 0, 0);
                }

            };
        }


        /**
         * 虽然定义了，但是并没有什么乱用
         */
        @Override
        public int getNextGroupOnPan(int group, float degree) {
            return 0;
        }

        /**
         * 当前组缩放完成之后下一组加载哪一组的数据 group： 表示当前是第几组
         */
        @Override
        public int getNextGroupOnZoom(int group, boolean isZoomIn) {
            Log.d("TGP", "getNextGroupOnZoom: " + group);
            // 0->1->2->0
            return (group + 1) % getGroupCount();
        }

    }

    private void toLoveoneActivity(Love love, PopupWindow mPopWindows, String bg) {
        Intent intent = new Intent(LoveActivity.this, LoveOne_ActivityView.class);
        intent.putExtra("Love", love);
        intent.putExtra("bg", bg);
        startActivity(intent);
        mPopWindows.dismiss();
    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "表白墙");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

}
