package com.lza.pad.ui.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lza.pad.R;
import com.lza.pad.db.dao.UserDao;
import com.lza.pad.db.model.NewVersionInfo;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.db.model.Version;
import com.lza.pad.handler.SimpleHttpResponseHandler;
import com.lza.pad.helper.JsonParseHelper;
import com.lza.pad.helper.UrlHelper;
import com.lza.pad.ui.fragment.base.BaseUserFragment;
import com.lza.pad.ui.main.AboutActivity;
import com.lza.pad.ui.main.BeginnerGuideActivity;
import com.lza.pad.ui.main.FeedbackActivity;
import com.lza.pad.ui.main.LoginActivity;
import com.lza.pad.ui.main.SchoolListActivity;
import com.lza.pad.ui.widget.MySlipSwitch;
import com.lza.pad.ui.widget.MySlipSwitch.OnSwitchListener;

import java.io.File;
import java.util.List;

import lza.com.lza.library.download.DownloadHelper;
import lza.com.lza.library.download.DownloadHelper.DownloadInfo;
import lza.com.lza.library.file.FileManager;
import lza.com.lza.library.util.ToastUtils;
import lza.com.lza.library.util.Utility;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/12/15.
 */
public class SettingsFragment extends BaseUserFragment implements View.OnClickListener {
    // controls
    private MySlipSwitch mMySlipSwitch = null;
    private RelativeLayout mClearCacheLayout = null;
    private TextView mCacheSizeTxt = null;
    private RelativeLayout mBeginnerGuideLayout = null;
    private RelativeLayout mFeedbackLayout = null;
    private RelativeLayout mAboutLayout = null;
    private RelativeLayout mVersionCheckLayout = null;
    private TextView mVersionInfoTxt = null;
    private ImageView mVersionRightImg = null;
    private ProgressBar mVersionDownloadPro = null;
    private RelativeLayout mLogoutLayout = null;
    private TextView mLogoutUserInfoTxt = null;

    private View mView = null;

    // data
    private SchoolVersion mSchoolVersion = null;
    private User mUser = null;

    private DownloadHelper mDownloadHelper = null;
    private SharedPreferences mySharedPreferences = null;

    public SettingsFragment newInstance(SchoolVersion schoolVersion, User mUser) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle mBundle = new Bundle();
        mBundle.putParcelable("schoolVersion", schoolVersion);
        mBundle.putParcelable("user", mUser);
        fragment.setArguments(mBundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDownloadHelper = DownloadHelper.getInstance(mActivity);
        mySharedPreferences = mActivity.getSharedPreferences("setting", 0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (null != getArguments()) {
            this.mSchoolVersion = getArguments().getParcelable("schoolVersion");
            this.mUser = getArguments().getParcelable("user");
        }

        if (null != mView) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.fragment_setting_layout, container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    private void initData() {
        boolean isForbidMobileNet = mySharedPreferences.getBoolean("isForbidMobileNet", false);
        mMySlipSwitch.updateSwitchState(isForbidMobileNet);

        mCacheSizeTxt.setText(FileManager.getCacheSize(mActivity));
        mVersionInfoTxt.setText(getString(R.string.setting_current_version_info) + Utility.getVersionName(mActivity));

        if( null != mUser ){
            mLogoutUserInfoTxt.setText(getString(R.string.setting_logout_txt) + mUser.getUsername());
        }
    }

    private void initView() {
        mMySlipSwitch = (MySlipSwitch) mView.findViewById(R.id.setting_mobile_forbid_slip_switch);
        mMySlipSwitch.setImageResource(R.mipmap.setting_switch_on,
                R.mipmap.setting_switch_off, R.mipmap.setting_switch_btn);
        mMySlipSwitch.setOnSwitchListener(new onTouchImpl());

        mClearCacheLayout = (RelativeLayout) mView.findViewById(R.id.setting_clear_cache_layout);
        mClearCacheLayout.setOnClickListener(this);
        mCacheSizeTxt = (TextView) mView.findViewById(R.id.setting_cache_size);

        mBeginnerGuideLayout = (RelativeLayout) mView.findViewById(R.id.setting_beginner_guide_layout);
        mBeginnerGuideLayout.setOnClickListener(this);

        mFeedbackLayout = (RelativeLayout) mView.findViewById(R.id.setting_feedback_layout);
        mFeedbackLayout.setOnClickListener(this);

        mAboutLayout = (RelativeLayout) mView.findViewById(R.id.setting_about_layout);
        mAboutLayout.setOnClickListener(this);

        mVersionCheckLayout = (RelativeLayout) mView.findViewById(R.id.setting_version_check_layout);
        mVersionCheckLayout.setOnClickListener(this);
        mVersionInfoTxt = (TextView) mView.findViewById(R.id.setting_version_check_version_info);
        mVersionRightImg = (ImageView) mView.findViewById(R.id.setting_version_check_right_img);
        mVersionDownloadPro = (ProgressBar) mView.findViewById(R.id.setting_version_download_progressbar);

        mLogoutLayout = (RelativeLayout) mView.findViewById(R.id.setting_logout_layout);
        mLogoutLayout.setOnClickListener(this);
        mLogoutUserInfoTxt = (TextView) mView.findViewById(R.id.setting_logout_user_info);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.setting_clear_cache_layout) {
            // 确认此方法是否可用
            FileManager.deleteCache(mActivity);
        } else if (view.getId() == R.id.setting_beginner_guide_layout) {
            // 引导页
            Intent mIntent = new Intent(mActivity, BeginnerGuideActivity.class);
            startActivity(mIntent);
        } else if (view.getId() == R.id.setting_feedback_layout) {
            Intent mIntent = new Intent(mActivity, FeedbackActivity.class);
            startActivity(mIntent);
        } else if (view.getId() == R.id.setting_about_layout) {
            Intent mIntent = new Intent(mActivity, AboutActivity.class);

            Bundle mBundle = new Bundle();
            mBundle.putParcelable("schoolVersion", mSchoolVersion);

            mIntent.putExtra("data", mBundle);

            startActivity(mIntent);
        } else if (view.getId() == R.id.setting_version_check_layout) {
            requestVersionInfo();
        } else if (view.getId() == R.id.setting_logout_layout) {
            // 安全退出相关操作
            showLogoutDialog();
        }
    }


    class onTouchImpl implements OnSwitchListener {
        public onTouchImpl() {
        }

        @Override
        public void onSwitched(boolean isSwitchOn) {
            SharedPreferences.Editor mEditor = mySharedPreferences.edit();
            mEditor.putBoolean("isForbidMobileNet", isSwitchOn);
            mEditor.commit();
        }
    }

    private void requestVersionInfo() {

        log("downloadNewVersion sdUrl = " + FileManager.getSdCardPath(mActivity));

        String url = UrlHelper.getUpdateVersionInfo(pickFirst(mSchoolVersion.getVersion_code()).getCode());
        log(" url = " + url);
        send(url, new getUpdateVersionInfoHandler());
    }

    private class getUpdateVersionInfoHandler extends SimpleHttpResponseHandler<NewVersionInfo> {
        @Override
        public ResponseData<NewVersionInfo> parseJson(String json) {
            return JsonParseHelper.parseNewVersionInfo(json);
        }

        @Override
        public void handleRespone(List<NewVersionInfo> content) {
            NewVersionInfo newVersion = pickFirst(content);

            int oldVersionCode = Utility.getVersionCode(mActivity);
            int newVersionCode = Integer.valueOf(pickFirst(newVersion.getUpdate_id()).getVersion_code());

            if (newVersionCode == oldVersionCode  ) {
                ToastUtils.showShort(mActivity, R.string.no_version_update);
            } else {
                showUpdateConfirmDialog(newVersion);
            }
        }

        @Override
        public void handleResponseFailed(Object... obj) {
        }
    }

    private void showUpdateConfirmDialog(final NewVersionInfo newVersionInfo) {
        Utility.showDialog(mActivity, R.string.dialog_prompt_title, R.string.has_version_update,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 下载最新版本
                        // ---- need to add ----
                        downloadNewVersion(newVersionInfo);
                    }
                });
    }

    private void downloadNewVersion(NewVersionInfo newVersionInfo) {
        mVersionRightImg.setVisibility(View.GONE);
        mVersionDownloadPro.setVisibility(View.VISIBLE);

        String url = pickFirst(newVersionInfo.getUpdate_id()).getUrl();

        final String saveUrl = FileManager.getApkDirPath(mActivity) + url.split("//")[2];
        log("downloadNewVersion sdUrl = " + FileManager.getSdCardPath(mActivity));
        log("downloadNewVersion saveUrl = " + saveUrl);

        FileManager.createNewFileInSDCard( FileManager.getApkDirPath(mActivity));

        // 若有之前下载的版本apk，删除
        File mDesApk = new File(saveUrl);
        if( mDesApk.exists() ){
            mDesApk.delete();
        }

        DownloadInfo mDownloadInfo = new DownloadInfo(url, saveUrl, new DownloadHelper.OnDownloadListener() {
            @Override
            public void onDownloadComplete(long reference) {
                mVersionRightImg.setVisibility(View.VISIBLE);
                mVersionDownloadPro.setVisibility(View.GONE);

                showDownloadSucDialog(saveUrl);
            }

            @Override
            public void onDownloadProgress(DownloadHelper.DownloadQuery query) {
                mVersionDownloadPro.setProgress(query.getPercent());
            }
        });

        mDownloadHelper.download(mDownloadInfo);
    }

    private void showDownloadSucDialog(final String mApkUrl){
        Utility.showDialog(mActivity,
                R.string.dialog_confirm_title,
                R.string.version_download_suc,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File mFile = new File(mApkUrl);
                        installApk(mFile);
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void showLogoutDialog() {
        Utility.showDialog(mActivity,
                R.string.dialog_confirm_title,
                R.string.confirm_to_logout,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 退出登录现在操作为删除User表中的数据信息
                        UserDao.getInstance(mActivity).delete(mUser);

                        // 返回到登录界面
                        Intent mIntent = new Intent(mActivity , SchoolListActivity.class);
                        startActivity(mIntent);

                        mActivity.finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }
}
