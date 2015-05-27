package com.svo.vhelper.app;

import java.net.URLEncoder;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.svo.library.common.persistence.http.JsonHttpResponseHandler;
import com.svo.library.common.persistence.http.RequestParams;
import com.svo.library.util.HttpUtil;
import com.svo.library.util.RLUiUtil;
import com.svo.library.widget.dialog.RLAlertDialog;
import com.svo.library.widget.dialog.RLAlertDialog.Listener;
import com.svo.library.widget.xListView.XListView;
import com.svo.library.widget.xListView.XListView.IXListViewListener;
import com.svo.vhelper.R;
import com.svo.vhelper.adapter.Ed2kAdapter;
import com.svo.vhelper.util.Cons;
import com.svo.vhelper.util.MyHttpResponseHandler;
import com.svo.vhelper.util.Utils;

public class Search1Activity extends Activity implements OnClickListener, IXListViewListener, OnItemClickListener {
	private int NUM = 10;
	private EditText editText;
	private Button searchBtn;
	private String key;
	private XListView listView;
	private LinkedList<JSONObject> list = new LinkedList<JSONObject>();
	private Ed2kAdapter adapter;
	private int curPage = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search1);
		editText = (EditText) findViewById(R.id.searchEt);
		searchBtn = (Button) findViewById(R.id.searchBtn);
		searchBtn.setOnClickListener(this);
		editText.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					onClick(null);
				}
				return false;
			}
		});
		listView = (XListView) findViewById(R.id.list);
		adapter = new Ed2kAdapter(this, list);
		listView.setAdapter(adapter);
		listView.setPullRefreshEnable(false);
		listView.setPullLoadEnable(false);
		listView.setXListViewListener(this);
		listView.setOnItemClickListener(this);
	}
	@Override
	public void onClick(View v) {
		key = editText.getText().toString().trim();
		if (TextUtils.isEmpty(key)) {
			RLUiUtil.toast(this, "搜索内容不可为空");
			return;
		}
		curPage = 1;
		RequestParams params = new RequestParams();
		params.put("flag", "ed2kSearch");
		params.put("key", key);
		params.put("num", NUM+"");
		params.put("page", ""+curPage);
		HttpUtil.post(this,Cons.gen+"Emule",params, new MyHttpResponseHandler(this){
			@Override
			public void onSuccess(JSONArray response) {
				super.onSuccess(response);
				list.clear();
				for (int i = 0; i < response.length(); i++) {
					list.add(response.optJSONObject(i));
				}
				listView.setPullLoadEnable(response.length() == NUM);
				adapter.notifyDataSetChanged();
			}
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				RLUiUtil.toast(getApplicationContext(), "请求失败");
			}
		});
		Utils.closeSoftKeyboard(this);
	}
	@Override
	public void onRefresh() {
	}
	@Override
	public void onLoadMore() {
		curPage++;
		RequestParams params = new RequestParams();
		params.put("flag", "ed2kSearch");
		params.put("key", URLEncoder.encode(key));
		params.put("num", NUM+"");
		params.put("page", ""+curPage);
		HttpUtil.post(this,Cons.gen+"Emule",params , new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONArray response) {
				super.onSuccess(response);
				for (int i = 0; i < response.length(); i++) {
					list.add(response.optJSONObject(i));
				}
				listView.setPullLoadEnable(response.length() == NUM);
				adapter.notifyDataSetChanged();
				listView.stopLoadMore();
			}
			@Override
			public void onFailure(Throwable e, JSONObject errorResponse) {
				super.onFailure(e, errorResponse);
				listView.stopLoadMore();
			}
		});
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (Utils.isAppInstalled(this, "com.svo.v")) {
			String link = (String) adapter.getItem(position-1);
			Utils.open(this, link);
		} else {
			new RLAlertDialog(this, "您还没有安装磁力视频播放器", "是否去市场下载磁力视频播放器", "确定", "取消", new Listener() {
				@Override
				public void onRightClick() {}
				@Override
				public void onLeftClick() {
					Uri uri = Uri.parse("market://details?id=com.svo.v");//id为包名 
		            Intent it = new Intent(Intent.ACTION_VIEW, uri); 
		            startActivity(Intent.createChooser(it, "下载磁力视频播放器")); 
				}
			}).show();
		}
	}
}
