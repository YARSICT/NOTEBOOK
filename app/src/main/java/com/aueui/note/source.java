/*
Copyright 2018 YARSICT

        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
*/
package com.aueui.note;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;

public class source extends BaseActivity {
    private WebView source_webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            CurrentTheme = savedInstanceState.getInt("CurrentTheme");
            setAllTheme(CurrentTheme);
        }
        if(isTheme().equals("blue")){
          setAllTheme(R.style.BLUE);
        }
        if(isTheme().equals("red")){
            setAllTheme(R.style.RED);
        }
        if(isTheme().equals("green")){
            setAllTheme(R.style.GREEN);
        }
        if(isTheme().equals("orange")){
            setAllTheme(R.style.ORANGE);
        }
        if(isTheme().equals("purple")){
            setAllTheme(R.style.PURPLE);
        }
        setContentView(R.layout.license);
        init();
        source_webview.loadUrl("https://github.com/YARSICT/NOTEBOOK");

    }

    public void init() {
        source_webview = (WebView) findViewById(R.id.license_webview);
    }
}

