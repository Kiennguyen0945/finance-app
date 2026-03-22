package com.example.ltdt5_test2

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OneFragment : Fragment() {

    private val transactionList = mutableListOf<Transaction>()
    private lateinit var adapter: TransactionAdapter
    private lateinit var dbHelper: DatabaseHelper // Khai báo DB

    private lateinit var tvTotalBalance: TextView
    private lateinit var tvTotalIncome: TextView
    private lateinit var tvTotalExpense: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_one, container, false)
        
        dbHelper = DatabaseHelper(requireContext()) // Khởi tạo DB

        // Ánh xạ
        tvTotalBalance = view.findViewById(R.id.tvTotalBalance)
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome)
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense)
        val rvRecent = view.findViewById<RecyclerView>(R.id.rvRecentTransactions)
        val fabAdd = view.findViewById<FloatingActionButton>(R.id.fabAddTransaction)

        // Setup RecyclerView
        adapter = TransactionAdapter(transactionList)
        rvRecent.layoutManager = LinearLayoutManager(context)
        rvRecent.adapter = adapter

        // Tải dữ liệu từ SQLite thay vì Mock Data
        loadDataFromDatabase()

        // Nút thêm giao dịch
        fabAdd.setOnClickListener {
            showAddTransactionDialog()
        }

        return view
    }

    // Hàm load dữ liệu từ DB
    private fun loadDataFromDatabase() {
        transactionList.clear()
        transactionList.addAll(dbHelper.getAllTransactions())
        adapter.notifyDataSetChanged()
        updateDashboard()
    }

    private fun updateDashboard() {
        var totalIncome = 0.0
        var totalExpense = 0.0
        
        // Lấy tháng/năm hiện tại để lọc (Ví dụ: "03/2026")
        val currentMonthYear = SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(Date())

        for (tx in transactionList) {
            // Cắt chuỗi ngày "dd/MM/yyyy" lấy "MM/yyyy" để so sánh
            val txMonthYear = tx.date.substringAfter("/")
            if (txMonthYear == currentMonthYear) {
                if (tx.type == 1) totalIncome += tx.amount
                else totalExpense += tx.amount
            }
        }
        val totalBalance = totalIncome - totalExpense

        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        tvTotalIncome.text = "${formatter.format(totalIncome)} đ"
        tvTotalExpense.text = "${formatter.format(totalExpense)} đ"
        tvTotalBalance.text = "${formatter.format(totalBalance)} đ"

        // Xử lý số dư âm
        if (totalBalance < 0) {
            tvTotalBalance.setTextColor(Color.parseColor("#F44336")) // Đỏ
        } else {
            tvTotalBalance.setTextColor(Color.parseColor("#000000")) // Đen
        }
    }

    private fun showAddTransactionDialog() {
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_add_transaction, null)
        val etAmount = dialogView.findViewById<EditText>(R.id.etAmount)
        val etCategory = dialogView.findViewById<EditText>(R.id.etCategory)
        val etNote = dialogView.findViewById<EditText>(R.id.etNote)
        val rgType = dialogView.findViewById<RadioGroup>(R.id.rgType)

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Lưu", null) // Set null để tự handle sự kiện click (tránh tắt dialog khi lỗi)
            .setNegativeButton("Hủy") { d, _ -> d.dismiss() }
            .create()

        // Khóa việc ấn ra ngoài dialog để tắt
        dialog.setCanceledOnTouchOutside(false)

        dialog.setOnShowListener {
            val button = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
            button.setOnClickListener {
                val amountStr = etAmount.text.toString()
                val category = etCategory.text.toString().trim()

                // Validate bỏ trống
                if (amountStr.isEmpty() || category.isEmpty()) {
                    Toast.makeText(context, "Vui lòng nhập số tiền và danh mục", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Validate số tiền
                val amount = amountStr.toDoubleOrNull() ?: 0.0
                if (amount <= 0) {
                    Toast.makeText(context, "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val note = etNote.text.toString().trim()
                val type = if (rgType.checkedRadioButtonId == R.id.rbIncome) 1 else 2
                
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                val date = sdf.format(Date())

                // Tạo đối tượng Transaction
                val newTx = Transaction(0, amount, type, category, date, note)
                
                // Lưu vào SQLite
                dbHelper.insertTransaction(newTx)
                
                // Load lại dữ liệu lên giao diện
                loadDataFromDatabase()
                
                dialog.dismiss()
            }
        }
        dialog.show()
    }
}