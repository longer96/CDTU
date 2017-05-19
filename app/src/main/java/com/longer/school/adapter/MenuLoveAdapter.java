package com.longer.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.longer.school.R;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.biz.LoveBiz;
import com.longer.school.view.activity.Add_loveActivity;
import com.longer.school.view.activity.Goods_Activity;
import com.longer.school.view.activity.LoveOne_ActivityView;
import com.longer.school.view.activity.MainActivity;
import com.sackcentury.shinebuttonlib.ShineButton;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 主界面 表白墙的适配器
 */
public class MenuLoveAdapter extends RecyclerView.Adapter<MenuLoveAdapter.MainCardHolder> {

    public List<Love> list = null;
    public Context context;

    public MenuLoveAdapter(List<Love> newses, Context context) {
        this.list = newses;
        this.context = context;
    }

    public void addEnditem() {
        Love love = null;
        this.list.add(love);
        super.notifyDataSetChanged();
    }


    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_loverecycle, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        holder.setDate();
        holder.setIsRecyclable(false);
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }


    public Love getDate(int position) {
        return list.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tx_lovemenu_object)
        TextView txLovemenuObject;
        @Bind(R.id.tx_lovemenu_content)
        TextView txLovemenuContent;
        @Bind(R.id.tx_lovemenu_name)
        TextView txLovemenuName;
        @Bind(R.id.tv_lovemenu_like)
        TextView tvLovemenuLike;
        @Bind(R.id.tv_lovemenu_comment)
        TextView tvLovemenuComment;
        @Bind(R.id.tv_lovemenu_tiem)
        TextView tvLovemenuTiem;
        @Bind(R.id.sb_lovemenu_like)
        ShineButton sbLovemenuLike;
        @Bind(R.id.sb_lovemenu_comment)
        ShineButton sbLovemenuComment;
        @Bind(R.id.sb_lovemenu_love)
        ShineButton sbLovemenuLove;
        @Bind(R.id.ll_lovemenu)
        LinearLayout linearLayout;
        @Bind(R.id.ll_lovemenu_below)
        LinearLayout linearLayoutbelow;
        private String[] colors = {"#01a3a1", "#ee697e", "#ff01b1bf", "#ff9d62", "#91bbeb", "#2d3867"};//颜色组

        public MainCardHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setDate() {
            final Love love = getDate(getAdapterPosition());
//            Log.d("tip", getAdapterPosition() + "长度：" + list.size());
            if (love == null) {
                sbLovemenuLove.setVisibility(View.VISIBLE);
                txLovemenuObject.setText("");
                txLovemenuContent.setText("我也要表白");
                txLovemenuContent.setTextColor(Color.parseColor("#202020"));
                txLovemenuName.setText("");
                linearLayoutbelow.setVisibility(View.GONE);
                linearLayout.setBackgroundResource(R.drawable.edittext_bg_love);
                linearLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(context, Add_loveActivity.class));
                    }
                });
                return;
            }
            sbLovemenuLove.setVisibility(View.GONE);
            txLovemenuObject.setText(love.getObject());
            txLovemenuContent.setText(love.getContent());
            txLovemenuName.setText(love.getName());
            tvLovemenuTiem.setText(TimeUtils.getFriendlyTimeSpanByNow(love.getCreatedAt()));
            if (love.getCommentnum() != null && love.getCommentnum() != 0) {
                tvLovemenuComment.setText("评论" + love.getCommentnum());
            }
            if (love.getLike() != null && love.getLike() != 0) {
                tvLovemenuLike.setText("赞" + love.getLike());
            }
            final String bg = colors[getAdapterPosition() % 6];
            linearLayout.setBackgroundColor(Color.parseColor(bg));
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toLoveOne_Activity(love, bg);
                }
            });
            sbLovemenuComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toLoveOne_Activity(love, bg);
                }
            });
            sbLovemenuLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int like = love.getLike() == null ? 0 : love.getLike();
                    boolean isadd;
                    if (sbLovemenuLike.isChecked()) {
                        like++;
                        isadd = true;
                    } else {
                        like--;
                        isadd = false;
                    }
                    new LoveBiz().updataLike(love, isadd, new Love.OnUpdataLikeOrComment() {
                        @Override
                        public void Success() {
                        }

                        @Override
                        public void Failed() {
                        }
                    });
                    love.setLike(like);
                    tvLovemenuLike.setText("赞" + like);
                }
            });

        }

        private void toLoveOne_Activity(Love love, String bg) {
            Intent intent = new Intent(context, LoveOne_ActivityView.class);
            ActivityOptionsCompat options =  ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.instance,
                    linearLayout, context.getString(R.string.transition_love_img));
            intent.putExtra("Love", love);
            intent.putExtra("bg", bg);
            ActivityCompat.startActivity(context, intent, options.toBundle());
        }
    }
}
