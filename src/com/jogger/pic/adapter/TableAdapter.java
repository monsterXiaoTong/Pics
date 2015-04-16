package com.jogger.pic.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.jog.pics.R;
import com.jog.pics.TableGridView;

/**
 * @create date 2014-12-22
 * @author xiaotong zhao
 * @class description 时间轴列表 Adapter
 */

public class TableAdapter extends BaseAdapter {

	/** classfied pics list */
	private List<Map<String, Object>> tableList 
			= new ArrayList<Map<String, Object>>();
	
	/** Context */
	private Context context = null;
		
	public TableAdapter(Context context, List list) {
		this.context = context;
		this.tableList = list;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return tableList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return tableList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		/** 存放本Item中的图片数据*/
		List<Map<String, String>> picList = null;
		
		View tableView = convertView;
		ViewHolder holder = null;
		if (null == convertView) {
			holder = new ViewHolder();
			tableView = LayoutInflater.from(this.context).inflate(
					R.layout.table_item, null);
			holder.monthDay = (TextView) tableView.findViewById(R.id.day);
			holder.month = (TextView) tableView.findViewById(R.id.month);
			holder.weekDay = (TextView) tableView.findViewById(R.id.weekDay);
			holder.gridPics = (TableGridView) tableView.findViewById(R.id.gridView);
			tableView.setTag(holder);
		} else {
			holder = (ViewHolder) tableView.getTag();
		}

		// initialise it
		// 2014-xx-xx
		String date = tableList.get(position).get("DATE").toString();
		String date01 = date.split("-")[1];
		String date02 = date.split("-")[2];
		holder.weekDay.setText(tableList.get(position).get("WEEKDAY").toString());
		
		SpannableString date03 = new SpannableString(date02);
		date03.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 
				0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  //粗体
		holder.monthDay.setText(date03);
		holder.month.setText(date01);
		picList = (ArrayList<Map<String, String>>) tableList.get(position).get("PICLIST");
		
		GridPicAdapter picAdapter = new GridPicAdapter(this.context, picList);
		holder.gridPics.setAdapter(picAdapter);

		// 弹窗显示图片
		holder.gridPics.setOnItemClickListener(new GridClick(picList, holder.gridPics));		
		return tableView;
	}

	/**
	 * 自定义的 图片列表点击监听器
	 * 解决了 IndexOutOfBound
	 * */
	private class GridClick implements OnItemClickListener {
		
		/** 传入的图片数据列表*/
		private List<Map<String, String>> picList = null;
		
		private GridView grid = null;
		
		public GridClick(List picList, GridView grid){
			this.picList = picList;
			this.grid = grid;
		}
		
		@Override
		public void onItemClick(AdapterView<?> parent
			, View source, int position, long id)
		{	
			View viewDialog = LayoutInflater.from(context).inflate(
					R.layout.view, null);
				// 获取viewDialog中ID为image的组件
				ImageView image = (ImageView) viewDialog
					.findViewById(R.id.image);
				// 设置image显示指定图片
				image.setImageBitmap(BitmapFactory.decodeFile(
					picList.get(position).get("PATH")));
				// 使用对话框显示用户单击的图片
				new AlertDialog.Builder(context)
					.setView(viewDialog)
					.setPositiveButton("OK", null)
					.show();
		}
	}
	
	/**
	 * ViewHolder 
	 * */
	private class ViewHolder {
		/** day of month */
		private TextView monthDay = null;

		/** month */
		private TextView month = null;

		/** weekDay */
		private TextView weekDay = null;

		/** GridView */
		private TableGridView gridPics = null;
	}
}