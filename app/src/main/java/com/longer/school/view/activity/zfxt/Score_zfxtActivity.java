package com.longer.school.view.activity.zfxt;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.modle.bean.ScoreClass;
import com.longer.school.R;
import com.longer.school.utils.LoginService;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.List;

/**
 * 正方系统主窗口
 * @author longer
 *
 */
public class Score_zfxtActivity extends AppCompatActivity {

	private Context context;
	private ProgressDialog pg;
	private List<ScoreClass> scores;
	private final int success = 1;// 成功
	private final int fish = 2;// 失败
	private static int tr = 0;// 判断是否登录成功 0:还在登录 1：登录成功 2：登录失败
	private static boolean pg_show;// 判断提示框是否show出
	private static boolean click;// 判断提示点击过下一个界面
	private static Intent intent = null;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (pg_show) {
				pg.dismiss();
				pg_show = false;
			}
			switch (msg.what) {
			case success:
				setdata();
				break;
			case fish:
				setdata();
				Toast.makeText(context, "啊呀，登录正方系统失败鸟~", 0).show();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(Application.theme);
		setContentView(R.layout.activity_score_zfxt);
		context = Score_zfxtActivity.this;
		tr = 0;
		pg_show = false;
		click = false;
		init();
	}

	// 直接登录正方系统，但是不显示对话框（新线程）
	public void init() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("正方教务系统");
		setSupportActionBar(toolbar);
		if (toolbar != null) {
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					finish();
				}
			});
		}


		new Thread() {
			public void run() {
				Message mes = new Message();
				if (LoginService.loginzfxt(context)) {
					tr = 1;
					mes.what = success;
				} else {
					tr = 2;
					mes.what = fish;
				}
				handler.sendMessage(mes);
			};
		}.start();
	}

	public void setdata() {
		if (tr == 0) {
			// 先一键登录正方系统
			pg = new ProgressDialog(context);
			pg.setMessage("正在一键登录正方系统...");
			pg.setCanceledOnTouchOutside(false);// 点击对话框以外是否消失
			pg.show();
			pg_show = true;
		} else if (tr == 1 && click) {
			startActivity(intent);
		} else if (tr == 2) {
//			AlertDialog.Builder builder = new Builder(context);
//			builder.setMessage("登录正方系统失败鸟~ 是否打开浏览器？");
//			builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					Intent intent = new Intent();
//					String link = "http://211.83.32.12/";
//					intent.setAction(intent.ACTION_VIEW);
//					intent.setData(Uri.parse(link));
//					startActivity(intent);
//					finish();
//				}
//			});
//			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					finish();
//				}
//			});
//			builder.setCancelable(false);
//			builder.create().show();
			
			Toast.makeText(context, "登录正方系统失败鸟~", 0).show();
		}

	}


	// 在校成绩查询
	public void onclick_score1(View view) {
		click = true;
		intent = new Intent(context, Score_zxcjActivity.class);
		setdata();
	}


	// 等级考试查询
	public void onclick_score2(View view) {
		click = true;
		intent = new Intent(context, Score_djksActivity.class);
		setdata();
	}

	// 学分统计
	public void onclick_score3(View view) {
		click = true;
		intent = new Intent(context, Score_xftjActivity.class);
		setdata();
	}
	// 课表查询
	public void onclick_score4(View view) {
		click = true;
		intent = new Intent(context, Score_kbcxActivity.class);
		setdata();
	}
	// 考室查询
	public void onclick_score5(View view) {
		click = true;
		intent = new Intent(context, Score_kscxActivity.class);
		setdata();
	}
	protected void onResume() {
		super.onResume();
		MiStatInterface.recordPageStart(this, "正方系统");
	}

	protected void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}
}
