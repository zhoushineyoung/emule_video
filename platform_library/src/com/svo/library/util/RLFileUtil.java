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
package com.svo.library.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;
/**
 * 文件操作工具类(单例模式)
 * @author 杜卫宾<br><br>
 * 使用：RLFileUtil util = RLFileUtil.getInstance();<br>
 * util.deleteDir();
 */
public class RLFileUtil {
	/**
	 * 将字符串保存到文件中
	 * @param content 待保存的内容.内容为空则不保存
	 * @param filePath 待保存文件的全路径
	 * @return 保存成功则返回保存的文件对象。保存失败则返回null
	 */
	public static File saveTxtFile(String content,String filePath){
		String[] arr = RLStrUtil.sepPath(filePath);
		return saveTxtFile(content, arr[0], arr[1]);
	}
	
	/**
	 * 将字符串保存到文件中
	 * @param content 待保存的内容.内容为空则不保存
	 * @param path 文件所在的目录
	 * @param fileName 文件名
	 * @return 保存成功则返回保存的文件对象。保存失败则返回null
	 */
	public static File saveTxtFile(String content,String path,String fileName){
		//内容为空则不保存
		if (TextUtils.isEmpty(content)) {
			return null;
		}
		//判断目录是否存在，不存在则创建
		File dir = new File(path);
		if (dir != null && !dir.exists()) {
			dir.mkdirs();
		}
		File file = new File(dir, fileName);
		try {
			PrintStream ps = new PrintStream(file);
			ps.println(content);
			ps.flush();
			ps.close();
			return file;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}  
	
	/**
	 * 读取文本文件
	 * @param filePath 待读取的文件绝对路径 
	 * @return 读取的文本内容;读取失败则返回null
	 */
	public static String readTxtFile(String filePath){
		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return null;
		}
		try {
			Scanner scanner = new Scanner(file);
			StringBuilder sb = new StringBuilder();
			while (scanner.hasNextLine()) {
				sb.append(scanner.nextLine());
			}
			scanner.close();
			return sb.toString();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}
	private static RLFileUtil fileUtil;
	private String rootPath = Environment.getExternalStorageDirectory().getPath();
	/**
	 * 复制文件
	 * @param sourceFile 原文件对象
	 * @param targetFile 目标文件对象
	 */
    public static boolean copyFile(File sourceFile, File targetFile){
        BufferedInputStream inBuff = null;
        BufferedOutputStream outBuff = null;
        try {
            // 新建文件输入流并对它进行缓冲
            inBuff = new BufferedInputStream(new FileInputStream(sourceFile));

            // 新建文件输出流并对它进行缓冲
            outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));

            // 缓冲数组
            byte[] b = new byte[1024 * 5];
            int len;
            while ((len = inBuff.read(b)) != -1) {
                outBuff.write(b, 0, len);
            }
            // 刷新此缓冲的输出流
            outBuff.flush();
            Log.i("FileUtil", "复制文件成功!"); 
            return true;
        }catch (Exception e) {  
        	Log.w("FileUtil", "复制单个文件操作出错!");  
            e.printStackTrace();  
        }   finally {
            try {
				// 关闭流
				if (inBuff != null)
				    inBuff.close();
				if (outBuff != null)
				    outBuff.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
        return false;
    }
	
	/**
	 * 复制某个文件
	 * @param oldPath 原文件的绝对路径
	 * @param newPath 目标文件的绝对路径。
	 * 注意：请确保目标文件所在的目录已存在
	 */
	public static boolean copyFile(String oldPath, String newPath) {  
		File sourceFile = new File(oldPath); 
		File targetFile = new File(newPath);
		return copyFile(sourceFile, targetFile);
    }
	/**
	 * @return
	 */
	public synchronized static RLFileUtil getInstance() {
        if (fileUtil == null) {
        	fileUtil = new RLFileUtil();
        }
        return fileUtil;
    }
	
	/**
	 * 是否有内存卡
	 * @return
	 */
	public static boolean hasExternalStorage(){
		return hasSDCard()||!RLSysUtil.isExternalStorageRemovable();
	}
	
	/**
	 * @return
	 */
	private static boolean hasSDCard(){
		return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
	}
	
	/**
	 * 异步保存文本到某个文件
	 * @param name 文件名或者路径（相对根路径）
	 * @param content 保存内容
	 */
	public void saveFile(final String name,final String content){
		new Thread(){
			@Override
			public void run(){
				FileOutputStream os = null;
				OutputStreamWriter osw = null;
				BufferedWriter bw = null;
				try {
					os = new FileOutputStream(rootPath+name);
					osw=new OutputStreamWriter(os);
					bw=new BufferedWriter(osw);
					bw.write(content);
				} catch (Exception e) {
					e.printStackTrace();
				}finally{
					try {
						if(bw!=null){
							bw.flush();
						}
						if(osw!=null){
							osw.flush();
						}
						if(bw!=null){
							bw.close();
						}
						if(osw!=null){
							osw.close();
						}
						if(os!=null){
							os.close();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}.start();
		
	}
	
	/**
	 * 将文本内容写入sdcard(非异步)
	 * @param filepath 
	 * @param content
	 */
	public void write2Sd(String filepath,String content){
		try{
			FileOutputStream fos=new FileOutputStream(filepath);
			OutputStreamWriter osw=new OutputStreamWriter(fos);
			BufferedWriter bw=new BufferedWriter(osw);
			bw.write(content);
			bw.flush();
			osw.flush();
			bw.close();
			osw.close();
			fos.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 读取文本内容
	 * @param name 文件路径
	 * @return
	 */
	public String readFromSd(String filename){
		try{
			FileInputStream fis=new FileInputStream(filename);
			InputStreamReader isr=new InputStreamReader(fis);
			BufferedReader br=new BufferedReader(isr);
			StringBuffer res=new StringBuffer();
			String x=null;
			while((x=br.readLine())!=null){
				res.append(x);
			}
			br.close();
			isr.close();
			fis.close();
			return res.toString();
		}catch (IOException e){
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 删除目录或者文件
	 * @param dir 文件路径或者目录路径
	 */
	public static void deleteFile(File dir) {
		if (dir != null && dir.isDirectory()) {
	        String[] children = dir.list();
	        for (int i = 0; i < children.length; i++) {
	        	deleteFile(new File(dir, children[i]));
	        }
	    }
    	dir.delete();
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
	/**
	 * 保存图片
	 * @param urlStr 图片的网络地址
	 * @param path 保存目录
	 * @param picName 图片名字
	 * @return 保存的文件对象;保存失败返回null
	 */
	public static File savePic(String picUrl,String path,String picName){
		try {
			URL url = new URL(picUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//连接超时时间
			conn.setConnectTimeout(20*1000);
			InputStream inputStream=conn.getInputStream();
			return savePic(inputStream, path, picName);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	/**
	 * 解压缩ZIP文件，将ZIP文件里的内容解压到targetDIR目录下.解压完之后会把压缩文件删除
	 * @param zipName	待解压缩的ZIP文件名
	 * @param targetBaseDirName	目标目录
	 */
    public static void upzipFile(String zipFileName, String targetBaseDirName){
		if (!targetBaseDirName.endsWith(File.separator)){
			targetBaseDirName += File.separator;
		}
        try {
        	//根据ZIP文件创建ZipFile对象
        	ZipFile zipFile = new ZipFile(zipFileName);
            ZipEntry entry = null;
            String entryName = null;
            String targetFileName = null;
            byte[] buffer = new byte[4096];
            int bytes_read; 
            //获取ZIP文件里所有的entry
            Enumeration<? extends ZipEntry> entrys = zipFile.entries();
            //遍历所有entry
            while (entrys.hasMoreElements()) {
            	entry = (ZipEntry)entrys.nextElement();
            	//获得entry的名字
            	entryName =  entry.getName();
            	targetFileName = targetBaseDirName + entryName;
            	if (entry.isDirectory()){
            		//  如果entry是一个目录，则创建目录
            		new File(targetFileName).mkdirs();
            		continue;
            	} else {
            		//	如果entry是一个文件，则创建父目录
            		new File(targetFileName).getParentFile().mkdirs();
            	}

            	//否则创建文件
            	File targetFile = new File(targetFileName);
            	//打开文件输出流
            	FileOutputStream os = new FileOutputStream(targetFile);
            	//从ZipFile对象中打开entry的输入流
            	InputStream  is = zipFile.getInputStream(entry);
            	while ((bytes_read = is.read(buffer)) != -1){
            		os.write(buffer, 0, bytes_read);
            	}
            	os.close( );
            	is.close( );
            }
        } catch (IOException err) {
        	err.printStackTrace();
        	File file = new File(zipFileName);
        	if(file.exists()){
        		file.delete();
        	}
        }finally{
        	File file = new File(zipFileName);
        	if(file.exists()){
        		file.delete();
        	}
        }
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
	/**
	 * 保存二进制文件
	 * @param url 网络地址
	 * @param path 保存目录
	 * @param fileName 保存文件名
	 * @return
	 */
	public static File saveFile(String urlStr,String path,String fileName){
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			//连接超时时间
			conn.setConnectTimeout(20*1000);
			InputStream inputStream=conn.getInputStream();
			if (inputStream == null) {
				return null;
			}
			File dir = new File(path);
			if (dir != null && !dir.exists()) {
				dir.mkdirs();
			}
			File file = new File(dir, fileName);
			FileOutputStream outputStream = null;
			try {
				outputStream = new FileOutputStream(file);
				byte[] bs = new byte[1024 * 8];
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
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	/**
	 * 获得某个目录的大小
	 * @param f
	 * @return
	 */
	public static long getDirectorySize(File f){
		if(f==null||!f.exists()||!f.isDirectory()){
			return 0;
		}
        long size = 0;
        File[] flist = f.listFiles();
        if(flist!=null){
        	for (int i = 0; i < flist.length; i++){
                if (flist[i].isDirectory()){
                    size = size + getDirectorySize(flist[i]);
                }else{
                    size = size + flist[i].length();
                }
            }
        }
        return size;
    }
}