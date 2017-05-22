package com.longer.school.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.utils.TimeUtils;
import com.bumptech.glide.Glide;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.jude.rollviewpager.RollPagerView;
import com.jude.rollviewpager.adapter.StaticPagerAdapter;
import com.jude.rollviewpager.hintview.ColorPointHintView;
import com.longer.school.R;
import com.longer.school.adapter.MenuGoodsAdapter;
import com.longer.school.adapter.MenuLoveAdapter;
import com.longer.school.modle.bean.CourseClass;
import com.longer.school.modle.bean.Good;
import com.longer.school.modle.bean.Lost;
import com.longer.school.modle.bean.Love;
import com.longer.school.modle.bean.PicHeadTip;
import com.longer.school.modle.bean.SchoolMes;
import com.longer.school.presenter.Fragement_MenuPresenter;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.PublicTools;
import com.longer.school.utils.StreamTools;
import com.longer.school.utils.TimeTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.activity.Add_lostActivity;
import com.longer.school.view.activity.Calan_Activity;
import com.longer.school.view.activity.CardToolsActivity;
import com.longer.school.view.activity.Card_Activity;
import com.longer.school.view.activity.Cengfan_Activity;
import com.longer.school.view.activity.CourseActivity;
import com.longer.school.view.activity.ImageActivity;
import com.longer.school.view.activity.LoginSchool_Activity;
import com.longer.school.view.activity.Lost_Activity;
import com.longer.school.view.activity.LoveActivity;
import com.longer.school.view.activity.MainActivity;
import com.longer.school.view.activity.NewsActivity;
import com.longer.school.view.activity.QueryActivity;
import com.longer.school.view.activity.Query_cardActivity;
import com.longer.school.view.activity.SignUp_Activity;
import com.longer.school.view.activity.Sqlite_selectActivity;
import com.longer.school.view.activity.Store_Activity;
import com.longer.school.view.activity.StudentActivity;
import com.longer.school.view.activity.library.LibraryActivity;
import com.longer.school.view.activity.library.Library_selectActivity;
import com.longer.school.view.activity.zfxt.Score_zfxtActivity;
import com.longer.school.view.iview.IFragment_MenuView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class Fragment_Menu extends Fragment implements IFragment_MenuView {
    @Bind(R.id.tv_menu_mes_qq)
    TextView tvMenuMesQq;
    @Bind(R.id.tv_menu_mes_sign)
    TextView tvMenuMesSign;
    @Bind(R.id.ll_menu_mes)
    LinearLayout llMenuMes;
    @Bind(R.id.card_menu_message)
    CardView cardMenuMessage;
    @Bind(R.id.card_menu_love)
    CardView cardMenuLove;
    @Bind(R.id.tv_menu_mes_type)
    TextView tvMenuMesType;
    @Bind(R.id.tv_menu_mes_info)
    TextView tvMenuMesInfo;
    @Bind(R.id.tv_menu_mes_sponsor)
    TextView tvMenuMesSponsor;
    @Bind(R.id.tv_menu_mes_time)
    TextView tvMenuMesTime;
    @Bind(R.id.recycle_item_menu_love)
    RecyclerView recycleItemMenuLove;
    @Bind(R.id.recycle_item_menu_goods)
    RecyclerView recycleItemMenuGoods;
    @Bind(R.id.card_menu_library)
    CardView cardMenuLibrary;
    private RollPagerView mRollViewPager;
    private TextView tv_money;//一卡通卡片,余额
    private View view;
    private Context context;
    private static boolean login;
    final MainActivity mainActivity = MainActivity.instance;;

    private final int refresh_success = 1;//刷新一卡通余额成功
    private final int refresh_nochange = 2;//刷新一卡通余额没有变化
    private final int refresh_fail = 3;
    private Fragement_MenuPresenter menuPresenter = new Fragement_MenuPresenter(this);


    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case refresh_success:
                    tv_money.setText(FileTools.getshare(context, "money"));
                    break;
                case refresh_nochange:
                    break;
                case refresh_fail:
                    Toast.show("啊呀，获取余额失败鸟~");
                    break;
            }
        }

        ;
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_menu, null);
        context = getActivity();
        ButterKnife.bind(this, view);
        MainActivity.setToolBarVisibale(true);
        init();
        setpic();
        return view;
    }


    /**
     * 设置图片轮显
     */
    public void setpic() {
        mRollViewPager = (RollPagerView) view.findViewById(R.id.roll_view_pager);
        mRollViewPager.setAnimationDurtion(700);
        mRollViewPager.setAdapter(new TestNormalAdapter());
        mRollViewPager.setHintView(new ColorPointHintView(context, Color.parseColor("#f0FF4081"), Color.parseColor("#a0ffffff")));

        menuPresenter.setHeadPic();
    }

    @Override
    public void setHeadPic(List<PicHeadTip> list) {
        mRollViewPager.setAdapter(new Test_networkAdapter(list));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    private class TestNormalAdapter extends StaticPagerAdapter {
        private int[] imgs = {
                R.mipmap.home_1,
                R.mipmap.home_2,
                R.mipmap.home_3,
                R.mipmap.home_4
        };

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            view.setImageResource(imgs[position]);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return imgs.length;
        }
    }

    private class Test_networkAdapter extends StaticPagerAdapter {
        private List<PicHeadTip> list;

        public Test_networkAdapter(List<PicHeadTip> list) {
            this.list = list;
        }

        private int[] imgs = {
                R.mipmap.home_1,
                R.mipmap.home_2,
                R.mipmap.home_3,
                R.mipmap.home_4,
                R.mipmap.home_2,
                R.mipmap.home_3
        };

        @Override
        public View getView(ViewGroup container, final int position) {
            ImageView view = new ImageView(container.getContext());
            Glide.with(context).load(list.get(position).getUrl())
                    .centerCrop()
                    .placeholder(imgs[position])
                    .error(imgs[position])
                    .crossFade(1000) // 可设置时长，默认“300ms”
                    .into(view);
            view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            return view;
        }

        @Override
        public int getCount() {
            return list.size() <= 4 ? 4 : list.size();
        }
    }

    public void init() {
        login = false;
        String str = FileTools.getshare(context, "login");
        if ("true".equals(str)) {// 表示已经成功登录过
            login = true;
        }

        view.findViewById(R.id.menu_calan).setOnClickListener(listener_calan);//校历
        view.findViewById(R.id.menu_course).setOnClickListener(listener_course);//课表
        view.findViewById(R.id.menu_zfxt).setOnClickListener(listener_zfxt);//正方系统
        view.findViewById(R.id.menu_library).setOnClickListener(listener_library);//图书借阅
        view.findViewById(R.id.menu_love).setOnClickListener(listener_love);//表白墙
        view.findViewById(R.id.menu_money).setOnClickListener(listener_money);//消费记录
        view.findViewById(R.id.menu_yellow).setOnClickListener(listener_yellow);//学校黄历
        view.findViewById(R.id.menu_more).setOnClickListener(listener_more);//更多功能


        mainActivity.findViewById(R.id.ll_menu_down).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.openmore();
            }
        });
        mainActivity.findViewById(R.id.view_menu_down).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.openmore();
            }
        });
        mainActivity.findViewById(R.id.menu_news).setOnClickListener(listener_news);//校园新闻
        mainActivity.findViewById(R.id.menu_car).setOnClickListener(listener_bus);//校车
        mainActivity.findViewById(R.id.menu_select_card).setOnClickListener(listener_select_card);//一卡通查询
        mainActivity.findViewById(R.id.menu_student_select).setOnClickListener(listener_student_select);//学生信息查询
        mainActivity.findViewById(R.id.menu_card_tools).setOnClickListener(listener_card_tools);//一卡通工具
        mainActivity.findViewById(R.id.menu_student).setOnClickListener(listener_student);//学生证
        mainActivity.findViewById(R.id.menu_vip).setOnClickListener(listener_vip);//免费会员
        mainActivity.findViewById(R.id.menu_store).setOnClickListener(listener_store);//同学的店
        ///-------------------------------------------------------------------------------------------------------

        setmenu_card();//一卡通消费记录卡片
        setmenu_course();//设置课表卡片
        setmenu_lost();//设置失物招领卡片
        menuPresenter.setMenuGoods();//设置跳蚤市场卡片
        menuPresenter.setMenuMessage();//设置校园活动通知卡片
        menuPresenter.setMenuLove();//设置表白墙卡片
        menuPresenter.setMenuLibrary(login);//设置图书馆卡片
    }
    //////////////////////////////////////////////////////////////////////////////////////

    /**
     * 设置一卡通消费记录卡片
     */
    private void setmenu_card() {
        CardView cardView = (CardView) view.findViewById(R.id.card_menu_card);
        cardView.setVisibility(View.GONE);
        view.findViewById(R.id.tv_menu_card).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//底部消费记录按钮
                startActivity(new Intent(context, Card_Activity.class));
            }
        });
        //如果登录过显示一卡通余额
        if (!login) {//表示没有登录
            return;
        }
        // 先解析一下保存到card 文本的内容，保存当前余额到share
        String money = FileTools.getshare(context, "money");
        tv_money = (TextView) view.findViewById(R.id.menu_card_money);
        if (!"".equals(money)) {
            tv_money.setText(money);
            cardView.setVisibility(View.VISIBLE);
        }
        // 并且更新当前余额
        if (TimeTools.refresh_card(context)) {
            refresh_money();
        }

        TextView tv = (TextView) view.findViewById(R.id.menu_card_xfjl);
        String str = StreamTools.getonecard();
        if (!str.isEmpty()) {
            tv.setText(str);
        }
    }

    /**
     * 设置课表卡片
     */
    private void setmenu_course() {
        //先将卡片隐藏
        CardView cardView = (CardView) view.findViewById(R.id.card_menu_course);
        cardView.setVisibility(View.GONE);
        //得到今天的课表
        List<CourseClass> classes = StreamTools.gettodaycourse();
        if (classes == null) {//如果为空则不显示
            return;
        }
        cardView.setVisibility(View.VISIBLE);
        TextView tv_jie = (TextView) view.findViewById(R.id.tv_menu_course_jie);
        int coursenum = classes.size();
        String strcourse = coursenum == 0 ? "今天没课，好好休息哟~" : "今天有" + coursenum + "节课";
        tv_jie.setText(strcourse);

        TextView tv = (TextView) view.findViewById(R.id.tv_menu_course_zhou);
        tv.setText("第" + TimeTools.course_zhoushu() + "周");

        int[] ll = {R.id.ll_menu_course1, R.id.ll_menu_course2, R.id.ll_menu_course3, R.id.ll_menu_course4, R.id.ll_menu_course5};
        int[] tv_name = {R.id.tv_menu_course_name1, R.id.tv_menu_course_name2, R.id.tv_menu_course_name3, R.id.tv_menu_course_name4, R.id.tv_menu_course_name5};
        int[] tv_info = {R.id.tv_menu_course_info1, R.id.tv_menu_course_info2, R.id.tv_menu_course_info3, R.id.tv_menu_course_info4, R.id.tv_menu_course_info5};

        CourseClass course = null;
        Log.d("有多少节课：", classes.size() + "");
        for (int i = 0; i < classes.size(); i++) {
            LinearLayout llyout = (LinearLayout) view.findViewById(ll[i]);
            llyout.setVisibility(View.VISIBLE);
            llyout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, CourseActivity.class));
                }
            });
            course = classes.get(i);// 拿到当前课程
            tv = (TextView) view.findViewById(tv_name[i]);
            tv.setText(course.getName());

            tv = (TextView) view.findViewById(tv_info[i]);
            StringBuffer sb = new StringBuffer();
            int x = (course.getId()) % 10;//行   得到对应的位置
//            Log.d("tip", "行" + x);
            switch (x) {
                case 1:
                    sb.append("上午1-2");
                    break;
                case 2:
                    sb.append("上午3-4");
                    break;
                case 3:
                    sb.append("下午1-2");
                    break;
                case 4:
                    sb.append("下午3-4");
                    break;
                case 5:
                    sb.append("晚上");
                    break;
            }
            sb.append("   ");
            sb.append(course.getRoom());
            tv.setText(sb.toString());
        }

    }


    /**
     * 设置失物招领卡片
     */
    private void setmenu_lost() {
        view.findViewById(R.id.tv_menu_lostadd).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//底部我要发布按钮
                startActivity(new Intent(context, Add_lostActivity.class));
            }
        });
        //得到数据
        BmobQuery<Lost> query = new BmobQuery<Lost>();
        query.order("-createdAt");
        query.setLimit(1);
        query.findObjects(new FindListener<Lost>() {
                              @Override
                              public void done(List<Lost> list, BmobException e) {
                                  if (e != null) {
                                      Log.d("查询失物招领出错", e.toString());
                                      e.printStackTrace();
                                      Toast.show("查询出错(" + e.getErrorCode() + ")");
                                      return;
                                  }
                                  Lost good = list.get(0);
                                  ImageView iv = (ImageView) view.findViewById(R.id.iv_menu_lost);
                                  if (good.getPic1() != null) {
                                      Glide.with(context).load(good.getPic1().getUrl())
                                              .placeholder(R.mipmap.imageselector_photo)
                                              .error(R.mipmap.imageselector_photo)
                                              .into(iv);
                                  } else {
                                      Glide.with(context).load(R.drawable.lost_nopic)
                                              .into(iv);
                                  }
                                  TextView tv = (TextView) view.findViewById(R.id.tv_menu_lost_name);
                                  tv.setText(good.getTitle());
                                  tv = (TextView) view.findViewById(R.id.tv_menu_lost_info);
                                  tv.setText(good.getInfor());
                                  tv = (TextView) view.findViewById(R.id.tv_menu_lost_time);
                                  tv.setText(TimeUtils.getFriendlyTimeSpanByNow(good.getCreatedAt()));

                                  //设置失物的点击事件
                                  final Lost good1 = good;
                                  view.findViewById(R.id.ll_menu_lost).setOnClickListener(new OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          Intent intent = new Intent(context, Lost_Activity.class);
                                          ActivityOptionsCompat options =
                                                  ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                                                          v.findViewById(R.id.iv_menu_lost), getString(R.string.transition_goods_img));
                                          intent.putExtra("Lost", good1);
                                          ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
                                      }
                                  });
                              }
                          }
        );
    }

    /**
     * 设置校园通知卡片
     */
    @Override
    public void setmenu_message(final SchoolMes schoolMes) {
        tvMenuMesType.setText(schoolMes.getType());
        tvMenuMesInfo.setText("        " + schoolMes.getInfor());
        tvMenuMesSponsor.setText(schoolMes.getSponsor());
        tvMenuMesTime.setText(schoolMes.getTime());

        if (schoolMes.getLink() != null && !schoolMes.getLink().isEmpty()) {//不为空我们可以设置他的点击事件，可以跳转到H5网页
            cardMenuMessage.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicTools.openhtml(context, schoolMes.getLink());
                }
            });
        }
        if (schoolMes.isSign()) {
            setmenu_message_llshow();
            setmenu_message_signshow();
            tvMenuMesSign.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SignUp_Activity.class);
                    intent.putExtra("sponsor", schoolMes.getSponsor());
                    startActivity(intent);
                }
            });
        }
        if (schoolMes.getQqgroup() != null && !schoolMes.getQqgroup().isEmpty()) {
            setmenu_message_llshow();
            setmenu_message_qqshow();
            tvMenuMesQq.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicTools.joinQQGroup(context, schoolMes.getQqgroup());
                }
            });
        }
    }

    @Override
    public void setmenu_message_llshow() {
        llMenuMes.setVisibility(View.VISIBLE);
    }

    @Override
    public void setmenu_message_llhide() {
        llMenuMes.setVisibility(View.GONE);
    }

    @Override
    public void setmenu_message_cardshow() {
        cardMenuMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void setmenu_message_cardhide() {
        cardMenuMessage.setVisibility(View.GONE);
    }

    @Override
    public void setmenu_message_qqshow() {
        tvMenuMesQq.setVisibility(View.VISIBLE);
    }

    @Override
    public void setmenu_message_qqhide() {
        tvMenuMesQq.setVisibility(View.GONE);
    }

    @Override
    public void setmenu_message_signshow() {
        tvMenuMesSign.setVisibility(View.VISIBLE);
    }

    @Override
    public void setmenu_message_signhide() {
        tvMenuMesSign.setVisibility(View.GONE);
    }

    /**
     * 设置表白墙卡片
     */
    @Override
    public void setmenu_love(List<Love> list) {
        view.findViewById(R.id.ll_item_menu_love).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//底部更多表白按钮
                startActivity(new Intent(context, LoveActivity.class));
            }
        });
        recycleItemMenuLove.setHasFixedSize(true);

        SnapHelper snapHelperStart = new GravitySnapHelper(Gravity.START);
        snapHelperStart.attachToRecyclerView(recycleItemMenuLove);

        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycleItemMenuLove.setLayoutManager(layoutManager);

        MenuLoveAdapter mAdapter = new MenuLoveAdapter(list, context);
        recycleItemMenuLove.setAdapter(mAdapter);
        mAdapter.addEnditem();

    }

    @Override
    public void setmenu_love_cardshow() {
        cardMenuLove.setVisibility(View.VISIBLE);
    }

    @Override
    public void setmenu_love_cardhide() {
        cardMenuLove.setVisibility(View.GONE);
    }

    @Override
    public void setmenu_library_cardshow(boolean isshow) {
        int show = isshow ? View.VISIBLE : View.GONE;
        cardMenuLibrary.setVisibility(show);
    }

    /**
     * 设置图书馆卡片
     */
    @Override
    public void setmenu_librarydata(String str) {
        view.findViewById(R.id.tv_menu_library_select).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//底部搜索图书
                startActivity(new Intent(context, Library_selectActivity.class));
            }
        });
        view.findViewById(R.id.tv_menu_library).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//底部查询更多
                startActivity(new Intent(context, LibraryActivity.class));
            }
        });

        try {
            JSONArray js = new JSONArray(str);
            int size = js.length();
            TextView tv = (TextView) view.findViewById(R.id.tv_menu_library_num);
            tv.setText("在借书籍：" + size + "本");

            int[] ll = {R.id.ll_menu_library1, R.id.ll_menu_library2, R.id.ll_menu_library3};
            int[] tv_name = {R.id.tv_menu_library_name1, R.id.tv_menu_library_name2, R.id.tv_menu_library_name3};
            int[] tv_info = {R.id.tv_menu_library_info1, R.id.tv_menu_library_info2, R.id.tv_menu_library_info3};

            for (int i = 0; i < size; i++) {
                if (i >= 3) {//当大于3本不显示
                    break;
                }
                LinearLayout llyout = (LinearLayout) view.findViewById(ll[i]);
                llyout.setVisibility(View.VISIBLE);

                JSONObject jo = (JSONObject) js.opt(i);
                String book_name = jo.getString("BT");
                String book_autor = jo.getString("DYZZ");

                tv = (TextView) view.findViewById(tv_name[i]);
                tv.setText("《" + book_name + "》");
                tv = (TextView) view.findViewById(tv_info[i]);
                tv.setText(book_autor + "        剩余天数：" + TimeTools.getday(jo.getString("YGHSJ")) + "天");
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置跳蚤市场卡片
     */
    @Override
    public void setmenu_goodsdata(List<Good> list) {
        view.findViewById(R.id.ll_item_menu_goods).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {//底部更多商品
                MainActivity.setBottomNavigation(1);
            }
        });
        recycleItemMenuGoods.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        recycleItemMenuGoods.setLayoutManager(layoutManager);
        MenuGoodsAdapter mAdapter = new MenuGoodsAdapter(list, context);
        recycleItemMenuGoods.setAdapter(mAdapter);
    }


    /////////////////////////////////////////////////////////////////////////////////////
    /**
     *
     */
    OnClickListener listener_ = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    /**
     * 一卡通查询
     */
    OnClickListener listener_select_card = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 判断是否已经登录
            if (login) {// 表示已经成功登录过
                startActivity(new Intent(context, Query_cardActivity.class));
            } else {
                showsnack();
            }
        }
    };
    /**
     * 学生信息查询
     */
    OnClickListener listener_student_select = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 判断是否已经登录
            if (login) {// 表示已经成功登录过
                startActivity(new Intent(context, QueryActivity.class));
            } else {
                showsnack();
            }
        }
    };
    /**
     * 一卡通工具
     */
    OnClickListener listener_card_tools = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (login) {// 表示已经成功登录过
                startActivity(new Intent(context, CardToolsActivity.class));
            } else {
                showsnack();
            }
        }
    };
    /**
     * 免费会员
     */
    OnClickListener listener_vip = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Snackbar.make(v, "1. 记住邀请码：223366\r\n2. 进入之后粘贴邀请码", Snackbar.LENGTH_LONG).setAction("复制并打开", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PublicTools.copy("223366");
                    startActivity(new Intent(context, Cengfan_Activity.class));
                }
            }).show();
        }
    };
    /**
     * 同学的店
     */
    OnClickListener listener_store = new OnClickListener() {
        @Override
        public void onClick(View v) {
                    startActivity(new Intent(context, Store_Activity.class));
        }
    };
    /**
     * 学生证
     */
    OnClickListener listener_student = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, StudentActivity.class));
        }
    };
    /**
     * 校历
     */
    OnClickListener listener_calan = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, Calan_Activity.class));
        }
    };

    /**
     * 课表
     */
    OnClickListener listener_course = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 判断是否已经登录
            if (login) {// 表示已经成功登录过
                Intent intent = new Intent(context, CourseActivity.class);
                startActivity(intent);
            } else {
                showsnack();
            }
        }
    };
    /**
     * 正方系统
     */
    OnClickListener listener_zfxt = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 判断是否已经登录
            if (login) {// 表示已经成功登录过
                Intent intent = new Intent(context, Score_zfxtActivity.class);
                startActivity(intent);
            } else {
                showsnack();
            }
        }
    };
    /**
     * 图书借阅
     */
    OnClickListener listener_library = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 判断是否已经登录
            if (login) {// 表示已经成功登录过
                startActivity(new Intent(context, LibraryActivity.class));
            } else {
                showsnack();
            }
        }
    };

    /**
     * 表白墙
     */
    OnClickListener listener_love = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, LoveActivity.class));
        }
    };

    /**
     * 更多功能
     */
    OnClickListener listener_more = new OnClickListener() {
        @Override
        public void onClick(View v) {
            MainActivity.openmore();
        }
    };

    /**
     * 消费记录
     */
    OnClickListener listener_money = new OnClickListener() {
        @Override
        public void onClick(View v) {
            // 判断是否已经登录
            if (login) {// 表示已经成功登录过
                startActivity(new Intent(context, Card_Activity.class));
            } else {
                showsnack();
            }
        }
    };

    /**
     * 学校黄页
     */
    OnClickListener listener_yellow = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, Sqlite_selectActivity.class));
        }
    };
    /**
     * 校园新闻
     */
    OnClickListener listener_news = new OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(context, NewsActivity.class));
        }
    };
    /**
     * 校车
     */
    OnClickListener listener_bus = new OnClickListener() {
        @Override
        public void onClick(View v) {
            int pic_id = R.drawable.school_bus;
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("pic_id", pic_id);
            intent.putExtra("title_name", "校车");
            startActivity(intent);
        }
    };


    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @return show  snack
     */
    public void showsnack() {
        Snackbar.make(MainActivity.bottomNavigation, "登录校园帐号才能使用", Snackbar.LENGTH_LONG).setAction("登录", new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginSchool_Activity.class));
            }
        }).show();
    }


    /**
     * 刷新当前一卡通余额
     */
    public void refresh_money() {
        new Thread() {
            public void run() {
                Integer change = LoginService.query_money(context);
                Message mes = null;
                if (change == 0) {
                    // 刷新成功保存当前余额到到shareprefence
                    mes = new Message();
                    mes.what = refresh_success;
                } else if (change == 2) {
                    // 刷新失败
                    mes = new Message();
                    mes.what = refresh_nochange;
                } else if (change == 3) {
                    // 刷新失败
                    mes = new Message();
                    mes.what = refresh_fail;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }

}
