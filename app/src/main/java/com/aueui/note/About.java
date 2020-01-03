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
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.Objects;

public class About extends BaseActivity {
    android.support.v7.widget.Toolbar about_toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        toolbarcolor = R.color.pure_white;
        setContentView(R.layout.about);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        about_toolbar = findViewById(R.id.about_toolbar);
        setSupportActionBar(about_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        initToolbar();
        getFragmentManager()
                .beginTransaction()
                .commit();

        if (isTheme().equals("night")) {

            initView();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;


    }
    private void initToolbar() {
        about_toolbar.setTitle("");
        setSupportActionBar(about_toolbar);

    }

    private void initView() {
        ConstraintLayout constraintLayout = findViewById(R.id.about_layout);
        TextView textView = findViewById(R.id.about_name);
        TextView textView1 = findViewById(R.id.about_link);
        TextView textView2 = findViewById(R.id.about_us);
        TextView textView3 = findViewById(R.id.toolbar_title);
        TextView textView4 = findViewById(R.id.about_info);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.night));
        textView.setTextColor(getResources().getColor(R.color.white));
        textView1.setTextColor(getResources().getColor(R.color.white));
        textView2.setTextColor(getResources().getColor(R.color.white));
        textView3.setTextColor(getResources().getColor(R.color.white));
        textView4.setTextColor(getResources().getColor(R.color.white));
        getWindow().setStatusBarColor(getResources().getColor(R.color.night));
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(About.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade,R.anim.fade_exit);
        }
        return false;
    }
}
