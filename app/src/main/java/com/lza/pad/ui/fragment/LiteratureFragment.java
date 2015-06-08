package com.lza.pad.ui.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lza.pad.R;
import com.lza.pad.db.model.LiteratureInfo;
import com.lza.pad.ui.adapter.LiteratureAdapter;
import com.lza.pad.ui.adapter.MagazineAdapter;
import com.lza.pad.ui.fragment.base.BaseUserFragment;
import com.lza.pad.ui.zrc.widget.SimpleFooter;
import com.lza.pad.ui.zrc.widget.SimpleHeader;
import com.lza.pad.ui.zrc.widget.ZrcListView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by lansing on 2015/6/4.
 */
public class LiteratureFragment extends BaseUserFragment{
    // controls
    private ZrcListView mZrcListView = null;

    // define
    private Handler handler = null;
    private View mView = null;
    private LiteratureAdapter mLiteratureAdapter = null;

    private List<LiteratureInfo> mLiteratureList = new ArrayList<LiteratureInfo>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.fragment_my_download_layout, container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initView();
        initData();
    }

    private void initView(){
        handler = new Handler();

        mZrcListView = (ZrcListView) mView.findViewById(R.id.e_book_zrc_list_view);

        // 设置下拉刷新的样式（可选，但如果没有Header则无法下拉刷新）
        SimpleHeader header = new SimpleHeader(mActivity);
        header.setTextColor(0xff0066aa);
        header.setCircleColor(0xff33bbee);
        mZrcListView.setHeadable(header);

        // 设置加载更多的样式（可选）
        SimpleFooter footer = new SimpleFooter(mActivity);
        footer.setCircleColor(0xff33bbee);
        mZrcListView.setFootable(footer);

        // 设置列表项出现动画（可选）
        mZrcListView.setItemAnimForTopIn(R.anim.topitem_in);
        mZrcListView.setItemAnimForBottomIn(R.anim.bottomitem_in);

        // 下拉刷新事件回调（可选）
        mZrcListView.setOnRefreshStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                refresh();
            }
        });

        // 加载更多事件回调（可选）
        mZrcListView.setOnLoadMoreStartListener(new ZrcListView.OnStartListener() {
            @Override
            public void onStart() {
                loadMore();
            }
        });

        mZrcListView.setOnItemClickListener(new ZrcListView.OnItemClickListener() {
            @Override
            public void onItemClick(ZrcListView parent, View view, int position, long id) {

            }
        });
    }

    private void refresh(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // +++ 添加刷新操作
                // --- 添加刷新操作
                mZrcListView.setRefreshSuccess();
                mZrcListView.startLoadMore();
            }
        }, 2000);
    }

    private void loadMore(){
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 对是否为最后一页进行判断，若不是最后一页，添加加载下一页操作
                mZrcListView.stopLoadMore();
            }
        }, 2000);
    }

    private void initData(){
        mLiteratureList.clear();
        LiteratureInfo mLiteratureInfo = new LiteratureInfo();
        mLiteratureInfo.setTitle("放牧强度和地形对蒙古典型草原物种多度分布的影响");
        mLiteratureInfo.setIntime("2015年06月02日   06:09:24");
        mLiteratureInfo.setContent("放牧强度和地形对蒙古典型草原物种多度分布的影响");
        mLiteratureList.add(mLiteratureInfo);

        mLiteratureAdapter = new LiteratureAdapter(mActivity , R.layout.item_literature_layout , mLiteratureList);
        mZrcListView.setAdapter(mLiteratureAdapter);
    }
}
