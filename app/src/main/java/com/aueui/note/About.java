/*
 *    Copyright (C)2018 YARSICT IT TEAM
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package com.aueui.note;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class About extends BaseActivity {
    androidx.appcompat.widget.Toolbar about_toolbar;

    private TextView  tv_update, tv_officalWebsite, tv_openSource, tv_originalSource;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR); }
            Button btn_back = findViewById(R.id.about_back);
            btn_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(About.this, MainActivity.class));
                }
            });


            about_toolbar = findViewById(R.id.about_toolbar);
            tv_update = findViewById(R.id.about_update);
            tv_officalWebsite = findViewById(R.id.about_officalwebsite);
            tv_openSource = findViewById(R.id.about_opensource);
            tv_originalSource = findViewById(R.id.about_originalsource);
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri uri = Uri.parse("https://www.coolapk.com/apk/com.aueui.note");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            tv_officalWebsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://aueui.m.icoc.bz/");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            tv_originalSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("https://github.com/aueui");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });
            tv_openSource.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse("http://www.apache.org/licenses/LICENSE-2.0.html");
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }
            });

            getFragmentManager()
                    .beginTransaction()
                    .commit();

            if (isTheme().equals("night")) {

                initView();
            }

        }

    private void initToolbar () {
            about_toolbar.setTitle("");
            setSupportActionBar(about_toolbar);

        }

        private void initView () {
            ConstraintLayout constraintLayout = findViewById(R.id.about_layout);
            TextView textView = findViewById(R.id.about_name);
            TextView textView2 = findViewById(R.id.about_us);
            TextView textView3 = findViewById(R.id.toolbar_title);
            TextView textView4 = findViewById(R.id.about_info);
            Button button = findViewById(R.id.about_back);
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.night));
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            textView.setTextColor(getResources().getColor(R.color.white));
            textView2.setTextColor(getResources().getColor(R.color.white));
            textView3.setTextColor(getResources().getColor(R.color.white));
            textView4.setTextColor(getResources().getColor(R.color.white));
            button.setBackground(getResources().getDrawable(R.drawable.back_white));
            getWindow().setStatusBarColor(getResources().getColor(R.color.night));

        }


    }
