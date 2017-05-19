
package com.longer.school.adapter;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longer.school.R;
import com.longer.school.modle.bean.Store;

import java.util.ArrayList;
import java.util.List;


public class Store_selectStoreAdapter extends Adapter<Store_selectStoreAdapter.DefineViewHolder> {

    private List<Store> list;

    public Itemclick mItemOnClickListener;

    public Store_selectStoreAdapter(List<Store> list) {
        this.list = list;
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public void onBindViewHolder(DefineViewHolder viewHolder, int position) {
        viewHolder.setData(list.get(position));
    }

    public void setOnItemclicklister(Itemclick mItemOnClickListener) {
        this.mItemOnClickListener = mItemOnClickListener;
    }

    public interface Itemclick {
        void Onclick(View view, List<Store> store);
    }


    @Override
    public DefineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_store_selectstore, parent, false);
        return new DefineViewHolder(view);
    }

    class DefineViewHolder extends ViewHolder {

        TextView tvName;
        TextView tvState;
        TextView tvWhere;
        CardView cardView;

        public DefineViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            cardView = (CardView) itemView.findViewById(R.id.card_item);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            tvWhere = (TextView) itemView.findViewById(R.id.tv_where);
        }

        public void setData(Store store) {
            tvName.setText(store.getName());
            String where = "0".equals(store.getWhere()) ? "全校" : store.getWhere() + "舍";
            tvWhere.setText("范围： " + where);

            if(store.isState()){
                tvState.setText("状态： 营业中");
            }else{
                tvState.setText("状态： 休息中");
                tvState.setTextColor(Color.RED);
            }

            cardView.setOnClickListener(onClickListener);
        }


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemOnClickListener != null) {
                    List<Store> mlist = new ArrayList<Store>();
                    mlist.add(list.get(getAdapterPosition()));
                    mItemOnClickListener.Onclick(v, mlist);
                }
            }
        };
    }

}
