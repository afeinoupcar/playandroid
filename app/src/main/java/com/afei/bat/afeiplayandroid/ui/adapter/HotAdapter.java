package com.afei.bat.afeiplayandroid.ui.adapter;


import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.bean.HotKey;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by MrLiu on 2018/3/13.
 */

public class HotAdapter extends BaseQuickAdapter<HotKey.DataBean,BaseViewHolder> {
    public HotAdapter() {
        super(R.layout.hot_adapter_item, null);
    }

    @Override
    protected void convert(BaseViewHolder helper, HotKey.DataBean item) {

    }
}
