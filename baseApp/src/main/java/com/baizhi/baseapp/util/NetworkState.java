package com.baizhi.baseapp.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkState {

	public NetworkState(Context context) {
		
	}

	public static boolean isActiveNetworkConnected(Context ctx) {
		NetworkInfo info = ((ConnectivityManager) ctx
		.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if (info != null) {
			if (info.getState() == NetworkInfo.State.CONNECTED)
				return true;
		}
		return false;
	}

	public static boolean isMobileNetworkConnected(Context ctx) {
		NetworkInfo info =((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		if (info != null) {
			if (info.getState() == NetworkInfo.State.CONNECTED)
				return true;
		}
		return false;
	}

	public static boolean isWifiNetworkConnected(Context ctx) {
		NetworkInfo info = ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (info != null) {
			if (info.getState() == NetworkInfo.State.CONNECTED)
				return true;
		}
		return false;
	}
	
	public static void isCTNetWorkConnected(Context ctx){
		
		ConnectivityManager	mConnectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		if(info != null){
			String nettype = info.getTypeName().toUpperCase();
			if(nettype.indexOf("WIFI") < 0){
				nettype = (mConnectivityManager.getAllNetworkInfo()[0].getSubtypeName()!=null ? 
						mConnectivityManager.getAllNetworkInfo()[0].getSubtypeName().toUpperCase() : nettype);
			}
		}
	}
}
