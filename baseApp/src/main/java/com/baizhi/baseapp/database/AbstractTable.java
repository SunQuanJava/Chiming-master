package com.baizhi.baseapp.database;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.baizhi.baseapp.application.BaseApplication;


/**
 *
 */
public abstract class AbstractTable {

    /** The DBHelper instance. */
    private BaseDBHelper mDBHelper = null;

    /**
     * The constructor method.
     */
    public AbstractTable() {
        Context context = BaseApplication.getAppContext();
        if (null != context) {
            mDBHelper = BaseDBHelper.getInstance(context);
        }
    }

    public abstract void createTable(SQLiteDatabase db);

    public abstract void deleteTable(SQLiteDatabase db);

    /**
     * Create and/or open a database that will be used for reading and writing.
     *
     * @return a read/write database object valid until
     * {@link #closeDB(android.database.sqlite.SQLiteDatabase)} is called.
     */
    public synchronized SQLiteDatabase getWritableDatabase() {
        return (null != mDBHelper) ? mDBHelper.getWritableDatabase() : null;
    }

    /**
     * Create and/or open a database.
     *
     * @return a database object valid until {@link #getWritableDatabase}
     * or {@link #closeDB(android.database.sqlite.SQLiteDatabase)} is called.
     */
    public synchronized SQLiteDatabase getReadableDatabase() {
        return (null != mDBHelper) ? mDBHelper.getReadableDatabase() : null;
    }

    /**
     * Close the data base if it is opened.
     *
     * @return true if succeeds, otherwise false.
     */
    public synchronized boolean closeDB(SQLiteDatabase db) {
        // In the whole life cycle of the application, we do not close the database.
        // When the application closes, we only close the database.
        /**
         if /null != db && db.isOpen()) {
            db.close();
         }
         */

        return true;
    }

    /**
     * Close the cursor, if it is open.
     *
     * @param cur to be closed cursor.
     */
    public synchronized void closeCursor(Cursor cur) {
        if (null != cur && !cur.isClosed()) {
            cur.close();
        }
    }

    /**
     * Execute a specified SQL.
     *
     * @param db       The database to be operated by SQL.
     * @param sql      The string of SQL.
     * @param bindArgs only byte[], String, Long and Double are supported in bindArgs.
     */
    public synchronized boolean execSQL(SQLiteDatabase db, String sql, Object[] bindArgs) {
        try {
            if (null == bindArgs) {
                db.execSQL(sql);
            } else {
                db.execSQL(sql, bindArgs);
            }

            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
