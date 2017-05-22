package com.longer.school.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
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
import com.longer.school.modle.bean.Good;
import com.longer.school.R;
import com.longer.school.utils.CopyText;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Axu on 2016/9/23.
 */
public class Goods_Activity extends AppCompatActivity {
    @Bind(R.id.tv_goods_name)
    TextView tvGoodsName;
    @Bind(R.id.tv_goods_linktel)
    TextView tvGoodsLinktel;
    @Bind(R.id.tv_goods_linkqq)
    TextView tvGoodsLinkqq;
    @Bind(R.id.tv_goods_title)
    TextView tvGoodsTitle;
    @Bind(R.id.tv_goods_price)
    TextView tvGoodsPrice;
    @Bind(R.id.tv_goods_info)
    TextView tvGoodsInfo;
    @Bind(R.id.ll_goods_tel)
    LinearLayout llGoodsTel;
    @Bind(R.id.ll_goods_qq)
    LinearLayout llGoodsQq;
    @Bind(R.id.tv_goods_knife)
    TextView tvGoodsKnife;
    @Bind(R.id.tv_goods_complete)
    TextView tvGoodsComplete;
    @Bind(R.id.tv_goods_time)
    TextView tvGoodsTime;
    private Good good;
    public ArrayList<String> urls = new ArrayList<String>();
    private RollPagerView mRollViewPager;
    private Context context;
    public ImageView view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        requesttitle();
        setContentView(R.layout.activity_goods);
        ButterKnife.bind(this);
        mRollViewPager = (RollPagerView) findViewById(R.id.roll_view_pager);
        context = Goods_Activity.this;
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        good = (Good) intent.getSerializableExtra("Good");
        settitle();
        setpic();
        setData();
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

    private void settitle() {
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("商品详细");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
    }

    public void setData() {
        llGoodsQq.setVisibility(View.GONE);
        llGoodsTel.setVisibility(View.GONE);
        tvGoodsPrice.setText(good.getPrice());
        tvGoodsInfo.setText(good.getInfor());
        tvGoodsTitle.setText(good.getTitle());
        tvGoodsName.setText(good.getName());
        tvGoodsTime.setText(TimeUtils.getFriendlyTimeSpanByNow(good.getCreatedAt()));
        if (!good.isComplete()) {
            tvGoodsComplete.setVisibility(View.GONE);
        }
        if (!good.isKnife()) {
            tvGoodsKnife.setVisibility(View.GONE);
        }
        if (!good.getLink_tel().equals("")) {
            llGoodsTel.setVisibility(View.VISIBLE);
            tvGoodsLinktel.setText(good.getLink_tel());
        }
        if (!good.getLink_qq().equals("")) {
            llGoodsQq.setVisibility(View.VISIBLE);
            tvGoodsLinkqq.setText(good.getLink_qq());
        }
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
            if (good.getSeller() != null) {//如果为空直接隐藏
                String id = good.getSeller().getObjectId();
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

    /**
     * 设置该条消息为完成
     */
    public void setcomplete() {
        if (good.isComplete()) {
            Toast.show("已经找到就不要再点人家啦~");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定已经交易完成了吗？");
        builder.setNegativeButton("肯定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Good mlost = good;
                mlost.setComplete(true);
                mlost.update(good.getObjectId(), new UpdateListener() {
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

    /**
     * 删除该条消息
     */
    public void deletelost() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("确定要删除该条消息吗？");
        builder.setNegativeButton("肯定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Good mlost = new Good();
                mlost.delete(good.getObjectId(), new UpdateListener() {
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


    /**
     * 设置图片轮显
     */
    public void setpic() {
        if (good.getPic1() != null) {
            urls.add(good.getPic1().getUrl());
        }
        if (good.getPic2() != null) {
            urls.add(good.getPic2().getUrl());
        }
        if (good.getPic3() != null) {
            urls.add(good.getPic3().getUrl());
        }
        //设置播放时间间隔
        mRollViewPager.setPlayDelay(3500);
        mRollViewPager.setAnimationDurtion(500);
        mRollViewPager.setAdapter(new TestNormalAdapter());

        mRollViewPager.setHintView(new ColorPointHintView(context, Color.parseColor("#FF4081"), Color.WHITE));
    }

    @OnClick({R.id.iv_goods_call, R.id.tv_goods_copy})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_goods_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + good.getLink_tel()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.tv_goods_copy:
                CopyText.settext(good.getLink_qq());
                break;
        }
    }

    //
//    "手机型号: " + android.os.Build.MODEL + ",\nSDK版本:"
//            + android.os.Build.VERSION.SDK + ",\n系统版本:"
//            + android.os.Build.VERSION.RELEASE
    private class TestNormalAdapter extends StaticPagerAdapter {
        @Override
        public View getView(ViewGroup container, final int position) {

            view = new ImageView(container.getContext());
            //系统版本号  大于5.0才设置动画
            int i = Integer.parseInt(Build.VERSION.SDK);
            if (i >= 21) {
                view.setTransitionName("transition_pictures_img");
            }
            String url1 = urls.get(position);
            Glide.with(context).load(url1)
                    .error(R.mipmap.imageselector_photo)
                    .into(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Picture_Activity.class);
                    intent.putExtra("url", urls);
                    intent.putExtra("position", position);
                    ActivityOptionsCompat options =
                            ActivityOptionsCompat.makeSceneTransitionAnimation(Goods_Activity.this,
                                    v, getString(R.string.transition_pictures_img));
                    ActivityCompat.startActivity(Goods_Activity.this, intent, options.toBundle());

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

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "跳蚤市场商品详细");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
