package com.longer.school.view.activity.library;

import com.longer.school.R;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 借还规则
 * @author longer
 *
 */
public class Fragment_Library_jhgz extends Fragment {
	private View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_library_jhgz, null);	
		return view;
	}
	@Override
	public void onStart() {
		LibraryActivity.sethead("借还规则");		
		super.onStart();
	}

}
