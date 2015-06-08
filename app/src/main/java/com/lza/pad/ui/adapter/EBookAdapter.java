package com.lza.pad.ui.adapter;

import android.content.Context;
import android.view.View;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.lza.pad.R;
import com.lza.pad.db.model.EBookInfo;

import java.util.List;

/**
 *
 * Created by lansing on 2015/6/3.
 */
public class EBookAdapter extends QuickAdapter<EBookInfo>{

    public EBookAdapter(Context context, int layoutResId, List<EBookInfo> data) {
        super(context, layoutResId, data);
    }

    @Override
    protected void convert(BaseAdapterHelper baseAdapterHelper, EBookInfo eBookInfo) {
        if( null != eBookInfo ){
            baseAdapterHelper.setText(R.id.e_book_name_txt , eBookInfo.getName());
            baseAdapterHelper.setText(R.id.e_book_publisher_txt , eBookInfo.getPress());

            baseAdapterHelper.setOnClickListener(R.id.e_book_delete_img, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 相关删除操作
                }
            });
        }
    }
}
