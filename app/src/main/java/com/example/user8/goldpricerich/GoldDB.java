package com.example.user8.goldpricerich;

import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GoldDB extends SQLiteOpenHelper {

    public static final String dbName = "dbMyGold";
    public static final String tblName = "Gold";
    public static final String colGoldPerGram = "goldpergram";
    public static final String colGramPur = "grampur";
    public static final String colTotal = "total";
    public static final String colID = "goldID";

    public GoldDB(Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query1 = String.format("CREATE TABLE " + tblName + "("
                + colID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + colGoldPerGram + "DOUBLE NOT NULL,"
                + colGramPur + " DOUBLE NOT NULL, "
                + colTotal + " DOUBLE NOT NULL" + ")");
        db.execSQL(query1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldversion, int newversion) {
        db.execSQL("DROP TABLE IF EXISTS expenses");
        onCreate(db);
    }

    public Cursor getDataById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT * FROM " + tblName + " WHERE " + colID + "= " + id, null);
        return cur;
    }

    public void fnExecuteSql(String strSql, Context appContext) {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            db.execSQL(strSql);
        } catch (Exception e) {
            Log.d("Unable to run query", "error");
        }
    }

    public int fnTotalRow() {
        int intRow;
        SQLiteDatabase db = this.getReadableDatabase();
        intRow = (int) DatabaseUtils.queryNumEntries(db, tblName);

        return intRow;
    }

}
