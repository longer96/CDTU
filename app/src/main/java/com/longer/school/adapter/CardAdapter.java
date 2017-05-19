package com.longer.school.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longer.school.modle.bean.CardClass;
import com.longer.school.R;

import java.util.List;


/**
 * 消费记录适配器
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MainCardHolder> {

    public Itemclick mItemOnClickListener;
    public List<CardClass> cards = null;

    public CardAdapter(List<CardClass> newses) {
        this.cards = newses;
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<CardClass> newses) {
        this.cards = newses;
        super.notifyDataSetChanged();
    }

    public void claerall() {
        this.cards.clear();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, parent, false));
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

    public CardClass getDate(int position) {
        return cards.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_where;
        TextView tv_time;
        TextView tv_money;
        TextView tv_day;

        public MainCardHolder(View itemView) {
            super(itemView);
            tv_where = (TextView) itemView.findViewById(R.id.card_tv_where);
            tv_time = (TextView) itemView.findViewById(R.id.card_tv_time);
            tv_money = (TextView) itemView.findViewById(R.id.card_tv_money);
            tv_day = (TextView) itemView.findViewById(R.id.card_tv_xinqi);
        }

        public void setDate() {
            CardClass card = getDate(getAdapterPosition());
            tv_where.setText(card.getWhere());
            tv_time.setText(card.getTime());
            tv_money.setText(card.getMoney());
            tv_day.setText(card.getDay());

            if ("-".equals(card.getMoney().substring(0, 1))) {
                tv_money.setTextColor(Color.parseColor("#008B8B"));
                tv_money.setText("+" + card.getMoney().substring(1));
            }else{
                tv_money.setTextColor(Color.parseColor("#ff0000"));
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
