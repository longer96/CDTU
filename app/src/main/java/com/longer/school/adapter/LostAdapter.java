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

import com.blankj.utilcode.utils.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longer.school.modle.bean.Lost;
import com.longer.school.R;

import java.util.List;

/**
 * Created by Axu on 2016/9/17.
 */
public class LostAdapter extends RecyclerView.Adapter<LostAdapter.MainCardHolder> {
    public Itemclick mItemOnClickListener;
    public List<Lost> losts = null;
    private Context context;
    String str;
    public LostAdapter(List<Lost> losts, Context context, String str) {
        this.context = context;
        this.losts = losts;
        this.str=str;
    }

    private void runEnterAnimation(View view, int position) {
//        Log.d("position", position + "#");
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Lost> mlosts) {
        losts.addAll(mlosts);
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lost, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (losts == null) ? 0 : losts.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Lost getDate(int position) {
        return losts.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView iv_pic;
        ImageView iv_ok;
        ImageView iv_type;
        TextView tv_title;
        TextView tv_time;
        TextView tv_things;
        TextView tv_where;
        CardView cv_last;

        public MainCardHolder(View itemView) {
            super(itemView);
            iv_pic = (ImageView) itemView.findViewById(R.id.iv_lost_pic);
            tv_title = (TextView) itemView.findViewById(R.id.tv_lost_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_lost_time);
            tv_things = (TextView) itemView.findViewById(R.id.tv_lost_things);
            tv_where = (TextView) itemView.findViewById(R.id.tv_lost_where);
            cv_last = (CardView) itemView.findViewById(R.id.cv_lost);
            iv_ok = (ImageView) itemView.findViewById(R.id.iv_lost_ok);
            iv_type = (ImageView) itemView.findViewById(R.id.iv_lost_type);
        }

        public void setDate() {
            Lost lost = getDate(getAdapterPosition());
            tv_time.setText(TimeUtils.getFriendlyTimeSpanByNow(lost.getCreatedAt()));
            tv_things.setText(lost.getThing());
            tv_where.setText(lost.getWhere());

            String str2 =lost.getTitle() ;
            int fstart = str2.indexOf(str);
            int fend = fstart + str.length();
            SpannableStringBuilder style = new SpannableStringBuilder(str2);
            style.setSpan(new ForegroundColorSpan(Color.RED), fstart, fend, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            tv_title.setText(style);
            cv_last.setOnClickListener(this);
            if(lost.isComplete()){
                iv_ok.setVisibility(View.VISIBLE);
            }else{
                iv_ok.setVisibility(View.GONE);
            }
            if (lost.isType()) {
                iv_type.setBackgroundResource(R.drawable.lost_zl);
            } else {
                iv_type.setBackgroundResource(R.drawable.lost_sw);
            }
            if (lost.getPic1() == null) {
                Glide.with(context).load(R.drawable.lost_nopic)
                        .placeholder(R.mipmap.imageselector_photo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.imageselector_photo)
                        .into(iv_pic);
            } else {
                String url = lost.getPic1().getUrl();
                Glide.with(context).load(url)
                        .placeholder(R.mipmap.imageselector_photo)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.imageselector_photo)
                        .into(iv_pic);
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
