package com.jogger.pic.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.jog.pics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @create date 2014-12-22
 * @author xiaotong zhao
 * @class description picList中GridView的Adapter
 */

public class GridPicAdapter extends BaseAdapter {

	/** Classfied data */
	List<Map<String, String>> list = new ArrayList<Map<String, String>>();

	/** Context */
	Context context = null;

	public GridPicAdapter(Context context, List list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View gridPic = convertView;
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			gridPic = LayoutInflater.from(context).inflate(R.layout.grid_item,
					null);
			holder.pic = (ImageView) gridPic.findViewById(R.id.gridPic);
			holder.picShadow = (ImageView) gridPic
					.findViewById(R.id.gridPicShadow);
			gridPic.setTag(holder);
		} else {
			holder = (ViewHolder) gridPic.getTag();
		}

		// holder.pic.setClickable(true);
		holder.pic.setId(4);
		holder.picShadow.setId(99);
		gridPic.setId(66);
		Map map2 = list.get(position);
		/** 图片路径 */
		String path = (String) map2.get("PATH");
		ImageLoader.getInstance().displayImage("file://" + path, holder.pic);

		return gridPic;
	}

	/**
	 * ViewHolder
	 * */
	private class ViewHolder {
		/** GridView Pic */
		private ImageView pic = null;

		/** GridView Pic */
		private ImageView picShadow = null;
	}
}