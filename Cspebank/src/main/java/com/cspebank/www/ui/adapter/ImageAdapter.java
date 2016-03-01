package com.cspebank.www.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cspebank.www.R;
import com.cspebank.www.ui.activity.GetPicActivity;
import com.cspebank.www.utils.LogTrace;
import com.cspebank.www.utils.ToastUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ImageAdapter extends CommonAdapter<String> {
	private Photo mPhoto;
	public static Map<String, String> map = new HashMap<String, String>();
	/**
	 * 用户选择的图片，存储为图片的完整路径
	 */
	public static List<String> mSelectedImage;

	/**
	 * 文件夹路径
	 */
	private String mDirPath;
	//已选图片数量
	private int checkedPicNumber;

	public ImageAdapter(Context context, List<String> mDatas, int itemLayoutId,
						String dirPath, Photo Photo, int checkedPicNumber) {
		super(context, mDatas, itemLayoutId);
//		this.mDirPath = dirPath;
		this.mPhoto = Photo;
		this.checkedPicNumber = checkedPicNumber;
		mSelectedImage = new LinkedList<String>();
	}


	public interface Photo {
		public void click();
	}

	@Override
	public void convert(
			final ImageViewHolder helper,final String item) {
		// 设置no_pic
		if (item.equals(GetPicActivity.CAMERA)) {
			// helper.setImageResource(R.id.id_item_image, R.drawable.camera);
			final ImageView mImageView = helper.getView(R.id.phone_album_item_image);
			helper.setImageResource(R.id.phone_album_item_image, R.drawable.phone_album_camera_bg);
			mImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mPhoto.click();
					ToastUtils.showShort("跳到CAMERA");
				}
			});
		} else {
			helper.setImageResource(R.id.phone_album_item_image,
					R.mipmap.phone_album_pictures_no);
			//设置no_selected
//			helper.setImageResource(R.id.id_item_select,
//					R.drawable.forum_picture_unselected);
			// 设置图片
			helper.setImageByUrl(R.id.phone_album_item_image,  item);

			final ImageView mImageView = helper.getView(R.id.phone_album_item_image);
			final ImageView mSelect = helper.getView(R.id.phone_album_item_select);
			mImageView.setColorFilter(null);
			// 设置ImageView的点击事件
			mImageView.setOnClickListener(new OnClickListener() {
				// 选择，则将图片变暗，反之则反之
				@Override
				public void onClick(View v) {

					if (item.equals(GetPicActivity.CAMERA)) {
						mPhoto.click();
						ToastUtils.showShort("跳到CAMERA");
					} else {
						String path =  item;
						LogTrace.i("selectItem:" + item);
						// 已经选择过该图片
						LogTrace.i("size:" + mSelectedImage.size());
						if (mSelectedImage.contains( item)) {
							LogTrace.i("已经选择过该图片");
							map.remove(path);
							mSelectedImage.remove( item);
							mSelect.setVisibility(View.GONE);
							mImageView.setColorFilter(null);
						} else if (mSelectedImage.size() >= (com.cspebank.www.utils.Bimp.TempImg - checkedPicNumber)) {
							LogTrace.i("图满");
							ToastUtils.showShort("最多只能" + (com.cspebank.www.utils.Bimp.TempImg) + "张图");
						} else {
							// 未选择该图片
							LogTrace.i("未选择该图片");
							map.put(path, path);
							mSelectedImage.add(item);
							mSelect.setVisibility(View.VISIBLE);
							mSelect.setImageResource(R.mipmap.phone_album_camera_icon_select);
							mImageView.setColorFilter(Color
									.parseColor("#77000000"));
						}
					}
				}
			});

			/**
			 * 已经选择过的图片，显示出选择过的效果
			 */
			if (mSelectedImage.contains(item)) {
//				mSelect.setVisibility(View.VISIBLE);
				LogTrace.i("选中的item：" + item);
				mSelect.setVisibility(View.VISIBLE);
				mSelect.setImageResource(R.mipmap.phone_album_camera_icon_select);
				mImageView.setColorFilter(Color.parseColor("#77000000"));
			}else {
				mSelect.setVisibility(View.GONE);
			}
		}
	}
}
