package com.longer.school.view.activity.library;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.longer.school.R;
import com.longer.school.utils.LoginService;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Library_selectActivity extends Activity {

    private Context context;
    private EditText ed_select;
    private ListView lv;
    private final int getbooksuccess = 1;// 得到书籍信息成功
    private final int getbookfish = 2;// 得到失败书籍信息
    private String text;// 返回的书籍信息
    private static String ed_text;
    private String[] name;
    private String[] author;
    private String[] link;

    private String[] from = {"name", "author"};
    private int[] to = {R.id.library_select_title, R.id.library_select_author};

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case getbooksuccess:
                    // pg.dismiss();
                    setdate();
                    break;
                case getbookfish:
                    // pg.dismiss();
                    Toast.makeText(context, "获取书籍失败", 0).show();
                    break;
            }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_library_select);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        init();
    }

    private void init() {
        context = Library_selectActivity.this;
        ed_select = (EditText) findViewById(R.id.library_select_et);
        lv = (ListView) findViewById(R.id.library_lv_select);
        ed_select.addTextChangedListener(textchange);
        lv.setOnItemClickListener(new ClickItem());
    }

    class ClickItem implements OnItemClickListener {
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(context, Library_select_bodyActivity.class);
            intent.putExtra("name", name[position]);
            intent.putExtra("link", link[position]);
            startActivity(intent);
        }

    }

    private TextWatcher textchange = new TextWatcher() {
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // System.out.println("-1-onTextChanged-->" +
            // ed_select.getText().toString() + "<--");
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // System.out.println("-2-onTextChanged-->" +
            // ed_select.getText().toString() + "<--");
        }

        // 监听搜索框文字的变化，用这个方法
        public void afterTextChanged(Editable s) {
            ed_text = ed_select.getText().toString();
            System.out.println("输入之后：" + ed_text + "<--");
            if (!"".equals(ed_text)) {
                select();
            }
        }
    };

    /**
     * 检索书籍
     */
    public void select() {
        new Thread() {
            public void run() {
                Message mes = new Message();
                text = LoginService.get_library_select(context, ed_select.getText().toString());
                if (text != null) {
                    mes.what = getbooksuccess;
                } else {
                    mes.what = getbookfish;
                }
                handler.sendMessage(mes);
            }

            ;
        }.start();
    }

    private void setdate() {
        try {
            Document doc = Jsoup.parse(text);
            Elements ele = doc.select("table[id=GridView1] tbody tr");
            ele.remove(0);
            int lo = ele.size();
            // Log.d("书本：", ele.toString() + "#");
            Log.d("多少本：", lo + "#");

            // 我们为了解析的更快，只解析 1.书名 2.作者 3.连接
            name = new String[lo];
            author = new String[lo];
            link = new String[lo];
            Elements ele2 = null;

            for (int i = 0; i < lo; i++) {
                ele2 = ele.get(i).select("td");
                name[i] = ele2.get(0).text();
                author[i] = "作者：" + ele2.get(1).text() + "\t\t出版时间：" + ele2.get(3).text();
                link[i] = "http://211.83.32.195/gdnetweb/" + ele2.get(0).select("a").attr("href");
                // Log.d("书名：", name[i]);
                // Log.d("作者：", author[i]);
//				 Log.d("地址：", link[i]);
            }

            List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
            Map<String, Object>[] map = new Map[lo];
            for (int i = 0; i < lo; i++) {
                map[i] = new HashMap<String, Object>();
                map[i].put("name", name[i]);
                map[i].put("author", author[i]);
                data.add(map[i]);
            }
            lv.setAdapter(new SimpleAdapter(context, data, R.layout.layout_library_select, from, to));

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, "呀，貌似没有相关书籍~", 0).show();
        }
    }

    // 返回按键
    public void onclick_back(View view) {
        this.finish();
    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "图书查询界面");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}
