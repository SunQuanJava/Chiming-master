package com.sunquan.chimingfazhou.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.baizhi.baseapp.database.BaseDBHelper;

/**
 * Created by sunquan on 2015/1/4.
 */
public class DBHelper extends BaseDBHelper {

    /**
     * Create a helper object to create, open, and/or manage a database. The
     * database is not actually created or opened until one of
     * {@link #getWritableDatabase} or {@link #getReadableDatabase} is called.
     *
     * @param context to use to open or create the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is
     *                older, {@link #onUpgrade} will be used to upgrade the database
     */
    protected DBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    protected void createTable(SQLiteDatabase db) {
        XiuSubTable.getInstance().createTable(db);
        XiuTable.getInstance().createTable(db);
    }

    @Override
    protected String[] getUpdateTables() {
        return new String[]{XiuTable.TABLE_NAME, XiuSubTable.TABLE_NAME};
    }


}
