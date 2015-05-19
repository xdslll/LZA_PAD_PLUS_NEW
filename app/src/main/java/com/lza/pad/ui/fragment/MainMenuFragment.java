package com.lza.pad.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.dao.ModuleDao;
import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.loader.VersionModuleLoader;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.VersionModule;
import com.lza.pad.ui.fragment.base.BaseUserFragment;
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
public class MainMenuFragment extends BaseUserFragment {

    GridView mGrid;
    ModuleAdapter mAdapter;
    List<Module> mModules = new ArrayList<Module>();

    RefreshMenuReceiver mRefreshMenuReceiver = new RefreshMenuReceiver();
    public static final String ACTION_REFRESH_MENU = "com.lza.action.refresh.menu";

    boolean mIsInit = true;
    private static final int LOADER_ID = 1;

    View mView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity.registerReceiver(mRefreshMenuReceiver, new IntentFilter(ACTION_REFRESH_MENU));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(mRefreshMenuReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.main_layout_menu, container, false);
            mGrid = (GridView) mView.findViewById(R.id.main_layout_menu_grid);
            createEmptyView();
        }
        return mView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refreshFromDB();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void createEmptyView() {
        TextView emptyText = new TextView(mActivity);
        emptyText.setLayoutParams(new ActionBar.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        emptyText.setVisibility(View.GONE);
        emptyText.setGravity(Gravity.CENTER);
        emptyText.setText(R.string.empty_text_add_new_module);
        ((ViewGroup) mGrid.getParent()).addView(emptyText);
        mGrid.setEmptyView(emptyText);
    }

    private class ModuleLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<VersionModule>> {

        @Override
        public Loader<List<VersionModule>> onCreateLoader(int id, Bundle args) {
            return new VersionModuleLoader(mActivity, "");
        }

        @Override
        public void onLoadFinished(Loader<List<VersionModule>> loader, List<VersionModule> data) {
            if (mModules.size() > 0) {
                mModules.clear();
            }
            for (VersionModule item : data) {
                mModules.add(item.getModule());
            }
            refreshAdapter();
        }

        @Override
        public void onLoaderReset(Loader<List<VersionModule>> loader) {
            loader.forceLoad();
        }
    }

    private void refreshFromDB() {
        if (mIsInit) {
            mActivity.getSupportLoaderManager().initLoader(LOADER_ID, null, new ModuleLoaderCallbacks());
            mIsInit = false;
        } else {
            mActivity.getSupportLoaderManager().restartLoader(LOADER_ID, null, new ModuleLoaderCallbacks());
        }
    }

    private void refreshAdapter() {
        if (mAdapter == null) {
            mAdapter = new ModuleAdapter(mActivity, R.layout.main_layout_menu_item, mModules);
            mGrid.setAdapter(mAdapter);
            mGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    showRemoveModuleDialog(mModules.get(position), position);
                    return true;
                }
            });
            mGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        } else {
            mAdapter.replaceAll(mModules);
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
                    mModules.remove(position);
                    mAdapter.remove(position);
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

    private class ModuleAdapter extends QuickAdapter<Module> {

        public ModuleAdapter(Context context, int layoutResId, List<Module> data) {
            super(context, layoutResId, data);
        }

        @Override
        protected void convert(final BaseAdapterHelper helper, Module item) {
            if (item != null) {
                helper.setText(R.id.main_layout_menu_item_text, item.getName());
                getImageHelper().loadImage(item.getIco(), new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        helper.setImageBitmap(R.id.main_layout_menu_item_img, loadedImage);
                    }
                });
            }
        }
    }


    private class RefreshMenuReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ACTION_REFRESH_MENU)) {
                refreshFromDB();
            }
        }
    }
}
