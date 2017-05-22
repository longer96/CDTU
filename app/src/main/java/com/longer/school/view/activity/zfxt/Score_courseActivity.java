package com.longer.school.view.activity.zfxt;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.longer.school.Application;
import com.longer.school.modle.bean.CourseClass;
import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.List;

public class Score_courseActivity extends AppCompatActivity implements View.OnClickListener {

    // 课程页面的button引用，6行7列
    private int[][] lessons = {
            {R.id.lesson11, R.id.lesson12, R.id.lesson13, R.id.lesson14, R.id.lesson15, R.id.lesson16, R.id.lesson17},
            {R.id.lesson21, R.id.lesson22, R.id.lesson23, R.id.lesson24, R.id.lesson25, R.id.lesson26, R.id.lesson27},
            {R.id.lesson31, R.id.lesson32, R.id.lesson33, R.id.lesson34, R.id.lesson35, R.id.lesson36, R.id.lesson37},
            {R.id.lesson41, R.id.lesson42, R.id.lesson43, R.id.lesson44, R.id.lesson45, R.id.lesson46, R.id.lesson47},
            {R.id.lesson51, R.id.lesson52, R.id.lesson53, R.id.lesson54, R.id.lesson55, R.id.lesson56,
                    R.id.lesson57},};
    // 某节课的背景图,用于随机获取
    private int[] bg = {R.drawable.kb1, R.drawable.kb2, R.drawable.kb3, R.drawable.kb4, R.drawable.kb5, R.drawable.kb6,
            R.drawable.kb7, R.drawable.kb8, R.drawable.kb9, R.drawable.kb10, R.drawable.kb11, R.drawable.kb12,
            R.drawable.kb13, R.drawable.kb14, R.drawable.kb15, R.drawable.kb16};

    private Context context;
    private PopupWindow mPopWindows2;// 课程详细
    private List<CourseClass> courses = new ArrayList<CourseClass>();
    private int position = -1;
    private static String str_course;// 课表元数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_score_course);

        str_course = getIntent().getStringExtra("course");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getIntent().getStringExtra("bj"));
        String bg_toolbar = FileTools.getshareString("refreshcolor").replaceFirst("#","#10");
        toolbar.setBackgroundColor(Color.parseColor(bg_toolbar));
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        setbg();
        context = Score_courseActivity.this;
        initview();
    }

    /**
     * 设置背景
     */
    private void setbg() {
        ImageView iv_bg = (ImageView) findViewById(R.id.iv_coures_bg);
        int bg = FileTools.getshareInt("bg_course");
        bg = bg == 404 ? R.drawable.bg_course1 : bg;
        iv_bg.setImageResource(bg);
    }


    // 解析课表
    public void initview() {
        CourseClass course = null;
        String[] str1 = str_course.split("@");
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
            // Log.d("有多少节课：", courses.size() + "");
            course = courses.get(i);// 拿到当前课程
            id = course.getId();// 得到对应的位置
            y = (id / 10) % 10;
            x = id % 10;
            // Log.d("x：", x + "");
            // Log.d("y：", y + "");
            Button lesson = (Button) findViewById(lessons[x - 1][y - 1]);// 获得该节课的button
            // Log.d("背景图：", course.getBg() + "");
            int bgRes = bg[course.getBg()];// 随机获取背景色
            lesson.setBackgroundResource(bgRes);// 设置背景
            lesson.setText(course.getName() + "@" + course.getRoom());// 设置文本为课程名+“@”+教室
            lesson.setOnClickListener(this);
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
        mPopWindows2 = new PopupWindow(contentView);
        mPopWindows2.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows2.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopWindows2.setFocusable(true);
        mPopWindows2.setBackgroundDrawable(new BitmapDrawable());

        TextView tv_name = (TextView) contentView.findViewById(R.id.popup_name);
        TextView tv_locationg = (TextView) contentView.findViewById(R.id.popup_location);
        TextView tv_teacher = (TextView) contentView.findViewById(R.id.popup_teacher);
        TextView tv_day = (TextView) contentView.findViewById(R.id.popup_day);
        Button but_delete = (Button) contentView.findViewById(R.id.course_delete);

        tv_name.setText(course.getName());
        tv_locationg.setText(course.getRoom());
        tv_teacher.setText(course.getTeacher());
        tv_day.setText(course.getZoushu());
        but_delete.setVisibility(View.GONE);

        mPopWindows2.showAtLocation(contentView, Gravity.CENTER, 0, 0);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
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
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "正方系统_课表查询_课表详细");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}


