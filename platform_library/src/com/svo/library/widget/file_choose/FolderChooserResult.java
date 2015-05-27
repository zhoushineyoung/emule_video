package com.svo.library.widget.file_choose;

import android.os.*;

public class FolderChooserResult implements Parcelable {
	public static final String TAG = FolderChooserResult.class.getSimpleName();

	public String selectedFolder;

	@Override public int describeContents() {
		return 0;
	}

	@Override public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(selectedFolder);
	}

	public static final Parcelable.Creator<FolderChooserResult> CREATOR = new Parcelable.Creator<FolderChooserResult>() {
		@Override public FolderChooserResult[] newArray(int size) {
			return new FolderChooserResult[size];
		}

		@Override public FolderChooserResult createFromParcel(Parcel in) {
			FolderChooserResult res = new FolderChooserResult();
			res.selectedFolder = in.readString();
			return res;
		}
	};
}
