package com.longer.school.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.adapter.LoveCommentAdapter;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.LoveComment;
import com.longer.school.presenter.LoveOneActivityPresenter;
import com.longer.school.utils.FullyLinearLayoutManager;
import com.longer.school.utils.PublicTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.iview.ILoveOne_ActivityView;
import com.sackcentury.shinebuttonlib.ShineButton;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoveOne_ActivityView extends AppCompatActivity implements ILoveOne_ActivityView {

    public Context context;
    public static LoveCommentAdapter adapter;
    @Bind(R.id.card_loveone_bg)
    CardView cardLoveoneBg;
    @Bind(R.id.tv_loveone_tiem)
    TextView tvLoveoneTiem;
    private FullyLinearLayoutManager linearLayoutManager;
    public Love love;
    public ProgressDialog pg;
    @Bind(R.id.tx_loveone_object)
    TextView txLoveoneObject;
    @Bind(R.id.tx_loveone_content)
    TextView txLoveoneContent;
    @Bind(R.id.tx_loveone_name)
    TextView txLoveoneName;
    @Bind(R.id.tv_loveone_like)
    TextView tvLoveoneLike;
    @Bind(R.id.tv_loveone_comment)
    TextView tvLoveoneComment;
    @Bind(R.id.ll_loveone_comment_true)
    LinearLayout llLoveoneCommentTrue;
    @Bind(R.id.ll_loveone_comment_false)
    LinearLayout llLoveoneCommentFalse;
    @Bind(R.id.sb_loveone_like)
    ShineButton sbLoveoneLike;
    @Bind(R.id.sb_loveone_comment)
    ShineButton sbLoveoneComment;
    @Bind(R.id.recycle_lovecomment)
    RecyclerView recyclerView;
    @Bind(R.id.library_fragment)
    LinearLayout libraryFragment;
    @Bind(R.id.edt_loveone_comment)
    EditText edtLoveoneComment;

    private LoveOneActivityPresenter mLoveOneActivityPresenter = new LoveOneActivityPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_loveone);
        ButterKnife.bind(this);
        love = (Love) getIntent().getSerializableExtra("Love");
        cardLoveoneBg.setBackgroundColor(Color.parseColor(getIntent().getStringExtra("bg")));
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("表白");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
    }

    public void inti() {
        context = LoveOne_ActivityView.this;
        txLoveoneObject.setText(love.getObject());
        txLoveoneContent.setText(love.getContent());
        txLoveoneName.setText(love.getName());
        tvLoveoneTiem.setText(TimeUtils.getFriendlyTimeSpanByNow(love.getCreatedAt()));

        pg = new ProgressDialog(context);
        pg.setMessage("正在提交评论...");
        pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失

        mLoveOneActivityPresenter.getCommentdata();
        mLoveOneActivityPresenter.getLoveLike();

        linearLayoutManager = null;
    }


    @Override
    public void setSms(Integer sms) {
        tvLoveoneComment.setText("评论" + sms);
    }

    @Override
    public void setLike(Integer num) {
        tvLoveoneLike.setText("赞" + num);
    }

    @Override
    public void showCommentView() {
        llLoveoneCommentTrue.setVisibility(View.VISIBLE);
        llLoveoneCommentFalse.setVisibility(View.GONE);
    }

    @Override
    public void hideComentView() {
        llLoveoneCommentTrue.setVisibility(View.GONE);
        llLoveoneCommentFalse.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCommenting() {
        if (pg.isShowing()) {
            pg.dismiss();
        } else {
            pg.show();
        }
    }

    @Override
    public void setCommentData(List<LoveComment> list) {
        linearLayoutManager = new FullyLinearLayoutManager(context);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new LoveCommentAdapter(list, context,love);
        adapter.setOnItemclicklister(itemclick);
        recyclerView.setAdapter(adapter);
    }

    LoveCommentAdapter.Itemclick itemclick = new LoveCommentAdapter.Itemclick() {
        @Override
        public void OnItemclick(View view, int floor) {
            //如果输入框显示，先隐藏
            if (llLoveoneCommentTrue.isShown()) {
                hideComentView();
                return;
            }
            mLoveOneActivityPresenter.showCommentview();
            edtLoveoneComment.setText("回复：" + floor + "楼\r\n");
            edtLoveoneComment.setSelection(edtLoveoneComment.getText().length());

        }
    };

    @Override
    public void addCommentData(LoveComment comment) {
        mLoveOneActivityPresenter.upLoveComment();
        int i = love.getCommentnum() == null ? 1 : love.getCommentnum() + 1;
        Log.d("tip", "评论数量：" + i);
        setSms(i);
        if (linearLayoutManager == null) {
            linearLayoutManager = new FullyLinearLayoutManager(context);
            recyclerView.setNestedScrollingEnabled(false);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setLayoutManager(linearLayoutManager);
            List<LoveComment> list = new ArrayList<LoveComment>();
            list.add(comment);
            adapter = new LoveCommentAdapter(list, context,love);
            adapter.setOnItemclicklister(itemclick);
            recyclerView.setAdapter(adapter);
            return;
        }
        //如果之前没有数据那么无法notifyDataSetChanged
        adapter.notifyDataSetChanged(comment);
    }


    @Override
    public void showsetCommentDataFail() {
        Toast.show("获取评论数据出错");
    }

    @Override
    public void showCommentFail() {
        Toast.show("评论失败了");
    }

    @Override
    public void clearCommnet() {
        edtLoveoneComment.setText("");
    }

    @Override
    public Love getLove() {
        return love;
    }

    @Override
    public String getComment() {
        return edtLoveoneComment.getText().toString().trim();
    }

    @Override
    public EditText getedittext() {
        return edtLoveoneComment;
    }

    @Override
    public boolean checksblike() {
        return sbLoveoneLike.isChecked() ? true : false;
    }

    @OnClick({R.id.sb_loveone_comment, R.id.ll_loveone_comment_false, R.id.btn_loveone_send, R.id.sb_loveone_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.sb_loveone_comment:
                if (sbLoveoneComment.isChecked()) {
                    mLoveOneActivityPresenter.showCommentview();
                } else {
                    hideComentView();
                }
                break;
            case R.id.sb_loveone_like:
                int like = love.getLike() == null ? 0 : love.getLike();
                if (sbLoveoneLike.isChecked()) {
                    like++;
                } else {
                    like--;
                }
                love.setLike(like);
                tvLoveoneLike.setText("赞" + like);
                break;
            case R.id.ll_loveone_comment_false:
                mLoveOneActivityPresenter.showCommentview();
                break;
            case R.id.btn_loveone_send:
                if (getComment().isEmpty()) {
                    Toast.show("评论内容不能为空哦");
                    break;
                }
                if (!PublicTools.checkfriendtext(getComment())) {
                    Toast.show("评论内容有点不正常啊！");
                    hideComentView();
                    clearCommnet();
                    PublicTools.closekeyboard(edtLoveoneComment);
                    break;
                }
                mLoveOneActivityPresenter.putCommenting();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && llLoveoneCommentTrue.isShown()) {
            hideComentView();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "表白墙详细");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mLoveOneActivityPresenter.upLoveLike();
    }
}
