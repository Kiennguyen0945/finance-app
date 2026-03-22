package com.example.ltdt5_test2

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "finance_db"
        private const val DATABASE_VERSION = 1

        const val TABLE_TRANSACTIONS = "transactions"
        const val COLUMN_ID = "id"
        const val COLUMN_AMOUNT = "amount"
        const val COLUMN_TYPE = "type"
        const val COLUMN_CATEGORY = "category"
        const val COLUMN_DATE = "date"
        const val COLUMN_NOTE = "note"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = ("CREATE TABLE $TABLE_TRANSACTIONS ("
                + "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "$COLUMN_AMOUNT REAL, "
                + "$COLUMN_TYPE INTEGER, "
                + "$COLUMN_CATEGORY TEXT, "
                + "$COLUMN_DATE TEXT, "
                + "$COLUMN_NOTE TEXT)")
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TRANSACTIONS")
        onCreate(db)
    }

    // Thêm giao dịch (CRUD - Create)
    fun insertTransaction(tx: Transaction): Long {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, tx.amount)
            put(COLUMN_TYPE, tx.type)
            put(COLUMN_CATEGORY, tx.category)
            put(COLUMN_DATE, tx.date)
            put(COLUMN_NOTE, tx.note)
        }
        val id = db.insert(TABLE_TRANSACTIONS, null, values)
        db.close()
        return id
    }

    // Lấy toàn bộ giao dịch (CRUD - Read) - Sắp xếp mới nhất lên đầu
    fun getAllTransactions(): List<Transaction> {
        val list = mutableListOf<Transaction>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_TRANSACTIONS ORDER BY $COLUMN_ID DESC", null)

        if (cursor.moveToFirst()) {
            do {
                val transaction = Transaction(
                    id = cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_ID)),
                    amount = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_AMOUNT)),
                    type = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_TYPE)),
                    category = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CATEGORY)),
                    date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE)),
                    note = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NOTE))
                )
                list.add(transaction)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return list
    }

    // Cập nhật giao dịch (CRUD - Update)
    fun updateTransaction(tx: Transaction): Int {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AMOUNT, tx.amount)
            put(COLUMN_TYPE, tx.type)
            put(COLUMN_CATEGORY, tx.category)
            put(COLUMN_DATE, tx.date)
            put(COLUMN_NOTE, tx.note)
        }
        val result = db.update(TABLE_TRANSACTIONS, values, "$COLUMN_ID = ?", arrayOf(tx.id.toString()))
        db.close()
        return result
    }

    // Xóa 1 giao dịch (CRUD - Delete)
    fun deleteTransaction(id: Long): Int {
        val db = this.writableDatabase
        val result = db.delete(TABLE_TRANSACTIONS, "$COLUMN_ID = ?", arrayOf(id.toString()))
        db.close()
        return result
    }

    // --- CÁC HÀM HỖ TRỢ TEST ---

    // Xóa toàn bộ dữ liệu (Clear)
    fun deleteAllTransactions() {
        val db = this.writableDatabase
        db.execSQL("DELETE FROM $TABLE_TRANSACTIONS")
        // Đặt lại ID tự tăng về 0
        db.execSQL("DELETE FROM sqlite_sequence WHERE name='$TABLE_TRANSACTIONS'")
        db.close()
    }

    // Chèn dữ liệu mẫu (Seed)
    fun insertDummyData() {
        val date = java.text.SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(java.util.Date())
        insertTransaction(Transaction(0, 15000000.0, 1, "Lương tháng", date, "Lương cty"))
        insertTransaction(Transaction(0, 50000.0, 2, "Cà phê", date, ""))
        insertTransaction(Transaction(0, 120000.0, 2, "Ăn trưa", date, ""))
    }
}