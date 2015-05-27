/**
 * Copyright (c) 2013-2014, 杜卫宾 快速开发平台(http://1.playandroid.duapp.com/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.svo.library.widget.dialog;

import com.svo.library.R;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;

public abstract class RLDialog extends Dialog{
	private Window window;
	private LayoutParams layoutParams;
	
	/**
	 * 
	 * @param context
	 */
	public RLDialog(Context context) {
		super(context,R.style.DIALOG_THEME);
	}
	
	/**
	 * 
	 * @param context
	 * @param theme
	 */
	public RLDialog(Context context, int theme) {
		super(context,theme);
	}
	
	/**
	 * 
	 */
	public void createView(){
		super.setContentView(getView());
		window=getWindow();
		layoutParams=window.getAttributes();
        super.setCanceledOnTouchOutside(true);
        window.addFlags(LayoutParams.FLAG_DIM_BEHIND);  
        layoutParams.alpha=0.98765f;
        layoutParams.dimAmount=0.4321f;
		window.setWindowAnimations(R.style.ANIMATIONS_FADE);
	}
	
	/**
	 * 
	 * @param xPos
	 * @param yPos
	 */
	public void setWindowPosition(int xPos, int yPos){
		if(layoutParams==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		layoutParams.x = xPos;
		layoutParams.y = yPos;
	}
	
	/**
	 * 
	 * @param width
	 * @param height
	 */
	public void setWindowSize(int width, int height){
		if(layoutParams==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		layoutParams.width=width;
		layoutParams.height=height;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getWindowWidth(){
		if(layoutParams==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		return layoutParams.width;
	}
	
	/**
	 * 
	 * @return
	 */
	public int getWindowHeight(){
		if(layoutParams==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		return layoutParams.height;
	}
	
	/**
	 * 
	 * @param gravity
	 */
	public void setWindowGravity(int gravity){
		if(window==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		window.setGravity(gravity);
	}
	
	/**
	 * 
	 * @param bgAlpha
	 */
	public void setWindowBgAlpha(float bgAlpha){
		if(layoutParams==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		layoutParams.dimAmount = bgAlpha;
	}
	
	/**
	 * 
	 * @param anims
	 */
	public void setWindowAnimations(int anims){
		if(window==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		window.setWindowAnimations(anims);
	}
	
	/**
	 * 
	 * @param alpha
	 */
	public void setWindowAlpha(float alpha){
		if(layoutParams==null){
			throw new RuntimeException("createView() should be called before this method!");
		}
		layoutParams.alpha=alpha;
	}
	
	/**
	 * 
	 * @return
	 */
	protected abstract View getView();
}