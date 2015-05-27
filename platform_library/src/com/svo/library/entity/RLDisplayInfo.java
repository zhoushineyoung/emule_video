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
package com.svo.library.entity;

import java.io.Serializable;

public class RLDisplayInfo implements Serializable{
	private static final long serialVersionUID = 4348988349135775928L;
	
	private int displayWidth;
	private int displayHeight;
	private float displayDensity;
	private int statusBarHeight;
	private int portraitNavigationBarHeight;
	private int landscapeNavigationBarHeight;
	public int getDisplayWidth() {
		return displayWidth;
	}
	public void setDisplayWidth(int displayWidth) {
		this.displayWidth = displayWidth;
	}
	public int getDisplayHeight() {
		return displayHeight;
	}
	public void setDisplayHeight(int displayHeight) {
		this.displayHeight = displayHeight;
	}
	public float getDisplayDensity() {
		return displayDensity;
	}
	public void setDisplayDensity(float displayDensity) {
		this.displayDensity = displayDensity;
	}
	public int getStatusBarHeight() {
		return statusBarHeight;
	}
	public void setStatusBarHeight(int statusBarHeight) {
		this.statusBarHeight = statusBarHeight;
	}
	public int getPortraitNavigationBarHeight() {
		return portraitNavigationBarHeight;
	}
	public void setPortraitNavigationBarHeight(int portraitNavigationBarHeight) {
		this.portraitNavigationBarHeight = portraitNavigationBarHeight;
	}
	public int getLandscapeNavigationBarHeight() {
		return landscapeNavigationBarHeight;
	}
	public void setLandscapeNavigationBarHeight(int landscapeNavigationBarHeight) {
		this.landscapeNavigationBarHeight = landscapeNavigationBarHeight;
	}
}