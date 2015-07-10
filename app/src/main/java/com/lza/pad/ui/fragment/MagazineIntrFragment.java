package com.lza.pad.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lza.pad.R;
import com.lza.pad.ui.fragment.base.BaseUserFragment;

/**
 *
 * Created by lansing on 2015/6/11.
 */
public class MagazineIntrFragment extends BaseUserFragment{
    // controls
    private TextView mMagzineName = null;
    private TextView mMagazinePress = null;
    private TextView mMagazineIssn = null;
    private TextView mMagazineTime = null;

    private View mView = null;

    private String mName = null;
    private String mPress = null;
    private String mIssn = null;
    private String mTime = null;

    public MagazineIntrFragment newInstance(String mName , String mPress , String mIssn , String mTime) {
        MagazineIntrFragment fragment = new MagazineIntrFragment();

        Bundle mBundle = new Bundle();
        mBundle.putString("name", mName);
        mBundle.putString("press", mPress);
        mBundle.putString("issn", mIssn);
        mBundle.putString("time", mTime);
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
            mView = inflater.inflate(R.layout.fragment_magazine_introduce_layout, container, false);
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if( null != getArguments() ){
            this.mName = getArguments().getString("name");
            this.mPress = getArguments().getString("press");
            this.mIssn = getArguments().getString("issn");
            this.mTime = getArguments().getString("time");
        }

        initView();
        initData();
    }

    private void initView(){
        mMagzineName = (TextView) mView.findViewById(R.id.magazine_introduce_name);
        mMagazinePress = (TextView) mView.findViewById(R.id.magazine_introduce_press);
        mMagazineIssn = (TextView) mView.findViewById(R.id.magazine_introduce_issn);
        mMagazineTime = (TextView) mView.findViewById(R.id.magazine_introduce_time);
    }

    private void initData(){
        mMagzineName.setText(mName);
        mMagazinePress.setText(mPress);
        mMagazineIssn.setText(mIssn);
        mMagazineTime.setText(mTime);
    }
}
