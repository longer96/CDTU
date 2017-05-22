package com.longer.school.view.activity.library;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longer.school.R;

/**
 * 图书赔偿规则
 * @author longer
 *
 */
public class Fragment_Library_tspc extends Fragment {
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_library_tspc, null);	
		return view;
	}
	@Override
	public void onStart() {
		LibraryActivity.sethead("图书赔偿");		
		super.onStart();
	}

}
