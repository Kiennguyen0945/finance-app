package com.example.ltdt5_test2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class ThreeFragment : Fragment() {
    
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_three, container, false)
        
        dbHelper = DatabaseHelper(requireContext())
        
        // Ánh xạ
        val rvStats = view.findViewById<RecyclerView>(R.id.rvStats)
        val switchDarkMode = view.findViewById<Switch>(R.id.switchDarkMode)
        val btnWipeData = view.findViewById<Button>(R.id.btnWipeData)

        // TODO CHO THẰNG BẠN 1 (THỐNG KÊ):
        // 1. Lấy dữ liệu: val allList = dbHelper.getAllTransactions()
        // 2. Gom nhóm theo Category bằng cách dùng hàm .groupBy { it.category } của Kotlin.
        // 3. Tính tổng amount từng nhóm.
        // 4. Tạo 1 cái Adapter đơn giản (chỉ gồm 1 text Category + 1 text Tổng tiền) rồi đổ vào rvStats.
        // GỢI Ý: Chú ý chỉ cộng các khoản Chi, không cộng chung Thu và Chi.

        // TODO CHO THẰNG BẠN 2 (DARK MODE):
        // 1. Khởi tạo SharedPreferences để lưu cờ isDarkMode.
        // 2. Set trạng thái ban đầu cho switchDarkMode cờ đó.
        // 3. Bắt sự kiện switchDarkMode.setOnCheckedChangeListener
        // 4. Update cờ vào SharePref -> Chạy lệnh AppCompatDelegate.setDefaultNightMode(...)

        // TODO CHO THẰNG BẠN 3 (WIPE DATA):
        // 1. Bắt sự kiện btnWipeData click.
        // 2. Hiện AlertDialog "Cảnh báo mất toàn bộ dữ liệu".
        // 3. Nếu OK -> Gọi dbHelper.deleteAllTransactions()
        // 4. Gợi ý: Làm mới lại rvStats ngay sau khi xóa, tránh list báo cáo vẫn kẹt data cũ.

        return view
    }
}