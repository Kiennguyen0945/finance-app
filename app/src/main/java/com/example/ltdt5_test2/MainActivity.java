package com.example.ltdt5_test2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    // Luồng mở  app đến khi xuất hiện 3 tab
    // Khi mở app hệ điều hành android gọi hàm onCreate đầu tiên -> gọi applySavedTheme để kiểm tra người dùng đang dùng chế độ sáng hay tối
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // ÁP DỤNG THEME TRƯỚC KHI SET CONTENT VIEW
        applySavedTheme();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // ViewPager = cái khung hiển thị
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);
        // Adapter dùng để quản lý danh sách fragment. Adapter = thằng cung cấp nội dung cho cái khung
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(), "Tổng quan");
        adapter.addFragment(new TwoFragment(), "Lịch sử");
        adapter.addFragment(new ThreeFragment(), "Cài đặt");
        // ViewPager không biết hiển thị gì -> nó hỏi thằng adapter trang thứ 1 là gì, trang thứ 0 đâu
        viewPager.setAdapter(adapter);
        // Bấm tab chuyển page, switch page -> dổi tab
        tabLayout.setupWithViewPager(viewPager);
    }

    private void applySavedTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("theme_prefs", Context.MODE_PRIVATE);
        boolean isDarkMode = sharedPreferences.getBoolean("isDarkMode", false);
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
