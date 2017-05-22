package com.longer.school.view.activity;

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

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class Course_editActivity extends AppCompatActivity {

	private ProgressDialog pg;
	private Context context;
	private final int success = 1;// 成功
	private final int fish = 2;// 失败
	private static String str_result;
	private ListView lv;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case success:
				pg.dismiss();
				intiview();
				break;
			case fish:
				pg.dismiss();
				Toast.makeText(context, "获取信息失败鸟~", 0).show();
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme_NoActionBar);
		setContentView(R.layout.activity_course_edit);

		lv = (ListView) findViewById(R.id.course_lv_edit);

		// 进入之后自动刷新调课信息
		context = Course_editActivity.this;
		initdate();
	}

	public void initdate() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("调、停课信息");
		setSupportActionBar(toolbar);
		if (toolbar != null) {
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
		}


		pg = new ProgressDialog(context);
		pg.setMessage("正在获取调课信息...");
		pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
		pg.show();
		new Thread() {
			public void run() {
				Message mes = null;
				if (LoginService.loginzfxt(context)) {
					str_result = LoginService.logincourse_edit(context);
					if (str_result == null) {
						mes = new Message();
						mes.what = fish;
					} else {
						mes = new Message();
						mes.what = success;
					}
				} else {
					mes = new Message();
					mes.what = fish;
				}
				handler.sendMessage(mes);
			}
		}.start();
	}

	public void intiview() {
		// 解析调课信息
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		try {
			Document doc1 = Jsoup.parse(str_result);
			Elements ele1 = doc1.select("table[class=datelist noprint]table[id=DBGrid] tbody tr");
			// 去除第一行
			ele1.remove(0);
			int size_edit = ele1.size();
			Log.d("调课信息多少条：", size_edit + "#");

			String[] course_no = new String[size_edit];
			String[] course_name = new String[size_edit];
			String[] course_time = new String[size_edit];
			String[] course_old = new String[size_edit];
			String[] course_new = new String[size_edit];

			for (int i = size_edit - 1; i >= 0; i--) {
				Elements ele2 = ele1.get(i).select("td");
				course_no[i] = ele2.get(0).text();
				course_name[i] = ele2.get(1).text();
				course_time[i] = "申请时间：" + ele2.get(4).text();
				course_old[i] = "原：" + ele2.get(2).text();
				course_new[i] = "现：" + ele2.get(3).text();
			}
			Map<String, Object>[] map = new Map[size_edit];
			for (int i = 0; i < size_edit; i++) {
				map[i] = new HashMap<String, Object>();
				map[i].put("no", course_no[i]);
				map[i].put("name", course_name[i]);
				map[i].put("time", course_time[i]);
				map[i].put("old", course_old[i]);
				map[i].put("new", course_new[i]);
				data.add(map[i]);
			}

		} catch (Exception e) {
			Log.d("调课信息多少条：", "111#");
			e.printStackTrace();
			return;
		}
		// 解析课表完毕之后，填充进入listview

		String[] from = { "no", "name", "time", "old", "new" };
		int[] to = { R.id.layout_course_no, R.id.layout_course_name, R.id.layout_course_time, R.id.layout_course_old,
				R.id.layout_course_new };
		lv.setAdapter(new SimpleAdapter(context, data, R.layout.layout_course_edit, from, to));
		Log.d("调课信息多少条：", "22222#");
	};

	protected void onResume() {
		super.onResume();
		MiStatInterface.recordPageStart(this, "调、停课信息");
	}

	protected void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}
}
