package com.avast.android.dialogs.trick;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.avast.android.dialogs.R;

import java.lang.reflect.Field;

/**
 * Created by Will on 2015/2/8.
 */
public class TrickyTimePicker extends TimePicker {

    public TrickyTimePicker(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            return;
        }
        
        Class<?> internalRID = null;
        try {
            internalRID = Class.forName("com.android.internal.R$id");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field hour = null;
        try {
            hour = internalRID.getField("hour");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        NumberPicker npHour = null;
        try {
            npHour = (NumberPicker) findViewById(hour.getInt(null));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Field minute = null;
        try {
            minute = internalRID.getField("minute");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        NumberPicker npMinute = null;
        try {
            npMinute = (NumberPicker) findViewById(minute.getInt(null));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


        Class<?> numberPickerClass = null;
        try {
            numberPickerClass = Class.forName("android.widget.NumberPicker");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Field selectionDivider = null;
        try {
            selectionDivider = numberPickerClass.getDeclaredField("mSelectionDivider");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            selectionDivider.setAccessible(true);
            selectionDivider.set(npHour, getResources().getDrawable(
                    R.drawable.np_numberpicker_selection_divider_green));
            selectionDivider.set(npMinute, getResources().getDrawable(
                    R.drawable.np_numberpicker_selection_divider_green));
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
