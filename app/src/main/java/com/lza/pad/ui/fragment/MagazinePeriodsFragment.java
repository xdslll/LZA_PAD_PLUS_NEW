package com.lza.pad.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import com.lza.pad.R;
import com.lza.pad.db.model.MagazineInfo;
import com.lza.pad.db.model.PadJournalPeriod;
import com.lza.pad.db.model.PadJournalYear;
import com.lza.pad.ui.adapter.MPeriodsExpandableAdapter;
import com.lza.pad.ui.fragment.base.BaseUserFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 期刊期数
 *
 * Created by lansing on 2015/6/11.
 */
public class MagazinePeriodsFragment extends BaseUserFragment {
    // controls
    private ExpandableListView mPeriodsListView = null;

    private View mView = null;

    private List<PadJournalYear> mYearList = null;
    private List<PadJournalPeriod> mPeriodList = null;
    private HashMap<String,ArrayList<PadJournalPeriod>> mPeriodHashMap = null;
    private MPeriodsExpandableAdapter mMPeriodsExpandableAdapter = null;

    public MagazinePeriodsFragment newInstance(MagazineInfo mMagazineInfo){
        MagazinePeriodsFragment fragment = new MagazinePeriodsFragment();

        Bundle mBundle = new Bundle();
        mBundle.putParcelable("magazine_info", mMagazineInfo);
        fragment.setArguments(mBundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        } else {
            mView = inflater.inflate(R.layout.fragment_magazine_periods_layout, container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if( null != getArguments() ){
            MagazineInfo mMagazineInfo = getArguments().getParcelable("magazine_info");
            this.mYearList = mMagazineInfo.getContents_qk_year();
            this.mPeriodList = mMagazineInfo.getContents();

            resetMagazineInfo();
        }

        mPeriodsListView = (ExpandableListView) mView.findViewById(R.id.magazine_periods_list);
        mMPeriodsExpandableAdapter = new MPeriodsExpandableAdapter(mActivity , mYearList , mPeriodHashMap);
        mPeriodsListView.setAdapter(mMPeriodsExpandableAdapter);
    }

    private void resetMagazineInfo(){
        for( PadJournalYear mYear : mYearList ){
            ArrayList<PadJournalPeriod> mYearPeriodList = new ArrayList<PadJournalPeriod>();
            for( PadJournalPeriod mPeriod : mPeriodList){
                if( mYear.getYear().equals(mPeriod.getYear())){
                    mYearPeriodList.add(mPeriod);
                }
            }
            mPeriodHashMap.put(mYear.getYear() , mYearPeriodList);
        }
    }
}
