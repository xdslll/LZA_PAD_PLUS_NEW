package com.lza.pad.ui.main;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.lza.pad.R;
import com.lza.pad.ui.base.BaseActivity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lza.com.lza.library.util.ToastUtils;

/**
 * Created by lansing on 2015/6/1.
 */
public class FeedbackActivity extends BaseActivity implements View.OnClickListener{

    // controls
    private EditText mTextEnterEdit = null;
    private EditText mEmailEnterEdit = null;
    private ImageView mSubmitImg = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initActionBar();
        initView();
    }

    private void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        log(" mActionBar = " + mActionBar);
        if( null == mActionBar )
            return;

        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle(R.string.setting_feedback);

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void initView(){
        setContentView(R.layout.activity_feedback_layout);

        mTextEnterEdit = (EditText) findViewById(R.id.feed_back_text_enter);
        mEmailEnterEdit = (EditText) findViewById(R.id.feed_back_email_enter);

        mSubmitImg = (ImageView) findViewById(R.id.feed_back_submit_img);
        mSubmitImg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if( view.getId() == R.id.feed_back_submit_img ){
            if( mTextEnterEdit.length() < 1 ){
                ToastUtils.showShort(this, R.string.no_version_update);
            }else if( mEmailEnterEdit.length() < 1 ){
                ToastUtils.showShort(this, R.string.no_version_update);
            }else if( !isEmail(mEmailEnterEdit.getText().toString())){
                ToastUtils.showShort(this, R.string.error_email_address);
            }else{
                // 提交相关反馈信息
            }
        }
    }

    // 判断邮箱格式是否正确
    public boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            FeedbackActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
