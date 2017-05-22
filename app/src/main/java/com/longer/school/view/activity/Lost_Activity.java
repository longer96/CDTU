package com.longer.school.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.longer.school.Application;
import com.longer.school.modle.bean.Lost;
import com.longer.school.R;
import com.longer.school.utils.CopyText;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.PublicTools;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Axu on 2016/9/18.
 */
public class Lost_Activity extends AppCompatActivity {
    public Context context;
    public ArrayList<String> urls = new ArrayList<String>();
    public Lost lost;
    private RollPagerView mRollViewPager;
    @Bind(R.id.tv_lost_title)
    TextView tvLostTitle;
    @Bind(R.id.tv_lost_infor)
    TextView tvLostInfor;
    @Bind(R.id.tv_lost_things)
    TextView tvLostThings;
    @Bind(R.id.tv_lost_where)
    TextView tvLostWhere;
    @Bind(R.id.tv_lost_name)
    TextView tvLostName;
    @Bind(R.id.tv_lost_complete)
    TextView tvLostComplete;
    @Bind(R.id.tv_lost_time)
    TextView tvLostTime;
    @Bind(R.id.tv_lost_linktel)
    TextView tvLostLinktel;
    @Bind(R.id.ll_lost_tel)
    LinearLayout llLostTel;
    @Bind(R.id.tv_lost_linkqq)
    TextView tvLostLinkqq;
    @Bind(R.id.ll_lost_qq)
    LinearLayout llLostQq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        requesttitle();
        setContentView(R.layout.activity_lost);
        ButterKnife.bind(this);
        context = Lost_Activity.this;
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);
        Intent intent = getIntent();
        lost = (Lost) intent.getSerializableExtra("Lost");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        String str = lost.isType() ? "招领详细" : "寻物详细";
        mCollapsingToolbarLayout.setTitle(str);
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
        setData();
        setpic();
    }

    /**
     * 设置图片轮显
     */
    public void setpic() {
        if (lost.getPic1() != null) {
            urls.add(lost.getPic1().getUrl());
        }
        if (lost.getPic2() != null) {
            urls.add(lost.getPic2().getUrl());
        }
        if (lost.getPic3() != null) {
            urls.add(lost.getPic3().getUrl());
        }
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(3500);
        //设置透明度
        mRollViewPager.setAnimationDurtion(500);
        //设置适配器
        mRollViewPager.setAdapter(new Lost_Activity.TestNormalAdapter());

        //设置指示器（顺序依次）
        //自定义指示器图片
        //设置圆点指示器颜色
        //设置文字指示器
        //隐藏指示器
        //mRollViewPager.setHintView(new IconHintView(this, R.drawable.point_focus, R.drawable.point_normal));
        mRollViewPager.setHintView(new ColorPointHintView(context, Color.parseColor("#FF4081"), Color.WHITE));
        //mRollViewPager.setHintView(new TextHintView(this));
        //mRollViewPager.setHintView(null);
    }

    private class TestNormalAdapter extends StaticPagerAdapter {
        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            //系统版本号  大于5.0才设置动画
            int i = Integer.parseInt(android.os.Build.VERSION.SDK);
            if (i >= 21) {
                view.setTransitionName("transition_pictures_img");
            }
            String url1 = urls.get(position);
            Glide.with(context).load(url1)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .placeholder(R.mipmap.imageselector_photo)
                    .error(R.mipmap.imageselector_photo)
                    .into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Picture_Activity.class);
                    intent.putExtra("url", urls);
                    intent.putExtra("position", position);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(Lost_Activity.this,
                                    v, getString(R.string.transition_pictures_img));
                    ActivityCompat.startActivity(Lost_Activity.this, intent, options.toBundle());

                }
            });
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return urls.size();
        }
    }

    /**
     * 将状态栏设置为透明
     */
    private void requesttitle() {
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                    | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            window.setNavigationBarColor(Color.TRANSPARENT);
        }
    }

    public void setData() {
        urls.clear();
        tvLostTitle.setText(lost.getTitle());
        tvLostInfor.setText(lost.getInfor());
        tvLostThings.setText(lost.getThing());
        tvLostWhere.setText(lost.getWhere());
        tvLostName.setText(lost.getName());
        tvLostTime.setText(TimeUtils.getFriendlyTimeSpanByNow(lost.getCreatedAt()) + "  发布");
//        ivUserotherAvatar.setImageResource(ali[new Random().nextInt(8)]);
        String str = lost.isComplete() ? "已找到" : "未找到";
        tvLostComplete.setText(str);

        if ("".equals(lost.getLink_tel()) || lost.getLink_tel() == null) {
            llLostTel.setVisibility(View.GONE);
        } else {
            tvLostLinktel.setText(lost.getLink_tel());
        }
        if ("".equals(lost.getLink_qq())) {
            llLostQq.setVisibility(View.GONE);
        } else {
            tvLostLinkqq.setText(lost.getLink_qq());
        }

//        if (lost.getPic1() != null) {
//            ivLostPic1.setVisibility(View.VISIBLE);
//            String url = lost.getPic1().getUrl();
//            urls.add(url);
//            Glide.with(context).load(url)
//                    .placeholder(R.mipmap.imageselector_photo)
//                    .error(R.mipmap.imageselector_photo)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(ivLostPic1);
//        }
//        if (lost.getPic2() != null) {
//            ivLostPic2.setVisibility(View.VISIBLE);
//            String url = lost.getPic2().getUrl();
//            urls.add(url);
//            Glide.with(context).load(url)
//                    .placeholder(R.mipmap.imageselector_photo)
//                    .error(R.mipmap.imageselector_photo)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(ivLostPic2);
//        }
//
//        if (lost.getPic3() != null) {
//            ivLostPic3.setVisibility(View.VISIBLE);
//            String url = lost.getPic3().getUrl();
//            urls.add(url);
//            Glide.with(context).load(url)
//                    .placeholder(R.mipmap.imageselector_photo)
//                    .error(R.mipmap.imageselector_photo)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(ivLostPic3);
//        }
    }

    /**
     * 三点菜单
     *
     * @param menu
     */
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_lost, menu);
        //判断是否为创建者，如果不是这隐藏菜单
        checkfather(menu);
        return true;
    }

    /**
     * 判断是否为创建者，如果不是这隐藏菜单
     */
    private void checkfather(Menu menu) {
        //为了保险起见，我们先将它设置为隐藏
        if (null != menu) {
            for (int i = 0; i < menu.size(); i++) {
                menu.getItem(i).setVisible(false);
                menu.getItem(i).setEnabled(false);
            }
        }
        String str = FileTools.getshareString("login");
        if ("true".equals(str)) {// 表示登录过
            if (lost.getUser() != null) {//如果为空直接隐藏
                String id = lost.getUser().getObjectId();
                if (Application.my != null && Application.my.getObjectId().equals(id)) {
                    if (null != menu) {
                        for (int i = 0; i < menu.size(); i++) {
                            menu.getItem(i).setVisible(true);
                            menu.getItem(i).setEnabled(true);
                        }
                    }
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_ok) {
            setcomplete();
        } else if (id == R.id.action_delete) {
            deletelost();
        }
        return super.onOptionsItemSelected(item);
    }

    public void setcomplete() {
        if (lost.isComplete()) {
            Toast.show("已经找到就不要再点人家啦~");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定已经找到了哇？");
        builder.setNegativeButton("肯定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Lost mlost = lost;
                mlost.setComplete(true);
                mlost.update(lost.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.show("修改成功,刷新后即可看到");
                        } else {
                            Toast.show("修改失败" + e.toString());
                        }
                    }
                });
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    public void deletelost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要删除该条消息吗？");
        builder.setNegativeButton("肯定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Lost mlost = new Lost();
                mlost.delete(lost.getObjectId(), new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Toast.show("删除成功,刷新后即可消失");
                            finish();
                        } else {
                            Toast.show("删除失败" + e.toString());
                        }
                    }
                });
            }
        });
        builder.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }

    @OnClick({R.id.tv_lost_copy, R.id.iv_lost_call})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_lost_copy:
                CopyText.settext(lost.getLink_qq());
                break;
            case R.id.iv_lost_call:
                PublicTools.call(lost.getLink_tel());
                break;
        }
    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "失物招领详细界面");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
