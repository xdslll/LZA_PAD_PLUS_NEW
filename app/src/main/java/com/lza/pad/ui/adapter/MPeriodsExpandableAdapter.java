package com.lza.pad.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.lza.pad.R;
import com.lza.pad.db.model.PadJournalPeriod;
import com.lza.pad.db.model.PadJournalYear;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 期刊期数 ExpandableListView Adapter
 *
 * Created by lansing on 2015/6/11.
 */
public class MPeriodsExpandableAdapter extends BaseExpandableListAdapter {

    private Context mContext = null;

    private List<PadJournalYear> mYearList = null;
    private HashMap<String,ArrayList<PadJournalPeriod>> mPeriodHashMap = null;

    public MPeriodsExpandableAdapter ( Context mContext ,List<PadJournalYear> mYearList,
                                       HashMap<String,ArrayList<PadJournalPeriod>> mPeriodHashMap ){
        this.mContext  = mContext;
        this.mYearList = mYearList;
        this.mPeriodHashMap = mPeriodHashMap;
    }

    @Override
    public int getGroupCount() {
        return mYearList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mPeriodHashMap.get(mYearList.get(i)).size();
    }

    @Override
    public Object getGroup(int i) {
        return mYearList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mPeriodHashMap.get(mYearList.get(i)).get(i1);
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
            view = LayoutInflater.from(mContext).inflate(R.layout.item_magazine_period_layout, null);

            mGroupHolder = new groupViewHolder();
            mGroupHolder.mGroupTxt = (TextView) view.findViewById(R.id.item_magazine_period_text);
            view.setTag(mGroupHolder);
        } else {
            mGroupHolder = (groupViewHolder) view.getTag();
        }

        mGroupHolder.mGroupTxt.setText(mYearList.get(i).getYear());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        childViewHolder mChildHolder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_magazine_period_layout, null);

            mChildHolder = new childViewHolder();
            mChildHolder.mChildTxt = (TextView) view.findViewById(R.id.item_magazine_period_text);
            view.setTag(mChildHolder);
        } else {
            mChildHolder = (childViewHolder) view.getTag();
        }

        mChildHolder.mChildTxt.setText(mPeriodHashMap.get(mYearList.get(i)).get(i1).getQi());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

    public class groupViewHolder {
        TextView mGroupTxt;
    }

    public class childViewHolder {
        TextView mChildTxt;
    }
}
