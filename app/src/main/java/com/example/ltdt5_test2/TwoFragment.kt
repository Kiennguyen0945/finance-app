package com.example.ltdt5_test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TwoFragment : Fragment() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: TransactionAdapter
    private var transactionList = mutableListOf<Transaction>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_two, container, false)

        dbHelper = DatabaseHelper(requireContext())

        // Ánh xạ View
        val spinnerMonth = view.findViewById<Spinner>(R.id.spinnerMonth)
        val rgFilterType = view.findViewById<RadioGroup>(R.id.rgFilterType)
        val tvEmptyState = view.findViewById<TextView>(R.id.tvEmptyState)
        val rvAllTransactions = view.findViewById<RecyclerView>(R.id.rvAllTransactions)

        // Setup RecyclerView
        adapter = TransactionAdapter(transactionList)
        rvAllTransactions.layoutManager = LinearLayoutManager(context)
        rvAllTransactions.adapter = adapter

        // TODO CHO THẰNG BẠN 1:
        // Viết hàm loadData() để lấy danh sách từ dbHelper.getAllTransactions()
        // Kiểm tra list rỗng thì cho tvEmptyState.visibility = View.VISIBLE, ngược lại thì GONE.

        // TODO CHO THẰNG BẠN 2:
        // Set sự kiện lắng nghe cho rgFilterType (RadioGroup) để lọc Thu/Chi.
        // Gợi ý: Dùng List.filter { it.type == 1 }

        // TODO CHO THẰNG BẠN 3 (LONG CLICK ĐỂ XÓA):
        // Vào TransactionAdapter.kt thêm Interface listener onClick, onLongClick.
        // Ở đây gọi AlertDialog xác nhận Xóa -> gọi dbHelper.deleteTransaction(id) -> Tải lại list.

        // TODO CHO THẰNG BẠN 4 (CLICK ĐỂ SỬA):
        // Mở lại Dialog y hệt bên OneFragment.kt, truyền dữ liệu cũ vào etAmount.setText(...)
        // Sau khi bấm Lưu thì gọi dbHelper.updateTransaction(tx) -> Tải lại list.

        return view
    }
}