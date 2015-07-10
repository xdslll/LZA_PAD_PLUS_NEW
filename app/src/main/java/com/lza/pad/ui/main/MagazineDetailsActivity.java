package com.lza.pad.ui.main;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.loopj.android.http.TextHttpResponseHandler;
import com.lza.pad.R;
import com.lza.pad.db.model.MagazineInfo;
import com.lza.pad.db.model.PadJournalContent;
import com.lza.pad.db.model.PadJournalPeriod;
import com.lza.pad.db.model.PadJournalYear;
import com.lza.pad.db.model.PadResource;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.handler.SimpleHttpResponseHandler;
import com.lza.pad.helper.JsonParseHelper;
import com.lza.pad.ui.base.BaseActivity;
import com.lza.pad.ui.fragment.MagazineIntrFragment;
import com.lza.pad.ui.fragment.MagazinePeriodsFragment;
import com.lza.pad.ui.widget.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

import lza.com.lza.library.http.HttpUtility;

/**
 *
 * Created by lansing on 2015/6/11.
 */
public class MagazineDetailsActivity extends BaseActivity implements ViewPager.OnClickListener{
    private final static int INTRODUCE_POSITION = 0;
    private final static int PERIODS_POSITION = 1;
    private final static int ALL_ACTION_BAR_TAB = 2;

    // controls
    private ImageView mIconImage = null;
    private TextView mNameTxt = null;
    private PagerSlidingTabStrip mPagerSlidingTabStrip = null;
    private ViewPager mViewPager = null;
    private ImageView mCollectImg = null;

    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics mDisplayMetrics = null;

    // data
    private PadResource mPadResource = null;
    private MagazineInfo mMagazineInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_magazine_details);

        mDisplayMetrics = getResources().getDisplayMetrics();

        mPadResource = getIntent().getParcelableExtra("magazine_info");

        initActionBar();
        initView();
        initData();
        getMagazineInfo();
    }

    private void initView(){
        mIconImage = (ImageView) findViewById(R.id.magazine_details_img);
        mNameTxt = (TextView) findViewById(R.id.magazine_details_name);
        mPagerSlidingTabStrip = (PagerSlidingTabStrip) findViewById(R.id.main_layout_tab);

        mViewPager = (ViewPager) findViewById(R.id.main_layout_viewpager);

        mCollectImg = (ImageView) findViewById(R.id.magazine_details_collect_img);
        mCollectImg.setOnClickListener(this);
    }

    private void initViewPager(){
        ViewPagerAdapter mViewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
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

    private void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        if( null == mActionBar )
            return;

        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle(R.string.magazine_details_page_title);

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initData(){
        if( null != mPadResource ){
            getImageHelper().loadImage(mPadResource.getIco(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mIconImage.setImageBitmap(loadedImage);
                }
            });

            mNameTxt.setText(mPadResource.getTitle());
        }
    }

    /**
     * 对PagerSlidingTabStrip的各项属性进行赋值。
     */
    private void setTabsValue() {
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

    @Override
    public void onClick(View view) {
        if( view.getId() == R.id.magazine_details_collect_img ){
            // 下载期刊
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private final String[] titles = { getString(R.string.magazine_details_text_introduce),
                getString(R.string.magazine_details_text_periods)};

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            if( position == INTRODUCE_POSITION ){
                return new MagazineIntrFragment().newInstance(mPadResource.getTitle() , mPadResource.getPress() ,
                        mPadResource.getIsbn() , mPadResource.getPubdate());
            }else if( position == PERIODS_POSITION ){
                return new MagazinePeriodsFragment().newInstance(mMagazineInfo);
            }
            return null;
        }

        @Override
        public int getCount() {
            return ALL_ACTION_BAR_TAB;
        }
    }

    private void getMagazineInfo(){
        showLoadingView();
        log(" mPadResource.getFulltext() = " + mPadResource.getFulltext());
        send(mPadResource.getFulltext() , new GetMagazineInfoHandler());
    }

    private class GetMagazineInfoHandler extends SimpleHttpResponseHandler<MagazineInfo> {

        @Override
        public ResponseData<MagazineInfo> parseJson(String json) {
            return JsonParseHelper.parseMagazineInfo(json);
        }

        @Override
        public void handleRespone(List<MagazineInfo> content) {
            super.handleRespone(content);
            dismissLoadingView();
            log("MagazineDetailsActivity handleResponse content = " + content);
            if( !isEmpty(content) ){
                mMagazineInfo = pickFirst(content);

                initViewPager();
                setTabsValue();
            }
        }

        @Override
        public void handleResponseFailed(Object... obj) {
            super.handleResponseFailed(obj);
            log("MagazineDetailsActivity handleResponseFailed " );
            dismissLoadingView();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            MagazineDetailsActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
