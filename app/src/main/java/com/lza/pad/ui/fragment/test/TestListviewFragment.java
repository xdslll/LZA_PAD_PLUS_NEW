package com.lza.pad.ui.fragment.test;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.loader.ModuleLoader;
import com.lza.pad.db.model.Module;
import com.lza.pad.ui.fragment.base.BaseUserFragment;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

import lza.com.lza.library.util.ToastUtils;

/**
 * Say something about this class
 *
 * @author xiads
 * @Date 5/13/15.
 */
public class TestListviewFragment extends BaseUserFragment {

    Button mBtnRefresh, mBtnRemove;
    ListView mList;

    List<Module> mModules;
    final int LOADER_ID = 1;
    //ModuleAdapter mAdapter;
    QuickModuleAdapter mAdapter;

    boolean mIsInit = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.test_list, container, false);
        mBtnRefresh = (Button) view.findViewById(R.id.settings_refresh);
        mBtnRemove = (Button) view.findViewById(R.id.settings_remove);
        mList = (ListView) view.findViewById(R.id.settings_list);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        refresh();
        mBtnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });
        mBtnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAdapter != null) {
                    if (mModules.size() > 0) {
                        log("adapter count:" + mAdapter.getCount() + ", module size:" + mModules.size());
                        mAdapter.remove(0);
                        log("adapter count:" + mAdapter.getCount() + ", module size:" + mModules.size());
                    } else {
                        ToastUtils.showLong(mActivity, "列表为空！");
                    }
                } else {
                    ToastUtils.showLong(mActivity, "列表尚未加载！");
                }
            }
        });
    }

    private void refresh() {
        if (mIsInit) {
            mActivity.getSupportLoaderManager().initLoader(LOADER_ID, null, new ModuleLoaderCallbacks());
            mIsInit = false;
        } else {
            mActivity.getSupportLoaderManager().restartLoader(LOADER_ID, null, new ModuleLoaderCallbacks());
        }
    }

    private class ModuleLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Module>> {

        @Override
        public Loader<List<Module>> onCreateLoader(int id, Bundle args) {
            return new ModuleLoader(mActivity);
        }

        @Override
        public void onLoadFinished(Loader<List<Module>> loader, List<Module> data) {
            mModules = data;
            //mAdapter = new ModuleAdapter();
            mAdapter = new QuickModuleAdapter(mActivity, R.layout.main_layout_menu_item, mModules);
            mList.setAdapter(mAdapter);
        }

        @Override
        public void onLoaderReset(Loader<List<Module>> loader) {
            loader.forceLoad();
        }
    }

    private class QuickModuleAdapter extends QuickAdapter<Module> {

        public QuickModuleAdapter(Context context, int layoutResId, List<Module> data) {
            super(context, layoutResId, data);
        }

        @Override
        protected void convert(final BaseAdapterHelper helper, Module item) {
            getImageHelper().loadImage(item.getIco(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    helper.setImageBitmap(R.id.main_layout_menu_item_img, loadedImage);
                }
            });
            helper.setText(R.id.main_layout_menu_item_text, item.getName());
        }
    }

    private class ModuleAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mModules.size();
        }

        @Override
        public Module getItem(int position) {
            return mModules.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                LayoutInflater inflater = mActivity.getLayoutInflater();
                convertView = inflater.inflate(R.layout.main_layout_menu_item, null);
                holder.img = (ImageView) convertView.findViewById(R.id.main_layout_menu_item_img);
                holder.text = (TextView) convertView.findViewById(R.id.main_layout_menu_item_text);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            Module module = getItem(position);
            getImageHelper().loadImage(module.getIco(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    holder.img.setImageBitmap(loadedImage);
                }
            });
            holder.text.setText(module.getName());
            return convertView;
        }
    }

    private class ViewHolder {
        ImageView img;
        TextView text;
    }

}
