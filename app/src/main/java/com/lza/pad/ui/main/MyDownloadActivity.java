package com.lza.pad.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;

import com.actionbarsherlock.app.ActionBar;
import com.lza.pad.R;
import com.lza.pad.ui.base.BaseActivity;
import com.lza.pad.ui.fragment.EbookFragment;
import com.lza.pad.ui.fragment.LiteratureFragment;
import com.lza.pad.ui.fragment.MagazineFragment;

/**
 *
 * Created by lansing on 2015/6/2.
 */
public class MyDownloadActivity extends BaseActivity {
    private final static int E_BOOK_POSITION = 0;
    private final static int MAGAZINE_POSITION = 1;
    private final static int LITERATURE_POSITION = 2;
    private final static int ALL_ACTION_BAR_TAB = 3;

    private ViewPager mViewPager = null;
    private ViewPagerAdapter mViewPagerAdapter = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Lza_Main_Theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        initActionBar();
        initViewPager();
    }

    private void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        mActionBar.setHomeButtonEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        mActionBar.setDisplayShowHomeEnabled(false);

        mActionBar.addTab(mActionBar.newTab()
                .setText(R.string.actionbar_tab_e_book)
                .setTabListener(new ActionBarTabListener()));
        mActionBar.addTab(mActionBar.newTab()
                .setText(R.string.actionbar_tab_magazine)
                .setTabListener(new ActionBarTabListener()));
        mActionBar.addTab(mActionBar.newTab()
                .setText(R.string.actionbar_tab_literature)
                .setTabListener(new ActionBarTabListener()));
    }

    private class ActionBarTabListener implements ActionBar.TabListener {

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            int position = tab.getPosition();
            if (mViewPager != null) {
                mViewPager.setCurrentItem(position, true);
            }
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    }

    private void initViewPager(){
        mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.main_layout_viewpager);
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                ActionBar bar = getSupportActionBar();
                bar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        });
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if( position == E_BOOK_POSITION ){
                return new EbookFragment();
            }else if( position == MAGAZINE_POSITION ){
                return new MagazineFragment();
            }else if (position == LITERATURE_POSITION){
                return new LiteratureFragment();
            }
            return null;
        }

        @Override
        public int getCount() {
            return ALL_ACTION_BAR_TAB;
        }
    }
}
