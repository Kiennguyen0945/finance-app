package com.example.ltdt5_test2

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar // <-- Thay đổi ở đây nè!
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Phải import đúng Toolbar của AndroidX thì ở đây nó mới hiểu
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val viewPager = findViewById<ViewPager>(R.id.viewpager)
        val tabLayout = findViewById<TabLayout>(R.id.tabs)

        // Giờ thì setSupportActionBar vô tư, không bị chửi nữa
        setSupportActionBar(toolbar)

        // Khởi tạo Adapter và thêm các Fragment vào
        val adapter = ViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(OneFragment(), "TAB 1")
        adapter.addFragment(TwoFragment(), "TAB 2")
        adapter.addFragment(ThreeFragment(), "TAB 3")

        // Đổ dữ liệu từ Adapter vào ViewPager
        viewPager.adapter = adapter

        // Kết nối TabLayout với ViewPager
        tabLayout.setupWithViewPager(viewPager)
    }
}