package com.cspebank.www.ui.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cspebank.www.R;

public class GuideOneFragment extends Fragment {

	private View rootView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (rootView == null){
			rootView = inflater.inflate(R.layout.fragment_one, container, false);
		}

		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null){
			parent.removeView(rootView);
		}
		return rootView;
	}

}
