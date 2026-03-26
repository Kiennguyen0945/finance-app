package com.example.ltdt5_test2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeFragment extends Fragment {

    private DatabaseHelper dbHelper;
    private RecyclerView rvStats;
    private StatAdapter statAdapter;
    private Switch switchDarkMode;
    private Button btnWipeData;
    private SharedPreferences sharedPreferences;

    private static final String PREFS_NAME = "theme_prefs";
    private static final String KEY_IS_DARK_MODE = "isDarkMode";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);

        dbHelper = new DatabaseHelper(requireContext());
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);

        // Ánh xạ View
        rvStats = view.findViewById(R.id.rvStats);
        switchDarkMode = view.findViewById(R.id.switchDarkMode);
        btnWipeData = view.findViewById(R.id.btnWipeData);

        // Setup RecyclerView Thống kê
        rvStats.setLayoutManager(new LinearLayoutManager(getContext()));
        statAdapter = new StatAdapter(new ArrayList<>());
        rvStats.setAdapter(statAdapter);

        loadStatistics();
        setupDarkMode();
        setupWipeData();

        return view;
    }

    private void loadStatistics() {
        List<Transaction> allList = dbHelper.getAllTransactions();
        
        // Key format: "CategoryName|Type"
        Map<String, Double> groupedMap = new HashMap<>();

        for (Transaction tx : allList) {
            String key = tx.getCategory() + "|" + tx.getType();
            double currentAmount = groupedMap.containsKey(key) ? groupedMap.get(key) : 0.0;
            groupedMap.put(key, currentAmount + tx.getAmount());
        }

        List<StatAdapter.StatItem> statsList = new ArrayList<>();
        for (Map.Entry<String, Double> entry : groupedMap.entrySet()) {
            String[] parts = entry.getKey().split("\\|");
            if (parts.length == 2) {
                String category = parts[0];
                int type = Integer.parseInt(parts[1]);
                statsList.add(new StatAdapter.StatItem(category, entry.getValue(), type));
            }
        }

        statAdapter.setData(statsList);
    }

    private void setupDarkMode() {
        boolean isDarkMode = sharedPreferences.getBoolean(KEY_IS_DARK_MODE, false);
        switchDarkMode.setChecked(isDarkMode);

        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(KEY_IS_DARK_MODE, isChecked);
                editor.apply();

                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    private void setupWipeData() {
        btnWipeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Cảnh báo")
                        .setMessage("Bạn có chắc chắn muốn xóa toàn bộ dữ liệu giao dịch không? Hành động này không thể hoàn tác.")
                        .setPositiveButton("Xóa tất cả", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteAllTransactions();
                                loadStatistics();
                                Toast.makeText(getContext(), "Đã xóa toàn bộ dữ liệu", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("Hủy", null)
                        .show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        loadStatistics();
    }
}
