package com.longer.school.view.activity;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.gordonwong.materialsheetfab.MaterialSheetFab;
import com.longer.school.Application;
import com.longer.school.modle.bean.Appinfor;
import com.longer.school.modle.bean.Message;
import com.longer.school.modle.bean.User;
import com.longer.school.Config;
import com.longer.school.R;
import com.longer.school.view.fragment.Fragment_Card;
import com.longer.school.view.fragment.Fragment_Goods;
import com.longer.school.view.fragment.Fragment_Lost;
import com.longer.school.view.fragment.Fragment_Menu;
import com.longer.school.utils.Fab;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.PublicTools;
import com.longer.school.utils.Toast;
import com.nineoldandroids.view.ViewHelper;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static Toolbar toolbar;
    private TextView tv_title;
//    private TextView tv_gg;//跳蚤市场公告
    public static AHBottomNavigation bottomNavigation;
    public Fab fab;
    private MaterialSheetFab materialSheetFab;
    private static DrawerLayout drawer;
    private Context context;
    public static MainActivity instance = null;//暴露给其他位置关闭主界面
    private long exitTime;//记录2次返回键的时间

    private Fragment_Menu fg_menu;//组界面
    private Fragment_Card fg_card;//一卡通界面
    private Fragment_Goods fg_good;//跳蚤市场
    private Fragment_Lost fg_lost;//失物招领

    private OnFabClickedListener onFabClickedListener;//查询接口
//    private OnFabClickedListener2 onFabClickedListener2;//筛选接口

    public static BottomSheetBehavior bottomSheetBehavior;//底部小菜单


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_main);
        instance = MainActivity.this;
        context = MainActivity.this;
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        showfragmenu();
        setFab();
        setbottom();
        init();
        bmob_checkup();//检查更新
        check_message();//检查有没有系统消息
        //如果点击了消费记录通知，跳转到消费记录界面
        bottomNavigation.setCurrentItem(getIntent().getIntExtra("it", 0));
    }

    /**
     * 检查有没有系统消息
     */
    private void check_message() {
        BmobQuery<Message> bmobQuery = new BmobQuery<Message>();
        bmobQuery.order("-updatedAt");
        bmobQuery.setLimit(1);//我们只需要第一条消息
        bmobQuery.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    //如果得到的id 不等于我们之前保存过得而id 那么就显示出来
                    String id = list.get(0).getObjectId();
                    String id_share = FileTools.getshareString("message");
                    if (list.get(0).isShow() && !id.equals(id_share)) {
                        showMessage(id, list.get(0).getInfor());
                    }
                } else {
                    Log.d("info_activity", "没有发现新消息或者出异常了");
                }
            }
        });

    }

    /**
     * 显示消息,确定后保存id
     */
    private void showMessage(final String id, String mes) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle("系统提示");
        builder.setCancelable(true);
        builder.setMessage(mes);
        builder.setPositiveButton("朕已阅", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileTools.saveshareString("message", id);
            }
        });
        builder.show();
    }

    /**
     * 默认加载主界面
     */
    private void showfragmenu() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
        if (fg_menu == null) {
            fg_menu = new Fragment_Menu();
            ft.add(R.id.content, fg_menu);
        }
        ft.show(fg_menu);
        ft.commit();
    }

    public void init() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("成都工业学院");

//        tv_gg = (TextView) findViewById(R.id.tv_goods_gg);
//        tv_gg.setVisibility(View.GONE);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView tv_name = (TextView) headerView.findViewById(R.id.tv_menu_name);
        TextView tv_class = (TextView) headerView.findViewById(R.id.tv_menu_class);
        String str = FileTools.getshare(this, "login");
        if ("true".equals(str)) {// 表示已经成功登录过
            tv_class.setText(FileTools.getshareString("banji"));
            tv_name.setText(FileTools.getshareString("name"));
            User bmobUser = BmobUser.getCurrentUser(User.class);
            if (bmobUser != null) {
                // 允许用户使用应用
                Log.d("tip", "缓存账户不为空" + bmobUser.getName());
                Application.setuser(bmobUser);
            } else {//如果为空自动进行登录
                Log.d("tip", "缓存账户为空自动登录");
                LoginService.loginbmob(FileTools.getshareString("username"), FileTools.getshareString("password"));
            }
        }
        drawer.addDrawerListener(drawerListener);

        bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.tab_layout));
    }

    /**
     * 打开更多菜单
     */
    public static void openmore() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }
    }

    /**
     * 设置FlotingButton
     */
    private void setFab() {
        fab = (Fab) findViewById(R.id.fab);
        View sheetView = findViewById(R.id.fab_sheet);
        View overlay = findViewById(R.id.overlay);
        int sheetColor = R.color.colorPrimary;
        int fabColor = R.color.colorAccent;

        materialSheetFab = new MaterialSheetFab<>(fab, sheetView, overlay, sheetColor, fabColor);
        findViewById(R.id.fab_goods_fb).setOnClickListener(listener_fb);
        findViewById(R.id.fab_goods_select).setOnClickListener(listener_cx);
//        findViewById(R.id.fab_goods_sx).setOnClickListener(listener_sx);
        findViewById(R.id.fab_goods_my).setOnClickListener(listener_my);
        fab.hide();
    }

    //接口，  0：商品查询  1：失物查询
    public interface OnFabClickedListener {
        void onclicked(int num);
    }

    //接口，  0：商品筛选  1：失物筛选
//    public interface OnFabClickedListener2 {
//        void onclicked(int num);
//    }

    /**
     * fab 的筛选 点击事件
     */
//    View.OnClickListener listener_sx = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            //还要判断登录
//            int i = bottomNavigation.getCurrentItem();
//            if (i == 1) {
//                onFabClickedListener2.onclicked(0);
//            } else if (i == 2) {
//                onFabClickedListener2.onclicked(1);
//            }
//            materialSheetFab.hideSheet();
//        }
//    };

    /**
     * fab 的查询 点击事件
     */
    View.OnClickListener listener_cx = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //还要判断登录
            int i = bottomNavigation.getCurrentItem();
            if (i == 1) {
                onFabClickedListener.onclicked(0);
            } else if (i == 2) {
                onFabClickedListener.onclicked(1);
            }
            materialSheetFab.hideSheet();
        }
    };

    /**
     * 查询接口
     */
    public void setFabClickedListener_cx(OnFabClickedListener fabClickedListener) {
        this.onFabClickedListener = fabClickedListener;
    }

    /**
     * 筛选接口
     */
//    public void setFabClickedListener_sx(OnFabClickedListener2 fabClickedListener) {
//        this.onFabClickedListener2 = fabClickedListener;
//    }

    /**
     * fab 的发布 点击事件
     */
    View.OnClickListener listener_fb = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            materialSheetFab.hideSheet();
            if (!checkupload()) {
                return;
            }
            int i = bottomNavigation.getCurrentItem();
            if (i == 1) {
                startActivity(new Intent(context, Add_goodsActivity.class));
            } else if (i == 2) {
                startActivity(new Intent(context, Add_lostActivity.class));
            }
        }
    };
    /**
     * fab 的个人中心 点击事件
     */
    View.OnClickListener listener_my = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            materialSheetFab.hideSheet();
            if (!checkupload()) {
                return;
            }
            int i = bottomNavigation.getCurrentItem();
            if (i == 1) {
                startActivity(new Intent(context, MyGoods_Activity.class));
            } else if (i == 2) {
                startActivity(new Intent(context, MyLost_Activity.class));
            }
        }
    };

    /**
     * 设置抽屉动画
     */
    DrawerLayout.DrawerListener drawerListener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            View mContent = drawer.getChildAt(0);//返回抽屉布局中的索引为0的子view
            View mMenu = drawerView;
            float scale = 1 - slideOffset;//偏移量导致scale从1.0-0.0
            float rightScale = 0.8f + scale * 0.2f;//将内容区域从1.0-0.0转化为1.0-0.8
            float leftScale = 1 - 0.3f * scale;//0.7-1.0
            ViewHelper.setScaleX(mMenu, leftScale);
            ViewHelper.setScaleY(mMenu, leftScale);
            ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));//开始这里设置成了这样，导致背景透明度有1.0-0.6
            ViewHelper.setTranslationX(mContent,
                    mMenu.getMeasuredWidth() * (1 - scale));
            ViewHelper.setPivotX(mContent, 0);
            ViewHelper.setPivotY(mContent,
                    mContent.getMeasuredHeight() / 2);
            mContent.invalidate();
            ViewHelper.setScaleX(mContent, rightScale);
            ViewHelper.setScaleY(mContent, rightScale);
        }

        @Override
        public void onDrawerOpened(View drawerView) {

        }

        @Override
        public void onDrawerClosed(View drawerView) {
            drawer.setDrawerLockMode(
                    DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };


    /**
     * 检查是否已经登录，没有返回false 并且提示更新
     *
     * @return false 没有登录
     */
    public boolean checkupload() {
        String str = FileTools.getshareString("login");
        if (!"true".equals(str)) {// 表示已经成功登录过
            Snackbar.make(bottomNavigation, "登录校园帐号才能使用", Snackbar.LENGTH_LONG).setAction("登录", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, LoginSchool_Activity.class));
                }
            }).show();
            return false;
        }
        return true;
    }

    public void setbottom() {
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
// Create items
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("首页", R.drawable.ic_main_main, R.color.colorPrimary);
//        AHBottomNavigationItem item2 = new AHBottomNavigationItem("一卡通", R.drawable.ic_main_card, R.color.colorPrimary);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("跳蚤市场", R.drawable.ic_main_tzsc, R.color.colorAccent);
        AHBottomNavigationItem item4 = new AHBottomNavigationItem("失物招领", R.drawable.ic_main_swzl, R.color.colorAccent);

// Add items
        bottomNavigation.addItem(item1);
//        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.addItem(item4);
        bottomNavigation.setBehaviorTranslationEnabled(false);
        bottomNavigation.setForceTitlesDisplay(false);

        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#fafbfe"));


// Set listeners
        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if (materialSheetFab.isSheetVisible()) {
                    materialSheetFab.hideSheet();
                    return false;
                }
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                if (position == 0 && !wasSelected) {
                    setToolBarVisibale(true);
                    settitle("成都工业学院");
                    showfab(false);
                    //隐藏所有fragment
                    hideFragment(ft);
                    if (fg_menu == null) {
                        fg_menu = new Fragment_Menu();
                        ft.add(R.id.content, fg_menu);
                    }
                    ft.show(fg_menu);
//                    showgg(false);
                } else if (position == 1 && !wasSelected) {
                    settitle("跳蚤市场");
                    showfab(true);
                    hideFragment(ft);
                    if (fg_good == null) {
                        fg_good = new Fragment_Goods();
                        ft.add(R.id.content, fg_good);
                    }
                    ft.show(fg_good);
//                    showgg(true);
                } else if (position == 2 && !wasSelected) {
                    settitle("失物招领");
                    showfab(true);
                    hideFragment(ft);
                    if (fg_lost == null) {
                        fg_lost = new Fragment_Lost();
                        ft.add(R.id.content, fg_lost);
                    }
                    ft.show(fg_lost);
//                    showgg(false);
                }
                ft.commit();
                return true;
            }
        });
    }

    /**
     * 方法，外面用来设置底部菜单所在位置
     *
     * @param num
     */
    public static void setBottomNavigation(int num) {
        bottomNavigation.setCurrentItem(num);
    }

    /**
     * 显示或者隐藏fab
     */
    private void showfab(boolean show) {
        if (show) {
            if (!fab.isShown())
                fab.show();
        } else {
            if (fab.isShown())
                fab.hide();
        }
    }

    /**
     * 显示公告按钮
     */
//    private void showgg(boolean show) {
//        if (show) {
//            tv_gg.setVisibility(View.VISIBLE);
//            tv_gg.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, ImageActivity.class);
//                    intent.putExtra("pic_id", R.drawable.goods_gg);
//                    intent.putExtra("title_name", "公告");
//                    startActivity(intent);
//                }
//            });
//        } else {
//            tv_gg.setVisibility(View.GONE);
//        }
//    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        if (fg_menu != null) {
            transaction.hide(fg_menu);
        }
//        if (fg_card != null) {
//            transaction.hide(fg_card);
//        }
        if (fg_good != null) {
            transaction.hide(fg_good);
            fg_good = null;
        }
        if (fg_lost != null) {
            transaction.hide(fg_lost);
            fg_lost = null;
        }
    }

    /**
     * 点击返回按键
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //检查抽屉是否关闭
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return false;
        }
        //检查fab是否展开
        if (materialSheetFab.isSheetVisible()) {
            materialSheetFab.hideSheet();
            return false;
        }
        //检查sheetbehavior是否展开
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            return false;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.show("再按一次退出");
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
        }
        return true;
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_user) {
            startActivity(new Intent(context, User_Activity.class));
        } else if (id == R.id.nav_skin) {
            Intent intent = new Intent(context, SkinActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_set) {
            startActivity(new Intent(context, SetActivity.class));
        } else if (id == R.id.nav_share) {
            PublicTools.shareapp(context);
        } else if (id == R.id.nav_infor) {
            startActivity(new Intent(context, Info_Activity.class));
        } else if (id == R.id.nav_comment) {
            PublicTools.comment(context);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * 接口，用来设置标题
     * @param title
     */
    public void settitle(String title) {
        tv_title.setText(title);
    }

    /**
     * 用来设置toolbar的隐藏与显示
     * @param isshow
     */
    public static void setToolBarVisibale(boolean isshow){
        if(isshow){
            toolbar.setVisibility(View.VISIBLE);
        }else{
            toolbar.setVisibility(View.GONE);
        }
    }


    /**
     * 用来检查更新
     */
    private void bmob_checkup() {
        //如果缓存里有更新提示，我们不用再匹配服务器版本
        String updatenow = FileTools.getshareString("updatenow");//如果为""表示没有缓存的强制更新，
        Log.d("tip","updatenow" + updatenow);
        if (!"".equals(updatenow)) {
            Appinfor Appinfor = null;
            updata(Appinfor);
            return;
        }

        BmobQuery<Appinfor> bmobQuery = new BmobQuery<Appinfor>();
        bmobQuery.order("-updatedAt");
        bmobQuery.setLimit(1);//我们只需要第一条数据就可以了
        bmobQuery.findObjects(new FindListener<Appinfor>() {
            @Override
            public void done(List<Appinfor> list, BmobException e) {
                if (e == null && list != null) {
                    Appinfor appinfor = list.get(0);
                    //如果服务器的版本号大于客服端，提示用户升级
                    if (appinfor.getVersion() > Config.version) {
                        Log.d("tip", "发现新版本:" + appinfor.getVersion());
                        updata(appinfor);
                    }
                } else {
                    Log.d("info_activity", "更新出异常了(" + e.getErrorCode() + ")");
                }
            }
        });
    }

    /**
     * 有可用更新
     */
    public void updata(final Appinfor appinfor) {
        //如果新版本号等于我们忽略的版本号也不用提示用户
        String ignoreVersion = FileTools.getshareString("ignoreVersion");
        if (appinfor != null && !"".equals(ignoreVersion) && ignoreVersion.equals(appinfor.getVersion() + "")) {
            Log.d("版本更新提示", "已经忽略该次更新");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("发现新版本");
        builder.setCancelable(false);
        String message = appinfor == null ? FileTools.getshareString("newVersion_Infor") : appinfor.getInfor();
        builder.setMessage(message);
        builder.setPositiveButton("下载Apk", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.pre)));
                System.exit(0);

            }
        });
        builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        if (appinfor != null) {
            //我们不在通过固定的版本强制用户更新，这要会有一些bug,比如3.0版本是强制更新，但是3.1版本很快发布，如果3.1是非强制更新会造成2.*版本的用户继续使用。如果是强制更新的话又会影响3.0版本用户的体验
            //所以我们在3.0版本使用minversion，如果低于此版本的就会强制更新
            //最低的版本，也就是低于此版本都会强制他升级(最低版本不包含当前版本，也就是比如2.4，那么2.4版本也会强制更新)
            if (Config.version > appinfor.getMinversion()) {
                builder.setNeutralButton("不再提示", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FileTools.saveshareString("ignoreVersion", appinfor.getVersion() + "");
                    }
                });
            } else {//强制更新，添加本地缓存信息，防止没有网络的情况下用户无法收到更新通知
                FileTools.saveshareString("updatenow", appinfor.getVersion() + "");
                FileTools.saveshareString("newVersion_Infor", appinfor.getInfor());
            }
        }
        builder.show();
    }

}
