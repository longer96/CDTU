package com.longer.school.view.activity.library;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.FileTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

public class LibraryActivity extends AppCompatActivity {
	private FragmentManager fm;
	private FragmentTransaction ft;
	public Context context;
	private static TextView tv_head;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTheme(Application.theme);
		setContentView(R.layout.activity_library);

		init();
	}

	public void init() {
		tv_head = (TextView) findViewById(R.id.activity_library_tv_head);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		toolbar.setTitle("");
		setSupportActionBar(toolbar);
		if (toolbar != null) {
			toolbar.setNavigationOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					String head = tv_head.getText().toString();
					if("图书馆".equals(head)) {
						finish();
					}else{
						fm.popBackStack();
					}
				}
			});
		}


		fm = getFragmentManager();
		ft = fm.beginTransaction();
		context = LibraryActivity.this;
		// 初始加载菜单，不放入任务栈中
		ft.replace(R.id.library_fragment, new Fragment_Library_menu());
		ft.commit();
	}

	public void click_back(View view) {
		fm = getFragmentManager();
		int back_num = fm.getBackStackEntryCount();
		Log.d("back tab的数量", back_num + "");
		if (back_num > 0) {
			fm.popBackStack();
		} else {
			this.finish();
		}
	}

	// Fragment 窗口的切换
	public void fragmentchange(Fragment frag) {
		fm = getFragmentManager();
		ft = fm.beginTransaction();
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		ft.replace(R.id.library_fragment, frag);
		ft.addToBackStack(null);
		ft.commit();
	}

	/**
	 * 三点菜单
	 * @param menu
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_library, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_password) {
			select();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/**
	 * 更改密码
	 */
	private void select() {
		final EditText editText = new EditText(context);
		editText.setHint("你自己改过的密码");
		String password = FileTools.getshareString("passwordLibrary");
		if(!"".equals(password)){
			editText.setText(password);
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("确定");
		builder.setView(editText, 80, 0, 80, 0);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String password = editText.getText().toString().trim();
				FileTools.saveshareString("passwordLibrary",password);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		builder.create().show();
	}


	// 我的再借书籍
	public void onclick_mybook(View view) {
		fragmentchange(new Fragment_Library_mybook());
	}

	// 借阅历史
	public void onclick_history(View view) {
		fragmentchange(new Fragment_Library_history());
	}

	// 借还规则
	public void onclick_jhgz(View view) {
		fragmentchange(new Fragment_Library_jhgz());
	}

	// 图书赔偿
	public void onclick_tspc(View view) {
		fragmentchange(new Fragment_Library_tspc());
	}
	// 检索图书
	public void onclick_select(View view) {
		Intent intent = new Intent(context, Library_selectActivity.class);
		startActivity(intent);
	}

	// 没有实现的功能
	public void onclick_other(View view) {
		Toast.makeText(context, "该功能还在建设中哦~", Toast.LENGTH_SHORT).show();
	}

	/*
	 * 接口，用来修改 “图书馆”标题的
	 */
	public static void sethead(String head) {
		tv_head.setText(head);
	}

	protected void onResume() {
		super.onResume();
		MiStatInterface.recordPageStart(this, "图书馆");
	}

	protected void onPause() {
		super.onPause();
		MiStatInterface.recordPageEnd();
	}
}
