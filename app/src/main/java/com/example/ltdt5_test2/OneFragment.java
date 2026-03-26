package com.example.ltdt5_test2;

import android.app.AlertDialog;
import android.graphics.Color;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OneFragment extends Fragment {

    private List<Transaction> transactionList = new ArrayList<>();
    private TransactionAdapter adapter;
    private DatabaseHelper dbHelper;

    private TextView tvTotalBalance;
    private TextView tvTotalIncome;
    private TextView tvTotalExpense;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_one, container, false);


        dbHelper = new DatabaseHelper(requireContext()); // đối tượng làm việc với Database (lấy context từ Fragment)

        tvTotalBalance = view.findViewById(R.id.tvTotalBalance);
        tvTotalIncome = view.findViewById(R.id.tvTotalIncome);
        tvTotalExpense = view.findViewById(R.id.tvTotalExpense);

        RecyclerView rvRecent = view.findViewById(R.id.rvRecentTransactions);
        FloatingActionButton fabAdd = view.findViewById(R.id.fabAddTransaction);
        FloatingActionButton fabLoad = view.findViewById(R.id.fab_LoadTransaction); // nút load _ Vĩ thêmmm

        adapter = new TransactionAdapter(transactionList); //adapter = cầu nối giữa: transactionList (dữ liệu) và RecyclerView (UI)

        rvRecent.setLayoutManager(new LinearLayoutManager(getContext())); // Hiển thị dạng: Danh sách dọc (vertical list)
        rvRecent.setAdapter(adapter); // gắn Adapter vào RecyclerView (dữ liệu sẽ hiện lên màn hình)

        // Load dữ liệu từ Database
        loadDataFromDatabase();

        fabAdd.setOnClickListener(v -> showAddTransactionDialog());
        fabLoad.setOnClickListener(v -> loadDataFromDatabase());

        return view;
    }

    private void loadDataFromDatabase() {
        transactionList.clear();
        transactionList.addAll(dbHelper.getAllTransactions());
        adapter.notifyDataSetChanged();
        updateDashboard(); // hàm này của riêng frag_1

        Toast.makeText(getContext(), "Đã load dữ liệu mới", Toast.LENGTH_SHORT).show();
    }

    private void updateDashboard() {
        double totalIncome = 0.0;
        double totalExpense = 0.0;

        // Lấy "MM/yyyy" hiện tại
        String currentMonthYear = new SimpleDateFormat("MM/yyyy", Locale.getDefault()).format(new Date());

        for (Transaction tx : transactionList) {
            String dateStr = tx.getDate();
            String txMonthYear = "";
            if (dateStr != null && dateStr.contains("/")) {
                // Tách lấy tháng và năm (sau dấu "/" cuối cùng, vd: 22/03/2026 -> 03/2026)
                txMonthYear = dateStr.substring(dateStr.indexOf("/") + 1);
            }

            if (txMonthYear.equals(currentMonthYear)) {
                if (tx.getType() == 1) {
                    totalIncome += tx.getAmount();
                } else {
                    totalExpense += tx.getAmount();
                }
            }
        }
        double totalBalance = totalIncome - totalExpense;

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        tvTotalIncome.setText(formatter.format(totalIncome) + " đ");
        tvTotalExpense.setText(formatter.format(totalExpense) + " đ");
        tvTotalBalance.setText(formatter.format(totalBalance) + " đ");

        if (totalBalance < 0) {
            tvTotalBalance.setTextColor(Color.parseColor("#F44336"));
        } else {
            tvTotalBalance.setTextColor(Color.parseColor("#000000"));
        }
    }

    private void showAddTransactionDialog() {
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

        dialog.setCanceledOnTouchOutside(false);

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {
                String amountStr = etAmount.getText().toString();
                String category = etCategory.getText().toString().trim();

                if (amountStr.isEmpty() || category.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập số tiền và danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                double amount;
                try {
                    amount = Double.parseDouble(amountStr);
                } catch (NumberFormatException e) {
                    amount = 0.0;
                }

                if (amount <= 0) {
                    Toast.makeText(getContext(), "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                String note = etNote.getText().toString().trim();
                int type = (rgType.getCheckedRadioButtonId() == R.id.rbIncome) ? 1 : 2;

                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = sdf.format(new Date());

                Transaction newTx = new Transaction(0, amount, type, category, date, note);
                dbHelper.insertTransaction(newTx);

                loadDataFromDatabase();
                Toast.makeText(getContext(), "Đã thêm dữ liệu mới", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
        });

        dialog.show();
    }
}