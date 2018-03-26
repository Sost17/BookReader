package com.lib.book.bookreader.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lib.book.bookreader.R;

import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;

public class RemotePDFActivity extends AppCompatActivity implements DownloadFile.Listener {

    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;
    PDFPagerAdapter adapter;
    TextView loadpdf;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * 参数初始化
         * */
        init();

        /**
         * 设置图书下载器
         * */
        setDownloadListener(geturl());
    }

    private void init(){
        setContentView(R.layout.activity_readbook);

        root = (LinearLayout) findViewById(R.id.remote_pdf_root);
        loadpdf = findViewById(R.id.loading);
    }

    /**
     * 获得页面跳转传过来的图书链接
     * */
    private String geturl(){
        Intent getintent = getIntent();
        return getintent.getStringExtra("url");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (adapter != null) {
            adapter.close();
        }
    }

    protected void setDownloadListener(String bookurl) {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        remotePDFViewPager = new RemotePDFViewPager(ctx, bookurl, listener);
        remotePDFViewPager.setId(R.id.pdfViewPager);
    }

    /**
     * 更新页面
     * */
    public void updateLayout() {
        root.removeAllViewsInLayout();
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    /**
     * 下载图书成功
     * */
    @Override
    public void onSuccess(String url, String destinationPath) {
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {

    }
}
