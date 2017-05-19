package com.longer.school.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longer.school.modle.bean.CardClass;
import com.longer.school.modle.bean.News;
import com.longer.school.R;

import java.util.List;


/**
 * 校园新闻适配器
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MainCardHolder> {

    public Itemclick mItemOnClickListener;
    public List<News> cards = null;

    public NewsAdapter(List<News> newses) {
        this.cards = newses;
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<News> newses) {
        this.cards = newses;
        super.notifyDataSetChanged();
    }


    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (cards == null) ? 0 : cards.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public News getDate(int position) {
        return cards.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView time;
        CardView cardView;
        public MainCardHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.new_tv_title);
            time = (TextView) itemView.findViewById(R.id.new_tv_time);
            cardView = (CardView) itemView.findViewById(R.id.cv_news);
        }

        public void setDate() {
            News card = getDate(getAdapterPosition());
            title.setText(card.getTitle());
            time.setText(card.getTime());
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition());
            }
        }
    }
}
