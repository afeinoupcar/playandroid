package com.afei.bat.afeiplayandroid.ui.adapter;

import android.text.TextUtils;
import android.view.View;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.CollectList;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import androidx.annotation.Nullable;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class ILikeAdapter extends BaseQuickAdapter<CollectList.DataBean.DatasBean, BaseViewHolder> {


    public ILikeAdapter(@Nullable List<CollectList.DataBean.DatasBean> data) {
        super(R.layout.home_adapter_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, CollectList.DataBean.DatasBean item) {
        helper.setText(R.id.home_adapter_item_author, item.getAuthor())
                .setText(R.id.home_adapter_item_title, item.getTitle())
                .setText(R.id.home_adapter_item_date, item.getNiceDate())
                .setText(R.id.home_adapter_item_name, item.getChapterName())
                .setText(R.id.home_adapter_item_desc, item.getDesc());
        helper.setImageResource(R.id.home_adapter_item_collection, R.drawable.ic_action_like);
        helper.addOnClickListener(R.id.home_adapter_item_name);
        helper.addOnClickListener(R.id.home_adapter_item_collection);
        if (TextUtils.isEmpty(item.getDesc())) {
            helper.getView(R.id.home_adapter_item_desc).setVisibility(View.GONE);
        }
    }
}
