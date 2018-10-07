package com.aueui.note;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;

public class source extends AppCompatActivity {
    private WebView source_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license);
        init();
        source_webview.loadUrl("https://github.com/YARSICT/NOTEBOOK");

    }

    public void init() {
        source_webview = (WebView) findViewById(R.id.license_webview);
    }
}

