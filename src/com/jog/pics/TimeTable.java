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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.ListView;

import com.jogger.pic.adapter.TableAdapter;
import com.jogger.pic.utils.UtilityTools;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * @create date 2014-12-22
 * @author xiaotong zhao
 * @class description show the pics in one fold in the TimeTable
 */

public class TimeTable extends Activity {

	/** TimeTable List */
	private ListView timeList = null;

	/** queryKey1: foldName */
	private String foldName = null;

	/** queryKey2: foldID */
	private String bucketID = null;

	/** all pics of fold List */
	private List<Map<String, String>> picsList = new ArrayList<Map<String, String>>();

	/** classified the purpose pics */
	private List<Map<String, Object>> tableList = new ArrayList<Map<String, Object>>();

	/** UMeng */
	// UMSocialService mController = null;

	@Override
	protected void onCreate(Bundle savedInstancedState) {
		super.onCreate(savedInstancedState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_timetable);

		getViews();
		getIntents();
		initData();
		loadView();
	}

	/**
	 * get widgets of the layout
	 * */
	private void getViews() {
		timeList = (ListView) findViewById(R.id.picListView);

		// ImageLoader 异步加载图片
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisk(true).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this).defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);

	}

	/**
	 * initialize UMeng
	 * */
	// private void initUmeng(){
	// // 首先在您的Activity中添加如下成员变�?
	// mController = UMServiceFactory.getUMSocialService("com.umeng.share");
	//
	// EmailHandler emailHandler = new EmailHandler();
	// emailHandler.addToSocialSDK();
	//
	// SmsHandler smsHandler = new SmsHandler();
	// smsHandler.addToSocialSDK();
	//
	// // 添加易信平台,参数1为当前activity, 参数2为在易信�?放平台申请到的app id
	// UMYXHandler yixinHandler = new UMYXHandler(this,
	// "54ab4178fd98c5d13d000e7d");
	// // 关闭分享时的等待Dialog
	// yixinHandler.enableLoadingDialog(false);
	// // 把易信添加到SDK�?
	// yixinHandler.addToSocialSDK();
	//
	// // 易信朋友圈平�?,参数1为当前activity, 参数2为在易信�?放平台申请到的app id
	// UMYXHandler yxCircleHandler = new UMYXHandler(this,
	// "54ab4178fd98c5d13d000e7d");
	// yxCircleHandler.setToCircle(true);
	// yxCircleHandler.addToSocialSDK();
	//
	//
	// UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, "100424468",
	// "c7394704798a158208a74ab60104f0ba");
	// qqSsoHandler.addToSocialSDK();
	//
	// //参数1为当前Activity�? 参数2为开发�?�在QQ互联申请的APP ID，参�?3为开发�?�在QQ互联申请的APP kEY.
	// QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(this, "100424468",
	// "c7394704798a158208a74ab60104f0ba");
	// qZoneSsoHandler.addToSocialSDK();
	//
	// // wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里�?要替换成你注册的AppID
	// String appID = "wx967daebe835fbeac";
	// String appSecret = "5fa9e68ca3970e87a1f83e563c8dcbce";
	// // 添加微信平台
	// UMWXHandler wxHandler = new UMWXHandler(this,appID,appSecret);
	// wxHandler.addToSocialSDK();
	// // 支持微信朋友�?
	// UMWXHandler wxCircleHandler = new UMWXHandler(this,appID,appSecret);
	// wxCircleHandler.setToCircle(true);
	// wxCircleHandler.addToSocialSDK();
	//
	// /**
	// * 设置分享内容
	// * */
	// // 设置分享内容
	// mController.setShareContent("友盟社会化组件（SDK）让移动应用快�?�整合社交分享功�?");
	// // 设置分享图片, 参数2为图片的url地址
	// mController.setShareMedia(new UMImage(this,
	// "http://www.baidu.com/img/bdlogo.png"));
	// }

	/**
	 * 获取传入的指定文件夹�?
	 * */
	private void getIntents() {
		Intent intent = getIntent();
		foldName = intent.getStringExtra("FoldName");
		bucketID = intent.getStringExtra("BucketID");
	}

	/**
	 * initialise the TimeTable List
	 * */
	private void initData() {

		queryPics();

		/**
		 * "2014-12-21 12:32  星期�?"
		 * */
		// 同一天的Pics集合的全部信�?
		Map<String, Object> weekMap = new HashMap<String, Object>();
		String dateFullFormat;
		String weekDay;
		String date;
		// parse the raw PicData
		for (int i = 0; i < picsList.size(); i++) {
			/** detailList */
			List<Map<String, String>> detailList = new ArrayList<Map<String, String>>();
			Map<String, String> picMap;
			dateFullFormat = picsList.get(i).get("DATE_ADDED");
			;
			weekDay = dateFullFormat.split(" ")[2];
			;
			date = dateFullFormat.split(" ")[0];
			if (0 == i) {
				picMap = new HashMap<String, String>();
				picMap.put("_ID", picsList.get(i).get("_ID"));
				picMap.put("PATH", picsList.get(i).get("DATA"));
				detailList.add(picMap);

				weekMap.put("WEEKDAY", weekDay);
				weekMap.put("DATE", date);
				weekMap.put("PICLIST", detailList);
				tableList.add(weekMap);
			} else {
				for (int j = 0; j < tableList.size(); j++) {
					if (!date.equals(tableList.get(j).get("DATE"))
							&& j == tableList.size() - 1) {
						picMap = new HashMap<String, String>();
						picMap.put("_ID", picsList.get(i).get("_ID"));
						picMap.put("PATH", picsList.get(i).get("DATA"));

						detailList = new ArrayList<Map<String, String>>();
						detailList.add(picMap);

						weekMap = new HashMap<String, Object>();
						weekMap.put("WEEKDAY", weekDay);
						weekMap.put("DATE", date);
						weekMap.put("PICLIST", detailList);
						tableList.add(weekMap);
						break;
					} else if (tableList.get(j).get("DATE").equals(date)) {
						picMap = new HashMap<String, String>();
						picMap.put("_ID", picsList.get(i).get("_ID"));
						picMap.put("PATH", picsList.get(i).get("DATA"));
						((ArrayList<Map>) tableList.get(j).get("PICLIST"))
								.add(picMap);
						break;
					}
				}
			}
		}
	}

	/**
	 * 加载picList
	 * */
	private void loadView() {
		TableAdapter adapter = new TableAdapter(this, tableList);
		timeList.setAdapter(adapter);
	}

	/**
	 * 根据queryKey�?索该文件夹下的全部pics
	 * */
	private void queryPics() {
		// defines the columns that will be returned for each row
		String[] mProjection = { Media._ID, Media.BUCKET_ID, Media.DATA,
				Media.TITLE, Media.MIME_TYPE, Media.SIZE, Media.DATE_ADDED };
		// 筛�?�条�?
		String mSelectionClause = Media.BUCKET_ID + "= ?";

		// 替代”？“的String
		String[] mSelectionArray = { "" };
		mSelectionArray[0] = bucketID;

		Cursor mCursor = getContentResolver().query(Media.EXTERNAL_CONTENT_URI,
				mProjection, mSelectionClause, mSelectionArray,
				Media.DATE_ADDED + " DESC");

		// count the serial num of the item
		String code = "1";
		try {
			while (mCursor != null && mCursor.moveToNext()) {
				String idStr = mCursor.getString(mCursor
						.getColumnIndex(Media._ID));
				String bucketIDStr = mCursor.getString(mCursor
						.getColumnIndex(Media.BUCKET_ID));
				String data = mCursor.getString(mCursor
						.getColumnIndex(Media.DATA));
				String picName = mCursor.getString(mCursor
						.getColumnIndex(Media.TITLE));
				String mimeType = mCursor.getString(mCursor
						.getColumnIndex(Media.MIME_TYPE));
				String size = mCursor.getString(mCursor
						.getColumnIndex(Media.SIZE));
				long timeStamp = mCursor.getLong(mCursor
						.getColumnIndex(Media.DATE_ADDED));

				String picType = mimeType.split("/")[1].toUpperCase();

				String picSize = UtilityTools.convertByte(String.valueOf(size));
				String date = UtilityTools.convertTimeStamp(timeStamp);

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
	 * when open the options_menu, callback this method
	 * */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflator = new MenuInflater(this);
		inflator.inflate(R.menu.options_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * invoke when user click on menuitem of the Options menu
	 * */
	@Override
	public boolean onOptionsItemSelected(MenuItem mi) {
		switch (mi.getItemId()) {
		case R.id.exit:
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_HOME);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			android.os.Process.killProcess(android.os.Process.myPid());
			break;

		// case R.id.share:
		// mController.openShare(this, false);
		//
		// break;
		}
		return true;
	}

}
