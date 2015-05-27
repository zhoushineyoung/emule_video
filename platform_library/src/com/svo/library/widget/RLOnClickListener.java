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
package com.svo.library.widget;

import android.view.View;
/**
 * 
 * @author 杜卫宾<br><br>
 *	重写View.OnClickListener，解决了连续点击过快的重复执行问题
 */
public abstract class RLOnClickListener implements View.OnClickListener{
	private boolean processFlag = true;
	private long delay=500;
	
	/**
	 * 
	 */
	public RLOnClickListener(){
		super();
	}
	
	/**
	 * 
	 * @param delay
	 */
	public RLOnClickListener(long delay){
		super();
		this.delay=delay;
	}
	
	/**
	 * 抽象方法，点击事件处理方法
	 * @param view
	 */
	public abstract void onClickX(View view);

	@Override
	public void onClick(View view){
		 if (processFlag) {
             setProcessFlag();
             onClickX(view);
             new DelayThread().start();
         }
	}
	
    private synchronized void setProcessFlag() {
        processFlag = false;
    }
    
    private class DelayThread extends Thread {
        public void run() {
            try {
                sleep(delay);
                processFlag = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}