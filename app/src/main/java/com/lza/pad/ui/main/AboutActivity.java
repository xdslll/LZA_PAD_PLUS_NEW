package com.lza.pad.ui.main;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.lza.pad.R;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.ui.base.BaseActivity;

import lza.com.lza.library.util.Utility;

/**
 * Created by lansing on 2015/6/1.
 */
public class AboutActivity extends BaseActivity {

    // controls
    private ImageView mIconImg = null;
    private TextView mContentTxt = null;
    private TextView mVersionInfoTxt = null;
    private TextView mCopyrightOwnerTxt = null;
    private TextView mCompanyTxt = null;

    // data
    private SchoolVersion mSchoolVersion = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSchoolVersion = getIntent().getBundleExtra("data").getParcelable("schoolVersion");

        initActionBar();
        initView();
        initData();
    }

    private void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        log(" mActionBar = " + mActionBar);
        if( null == mActionBar )
            return;

        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle(R.string.setting_about);

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        setContentView(R.layout.activity_about_layout);

        mIconImg = (ImageView) findViewById(R.id.about_icon_img);
        mContentTxt = (TextView) findViewById(R.id.about_content_txt);
        mVersionInfoTxt = (TextView) findViewById(R.id.about_version_info_txt);
        mCopyrightOwnerTxt = (TextView) findViewById(R.id.about_copyright_owner_txt);
        mCompanyTxt = (TextView) findViewById(R.id.about_company_txt);
    }

    private void initData(){
        mContentTxt.setText(Utility.getApplicationName(this));
        mVersionInfoTxt.setText(Utility.getVersionName(this));
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            AboutActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
