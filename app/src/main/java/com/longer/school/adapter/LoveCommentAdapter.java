package com.longer.school.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.longer.school.R;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.LoveComment;
import com.longer.school.modle.bean.User;
import com.longer.school.utils.GlideCircleTransform;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 表白墙评论适配器
 */
public class LoveCommentAdapter extends RecyclerView.Adapter<LoveCommentAdapter.MainCardHolder> {

    public Itemclick mItemOnClickListener;
    public List<LoveComment> lists = null;
    private Context context;
    private Love bbz;//表白发布者

    public LoveCommentAdapter(List<LoveComment> lists, Context context, Love bbz) {
        this.lists = lists;
        this.context = context;
        this.bbz = bbz;
    }


    public void setOnItemclicklister(Itemclick ItemOnclickListener) {
        mItemOnClickListener = ItemOnclickListener;
    }


    public void notifyDataSetChanged(LoveComment comment) {
        lists.add(comment);
        super.notifyDataSetChanged();
    }

    @Override
    public MainCardHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MainCardHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lovecomment, parent, false));
    }

    @Override
    public void onBindViewHolder(MainCardHolder holder, int position) {
        holder.setDate();
    }

    @Override
    public int getItemCount() {
        return (lists == null) ? 0 : lists.size();
    }


    public interface Itemclick {
        void OnItemclick(View view, int floor);
    }

    public LoveComment getDate(int position) {
        return lists.get(position);
    }

    public class MainCardHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.image_lovecomment_headpic)
        ImageView imageLovecommentHeadpic;
        @Bind(R.id.tv_lovecomment_floor)
        TextView tvLovecommentFloor;
        @Bind(R.id.tv_lovecomment_username)
        TextView tvLovecommentUsername;
        @Bind(R.id.tv_lovecomment_time)
        TextView tvLovecommentTime;
        @Bind(R.id.tv_lovecomment_content)
        TextView tvLovecommentContent;
        @Bind(R.id.card_lovecomment)
        CardView cardView;

        MainCardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setDate() {
            LoveComment date = getDate(getAdapterPosition());
            tvLovecommentContent.setText(date.getContent());
            tvLovecommentTime.setText(TimeUtils.getFriendlyTimeSpanByNow(date.getCreatedAt()));
            int floornum = getAdapterPosition() + 1;
            tvLovecommentFloor.setText(floornum + "楼");
            tvLovecommentUsername.setText(getcommentr(date.getMy()));

            int head;
            if (date.getMy() != null && date.getMy().getSex() != null) {
                head = "boy".equals(date.getMy().getSex()) ? R.drawable.head_boy : R.drawable.head_girl;
            } else {
                head = R.drawable.head_girl;
            }
            Glide.with(context)
                    .load(head)
                    .centerCrop()
                    .transform(new GlideCircleTransform(context))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageLovecommentHeadpic);
            cardView.setOnClickListener(this);
        }

        /**
         * 计算出评论者的昵称
         */
        private String getcommentr(User user) {
            if (user == null || user.getUsername() == null || bbz.getUser() == null) {
                return "匿名用户";
            }
            if (bbz.getUser().getObjectId().equals(user.getObjectId())) {
                return "表白者";
            }

            String username = user.getUsername().substring(0, 4);
            StringBuffer sb = new StringBuffer();

            String usernamenew = username.substring(0, 2);
            if ("15".equals(usernamenew) || "16".equals(usernamenew)) {//说明不是2014级之前的学生
                switch (usernamenew) {
                    case "15":
                        sb.append("大二");
                        if(user.getSex() == null){
                            sb.append("同学");
                            break;
                        }

                        if ("boy".equals(user.getSex())) {
                            sb.append("学长");
                        } else {
                            sb.append("学姐");
                        }
                        break;
                    case "16":
                        sb.append("大一");
                        if(user.getSex() == null){
                            sb.append("同学");
                            break;
                        }
                        if ("boy".equals(user.getSex())) {
                            sb.append("学弟");
                        } else {
                            sb.append("学妹");
                        }
                        break;
                }

                return sb.toString();
            }

            switch (username) {
                case "2014":
                    sb.append("大三");
                    if(user.getSex() == null){
                        sb.append("同学");
                        break;
                    }
                    if ("boy".equals(user.getSex())) {
                        sb.append("学长");
                    } else {
                        sb.append("学姐");
                    }
                    break;
                case "2013":
                    sb.append("大四");
                    if(user.getSex() == null){
                        sb.append("同学");
                        break;
                    }
                    if ("boy".equals(user.getSex())) {
                        sb.append("学长");
                    } else {
                        sb.append("学姐");
                    }
                    break;
                default:
                    sb.setLength(0);
                    sb.append("匿名用户");
            }
            return sb.toString();

        }

        @Override
        public void onClick(View v) {
            if (mItemOnClickListener != null) {
                mItemOnClickListener.OnItemclick(v, getAdapterPosition() + 1);
            }
        }
    }

}
