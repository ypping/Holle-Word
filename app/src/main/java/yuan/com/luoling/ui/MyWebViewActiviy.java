package yuan.com.luoling.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

/**
 * Created by yuan-pc on 2016/05/31.
 */
public class MyWebViewActiviy extends Activity {
    WebView webView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initWebView();
    }


    private void initWebView() {
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(false);
        webView.getSettings().setDisplayZoomControls(false);
        webView.getSettings().setPluginState(WebSettings.PluginState.ON);
        webView.getSettings()
                .setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//		 synCookies(context, mUrl);

        webView.getSettings().setLoadWithOverviewMode(true);
        webView.setVisibility(View.VISIBLE);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.getSettings().setDomStorageEnabled(true);
        // webView.getSettings().setTextSize(WebSettings.TextSize.SMALLER);
        String cacheDirPath = getCacheDir().getAbsolutePath()
                + "/webviewCache";
        // log.i("WebActivity1.initWebView()", "cacheDirPath="+cacheDirPath);
        // 设置数据库缓存路径
        webView.getSettings().setDatabasePath(cacheDirPath);
        // 设置 Application Caches 缓存目录
        webView.getSettings().setAppCachePath(cacheDirPath);
        // 开启 Application Caches 功能
        webView.getSettings().setAppCacheEnabled(true);
        // 自动打开窗口
        // webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,
                                        String contentDisposition, String mimetype,
                                        long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        // 设置Web视图
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                Toast.makeText(getApplication(), "我刷新了数据", Toast.LENGTH_LONG);
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {

                if (dialog == null) {
                    dialog = new Dialog(MyWebViewActiviy.this);
                }
                if (!dialog.isShowing()) {
                    // dialog.show();
                }

                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                super.onPageFinished(view, url);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                //  super.onReceivedError(view, request, error);
                // webView.loadUrl("file:///android_asset/error_icon.png");
                Toast.makeText(MyWebViewActiviy.this, error + "", Toast.LENGTH_LONG).show();
            }

            public void onReceivedError(WebView view, int errorCode,
                                        String description, String failingUrl) {
                // TODO Auto-generated method stub

                //  super.onReceivedError(view, errorCode, description, failingUrl);
                //   webView.loadUrl("file:///android_asset/error_icon.png");
                Toast.makeText(MyWebViewActiviy.this, errorCode + "", Toast.LENGTH_LONG).show();

            }
        });
        // 此方法可以处理webview 在加载时和加载完成时一些操作
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    Dialog dialog;
}
