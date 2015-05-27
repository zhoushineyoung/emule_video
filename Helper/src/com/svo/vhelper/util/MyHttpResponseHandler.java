package com.svo.vhelper.util;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;

import com.svo.library.common.persistence.http.JsonHttpResponseHandler;
import com.svo.library.widget.dialog.LoadingDialog;
public class MyHttpResponseHandler extends JsonHttpResponseHandler {
	private Activity activity;
	public MyHttpResponseHandler(Activity activity) {
		this.activity = activity;
	}
	private LoadingDialog dialog;
	@Override
	public void onStart() {
		super.onStart();
		if (dialog == null || !dialog.isShowing()) {
			dialog = new LoadingDialog(activity);
			dialog.show();
		}
	}
	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
		super.onSuccess(statusCode, headers, response);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	@Override
	public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
		super.onSuccess(statusCode, headers, response);
		if (dialog != null) {
			dialog.dismiss();
		}
	}
	@Override
	public void onFailure(Throwable e, JSONObject errorResponse) {
		super.onFailure(e, errorResponse);
//		RLUiUtil.toast(activity, R.string.check_net);
	}
	@Override
	public void onFinish() {
		super.onFinish();
		if (dialog != null) {
			dialog.dismiss();
		}
	}
}
