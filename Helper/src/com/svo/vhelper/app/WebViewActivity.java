package com.svo.vhelper.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.TextView;

import com.svo.vhelper.R;

public class WebViewActivity extends Activity implements OnClickListener {
	private WebView webView;
	private TextView titleTv;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.webview);
		findViewById(R.id.btn_back).setOnClickListener(this);
		Intent intent = getIntent();
		String title = intent.getStringExtra("title");
		String url = intent.getStringExtra("url");
		titleTv = (TextView) findViewById(R.id.txt_title);
		titleTv.setText(title);
		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		webView.loadUrl(url);
	}
	@Override
	public void onClick(View v) {
		finish();
	}
}
