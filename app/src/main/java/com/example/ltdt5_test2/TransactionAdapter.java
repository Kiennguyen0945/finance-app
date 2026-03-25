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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    private List<Transaction> transactions;
    private OnItemClickListener listener; // Interface để xử lý sự kiện click


    // Interface để xử lý sự kiện click
    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TransactionAdapter(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvCategory;
        public TextView tvDate;
        public TextView tvAmount;

        public ViewHolder(View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvDate = view.findViewById(R.id.tvDate);
            tvAmount = view.findViewById(R.id.tvAmount);
        }

        // Gắn sự kiện click vào item
        public void bind(final Transaction transaction, final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        listener.onItemClick(transaction);
                    }
                }
            });
        }
    }

    // Tạo ViewHolder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    // Gắn dữ liệu vào ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction tx = transactions.get(position);
        holder.tvCategory.setText(tx.getCategory());
        holder.tvDate.setText(tx.getDate());

        NumberFormat formatter = NumberFormat.getInstance(new Locale("vi", "VN"));
        String formattedAmount = formatter.format(tx.getAmount()) + " đ";

        if (tx.getType() == 1) { // Thu
            holder.tvAmount.setText("+" + formattedAmount);
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50"));
        } else { // Chi
            holder.tvAmount.setText("-" + formattedAmount);
            holder.tvAmount.setTextColor(Color.parseColor("#F44336"));
        }

        holder.bind(tx, listener); // Gắn sự kiện click vào item
    }

    // Số lượng item hiển thị
    @Override
    public int getItemCount() {
        return transactions.size();
    }
}
