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
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

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
    }

    @Override
    public int getItemCount() {
        return Math.min(transactions.size(), 5);
    }
}