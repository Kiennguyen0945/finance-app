package com.example.ltdt5_test2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ánh xạ View
//        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager); //ViewPaper cho phép vuốt trái phải chuyển màn hình
        TabLayout tabLayout = findViewById(R.id.tabs);

//        setSupportActionBar(toolbar);  // Biến Toolbar thành thanh điều hướng chính

        // Khởi tạo Adapter và gắn mảnh (Fragment) vào
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager()); //getSupportFragmentManager() để quản lý các Fragment

        // add
        adapter.addFragment(new OneFragment(), "Tổng quan");
        adapter.addFragment(new TwoFragment(), "Lịch sử");
        adapter.addFragment(new ThreeFragment(), "Cài đặt");

        // gắn Adapter vào ViewPager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager); // kết nối TabLayout với ViewPager

    }
}