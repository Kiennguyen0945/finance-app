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

        Toolbar toolbar = findViewById(R.id.toolbar);
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tabs);

        setSupportActionBar(toolbar);

        // Khởi tạo Adapter và gắn mảnh (Fragment) vào
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        
        adapter.addFragment(new OneFragment(), "Tổng quan");
        
        // Đã bỏ dấu // ở 2 dòng dưới để hiện tab
        adapter.addFragment(new TwoFragment(), "Lịch sử");
        adapter.addFragment(new ThreeFragment(), "Cài đặt");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}