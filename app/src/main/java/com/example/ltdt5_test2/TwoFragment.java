package com.example.ltdt5_test2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TwoFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private TransactionAdapter adapter;
    private List<Transaction> transactionList = new ArrayList<>();

    private Spinner spnThang;
    private RadioGroup radioGroup;
    private TextView tvEmptyState;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        dbHelper = new DatabaseHelper(requireContext());

        // Ánh xạ View
        spnThang = view.findViewById(R.id.spinnerMonth);
        radioGroup = view.findViewById(R.id.rgFilterType);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);
        RecyclerView rvAllTransactions = view.findViewById(R.id.rvAllTransactions);

        // Setup RecyclerView
        adapter = new TransactionAdapter(transactionList);
        rvAllTransactions.setLayoutManager(new LinearLayoutManager(getContext()));
        rvAllTransactions.setAdapter(adapter);

        // Thiết lập sự kiện click vào item
        adapter.setOnItemClickListener(new TransactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Transaction transaction) {
                showEditTransactionDialog(transaction);
            }
        });

        XuLy_Spinner();
        XuLy_RadioButton();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // QUAN TRỌNG: Tự động load lại dữ liệu mỗi khi quay lại Tab này
        refreshDataByFilter();
    }

    private void refreshDataByFilter() {
        if (radioGroup == null) return;
        
        int checkedId = radioGroup.getCheckedRadioButtonId();
        transactionList.clear();

        if (checkedId == R.id.rbAll || checkedId == -1) {
            transactionList.addAll(dbHelper.getAllTransactions());
        } else if (checkedId == R.id.rbIncomeOnly) {
            transactionList.addAll(dbHelper.get_Incomes_Transactions());
        } else if (checkedId == R.id.rbExpenseOnly) {
            transactionList.addAll(dbHelper.get_Expenses_Transactions());
        }

        adapter.notifyDataSetChanged();
        updateEmptyState();
    }

    private void XuLy_Spinner(){
        List<String> listThang = new ArrayList<>();
        for(int i=1; i<=12; i++) listThang.add("Tháng " + i);

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listThang);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spnThang.setAdapter(adapterSpinner);
    }

    private void XuLy_RadioButton(){
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            refreshDataByFilter();
        });
    }

    private void updateEmptyState() {
        if (tvEmptyState == null) return;
        if (transactionList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }
    }

    private void showEditTransactionDialog(Transaction transaction) {
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_transaction, null);
        EditText etAmount = dialogView.findViewById(R.id.etAmount);
        EditText etCategory = dialogView.findViewById(R.id.etCategory);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        RadioGroup rgType = dialogView.findViewById(R.id.rgType);
        Button btn_Xoa = dialogView.findViewById(R.id.btn_xoa);

        AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Lưu", null)
                .setNegativeButton("Hủy", (d, which) -> d.dismiss())
                .create();

        btn_Xoa.setOnClickListener(v -> DeleteTransaction(transaction, dialog));

        etAmount.setText(String.valueOf(transaction.getAmount()));
        etCategory.setText(transaction.getCategory());
        etNote.setText(transaction.getNote());

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                String amountStr = etAmount.getText().toString();
                String category = etCategory.getText().toString().trim();

                if (amountStr.isEmpty() || category.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập số tiền và danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount = Double.parseDouble(amountStr);
                int type = (rgType.getCheckedRadioButtonId() == R.id.rbIncome) ? 1 : 2;
                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

                Transaction newTx = new Transaction(transaction.getId(), amount, type, category, date, etNote.getText().toString().trim());
                dbHelper.updateTransaction(newTx);

                refreshDataByFilter();
                dialog.dismiss();
            });
        });
        dialog.show();
    }

    private void DeleteTransaction(Transaction transaction, AlertDialog editDialog) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xác nhận")
                .setMessage("Bạn có muốn xóa giao dịch này?")
                .setPositiveButton("Xóa", (d, which) -> {
                    dbHelper.deleteTransaction(transaction.getId());
                    refreshDataByFilter();
                    if (editDialog != null) editDialog.dismiss();
                    Toast.makeText(getContext(), "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}
