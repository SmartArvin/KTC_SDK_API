package com.ktc.opensdk;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.mstar.android.tvapi.common.TvManager;
import com.mstar.android.tvapi.common.exception.TvCommonException;
import com.mstar.android.tvapi.factory.FactoryManager;


/**
*TODO KTC工厂相关功能实现
*@author Arvin
*@Time 2018-1-10 上午09:02:10
*/
public class KtcFactoryUtil {
	
	private static final String TAG = "KtcFactoryUtil";
	private static KtcFactoryUtil mKtcFactoryUtil ;
	private static KtcLogerUtil mKtcLogerUtil ;
	private static Context mContext = null;
	
	public static KtcFactoryUtil getInstance(Context context) {
		mContext = context;
    	if (mKtcFactoryUtil == null) {
    		mKtcFactoryUtil = new KtcFactoryUtil();
    	}
    	return mKtcFactoryUtil;
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
	
	public void factoryReset(){
		new factoryResetTask().execute();
	}

	class factoryResetTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			// TODO Auto-generated method stub
			FactoryRestordefalut();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		}
	}
	
	 private void FactoryRestordefalut() {
        restoreFactoryAtvProgramTable((short) 0);
    	restoreToDefault();
    }
	 
	 private void restoreFactoryAtvProgramTable(short cityIndex) {
		getKtcLogerUtil().I(TAG, "----restoreFactoryAtvProgramTable----");
		try {
			FactoryManager fm = TvManager.getInstance().getFactoryManager();
			fm.restoreFactoryAtvProgramTable(cityIndex);
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	 private boolean restoreToDefault() {

			boolean ret = true;
			boolean result = false;
			try {
				TvManager.getInstance().setTvosCommonCommand("SetFactoryResetStatus");
			} catch (TvCommonException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			File srcFile = new File("/tvdatabase/DatabaseBackup/",
					"user_setting.db");
			File destFile = new File("/tvdatabase/Database/", "user_setting.db");
			result = copyFile(srcFile, destFile);
			result = false;
			if (result == false) {
				ret = false;
			}

			srcFile = new File("/tvdatabase/DatabaseBackup/", "factory.db");
			destFile = new File("/tvdatabase/Database/", "factory.db");
			result = copyFile(srcFile, destFile);
			result = false;

			try {
				Runtime.getRuntime().exec("sync");
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				TvManager.getInstance().setTvosCommonCommand(
						"RestoreFactoryResetStatus");
			} catch (TvCommonException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			if (result == false) {
				ret = false;
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			mContext.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
			getKtcLogerUtil().I(TAG, "restoreToDefault:  "+ret);
			return ret;

		}
	 
	 
	 /**
	 * TODO 预制工厂频道
	 * @param 
	 * @return void
	 */
	public void resetChannel() {
		getKtcLogerUtil().I(TAG, "---resetChannel---");
		try{
			if (hasDtmb()) {
				TvManager.getInstance().setTvosCommonCommand("SetResetATVDTVChannel");
			} else {
				TvManager.getInstance().setTvosCommonCommand("SetResetATVChannel");
			}
		} catch (TvCommonException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	private boolean hasDtmb(){
		boolean hasDtmb = false;
		Properties props = new Properties();
		InputStream in;
		try {
			in = new BufferedInputStream(new FileInputStream("/system/build.prop"));
			props.load(in);
			String value = props.getProperty("ktc.dtmb");
			hasDtmb = value != null && value.equals("true");
		} catch (Exception e) {
			hasDtmb = false;		
		}
		getKtcLogerUtil().I(TAG, "hasDtmb:  "+hasDtmb);
		return hasDtmb;
	}
	 

    /**
     * Copy data from a source stream to destFile.
     * Return true if succeed, return false if failed.
     */
    private  boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            if (destFile.exists()) {
                destFile.delete();
            }
            FileOutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    Log.d(" out.write(buffer, 0, bytesRead);", " out.write(buffer, 0, bytesRead);");
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.flush();
                try {
                    out.getFD().sync();
                } catch (IOException e) {
                }
                out.close();
            }
            return true;
        } catch (IOException e) {
            Log.d("copyToFile(InputStream inputStream, File destFile)", e.getMessage());
            return false;
        }
    }
    
    // copy a file from srcFile to destFile, return true if succeed, return
    // false if fail
    private  boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally  {
                in.close();
            }
        } catch (IOException e) {
            Log.d("copyFile(File srcFile, File destFile)", e.getMessage());
            result = false;
        }
        chmodFile(destFile);
        return result;
    }
    
    private void chmodFile(File destFile){
        try {
            String command = "chmod 666 " + destFile.getAbsolutePath();
            getKtcLogerUtil().I(TAG, "chmodFile:  "+command);
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec(command);
           } catch (IOException e) {
            getKtcLogerUtil().I(TAG, "chmodFile:  chmod fail!!!!");
            e.printStackTrace();
           }
    }
}
