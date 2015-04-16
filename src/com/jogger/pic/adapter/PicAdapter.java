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
import android.widget.TextView;

import com.jog.pics.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @create date 2014-12-19
 * @author xiaotong zhao
 * @class description 图片列表布局
 */

public class PicAdapter extends BaseAdapter{

	/** Context*/
	private Context context = null;

	/** List*/
	private List<Map<String, String>> picList = new ArrayList<Map<String, String>>();
	
	public PicAdapter(Context context, List<Map<String, String>> picList){
		this.context = context;
		this.picList = picList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return picList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return picList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		View v = convertView;
		if (convertView == null) {
			holder = new ViewHolder();
			v = LayoutInflater.from(context)
					.inflate(R.layout.pic_info, null);
			holder.image = (ImageView) v.findViewById(R.id.image);
			holder.picName = (TextView) v.findViewById(R.id.picName);
			holder.picType = (TextView) v.findViewById(R.id.picType);
			holder.picSize = (TextView) v.findViewById(R.id.picSize);
			holder.picAddTime = (TextView) v.findViewById(R.id.picCreateDate);
			holder.count = (TextView) v.findViewById(R.id.serialCode);	
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		
		ImageLoader.getInstance().displayImage("file://" + picList.get(position).get("DATA"),
				holder.image);
		//holder.image.setImageBitmap(BitmapFactory
		//		.decodeFile(picList.get(position).get("DATA")));
		holder.picName.setText(picList.get(position).get("TITLE"));
		holder.picType.setText(picList.get(position).get("TYPE"));
		holder.picSize.setText(picList.get(position).get("SIZE"));
		holder.picAddTime.setText(picList.get(position).get("DATE_ADDED"));
		holder.count.setText(position + 1 + "");
		return v;
	}
	
	private class ViewHolder{
		
		private ImageView image = null;
		
		private TextView picName = null;
		
		private TextView picType = null;
		
		private TextView picSize = null;
		
		private TextView picAddTime = null;
		
		private TextView count = null;
	} 
}