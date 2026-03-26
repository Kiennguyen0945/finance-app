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

import java.text.DecimalFormat;
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

    private Button btn_Xoa;

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


        // Thiết lập sự kiện click vào item
        adapter.setOnItemClickListener(new TransactionAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Transaction transaction) {
                showAddTransactionDialog(transaction);
            }
        });

        // Load dữ liệu ban đầu
        filterData();





        // TODO CHO THẰNG BẠN 1:  --- XONGGGGG
        // Viết hàm loadData() để lấy danh sách từ dbHelper.getAllTransactions() --- XONGGGGG

        // Kiểm tra list rỗng thì cho tvEmptyState.setVisibility(View.VISIBLE), ngược lại thì View.GONE. --- XONGGGGG

        // TODO CHO THẰNG BẠN 2: --- XONGGGGG
        // Set sự kiện lắng nghe cho rgFilterType (RadioGroup) để lọc Thu/Chi. --- XONGGGGG
        // Gợi ý: Dùng vòng lặp for lọc if (tx.getType() == 1) nhét vào list mới rồi update adapter.

        // TODO CHO THẰNG BẠN 3 (LONG CLICK ĐỂ XÓA): - XONG (Thay thế = nút xóa , KO dung longclik)
        // Vào TransactionAdapter.java thêm Interface listener onClick, onLongClick.
        // Ở đây gọi AlertDialog xác nhận Xóa -> gọi dbHelper.deleteTransaction(id) -> Tải lại list.

        // TODO CHO THẰNG BẠN 4 (CLICK ĐỂ SỬA): --- XONGGGGGg
        // Mở lại Dialog y hệt bên OneFragment.kt, truyền dữ liệu cũ vào etAmount.setText(...)
        // Sau khi bấm Lưu thì gọi dbHelper.updateTransaction(tx) -> Tải lại list.

        return view;
    }


    // Hàm load dữ liệu từ database
    private void loadDataFromDatabase() {
        filterData();

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

            // Mặc định chọn tháng hiện tại khi mở ứng dụng
            int currentMonth = Integer.parseInt(new SimpleDateFormat("MM", Locale.getDefault()).format(new Date()));
            spnThang.setSelection(currentMonth - 1);

        }

        // xử lý sự kiện khi chọn item trong spinner
        spnThang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                filterData();

                String selectedItem = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(getContext(), "Bạn chọn: " + selectedItem    , Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    // Hàm xử lý radiobutton
    private void XuLy_RadioButton(){

        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {

            filterData();
        });

    }


    // HÀm kiểm tra có dữ liệu hay ko
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
                .setPositiveButton("Lưu", null) //BUTTON_POSITIVE
                .setNegativeButton("Hủy", (d, which) -> d.dismiss())
                .create();

        dialog.setCanceledOnTouchOutside(false); // ko cho nhấn bên ngoài để tắt

        btn_Xoa = dialogView.findViewById(R.id.btn_xoa);
        btn_Xoa.setOnClickListener(v -> DeleteTransaction(transaction, dialog));

        // lấy dữ liệu cũ
        long _id = transaction.getId(); // lấy id để cập nhật

        // set dữ liệu cũ vào dialog

        DecimalFormat df = new DecimalFormat("#,###"); //điều chỉnh hiển thị ra số đầy đủ
        double value = transaction.getAmount();
        String result = df.format(value);

        etAmount.setText(result);
        etCategory.setText(transaction.getCategory());
        etNote.setText(transaction.getNote());

        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {

                String amountStr = etAmount.getText().toString();
                String category = etCategory.getText().toString().trim();

                //kiểm tra các ô có rỗng ko
                if (amountStr.isEmpty() || category.isEmpty()) {
                    Toast.makeText(getContext(), "Vui lòng nhập số tiền và danh mục", Toast.LENGTH_SHORT).show();
                    return;
                }

                // kiểm tra tiền nhập có sai ko
                double amount;
                try {
                    amount = Double.parseDouble(amountStr);

                } catch (NumberFormatException e) {
                    amount = 0.0;
                }

                if (amount <= 0) {  // nếu tiền < 0
                    Toast.makeText(getContext(), "Số tiền phải lớn hơn 0", Toast.LENGTH_SHORT).show();
                    return;
                }

                // lấy type mới (radio button)
                int type = (rgType.getCheckedRadioButtonId() == R.id.rbIncome) ? 1 : 2;

                //lấy date mới
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                String date = sdf.format(new Date());

                // lấy note mới
                String note = etNote.getText().toString().trim();

                //tạo transaction với các thông tin mới
                Transaction newTx = new Transaction(_id, amount, type, category, date, note);
                dbHelper.updateTransaction(newTx); // cập nhật

                loadDataFromDatabase();
                dialog.dismiss();
            });
        });

        dialog.show();
    }

    private  void DeleteTransaction(Transaction transaction,  android.app.AlertDialog editDialog) {

        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_delete_transaction, null);
        android.app.AlertDialog dialog = new android.app.AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Xác nhận", null) //BUTTON_POSITIVE
                .setNegativeButton("Hủy", (d, which) -> d.dismiss())
                .create();

        dialog.setCanceledOnTouchOutside(false); // ko cho nhấn bên ngoài để tắt

        // sự kiện nút xóa
        dialog.setOnShowListener(dialogInterface -> {
            Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(v -> {

                //tạo transaction với các thông tin mới

                dbHelper.deleteTransaction(transaction.getId());

                loadDataFromDatabase();

                // ĐÓNG CẢ HAI DIALOG
                dialog.dismiss();

                if (editDialog != null) {
                    editDialog.dismiss();
                }

                Toast.makeText(getContext(), "Đã xóa thành công", Toast.LENGTH_SHORT).show();

            });
        });

        dialog.show();
    }


    /**
     * HÀM QUAN TRỌNG: Lọc dữ liệu kết hợp cả Tháng và Loại giao dịch
     */
    private void filterData() {
        if (spnThang == null || radioGroup == null) return;

        // 1. Lấy tháng đang chọn từ Spinner (giá trị từ 1 đến 12)
        int selectedMonth = spnThang.getSelectedItemPosition() + 1;

        // 2. Lấy loại đang chọn từ RadioGroup
        int checkedId = radioGroup.getCheckedRadioButtonId();

        // 3. Lấy tất cả giao dịch từ DB để lọc trong bộ nhớ (hoặc viết query SQL lọc thẳng trong DB)
        List<Transaction> allTransactions = dbHelper.getAllTransactions();
        List<Transaction> filteredList = new ArrayList<>();

        for (Transaction tx : allTransactions) {
            // Tách chuỗi ngày "dd/MM/yyyy" để lấy tháng
            String[] dateParts = tx.getDate().split("/");

            if (dateParts.length >= 2) {
                int txMonth = Integer.parseInt(dateParts[1]);

                // Kiểm tra điều kiện 1: Đúng tháng đang chọn
                if (txMonth == selectedMonth) {

                    // Kiểm tra điều kiện 2: Đúng loại (Tất cả / Thu / Chi)
                    if (checkedId == R.id.rbAll) {
                        filteredList.add(tx);
                    } else if (checkedId == R.id.rbIncomeOnly && tx.getType() == 1) {
                        filteredList.add(tx);
                    } else if (checkedId == R.id.rbExpenseOnly && tx.getType() == 2) {
                        filteredList.add(tx);
                    }
                }
            }
        }

        // 4. Cập nhật danh sách hiển thị
        transactionList.clear();
        transactionList.addAll(filteredList);
        adapter.notifyDataSetChanged();

        // 5. Kiểm tra trạng thái rỗng
        updateEmptyState(tvEmptyState);
    }

}// end