package com.lza.pad.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.dao.ModuleDao;
import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.loader.VersionModuleLoader;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.SchoolVersion;
import com.lza.pad.db.model.User;
import com.lza.pad.db.model.VersionModule;
import com.lza.pad.ui.fragment.base.BaseUserFragment;
import com.lza.pad.ui.main.WebViewActivity;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import lza.com.lza.library.util.ToastUtils;
import lza.com.lza.library.util.Utility;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/12/15.
 */
public class MyLibraryFragment extends BaseUserFragment {
    public static final String ACTION_REFRESH_LIST_MENU = "com.lza.action.refresh.list";
    private static final int LOADER_ID = 2;

    // controls
    private View mView = null;
    private ListView mListView = null;

    // data
    private String mModuleTypeIndex = null;
    private SchoolVersion mSchoolVersion = null;
    private User mUser = null;
    private ArrayList<Module> mModuleList = new ArrayList<Module>();
    private ModuleAdapter mModuleAdapter = null;

    private boolean isInit = true;
    private refreshMenuReceiver mRefreshMenuReceiver = new refreshMenuReceiver();

    public  MyLibraryFragment newInstance(String moduleTypeIndex  , SchoolVersion schoolVersion , User mUser){
        MyLibraryFragment mFragment = new MyLibraryFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString("moduleTypeIndex", moduleTypeIndex);
        mBundle.putParcelable("schoolVersion", schoolVersion);
        mBundle.putParcelable("user", mUser);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mActivity.registerReceiver(mRefreshMenuReceiver, new IntentFilter(ACTION_REFRESH_LIST_MENU));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if( null != getArguments() ){
            this.mModuleTypeIndex = getArguments().getString("moduleTypeIndex");
            this.mSchoolVersion = getArguments().getParcelable("schoolVersion");
            this.mUser = getArguments().getParcelable("user");
        }

        if( null != mView ){
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }else{
            mView = inflater.inflate(R.layout.main_layout_menu,container,false);

            mListView = (ListView) mView.findViewById(R.id.main_layout_menu_list);
            mListView.setVisibility(View.VISIBLE);

            createEmptyView();
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        refreshDataFromDB();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mRefreshMenuReceiver);
    }

    private void createEmptyView() {
        TextView emptyText = new TextView(mActivity);
        emptyText.setLayoutParams(new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyText.setVisibility(View.GONE);
        emptyText.setGravity(Gravity.CENTER);
        emptyText.setText(R.string.empty_text_add_new_module);
        ((ViewGroup) mListView.getParent()).addView(emptyText);
        mListView.setEmptyView(emptyText);
    }

    private class MyLibraryModuleLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<VersionModule>> {

        @Override
        public Loader<List<VersionModule>> onCreateLoader(int id, Bundle args) {
            return new VersionModuleLoader(mActivity, "");
        }

        @Override
        public void onLoadFinished(Loader<List<VersionModule>> loader, List<VersionModule> data) {
            if (mModuleList.size() > 0) {
                mModuleList.clear();
            }

            for (VersionModule item : data) {
                if( item.getModule_type().getIndex().equals(mModuleTypeIndex)){
                    mModuleList.add(item.getModule());
                }
            }

            refreshListAdapter();
        }

        @Override
        public void onLoaderReset(Loader<List<VersionModule>> loader) {
            loader.forceLoad();
        }
    }

    private void refreshDataFromDB() {
        if (isInit) {
            mActivity.getSupportLoaderManager().initLoader(LOADER_ID, null, new MyLibraryModuleLoaderCallbacks());
            isInit = false;
        } else {
            mActivity.getSupportLoaderManager().restartLoader(LOADER_ID, null, new MyLibraryModuleLoaderCallbacks());
        }
    }

    private void refreshListAdapter(){
        if( null == mModuleAdapter ){
            mModuleAdapter = new ModuleAdapter(mActivity , R.layout.main_layout_menu_list_item , mModuleList);
            mListView.setAdapter(mModuleAdapter);

            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    showRemoveModuleDialog(mModuleList.get(i), i);
                    return true;
                }
            });

            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Module mModule = mModuleList.get(i);

                    Intent mIntent = null;
                    if( mModule.getType().equals("WEB")){
                        mIntent = new Intent(mActivity, WebViewActivity.class);

                        Bundle mBundle = new Bundle();
                        mBundle.putParcelable("schoolVersion",mSchoolVersion);
                        mBundle.putParcelable("user",mUser);
                        mBundle.putParcelable("module", mModule);

                        mIntent.putExtra("data", mBundle);
                    }else if( mModule.getType().equals("NATIVE")){

                    }else if( mModule.getType().equals("APP")){
                        Intent intent =  mActivity.getPackageManager().getLaunchIntentForPackage(mModule.getParse_path());
                        if( null != intent ){
                            startActivity(intent);
                        }else{
                            Uri uri = Uri.parse(mModule.getUrl());
                            Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                            startActivity(intent1);
                            //ToastUtils.showShort(mActivity,getString(R.string.require_install_apk));
                        }
                    }

                    if( null != mIntent)
                        startActivity(mIntent);
                }
            });
        }else{
            mModuleAdapter.replaceAll(mModuleList);
        }
    }

    private class ModuleAdapter extends QuickAdapter<Module> {

        public ModuleAdapter(Context context, int layoutResId, List<Module> data) {
            super(context, layoutResId, data);
        }

        @Override
        protected void convert(final BaseAdapterHelper helper, Module item) {
            if (item != null) {
                helper.setText(R.id.main_layout_menu_list_name, item.getName());
                getImageHelper().loadImage(item.getIco(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        helper.setImageBitmap(R.id.main_layout_menu_list_img, loadedImage);
                    }
                });
            }
        }
    }

    private void showRemoveModuleDialog(final Module module, final int position) {
        String moduleName = module == null ? "" : module.getName();
        Utility.showDialog(mActivity, getString(R.string.dialog_confirm_title),
                String.format(getString(R.string.dialog_confirm_remove_module), moduleName),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        int effect = ModuleDao.getInstance(mActivity).delete(module);
                        int effect2 = VersionModuleDao.getInstance(mActivity).deleteByModule(module);
                        if (effect == 1 && effect2 == 1) {
                            ToastUtils.showShort(mActivity, R.string.dialog_confirm_remove_success);
                            mModuleList.remove(position);
                            mModuleAdapter.remove(position);
                        } else {
                            ToastUtils.showShort(mActivity, R.string.dialog_confirm_remove_failed);
                        }
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    private class refreshMenuReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_REFRESH_LIST_MENU)) {
                refreshDataFromDB();
            }
        }
    }
}
