package com.longer.school.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.Toast;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ZhuanzhouActivity extends AppCompatActivity {

	private Context context;
	private ListView lv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(R.style.AppTheme_NoActionBar);
		setContentView(R.layout.activity_zhuanzou);
		inti();
	}

	public void inti() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("专周详细");
		setSupportActionBar(toolbar);
		if (toolbar != null) {
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
		}

		context = ZhuanzhouActivity.this;
		lv = (ListView) findViewById(R.id.zhuanzhou_lv);

		String str = FileTools.getFile(context, "zhuanzhou.txt");
		if (str == null) {
			Toast.show("读取文件失败");
			return;
		} else {
			if(str.length() == 0){
				Toast.show("你们没有专周耶~");
				return;
			}
			// 适配数据
			String[] zhuan = str.split("@");
			int i = zhuan.length;
//			Log.d("数据长度","I：" + i + "  文件夹长度：" + str.length());
			String[] name = new String[i];
			String[] teacher = new String[i];
			String[] day = new String[i];

			for (int j = 0; j < i; j++) {
				String[] zhou = zhuan[j].split("#");
				name[j] = zhou[0];
				teacher[j] = zhou[1] + "     " + zhou[2]+"学分";
				day[j] = zhou[3]+"周";
			}

			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String, Object>[] map = new Map[i];
			
			for (int g = 0; g < i; g++) {
				map[g] = new HashMap<String, Object>();
				map[g].put("name", name[g]);
				map[g].put("teacher", teacher[g]);
				map[g].put("day", day[g]);
				data.add(map[g]);
			}

			String[] from = { "name", "teacher", "day" };
			int[] to = { R.id.zhuanzhou_tv_name, R.id.zhuanzhou_tv_teacher, R.id.zhuanzhou_tv_day };
			lv.setAdapter(new SimpleAdapter(context, data, R.layout.layout_zhuanzhou, from, to));
		}
	}

	protected void onResume() {
		super.onResume();
		MiStatInterface.recordPageStart(this, "课表_专周查询");
	}

	protected void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}
}
