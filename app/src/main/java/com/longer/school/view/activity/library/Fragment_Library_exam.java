package com.longer.school.view.activity.library;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.longer.school.R;

public class Fragment_Library_exam extends Fragment {
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_design, null);	
		init();
		return view;
	}
	@Override
	public void onStart() {
		LibraryActivity.sethead("实例");		
		super.onStart();
	}
	public void init(){
		
	}
}
