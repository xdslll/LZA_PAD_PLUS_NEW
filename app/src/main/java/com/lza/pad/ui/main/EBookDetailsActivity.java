package com.lza.pad.ui.main;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.lza.pad.R;
import com.lza.pad.db.dao.EBookDao;
import com.lza.pad.db.model.EBookInfo;
import com.lza.pad.db.model.PadResource;
import com.lza.pad.ui.base.BaseActivity;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.ObjectInputStream;

import lza.com.lza.library.download.DownloadHelper;
import lza.com.lza.library.file.FileManager;
import lza.com.lza.library.util.ToastUtils;

/**
 *
 * Created by lansing on 2015/6/10.
 */
public class EBookDetailsActivity extends BaseActivity implements View.OnClickListener{
    // controls
    private ImageView mIconImg = null;
    private TextView mNameTxt = null;
    private TextView mAuthorTxt = null;
    private TextView mPublisherTxt = null;
    private TextView mTimeTxt = null;
    private TextView mSummaryTxt = null;
    private Button mDownloadBtn = null;
    private TextView mDownloadPercentTxt = null;
    private ProgressBar mDownloadProgress = null;
    private Button mOpenBtn = null;

    // data
    private boolean isFromScan = false;
    private PadResource mPadResource = null;
    private DownloadHelper mDownloadHelper = null;
    private EBookInfo mEBookInfo = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ebook_details);

        isFromScan = getIntent().getBooleanExtra("isFromScan",true);
        if( isFromScan ){
            mPadResource = getIntent().getParcelableExtra("book_info");
        }else{
            mEBookInfo = getIntent().getParcelableExtra("book_info");
        }

        mDownloadHelper = DownloadHelper.getInstance(this);

        initActionBar();
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initView(){
        mIconImg = (ImageView) findViewById(R.id.e_book_details_img);
        mNameTxt = (TextView) findViewById(R.id.e_book_details_book_name);
        mAuthorTxt = (TextView) findViewById(R.id.e_book_details_author);
        mPublisherTxt = (TextView) findViewById(R.id.e_book_details_publisher);
        mTimeTxt = (TextView) findViewById(R.id.e_book_details_time);
        mSummaryTxt = (TextView) findViewById(R.id.e_book_details_summary);
        mDownloadBtn = (Button) findViewById(R.id.e_book_details_download_book);
        mDownloadBtn.setOnClickListener(this);
        mDownloadPercentTxt = (TextView) findViewById(R.id.e_book_details_download_percent);
        mDownloadProgress = (ProgressBar) findViewById(R.id.e_book_details_download_progressbar);
        mOpenBtn = (Button) findViewById(R.id.e_book_details_open_book);
        mOpenBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if( view.getId() == R.id.e_book_details_download_book ){
            downLoadBook();
        }else if( view.getId() == R.id.e_book_details_open_book ){
            //添加打开书籍
        }
    }

    private void initData(){
        if( isFromScan ){
            resetData();
        }

        getImageHelper().loadImage(mEBookInfo.getImg(), new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                mIconImg.setImageBitmap(loadedImage);
            }
        });

        mNameTxt.setText(mEBookInfo.getName());
        mAuthorTxt.setText(mEBookInfo.getAuthor());
        mPublisherTxt.setText(mEBookInfo.getPress());
        mTimeTxt.setText(mEBookInfo.getPubdate());
        mSummaryTxt.setText(mEBookInfo.getSummary());

        // 判断这本书在数据库中是否已经下载
        if( null != EBookDao.getInstance(this).queryForId(mEBookInfo.getId())){
            mDownloadBtn.setVisibility(View.GONE);
            mOpenBtn.setVisibility(View.VISIBLE);
        }else{
            mDownloadBtn.setVisibility(View.VISIBLE);
            mOpenBtn.setVisibility(View.GONE);
        }
    }

    private void initActionBar(){
        ActionBar mActionBar = getSupportActionBar();
        log(" mActionBar = " + mActionBar);
        if( null == mActionBar )
            return;

        mActionBar.setDisplayShowTitleEnabled(true);
        mActionBar.setTitle(R.string.e_book_page_title);

        mActionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void downLoadBook(){
        mDownloadBtn.setVisibility(View.GONE);
        mDownloadProgress.setVisibility(View.VISIBLE);
        mDownloadPercentTxt.setVisibility(View.VISIBLE);

        String url = mPadResource.getFulltext();
        log("EBookDetailsActivity url = " + url);

        final String saveUrl = FileManager.getEbookDirPath(this) + url.split("//")[2];
        log("EBookDetailsActivity saveUrl = " + saveUrl);

        DownloadHelper.DownloadInfo mDownloadInfo = new DownloadHelper.DownloadInfo(url, saveUrl, new DownloadHelper.OnDownloadListener() {
            @Override
            public void onDownloadComplete(long reference) {
                //下载成功的书籍信息写入数据库
                EBookDao.getInstance(EBookDetailsActivity.this).createOrUpdate(mEBookInfo);

                mOpenBtn.setVisibility(View.VISIBLE);
                mDownloadPercentTxt.setVisibility(View.GONE);
                mDownloadProgress.setVisibility(View.GONE);

                ToastUtils.showShort(EBookDetailsActivity.this, R.string.e_book_download_success);
            }

            @Override
            public void onDownloadProgress(DownloadHelper.DownloadQuery query) {
                mDownloadProgress.setProgress(query.getPercent());
                mDownloadPercentTxt.setText(query.getPercent() + "%");
            }
        });

        mDownloadHelper.download(mDownloadInfo);
    }

    private void resetData(){
        mEBookInfo = new EBookInfo();
        mEBookInfo.setId(mPadResource.getId());
        mEBookInfo.setImg(mPadResource.getIco());
        mEBookInfo.setName(mPadResource.getTitle());
        mEBookInfo.setAuthor(mPadResource.getAuthor());
        mEBookInfo.setPress(mPadResource.getPress());
        mEBookInfo.setPubdate(mPadResource.getPubdate());
        mEBookInfo.setSummary(mPadResource.getAbs());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if( item.getItemId() == android.R.id.home ){
            EBookDetailsActivity.this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
