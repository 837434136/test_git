package com.cspebank.www.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cspebank.www.R;
import com.cspebank.www.ui.activity.LoginActivity;
import com.cspebank.www.utils.Constant;
import com.cspebank.www.utils.PreferencesUtils;

public class GuideThreeFragment extends Fragment {

	private View rootView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if (rootView == null){
			rootView = inflater.inflate(R.layout.fragment_three, container, false);
		}

		ViewGroup parent = (ViewGroup) rootView.getParent();
		if (parent != null){
			parent.removeView(rootView);
		}
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		TextView experience = (TextView) getView().findViewById(R.id.tvInNew);

		experience.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				PreferencesUtils.putBoolean(getActivity(), Constant.IS_FIRST, false);
				startActivity(new Intent(getActivity(), LoginActivity.class));
				getActivity().finish();
			}
		});
	}
}
