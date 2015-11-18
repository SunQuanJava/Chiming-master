package com.sunquan.chimingfazhou.util;

import java.util.regex.Pattern;

/**
 * 判断工具
 * Created by czh on 2015/6/8.
 */
public class MobileJudge {
    /**判断是否是电话号码*/
    public static boolean isMobileNO(String mobiles) {
        /*
		 * 移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
		 * 联通：130、131、132、152、155、156、185、186 电信：133、153、180、189、（1349卫通）
		 * 总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
		 */
        String telRegex = "[1][358]\\d{9}";// "[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (mobiles.isEmpty())
            return false;
        else
            return mobiles.matches(telRegex);
    }
    /**判断字符串中是否全是数字*/
    public static boolean  isNumeric(String str){
        final Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }
}
