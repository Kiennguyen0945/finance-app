package com.example.ltdt5_test2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class ThreeFragment extends Fragment {

    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Ánh xạ View
        RecyclerView rvStats = view.findViewById(R.id.rvStats);
        Switch switchDarkMode = view.findViewById(R.id.switchDarkMode);
        Button btnWipeData = view.findViewById(R.id.btnWipeData);

        // TODO CHO THẰNG BẠN 1 (THỐNG KÊ):
        // 1. Lấy dữ liệu: List<Transaction> allList = dbHelper.getAllTransactions();
        // 2. Dùng Map<String, Double> hoặc 2 mảng đồng thời để gom nhóm theo category và cộng dồn amount.
        // 3. Tạo 1 cái Adapter đơn giản (chỉ gồm 1 text Category + 1 text Tổng tiền) rồi đổ vào rvStats.
        // GỢI Ý: Chú ý chỉ cộng các khoản Chi (type == 2), không cộng chung Thu và Chi.

        // TODO CHO THẰNG BẠN 2 (DARK MODE):
        // 1. Khởi tạo SharedPreferences để lưu cờ isDarkMode.
        // 2. Set trạng thái ban đầu cho switchDarkMode cờ đó.
        // 3. Bắt sự kiện switchDarkMode.setOnCheckedChangeListener
        // 4. Update cờ vào SharePref -> Chạy lệnh AppCompatDelegate.setDefaultNightMode(...)

        // TODO CHO THẰNG BẠN 3 (WIPE DATA):
        // 1. Bắt sự kiện btnWipeData click.
        // 2. Hiện AlertDialog "Cảnh báo mất toàn bộ dữ liệu".
        // 3. Nếu OK -> Gọi dbHelper.deleteAllTransactions();
        // 4. Gợi ý: Làm mới lại rvStats ngay sau khi xóa, tránh list báo cáo vẫn kẹt data cũ.

        return view;
    }
}