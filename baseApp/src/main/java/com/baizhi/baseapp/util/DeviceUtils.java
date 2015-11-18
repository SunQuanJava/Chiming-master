package com.baizhi.baseapp.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import java.util.Locale;

public class DeviceUtils {

	public static final String DEVICE_TYPE = "Android";
	
	
	public static int getWindowWidth(Context ctx) {
		Display display = ((WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		return metrics.widthPixels;
	}


	public static int getWindowHeight(Context ctx) {

		Display display = ((WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);

		return metrics.heightPixels;
	}

	public static int getWindowDensity(Context ctx) {
		Display display = ((WindowManager) ctx
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		display.getMetrics(metrics);
		return metrics.densityDpi;
	}

	public static String getSimCard(Context ctx) {
		TelephonyManager mTelephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyManager.getSimSerialNumber();
	}

	public static int getCellId(Context ctx) {
		TelephonyManager mTelephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		GsmCellLocation g = (GsmCellLocation) mTelephonyManager
				.getCellLocation();
		return g == null ? -1: g.getCid();
	}
	
	public static String getIMSI(Context ctx){
		TelephonyManager mTelephonyManager = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		return mTelephonyManager.getSubscriberId();
	}
	
	public static String getLanguage(Context ctx){
		return Locale.getDefault().getLanguage();
	}
	
	 public static int getStatusBarHeight(Activity currActivity) {
	     Rect rect = new Rect();
	     currActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
	     return rect.top;

	   }
	
}
