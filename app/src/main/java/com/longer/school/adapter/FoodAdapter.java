package com.longer.school.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.longer.school.R;
import com.longer.school.modle.bean.Food;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by longer on 2017/5/12.
 */
public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.MainCardHolder> {

    public Itemclick mItemOnClickListener;
    public List<Food> list = null;
    private Context context;

    public FoodAdapter(List<Food> list, Context context) {
        this.context = context;
        this.list = list;
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(List<Food> mlosts) {
        list.addAll(mlosts);
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_store, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (list == null) ? 0 : list.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int position);
    }

    public Food getDate(int position) {
        return list.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_food_pic)
        ImageView ivFoodPic;
        @Bind(R.id.tv_food_name)
        TextView tvFoodName;
        @Bind(R.id.tv_food_price)
        TextView tvFoodPrice;
        @Bind(R.id.card_food)
        CardView cardFood;



        public MainCardHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void setDate() {
            Food food = getDate(getAdapterPosition());

            int no = getAdapterPosition() + 1;
            tvFoodName.setText(food.getName());
            tvFoodPrice.setText(food.getPrice());
            if (food.getPic1() == null) {
                Glide.with(context).load(R.drawable.lost_nopic)
                        .placeholder(R.mipmap.imageselector_photo)
                        .error(R.mipmap.imageselector_photo)
                        .into(ivFoodPic);
            } else {
                String url = food.getPic1().getUrl();
                Glide.with(context).load(url)
                        .placeholder(R.mipmap.imageselector_photo)
                        .error(R.mipmap.imageselector_photo)
                        .into(ivFoodPic);
            }
            cardFood.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition());
            }
        }
    }


}
