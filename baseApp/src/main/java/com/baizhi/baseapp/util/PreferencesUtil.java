package com.baizhi.baseapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferencesUtil {
	public static final String CHIMINGAZHOU ="app";
	
	
	public static String getStringByName(Context context, String name,
			String default_value) {

		return PreferenceManager.getDefaultSharedPreferences(context)
				.getString(name, default_value);

	}

	public static int getIntByName(Context context, String name,
			int default_value) {

		return PreferenceManager.getDefaultSharedPreferences(context).getInt(
				name, default_value);

	}

	public static long getLongByName(Context context, String name,
			long default_value) {

		return PreferenceManager.getDefaultSharedPreferences(context).getLong(
				name, default_value);

	}

	public static boolean getBooleanByName(Context context, String name,
			boolean default_value) {

		return PreferenceManager.getDefaultSharedPreferences(context)
				.getBoolean(name, default_value);

	}

	public static void setStringByName(Context context, String name,
			String value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putString(name, value);
		editor.commit();
	}

	public static void setBooleanByName(Context context, String name,
			Boolean value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putBoolean(name, value);
		editor.commit();
	}

	public static void setIntByName(Context context, String name, int value) {
		SharedPreferences sp =  PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putInt(name, value);
		editor.commit();
	}

	public static void setLongByName(Context context, String name, long value) {
		SharedPreferences sp = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = sp.edit();
		editor.putLong(name, value);
		editor.commit();
	}

	public static void clearValues(Context context) {
		SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);
		Editor editor = pref.edit();
		editor.clear();
		editor.commit();
	}
	
}
