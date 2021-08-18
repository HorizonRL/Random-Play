package com.horizon.randomplay.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.horizon.randomplay.Activities.base.BaseActivity;
import com.horizon.randomplay.R;
import com.horizon.randomplay.series.SeriesHolder;
import com.horizon.randomplay.util.FragmentAdapter;
import com.horizon.randomplay.util.SharedData;
import com.horizon.randomplay.util.Vars;

import java.util.Objects;


public class MainActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager2 pager2;
    private FragmentAdapter fragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedData.getInstance(this);
        SeriesHolder.init(this);

        MainActivity cont = this;

        this.tabLayout = findViewById(R.id.tab_layout);
        this.pager2 = findViewById(R.id.view_pager);

        final FragmentManager fm = getSupportFragmentManager();
        this.fragmentAdapter = new FragmentAdapter(fm, getLifecycle());
        this.pager2.setAdapter(this.fragmentAdapter);

        this.tabLayout.addTab(this.tabLayout.newTab().setText(this.fragmentAdapter.getPageTitle(FragmentAdapter.Tabs.HISTORY.getTabNum())));
        this.tabLayout.addTab(this.tabLayout.newTab().setText(this.fragmentAdapter.getPageTitle(FragmentAdapter.Tabs.SERIES.getTabNum())));
        this.tabLayout.addTab(this.tabLayout.newTab().setText(this.fragmentAdapter.getPageTitle(FragmentAdapter.Tabs.MOVIE.getTabNum())));
        this.tabLayout.addTab(this.tabLayout.newTab().setText(this.fragmentAdapter.getPageTitle(FragmentAdapter.Tabs.SETTINGS.getTabNum())));
        this.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        FragmentAdapter.Tabs selectedTab = Vars.currentTab;

        this.pager2.setCurrentItem(selectedTab.getTabNum());
        Objects.requireNonNull(this.tabLayout.getTabAt(selectedTab.getTabNum())).select();

        this.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if ((SharedData.getInstance().getSeriesHandler().getChosen().isEmpty() || SharedData.getInstance().getMovieHandler().getChosen().isEmpty())
                            && tab.getText() != fragmentAdapter.getPageTitle(FragmentAdapter.Tabs.SETTINGS.getTabNum())) {
                    pager2.setCurrentItem(FragmentAdapter.Tabs.SETTINGS.getTabNum());
                    tabLayout.selectTab(tabLayout.getTabAt(FragmentAdapter.Tabs.SETTINGS.getTabNum()));
                    setPopWin(cont, "Note", "You must choose at list one series or a movie!", "Okay", (dialog, which) -> {
                    }).show();
                } else {
                    pager2.setCurrentItem(tab.getPosition());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        this.pager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    @Override
    public void onBackPressed() {
        switch (this.pager2.getCurrentItem()) {
            case 0:
            case 2:
            case 3:
                pager2.setCurrentItem(FragmentAdapter.Tabs.SERIES.getTabNum());
                tabLayout.selectTab(tabLayout.getTabAt(FragmentAdapter.Tabs.SERIES.getTabNum()));
                break;
            case 1:
                exit();
        }
    }
}
