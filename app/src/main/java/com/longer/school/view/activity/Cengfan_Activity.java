package com.longer.school.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.longer.school.Application;
import com.longer.school.R;
import com.longer.school.utils.PublicTools;
import com.xiaomi.mistatistic.sdk.MiStatInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import butterknife.Bind;
import butterknife.ButterKnife;

public class Cengfan_Activity extends AppCompatActivity {

    public Context context;
    @Bind(R.id.progressBar)
    ProgressBar progressBar;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    WebView webView;
    @Bind(R.id.ll_cengfan)
    LinearLayout llCengfan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Application.theme);
        setContentView(R.layout.activity_cengfan);
        ButterKnife.bind(this);
        toolbar.setTitle("免费会员");
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
        inti();
    }

    public void inti() {
        context = Cengfan_Activity.this;
        setWebView();
        setWebData();
    }

    private void setWebView() {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        webView = new WebView(getApplicationContext());
        webView.setLayoutParams(params);
        llCengfan.addView(webView);

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();
        //启用支持javascript
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件


        webView.addJavascriptInterface(new MyJavaScript(), "HTMLOUT");
    }

    Document doc;

    class MyJavaScript {
        @JavascriptInterface
        public void showHTML(final String html) {
//            Log.d("HTML", html);
            //解析源码，如果发现有帐号提示用户
            doc = Jsoup.parse(html);
            final Elements ele1 = doc.select("div[class=box box2] ul[class=ul2 ulp2]");
//            Log.d("tip","ele" + ele1.toString());
            final String history = ele1.text();
            if (history.length() > 10) {
                llCengfan.post(new Runnable() {
                    @Override
                    public void run() {
                        Snackbar.make(llCengfan, "发现有提取记录是否复制？", Snackbar.LENGTH_INDEFINITE).setAction("复制", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                PublicTools.copy(history);
                            }
                        }).show();
                    }
                });
            }
        }
    }

    final class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //设定加载开始的操作
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            //设定加载结束的操作
            view.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
            progressBar.setVisibility(View.GONE);
            super.onPageFinished(view, url);
        }
    }

    private void setWebData() {
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new MyWebViewClient());
        webView.loadUrl("http://vip.cengfan6.com/y/");
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                toolbar.setSubtitle(title);
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                // TODO Auto-generated method stub
                if (newProgress == 100) {
                    // 网页加载完成
                    Log.d("tip", "加载成功");
                } else {
                    // 加载中
                    progressBar.setProgress(newProgress);
                    Log.d("tip", newProgress + "%");
                }
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.destroy();
            webView = null;
        }
        super.onDestroy();
    }

    //改写物理按键——返回的逻辑
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (webView.canGoBack()) {
                webView.goBack();//返回上一页面
                return true;
            } else {
                System.exit(0);//退出程序
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "免费会员");
    }

    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }
}










