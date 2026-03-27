package com.example.ltdt5_test2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    // Lưu toàn bộ các fragment (màn hình). index 0 -> OneFragment. index 1 -> TwoFragment
    private final List<Fragment> fragmentList = new ArrayList<>();
    // Lưu tên tab tương ứng -> dùng cho thằng layout hiển thị
    private final List<String> fragmentTitleList = new ArrayList<>();
    // Nếu không có dòng này  Fragment không hiển thị vẫn sẽ chạy, gây lag. Nếu có chỉ fragment đang hiển thị sẽ hoạt động
        public ViewPagerAdapter(@NonNull FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }
    // NonNull tức là hàm này phải luôn trả về result khác null
    @NonNull
    @Override
    // Đây là chỗ thằng viewpager gọi tới. Cho tao vị trí ở position = 1 . Adpater trả  fragmentList.get(1)
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    // Viewpager hỏi cái này có bao nhiêu trang, adpater trả 3 trang
    public int getCount() {
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        fragmentList.add(fragment);
        fragmentTitleList.add(title);
    }
    // Tức là hàm này trâ vè giá trị null vẫn được
    @Nullable
    @Override
    // Tab layout gọi cái này để hiển thị title
    public CharSequence getPageTitle(int position) {
        return fragmentTitleList.get(position);
    }
}