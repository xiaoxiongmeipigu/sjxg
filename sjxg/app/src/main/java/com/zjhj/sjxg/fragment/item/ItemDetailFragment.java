package com.zjhj.sjxg.fragment.item;


import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.lzy.widget.vertical.VerticalWebView;
import com.zjhj.commom.util.DebugLog;
import com.zjhj.sjxg.R;
import com.zjhj.sjxg.base.BaseActivity;
import com.zjhj.sjxg.base.BaseFrag;
import com.zjhj.sjxg.util.webview.WebViewUtil;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class ItemDetailFragment extends BaseFrag {


    @Bind(R.id.webView)
    VerticalWebView webView;
    @Bind(R.id.progressbar)
    ProgressBar progressbar;
    String url = "";
    public ItemDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_item_detail, container, false);
        ButterKnife.bind(this, view);
        initView();
        initListener();
        return view;
    }

    @JavascriptInterface
    public void initView() {

        WebSettings webSetting = webView.getSettings();


        if(Build.VERSION.SDK_INT >= 19) {
            webSetting.setLoadsImagesAutomatically(true);
        } else {
            webSetting.setLoadsImagesAutomatically(false);
        }

//        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP)
//            webSetting.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);

        webSetting.setAllowFileAccess(true);
        webSetting.setLoadWithOverviewMode(true);
        webSetting.setJavaScriptEnabled(true);
        webSetting.setAllowContentAccess(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setSupportMultipleWindows(true);
        webSetting.setDatabaseEnabled(true);
        webSetting.setAppCacheEnabled(true);
        webSetting.setBuiltInZoomControls(false);
        webSetting .setBlockNetworkImage(false);//解决图片不显示
        webSetting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//LOAD_CACHE_ELSE_NETWORK
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.addJavascriptInterface(new WebViewUtil(getActivity(), webView), "app");

    }

    private void initListener() {

    }

    public void load() {

        progressbar.setVisibility(View.GONE);
//        showLoading();
        webView.loadUrl(url, WebViewUtil.getWebviewHeader());//加载网页webview.loadUrl(linkUrl, WebViewUtil.getWebviewHeader());//加载网页

        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(final WebView view, String url) {

                DebugLog.i("url="+url);

                return WebViewUtil.shouldOverrideUrlLoading((BaseActivity) getActivity(), view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                hideLoading();
                if(!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
            }

            @Override
            public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
                super.onReceivedHttpError(view, request, errorResponse);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
//                super.onReceivedSslError(view, handler, error);
                if (error.getPrimaryError() == SslError.SSL_DATE_INVALID
                        || error.getPrimaryError() == SslError.SSL_EXPIRED
                        || error.getPrimaryError() == SslError.SSL_INVALID
                        || error.getPrimaryError() == SslError.SSL_UNTRUSTED) {

                    handler.proceed();// 接受所有网站的证书

                } else {
                    handler.cancel();
                }
            }
        });

    }

    public void setWebUrl(String url){
        this.url = url;
        DebugLog.i("url===>"+url);
    }

    @Override
    public void goTop() {
        if(null!=webView)
            webView.goTop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        webView.removeAllViews();
        webView.destroy();
        ButterKnife.unbind(this);
    }

}
