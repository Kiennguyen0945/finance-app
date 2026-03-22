package com.example.ltdt5_test2

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.NumberFormat
import java.util.Locale

class TransactionAdapter(private val transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvCategory: TextView = view.findViewById(R.id.tvCategory)
        val tvDate: TextView = view.findViewById(R.id.tvDate)
        val tvAmount: TextView = view.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_transaction, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tx = transactions[position]
        holder.tvCategory.text = tx.category
        holder.tvDate.text = tx.date

        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        val formattedAmount = "${formatter.format(tx.amount)} đ"

        if (tx.type == 1) { // Thu
            holder.tvAmount.text = "+$formattedAmount"
            holder.tvAmount.setTextColor(Color.parseColor("#4CAF50"))
        } else { // Chi
            holder.tvAmount.text = "-$formattedAmount"
            holder.tvAmount.setTextColor(Color.parseColor("#F44336"))
        }
    }

    override fun getItemCount(): Int {
        return if (transactions.size > 5) 5 else transactions.size
    }
}