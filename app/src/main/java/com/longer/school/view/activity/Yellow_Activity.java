package com.longer.school.view.activity;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.longer.school.Application;
import com.longer.school.modle.bean.Yellow;
import com.longer.school.R;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Axu on 2016/9/25.
 */
public class Yellow_Activity extends AppCompatActivity {
    public Yellow yellow;
    private int[] ali = new int[]{R.drawable.ali0, R.drawable.ali1, R.drawable.ali3, R.drawable.ali5, R.drawable.ali6, R.drawable.ali7, R.drawable.ali8, R.drawable.ali9};
    @Bind(R.id.iv_yellow_ali)
    ImageView ivYellowAli;
    @Bind(R.id.tv_yellow_name)
    TextView tvYellowName;
    @Bind(R.id.tv_yellow_type)
    TextView tvYellowType;
    @Bind(R.id.tv_yellow_dw)
    TextView tvYellowDw;
    @Bind(R.id.tv_yellow_tel)
    TextView tvYellowTel;
    @Bind(R.id.iv_yellow_call)
    ImageView ivYellowCall;
    @Bind(R.id.tv_yellow_infor)
    TextView tvYellowInfor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_yellow);
        ButterKnife.bind(this);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        CollapsingToolbarLayout mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        mCollapsingToolbarLayout.setTitle("详细信息");
        //通过CollapsingToolbarLayout修改字体颜色
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));//设置还没收缩时状态下字体颜色
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//

        Intent intent = getIntent();
        yellow = (Yellow) intent.getSerializableExtra("Yellow");
        setData();
    }

    public void setData() {
        tvYellowDw.setText(yellow.getDw());
        if (yellow.getInfor() == null) {
            tvYellowInfor.setText("暂时没有简介");
        } else {
            tvYellowInfor.setText(yellow.getInfor());
        }

        tvYellowName.setText(yellow.getName());
        tvYellowTel.setText(yellow.getTel());
        tvYellowType.setText(yellow.getType());
        ivYellowAli.setImageResource(ali[new Random().nextInt(8)]);

    }

    @OnClick(R.id.iv_yellow_call)
    public void onClick() {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + yellow.getTel()));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "学校黄页详细信息");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
