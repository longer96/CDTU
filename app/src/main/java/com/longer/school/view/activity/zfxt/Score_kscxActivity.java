package com.longer.school.view.activity.zfxt;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.R.id;
import com.longer.school.R.layout;
import com.longer.school.utils.LoginService;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class Score_kscxActivity extends AppCompatActivity {

	private Context context;
	private final int success = 1;// 成功
	private final int fish = 2;// 失败
	private ProgressDialog pg;
	private ListView lv;
	private String str;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case success:
				pg.dismiss();
				initdata();
				break;
			case fish:
				pg.dismiss();
				Toast.makeText(context, "啊呀，获取数据出问题鸟~", 0).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(Application.theme);
		setContentView(R.layout.activity_score_kscx);

		lv = (ListView) findViewById(id.score_kscx_lv);
		context = Score_kscxActivity.this;
		init();
	}

	public void init() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("考试查询");
		setSupportActionBar(toolbar);
		if (toolbar != null) {
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
		}

		pg = new ProgressDialog(context);
		pg.setMessage("正在获取数据.....");
		pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
		pg.show();
		new Thread() {
			public void run() {
				Message mes = new Message();
				str = LoginService.get_score_kscx(context);
				if (str != null) {
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

	// 适配数据
	private void initdata() {
		// 创建解析器
		Document doc = Jsoup.parse(str);
		Elements ele = doc.select("table[id=DataGrid1]table[class=datelist] tbody tr");
		ele.remove(0);

		int lo = ele.size();
		Log.e("多少个考试安排：", lo + "#");
		String[] name = new String[lo];

		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < ele.size(); i++) {
			Elements ele3 = ele.get(i).select("td");

			sb.append("课名：");
			sb.append(ele3.get(1).text());
			sb.append("\n");

			sb.append("座位号：");
			sb.append(ele3.get(6).text());
			sb.append("\n");

			sb.append("考试地点：");
			sb.append(ele3.get(4).text());
			sb.append("\n");

			sb.append("时间：");
			sb.append(ele3.get(3).text());
			sb.append("\n");

			sb.append("考试形式：");
			sb.append(ele3.get(5).text());
			sb.append("\n");

			Log.e("11", "1");
			name[i] = sb.toString();
			sb.setLength(0);
		}
		Log.e("11", "222");
		lv.setAdapter(new ArrayAdapter<String>(this, layout.layout_score_kscx, id.score_kscx_tv, name));
		
		Log.e("11", "333");
	}

	protected void onResume() {
		super.onResume();
		MiStatInterface.recordPageStart(this, "正方系统_考室查询");
	}

	protected void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}
}
