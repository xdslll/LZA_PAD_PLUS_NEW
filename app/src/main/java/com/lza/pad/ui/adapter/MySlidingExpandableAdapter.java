package com.lza.pad.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.j256.ormlite.dao.Dao;
import com.lza.pad.R;
import com.lza.pad.db.dao.ConfigDao;
import com.lza.pad.db.dao.ConfigGroupDao;
import com.lza.pad.db.dao.ConfigsDao;
import com.lza.pad.db.dao.ModuleDao;
import com.lza.pad.db.dao.VersionModuleDao;
import com.lza.pad.db.model.Config;
import com.lza.pad.db.model.ConfigGroup;
import com.lza.pad.db.model.Configs;
import com.lza.pad.db.model.Module;
import com.lza.pad.db.model.ModuleType;
import com.lza.pad.db.model.ResponseData;
import com.lza.pad.db.model.VersionModule;
import com.lza.pad.handler.SimpleHttpResponseHandler;
import com.lza.pad.helper.ImageHelper;
import com.lza.pad.helper.JsonParseHelper;
import com.lza.pad.helper.UrlHelper;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lza.com.lza.library.db.DatabaseTools;
import lza.com.lza.library.http.HttpUtility;

/**
 * Created by lansing on 2015/5/25.
 */
public class MySlidingExpandableAdapter extends BaseExpandableListAdapter {
    private Context mContext = null;
    private ArrayList<ModuleType> mGroupList = null;
    private HashMap<String, ArrayList<VersionModule>> mChildMap = null;
    private Handler mHandler = null;


    // data
    private List<Config> mModuleConfigs = new ArrayList<Config>();

    public MySlidingExpandableAdapter(Context mContext, ArrayList<ModuleType> mGroupList,
                                      HashMap<String, ArrayList<VersionModule>> mChildMap,
                                      Handler mHandler) {
        this.mContext = mContext;
        this.mGroupList = mGroupList;
        this.mChildMap = mChildMap;
        this.mHandler = mHandler;
    }

    private boolean isSubscribe(String id) {
        return ModuleDao.getInstance(mContext).isExists(id);
    }

    @Override
    public int getGroupCount() {
        return mGroupList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mChildMap.get(mGroupList.get(i).getIndex()).size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroupList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mChildMap.get(mGroupList.get(i).getIndex()).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        groupViewHolder mGroupHolder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.sliding_expandablelistview_group, null);

            mGroupHolder = new groupViewHolder();
            mGroupHolder.mCatNameTxt = (TextView) view.findViewById(R.id.sliding_expandable_parent_category_name);
            view.setTag(mGroupHolder);
        } else {
            mGroupHolder = (groupViewHolder) view.getTag();
        }

        mGroupHolder.mCatNameTxt.setText(mGroupList.get(i).getName());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        final childViewHolder mChildViewHolder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.sliding_menu_item, null);

            mChildViewHolder = new childViewHolder();
            mChildViewHolder.mModImg = (ImageView) view.findViewById(R.id.sliding_menu_img);
            mChildViewHolder.mModNameTxt = (TextView) view.findViewById(R.id.sliding_menu_text);
            mChildViewHolder.mSubscribeBtn = (Button) view.findViewById(R.id.sliding_menu_subscribe);

            view.setTag(mChildViewHolder);
        } else {
            mChildViewHolder = (childViewHolder) view.getTag();
        }

        final String mGroupIndex = mGroupList.get(i).getIndex();
        final VersionModule mVersionModule = mChildMap.get(mGroupIndex).get(i1);

        if(mVersionModule.getModule_id().size() > 0 ){
            final Module mModule = mVersionModule.getModule_id().get(0);
            mChildViewHolder.mModNameTxt.setText(mModule.getName());

            getImageHelper().loadImage(mModule.getIco(), new SimpleImageLoadingListener() {
                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    mChildViewHolder.mModImg.setImageBitmap(loadedImage);
                }
            });

            if (isSubscribe(mModule.getId())) {
                mChildViewHolder.mSubscribeBtn.setText(R.string.menu_unsubscribe);
            } else {
                mChildViewHolder.mSubscribeBtn.setText(R.string.menu_subscribe);
            }

            mChildViewHolder.mSubscribeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (isSubscribe(mModule.getId())) {
                        int effect = ModuleDao.getInstance(mContext).delete(mModule);
                        int effect2 = VersionModuleDao.getInstance(mContext).delete(mVersionModule);
                        if (effect == 1 && effect2 == 1) {
                            mChildViewHolder.mSubscribeBtn.setText(R.string.menu_subscribe);
                        }
                    } else {
                        // ------------ 将从网络获取的数据存储到数据库 start ----------------
                        mVersionModule.setModule(mModule);

                        ConfigGroup configGroup = null;
                        if( mVersionModule.getConfig_group_id().size() > 0 ){
                            configGroup = mVersionModule.getConfig_group_id().get(0);
                            if ( null != configGroup ) {
                                mVersionModule.setConfig_group(configGroup);
                                ConfigGroupDao.getInstance(mContext).createOrUpdate(configGroup);

                                requestConfig(configGroup);

                            }
                        }

                        ModuleType mModuleType = null;
                        if( mVersionModule.getType().size() > 0 ){
                            mModuleType = mVersionModule.getType().get(0);
                            if( null != mModuleType ){
                                mVersionModule.setModule_type(mModuleType);
                            }
                        }

                        Dao.CreateOrUpdateStatus versionModuleStatus = VersionModuleDao.getInstance(mContext).createOrUpdate(mVersionModule);
                        Dao.CreateOrUpdateStatus moduleStatus = ModuleDao.getInstance(mContext).createOrUpdate(mModule);

                        // ------------ 将从网络获取的数据存储到数据库 end ----------------

                        if (DatabaseTools.isCreateOrUpdateSuccess(versionModuleStatus) &&
                                DatabaseTools.isCreateOrUpdateSuccess(moduleStatus)) {
                            mChildViewHolder.mSubscribeBtn.setText(R.string.menu_unsubscribe);
                        } else {
                            mChildViewHolder.mSubscribeBtn.setText(R.string.menu_subscribe);
                        }
                    }

                    Message mMsg = new Message();
                    mMsg.what = 1;
                    Bundle mBundle = new Bundle();
                    mBundle.putString("moduleTypeIndex",mGroupIndex);
                    mMsg.setData(mBundle);
                    mHandler.sendMessage(mMsg);
                }
            });
        }

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class groupViewHolder {
        TextView mCatNameTxt;
    }

    public class childViewHolder {
        ImageView mModImg;
        TextView mModNameTxt;
        Button mSubscribeBtn;
    }

    protected ImageHelper getImageHelper() {
        return ImageHelper.getInstance(mContext);
    }

    private void requestConfig(ConfigGroup mConfigGroup) {
        String url = UrlHelper.getConfigs(mConfigGroup);
        //send(url, new LoginConfigHandler());
        HttpUtility httpUtility = new HttpUtility(mContext, HttpUtility.ASYNC_HTTP_CLIENT);
        httpUtility.send(url, new getConfigHandler());
    }

    private class getConfigHandler extends SimpleHttpResponseHandler<Configs> {

        @Override
        public ResponseData<Configs> parseJson(String json) {
            return JsonParseHelper.parseConfigs(json);
        }

        @Override
        public void handleRespone(List<Configs> content) {
            for (Configs configs : content) {
//                if (!isEmpty(configs.getConfig_id())) {
//                    //mLoginConfigs.add(pickFirst(configs.getConfig_id()));
//                }

                ConfigsDao.getInstance(mContext).createOrUpdate(configs);

                if(configs.getConfig_id().size() > 0){
                    mModuleConfigs.add(configs.getConfig_id().get(0));

                    ConfigDao.getInstance(mContext).createOrUpdate(configs.getConfig_id().get(0));
                }
            }
        }

        @Override
        public void handleResponseFailed(Object... obj) {
        }
    }


}
