package com.jog.pics;

import java.sql.Date;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jogger.pic.adapter.PicAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * @create date 2014-12-12
 * @author xiaotong zhao
 * @class description show all pics of the certain fold
 */

public class SingleFoldPics extends Activity
{	
	/** queryKey1: foldName*/
	private String foldName = null;

	/** queryKey2: foldID*/
	private String bucketID = null;
	
	/** Pics ListView*/
	private ListView picLV = null;
	
	/** all pics of fold List*/
	private List<Map<String, String>> picsList = 
			new ArrayList<Map<String, String>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.single_fold_layout);
		
		picLV = (ListView) findViewById(R.id.PicsListOfFold);
		getIntents();
		queryPics();
		
		initPicList();
		
		// ImageLoader 异步加载图片
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
	        .cacheInMemory(true)
	        .cacheOnDisk(true)
	        .build();				
        ImageLoaderConfiguration config = 
        		new ImageLoaderConfiguration.Builder(this)
        			.defaultDisplayImageOptions(defaultOptions)
        			.build();
        ImageLoader.getInstance().init(config);
	}
	
	/**
	 * 获取传入的指定文件夹�?
	 * */
	private void getIntents()
	{
		Intent intent = getIntent();
		foldName = intent.getStringExtra("FoldName");
		bucketID = intent.getStringExtra("BucketID");
	}
	
	/**
	 *  根据queryKey�?索该文件夹下的全部pics
	 * */	
	private void queryPics()
	{
		// defines the columns that will be returned for each row
		String[] mProjection = 
			{
				Media._ID,
				Media.BUCKET_ID,
				Media.DATA,
				Media.TITLE,
				Media.MIME_TYPE,
				Media.SIZE,
				Media.DATE_ADDED
			};
		
		// 替代String, initialize it
		String[] mSelectionArray = {""};
		mSelectionArray[0] = bucketID;
		
		// 筛�?�条�?
		String mSelectionClause = Media.BUCKET_ID + "= ?";
		
		Cursor mCursor =  getContentResolver().query(
				Media.EXTERNAL_CONTENT_URI, 
				mProjection,
				mSelectionClause,
				mSelectionArray,
				null);
		
		// count the serial num of the item
		String code = "1";
		try {
			while (mCursor != null && mCursor.moveToNext()){
				String idStr = mCursor.getString(
						mCursor.getColumnIndex(Media._ID)); 
				String bucketIDStr = mCursor.getString(
						mCursor.getColumnIndex(Media.BUCKET_ID)); 
				String data = mCursor.getString(
						mCursor.getColumnIndex(Media.DATA));				
				String picName = mCursor.getString(
						mCursor.getColumnIndex(Media.TITLE));
				String mimeType = mCursor.getString(
						mCursor.getColumnIndex(Media.MIME_TYPE));
				String size = mCursor.getString(
						mCursor.getColumnIndex(Media.SIZE));
				long timeStamp = mCursor.getLong(
						mCursor.getColumnIndex(Media.DATE_ADDED));	
				
				String picType = mimeType.split("/")[1].toUpperCase();
				
				String picSize = convertByte(String.valueOf(size));
				String date = convertTimeStamp(timeStamp);
				
				Map<String, String> picMap = new HashMap<String, String>();
				picMap.put("_ID", idStr);
				picMap.put("BUCKET_ID", bucketIDStr);
				picMap.put("DATA", data);
				picMap.put("TITLE", picName);
				picMap.put("TYPE", picType);
				picMap.put("SIZE", picSize);
				picMap.put("DATE_ADDED", date);
				picMap.put("Code", code);
				code = String.valueOf(Integer.parseInt(code) + 1);
				picsList.add(picMap);
			}
		} finally {
			mCursor.close();
		}
	}
	
	
	/**
	 * 初始化文件夹中的 图片列表
	 * */
	private void initPicList()
	{
		// 直接使用SimpleAdapter加载数据
//		SimpleAdapter adapter = new SimpleAdapter(
//				SingleFoldPics.this,
//				picsList,
//				R.layout.pic_info,
//				new String[]{"TITLE", "TYPE", "SIZE", "DATE_ADDED", "Code"},
//				new int[]{	R.id.picName, R.id.picType, 
//							R.id.picSize, R.id.picCreateDate, R.id.serialCode});
//		picLV.setAdapter(adapter);
		
		PicAdapter picAdapter = new PicAdapter(SingleFoldPics.this, picsList);
		picLV.setAdapter(picAdapter);
		
		// 弹窗显示图片
		picLV.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent
				, View source, int position, long id)
			{
				// 加载view.xml界面布局文件
				View viewDialog = getLayoutInflater().inflate(
					R.layout.view, null);
				// 获取viewDialog中ID为image的组�?
				ImageView image = (ImageView) viewDialog
					.findViewById(R.id.image);
				// 设置image显示指定图片
				image.setImageBitmap(BitmapFactory.decodeFile(
					picsList.get(position).get("DATA")));
				// 使用对话框显示用户单击的图片
				new AlertDialog.Builder(SingleFoldPics.this)
					.setView(viewDialog)
					.setPositiveButton("OK", null)
					.show();
			}
		});
	}
	
	/**
	 * convert UNIX timestamp to Data String
	 * @param timestamp
	 * */
	private String convertTimeStamp(long unixTime)
	{
		String date = null;
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss E");
		date = formatter.format(new Date(unixTime * 1000));
		return date;
	}
	
	
	/**
	 * convert byte to KB/MB or do nothing
	 * @param sizeStr
	 * */
	private String convertByte(String sizeStr){
		if(TextUtils.isEmpty(sizeStr)){
			return "未知";
		}
		int size = Integer.parseInt(sizeStr);
		int sizeKB = size / 1024;
		int sizeB = size % 1024;
		float sizeMB = 0;
		String result = null;
		if(0 == sizeKB){
			result = String.valueOf(size);
		} else if (sizeB > 512 && sizeKB < 1024){
			sizeKB++;
			result = String.valueOf(sizeKB) + "KB";
		} else if(sizeKB > 1024){
			if(sizeB > 512){
				sizeKB++;
			}
			sizeMB = (float) sizeKB / 1024;
			DecimalFormat df = new DecimalFormat(".00");
			result = df.format(sizeMB) + "MB";
		} else {
			result = String.valueOf(sizeKB) + "KB";
		}
		return result;
	}
}

