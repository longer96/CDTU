package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.longer.school.Application;
import com.longer.school.modle.bean.CourseClass;
import com.longer.school.R;
import com.longer.school.R.id;
import com.longer.school.R.layout;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.List;

public class Course_addActivity extends AppCompatActivity {

    private Spinner spin_day;// 周一", "周二", "周三", "
    private Spinner spin_jie;// "1、2节", "3、4节", "5、6节",
    private Spinner spin_js;// "允明楼", "文澄楼", "其他"
    private Spinner spin_class;// 课程选择
    private Context context;
    private EditText edi_name;
    private EditText edi_class;
    private EditText edi_teacher;
    private static String sp_day;
    private static String sp_jie;
    private static String sp_js;
    private static String str;
    private int sp_day_position;
    private int sp_jie_position;
    private int sp_js_position;
    private List<CourseClass> courses = new ArrayList<CourseClass>();

    String[] Day = new String[]{"周一", "周二", "周三", "周四", "周五", "周六", "周日"};
    String[] Jie = new String[]{"1、2节", "3、4节", "5、6节", "7、8节", "9、10、11节"};
    String[] JS = new String[]{"允明楼", "文澄楼", "其他"};
    static String[] Name = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_course_add);

        context = Course_addActivity.this;
        spin_day = (Spinner) findViewById(id.course_add_spinner1);
        spin_jie = (Spinner) findViewById(id.course_add_spinner2);
        spin_js = (Spinner) findViewById(id.course_add_spinner3);
        spin_class = (Spinner) findViewById(id.course_add_spinner4);
        edi_name = (EditText) findViewById(id.course_add_name);
        edi_class = (EditText) findViewById(id.course_add_class);
        edi_teacher = (EditText) findViewById(id.course_add_teacher);

        inti_data();
        inti();
    }

    private void inti() {

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("添加课程");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Day);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_day.setAdapter(adapter);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Jie);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin_jie.setAdapter(adapter2);

        try {
            ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, JS);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_js.setAdapter(adapter3);


            ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Name);
            adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spin_class.setAdapter(adapter4);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.show("哎呀~ 好像出问题了");
            return;
        }

        spin_day.setOnItemSelectedListener(spin_daylister);
        spin_jie.setOnItemSelectedListener(spin_jielister);
        spin_js.setOnItemSelectedListener(spin_jslister);
        spin_class.setOnItemSelectedListener(spin_classlister);
    }

    public void inti_data() {
        CourseClass course = null;

        str = FileTools.getFile(context, "course.txt");
        if (str == "") {
            Toast.show("课表为空，请刷新课表！");
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

        //去除重复的课
        for (int i = 0; i < courses.size() - 1; i++) {
            for (int j = courses.size() - 1; j > i; j--) {
                if (courses.get(j).getName().equals(courses.get(i).getName())) {
                    courses.remove(j);
                }
            }
        }

        //将数据放入下拉菜单，添加不重复的课程
        Name = new String[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            Name[i] = courses.get(i).getName();
        }
    }

    private Spinner.OnItemSelectedListener spin_daylister = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp_day = parent.getSelectedItem().toString();
            sp_day_position = position + 1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private Spinner.OnItemSelectedListener spin_jielister = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp_jie = parent.getSelectedItem().toString();
            sp_jie_position = position + 1;
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private Spinner.OnItemSelectedListener spin_jslister = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            sp_js = parent.getSelectedItem().toString();
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private Spinner.OnItemSelectedListener spin_classlister = new Spinner.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            edi_name.setText(parent.getSelectedItem().toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    // 修改按键
    public void edit_click(View view) {
        // 先判断不能为空
        if (TextUtils.isEmpty(edi_name.getText()) || TextUtils.isEmpty(edi_class.getText())) {
            Toast.show("课名或教室不能为空！");
            return;
        }
        // 再判断是否存在课（若存在提示先删除）
        int position = sp_day_position * 10 + sp_jie_position;
        for (CourseClass info : courses) {
            if (info.getId() == position) {
                Toast.show("该时间段已经存在课，请删除后再添加！");
                return;
            }
        }
        // 设置背景图
        int i = 0;
        for (CourseClass info : courses) {
            if (info.getName().contains(edi_name.getText())) {
                i = info.getBg();
                break;
            } else {
                i = info.getBg() + 1;
            }
        }
        StringBuffer sb = new StringBuffer();
        sb.append(str);
        sb.append("@");
        sb.append(i);
        sb.append("#");
        sb.append(position);
        sb.append("#");
        sb.append(edi_name.getText());
        sb.append("#");
        sb.append(sp_js);
        sb.append(edi_class.getText());
        sb.append("#");
        sb.append(edi_teacher.getText());
        sb.append("#");
        sb.append(sp_day);
        sb.append(sp_jie);
        //保存重新添加的课表
        FileTools.saveFile(context, "course.txt", sb.toString());
        CourseActivity.instance.finish();
        Intent intent = new Intent(context, CourseActivity.class);
        startActivity(intent);

    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "添加课程");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
