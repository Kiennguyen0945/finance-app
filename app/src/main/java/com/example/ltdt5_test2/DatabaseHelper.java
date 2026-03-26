package com.example.ltdt5_test2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "finance_db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TRANSACTIONS = "transactions";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_AMOUNT = "amount";
    public static final String COLUMN_TYPE = "type";
    public static final String COLUMN_CATEGORY = "category";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_NOTE = "note";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_TRANSACTIONS + " ("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_AMOUNT + " REAL, "
                + COLUMN_TYPE + " INTEGER, "
                + COLUMN_CATEGORY + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_NOTE + " TEXT)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TRANSACTIONS);
        onCreate(db);
    }

    public long insertTransaction(Transaction tx) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_AMOUNT, tx.getAmount());
        values.put(COLUMN_TYPE, tx.getType());
        values.put(COLUMN_CATEGORY, tx.getCategory());
        values.put(COLUMN_DATE, tx.getDate());
        values.put(COLUMN_NOTE, tx.getNote());

        long id = db.insert(TABLE_TRANSACTIONS, null, values);
        db.close();
        return id;
    }

    // Lấy tất cả giao dịch (CRUD - Read)
    public List<Transaction> getAllTransactions() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
                );
                list.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Lấy tất cả giao dịch "thu" (CRUD - Read)
    public List<Transaction> get_Incomes_Transactions() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 1" + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
                );
                list.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Lấy tất cả giao dịch "chi" (CRUD - Read)
    public List<Transaction> get_Expenses_Transactions() {
        List<Transaction> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " WHERE " + COLUMN_TYPE + " = 2" + " ORDER BY " + COLUMN_ID + " DESC", null);

        if (cursor.moveToFirst()) {
            do {
                Transaction transaction = new Transaction(
                        cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
                );
                list.add(transaction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    // Cập nhật giao dịch (CRUD - Update)
    public int updateTransaction(Transaction tx) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_AMOUNT, tx.getAmount());
        values.put(COLUMN_TYPE, tx.getType());
        values.put(COLUMN_CATEGORY, tx.getCategory());
        values.put(COLUMN_DATE, tx.getDate());
        values.put(COLUMN_NOTE, tx.getNote());

        int result = db.update(TABLE_TRANSACTIONS,
                values,
                COLUMN_ID + " = ?",
                new String[]{String.valueOf(tx.getId())}); // chổ này lấy id truyền vào chổ "?"
        db.close();

        return result;
    }

    // Xóa 1 giao dịch (CRUD - Delete)
    public int deleteTransaction(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_TRANSACTIONS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
        return result;
    }

    // --- CÁC HÀM HỖ TRỢ TEST ---

    // Xóa toàn bộ dữ liệu (Clear)
    public void deleteAllTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_TRANSACTIONS);
        // Đặt lại ID tự tăng về 0
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='" + TABLE_TRANSACTIONS + "'");
        db.close();
    }

    // Chèn dữ liệu mẫu (Seed)
    public void insertDummyData() {
        String date = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
        insertTransaction(new Transaction(0, 15000000.0, 1, "Lương tháng", date, "Lương cty"));
        insertTransaction(new Transaction(0, 50000.0, 2, "Cà phê", date, ""));
        insertTransaction(new Transaction(0, 120000.0, 2, "Ăn trưa", date, ""));
    }
}
