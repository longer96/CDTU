package com.longer.school.view.activity.zfxt;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.modle.bean.Score2Class;
import com.longer.school.R;
import com.longer.school.R.id;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.StreamTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.List;

public class Score_xftjActivity extends AppCompatActivity {

	private Context context;
	private final int success = 1;// 成功
	private final int fish = 2;// 失败
	private ProgressDialog pg;
	private static List<Score2Class> score2s = null;
	private static Score2Class score2 = null;
	private LinearLayout ly_name;
	private LinearLayout ly_yq;
	private LinearLayout ly_yh;
	private LinearLayout ly_wtg;
	private LinearLayout ly_hx;

	

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
		setContentView(R.layout.activity_score_xftj);
		
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

		ly_name = (LinearLayout) findViewById(id.xftj_name);
		ly_yq = (LinearLayout) findViewById(id.xftj_yq);
		ly_wtg = (LinearLayout) findViewById(id.xftj_wtg);
		ly_hx = (LinearLayout) findViewById(id.xftj_hx);
		ly_yh = (LinearLayout) findViewById(id.xftj_yh);
		

		context = Score_xftjActivity.this;
		init();
	}

	public void init() {


		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("学分统计");
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
				String str = LoginService.get_score_xftj(context);
				if (str != null) {
					score2s = StreamTools.getscore_xftj(context, str);
					if (score2s != null) {
						mes = new Message();
						mes.what = success;
					} else {
						mes = new Message();
						mes.what = fish;
					}
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
		String name;
		String xf_hx;
		String xf_wtg;
		String xf_yq;
		String xf_yh;
		
		for (int i = 0; i < score2s.size(); i++) {			
			score2 = score2s.get(i);// 拿到当前成绩
			
			name = score2.getName();
			xf_hx = score2.getXf_hx();
			xf_wtg = score2.getXf_wtg();
			xf_yq = score2.getXf_yq();
			xf_yh = score2.getXf_yh();	
			
			TextView tv1 = new TextView(this);
			TextView tv2 = new TextView(this);
			TextView tv3 = new TextView(this);
			TextView tv4 = new TextView(this);
			TextView tv5 = new TextView(this);
			
			tv1.setText(name);	
			ly_name.addView (tv1);
			
			tv2.setText(xf_hx);
			tv2.setTextColor(Color.parseColor("#2dc123"));
			ly_hx.addView (tv2);
			
			tv3.setText(xf_wtg);
			tv3.setTextColor(Color.parseColor("#2dc123"));
			ly_wtg.addView (tv3);
			
			tv4.setText(xf_yq);
			tv4.setTextColor(Color.parseColor("#2dc123"));
			ly_yq.addView (tv4);
			
			tv5.setText(xf_yh);
			tv5.setTextColor(Color.parseColor("#2dc123"));
			ly_yh.addView (tv5);
			
		}
	}

	protected void onResume() {
		super.onResume();
		MiStatInterface.recordPageStart(this, "正方系统_学分统计");
	}

	protected void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}
}
