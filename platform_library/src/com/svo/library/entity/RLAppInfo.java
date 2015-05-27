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

import android.graphics.drawable.Drawable;

public class RLAppInfo implements Serializable{
	private static final long serialVersionUID = 30332476274311364L;
	private String packageName;
	private String appName;
	private String versionName;
	private int versionCode;
	private Drawable appIcon;
	private boolean isSys;
	private String publicSourceDir;
	private String dataDir;
	private long firstInstallTime;
	private long lastUpdateTime;
	private String processName;
	private int targetSdkVersion;
	private String targetOsVersion;
	private String nativeLibDir;
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public int getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}
	public Drawable getAppIcon() {
		return appIcon;
	}
	public void setAppIcon(Drawable appIcon) {
		this.appIcon = appIcon;
	}
	public boolean isSys() {
		return isSys;
	}
	public void setSys(boolean isSys) {
		this.isSys = isSys;
	}
	public String getDataDir() {
		return dataDir;
	}
	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}
	public String getPublicSourceDir() {
		return publicSourceDir;
	}
	public void setPublicSourceDir(String publicSourceDir) {
		this.publicSourceDir = publicSourceDir;
	}
	public long getFirstInstallTime() {
		return firstInstallTime;
	}
	public void setFirstInstallTime(long firstInstallTime) {
		this.firstInstallTime = firstInstallTime;
	}
	public long getLastUpdateTime() {
		return lastUpdateTime;
	}
	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}
	public String getProcessName() {
		return processName;
	}
	public void setProcessName(String processName) {
		this.processName = processName;
	}
	public int getTargetSdkVersion() {
		return targetSdkVersion;
	}
	public void setTargetSdkVersion(int targetSdkVersion) {
		this.targetSdkVersion = targetSdkVersion;
	}
	public String getTargetOsVersion() {
		return targetOsVersion;
	}
	public void setTargetOsVersion(String targetOsVersion) {
		this.targetOsVersion = targetOsVersion;
	}
	public String getNativeLibDir() {
		return nativeLibDir;
	}
	public void setNativeLibDir(String nativeLibDir) {
		this.nativeLibDir = nativeLibDir;
	}
}