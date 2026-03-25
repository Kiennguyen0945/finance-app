package com.example.ltdt5_test2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TwoFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Ánh xạ View
        Spinner spinnerMonth = view.findViewById(R.id.spinnerMonth);
        RadioGroup rgFilterType = view.findViewById(R.id.rgFilterType);
        TextView tvEmptyState = view.findViewById(R.id.tvEmptyState);
        RecyclerView rvAllTransactions = view.findViewById(R.id.rvAllTransactions);

        // Setup RecyclerView
        adapter = new TransactionAdapter(transactionList);
        rvAllTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAllTransactions.setAdapter(adapter);

        // TODO CHO THẰNG BẠN 1: 
        // Viết hàm loadData() để lấy danh sách từ dbHelper.getAllTransactions()
        // Kiểm tra list rỗng thì cho tvEmptyState.setVisibility(View.VISIBLE), ngược lại thì View.GONE.

        // TODO CHO THẰNG BẠN 2:
        // Set sự kiện lắng nghe cho rgFilterType (RadioGroup) để lọc Thu/Chi.
        // Gợi ý: Dùng vòng lặp for lọc if (tx.getType() == 1) nhét vào list mới rồi update adapter.

        // TODO CHO THẰNG BẠN 3 (LONG CLICK ĐỂ XÓA):
        // Vào TransactionAdapter.java thêm Interface listener onClick, onLongClick.
        // Ở đây gọi AlertDialog xác nhận Xóa -> gọi dbHelper.deleteTransaction(id) -> Tải lại list.

        // TODO CHO THẰNG BẠN 4 (CLICK ĐỂ SỬA):
        // Mở lại Dialog y hệt bên OneFragment.kt, truyền dữ liệu cũ vào etAmount.setText(...)
        // Sau khi bấm Lưu thì gọi dbHelper.updateTransaction(tx) -> Tải lại list.

        return view;
    }
}