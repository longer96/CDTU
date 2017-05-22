package com.longer.school.view.activity;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.longer.school.Application;
import com.longer.school.adapter.Image_9Adapter;
import com.longer.school.modle.bean.CourseClass;
import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.TimeTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.yancy.imageselector.ImageConfig;
import com.yancy.imageselector.ImageLoader;
import com.yancy.imageselector.ImageSelector;
import com.yancy.imageselector.ImageSelectorActivity;
import com.yanzhenjie.permission.AndPermission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class CourseActivity extends AppCompatActivity implements View.OnClickListener {

    // 课程页面的button引用，6行7列
    private int[][] lessons = {
            {R.id.lesson11, R.id.lesson12, R.id.lesson13, R.id.lesson14, R.id.lesson15, R.id.lesson16, R.id.lesson17},
            {R.id.lesson21, R.id.lesson22, R.id.lesson23, R.id.lesson24, R.id.lesson25, R.id.lesson26, R.id.lesson27},
            {R.id.lesson31, R.id.lesson32, R.id.lesson33, R.id.lesson34, R.id.lesson35, R.id.lesson36, R.id.lesson37},
            {R.id.lesson41, R.id.lesson42, R.id.lesson43, R.id.lesson44, R.id.lesson45, R.id.lesson46, R.id.lesson47},
            {R.id.lesson51, R.id.lesson52, R.id.lesson53, R.id.lesson54, R.id.lesson55, R.id.lesson56,
                    R.id.lesson57}};
    // 某节课的背景图,用于随机获取
    private int[] bg = {R.drawable.kb1, R.drawable.kb2, R.drawable.kb3, R.drawable.kb4, R.drawable.kb5, R.drawable.kb6,
            R.drawable.kb7, R.drawable.kb8, R.drawable.kb9, R.drawable.kb10, R.drawable.kb11, R.drawable.kb12,
            R.drawable.kb13, R.drawable.kb14, R.drawable.kb15, R.drawable.kb16};

    private Context context;
    private ProgressDialog pg;
    private PopupWindow mPopWindows;// 课程详细
    private List<CourseClass> courses = new ArrayList<CourseClass>();
    private int position = -1;
    private final int success = 1;// 成功
    private final int fish = 2;// 失败
    public static CourseActivity instance = null;
    public ImageView iv_bg;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case success:
                    Toast.makeText(context, "刷新课表成功", Toast.LENGTH_SHORT).show();
                    pg.dismiss();
                    finish();
                    Intent intent = new Intent(context, CourseActivity.class);
                    startActivity(intent);
//                    setkjfs();
                    break;
                case fish:
                    pg.dismiss();
                    Toast.makeText(context, "获取课表失败了", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_course);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        String bg_toolbar = FileTools.getshareString("refreshcolor").replaceFirst("#","#10");
        toolbar.setBackgroundColor(Color.parseColor(bg_toolbar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        context = CourseActivity.this;
        instance = this;
        setbg();
        intivalue();
    }

    /**
     * 设置背景
     */
    private void setbg() {
        iv_bg = (ImageView) findViewById(R.id.iv_coures_bg);
        String bg_user = FileTools.getshareString("bg_course_diy");
        if (bg_user != null && !"".equals(bg_user)) {
            Glide.with(context)
                    .load(bg_user)
                    .placeholder(R.drawable.bg_course1)
                    .centerCrop()
                    .into(iv_bg);
            return;
        }

        int bg = FileTools.getshareInt("bg_course");
        bg = bg == 404 ? R.drawable.bg_course1 : bg;
        iv_bg.setImageResource(bg);
    }


    // 初始化变量
    public void intivalue() {
        TextView tv = (TextView) findViewById(R.id.activity_course_zhoushu);
        String str = "第" + TimeTools.course_zhoushu() + "周";
        tv.setText(str);
        // 先判断是否已经获取过课表
        str = FileTools.getshare(context, "course");
        if ("true".equals(str)) {// 表示已经成功获取过
            intiview();
        } else {// 第一次获取课表
            pg = new ProgressDialog(context);
            pg.setMessage("正在获取课表...");
            pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
            pg.show();
            new Thread() {
                public void run() {
                    Message mes = null;
                    if (LoginService.loginzfxt(context) && LoginService.logincourse(context)) {
                        mes = new Message();
                        mes.what = success;
                    } else {
                        mes = new Message();
                        mes.what = fish;
                    }
                    handler.sendMessage(mes);
                }
            }.start();
        }
    }

    // 解析课表
    public void intiview() {
        try {
            CourseClass course = null;
            String str = FileTools.getFile(context, "course.txt");
            if (str == "") {
                Toast.makeText(context, "课表为空，请刷新课表！", Toast.LENGTH_SHORT).show();
                return;
            }
            String[] str1 = str.split("@");
            for (String info : str1) {
                course = new CourseClass();
                String[] str2 = info.split("#");
                course.setBg(Integer.parseInt(str2[0]));
                course.setId(Integer.parseInt(str2[1]));
                course.setName(str2[2]);
                course.setRoom(str2[3]);
                course.setTeacher(str2[4]);
                course.setZoushu(str2[5]);
                courses.add(course);
                course = null;
            }

            int id, x, y;
            // 循环遍历
            for (int i = 0; i < courses.size(); i++) {
                Log.d("有多少节课：", courses.size() + "");
                course = courses.get(i);// 拿到当前课程
                id = course.getId();// 得到对应的位置
                y = (id / 10) % 10;//列
                x = id % 10;//行
                Log.d("行x：", x + "");
                Log.d("列y：", y + "");
                Button lesson = (Button) findViewById(lessons[x - 1][y - 1]);// 获得该节课的button
                // Log.d("背景图：", course.getBg() + "");
                int bgRes = bg[course.getBg()];// 随机获取背景色
                lesson.setBackgroundResource(bgRes);// 设置背景
                lesson.setText(course.getName() + "@" + course.getRoom());// 设置文本为课程名+“@”+教室
                lesson.setOnClickListener(this);
            }
        } catch (Exception e) {
            Toast.makeText(context, "解析课表出问题鸟~请联系开发者，么么哒", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    /**
     * 三点菜单
     *
     * @param menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_course, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_course_zz) {
            startActivity(new Intent(context, ZhuanzhouActivity.class));
            return true;
        } else if (id == R.id.action_course_setbg) {
            // 先判断是否有权限。
            if (AndPermission.hasPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // 有权限，直接do anything.
                checkpic();
            } else {
                //Log.d("tip","没有权限");
                // 申请权限。
                AndPermission.with(CourseActivity.this)
                        .requestCode(100)
                        .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .send();
            }
            return true;
        }else if (id == R.id.action_course_zxsj) {
            int pic_id = R.drawable.zxsj;
            Intent intent = new Intent(context, ImageActivity.class);
            intent.putExtra("pic_id", pic_id);
            intent.putExtra("title_name", "教学时间表");
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static final int REQUEST_CODE = 1000;
    private ArrayList<String> path = new ArrayList<>();

    /**
     * 选择照片
     */
    public void checkpic() {
//        RecyclerView recycler = (RecyclerView) findViewById(R.id.recy_addlost);

        ImageConfig imageConfig = new ImageConfig.Builder(new GlideLoader())
                .steepToolBarColor(Color.parseColor(FileTools.getshareString("refreshcolor")))
                .titleBgColor(Color.parseColor(FileTools.getshareString("refreshcolor")))
                .titleSubmitTextColor(getResources().getColor(R.color.white))
                .titleTextColor(getResources().getColor(R.color.white))
                .crop(9, 16, 1080, 1920)
                .singleSelect()
                .pathList(path)
                .requestCode(REQUEST_CODE)
                .build();
        ImageSelector.open((Activity) context, imageConfig);   // 开启图片选择器
    }

    public class GlideLoader implements ImageLoader {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context)
                    .load(path)
                    .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                    .centerCrop()
                    .into(imageView);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && data != null) {
            List<String> pathList = data.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT);
            for (String path : pathList) {
                Log.i("ImagePathList", path);
                //保存到shareprefresh
                FileTools.saveshareString("bg_course_diy", path);
                Glide.with(context)
                        .load(path)
                        .placeholder(com.yancy.imageselector.R.mipmap.imageselector_photo)
                        .centerCrop()
                        .into(iv_bg);
            }
        }
    }


    /**
     * 课程详细
     *
     * @param course
     * @param position
     */
    public void Course(CourseClass course, int position) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.popup_course, null);
        mPopWindows = new PopupWindow(contentView);
        mPopWindows.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows.setFocusable(true);
        mPopWindows.setBackgroundDrawable(new BitmapDrawable());

        TextView tv_name = (TextView) contentView.findViewById(R.id.popup_name);
        TextView tv_locationg = (TextView) contentView.findViewById(R.id.popup_location);
        TextView tv_teacher = (TextView) contentView.findViewById(R.id.popup_teacher);
        TextView tv_day = (TextView) contentView.findViewById(R.id.popup_day);
        Button but_delete = (Button) contentView.findViewById(R.id.course_delete);

        tv_name.setText(course.getName());
        tv_locationg.setText(course.getRoom());
        tv_teacher.setText(course.getTeacher());
        tv_day.setText(course.getZoushu());
        but_delete.setOnClickListener(this);

        mPopWindows.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    // 底部刷新课程按键
    public void onclick_refresh(View view) {
        pg = new ProgressDialog(context);
        pg.setMessage("正在获取课表...");
        pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
        pg.show();
        new Thread() {
            public void run() {
                Message mes = new Message();
                if (LoginService.loginzfxt(context) && LoginService.logincourse(context)) {
                    mes.what = success;
                } else {
                    mes.what = fish;
                }
                handler.sendMessage(mes);
            }
        }.start();
    }

    // 底部添加课程按键
    public void onclick_add(View view) {
        Intent intent = new Intent(context, Course_addActivity.class);
        startActivity(intent);
    }

    // 底部查看调课按键
    public void onclick_select(View view) {
        Intent intent = new Intent(context, Course_editActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            // 删除课程
            case R.id.course_delete: {
                // Toast.makeText(context, courses.get(position).getName()+"ok",
                // 0).show();
                Builder builder = new Builder(context);
                builder.setMessage("确认删除 " + courses.get(position).getName() + " ?");
                builder.setPositiveButton("删除", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (position != -1) {
                            courses.remove(position);
                            StringBuffer sb = new StringBuffer();
                            for (CourseClass info : courses) {
                                sb.append(info.getBg());
                                sb.append("#");
                                sb.append(info.getId());
                                sb.append("#");
                                sb.append(info.getName());
                                sb.append("#");
                                sb.append(info.getRoom());
                                sb.append("#");
                                sb.append(info.getTeacher());
                                sb.append("#");
                                sb.append(info.getZoushu());
                                sb.append("@");
                            }
                            String text8 = "";
                            if (sb.length() != 0) {
                                text8 = sb.toString().substring(0, sb.length() - 1);
                            }
                            // 保存文件
                            FileTools.saveFile(context, "course.txt", text8);
                            // 重新解析
                            finish();
                            Intent intent = new Intent(context, CourseActivity.class);
                            startActivity(intent);
                            mPopWindows.dismiss();
                        }
                    }
                });
                builder.setNegativeButton("取消", new OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPopWindows.dismiss();
                    }
                });
                builder.setCancelable(false);
                builder.create().show();

                break;
            }
            default: {
                // 先找到对应的二维数组编号
                int id2 = 0;
                boolean tr = false;
                for (int i = 0; i < 5; i++) {
                    for (int j = 0; j < 7; j++) {
                        if (lessons[i][j] == id) {
                            id2 = (j + 1) * 10 + (i + 1);
                            tr = true;
                            break;
                        }
                        if (tr)
                            break;
                    }
                }
                // 遍历链表，找到对应的节点
                position = -1;
                for (CourseClass course : courses) {
                    position++;
                    if (course.getId() == id2) {
                        Course(course, position);
                        break;
                    }
                }
            }
        }

    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "课表");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

}
