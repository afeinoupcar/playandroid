package com.afei.bat.afeiplayandroid.ui.adapter;

import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.HomeList;
import com.afei.bat.afeiplayandroid.bean.KnowledgeList;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by MrLiu on 2018/3/8.
 */

public class SearchAdapter extends BaseQuickAdapter<HomeList.DataBean.DatasBean, BaseViewHolder> {


    public SearchAdapter(@Nullable List<HomeList.DataBean.DatasBean> data) {
        super(R.layout.home_adapter_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HomeList.DataBean.DatasBean item) {
        helper.setText(R.id.home_adapter_item_author, item.getAuthor())
                .setText(R.id.home_adapter_item_title, Html.fromHtml(item.getTitle()))
                .setText(R.id.home_adapter_item_date, item.getNiceDate())
                .setText(R.id.home_adapter_item_name, item.getChapterName())
                .setText(R.id.home_adapter_item_desc, item.getDesc());
        helper.setImageResource(R.id.home_adapter_item_collection, item.isCollect()
                ? R.drawable.ic_action_like : R.drawable.ic_action_no_like);
        helper.addOnClickListener(R.id.home_adapter_item_name);
        helper.addOnClickListener(R.id.home_adapter_item_collection);
        if (TextUtils.isEmpty(item.getDesc())) {
            helper.getView(R.id.home_adapter_item_desc).setVisibility(View.GONE);
        }
    }
}
