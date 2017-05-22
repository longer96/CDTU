package com.longer.school.view.activity.library;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.StreamTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Library_history extends Fragment {
	private View view;
	private ProgressDialog pg;
	private Context context;
	private ListView lv;
	private final int getbooksuccess = 1;// 得到书籍信息成功
	private final int getbookfish = 2;// 得到失败书籍信息
	String str_his = null;// 所有的借书信息
	private String[] name;
	private String[] time;
	private String[] what;
	private int total_borrow;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case getbooksuccess:
				pg.dismiss();
				setdate();
				break;
			case getbookfish:
				pg.dismiss();
				Toast.makeText(context, "登录失败,若单独修改过图书馆密码，可在主页右上角更改.", Toast.LENGTH_LONG).show();
				break;

			}

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_library_history, null);
		init();
		initdate();// 进入之后自动登录图书馆
		return view;
	}

	@Override
	public void onStart() {
		LibraryActivity.sethead("借阅历史");
		super.onStart();
	}
	public void onResume() {
		super.onResume();
		// 在fragment中的调用用于记录页面访问信息
		MiStatInterface.recordPageStart(getActivity(), "借阅历史");
	}

	public void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}

	public void init() {
		context = getActivity();
		lv = (ListView) view.findViewById(R.id.frag_library_lv_history);

	}

	public void initdate() {
		pg = new ProgressDialog(context);
		pg.setMessage("正在一键登录图书馆.....");
		pg.setCanceledOnTouchOutside(false);
		pg.show();
		new Thread() {
			public void run() {
				Message mes = new Message();
				str_his = LoginService.get_card_history(context);
				if (str_his != null) {
					mes.what = getbooksuccess;
				} else {
					mes.what = getbookfish;
				}
				handler.sendMessage(mes);
			};
		}.start();

	}

	/*
	 * 将数据适配到控件
	 */
	public void setdate() {

		Elements ele = StreamTools.get_library_history(str_his);
		int lo = ele.size();
		Elements ele2 = null;

		name = new String[lo];
		time = new String[lo];
		what = new String[lo];
		total_borrow = 0;// 统计一共借阅书籍

		for (int i = 0; i < lo; i++) {
			ele2 = ele.get(i).select("td");
			name[i] = ele2.get(0).text();
			time[i] = ele2.get(3).text();
			what[i] = ele2.get(4).text();
			if ("借书".equals(what[i])) {
				total_borrow++;
			}
		}
		// 储存一共借了多少本书
		FileTools.saveshare(context, "book_borrow", total_borrow + "");
		// Log.e("多少条", total + "#");

		if (lo == 0) {
			String[] name2 = { "少侠！你的书架空空啊" };
			lv.setAdapter(new ArrayAdapter<String>(context, R.layout.layout_library_history, R.id.library_history_title,
					name2));
			return;
		} else {

			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String, Object>[] map = new Map[lo];
			for (int i = 0; i < lo; i++) {
				map[i] = new HashMap<String, Object>();
				map[i].put("name", name[i]);
				map[i].put("time", "处理时间:" + time[i]);
				map[i].put("what", what[i]);
				data.add(map[i]);
			}

			String[] from = { "name", "time", "what" };
			int[] to = { R.id.library_history_title, R.id.library_history_time, R.id.library_history_do };
			lv.setAdapter(new SimpleAdapter(context, data, R.layout.layout_library_history, from, to));

		}
	}

}
