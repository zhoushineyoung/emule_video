package com.svo.vhelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.svo.library.util.RLIntentUtil;
import com.svo.library.util.RLNetUtil;
import com.svo.library.util.RLUiUtil;
import com.svo.vhelper.app.Search1Activity;
import com.svo.vhelper.app.WebViewActivity;
import com.svo.vhelper.util.Cons;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

public class MainActivity extends Activity implements OnClickListener {
	private ImageView btnMore;
	private SlidingMenu menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
		UmengUpdateAgent.update(this);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
        menu = new SlidingMenu(this);
        menu.setMode(SlidingMenu.RIGHT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.frame_menu);
        initView();
    }

	private void initView() {
		findViewById(R.id.btn_back).setVisibility(View.GONE);
        btnMore = (ImageView) findViewById(R.id.btn_more);
        btnMore.setVisibility(View.VISIBLE);
        btnMore.setOnClickListener(this);
        btnMore.setImageResource(R.drawable.ic_more);
        findViewById(R.id.searchEt1).setOnClickListener(this);
        findViewById(R.id.searchEt2).setOnClickListener(this);
        
        menu.findViewById(R.id.checkNew).setOnClickListener(this);
        menu.findViewById(R.id.share).setOnClickListener(this);
        menu.findViewById(R.id.feedback).setOnClickListener(this);
        menu.findViewById(R.id.about).setOnClickListener(this);
        menu.findViewById(R.id.exit).setOnClickListener(this);
	}

    public void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_more:
			menu.showMenu(true);
			break;
		case R.id.searchEt1:
			startActivity(new Intent(this, Search1Activity.class));
			break;
		case R.id.searchEt2:
			
			break;
		case R.id.checkNew:
			if (!RLNetUtil.isNetworkAvailable(this)) {
				RLUiUtil.toast(this, R.string.check_net);
				return;
			}
			UmengUpdateAgent.forceUpdate(this);
			UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
				@Override
				public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
					switch (updateStatus) {
					case UpdateStatus.Yes: // has update
						UmengUpdateAgent.showUpdateDialog(MainActivity.this, updateInfo);
						break;
					case UpdateStatus.No: // has no update
						RLUiUtil.toast(MainActivity.this, "已是最新版");
						break;
					case UpdateStatus.NoneWifi: // none wifi
						RLUiUtil.toast(MainActivity.this, "没有wifi连接， 只在wifi下更新");
						break;
					case UpdateStatus.Timeout: // time out
						RLUiUtil.toast(MainActivity.this, "网络连接超时");
						break;
					}
				}
			});
			break;
		case R.id.share:
			RLIntentUtil.callSysShare(this, "分享给小伙伴", "电驴助手,在线看片", "APP下载地址："+Cons.DOWN_URL, "text/plain", null);
			break;
		case R.id.about:
			Intent intent = new Intent(this,WebViewActivity.class);
			intent.putExtra("title", "关于");
			intent.putExtra("url", "file:///android_asset/about.html");
			startActivity(intent);
			break;
		case R.id.feedback:
			FeedbackAgent agent = new FeedbackAgent(this);
			agent.startFeedbackActivity();
			agent.sync();
			break;
		case R.id.exit:
			finish();
			break;
		default:
			break;
		}
	}
    @Override
    public void onBackPressed() {
    	if (menu.isMenuShowing()) {
			menu.showContent(true);
		}else {
			super.onBackPressed();
		}
    }
}
