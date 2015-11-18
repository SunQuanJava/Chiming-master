package com.baizhi.baseapp.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.view.MotionEvent;

public class MultiTouchUtils {
    private static Reflection   mReflection = new Reflection();

    private static Boolean mIsSupportMultiTouch;
    /**
     * 是否支持多点触控
     * @return
     */
    public static boolean isSupportMultiTouch( Context context ) {
        if( mIsSupportMultiTouch == null ) {
            final PackageManager pm = context.getPackageManager();

            String featureName = "android.hardware.touchscreen.multitouch";
            String methodName = "hasSystemFeature";
            try {
                mIsSupportMultiTouch = (Boolean) mReflection.invokeMethod( pm, methodName,
                        new Object[] { featureName } );
            }
            catch (Exception e) {
                e.printStackTrace();
                mIsSupportMultiTouch = false;
            }
            
        }
        return mIsSupportMultiTouch;
    }

    /**
     * 由于多点触控为Level 5 API提供，为了能够在Level 4下编译，所以使用反射
     * @author SinaDev
     *
     */
    public static class MotionEventUtils {
        private static final String TAG_ACTION_POINTER_DOWN = "ACTION_POINTER_DOWN";
        private static final String TAG_ACTION_POINTER_UP   = "ACTION_POINTER_UP";
        private static final String TAG_ACTION_MASK         = "ACTION_MASK";
        private static final String TAG_GET_X               = "getX";
        private static final String TAG_GET_Y               = "getY";

        public static final int     UNKNOWN_VALUE           = -1;

        public static int           ACTION_POINTER_DOWN;

        public static int           ACTION_POINTER_UP;

        public static int           ACTION_MASK;

        private static Reflection   mReflectionTool;

        // static block
        static {
            mReflectionTool = new Reflection();
            try {
                ACTION_POINTER_DOWN = (Integer) mReflectionTool.getStaticProperty(
                        MotionEvent.class.getName(), TAG_ACTION_POINTER_DOWN );
                ACTION_POINTER_UP = (Integer) mReflectionTool.getStaticProperty(
                        MotionEvent.class.getName(), TAG_ACTION_POINTER_UP );
                ACTION_MASK = (Integer) mReflectionTool.getStaticProperty(
                        MotionEvent.class.getName(), TAG_ACTION_MASK );
            }
            catch (Exception e) {
                e.printStackTrace();
                ACTION_POINTER_DOWN = UNKNOWN_VALUE;
                ACTION_POINTER_UP = UNKNOWN_VALUE;
                ACTION_MASK = 0x000000ff;
            }
        }

        public static float getX( MotionEvent event, int pointerIndex ) {
            Object ret = mReflectionTool.invokeMethod( event, TAG_GET_X, new Class[] { int.class },
                    new Object[] { pointerIndex } );
            if( ret != null ) {
                return (Float) ret;
            }

            return UNKNOWN_VALUE;
        }

        public static float getY( MotionEvent event, int pointerIndex ) {
            Object ret = (Float) mReflectionTool.invokeMethod( event, TAG_GET_Y, new Class[]{ int.class },
                    new Object[] { pointerIndex } );
            if( ret != null ) {
                return (Float) ret;
            }

            return UNKNOWN_VALUE;
        }
    }
}
