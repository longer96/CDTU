
package com.longer.school.adapter;

import android.support.v7.widget.RecyclerView.Adapter;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.longer.school.R;

import java.util.List;


public class Score_zxcjAdapter extends Adapter<Score_zxcjAdapter.DefineViewHolder> {

    private List<String> list;

    public Itemclick mItemOnClickListener;

    public Score_zxcjAdapter(List<String> list) {
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
        void Onclick(View view, String xn);
    }


    @Override
    public DefineViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.item_score_zxcj, parent, false);
        return new DefineViewHolder(view);
    }

    class DefineViewHolder extends ViewHolder {

        TextView tvTitle;

        public DefineViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            tvTitle.setOnClickListener(onClickListener);
        }

        public void setData(String data) {
            tvTitle.setText(data);
        }


        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemOnClickListener != null) {
                    mItemOnClickListener.Onclick(v, list.get(getAdapterPosition()));
                }
            }
        };
    }

}
