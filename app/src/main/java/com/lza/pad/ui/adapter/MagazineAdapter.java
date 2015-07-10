package com.lza.pad.ui.adapter;

import android.content.Context;
import android.view.View;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.model.MagazineInfo;

import java.util.List;

/**
 *
 * Created by lansing on 2015/6/4.
 */
public class MagazineAdapter extends QuickAdapter<MagazineInfo>{

    public MagazineAdapter(Context context, int layoutResId, List<MagazineInfo> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, MagazineInfo magazineInfo) {
        if ( null != magazineInfo ){
//            baseAdapterHelper.setText(R.id.magazine_title_txt , magazineInfo.getTitle());
//            baseAdapterHelper.setText(R.id.magazine_host_unit_txt , magazineInfo.getCompany());
//            baseAdapterHelper.setText(R.id.magazine_issn_txt , magazineInfo.getIssn());

            baseAdapterHelper.setOnClickListener(R.id.magazine_delete_img, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 删除相关操作
                }
            });
        }
    }
}
