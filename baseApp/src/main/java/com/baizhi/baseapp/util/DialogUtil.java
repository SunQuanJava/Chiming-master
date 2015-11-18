package com.baizhi.baseapp.util;


import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baizhi.baseapp.R;


public class DialogUtil {

    public static Dialog createProgressDialog(Context context, String message) {
        View layout = LayoutInflater.from(context).inflate(R.layout.progress_toast_style, null);
        TextView textView = (TextView) layout.findViewById(R.id.tv_progressbar_toast_hint);
        textView.setText(message);

        Dialog progressDialog = new Dialog(context, R.style.TransparentDialog);
        progressDialog.setContentView(layout, new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return progressDialog;
    }
}
