package com.cspebank.www.ui.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cspebank.www.R;
import com.cspebank.www.receiver.LogOutReceiver;
import com.cspebank.www.ui.activity.dialog.ProgressDialog;
import com.cspebank.www.utils.Bimp;
import com.cspebank.www.utils.ImageFloder;
import com.cspebank.www.utils.LogTrace;
import com.cspebank.www.ui.adapter.ImageAdapter;
import com.cspebank.www.utils.OnImageDirSelected;
import com.cspebank.www.utils.ToastUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;


public class GetPicActivity extends BaseSwipeBackActivity implements OnImageDirSelected, ImageAdapter.Photo {
	private String Camerapath = "";
	public static String CAMERA = "camera";
	private static final int TAKE_PICTURE = 0;
	private String DCIMpath = "/storage/emulated/0/DCIM";
	private String DCIMImgs;
	private String path = "";
	 /**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<String> mImgs;

	private GridView mGirdView;
	private ImageAdapter mAdapter;
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();
	private List<String> mImgList = new ArrayList<String>();
	private RelativeLayout mBottomLy;
	private RelativeLayout titleLayout;
	private TextView mChooseDir;
	private TextView mImageCount;
	int totalCount = 0;

	private int mScreenHeight;

	//private ListImageDirPopupWindow mListImageDirPopupWindow;

	private Button mFinish;
	private LinearLayout llBack;
	private TextView mPhotoAlbum;
	//已选图片数量
	private int checkedPicNumber;
	public static final String CHECKED_PIC_NUMBER = "checkedPicNumber";
	
	
	public static final String FROM_WHICH_ACTIVITY = "from_which_activity";
	
	/**
	 * 相册穿回去的
	 */
	final private int PHOTO_ALBUM=1;
	
	private String fromWhere;
	//进度条
	private Dialog dialog;
	private LogOutReceiver logOutReceiver;  //退出登陆接收器
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.phone_album_gridview_activity);
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		checkedPicNumber = getIntent().getIntExtra(CHECKED_PIC_NUMBER, 0);
		initView();
		addListener();
		registerLogOutReceiver();
		getImages();


	}

	/**
	 * 初始化View
	 */
	private void initView() {
		titleLayout = (RelativeLayout) findViewById(R.id.phone_album_rl_title);
		mGirdView = (GridView) findViewById(R.id.phone_album_gridView);
		mChooseDir = (TextView) findViewById(R.id.phone_album_choose_dir);
		mImageCount = (TextView) findViewById(R.id.phone_album_total_count);
		mFinish = (Button) findViewById(R.id.phone_album_btn_finish);
		llBack = (LinearLayout) findViewById(R.id.ll_back);
		mPhotoAlbum = (TextView) findViewById(R.id.phone_album_tv_title);
		mBottomLy = (RelativeLayout) findViewById(R.id.phone_album_bottom_rl);
		try {
			Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/MFYiHei_Noncommercial_Regular.otf");
			mPhotoAlbum.setTypeface(typeface);
			mPhotoAlbum.setTextScaleX(1.2f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		dialog = ProgressDialog.createLoadingDialog(GetPicActivity.this, "正在加载");
	}
	
	private void addListener() {
		llBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				finish();
			}
		});
		mFinish.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				LogTrace.e("map.size--->" + ImageAdapter.map.size());
				ArrayList<String> list = new ArrayList<String>();
				Collection<String> c = ImageAdapter.map.values();
				Iterator<String> it = c.iterator();
				while (it.hasNext()) {
					list.add(it.next());
				}

				for (int i = 0; i < list.size(); i++) {
					if (Bimp.mbimppath.size() < Bimp.TempImg) {
						Bimp.mbimppath.add(list.get(i));
					}
				}
				ImageAdapter.map.clear();
				finish();
			}
		});
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			//initListDirPopupWindw();
		}
	};
	
	/**
	 * 为View绑定数据
	 */
	private void data2View() {
		if (mImgList == null) {
			ToastUtils.showShort("一张图片没扫描到.");
			return;
		}
		if (!Bimp.ISCHANGEICON) {
			mImgList.add(0,CAMERA);
		}
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new ImageAdapter(getApplicationContext(), mImgList,
				R.layout.phone_album_gridview_item, mImgDir.getAbsolutePath(), this, checkedPicNumber);
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText(totalCount + "张");
	};

	/**
	 * 打开系统相机
	 */
	public void photo() {
		try {
			Intent openCameraIntent = new Intent(
					MediaStore.ACTION_IMAGE_CAPTURE);// 跳转到CAMERA

			String sdcardState = Environment.getExternalStorageState();// 是否有SD卡
			String sdcardPathDir = Environment.getExternalStorageDirectory()
					.getPath() + "/DCIM/";// 图片保存路径
			File file = null;
			if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
				// 有sd卡，是否有myImage文件夹
				File fileDir = new File(sdcardPathDir);
				if (!fileDir.exists()) {
					fileDir.mkdirs();
				}
				// 是否有headImg文件
				file = new File(sdcardPathDir + System.currentTimeMillis()
						+ ".jpg");
			}
			if (file != null) {
				Camerapath = file.getPath();
				Uri imageUri = Uri.fromFile(file);
				openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

				startActivityForResult(openCameraIntent, TAKE_PICTURE);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case TAKE_PICTURE:
			if (Bimp.mbimppath.size() < 9 && resultCode == -1) {
				Bimp.mbimppath.add(Camerapath);
				finish();
			}
			break;
		}
	}

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	/*private void initListDirPopupWindw() {
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.forum_list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});*/
		// 设置选择文件夹的回调
		//mListImageDirPopupWindow.setOnImageDirSelected(this);
	//}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			ToastUtils.showShort("暂无外部存储.");
			return;
		}
		if (dialog == null) {
			dialog = ProgressDialog.createLoadingDialog(GetPicActivity.this, "正在加载");
		}
		dialog.show();
		new Thread(new Runnable() {
			@Override
			public void run() {

				String firstImage = null;
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = GetPicActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				Log.i("TAG", mCursor.getCount() + "");
				while (mCursor.moveToNext()) {
					// 获取图片的路径
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.i("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					mImgList.add(path);
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath)) {
						continue;
					} else {
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}
					//判断文件是否符合图片格式，null表示没有图片文件
					String[] fileArray = parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")){
								return true;
							}
							return false;
						}
					});
					
					if (fileArray == null) {
						continue;
					}
					LogTrace.i("fileName:" + fileArray);
					int picSize = fileArray.length;
					LogTrace.i("picSize:" + picSize);
					// totalCount += picSize;
//					mImgList.add(path);
					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					// if (picSize > mPicsSize) {
					// mPicsSize = picSize;
					mImgDir = parentFile;
					// }
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);
			}
		}).start();
	}

	//private void initEvent() {
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		/*mBottomLy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListImageDirPopupWindow
						.setAnimationStyle(R.style.forum_anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});*/
	//}

	@Override
	public void selected(ImageFloder floder) {

		mImgDir = new File(floder.getDir());
		mImgs = Arrays.asList(mImgDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		ArrayList<String> arrayList = new ArrayList<String>(mImgs);
		arrayList.add(0, CAMERA);
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new ImageAdapter(getApplicationContext(), arrayList,
				R.layout.phone_album_gridview_item, mImgDir.getAbsolutePath(), this, checkedPicNumber);
		mGirdView.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		mImageCount.setText(floder.getCount() + "张");
		mChooseDir.setText(floder.getName());
		//mListImageDirPopupWindow.dismiss();

	}

	@Override
	public void click() {
		if (!Bimp.ISCHANGEICON) {
			photo();
		}
	}
	
	@Override
	public void finish() {
		ImageAdapter.mSelectedImage.clear();// 删除被选中的图片
		super.finish();
	}
	
	
	/**
	 * 发送广播注销页面
	 */
	private void registerLogOutReceiver() {
		logOutReceiver = new LogOutReceiver(GetPicActivity.this);
		IntentFilter filter = new IntentFilter();
		filter.addAction("studentLogOut");
		LogTrace.i("注册接收退出登陆广播");
		registerReceiver(logOutReceiver, filter);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(logOutReceiver);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

}
