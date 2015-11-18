package com.avast.android.dialogs.util;

import java.util.HashSet;

/**
 * Created by Will on 2015/2/9.
 */
public class DialogTagHolder {
    public static HashSet<String> mTagHolder = new HashSet<String>();

    public static final void putTag(String tag) {
        mTagHolder.add(tag);
    }

    public static final void removeTag(String tag) {
        if (checkHasTag(tag)) {
            mTagHolder.remove(tag);
        }
    }

    public static final boolean checkHasTag(String tag) {
        return mTagHolder.contains(tag);
    }

    public static final void cleanDialogTags() {
        mTagHolder.clear();
    }
}
