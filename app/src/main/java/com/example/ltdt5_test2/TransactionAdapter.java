package com.example.ltdt5_test2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
//Luồng dữ liệu Data -> Adapter -> ViewHolder -> RecycleView hiển thị
public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    // Danh sách giao dịch
    private List<Transaction> transactions;
    private OnItemClickListener listener; // Interface để xử lý sự kiện click
    private View.OnLongClickListener longClickListener; // Interface để xử lý sự kiện long click


// 
    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }
    // Gán
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    //Interface để xử lý sự kiện long click
    public interface OnItemLongClickListener {
        void onItemLongClick(Transaction transaction);
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClick) {
        this.longClickListener = (View.OnLongClickListener) longClick;
    }

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }
    // ViewHolder là một item UI trong list
    // Mỗi item có category, date, amount, note
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCategory;
        public TextView tvDate;
        public TextView tvAmount;

        public TextView tvNote; // tui mới thêm ghi chú


        public ViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmount = view.findViewById(R.id.tvAmount);
            tvNote = view.findViewById(R.id.tvNote); // tui mới thêm ghi chú
        }

        // Gắn sự kiện click vào item -> trả đúng về object giao dịch
        public void bind(final Transaction transaction, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(transaction);
                    }
                }
            });

//            //gan su kien long click
//            itemView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View v) {
//                    if (longClickListener != null) {
//                        longClickListener.onItemLongClick(transaction);
//                        return true;
//                    }
//                    return false;
//                }
//            });
        }
    }

    // Tạo ViewHolder
    @NonNull
    @Override
    // Tạo UI cho một cái item -> biến xml item thành view
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    // Gắn dữ liệu vào ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Lấy data tại vị trí
        Transaction tx = transactions.get(position);
        // Gán dữ liệu vô UI
        holder.tvCategory.setText(tx.getCategory());
        holder.tvDate.setText(tx.getDate());
        holder.tvNote.setText(tx.getNote()); // tui mới thêm ghi chú


        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedAmount = formatter.format(tx.getAmount()) + " đ";

        // Phân biệt màu chữ dựa trên loại giao dịch
        if (tx.getType() == 1) { // Thu
            holder.tvAmount.setText("+" + formattedAmount);
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50"));
        } else { // Chi
            holder.tvAmount.setText("-" + formattedAmount);
            holder.tvAmount.setTextColor(Color.parseColor("#F44336"));
        }

        holder.bind(tx, listener);
    }

    // Số lượng item hiển thị
    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
