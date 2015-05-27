package com.svo.library.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 图片工具类
 * @author duweibin
 * @version 创建时间:2013年9月6日  图片工具类
 */
public class PicUtil {
	/**
	 * 保存图片
	 * @param imageView
	 * @param path 保存目录
	 * @param picName 图片名字
	 * @return 保存的文件对象;保存失败返回null
	 */
	public static File savePic(ImageView imageView,String path,String picName){
		BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
		if (bitmapDrawable == null) {
			return null;
		}
		return saveBitmap(bitmapDrawable.getBitmap(), path, picName);
	}
	public static File saveBitmap(Bitmap bitmap,String path,String picName){
		File dir = new File(path);
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, picName);
		try {
			FileOutputStream fos = new FileOutputStream(file);
			bitmap.compress(CompressFormat.PNG, 100, fos);
			fos.close();
		}catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return file;
	}
	/**
	 * 使用默认方法展示图片
	 * @param context
	 * @param uri 图片的uri.可接收的uri如下:
	 *  String imageUri = "http://site.com/image.png"; // 从网络加载
		String imageUri = "/mnt/sdcard/image.png"; // 从本地sd卡加载
		String imageUri = "content://media/external/audio/albumart/13"; // 从content provider中加载
		String imageUri = "assets://image.png"; // 从assets中加载
	 * @param imageView
	 */
	public static void displayImage(String uri, ImageView imageView)
	{
		displayImage(uri, imageView, new SimpleImageLoadingListener());
	}
	public static void displayImage(String uri, ImageView image,
			DisplayImageOptions options) {
		ImageLoader imageLoader = ImageLoader.getInstance();
		imageLoader.displayImage(uri, image, options);
	}
	/**
	 * 带回调方法的图片展示方法
	 * @param context
	 * @param uri
	 * @param imageView
	 * @param imageLoadingListener 回调接口
	 */
	public static void displayImage(String uri, ImageView imageView,ImageLoadingListener imageLoadingListener)
	{
		displayImage(uri, imageView, imageLoadingListener,false);
	}
	/**
	* 带回调方法的图片展示方法
	 * @param context
	 * @param uri
	 * @param imageView
	 * @param imageLoadingListener 回调接口
	 * @param isFadeIn 展示图片时是否使用动画，默认不使用
	 */
	public static void displayImage(String uri, ImageView imageView,ImageLoadingListener imageLoadingListener,boolean isFadeIn)
	{
		ImageLoader imageLoader = ImageLoader.getInstance();
		if (!TextUtils.isEmpty(uri) && (uri.startsWith("/mnt/sdcard")||uri.startsWith("/storage"))) {
			uri = "file://".concat(uri);
		}
		imageLoader.displayImage(uri, imageView,imageLoadingListener);
	}
	public static void displayImage(String uri, ImageView imageView,boolean isFadeIn)
	{
		displayImage(uri, imageView, new SimpleImageLoadingListener(){},isFadeIn);
	}
	/**
	 * 根据图片的uri得到图片在sd卡中的路径。
	 * @param uri 图片的uri
	 * @return 以文件对象返回
	 */
	public static File getPicFile(String uri){
		ImageLoader imageLoader = ImageLoader.getInstance();
		return imageLoader.getDiskCache().get(uri);
	}
	 /**
	  * 解析文件对象得到位图
	  * @param file 文件对象
	  * @return 位图
	  */
    public static Bitmap parseBitmap(File file) {
    	Bitmap d;
    	try {
			d = BitmapFactory.decodeFile(file.getAbsolutePath());
		} catch (Throwable e) {
			System.err.println("PicUtil   内存溢出");
			try {
				d = decodeScaledBitmap(file.getAbsolutePath(),4);
			} catch (Throwable e1) {
				try {
				d = decodeScaledBitmap(file.getAbsolutePath(),8);
				} catch (Throwable e2) {
					System.err.println("PicUtil 内存溢出2");
					d = null;
				}
			}
		}
    	return d;
	}
    /**
     * 得到图片的位图
     * @param filename 文件名
     * @param scale 缩小值.2表示长和宽各缩小1/2
     * @return Bitmap
     */
	public static Bitmap decodeScaledBitmap(String filename,int scale) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filename, options);
		options.inSampleSize = scale;
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(filename, options);
	}
	public static File zipImage(String srcPath,int kbSize) {
		BitmapFactory.Options newOpts = new BitmapFactory.Options();
		// 开始读入图片，此时把options.inJustDecodeBounds 设回true了
		newOpts.inJustDecodeBounds = true;
		Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 此时返回bm为空
		newOpts.inJustDecodeBounds = false;
		int w = newOpts.outWidth;
		int h = newOpts.outHeight;
		// 现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
		float hh = 800f;// 这里设置高度为800f
		float ww = 480f;// 这里设置宽度为480f
		// 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
		int be = 1;// be=1表示不缩放
		if (w > h && w > ww) {// 如果宽度大的话根据宽度固定大小缩放
			be = (int) (newOpts.outWidth / ww);
		} else if (w < h && h > hh) {// 如果高度高的话根据宽度固定大小缩放
			be = (int) (newOpts.outHeight / hh);
		}
		if (be <= 0)
			be = 1;
		newOpts.inSampleSize = be;// 设置缩放比例
		// 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
		bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 100;
		while (baos.toByteArray().length / 1024 > kbSize) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			options -= 3;// 每次都减少3
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(baos.toByteArray());
		return  savePic(bis, "/mnt/sdcard/Pictures/",System.currentTimeMillis()+".jpg");// 把压缩后的数据baos存放到ByteArrayInputStream中
	}
	/**
	 * 压缩算法，按480*800压缩，图片大小不大于200Kb.压缩后文件会保存到缓存目录
	 * @param srcPath
	 * @return 压缩后的文件路径,
	 */
	public static File zipImage(String srcPath) {
		return  zipImage(srcPath,200);
	}
	/**
	 * 保存图片
	 * @param inputStream 输入流
	 * @param path 保存目录
	 * @param picName 图片名字
	 * @return 保存的文件对象;保存失败返回null
	 */
	public static File savePic(InputStream inputStream,String path,String picName){
		if (inputStream == null) {
			return null;
		}
		File dir = new File(path);
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, picName);
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(file);
			byte[] bs = new byte[1024 * 4];
			int len = -1;
			while ((len = inputStream.read(bs)) != -1) {
				outputStream.write(bs, 0, len);
			}
			outputStream.flush();
			outputStream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
				try {
					inputStream.close();
					inputStream = null;
					if (outputStream != null) {
						outputStream.close();
						outputStream = null;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
		return file;
	}
}
