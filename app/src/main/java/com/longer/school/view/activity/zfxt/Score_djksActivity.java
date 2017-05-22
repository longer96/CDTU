package com.longer.school.view.activity.zfxt;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.R.id;
import com.longer.school.R.layout;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.StreamTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.List;

public class Score_djksActivity extends AppCompatActivity {

	private ListView lv;
	private Context context;
	private final int success = 1;// 成功
	private final int fish = 2;// 失败
	private ProgressDialog pg;
	private static List<String[]> score = null;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case success:
				pg.dismiss();
				initdata();
				break;
			case fish:
				pg.dismiss();
				Toast.makeText(context, "啊呀，获取成绩出问题鸟~", 0).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(Application.theme);
		setContentView(R.layout.activity_score_djks);
		
		context = Score_djksActivity.this;
		lv = (ListView) findViewById(id.score_djks_lv);
		init();
	}

	public void init() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("等级考试");
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
				String str = LoginService.get_score_djks(context);
				if (str != null) {
					score = StreamTools.getscore_djks(context, str);
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

		String[] name = new String[score.size()];
		for (int i = 0; i < score.size(); i++) {
			name[i] = score.get(i)[0] + "\n" + score.get(i)[1] + "\n" + score.get(i)[2] + "\n" + score.get(i)[3];
		}

		lv.setAdapter(new ArrayAdapter<String>(this, layout.layout_score_djks, id.score_djks_tv, name));

	}

	protected void onResume() {
		super.onResume();
		MiStatInterface.recordPageStart(this, "正方系统_等级考试");
	}

	protected void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}
}
