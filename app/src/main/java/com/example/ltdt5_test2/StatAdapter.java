package com.example.ltdt5_test2;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

public class StatAdapter extends RecyclerView.Adapter<StatAdapter.StatViewHolder> {

    public static class StatItem {
        String category;
        double amount;
        int type; // 1 for Income, 2 for Expense

        public StatItem(String category, double amount, int type) {
            this.category = category;
            this.amount = amount;
            this.type = type;
        }
    }

    private List<StatItem> statsList;

    public StatAdapter(List<StatItem> statsList) {
        this.statsList = statsList;
    }

    public void setData(List<StatItem> statsList) {
        this.statsList = statsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stat, parent, false);
        return new StatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatViewHolder holder, int position) {
        StatItem item = statsList.get(position);
        holder.tvCategory.setText(item.category);
        
        DecimalFormat df = new DecimalFormat("#,###");
        String prefix = (item.type == 1) ? "+" : "-";
        holder.tvTotalAmount.setText(prefix + df.format(item.amount) + "đ");
        
        // Đổi màu: xanh cho thu nhập, đỏ cho chi tiêu
        if (item.type == 1) {
            holder.tvTotalAmount.setTextColor(Color.parseColor("#4CAF50")); // Green
        } else {
            holder.tvTotalAmount.setTextColor(Color.parseColor("#F44336")); // Red
        }
    }

    @Override
    public int getItemCount() {
        return statsList == null ? 0 : statsList.size();
    }

    static class StatViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategory, tvTotalAmount;

        public StatViewHolder(@NonNull View view) {
            super(view);
            tvCategory = view.findViewById(R.id.tvCategory);
            tvTotalAmount = view.findViewById(R.id.tvTotalAmount);
        }
    }
}
