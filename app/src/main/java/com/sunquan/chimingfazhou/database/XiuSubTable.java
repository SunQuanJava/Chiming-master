package com.sunquan.chimingfazhou.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.baizhi.baseapp.database.AbstractTable;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.XiuInfo;
import com.sunquan.chimingfazhou.models.XiuSubInfo;

import java.util.ArrayList;

/**
 * 修二级dao层
 * <p/>
 * Created by Administrator on 2015/5/12.
 */
public class XiuSubTable extends AbstractTable {

    public static final String _ID = "_id";

    public static final String TABLE_NAME = "xiu_sub_info";

    public static final String XIU_TASK_ID = "task_id";
    public static final String XIU_TASK_NAME = "task_name";
    public static final String XIU_FATHER_TASK_ID = "father_task_id";
    public static final String XIU_FATHER_TASK_NAME = "father_task_name";
    public static final String XIU_LAST_PRACTICE_TIME = "last_practice_time";
    public static final String XIU_COUNT = "count";
    public static final String XIU_UID = "uid";

    private static final XiuSubTable instance = new XiuSubTable();

    private XiuSubTable() {
        super();
    }


    public static XiuSubTable getInstance() {
        return instance;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " integer primary key autoincrement, " +
                    XIU_TASK_ID + " text, " +
                    XIU_TASK_NAME + " text, " +
                    XIU_FATHER_TASK_ID + " text, " +
                    XIU_FATHER_TASK_NAME + " text, " +
                    XIU_LAST_PRACTICE_TIME + " text, " +
                    XIU_UID + " text, " +
                    XIU_COUNT + " text " + ")";
            if (db != null) {
                // Call this method to perform a SQL.
                db.execSQL(sql);
            }
        } catch (SQLException ignored) {
            ignored.printStackTrace();
        }
    }

    @Override
    public void deleteTable(SQLiteDatabase db) {
        if (db != null) {
            try {
                db.beginTransaction();
                db.delete(TABLE_NAME, null, null);
                db.endTransaction();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeDB(db);
            }
        }
    }

    /**
     * 获取所有二级修的数据
     *
     * @return
     */
    public synchronized ArrayList<XiuSubInfo> queryAllXiuSubInfos() {
        Cursor cursor = null;
        SQLiteDatabase db;
        final ArrayList<XiuSubInfo> xiuSubInfos = new ArrayList<>();
        try {
            db = getReadableDatabase();
            if (db == null) {
                return null;
            }
            final String mUid = GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid();
            cursor = db.query(TABLE_NAME, null, XIU_UID + " =? ", new String[]{mUid}, null, null, null);
            while (cursor.moveToNext()) {
                XiuSubInfo xiuSubInfo = fillXiuSubInfo(cursor);
                xiuSubInfos.add(xiuSubInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeCursor(cursor);
        }
        return xiuSubInfos;
    }


    /**
     * 获取所有的二级修的数据
     *
     * @param xiuInfo
     * @return
     */
    public synchronized ArrayList<XiuSubInfo> queryXiuSubInfosByXiuInfo(XiuInfo xiuInfo) {
        Cursor cursor = null;
        SQLiteDatabase db;
        final ArrayList<XiuSubInfo> xiuSubInfos = new ArrayList<>();
        try {
            db = getReadableDatabase();
            if (db == null) {
                return null;
            }
            final String mUid = GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid();
            cursor = db.query(TABLE_NAME, null, XIU_UID + " =? AND " + XIU_FATHER_TASK_ID + " =? ", new String[]{mUid, xiuInfo.getTask_id()}, null, null, null);
            while (cursor.moveToNext()) {
                XiuSubInfo xiuSubInfo = fillXiuSubInfo(cursor);
                xiuSubInfos.add(xiuSubInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeCursor(cursor);
        }
        return xiuSubInfos;
    }

    /**
     * 插入功课信息
     *
     * @param xiuSubInfos
     * @return
     */
    public synchronized boolean insertXiuSubInfos(ArrayList<XiuSubInfo> xiuSubInfos) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            for (XiuSubInfo xiuSubInfo : xiuSubInfos) {
                ContentValues cValues = fillContentValues(xiuSubInfo);
                db.insert(TABLE_NAME, null, cValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != db && db.isOpen()) {
                db.endTransaction();
            }
        }
        return true;
    }

    /**
     * 删除所有功课信息
     */
    public synchronized void deleteAllXiuSubInfos() {
        SQLiteDatabase db;
        try {
            final String mUid = GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid();
            db = getWritableDatabase();
            db.delete(TABLE_NAME, XIU_UID + " =? ", new String[]{mUid});
        } catch (Exception ignored) {
        }
    }

    /**
     * 插入修信息并删除旧数据
     *
     * @param xiuSubInfos
     * @return
     */
    public synchronized boolean insertXiuInfosWithDeleteFirst(ArrayList<XiuSubInfo> xiuSubInfos) {
        SQLiteDatabase db = null;
        try {
            final String mUid = GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid();
            db = getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_NAME, XIU_UID + " =? ", new String[]{mUid});
            for (XiuSubInfo xiuInfo : xiuSubInfos) {
                ContentValues cValues = fillContentValues(xiuInfo);
                db.insert(TABLE_NAME, null, cValues);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (null != db && db.isOpen()) {
                db.endTransaction();
            }
        }
        return true;
    }

    private XiuSubInfo fillXiuSubInfo(Cursor cursor) {
        final XiuSubInfo xiuSubInfo = new XiuSubInfo();
        xiuSubInfo.setLast_practice_time(cursor.getString(cursor.getColumnIndex(XIU_LAST_PRACTICE_TIME)));
        xiuSubInfo.setTask_id(cursor.getString(cursor.getColumnIndex(XIU_TASK_ID)));
        xiuSubInfo.setTask_name(cursor.getString(cursor.getColumnIndex(XIU_TASK_NAME)));
        xiuSubInfo.setParent_task_id(cursor.getString(cursor.getColumnIndex(XIU_FATHER_TASK_ID)));
        xiuSubInfo.setParent_task_name(cursor.getString(cursor.getColumnIndex(XIU_FATHER_TASK_NAME)));
        xiuSubInfo.setUid(cursor.getString(cursor.getColumnIndex(XIU_UID)));
        xiuSubInfo.setCount(cursor.getString(cursor.getColumnIndex(XIU_COUNT)));
        return xiuSubInfo;
    }

    private synchronized ContentValues fillContentValues(XiuSubInfo xiuSubInfo) {
        final ContentValues cValues = new ContentValues();
        cValues.put(XIU_LAST_PRACTICE_TIME, xiuSubInfo.getLast_practice_time());
        cValues.put(XIU_TASK_ID, xiuSubInfo.getTask_id());
        cValues.put(XIU_TASK_NAME, xiuSubInfo.getTask_name());
        cValues.put(XIU_FATHER_TASK_ID, xiuSubInfo.getParent_task_id());
        cValues.put(XIU_FATHER_TASK_NAME, xiuSubInfo.getParent_task_name());
        cValues.put(XIU_COUNT, xiuSubInfo.getCount());
        cValues.put(XIU_UID, GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid());
        return cValues;
    }
}
