package com.cspebank.www.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.cspebank.www.R;
import com.cspebank.www.app.CspeManager;
import com.cspebank.www.ui.fragment.GuideOneFragment;
import com.cspebank.www.ui.fragment.GuideThreeFragment;
import com.cspebank.www.ui.fragment.GuideTwoFragment;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends FragmentActivity {

	private ViewPager viewPage;
	private GuideOneFragment mFragment1;
	private GuideTwoFragment mFragment2;
	private GuideThreeFragment mFragment3;
	private ViewPagerAdapter mPgAdapter;
	private RadioGroup dotLayout;
	private List<Fragment> mListFragment = new ArrayList<Fragment>();
	private GuideActivity mInstance;
	private CspeManager mManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		mInstance = GuideActivity.this;
		mManager = new CspeManager();
		mManager.addActivity(mInstance);
		initView();
		viewPage.setOnPageChangeListener(new MyPagerChangeListener());

	}

	private void initView() {
		dotLayout = (RadioGroup) findViewById(R.id.advertise_point_group);
		viewPage = (ViewPager) findViewById(R.id.viewpager);
		mFragment1 = new GuideOneFragment();
		mFragment2 = new GuideTwoFragment();
		mFragment3 = new GuideThreeFragment();
		mListFragment.add(mFragment1);
		mListFragment.add(mFragment2);
		mListFragment.add(mFragment3);
		mPgAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
				mListFragment);
		viewPage.setAdapter(mPgAdapter);

	}

	public class MyPagerChangeListener implements OnPageChangeListener {

		public void onPageSelected(int position) {
			((RadioButton) dotLayout.getChildAt(position)).setChecked(true);
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int position, float positionOffset,
				int positionOffsetPixels) {
		}

	}
	
	
	class ViewPagerAdapter extends FragmentPagerAdapter {

		private List<Fragment> fragmentList = new ArrayList<Fragment>();

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public ViewPagerAdapter(FragmentManager fragmentManager,
				List<Fragment> arrayList) {
			super(fragmentManager);
			this.fragmentList = arrayList;
		}

		@Override
		public Fragment getItem(int arg0) {
			return fragmentList.get(arg0);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mManager.removeActivity(mInstance);
	}
}
