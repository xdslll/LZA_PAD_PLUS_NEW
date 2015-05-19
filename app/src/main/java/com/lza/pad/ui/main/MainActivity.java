package com.lza.pad.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.dao.Dao;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.dao.ModuleDao;
import com.lza.pad.db.dao.UserDao;
import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.model.ConfigGroup;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.db.model.School;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.db.model.Version;
import com.lza.pad.db.model.VersionModule;
import com.lza.pad.handler.SimpleHttpResponseHandler;
import com.lza.pad.helper.JsonParseHelper;
import com.lza.pad.helper.Settings;
import com.lza.pad.helper.UrlHelper;
import com.lza.pad.ui.base.BaseSlidingActivity;
import com.lza.pad.ui.fragment.MainMenuFragment;
import com.lza.pad.ui.fragment.MyLibraryFragment;
import com.lza.pad.ui.fragment.SettingsFragment;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import lza.com.lza.library.db.DatabaseTools;
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
    List<VersionModule> mNormalVersionModules = new ArrayList<VersionModule>();
    VersionModule mMyLibModule;
    String mSchoolBh, mSchoolName;

    ListView mListModule;
    TextView mTxtLoginStatus, mTxtMenuRefresh;
    ViewPager mViewPager;

    SlidingLayoutAdapter mSlidingAdapter;

    MenuPagerAdapter mMenuAdapterPager;

    List<Fragment> mMenuFragments;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mUser = getIntent().getParcelableExtra(KEY_USER);
        mSchoolVersion = getIntent().getParcelableExtra(KEY_SCHOOL_VERSION);
        if (mSchoolVersion == null) {
            mSchoolBh = getIntent().getStringExtra(KEY_SCHOOL_BH);
            mSchoolName = getIntent().getStringExtra(KEY_SCHOOL_NAME);
            if (isEmpty(mSchoolBh)) {
                mSchoolBh = Settings.getSchoolBh(mCtx);
            }
            if (isEmpty(mSchoolName)) {
                mSchoolName = Settings.getSchoolName(mCtx);
            }
        } else {
            parseSchoolVersion();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private void parseSchoolVersion() {
        mSchool = pickFirst(mSchoolVersion.getSchool_bh());
        mVersion = pickFirst(mSchoolVersion.getVersion_code());
        if (mSchool != null) {
            mSchoolBh = mSchool.getBh();
            mSchoolName = mSchool.getTitle();
        }
    }

    private void initView() {
        mViewPager = (ViewPager) findViewById(R.id.main_layout_viewpager);
        refreshMenu();

        mTxtLoginStatus = (TextView) findViewById(R.id.sliding_menu_login_status);
        if (mUser != null) {
            mTxtLoginStatus.setText(String.format(
                    getString(R.string.login_status_ok), mUser.getUsername()));
        } else {
            mTxtLoginStatus.setText(getString(R.string.login_status_none));
        }
        mTxtLoginStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoginDialog();
            }
        });

        mListModule = (ListView) findViewById(R.id.sliding_menu_list);
        mTxtMenuRefresh = (TextView) findViewById(R.id.sliding_menu_refresh);
        mTxtMenuRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestNewModule();
            }
        });
        requestNewModule();
    }

    private void requestNewModule() {
        mTxtMenuRefresh.setEnabled(false);
        showLoadingView();
        if (mSchoolVersion != null) {
            if (mVersion != null) {
                String url = UrlHelper.getAllVersionModule(mVersion);
                send(url, new VersionModuleHandler());
            } else {
                showSchoolIllegalDialog();
            }
        } else {
            String url = UrlHelper.getSchoolVersionByBh(mSchoolBh);
            send(url, new SchoolVersionHandler());
        }
    }

    private void loadSlidingLayout() {
        if (mSlidingAdapter != null) {
            mSlidingAdapter.replaceAll(mNormalVersionModules);
        } else {
            mSlidingAdapter = new SlidingLayoutAdapter(mCtx, R.layout.sliding_menu_item, mNormalVersionModules);
            mListModule.setAdapter(mSlidingAdapter);
            mListModule.setOnScrollListener(getImageHelper().getScrollListener());
            mSlidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
                @Override
                public void onClose() {
                    refreshServiceMenu();
                }
            });
            mSlidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
                @Override
                public void onOpen() {
                    loadSlidingLayout();
                }
            });
        }
    }

    /**
     * 只刷新服务菜单
     */
    private void refreshServiceMenu() {
        Intent intent = new Intent();
        intent.setAction(MainMenuFragment.ACTION_REFRESH_MENU);
        sendBroadcast(intent);
    }

    /**
     * 刷新所有的菜单
     */
    private void refreshMenu() {
        mMenuFragments = new ArrayList<Fragment>();
        mMenuFragments.add(new MainMenuFragment());
        mMenuFragments.add(new MyLibraryFragment());
        mMenuFragments.add(new SettingsFragment());
        mMenuAdapterPager = new MenuPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mMenuAdapterPager);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ActionBar bar = getSupportActionBar();
                bar.setSelectedNavigationItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private class VersionModuleHandler extends SimpleHttpResponseHandler<VersionModule> {

        @Override
        public ResponseData<VersionModule> parseJson(String json) {
            return JsonParseHelper.parseVersionModule(json);
        }

        @Override
        public void handleRespone(List<VersionModule> content) {
            dismissLoadingView();
            if (mNormalVersionModules.size() > 0) {
                mNormalVersionModules.clear();
            }
            for (VersionModule data : content) {
                if (data.getType().equals("")) {
                    mNormalVersionModules.add(data);
                } else if (data.getType().equals("")) {
                    mMyLibModule = data;
                }
            }
            loadSlidingLayout();
            mTxtMenuRefresh.setEnabled(true);
        }

        @Override
        public void handleResponseFailed(Object... obj) {
            dismissLoadingView();
            mTxtMenuRefresh.setEnabled(true);
            showRefreshMenuDialog();
        }
    }

    private class SchoolVersionHandler extends SimpleHttpResponseHandler<SchoolVersion> {

        @Override
        public ResponseData<SchoolVersion> parseJson(String json) {
            return JsonParseHelper.parseSchoolVersion(json);
        }

        @Override
        public void handleRespone(List<SchoolVersion> content) {
            dismissLoadingView();
            mSchoolVersion = pickFirst(content);
            parseSchoolVersion();
            requestNewModule();
        }

        @Override
        public void handleResponseFailed(Object... obj) {
            dismissLoadingView();
            mTxtMenuRefresh.setEnabled(true);
            handleErrorProcess(R.string.dialog_request_failed_title,
                    R.string.school_list_request_failed_message,
                    new Runnable() {
                        @Override
                        public void run() {
                            requestNewModule();
                        }
                    });
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
        /*actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        if (!isEmpty(mSchoolName)) {
            actionBar.setTitle(String.format(getString(R.string.title_main_with_school_name), mSchoolName));
        } else {
            actionBar.setTitle(R.string.title_main);
        }*/

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayOptions(0, android.app.ActionBar.DISPLAY_SHOW_TITLE | android.app.ActionBar.DISPLAY_SHOW_HOME);
        actionBar.addTab(actionBar.newTab()
                .setText(R.string.actionbar_tab_lib_service)
                .setTabListener(new ActionBarTabListener()));
        actionBar.addTab(actionBar.newTab()
                .setText(R.string.actionbar_tab_my_lib)
                .setTabListener(new ActionBarTabListener()));
        actionBar.addTab(actionBar.newTab()
                .setText(R.string.actionbar_tab_settings)
                .setTabListener(new ActionBarTabListener()));
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
    public void onBackPressed() {
        showQuitDialog();
    }

    public static final int REQUEST_LOGIN = 101;
    private void jumpToLogin() {
        Intent intent = new Intent(mCtx, LoginActivity.class);
        if (mSchoolVersion != null) {
            intent.putExtra(KEY_SCHOOL_VERSION, mSchoolVersion);
        } else if (!isEmpty(mSchoolBh)) {
            intent.putExtra(KEY_SCHOOL_BH, mSchoolBh);
            if (!isEmpty(mSchoolName)) {
                intent.putExtra(KEY_SCHOOL_NAME, mSchoolName);
            }
        }
        intent.putExtra(KEY_LOGIN_USAGE, CUSTOM_LOGIN);
        startActivityForResult(intent, REQUEST_LOGIN);
    }

    private void clearLoginUser() {
        UserDao userDao = UserDao.getInstance(mCtx);
        long effect = userDao.clear();
        log("共删除了：" + effect + "个用户");
        mUser = null;
    }

    private void reselectSchool() {
        Settings.setIfSkipLogin(mCtx, false);
        Settings.removeSchoolBh(mCtx);
        Settings.removeSchoolName(mCtx);
        clearLoginUser();
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
        } else if (item.getItemId() == R.id.main_login) {
            showLoginDialog();
        } else if (item.getItemId() == R.id.main_quit) {
            showQuitDialog();
        } else if (item.getItemId() == R.id.main_add_module) {
            mSlidingMenu.toggle(true);
        }
        return super.onMenuItemSelected(featureId, item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mSlidingMenu.toggle();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_LOGIN &&
                resultCode == RESULT_OK) {
            if (data != null) {
                mUser = data.getParcelableExtra(KEY_USER);
            }
        }
    }

    private class SlidingLayoutAdapter extends QuickAdapter<VersionModule> {

        public SlidingLayoutAdapter(Context context, int layoutResId, List<VersionModule> data) {
            super(context, layoutResId, data);
        }

        private boolean isSubscribe(String id) {
            return ModuleDao.getInstance(mCtx).isExists(id);
        }

        @Override
        protected void convert(final BaseAdapterHelper helper, final VersionModule item) {
            final Module module = pickFirst(item.getModule_id());
            if (module != null) {
                String name = module.getName();
                String ico = module.getIco();
                String ico2 = module.getIco2();
                helper.setText(R.id.sliding_menu_text, name);
                getImageHelper().loadImage(ico, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        helper.setImageBitmap(R.id.sliding_menu_img, loadedImage);
                    }
                });
                if (isSubscribe(module.getId())) {
                    helper.setText(R.id.sliding_menu_subscribe, getString(R.string.menu_unsubscribe));
                } else {
                    helper.setText(R.id.sliding_menu_subscribe, getString(R.string.menu_subscribe));
                }
                helper.setOnClickListener(R.id.sliding_menu_subscribe, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isSubscribe(module.getId())) {
                            int effect = ModuleDao.getInstance(mCtx).delete(module);
                            int effect2 = VersionModuleDao.getInstance(mCtx).delete(item);
                            if (effect == 1 && effect2 == 1) {
                                helper.setText(R.id.sliding_menu_subscribe, getString(R.string.menu_subscribe));
                            }
                        } else {
                            item.setModule(module);
                            ConfigGroup configGroup = pickFirst(item.getConfig_group_id());
                            if (configGroup != null) {
                                item.setConfig_group(configGroup);
                            }

                            Dao.CreateOrUpdateStatus versionModuleStatus = VersionModuleDao.getInstance(mCtx).createOrUpdate(item);
                            Dao.CreateOrUpdateStatus moduleStatus = ModuleDao.getInstance(mCtx).createOrUpdate(module);
                            if (DatabaseTools.isCreateOrUpdateSuccess(versionModuleStatus) &&
                                    DatabaseTools.isCreateOrUpdateSuccess(moduleStatus)) {
                                helper.setText(R.id.sliding_menu_subscribe, getString(R.string.menu_unsubscribe));
                            } else {
                                helper.setText(R.id.sliding_menu_subscribe, getString(R.string.menu_subscribe));
                            }
                        }
                    }
                });
            }
        }
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

    private void showLoginDialog() {
        if (mUser != null) {
            Utility.showDialog(mCtx, R.string.dialog_confirm_title,
                    R.string.dialog_confirm_relogin,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Settings.setIfSkipLogin(mCtx, false);
                            clearLoginUser();
                            jumpToLogin();
                        }
                    },
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
        } else {
            Settings.setIfSkipLogin(mCtx, false);
            jumpToLogin();
        }
    }

    private void showSchoolIllegalDialog() {
        Utility.showDialog(mCtx, R.string.dialog_prompt_title,
                R.string.dialog_none_school_version,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        reselectSchool();
                    }
                });
    }

    private void showRefreshMenuDialog() {
        Utility.showDialog(mCtx,
                R.string.dialog_request_failed_title,
                R.string.module_list_request_failed_message,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        requestNewModule();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
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

    private class MenuPagerAdapter extends FragmentPagerAdapter {

        public MenuPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mMenuFragments.get(position);
        }

        @Override
        public int getCount() {
            return mMenuFragments.size();
        }

    }

}
