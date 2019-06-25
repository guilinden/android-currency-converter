package br.feevale.conversordemoedas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class DataBaseStructure {

    private Context ctx;
    public static final String DATABASE_NAME = "trabalho2.db";
    public static final Integer DATABASE_VERSION = 4;
    private SQLiteDatabase db;
    private DatabaseHelper dbHelper;

    public DataBaseStructure(Context ctx){
        this.ctx = ctx;
        dbHelper = new DatabaseHelper();
        db = dbHelper.getWritableDatabase();
    }

    public static class RateTable implements BaseColumns {
        public static final String TABLE_NAME = "rates";
        public static final String COLUMN_BASE_CURRENCY = "baseCurrency";
        public static final String COLUMN_TO_CURRENCY = "toCurrency";
        public static final String COLUMN_RATE_VALUE = "rateValue";
        public static final String COLUMN_DATE = "date";

        public static String getSQL(){
            String sql = "CREATE TABLE " + TABLE_NAME + " ("+
                    _ID                  + " INTEGER PRIMARY KEY, " +
                    COLUMN_BASE_CURRENCY        + " TEXT, " +
                    COLUMN_TO_CURRENCY        + " TEXT, " +
                    COLUMN_RATE_VALUE        + " TEXT, " +
                    COLUMN_DATE        + " TEXT " +
                    ")";
            return sql;
        }
    }

    public Rate getRate(Long id){
        String cols[] = {RateTable._ID, RateTable.COLUMN_BASE_CURRENCY, RateTable.COLUMN_TO_CURRENCY, RateTable.COLUMN_RATE_VALUE,RateTable.COLUMN_DATE};
        String args[] = {id.toString()};
        Cursor cursor = db.query(RateTable.TABLE_NAME, cols, RateTable._ID+"=?", args, null, null, RateTable._ID);


        if(cursor.getCount() == 0){
            return null;
        }

        cursor.moveToNext();
        Rate c = new Rate();
        c.setId(cursor.getLong(cursor.getColumnIndex(RateTable._ID)));
        c.setBaseCurrency(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_BASE_CURRENCY)));
        c.setToCurrency(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_TO_CURRENCY)));
        c.setToCurrency(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_DATE)));
        c.setRateValue(cursor.getDouble(cursor.getColumnIndex(RateTable.COLUMN_RATE_VALUE)));

        return c;
    }

    public List<Rate> getRates(){
        String cols[] = {RateTable._ID, RateTable.COLUMN_BASE_CURRENCY, RateTable.COLUMN_TO_CURRENCY, RateTable.COLUMN_RATE_VALUE,RateTable.COLUMN_DATE};
        Cursor cursor = db.query(RateTable.TABLE_NAME, cols, null, null, null, null, RateTable._ID);
        List<Rate> Rates = new ArrayList<>();
        Rate s;

        while(cursor.moveToNext()){
            s = new Rate();
            s.setId(cursor.getLong(cursor.getColumnIndex(RateTable._ID)));
            s.setBaseCurrency(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_BASE_CURRENCY)));
            s.setToCurrency(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_TO_CURRENCY)));
            s.setRateValue(cursor.getDouble(cursor.getColumnIndex(RateTable.COLUMN_RATE_VALUE)));
            s.setDate(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_DATE)));
            Rates.add(s);
        }

        return Rates;
    }

    public List<Rate> getRateByCurrency(String baseCurrency){
        String cols[] = {RateTable._ID, RateTable.COLUMN_DATE, RateTable.COLUMN_BASE_CURRENCY, RateTable.COLUMN_RATE_VALUE,RateTable.COLUMN_TO_CURRENCY};
        String args[] = {baseCurrency};
        Cursor cursor = db.query(RateTable.TABLE_NAME, cols, RateTable.COLUMN_TO_CURRENCY+"=?", args, null, null, RateTable._ID);
        List<Rate> rates = new ArrayList<>();
        Rate s;

        while(cursor.moveToNext()){
            s = new Rate();
            s.setId(cursor.getLong(cursor.getColumnIndex(RateTable._ID)));
            s.setDate(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_DATE)));
            s.setBaseCurrency(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_BASE_CURRENCY)));
            s.setToCurrency(cursor.getString(cursor.getColumnIndex(RateTable.COLUMN_TO_CURRENCY)));
            s.setRateValue(cursor.getDouble(cursor.getColumnIndex(RateTable.COLUMN_RATE_VALUE)));

            rates.add(s);
        }

        return rates;
    }

    public Long addRate(Rate d){
        ContentValues values = new ContentValues();

        values.put(RateTable.COLUMN_BASE_CURRENCY,d.getBaseCurrency());
        values.put(RateTable.COLUMN_TO_CURRENCY,d.getToCurrency());
        values.put(RateTable.COLUMN_RATE_VALUE,d.getRateValue());
        values.put(RateTable.COLUMN_DATE,d.getDate());

        return db.insert(RateTable.TABLE_NAME, null, values);
    }

    private class DatabaseHelper extends SQLiteOpenHelper{

        DatabaseHelper(){
            super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(RateTable.getSQL());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TABLE IF EXISTS " + RateTable.TABLE_NAME);
            onCreate(db);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }
    }
}
