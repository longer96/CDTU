package com.longer.school.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longer.school.R;
import com.longer.school.modle.bean.Good;

import java.util.List;

/**
 * Created by Axu on 2016/9/21.
 */
public class GoodsAdapter extends RecyclerView.Adapter<GoodsAdapter.MainCardHolder> {
    public Itemclick mItemOnClickListener;
    public List<Good> goods = null;
    String str;
    private Context context;
//    private boolean animateItems = false;//添加数据之后不用显示动画

    public GoodsAdapter(List<Good> goods, Context context, String str) {
        this.context = context;
        this.goods = goods;
        this.str = str;
    }

    private void runEnterAnimation(View view, int position) {
//        if (animateItems || position > 5) {
//            return;
//        } else {
//            view.setTranslationY(150);
//            view.animate()
//                    .translationY(0)
//                    .setStartDelay(200 * position)
//                    .setInterpolator(new DecelerateInterpolator(1.f))
//                    .setDuration(500)
//                    .start();
//        }
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Good> goods) {
//        animateItems = true;
        this.goods.addAll(goods);
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_goods, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (goods == null) ? 0 : goods.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Good getDate(int position) {
        return goods.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_thing;
        TextView tv_price;
        ImageView iv_show;
        ImageView iv_ok;
        ImageView iv_type;
        CardView card_goods;
        TextView tv_knife;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv_thing = (TextView) itemView.findViewById(R.id.tv_goods_thing);
            tv_price = (TextView) itemView.findViewById(R.id.tv_goods_price);
            iv_show = (ImageView) itemView.findViewById(R.id.iv_goods_show);
            card_goods = (CardView) itemView.findViewById(R.id.card_goods);
            tv_knife = (TextView) itemView.findViewById(R.id.tv_goods_knife);
            iv_ok = (ImageView) itemView.findViewById(R.id.iv_goods_ok);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_goods_type);
        }

        public void setDate() {
            Good good = getDate(getAdapterPosition());

            String str2 = good.getTitle();
            int fstart = str2.indexOf(str);
            int fend = fstart + str.length();
            SpannableStringBuilder style = new SpannableStringBuilder(str2);
            style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_thing.setText(style);
            tv_price.setText(good.getPrice());

            int visib = good.isComplete() ? View.VISIBLE : View.GONE;
            iv_ok.setVisibility(visib);

            int type = good.isType() ? R.drawable.goods_cs : R.drawable.goods_qg;
            iv_type.setBackgroundResource(type);

            int visib_knife = good.isKnife() ? View.VISIBLE : View.GONE;
            tv_knife.setVisibility(visib_knife);

            card_goods.setOnClickListener(this);
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
        }

        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition());
            }
        }
    }
}
