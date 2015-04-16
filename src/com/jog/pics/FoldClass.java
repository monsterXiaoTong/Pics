package com.jog.pics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;

/**
 * @create date 2014-12-11
 * @author xiaotong zhao
 * @class description 全局图片文件  分类列表显示 界面
 */
public class FoldClass extends Activity
{
	/** 文件夹ListView*/
	private ListView foldLV = null;
	
	/** 文件夹信息列表*/
	private List<Map<String, String>> foldList = 
			new ArrayList<Map<String, String>>();
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.fold_class_layout);		
		foldLV = (ListView) findViewById(R.id.foldList);	
		getData();
		initViews();
		
		// ListView添加列表项单击监听器
		foldLV.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String foldName = foldList.get(position).get("FoldName");
				String bucketID = foldList.get(position).get("BucketID");
				Intent intent = new Intent(FoldClass.this, TimeTable.class);
				intent.putExtra("FoldName", foldName);
				intent.putExtra("BucketID", bucketID);
				startActivity(intent);
			}
		});
	}


	/** 
	 * 获取并解析 
	 * */
	private void getData()
	{
		foldList.clear();		
		
		Cursor cursor = getContentResolver()
				.query(Media.EXTERNAL_CONTENT_URI, null, null, null, 
						Media.DATE_ADDED + " DESC");
		
		try {
			// iterator the cursor
			while(cursor != null && cursor.moveToNext()){
				// # 1 #  	Bucket ID
				String bucketID = cursor.getString(
						cursor.getColumnIndex(Media.BUCKET_ID));				
				// # 2 # 	Bucket Name
				String bucketName = cursor.getString(
						cursor.getColumnIndex(Media.BUCKET_DISPLAY_NAME));
				
				// # 3 #    data
				String fullPath = cursor.getString(cursor
						.getColumnIndex(Media.DATA));
				int end = fullPath.length();
				StringBuilder sBuilder = new StringBuilder(fullPath);
				String[] foldArray =  fullPath.split("/");
				int pathLength = foldArray.length;
				String displayFullName = foldArray[pathLength - 1];
				// 			end - '/displayFullName'
				int start = end - displayFullName.length() - bucketName.length() -1;
				// cut the tail- FoldName
				sBuilder.replace(start , end, "");
				
				// cut the head- "/storage"
				// # 4 # 	path of the fold which certain pic's 'fold' locate in
				String foldFullPath = sBuilder.replace(0 , "/storage".length(), "").toString();	
								
				if(cursor.isFirst()){
					Map<String, String> newPath1 = 
							new HashMap<String, String>();
					// Key1 (a new list-member)		
					newPath1.put("BucketID", bucketID);	
					newPath1.put("FoldName", bucketName);					
					newPath1.put("FoldPath", foldFullPath);
					newPath1.put("FileCount", String.valueOf(1));

					foldList.add(newPath1);				
				} else {			
					for(int i=0;i < foldList.size(); i++){	
						// no match foldPath, then add it 
						if(!(foldList.get(i).containsValue(bucketID)) && 
								i == foldList.size() - 1){
							Map<String, String> newPath = 
									new HashMap<String, String>();
							// Key1 (a new list-member)
							newPath.put("FoldPath", foldFullPath);
							newPath.put("FoldName", bucketName);
							newPath.put("FileCount", String.valueOf(1));
							newPath.put("BucketID", bucketID);
							foldList.add(newPath); 
							break;
						} else if (foldList.get(i).containsValue(bucketID)){
							// Key3"FileCount" add one
							int count = Integer.valueOf(foldList.get(i).get("FileCount"));
							foldList.get(i).put("FileCount", String.valueOf(++count));
							break;
						}						
					}  
				}
			} 
		} finally {
			cursor.close();
		}
	}
			
	/** 

	 * */
	private void initViews(){
		// 创建一个SimpleAdapter
		SimpleAdapter adapter = new SimpleAdapter(
			FoldClass.this, 
			foldList, 
			R.layout.fold_info,
			new String[]{"FoldPath", "FoldName", "FileCount"}, 
			new int[]{R.id.foldPath, R.id.foldName, R.id.fileCount});
		// 为show设置Adapter
		foldLV.setAdapter(adapter);		
	}

}