package com.lza.pad.ui.adapter;

import android.content.Context;
import android.view.View;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.model.LiteratureInfo;

import java.util.List;

/**
 *
 * Created by lansing on 2015/6/4.
 */

public class LiteratureAdapter extends QuickAdapter<LiteratureInfo>{

    public LiteratureAdapter(Context context, int layoutResId, List<LiteratureInfo> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, LiteratureInfo literatureInfo) {
        if ( null != literatureInfo ){
            baseAdapterHelper.setText(R.id.literature_title_txt , literatureInfo.getTitle());
            baseAdapterHelper.setText(R.id.literature_time_txt , literatureInfo.getIntime());
            baseAdapterHelper.setText(R.id.literature_summary_txt , literatureInfo.getContent());

            baseAdapterHelper.setOnClickListener(R.id.literature_open_img, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });

            baseAdapterHelper.setOnClickListener(R.id.literature_delete_img, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
    }
}
