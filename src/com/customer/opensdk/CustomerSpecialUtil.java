package com.customer.opensdk;

import android.content.Context;

/**
*TODO 客户专用功能处理工具类
*@author Arvin
*@Time 2018-6-20 下午13:24:02
*/
public class CustomerSpecialUtil {
	
	private final String TAG = "CustomerSpecialUtil";
	private static CustomerSpecialUtil mCustomerSpecialUtil;
	private static Context mContext = null;
	
	public static CustomerSpecialUtil getInstance(Context context) {
		mContext = context;
    	if (mCustomerSpecialUtil == null) {
    		mCustomerSpecialUtil = new CustomerSpecialUtil();
    	}
    	return mCustomerSpecialUtil;
    }
	
}
