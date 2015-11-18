package com.baizhi.baseapp.widget;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

import com.baizhi.baseapp.R;
import com.baizhi.baseapp.util.DeviceUtils;

import java.lang.ref.WeakReference;

/**
 * 封装Toast的简易实现
 * 
 * @author 13leaf
 * 
 */
public class WToast {

	private Toast mToast;
	private final WeakReference<Context> context;

	public WToast(final Context context) {
		this.context = new WeakReference<>(context);
	}

	/**
	 * 在底部显示一条toast信息,大约3秒钟时间。<br>
	 * 若想让toast显示时间较长，请调用showLongMessage
	 * 
	 * @param msg
	 */
	public void showMessage(final Object msg) {
		if (context.get() != null) {
			showToast(context.get(), msg);
		}
	}

	private void showToast(Context context, Object msg) {
		if (mToast == null) {
            mToast = new Toast(context);
            final int offSet = (int) (DeviceUtils.getWindowHeight(context)*((float)1/3));
            mToast.setGravity(Gravity.CENTER,0, offSet);
		}
        final TextView textView = new TextView(context);
        if(msg instanceof String) {
            textView.setText((String)msg);
        }
        else if(msg instanceof Integer) {
            textView.setText((Integer) msg);
        }
        else {
            throw new IllegalArgumentException("The type of the parameter is not valid");
        }
        textView.setGravity(Gravity.CENTER);
        textView.setBackgroundResource(R.drawable.toast_style);
        textView.setTextColor(Color.WHITE);
        textView.getBackground().setAlpha(205);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,context.getResources().getDimensionPixelSize(R.dimen.toast_text_size));
        final int padding = context.getResources().getDimensionPixelSize(R.dimen.toast_padding);
        textView.setPadding(padding,0,padding,0);
        textView.setMinimumHeight(context.getResources().getDimensionPixelSize(R.dimen.toast_height));
        textView.setMinimumWidth(context.getResources().getDimensionPixelSize(R.dimen.toast_width));
        mToast.setView(textView);
        mToast.setDuration(Toast.LENGTH_SHORT);
		mToast.show();
	}

	/**
	 * 仅在debug模式下显示toast消息
	 * 
	 * @param msg
	 */
	public void testShowMessage(final Object msg) {
		if (context.get() != null) {
			boolean isDebug = (context.get().getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
			if (isDebug) {
				showMessage(msg);
			}
		}
	}
}
