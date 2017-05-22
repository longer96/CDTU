package com.longer.school.view.activity.library;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.longer.school.utils.LoginService;
import com.longer.school.utils.TimeTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Fragment_Library_mybook extends Fragment {

	private View view;
	private ProgressDialog pg;
	private Context context;
	private final int getbooksuccess = 1;// 得到书籍信息成功
	private final int getbookfish = 2;// 得到失败书籍信息
	private final int xj_success = 3;// 续借成功
	private final int xj_fish = 4;// 续借失败
	private List<String[]> list;
	private ListView lv;
	private TextView tv_username;
	private TextView tv_money1;
	private TextView tv_money2;
	private Button but_xj;
	private String[] book_link = null;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case getbooksuccess:
				pg.dismiss();
				setdate();
				break;
			case getbookfish:
				pg.dismiss();
				Toast.makeText(context, "登录失败", Toast.LENGTH_LONG).show();
				break;
			case xj_success:
				pg.dismiss();
				initdate();
				Toast.makeText(context, "续借成功", 0).show();
				break;
			case xj_fish:
				pg.dismiss();
				Toast.makeText(context, "续借失败", 0).show();
				break;
			}
			

		};
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_library_mybook, null);
		inti();

		initdate();// 进入之后自动登录图书馆
		return view;
	}

	public void inti() {
		context = getActivity();
		lv = (ListView) view.findViewById(R.id.activity_library_lv_mybook);
		tv_username = (TextView) view.findViewById(R.id.activity_library_tv_name);
		tv_money1 = (TextView) view.findViewById(R.id.activity_library_tv_money1);
		tv_money2 = (TextView) view.findViewById(R.id.activity_library_tv_money2);
		but_xj = (Button) view.findViewById(R.id.library_my_but_xj);
		LibraryActivity.sethead("我的书籍");

		but_xj.setOnClickListener(new onclick_xj());
	}
	public void onResume() {
		super.onResume();
		// 在fragment中的调用用于记录页面访问信息
		MiStatInterface.recordPageStart(getActivity(), "我的书籍");
	}

	public void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}

	class onclick_xj implements OnClickListener {
		public void onClick(View v) {
			// 如果得到的连接字符串数组不为空就遍历数组
			if (book_link == null) {
				Toast.makeText(context, "少侠，不能续借哦", 0).show();
				return;
			}
			pg = new ProgressDialog(context);
			pg.setMessage("正在一键续借图书.....");
			pg.setCanceledOnTouchOutside(false);
			pg.show();
			
			//遍历
			new Thread(){
				public void run() {
					Message mes = new Message();
					for(String str:book_link){
						int j = 0;
						while(j<=1){
							if(!LoginService.library_xj(str)){
								mes.what = xj_fish;
								handler.sendMessage(mes);
								return;
							}
							j++;
						}
					}
					mes.what = xj_success;
					handler.sendMessage(mes);
				};
			}.start();
		}
	}

	public void initdate() {
		pg = new ProgressDialog(context);
		pg.setMessage("正在一键获取图书.....");
		pg.setCanceledOnTouchOutside(false);
		pg.show();
		new Thread() {
			public void run() {
				Message mes = new Message();
				list = LoginService.getmybook(context);
				if (list != null) {
					mes.what = getbooksuccess;
				} else {
					mes.what = getbookfish;
				}
				handler.sendMessage(mes);
			};
		}.start();

	}

	public void setdate() {
		int lo = list.size();
		tv_username.setText(FileTools.getshare(context, "username"));
		tv_money1.setText(FileTools.getshare(context, "library_qfk"));
		tv_money2.setText(FileTools.getshare(context, "library_qpk"));
		// Log.e("欠罚款", FileTools.getshare(context, "library_qfk")+"#");
		// Log.e("欠罚款2", FileTools.getshare(context, "library_qpk")+"#");
		if (lo == 0) {
			String[] name = { "少侠！你的书架空空啊" };
			lv.setAdapter(new ArrayAdapter<String>(context, R.layout.layout_library, R.id.library_book, name));
			return;
		} else {
			String[] book_name = new String[lo];
			String[] book_where = new String[lo];
			String[] book_borrow = new String[lo];
			String[] book_return = new String[lo];
			String[] book_num = new String[lo];// 续借次数
			String[] book_time = new String[lo];// 剩余还书时间
			book_link = new String[lo];// 剩余还书时间

			for (int i = 0; i < lo; i++) {
				book_name[i] = list.get(i)[0];
				book_where[i] = list.get(i)[3];
				book_borrow[i] = list.get(i)[1];
				book_return[i] = list.get(i)[2];
				book_num[i] = list.get(i)[4];
				book_link[i] = list.get(i)[5];
				book_time[i] = TimeTools.getday(list.get(i)[2]);
			}

			List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
			Map<String, Object>[] map = new Map[lo];
			for (int i = 0; i < lo; i++) {
				map[i] = new HashMap<String, Object>();
				map[i].put("name", "《" + book_name[i] + "》");
				map[i].put("book_where", book_where[i]);
				map[i].put("borrow_time", "借书时间： " + book_borrow[i]);
				map[i].put("return_time", "应还时间： " + book_return[i]);
				map[i].put("book_time", "    " + book_time[i] + "天");
				map[i].put("book_num", "续借次数： " + book_num[i]);
				data.add(map[i]);
			}

			String[] from = { "name", "book_where", "borrow_time", "return_time", "book_time", "book_num" };
			int[] to = { R.id.library_book, R.id.library_author, R.id.library_borrow, R.id.library_return,
					R.id.library_time, R.id.library_number };
			lv.setAdapter(new SimpleAdapter(context, data, R.layout.layout_library, from, to));
		}
	}

}
