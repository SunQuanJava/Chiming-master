package com.baizhi.baseapp.util;

import android.content.Context;

public class TextTransformer {
	 /**
	   * 判断一个字符是否为中文
	   * 
	   * @param character
	   * @return
	   */
	  public static boolean isChineseCharacter(Character character) {
          return (character >= 0x4e00) && (character <= 0x9fbb);
      }

    //  GENERAL_PUNCTUATION 判断中文的“号
    //  CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
    //  HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

	  /**
	   * 将px值转换为dip或dp值，保证尺寸大小不变
	   * 
	   * @param pxValue
	   * @return
	   */
	  public static int px2dip(Context context, float pxValue) {
	    final float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (pxValue / scale + 0.5f);
	  }
	
	  /**
	   * 将dip或dp值转换为px值，保证尺寸大小不变
	   * 
	   * @param dipValue
	   * @return
	   */
	  public static int dip2px(Context context, float dipValue) {
	    final float scale = context.getResources().getDisplayMetrics().density;
	    return (int) (dipValue * scale + 0.5f);
	  }
	
	  /**
	   * 将px值转换为sp值，保证文字大小不变
	   * 
	   * @param pxValue
	   * @return
	   */
	  public static int px2sp(Context context, float pxValue) {
	    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	    return (int) (pxValue / fontScale + 0.5f);
	  }
	
	  /**
	   * 将sp值转换为px值，保证文字大小不变
	   * 
	   * @param spValue
	   * @return
	   */
	  public static int sp2px(Context context, float spValue) {
	    final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
	    return (int) (spValue * fontScale + 0.5f);
	  }
	
	  public static int sp2px(float spVaule, float fontScale) {
	    return (int) (spVaule * fontScale + 0.5f);
	  }
	
}

