package com.lza.pad.ui.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lza.pad.R;
import com.lza.pad.db.dao.UserDao;
import com.lza.pad.db.model.School;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.db.model.Version;
import com.lza.pad.helper.Settings;
import com.lza.pad.helper.UrlHelper;
import com.lza.pad.ui.base.BaseSlidingActivity;

import lza.com.lza.library.util.Utility;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/8/15.
 */
public class MainActivity extends BaseSlidingActivity {

    SlidingMenu mSlidingMenu;
    User mUser;
    SchoolVersion mSchoolVersion;
    School mSchool;
    Version mVersion;
    String mSchoolBh, mSchoolName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mUser = getIntent().getParcelableExtra(KEY_USER);
        mSchoolVersion = getIntent().getParcelableExtra(KEY_SCHOOL_VERSION);
        if (mSchoolVersion == null) {
            mSchoolBh = getIntent().getStringExtra(KEY_SCHOOL_BH);
            mSchoolName = getIntent().getStringExtra(KEY_SCHOOL_NAME);
        } else {
            mSchool = pickFirst(mSchoolVersion.getSchool_bh());
            mVersion = pickFirst(mSchoolVersion.getVersion_code());
            if (mSchool != null) {
                mSchoolBh = mSchool.getBh();
                mSchoolName = mSchool.getTitle();
            }
        }

        if (!isEmpty(mSchoolName)) {
            setTitle(String.format(getString(R.string.title_main_with_school_name), mSchoolName));
        } else {
            setTitle(R.string.title_main);
        }

        setTheme(R.style.Lza_Main_Theme);
        super.onCreate(savedInstanceState);

        initSlideMenu(R.layout.sliding_menu);
        setContentView(R.layout.main_layout);
        initActionBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (checkSchoolBh()) {
            initView();
        }
    }

    ListView mListModule;
    private void initView() {
        mListModule = (ListView) findViewById(R.id.sliding_module_list);
        requestNewModule();

        if (mUser != null) {
            setText(R.id.main_layout_hello, mUser.toString());
        }
    }

    private void requestNewModule() {
        if (mVersion != null) {
            String url = UrlHelper.getVersionModule(mVersion);
        } else {
            String url = UrlHelper.getSchoolVersion();
        }
    }

    private boolean checkSchoolBh() {
        if (mSchoolVersion != null ||
                !isEmpty(mSchoolBh)) {
            return true;
        } else {
            showRetryDialog();
            return false;
        }
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar == null) return;
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
    }

    private void initSlideMenu(int layoutId) {
        setBehindContentView(layoutId);
        mSlidingMenu = getSlidingMenu();
        mSlidingMenu.setShadowWidthRes(R.dimen.main_sliding_menu_shadow_width);
        mSlidingMenu.setShadowDrawable(R.drawable.shadow);
        mSlidingMenu.setBehindOffsetRes(R.dimen.main_sliding_menu_behind_offset);
        mSlidingMenu.setFadeDegree(0.35f);
        mSlidingMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        mSlidingMenu.setMode(SlidingMenu.LEFT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mSlidingMenu.toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        showQuitDialog();
    }

    private void showQuitDialog() {
        Utility.showDialog(mCtx, R.string.dialog_confirm_title,
                R.string.dialog_confirm_quit,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void showRetryDialog() {
        Utility.showDialog(mCtx, R.string.dialog_confirm_title,
                R.string.dialog_confirm_none_school,
                R.string.dialog_button_confirm,
                R.string.dialog_button_exit,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reselectSchool();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
    }

    private void showSelectSchoolDialog() {
        Utility.showDialog(mCtx, R.string.dialog_confirm_title,
                R.string.dialog_confirm_select_school,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reselectSchool();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private void reselectSchool() {
        Settings.setIfSkipLogin(mCtx, false);
        Settings.removeSchoolBh(mCtx);
        Settings.removeSchoolName(mCtx);
        UserDao userDao = new UserDao(mCtx);
        long effect = userDao.clear();
        log("共删除了：" + effect + "个用户");
        startActivity(new Intent(mCtx, SchoolListActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getSupportMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        if (item.getItemId() == R.id.main_select_school) {
            showSelectSchoolDialog();
        }
        return super.onMenuItemSelected(featureId, item);
    }
}
