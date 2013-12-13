package com.odong.client;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

/**
 * Created by flamen on 13-12-9.
 */
public class VideoItemA extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.video);

        WebView wv = (WebView) findViewById(R.id.videoView);

        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setLoadsImagesAutomatically(true);
        //settings.setPluginState(WebSettings.PluginState.ON);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        wv.setWebChromeClient(new WebChromeClient());

        wv.loadUrl("http://player.youku.com/embed/XMTk1NjY1NzA4");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        WebView wv = (WebView) findViewById(R.id.videoView);
        wv.onResume();
    }
}