package com.example.ltdt5_test2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OneFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TextView tvTotalBalance, tvTotalIncome, tvTotalExpense;
    private TransactionAdapter adapter;
    private List<Transaction> recentList = new ArrayList<>();

    @Nullable
    @Override
    // Tạo giao diện cho cái fragment
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Ánh xạ View
        tvTotalBalance = view.findViewById(R.id.tvTotalBalance);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);
        RecyclerView rvRecent = view.findViewById(R.id.rvRecentTransactions);

        // Setup RecyclerView
        adapter = new TransactionAdapter(recentList);
        rvRecent.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRecent.setAdapter(adapter);

        // Nút Load của Vĩ
        FloatingActionButton fabLoad = view.findViewById(R.id.fab_LoadTransaction);
        if (fabLoad != null) {
            fabLoad.setOnClickListener(v -> refreshData());
        }

        // Nút Thêm (FAB)
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddTransaction);
        if (fabAdd != null) {
            fabAdd.setOnClickListener(v -> showAddDialog());
        }

        refreshData();
        return view;
    }

    private void showAddDialog() {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_transaction, null);
        EditText etAmount = dialogView.findViewById(R.id.etAmount);
        EditText etCategory = dialogView.findViewById(R.id.etCategory);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        RadioGroup rgType = dialogView.findViewById(R.id.rgType);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", (d, which) -> d.dismiss())
                .create();

        dialog.setOnShowListener(dInterface -> {
            Button btnSave = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnSave.setOnClickListener(v -> {
                String amountStr = etAmount.getText().toString();
                String category = etCategory.getText().toString().trim();

                if (amountStr.isEmpty() || category.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                int type = (rgType.getCheckedRadioButtonId() == R.id.rbIncome) ? 1 : 2;
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                Transaction tx = new Transaction(0, amount, type, category, date, etNote.getText().toString().trim());
                dbHelper.insertTransaction(tx);

                refreshData();
                dialog.dismiss();
                Toast.makeText(getContext(), "Đã thêm giao dịch", Toast.LENGTH_SHORT).show();
            });
        });

        dialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        List<Transaction> all = dbHelper.getAllTransactions();
        
        double income = 0, expense = 0;
        for (Transaction t : all) {
            if (t.getType() == 1) income += t.getAmount();
            else expense += t.getAmount();
        }

        DecimalFormat df = new DecimalFormat("#,###");
        if (tvTotalIncome != null) tvTotalIncome.setText(df.format(income) + " đ");
        if (tvTotalExpense != null) tvTotalExpense.setText(df.format(expense) + " đ");
        if (tvTotalBalance != null) tvTotalBalance.setText(df.format(income - expense) + " đ");

        recentList.clear();
        if (all.size() > 5) recentList.addAll(all.subList(0, 5));
        else recentList.addAll(all);
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}
