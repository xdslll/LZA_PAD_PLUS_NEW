package com.lza.pad.ui.main;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.lza.pad.R;
import com.lza.pad.ui.base.BaseActivity;

/**
 *
 * Created by lansing on 2015/6/1.
 */
public class BeginnerGuideActivity extends BaseActivity{

    // controls
    private ViewPager mViewPager = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_beginner_guide_layout);

        mViewPager = (ViewPager) findViewById(R.id.beginner_guide_viewpager);
    }

}
