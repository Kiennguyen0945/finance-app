package com.example.ltdt5_test2

data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: Int, // 1: Thu nhập (Income), 2: Chi tiêu (Expense)
    val category: String,
    val date: String,
    val note: String
)