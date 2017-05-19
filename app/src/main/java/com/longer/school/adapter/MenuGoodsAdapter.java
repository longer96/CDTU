package com.longer.school.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.modle.bean.Good;
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
 * 主界面 跳蚤市场的适配器
 */
public class MenuGoodsAdapter extends RecyclerView.Adapter<MenuGoodsAdapter.MainCardHolder> {

    public List<Good> list = null;
    public Context context;

    public MenuGoodsAdapter(List<Good> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_goodsrecycle, parent, false));
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


    public Good getDate(int position) {
        return list.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder {
        TextView tv_thing;
        TextView tv_price;
        ImageView iv_show;
        ImageView iv_ok;
        ImageView iv_type;
        CardView card_goods;
        TextView tv_knife;


        public MainCardHolder(View view) {
            super(view);
            tv_thing = (TextView) itemView.findViewById(R.id.tv_goods_thing);
            tv_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            iv_show = (ImageView) itemView.findViewById(R.id.iv_goods_show);
            card_goods = (CardView) itemView.findViewById(R.id.card_goodsmenu);
            tv_knife = (TextView) itemView.findViewById(R.id.tv_goods_knife);
            iv_ok = (ImageView) itemView.findViewById(R.id.iv_goods_ok);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_goods_type);
        }

        public void setDate() {
            final Good good = getDate(getAdapterPosition());

            tv_thing.setText(good.getTitle());
            tv_price.setText(good.getPrice());
            int visib = good.isComplete() ? View.VISIBLE : View.GONE;
            iv_ok.setVisibility(visib);

            int type = good.isType() ? R.drawable.goods_cs : R.drawable.goods_qg;
            iv_type.setBackgroundResource(type);

            int visib_knife = good.isKnife() ? View.VISIBLE : View.GONE;
            tv_knife.setVisibility(visib_knife);

            if (good.getPic1() == null) {
                Glide.with(context).load(R.drawable.lost_nopic)
                        .placeholder(R.mipmap.imageselector_photo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.imageselector_photo)
                        .into(iv_show);
            } else {
                Glide.with(context).load(good.getPic1().getUrl())
                        .placeholder(R.mipmap.imageselector_photo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.imageselector_photo)
                        .into(iv_show);
            }
            card_goods.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Goods_Activity.class);
                    ActivityOptionsCompat options =  ActivityOptionsCompat.makeSceneTransitionAnimation(MainActivity.instance,
                            iv_show, context.getString(R.string.transition_goods_img));
                    intent.putExtra("Good", good);
                    ActivityCompat.startActivity(context, intent, options.toBundle());
                }
            });

        }

        private void toLoveOne_Activity(Love love, String bg) {
            Intent intent = new Intent(context, LoveOne_ActivityView.class);
            intent.putExtra("Love", love);
            intent.putExtra("bg", bg);
            context.startActivity(intent);
        }
    }
}
