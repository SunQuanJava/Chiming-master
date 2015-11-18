/**
 *
 */
package com.baizhi.baseapp.controller;

import android.os.Handler;
import android.os.Looper;

/**
 * @author sunquan
 */
public class BaseHandler extends Handler {

    public BaseHandler() {
    }

    public BaseHandler(Callback callback) {
        super(callback);
    }

    public BaseHandler(Looper looper) {
        super(looper);
    }

    public BaseHandler(Looper looper, Callback callback) {
        super(looper, callback);
    }

    private String id;


    public String getId() {
        return id;
    }


    public void setId(String id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BaseHandler) {
            if (((BaseHandler) obj).id != null
                    && ((BaseHandler) obj).id.equals(this.id)) {
                return true;
            }
        }
        return false;
    }
}
