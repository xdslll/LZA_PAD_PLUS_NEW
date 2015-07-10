package com.lza.pad.ui.main;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.dao.ModuleDao;
import com.lza.pad.db.dao.ModuleTypeDao;
import com.lza.pad.db.dao.UserDao;
import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.model.ConfigGroup;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.ModuleType;
import com.lza.pad.db.model.PadResource;
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
import com.lza.pad.ui.adapter.MySlidingExpandableAdapter;
import com.lza.pad.ui.base.BaseSlidingActivity;
import com.lza.pad.ui.fragment.MainMenuFragment;
import com.lza.pad.ui.fragment.MyLibraryFragment;
import com.lza.pad.ui.fragment.SettingsFragment;
import com.lza.pad.ui.widget.PagerSlidingTabStrip;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
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

    // actionbar
    private RelativeLayout mActionBarBackLayout = null;
    private TextView mTitleTxt = null;
    private ImageView mBackImg = null;

    SlidingMenu mSlidingMenu;
    User mUser;
    SchoolVersion mSchoolVersion;
    School mSchool;
    Version mVersion;
    List<VersionModule> mNormalVersionModules = new ArrayList<VersionModule>();
    VersionModule mMyLibModule;

    String mSchoolBh, mSchoolName;

    //ListView mListModule;
    TextView mTxtLoginStatus, mTxtMenuRefresh;
    ViewPager mViewPager;

    SlidingLayoutAdapter mSlidingAdapter;

    MenuPagerAdapter mMenuAdapterPager;

    List<Fragment> mMenuFragments;


    // add by lfj

    // ExpandableListView parent list
    private ArrayList<ModuleType> mModuleTypeParentList = new ArrayList<ModuleType>();
    // ExpandableListView children list
    private HashMap<String, ArrayList<VersionModule>> mModuleTypeChildMap = new HashMap<String, ArrayList<VersionModule>>();

    private ExpandableListView mModuleExpandableListView = null;

    private PagerSlidingTabStrip mPagerSlidingTabStrip = null;
    private List<String> mTabTitleList = new ArrayList<String>();
    /**
     * 获取当前屏幕的密度
     */
    private DisplayMetrics mDisplayMetrics = null;

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

        mDisplayMetrics = getResources().getDisplayMetrics();

        initSlideMenu(R.layout.sliding_menu);
        setContentView(R.layout.main_layout);
        //initActionBar();

        if (checkSchoolBh()) {
            initView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        //refreshMenu();

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

        //mListModule = (ListView) findViewById(R.id.sliding_menu_list);
        mModuleExpandableListView = (ExpandableListView) findViewById(R.id.sliding_menu_expandable_list);

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
                log("Module url = " + url);
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
//        if (mSlidingAdapter != null) {
//            mSlidingAdapter.replaceAll(mNormalVersionModules);
//        } else {
//            mSlidingAdapter = new SlidingLayoutAdapter(mCtx, R.layout.sliding_menu_item, mNormalVersionModules);
//            mListModule.setAdapter(mSlidingAdapter);
//            mListModule.setOnScrollListener(getImageHelper().getScrollListener());
//            mSlidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
//                @Override
//                public void onClose() {
//                    refreshServiceMenu();
//                }
//            });
//            mSlidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
//                @Override
//                public void onOpen() {
//                    loadSlidingLayout();
//                }
//            });
//        }

        MySlidingExpandableAdapter mMySlidingExpandableAdapter = new MySlidingExpandableAdapter(mCtx, mModuleTypeParentList, mModuleTypeChildMap,new mHandler(MainActivity.this));
        mModuleExpandableListView.setAdapter(mMySlidingExpandableAdapter);
        mModuleExpandableListView.expandGroup(0);


        mSlidingMenu.setOnCloseListener(new SlidingMenu.OnCloseListener() {
            @Override
            public void onClose() {
                //refreshServiceMenu();
                //refreshLibraryMenu();
            }
        });
        mSlidingMenu.setOnOpenListener(new SlidingMenu.OnOpenListener() {
            @Override
            public void onOpen() {
                //loadSlidingLayout();
            }
        });
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
     * 刷新MyLibraryList
     */
    private void refreshLibraryMenu() {
        Intent mIntent = new Intent();
        mIntent.setAction(MyLibraryFragment.ACTION_REFRESH_LIST_MENU);
        sendBroadcast(mIntent);
    }

    /**
     * 刷新所有的菜单
     */
    private void refreshMenu() {
        mMenuFragments = new ArrayList<Fragment>();

//        mMenuFragments.add(new MainMenuFragment());
//        mMenuFragments.add(new MyLibraryFragment());

        // ------------ change by lfj --------------------
        for (ModuleType type : mModuleTypeParentList) {
            mTabTitleList.add(type.getName());

            if (type.getDisplay_mode().equals("GRID")) {
                mMenuFragments.add(new MainMenuFragment().newInstance(type.getIndex() , mSchoolVersion , mUser));
            } else if (type.getDisplay_mode().equals("LIST")) {
                mMenuFragments.add(new MyLibraryFragment().newInstance(type.getIndex() , mSchoolVersion , mUser));
            }
        }
        mTabTitleList.add(getString(R.string.actionbar_tab_settings));
        mMenuFragments.add(new SettingsFragment().newInstance(mSchoolVersion ,mUser));
        // ------------ change by lfj --------------------

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

            if (null != mModuleTypeParentList) {
                mModuleTypeParentList.clear();
            }

            if (null != mModuleTypeChildMap) {
                mModuleTypeChildMap.clear();
            }
            for (VersionModule data : content) {
//                if (data.getType().equals("")) {
//                    mNormalVersionModules.add(data);
//                } else if (data.getType().equals("")) {
//                    mMyLibModule = data;
//                }
//
//                if( !index.equals("0")){
//                    mNormalVersionModules.add(data);
//                }

                // -------- changed by lfj ----------------
                // 数据解析
                String index = data.getType().get(0).getIndex();
                if (!data.getType().get(0).getIndex().equals("0")) {
                    // ExpandableListView parentList 添加成员
                    if (!checkModuleTypeInfo(index)) {
                        mModuleTypeParentList.add(data.getType().get(0));
                        // 数据保存到数据库
                        ModuleTypeDao.getInstance(mCtx).createOrUpdate(data.getType().get(0));

                        mModuleTypeChildMap.put(index, new ArrayList<VersionModule>());
                    }

                    // ExpandableListView childMap 添加成员
                    mModuleTypeChildMap.get(index).add(data);
                }
                // -------- changed by lfj ----------------
            }
            loadSlidingLayout();
            // -------- changed by lfj ----------------
            initActionBar();
            refreshMenu();
            setTabsValue();
            // -------- changed by lfj ----------------
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

        //actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        //actionBar.setDisplayOptions(0, android.app.ActionBar.DISPLAY_SHOW_TITLE | android.app.ActionBar.DISPLAY_SHOW_HOME);


        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.action_bar_title_layout);

        mActionBarBackLayout = (RelativeLayout) findViewById(R.id.action_bar_custom_view_layout);
        mActionBarBackLayout.setBackgroundColor(Color.RED);
        mTitleTxt = (TextView) findViewById(R.id.action_bar_title);
        mTitleTxt.setText(Utility.getApplicationName(this));

        mBackImg = (ImageView) findViewById(R.id.action_bar_back_img);
        mBackImg.setVisibility(View.GONE);

//        actionBar.addTab(actionBar.newTab()
//                .setText(R.string.actionbar_tab_lib_service)
//                .setTabListener(new ActionBarTabListener()));
//        actionBar.addTab(actionBar.newTab()
//                .setText(R.string.actionbar_tab_my_lib)
//                .setTabListener(new ActionBarTabListener()));

        // -------- change by lfj ----------------
//        actionBar.removeAllTabs();
//
//        for (ModuleType type : mModuleTypeParentList) {
//            actionBar.addTab(actionBar.newTab().setText(type.getName()).setTabListener(new ActionBarTabListener()));
//        }
//        actionBar.addTab(actionBar.newTab()
//                .setText(R.string.actionbar_tab_settings)
//                .setTabListener(new ActionBarTabListener()));
        // -------- change by lfj ----------------
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
    public static final int SCANNIN_GREQUEST_CODE = 102;

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
        } else if( item.getItemId() == R.id.main_scan){
            Intent intent = new Intent();
            intent.setClass(this, MipcaActivityCapture.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
            final ModuleType moduleType = pickFirst(item.getType());

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
                            item.setModule_type(moduleType);

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
        public CharSequence getPageTitle(int position) {
            return mTabTitleList.get(position);
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

    // ----------- add by lfj ---------------------
    // 判断需要添加的模块类型是否已经添加到相关列表
    private boolean checkModuleTypeInfo(String mIndex) {
        for (ModuleType type : mModuleTypeParentList) {
            if (mIndex.equals(type.getIndex())) {
                return true;
            }
        }
        return false;
    }

    private static class mHandler extends Handler {
        private WeakReference<MainActivity> mWeakActivity;

        public mHandler(MainActivity activity) {
            mWeakActivity = new WeakReference<MainActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            MainActivity activity = mWeakActivity.get();
            if( msg.what == 1 ){
                String mModuleTypeIndex = msg.getData().getString("moduleTypeIndex");
                if( mModuleTypeIndex.equals("1")){
                    activity.refreshServiceMenu();
                }else if( mModuleTypeIndex.equals("2") ){
                    activity.refreshLibraryMenu();
                }
            }
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
                TypedValue.COMPLEX_UNIT_SP, 14, mDisplayMetrics));
        // 设置Tab Indicator的颜色
        mPagerSlidingTabStrip.setIndicatorColor(Color.parseColor("#ff33b5e5"));
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        mPagerSlidingTabStrip.setSelectedTextColor(Color.parseColor("#ff33b5e5"));
        // 取消点击Tab时的背景色
        mPagerSlidingTabStrip.setTabBackground(0);

        mPagerSlidingTabStrip.setTextColor(Color.BLACK);
    }
}
