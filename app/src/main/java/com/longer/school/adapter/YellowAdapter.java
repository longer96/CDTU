package com.longer.school.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longer.school.modle.bean.Yellow;
import com.longer.school.R;

import java.util.List;

/**
 * 学校黄页的适配器
 * Created by Longer on 2016/9/17.
 */
public class YellowAdapter extends RecyclerView.Adapter<YellowAdapter.MainCardHolder> {
    public Itemclick mItemOnClickListener;
    public List<Yellow> yellows = null;
    private Context context;
    private int[] bg = {R.drawable.kb1, R.drawable.kb2, R.drawable.kb3, R.drawable.kb4, R.drawable.kb5, R.drawable.kb6,
            R.drawable.kb7, R.drawable.kb8, R.drawable.kb9, R.drawable.kb10, R.drawable.kb11, R.drawable.kb12,
            R.drawable.kb13, R.drawable.kb14, R.drawable.kb15, R.drawable.kb16};

    public YellowAdapter(List<Yellow> losts, Context context) {
        this.context = context;
        this.yellows = losts;
    }

    private void runEnterAnimation(View view, int position) {
        Log.d("position", position + "#");
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Yellow> losts) {
        this.yellows.addAll(losts);
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_yellow, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        runEnterAnimation(holder.itemView, position);
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (yellows == null) ? 0 : yellows.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Yellow getDate(int position) {
        return yellows.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout ll;
        ImageView iv_bg;
        TextView tv_xin;
        TextView tv_name;
        TextView tv_type;

        public MainCardHolder(View itemView) {
            super(itemView);
            iv_bg = (ImageView) itemView.findViewById(R.id.sql_iv);
            tv_xin = (TextView) itemView.findViewById(R.id.sqlite_tv_xin);
            tv_name = (TextView) itemView.findViewById(R.id.sqlite_select_name);
            tv_type = (TextView) itemView.findViewById(R.id.sqlite_select_type);
            ll=(LinearLayout)itemView.findViewById(R.id.ll_yellow);
        }

        public void setDate() {
            Yellow yellow = getDate(getAdapterPosition());

            tv_xin.setText(yellow.getName().substring(0, 1));
            tv_name.setText(yellow.getName());
            tv_type.setText(yellow.getType());
            ll.setOnClickListener(this);
            iv_bg.setImageResource(bg[getAdapterPosition() % 16]);
//            cv_last.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition());
            }
        }
    }


}
