package com.example.ltdt5_test2;

import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
    private RadioButton rb_TatCa, rb_Thu, rb_Chi;

    private TextView tvEmptyState;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_two, container, false);

        dbHelper = new DatabaseHelper(requireContext()); // đối tượng làm việc với Database (lấy context từ Fragment)

        dbHelper.getReadableDatabase();

        //spinner
        spnThang = view.findViewById(R.id.spinnerMonth);
        XuLy_Spinner(); //goi ham xu ly spinner


        //radiobutton
        radioGroup = view.findViewById(R.id.rgFilterType);
        rb_TatCa = view.findViewById(R.id.rbAll);
        rb_Thu = view.findViewById(R.id.rbIncomeOnly);
        rb_Chi = view.findViewById(R.id.rbExpenseOnly);
        XuLy_RadioButton(); //goi ham xu ly radiobutton





        // Ánh xạ View
        Spinner spinnerMonth = view.findViewById(R.id.spinnerMonth);
        RadioGroup rgFilterType = view.findViewById(R.id.rgFilterType);
        tvEmptyState = view.findViewById(R.id.tvEmptyState);

        RecyclerView rvAllTransactions = view.findViewById(R.id.rvAllTransactions);

        // Setup RecyclerView
        adapter = new TransactionAdapter(transactionList); //adapter = cầu nối giữa: transactionList (dữ liệu) và RecyclerView (UI)
        rvAllTransactions.setLayoutManager(new LinearLayoutManager(getContext())); // Hiển thị dạng: Danh sách dọc (vertical list)
        rvAllTransactions.setAdapter(adapter);


        // Thiết lập sự kiện click vào item --- chưa xonggggggggggggggg
//        adapter.setOnItemClickListener(new TransactionAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Transaction transaction) {
//                showAddTransactionDialog(transaction);
//            }
//        });

        // Load dữ liệu từ Database
        loadDataFromDatabase();






        // TODO CHO THẰNG BẠN 1:  --- XONGGGGG
        // Viết hàm loadData() để lấy danh sách từ dbHelper.getAllTransactions() --- XONGGGGG

        // Kiểm tra list rỗng thì cho tvEmptyState.setVisibility(View.VISIBLE), ngược lại thì View.GONE. --- XONGGGGG

        // TODO CHO THẰNG BẠN 2: --- XONGGGGG
        // Set sự kiện lắng nghe cho rgFilterType (RadioGroup) để lọc Thu/Chi. --- XONGGGGG
        // Gợi ý: Dùng vòng lặp for lọc if (tx.getType() == 1) nhét vào list mới rồi update adapter.

        // TODO CHO THẰNG BẠN 3 (LONG CLICK ĐỂ XÓA):
        // Vào TransactionAdapter.java thêm Interface listener onClick, onLongClick.
        // Ở đây gọi AlertDialog xác nhận Xóa -> gọi dbHelper.deleteTransaction(id) -> Tải lại list.

        // TODO CHO THẰNG BẠN 4 (CLICK ĐỂ SỬA):
        // Mở lại Dialog y hệt bên OneFragment.kt, truyền dữ liệu cũ vào etAmount.setText(...)
        // Sau khi bấm Lưu thì gọi dbHelper.updateTransaction(tx) -> Tải lại list.

        return view;
    }


    // Hàm load dữ liệu từ database
    private void loadDataFromDatabase() {
        transactionList.clear();
        transactionList.addAll(dbHelper.getAllTransactions());
        adapter.notifyDataSetChanged();


        updateEmptyState(tvEmptyState);

    }

    //Hàm xử lý spinner chọn Tháng.
    private void XuLy_Spinner(){
        List<String> listThang = new ArrayList<>();

        if(spnThang != null){
            //tao ds luu cac Thang
            listThang.add("Tháng 1");
            listThang.add("Tháng 2");
            listThang.add("Tháng 3");
            listThang.add("Tháng 4");
            listThang.add("Tháng 5");
            listThang.add("Tháng 6");
            listThang.add("Tháng 7");
            listThang.add("Tháng 8");
            listThang.add("Tháng 9");
            listThang.add("Tháng 10");
            listThang.add("Tháng 11");
            listThang.add("Tháng 12");

            //tao adapter do dl Thang vao spinner
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, listThang);

            // thiết lập layout spinner khi mở (single choice là chỉ 1 lựa chọn)
            adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

            //set - do du lieu
            spnThang.setAdapter(adapter);

        }

        // xử lý sự kiện khi chọn item trong spinner
        spnThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    // Hàm xử lý radiobutton
    private void XuLy_RadioButton(){

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            transactionList.clear();

            if (checkedId == R.id.rbAll) {
                // chọn Tat ca
                transactionList.addAll(dbHelper.getAllTransactions());

            } else if (checkedId == R.id.rbIncomeOnly) {
                // chọn Thu
                transactionList.addAll(dbHelper.get_Incomes_Transactions()); // hàm load dữ liệu THU từ database (mới tạo bên DatabaseHelpler)

            } else if (checkedId == R.id.rbExpenseOnly) {
                // chọn Chi
                transactionList.addAll(dbHelper.get_Expenses_Transactions());
            }

            adapter.notifyDataSetChanged();


            updateEmptyState(tvEmptyState);

        });

    }


    private void updateEmptyState(TextView tvEmptyState) {
        if (transactionList.isEmpty()) {
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
        }
    }

    // Hàm hiển thị Dialog chỉnh sửa giao dịch - CHƯA XOGNGGGGGGGGGGGGGGGGGGGG
    private void showAddTransactionDialog(Transaction transaction) {

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_edit_transaction, null);
        EditText etAmount = dialogView.findViewById(R.id.etAmount);
        EditText etCategory = dialogView.findViewById(R.id.etCategory);
        EditText etNote = dialogView.findViewById(R.id.etNote);
        RadioGroup rgType = dialogView.findViewById(R.id.rgType);

        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(requireContext())
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
                dbHelper.updateTransaction(newTx); // cập nhật

                loadDataFromDatabase();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

}// end