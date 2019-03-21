package com.afei.bat.afeiplayandroid.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.afei.bat.afeiplayandroid.R;
import com.afei.bat.afeiplayandroid.constant.Constant;
import com.afei.bat.afeiplayandroid.ui.base.BaseActivity;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.just.agentweb.AgentWeb;

import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

@Route(path = "/activity/WebContentActivity")
public class WebContentActivity extends BaseActivity {

    @BindView(R.id.activity_webcontent_frame)
    FrameLayout activityWebcontentFrame;
    private String title;
    private String url;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_web_content;
    }

    @Override
    protected void findViewId() {
        ButterKnife.bind(this);
        addActivityInList(this);
        title = getIntent().getStringExtra(Constant.WEB_TITLE);
        url = getIntent().getStringExtra(Constant.WEB_URL);
    }

    @Override
    protected void initToolbar(Toolbar toolbar) {
        toolbar.setTitle("");
    }

    @Override
    protected void initCenterTitle(TextView centerTitle) {
        centerTitle.setText(title);
    }

    @Override
    protected void initData(Bundle bundle) {
        AgentWeb.with(this)
                .setAgentWebParent(activityWebcontentFrame, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_content, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menuShare) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_article_url, getString(R.string.app_name), title, url));
            intent.setType("text/plain");
            startActivity(Intent.createChooser(intent, getString(R.string.share_title)));
        } else if (item.getItemId() == R.id.menuLike) {
            showToast("待开发...");
        } else if (item.getItemId() == R.id.menuBrowser) {
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setData(Uri.parse(url));
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
