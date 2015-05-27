package com.svo.library.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.svo.library.R;

public class LoadingDialog extends Dialog {
	private TextView loadingText;
	private RotateImageView rotateImageView;

	public LoadingDialog(Context paramContext) {
		super(paramContext,R.style.BalanceDialog);
	}

	public LoadingDialog(Context paramContext, int paramInt) {
		super(paramContext, paramInt);
	}

	public void dismiss() {
		super.dismiss();
	}

	protected void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.load_dialog, null);
		setContentView(view);
		Window window = getWindow();
		window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		this.rotateImageView = ((RotateImageView) view
				.findViewById(R.id.loading_progress));
		this.loadingText = ((TextView) findViewById(R.id.loading_text));
	}

	protected void onStop() {
		super.onStop();
	}

	public void show() {
		super.show();
		this.rotateImageView.rotate();
	}

	public void show(String paramString) {
		show();
		try {
			this.loadingText.setText(paramString + "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setMsg(String msg) {
		try {
			this.loadingText.setText(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
