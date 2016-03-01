package com.cspebank.www.ui.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 基类 Adapter
 * @author yisinian.deng
 * 2015.11.18
 * @param <T>
 */
public abstract class CspeBaseAdapter<T> extends BaseAdapter {

	protected static int TYPE_COUNTER = 1;//item 的类型总数，默认为1
	protected  Context mContext;
	protected List<T> mDataList;
	
	public CspeBaseAdapter(Context context, List<T> list) {
		this.mContext = context;
		setData(list);  
	}

	public CspeBaseAdapter(Context context, List<T> list, int viewTypeCount) {
		this(context, list);
		TYPE_COUNTER = viewTypeCount;
	}

	private void setData(List<T> list) {
		if (list == null) {
			this.mDataList = new ArrayList<T>();
		}else {
			this.mDataList = list;
		}
	}

	public void updateData(List<T> list){
		setData(list);
		this.notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mDataList != null ? mDataList.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if (position >= mDataList.size()) {
			return null;
		}
		return mDataList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNTER;
	}
	
	/**
	 * 该方法需要子类实现，需要根据type返回item布局的resource id
	 * 
	 * @param type
	 * @return
	 */
	public abstract int getItemResourceId(int type);

	/**
     * 使用该getItemView方法替换原来的getView方法中部分功能，需要子类实现
     * 
     * @param position
     * @param convertView
     * @param holder
     * @param type
     * @return
     */
	public abstract View getItemView(int position, View convertView, ViewHolder holder, int type);

	public class ViewHolder {
		
//		SparseArray是android里为<Interger,Object>这样的Hashmap而专门写的类,目的是提高效率，其核心是折半查找函数（binarySearch）。
//	   	 在Android中，当我们需要定义HashMap<Integer, E> hashMap = new HashMap<Integer, E>();
//		时，我们可以使用如下的方式来取得更好的性能.
	    private SparseArray<View> views = new SparseArray<View>();
	    private View convertView;

	    public ViewHolder(View convertView) {
	        this.convertView = convertView;
	    }

		@SuppressWarnings({ "unchecked", "hiding" })
		public <T extends View> T getView(int resId) {
	        View v = views.get(resId);
	        if (null == v) {
	            v = convertView.findViewById(resId);
	            views.put(resId, v);
	        }
	        return (T) v;
	    }
	}
	
}
