package com.afei.bat.afeiplayandroid.ui.adapter;

import android.support.annotation.Nullable;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.StudyList;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/12.
 */

public class StudyAdapter extends BaseQuickAdapter<StudyList.DataBean, BaseViewHolder> {
    public StudyAdapter(@Nullable List<StudyList.DataBean> data) {
        super(R.layout.study_adapter_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StudyList.DataBean item) {
        helper.setText(R.id.study_adapter_item_title, item.getName());
        StringBuffer sb = new StringBuffer();
        for (StudyList.DataBean.ChildrenBean childrenBean : item.getChildren()) {
            sb.append(childrenBean.getName() + "      ");
        }
        helper.setText(R.id.study_adapter_item_title_second, sb.toString());
    }
}
