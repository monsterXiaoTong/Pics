package com.jog.pics;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Images.Media;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;


/**
 * @author 
 * @deprecate:
 */

public class PicScan extends Activity
{
	Button add;
	Button view;
	ListView show;
	ArrayList<String> names = new ArrayList<String>();
	ArrayList<String> descs = new ArrayList<String>();
	ArrayList<String> fileNames = new ArrayList<String>();
	/** 文件 按钮*/
	private Button fold = null;
	
	/** TextView*/
	private TextView count = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);		
		setContentView(R.layout.main);
	
		add = (Button) findViewById(R.id.add);
		view = (Button) findViewById(R.id.view);
		fold = (Button) findViewById(R.id.fold);
		show = (ListView) findViewById(R.id.show);
		count = (TextView) findViewById(R.id.countPic);
		add.setEnabled(false);
		
		fold.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(
						PicScan.this, FoldClass.class);
				startActivity(intent);
			}
		});
		
		/**
		 * add按钮添加事件监听�?
		 * */
		add.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 创建ContentValues对象,准备插入数据
				ContentValues values = new ContentValues();
				values.put(Media.DISPLAY_NAME, "jinta");
				values.put(Media.DESCRIPTION, "Gold");
				values.put(Media.MIME_TYPE, "image/jpeg");
				// 播放数据,返回�?插入数据的Uri
				Uri uri = getContentResolver().insert(
					Media.EXTERNAL_CONTENT_URI, values);
				// 加载应用程序下的 金塔 图片
				Bitmap bitmap = BitmapFactory.decodeResource(
						PicScan.this.getResources(),
					R.drawable.jinta);
				OutputStream os = null;
				try {
					// 获取刚插入的数据的Uri对应的输出流
					os = getContentResolver().openOutputStream(uri); 
					// 将bitmap图片保存到Uri对应的数据节点中
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
					os.close();
				}
				catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		
		// 为view按钮的单击事件绑定事件监听器
		view.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// 清空names,descs,fileNames集合里原有的数据
				names.clear();
				descs.clear();
				fileNames.clear();
				// ͨ通过ContentResolver查询�?有图片信�?
				// EXTERNAL_CONTENT_URI
				Cursor cursor = getContentResolver().query(
					Media.EXTERNAL_CONTENT_URI, null, null, null, null);
				while (cursor.moveToNext())
				{
					//�?获取图片的显示名 
					//String name = cursor.getString(cursor
					//	.getColumnIndex(Media.DISPLAY_NAME));
					String name = cursor.getString(cursor
							.getColumnIndex(Media.DATA));
					
					// 获取图片的描述信�?-- 图片文件的大�?
					//String desc = cursor.getString(cursor
					// 	.getColumnIndex(Media.DESCRIPTION));
					String desc = "";
					float sizeMB;
					int size = cursor.getInt(cursor
							.getColumnIndex(Media.SIZE));
					int sizeKB = size / 1024;
					int sizeB = size % 1024;
					if(0 == sizeKB){
						desc = String.valueOf(size) + "B";
					} else if (sizeB > 512 && sizeKB < 1024){
						sizeKB++;
						desc = String.valueOf(sizeKB) + "KB";
					} else if(sizeKB > 1024){
						if(sizeB > 512){
							sizeKB++;
						}
						sizeMB = (float) sizeKB / 1024;
						DecimalFormat df = new DecimalFormat(".00");
						desc = df.format(sizeMB) + "MB";
					} else {
						desc = String.valueOf(sizeKB) + "KB";
					}
					
					// 获取图片实际内容
					//byte[] data = cursor.getBlob(cursor
					//	.getColumnIndex(Media.DATA));
					// 获取图片的路�?
					String data = cursor.getString(cursor
							.getColumnIndex(Media.DATA));
					
					// 将图片名添加到names集合�?
					names.add(name);
					
					// 将图片的描述信息添加到descs集合�?
					descs.add(desc);
					
					// 将图片保存路径添加到fileNames集合�?
					fileNames.add(data);
					//fileNames.add(new String(data, 0, data.length - 1));
				}
				// 设置countPic显示的图片数�?
				count.setText(names.size() + "");
				
				// 创建�?个List,其集合元素是Map
				List<Map<String, Object>> listItems =
					new ArrayList<Map<String, Object>>();
				// 将names与descs两个集合对象的数据转换到Map集合�?
				for (int i = 0; i < names.size(); i++)
				{
					Map<String, Object> listItem =
						new HashMap<String, Object>();
					listItem.put("name", names.get(i));
					listItem.put("desc", descs.get(i));
					listItem.put("num", i);
					listItems.add(listItem);
				}
				// 创建�?个SimpleAdapter
				SimpleAdapter simpleAdapter = new SimpleAdapter(
					PicScan.this, 
					listItems, 
					R.layout.line,
					new String[]{"name", "desc", "num"}, 
					new int[]{R.id.name, R.id.desc, R.id.num});
				// 为show设置Adapter
				show.setAdapter(simpleAdapter);
			}
		});
		
		// 为show ListView列表项单击事件添加监听器
		show.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent,
					View source, int position, long id)
			{
				// 加载view.xml界面布局文件
				View viewDialog = getLayoutInflater().inflate(
					R.layout.view, null);
				// 获取viewDialog中ID为image的组�?
				ImageView image = (ImageView) viewDialog
					.findViewById(R.id.image);
				// 设置image显示指定图片
				image.setImageBitmap(BitmapFactory.decodeFile(
					fileNames.get(position)));
				// 使用对话框显示用户单击的图片
				new AlertDialog.Builder(PicScan.this)
					.setView(viewDialog)
					.setPositiveButton("OK", null)
					.show();
			}
		});
	}
}
