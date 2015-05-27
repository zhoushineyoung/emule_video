package com.svo.vhelper.adapter;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.svo.library.util.PicUtil;
import com.svo.vhelper.R;
import com.svo.vhelper.util.EncryptUtil;
import com.svo.vhelper.util.Utils;

public class Ed2kAdapter extends BaseAdapter {
//	private Context context;
	private List<JSONObject> list;
	private LayoutInflater inflater;
	private EncryptUtil encryp;
	public Ed2kAdapter(Context context,List<JSONObject> list){
//		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
		encryp = new EncryptUtil();
	}
	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		}
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		JSONObject jo = list.get(position);
		String link = jo.optString("link");
		link = new EncryptUtil().decrypt(link);
		return link;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.ed2k_item, null);
		}
		TextView nameTv = (TextView) convertView.findViewById(R.id.nameTv);
		TextView infoTv = (TextView) convertView.findViewById(R.id.infoTv);
		ImageView thumb = (ImageView) convertView.findViewById(R.id.thumb);
		JSONObject jo = list.get(position);
		String link = jo.optString("link");
		link = new EncryptUtil().decrypt(link);
		nameTv.setText(Utils.getEd2kName(link));
//		infoTv.setText(String.format("文件大小:%s | 种子数:%d", Utils.sizeConvert(jo.optLong("size")),jo.optInt("seed_count")));
		infoTv.setText(String.format("文件大小:%s", Utils.sizeConvert(jo.optLong("size"))));
		if (TextUtils.isEmpty(jo.optString("thumb"))) {
			thumb.setVisibility(View.GONE);
		} else {
			thumb.setVisibility(View.VISIBLE);
			DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
			DisplayImageOptions options = builder.cacheOnDisk(true).cacheInMemory(true).build();
			thumb.setImageResource(R.drawable.main_pic);
			PicUtil.displayImage(jo.optString("thumb"), thumb, options);
		}
		/*DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
		DisplayImageOptions options = builder.cacheOnDisk(true).cacheInMemory(true).build();
		ImageLoader imgLoader=ImageLoader.getInstance();
		thumb.setImageResource(R.drawable.main_pic);
		imgLoader.displayImage("http://thumb.donkey4u.com/"+jo.optString("hash")+"/thumb.jpg",thumb,options);*/
		return convertView;
	}

}
