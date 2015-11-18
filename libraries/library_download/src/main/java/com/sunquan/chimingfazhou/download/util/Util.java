package com.sunquan.chimingfazhou.download.util;

import java.text.DecimalFormat;

public class Util {
    public static final String NUMBER_FORMATE = "##";

    public static String formatNumber(double org) {
        DecimalFormat df = new DecimalFormat(NUMBER_FORMATE);
        return df.format(org);
    }


    public static String getPercentFormat(long one, long total) {
        final String fenzi = formatNumber(((double) one / (double) total * 100));
        if ("NaN".equals(fenzi)) {
            return "0%";
        } else {
            return formatNumber(((double) one / (double) total * 100)) + "%";
        }
    }

    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1fG", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0fM" : "%.1fM", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0fK" : "%.1fK", f);
        } else
            return String.format("%dB", size);
    }

}
