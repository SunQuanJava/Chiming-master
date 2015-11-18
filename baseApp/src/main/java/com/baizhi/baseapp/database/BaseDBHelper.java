package com.baizhi.baseapp.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sunquan on 2015/1/4.
 */
public class BaseDBHelper extends SQLiteOpenHelper {
    /** The database name. */
    public final static String DATABASE_NAME = "chimingfazhou.db";

    /** The database version. {}*/
    public final static int DATABASE_VERSION = 1;

    /** The lock object for locking the lasy loading instance. */
    private static final Object mLasyLoadingLock = new Object();

    /** The instance of DBHelper class. */
    private static volatile BaseDBHelper mDBHelper = null;

    /** The database instance. */
    private SQLiteDatabase mDBInstance = null;

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
    protected BaseDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Call this method to create a database helper instance.
     *
     * @param context to use to open or create the database.
     * @return The instance of the DBHelper class.
     */
    public static BaseDBHelper getInstance(Context context) {
        synchronized (mLasyLoadingLock) {
            // If the instance is null, allocate memory for it.
            if (null == mDBHelper) {
                mDBHelper = new BaseDBHelper(
                        context,
                        DATABASE_NAME,
                        null,
                        DATABASE_VERSION);
//
//                // This calling will lead to create a actually database.
//                mDBHelper.getWritableDatabase();
            }

            return mDBHelper;
        }
    }

    /**
     * Called when the database is created for the first time. This is where the
     * creation of tables and the initial population of the tables should
     * happen.
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            createTable(db);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void createTable(SQLiteDatabase db){}

    /**
     * Called when the database needs to be upgraded. The implementation should
     * use this method to drop tables, add tables, or do anything else it needs
     * to upgrade to the new schema version.
     * <p/>
     * <p/>
     * The SQLite ALTER TABLE documentation can be found <a
     * href="http://sqlite.org/lang_altertable.html">here</a>. If you add new
     * columns you can use ALTER TABLE to insert them into a live table. If you
     * rename or remove columns you can use ALTER TABLE to rename the old table,
     * then create the new table and then populate the new table with the
     * contents of the old table.
     *
     * @param db         The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion) {
            try {
                final String[] tableNames = getUpdateTables();
                db.beginTransaction();

                for(String table:tableNames) {
                    db.execSQL("DROP TABLE IF EXISTS " + table);
                }
                db.setTransactionSuccessful();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.endTransaction();
            }
        }

        onCreate(db);
    }

    protected String[] getUpdateTables(){
        return null;
    }

    /**
     * Called when the database has been opened. The implementation should check
     * {@link android.database.sqlite.SQLiteDatabase#isReadOnly} before updating the database.
     *
     * @param db The database.
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

        if (db.isReadOnly()) {
            db.execSQL("PRAGMA foreign_keys = ON;");
            db.execSQL("PRAGMA default_cache_size = 10000;");
        }
    }

    /**
     * Create and/or open a database that will be used for reading and writing.
     * The first time this is called, the database will be opened and
     * {@link #onCreate}, {@link #onUpgrade} and/or {@link #onOpen} will be
     * called.
     */
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (null == mDBInstance) {
            mDBInstance = super.getWritableDatabase();
        }

        return mDBInstance;
    }

    /**
     * Create and/or open a database.  This will be the same object returned by
     * {@link #getWritableDatabase} unless some problem, such as a full disk,
     * requires the database to be opened read-only.  In that case, a read-only
     * database object will be returned.  If the problem is fixed, a future call
     * to {@link #getWritableDatabase} may succeed, in which case the read-only
     * database object will be closed and the read/write object will be returned
     * in the future.
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        //return super.getReadableDatabase();

        // Always return the writable database.
        return getWritableDatabase();
    }

    /**
     * Close the database.
     */
    @Override
    public synchronized void close() {
        super.close();

        final SQLiteDatabase db = mDBInstance;

        if (null != db) {
            if (db.isOpen()) {
                db.close();
            }
        }

        mDBInstance = null;
    }
}
