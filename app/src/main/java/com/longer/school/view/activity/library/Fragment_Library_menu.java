package com.longer.school.view.activity.library;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.longer.school.R;
import com.longer.school.utils.FileTools;

public class Fragment_Library_menu extends Fragment {

	private View view;
	private LinearLayout ly1;
	private LinearLayout ly2;
	private TextView tv_name;
	private TextView tv_total;
	private TextView tv_book;//我的再借书籍


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_library_menu, null);
		inti();
		return view;
	}

	public void inti() {
		ly1 = (LinearLayout) view.findViewById(R.id.library_lin_1);
		ly2 = (LinearLayout) view.findViewById(R.id.library_lin_2);
		tv_book = (TextView) view.findViewById(R.id.library_my_tv_book);
		tv_total = (TextView) view.findViewById(R.id.library_my_tv_total);
		tv_name = (TextView) view.findViewById(R.id.library_my_tv_name);
		
	}
	@Override
	public void onStart() {
		LibraryActivity.sethead("图书馆");
		
		String name = FileTools.getshare(getActivity(), "name");
		String total = FileTools.getshare(getActivity(), "book_borrow");
		String book = FileTools.getshare(getActivity(), "library_book");
		
		if(!"".equals(book)){
			tv_book.setText(book);
			tv_name.setText(name);
			tv_name.setVisibility(View.VISIBLE);
			ly1.setVisibility(View.VISIBLE);			
		}
		if(!"".equals(total)){
			tv_total.setText(total);
			tv_name.setText(name);
			tv_name.setVisibility(View.VISIBLE);
			ly2.setVisibility(View.VISIBLE);			
		}
		
		super.onStart();
	}

}
