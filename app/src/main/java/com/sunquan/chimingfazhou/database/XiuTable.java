package com.sunquan.chimingfazhou.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.baizhi.baseapp.database.AbstractTable;
import com.sunquan.chimingfazhou.application.MyApplication;
import com.sunquan.chimingfazhou.controller.GlobalDataHolder;
import com.sunquan.chimingfazhou.models.XiuInfo;

import java.util.ArrayList;

/**
 * 修dao层
 * <p/>
 * Created by Administrator on 2015/5/12.
 */
public class XiuTable extends AbstractTable {

    public static final String _ID = "_id";

    public static final String TABLE_NAME = "xiu_info";

    public static final String XIU_TASK_ID = "task_id";
    public static final String XIU_TASK_NAME = "task_name";
    public static final String XIU_UID = "uid";
    public static final String XIU_CREATE_DATE = "create_date";
    public static final String XIU_SHOW_TYPE = "show_type";

    private static final XiuTable instance = new XiuTable();

    private XiuTable() {
        super();
    }


    public static XiuTable getInstance() {
        return instance;
    }

    @Override
    public void createTable(SQLiteDatabase db) {
        try {
            String sql = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                    _ID + " integer primary key autoincrement, " +
                    XIU_TASK_ID + " text, " +
                    XIU_TASK_NAME + " text, " +
                    XIU_CREATE_DATE + " text, " +
                    XIU_SHOW_TYPE + " text, " +
                    XIU_UID + " text " + ")";
            if (db != null) {
                // Call this method to perform a SQL.
                db.execSQL(sql);
            }
        } catch (SQLException ignored) {
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
     * 查找到当前用户的所有功课记录
     *
     * @return
     */
    public synchronized ArrayList<XiuInfo> queryAllXiuInfos() {
        Cursor cursor = null;
        SQLiteDatabase db;
        final ArrayList<XiuInfo> xiuInfos = new ArrayList<>();
        try {
            db = getReadableDatabase();
            if (db == null) {
                return null;
            }
            final String mUid = GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid();
            cursor = db.query(TABLE_NAME, null, XIU_UID + " =? ", new String[]{mUid}, null, null, null);
            while (cursor.moveToNext()) {
                XiuInfo xiuInfo = fillXiuInfo(cursor);
                xiuInfos.add(xiuInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeCursor(cursor);
        }
        return xiuInfos;
    }

    /**
     * 插入功课信息
     *
     * @param xiuInfos
     * @return
     */
    public synchronized boolean insertXiuInfos(ArrayList<XiuInfo> xiuInfos) {
        SQLiteDatabase db = null;
        try {
            db = getWritableDatabase();
            db.beginTransaction();
            for (XiuInfo xiuInfo : xiuInfos) {
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

    /**
     * 删除所有功课信息
     */
    public synchronized void deleteAllXiuInfos() {
        SQLiteDatabase db;
        try {
            final String mUid = GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid();
            db = getWritableDatabase();
            db.delete(TABLE_NAME, XIU_UID + " =? ", new String[]{mUid});
        } catch (Exception ignored) {
        }
    }

    /**
     * 插入修信息并删除久数据
     *
     * @param xiuInfos
     * @return
     */
    public synchronized boolean insertXiuInfosWithDeleteFirst(ArrayList<XiuInfo> xiuInfos) {
        SQLiteDatabase db = null;
        try {
            final String mUid = GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid();
            db = getWritableDatabase();
            db.beginTransaction();
            db.delete(TABLE_NAME, XIU_UID + " =? ", new String[]{mUid});
            for (XiuInfo xiuInfo : xiuInfos) {
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

    private XiuInfo fillXiuInfo(Cursor cursor) {
        final XiuInfo xiuInfo = new XiuInfo();
        xiuInfo.setCreate_time(cursor.getString(cursor.getColumnIndex(XIU_CREATE_DATE)));
        xiuInfo.setTask_id(cursor.getString(cursor.getColumnIndex(XIU_TASK_ID)));
        xiuInfo.setTask_name(cursor.getString(cursor.getColumnIndex(XIU_TASK_NAME)));
        xiuInfo.setUid(GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid());
        xiuInfo.setShowType(Integer.valueOf(cursor.getString(cursor.getColumnIndex(XIU_SHOW_TYPE))));
        return xiuInfo;
    }

    private synchronized ContentValues fillContentValues(XiuInfo xiuInfo) {
        final ContentValues cValues = new ContentValues();
        cValues.put(XIU_CREATE_DATE, xiuInfo.getCreate_time());
        cValues.put(XIU_TASK_ID, xiuInfo.getTask_id());
        cValues.put(XIU_TASK_NAME, xiuInfo.getTask_name());
        cValues.put(XIU_UID, GlobalDataHolder.getInstance(MyApplication.getAppContext()).getUid());
        cValues.put(XIU_SHOW_TYPE, String.valueOf(xiuInfo.getShowType()));
        return cValues;
    }

}
