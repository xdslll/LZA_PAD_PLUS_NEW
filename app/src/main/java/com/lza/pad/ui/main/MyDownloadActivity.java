package com.lza.pad.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.lza.pad.R;
import com.lza.pad.ui.base.BaseActivity;
import com.lza.pad.ui.fragment.EbookFragment;
import com.lza.pad.ui.fragment.LiteratureFragment;
import com.lza.pad.ui.fragment.MagazineFragment;
import com.lza.pad.ui.widget.PagerSlidingTabStrip;

import java.util.List;

import lza.com.lza.library.util.Utility;

/**
 *
 * Created by lansing on 2015/6/2.
 */
public class MyDownloadActivity extends BaseActivity {
    private final static int E_BOOK_POSITION = 0;
    private final static int MAGAZINE_POSITION = 1;
    private final static int LITERATURE_POSITION = 2;
    private final static int ALL_ACTION_BAR_TAB = 3;

    // actionbar
    private RelativeLayout mActionBarBackLayout = null;
    private TextView mTitleTxt = null;
    private ImageView mBackImg = null;

    private PagerSlidingTabStrip mPagerSlidingTabStrip = null;
    private ViewPager mViewPager = null;
    private ViewPagerAdapter mViewPagerAdapter = null;

    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics mDisplayMetrics = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Lza_Main_Theme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        mDisplayMetrics = getResources().getDisplayMetrics();

        initActionBar();
        initViewPager();
        setTabsValue();
    }

    private void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setCustomView(R.layout.action_bar_title_layout);

        mActionBarBackLayout = (RelativeLayout) findViewById(R.id.action_bar_custom_view_layout);
        mActionBarBackLayout.setBackgroundColor(Color.RED);
        mTitleTxt = (TextView) findViewById(R.id.action_bar_title);
        mTitleTxt.setText(R.string.my_download_title);

        mBackImg = (ImageView) findViewById(R.id.action_bar_back_img);
        mBackImg.setVisibility(View.GONE);
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
        private final String[] titles = { getString(R.string.actionbar_tab_e_book),
                getString(R.string.actionbar_tab_magazine), getString(R.string.actionbar_tab_literature)};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
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

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.main_layout_tab);
        mPagerSlidingTabStrip.setViewPager(mViewPager);

        // 设置Tab是自动填充满屏幕的
        mPagerSlidingTabStrip.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        mPagerSlidingTabStrip.setDividerColor(Color.GRAY);
        // 设置Tab底部线的高度
        mPagerSlidingTabStrip.setUnderlineHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 1, mDisplayMetrics));
        // 设置Tab Indicator的高度
        mPagerSlidingTabStrip.setIndicatorHeight((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, 4, mDisplayMetrics));
        // 设置Tab标题文字的大小
        mPagerSlidingTabStrip.setTextSize((int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_SP, 16, mDisplayMetrics));
        // 设置Tab Indicator的颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#ff33b5e5"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#ff33b5e5"));
        // 取消点击Tab时的背景色
        mPagerSlidingTabStrip.setTabBackground(0);

        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }
}
