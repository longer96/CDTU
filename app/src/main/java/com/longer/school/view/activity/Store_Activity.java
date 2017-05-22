package com.longer.school.view.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.adapter.FoodAdapter;
import com.longer.school.adapter.Store_selectStoreAdapter;
import com.longer.school.modle.bean.Food;
import com.longer.school.modle.bean.Store;
import com.longer.school.presenter.Store_ActivityPresenter;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.longer.school.view.iview.IStore_ActivityView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.goodiebag.carouselpicker.CarouselPicker;
import q.rorbin.badgeview.Badge;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.TabView;

public class Store_Activity extends AppCompatActivity implements IStore_ActivityView {

    public Context context;
    public static LinearLayoutManager linearLayoutManager;
    public static List<Food> list = null;
    public FoodAdapter foodAdapter;
    @Bind(R.id.tv_store_null)
    TextView tvStoreNull;
    private Boolean atbottom = true;
    RecyclerView recyclerView;//bottomsheetdialog的对话框
    private String select_type = null;//商品类型

    @Bind(R.id.ll_store_car)
    LinearLayout llStoreCar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.recycle_storp)
    RecyclerView recycleStorp;
    @Bind(R.id.swipe)
    SwipeRefreshLayout swipe;
    private PopupWindow mPopWindows;// 选择宿舍
    public static int i;//初始查询10条数据
    private BottomSheetDialog mBottomSheetDialog = null;
    private List<Store> storeList = new ArrayList<Store>();//保存店铺信息，看是单个的店铺还是所有店铺

    private Store_ActivityPresenter presenter = new Store_ActivityPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_store);
        ButterKnife.bind(this);
        toolbar.setTitle("同学的店");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        init();
    }

    public void init() {
        context = Store_Activity.this;
        initTab();

        linearLayoutManager = new LinearLayoutManager(context);
        recycleStorp.setItemAnimator(new DefaultItemAnimator());
        recycleStorp.setLayoutManager(linearLayoutManager);
        recycleStorp.addOnScrollListener(onScrollListener);

        setSwipe();
        initData();

        i = 10;
    }

    /**
     * 检查是否选择过宿舍，如果没有弹出对话框，
     * 如果有，直接读取数据，进行查询
     */
    private void initData() {
        if (SelectSushe()) {
            String sushe = FileTools.getshareString("sushe");
            presenter.getData();
            toolbar.setSubtitle(sushe + "舍 -  所有店铺");
            //先设置i = 10;
            i = 10;
        }
    }

    private boolean SelectSushe() {
        //检查是否选择过宿舍，如果没有弹出对话框，如果有，直接读取数据，进行查询
        String sushe = FileTools.getshareString("sushe");
        if ("".equals(sushe)) {//我们发现不能在没有显示的时候创建popupwindows,所以我们添加一个snack
            Snackbar.make(llStoreCar, "请先选择一栋宿舍！", Snackbar.LENGTH_INDEFINITE)
                    .setAction("选择", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            select_sushe();
                        }
                    })
                    .show();
            return false;
        } else {//如果之前已经保存过，则直接进行查询
            return true;
        }
    }

    private void setSwipe() {
        swipe.setColorSchemeColors(Color.parseColor("#FF4081"));
        swipe.setOnRefreshListener(onRefreshListener);
    }

    /**
     * 下拉刷新
     */
    SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        @Override
        public void onRefresh() {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    //我们暂时在下拉刷新里不做什么事
                    showrefresh(false);
                }
            }, 1200);
        }
    };

    /**
     * 设置滑动监听，实现上拉加载更多
     */
    public RecyclerView.OnScrollListener onScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int visible = linearLayoutManager.getChildCount();
            int total = linearLayoutManager.getItemCount();
            int past = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
            if ((visible + past) >= total) {
                if (atbottom) {
                    presenter.getFood(i, storeList, select_type);
                    atbottom = false;
                }
            }
        }
    };

    /**
     * 选择寝室
     */
    private void select_sushe() {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_select_room, null);
        mPopWindows = new PopupWindow(contentView);
        mPopWindows.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindows.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        mPopWindows.setFocusable(true);
        mPopWindows.setBackgroundDrawable(new BitmapDrawable());

        final CarouselPicker carouselPicker = (CarouselPicker) contentView.findViewById(R.id.carousel);
        List<CarouselPicker.PickerItem> textItems = new ArrayList<>();
        //20 here represents the textSize in dp, change it to the value you want.
        textItems.add(new CarouselPicker.TextItem("1舍", 20));
        textItems.add(new CarouselPicker.TextItem("2舍", 20));
        textItems.add(new CarouselPicker.TextItem("3舍", 20));
        textItems.add(new CarouselPicker.TextItem("4舍", 20));
        textItems.add(new CarouselPicker.TextItem("5舍", 20));
        textItems.add(new CarouselPicker.TextItem("6舍", 20));
        textItems.add(new CarouselPicker.TextItem("7舍", 20));
        CarouselPicker.CarouselViewAdapter textAdapter = new CarouselPicker.CarouselViewAdapter(context, textItems, 0);
        carouselPicker.setAdapter(textAdapter);
        int i = 2;
        if (!"".equals(FileTools.getshareString("sushe"))) {
            i = Integer.parseInt(FileTools.getshareString("sushe")) - 1;
        }
        carouselPicker.setCurrentItem(i);

        contentView.findViewById(R.id.ll_poplove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindows.dismiss();
            }
        });
        contentView.findViewById(R.id.card_poplove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        contentView.findViewById(R.id.select_room_quit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopWindows.dismiss();
            }
        });
        contentView.findViewById(R.id.select_room_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int sushe = carouselPicker.getCurrentItem() + 1;
                FileTools.saveshareString("sushe", sushe + "");
                mPopWindows.dismiss();
                initData();//再次执行，从新获取数据
            }
        });

        mPopWindows.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    private void showdialog(boolean isshow) {
        if (isshow) {
            mBottomSheetDialog.show();
        } else {
            if (mBottomSheetDialog.isShowing()) {
                mBottomSheetDialog.dismiss();
            }
        }
    }


    private void initTab() {
        final VerticalTabLayout tablayout = (VerticalTabLayout) findViewById(R.id.tablayout);
        tablayout.setTabAdapter(new MytabAdapter());
        tablayout.setTabBadge(1, 32);
        tablayout.setTabBadge(2, -1);
        tablayout.setTabBadge(4, -1);
        tablayout.addOnTabSelectedListener(onTabSelectedListener);

    }

    /**
     * 同学的店，标签的点击事件
     */
    VerticalTabLayout.OnTabSelectedListener onTabSelectedListener = new VerticalTabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabView tab, int position) {
            if (SelectSushe()) {
                select_type = tab.getTitle().getContent();
                presenter.getFood(0, storeList, select_type);
            }
        }

        @Override
        public void onTabReselected(TabView tab, int position) {
        }
    };

    @Override
    public void getFoodFailed(String e) {
        Toast.show("查询寝室失败：" + e);
    }

    @Override
    public void addData(List<Food> list) {

        foodAdapter.notifyDataSetChanged(list);
    }

    @Override
    public void setData(List<Food> list) {
        i = 10;
        this.list = list;
        foodAdapter = new FoodAdapter(list, context);
        recycleStorp.setAdapter(foodAdapter);
        foodAdapter.setOnItemclicklister(itemclick);
        atbottom = true;
        tvStoreNull.setVisibility(View.GONE);
        if (list.size() == 0) {
            tvStoreNull.setVisibility(View.VISIBLE);
        }

    }

    @Override
    //查询宿舍，将返回的小卖部放入适配器bottomsheetdialog
    public void createBottomSheetDialog(List<Store> list) {

        mBottomSheetDialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_bottomsheet_store, null, false);
        mBottomSheetDialog.setContentView(view);


        recyclerView = (RecyclerView) view.findViewById(R.id.recycle_zxcj);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        setBehaviorCallback();

        Store_selectStoreAdapter adapter = new Store_selectStoreAdapter(list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemclicklister(new Store_selectStoreAdapter.Itemclick() {
            @Override
            public void Onclick(View view, List<Store> list1) {
                showdialog(false);
                if (!list1.get(0).isState()) {
                    Toast.show("别人已经休息了哦~");
                } else {
                    toolbar.setSubtitle(FileTools.getshareString("sushe") + "舍 -  " + list1.get(0).getName());
                    //设置新的数据，自动小卖部
                    i = 10;
                    presenter.getFood(0, list1, select_type);
                    storeList = list1;
                }
            }
        });
    }

    @Override
    public void getStoreSuccess(List<Store> list) {
        presenter.getFood(0, list, select_type);
        storeList = list;
    }

    private void setBehaviorCallback() {
        View view = mBottomSheetDialog.getDelegate().findViewById(android.support.design.R.id.design_bottom_sheet);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(view);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    mBottomSheetDialog.dismiss();
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }


    /**
     * 每一行的单击事件
     */
    FoodAdapter.Itemclick itemclick = new FoodAdapter.Itemclick() {
        public void OnItemclick(View v, int position) {
//            Intent intent = new Intent(context,Food_Activity.class);
//            intent.putExtra("food",list.get(position));
//            startActivity(intent);
            Toast.show("点击了： " + position);
            FileTools.saveshareString("sushe", "");
        }
    };

    @Override
    public void showrefresh(final boolean isshow) {
        swipe.post(new Runnable() {
            @Override
            public void run() {
                if (isshow) {
                    if (!swipe.isRefreshing()) {
                        swipe.setRefreshing(true);
                    }
                } else {
                    if (swipe.isRefreshing()) {
                        swipe.setRefreshing(false);
                    }
                }
            }
        });
    }


    class MytabAdapter implements TabAdapter {

        List<String> titles;

        public MytabAdapter() {
            titles = new ArrayList<>();
            Collections.addAll(titles, "所有商品", "方便速食", "休闲零食", "饮料", "生活用品", "其他");
        }

        @Override
        public int getCount() {
            return titles.size();
        }

        @Override
        public TabView.TabBadge getBadge(int position) {
            if (position == 3)
                return new TabView.TabBadge.Builder().setBadgeNumber(66)
                        .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                            @Override
                            public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                            }
                        }).build();
            return null;
        }

        @Override
        public TabView.TabIcon getIcon(int position) {
            return null;
        }

        @Override
        public TabView.TabTitle getTitle(int position) {
            return new TabView.TabTitle.Builder()
                    .setContent(titles.get(position))
                    .setTextColor(Color.WHITE, 0xBB303030)
                    .setTextSize(13)
                    .build();
        }

        @Override
        public int getBackground(int position) {
            return 0;
        }
    }


    /**
     * 三点菜单
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_store, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_store_search) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick({R.id.tv_store_selectss, R.id.tv_store_selectstore})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_store_selectss:
                select_sushe();
                break;
            case R.id.tv_store_selectstore:
                if (SelectSushe() && mBottomSheetDialog != null)
                    mBottomSheetDialog.show();
                break;
        }
    }

}
