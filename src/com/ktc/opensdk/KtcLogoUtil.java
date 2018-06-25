package com.ktc.opensdk;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import android.content.Context;
import android.os.SystemProperties;
import com.mstar.android.tvapi.common.TvManager;

/**
*TODO KTC开机logo及开机动画替换、还原
*@author Arvin
*@Time 2018-1-9 下午19:21:30
*/
public class KtcLogoUtil {
	
	private static final String TAG = "KtcLogoUtil";
	private static KtcLogoUtil mKtcLogoUtil;
	private static KtcSystemUtil mKtcSystemUtil ;
	private static KtcLogerUtil mKtcLogerUtil;
	private static Context mContext = null;
	
	private long limitSize = 500*1024 ;//logo file limit(500KB)
	private long limitVideoSize = 20480*1024 ;//boot video limit(20M)
	
	public static KtcLogoUtil getInstance(Context context) {
		mContext = context;
    	if (mKtcLogoUtil == null) {
    		mKtcLogoUtil = new KtcLogoUtil();
    	}
    	return mKtcLogoUtil;
    }
	
	public static KtcSystemUtil getKtcSystemUtil() {
    	if (mKtcSystemUtil == null) {
    		mKtcSystemUtil = new KtcSystemUtil();
    	}
    	return mKtcSystemUtil;
    }
	
	/**
	 * TODO 获取KtcLogerUtil实例对象
	 * @param null
	 * @return KtcLogerUtil
	 */
	private static KtcLogerUtil getKtcLogerUtil() {
    	if (mKtcLogerUtil == null) {
    		mKtcLogerUtil = new KtcLogerUtil();
    	}
    	return mKtcLogerUtil;
    }
	
	/**
	 * TODO 替换开机logo
	 * @param null
	 * @return boolean
	 */
	public boolean changeBootLogo(){
		return changeBootLogo(null);
	}
	
	/**
	 * TODO 替换开机logo
	 * @param String logoPath
	 * @return boolean
	 */
	public boolean changeBootLogo(String logoPath) {
		getKtcLogerUtil().I(TAG, "changeBootLogo:  "+logoPath);
		
		boolean ret = false;
		String destPath = new String();
		String defaultFilePath = new String();
		
		File srcFile;
		File destFile;
		File defaultFile;
		
		destPath = "/tvconfig/boot0.jpg";
		defaultFilePath="/tvcustomer/Customer/boot.jpg";
		
		if(logoPath == null || logoPath == ""){
			logoPath = getKtcSystemUtil().getFirstUsbPath()+"boot0.jpg";
		}
		
		srcFile = new File(logoPath);
		destFile = new File(destPath);
		defaultFile = new File(defaultFilePath);
		
		if(!srcFile.exists()){
			getKtcLogerUtil().I(TAG, "Logo file not exit!");
			return false;
		}else if(!getExtensionName(logoPath).equals("jpg")){
			getKtcLogerUtil().I(TAG, "Logo file format Illegal!");
			return false;
		}
		
		if(srcFile.length() > limitSize){
			getKtcLogerUtil().I(TAG, "Logo file too large");
			return false;
		}else {
			try {
				if(!defaultFile.exists()){
					copyFile(destFile, defaultFile);
				}
				
				ret = copyFile(srcFile, destFile);
				
				chmodFile(destFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (ret){
			getKtcLogerUtil().I(TAG, "Logo change success!!!");
			try{
				TvManager.getInstance().setEnvironment("db_table", "0");
			}catch (Exception e){
		    }
		}else{
			getKtcLogerUtil().I(TAG, "Logo change fail!!!");
		}
		return ret ;
	}
	
	/**
	 * TODO 还原开机logo
	 * @param 
	 * @return boolean
	 */
	public boolean restoreBootLogo(){
		getKtcLogerUtil().I(TAG, "---restoreBootLogo---");
		
		String destPath = new String();
		String defaultFilePath = new String();
		
		File destFile;
		File defaultFile;
		
		destPath = "/tvconfig/boot0.jpg";
		defaultFilePath="/tvcustomer/Customer/boot.jpg";
		getKtcLogerUtil().I(TAG , "....1....destPath is " + destPath + "..........");
		
		destFile = new File(destPath);
		defaultFile = new File(defaultFilePath);
		
		if(!defaultFile.exists()){
			return false;
		}
		
		try {
			copyFile(defaultFile, destFile);
			chmodFile(destFile);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		
		try{
			TvManager.getInstance().setEnvironment("db_table", "0");
		}catch (Exception e){
			return false;
		}
		
		return true;
	}
	
	
	/**
	 * TODO 更换开机动画
	 * @param null
	 * @return void
	 */
	public void changeBootAnim() {
		File file = new File("/system/media/bootanimation.zip");
		if(file.exists()){
			try {
				File file_bk = new File("/system/media/bootanimation1.zip");
				if(file_bk == null || !file_bk.exists()){
					backupBootAnim();
				}
				new Thread().sleep(3500);
				SystemProperties.set("ctl.start", "cp_bootanim");
			} catch (Exception e) {
				e.printStackTrace();
				getKtcLogerUtil().I(TAG, "BootAnim change fail!!!");
			}
		}else {
			getKtcLogerUtil().I(TAG, "BootAnim change fail!!!");
		}
    }
	
	/**
	 * TODO 更换指定path的开机动画
	 * @param null
	 * @return void
	 */
	public void changeBootAnim(String animPath) {
		File file = new File("/system/media/bootanimation.zip");
		if(file.exists()){
			try {
				File file_bk = new File("/system/media/bootanimation1.zip");
				if(file_bk == null || !file_bk.exists()){
					backupBootAnim();
				}
				new Thread().sleep(3500);
				SystemProperties.set("ctl.start", "cp_bootanim");
			} catch (Exception e) {
				e.printStackTrace();
				getKtcLogerUtil().I(TAG, "BootAnim change fail!!!");
			}
		}else {
			getKtcLogerUtil().I(TAG, "BootAnim change fail!!!");
		}
    }
	
	/**
	 * TODO 备份开机动画
	 * @param null
	 * @return void
	 */
	private void backupBootAnim() {
		getKtcLogerUtil().I(TAG, "---backupBootAnim---");
		
		File file = new File("/system/media/bootanimation1.zip");
		if(!file.exists()){
			try {
				SystemProperties.set("ctl.start", "cp_bootfirstanim");
			} catch (Exception e) {
				e.printStackTrace();
				getKtcLogerUtil().I(TAG, "BootAnim backup fail!!!");
			}
		}
    }
	
	/**
	 * TODO 还原开机动画
	 * @param null
	 * @return void
	 */
	public void restoreBootanimation() {
		getKtcLogerUtil().I(TAG, "---restoreBootanimation---");
		
		File file = new File("/system/media/bootanimation1.zip");
		if(file.exists()){
			try {
				SystemProperties.set("ctl.start", "cp_bootad_hotel");
			} catch (Exception e) {
				e.printStackTrace();
				getKtcLogerUtil().I(TAG, "BootAnim restore fail!!!");
			}
		}else{
			getKtcLogerUtil().I(TAG, "BootAnim_bk not exit!!!");
		}
	 }
	
	/**
	 * TODO 从任意路径更换开机视频
	 * @param String videoPath
	 * @return boolean
	 */
	public boolean changeBootVideo(String videoPath) {
		getKtcLogerUtil().I(TAG, "changeBootVideo:  "+videoPath);
		
		if(videoPath == null || videoPath == ""){
			getKtcLogerUtil().I(TAG, "-------videoPath == null-------");
			return false;
		}
		
		try{
			File file = new File("/system/media/video1.ts");
			if(file == null || !file.exists()){
				SystemProperties.set("ctl.start", "cp_bootfirstad");
			}
			new Thread().sleep(3500);
			SystemProperties.set("ctl.start", "cp_bootad");
			return true;
		}catch(Exception e){
			e.printStackTrace();
			getKtcLogerUtil().I(TAG , "changeBootVideo  fail!!!");
			return false;
		}
	}
	
	/**
	 * TODO 还原开机视频
	 * @param null
	 * @return void
	 */
	public void restoreBootVideo() {
		getKtcLogerUtil().I(TAG, "---restoreBootVideo---");
		 SystemProperties.set("ctl.start", "cp_restorebootad");
	}
    
	private String getExtensionName(String filename) {  
	    if ((filename != null) && (filename.length() > 0)) {  
	        int dot = filename.lastIndexOf('.');  
	        if ((dot >-1) && (dot < (filename.length() - 1))) {  
	            return filename.substring(dot + 1);  
	        }  
	    }  
	    return filename;  
	}  
	
	private static void chmodFile(File destFile) {
      try {
           String command = "chmod 644 " + destFile.getAbsolutePath();
           Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
        } catch (IOException e) {
               e.printStackTrace();
          }
      }
	
	private static boolean copyFile(File srcFile, File destFile) throws IOException {
		if (!srcFile.exists()) {
			getKtcLogerUtil().I(TAG, "~src file not exits~");
			return false;
		}
		
		if (!srcFile.isFile()) {
			getKtcLogerUtil().I(TAG, "~src file is not a file~");
			return false;
		}
		if (!srcFile.canRead()) {
			getKtcLogerUtil().I(TAG, "~src file can  not read~");
			return false;
		}

		if (!destFile.exists()) {
			getKtcLogerUtil().I(TAG, "~dest file not exits~");
			if (!destFile.getParentFile().exists()) {
				destFile.getParentFile().mkdirs();
			}
			destFile.createNewFile();
		}
		if (!destFile.canRead()) {
			getKtcLogerUtil().I(TAG, "~dest file can  not read~");
			return false;
		}

		getKtcLogerUtil().I(TAG, "~src file OK~");
		FileInputStream input = new FileInputStream(srcFile);
		BufferedInputStream inBuff = new BufferedInputStream(input);
		FileOutputStream output = new FileOutputStream(destFile);
		BufferedOutputStream outBuff = new BufferedOutputStream(output);
		byte[] b = new byte[1024 * 2000];
		int len;
		while ((len = inBuff.read(b)) != -1) {
			outBuff.write(b, 0, len);
		}
		outBuff.flush();
		inBuff.close();
		outBuff.close();
		output.close();
		input.close();
		getKtcLogerUtil().I(TAG, "~chmod dest file OK~");
		return true;
	}
	
}
