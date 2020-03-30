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
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.Objects;


public class Settings extends BaseActivity {
    android.support.v7.widget.Toolbar setting_toolbar;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        Button btn_back = findViewById(R.id.settings_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Settings.this, MainActivity.class));
            }
        });
        setting_toolbar= findViewById(R.id.settings_toolbar);
        setSupportActionBar(setting_toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);
        initToolbar();
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_fra, new SettingsPreference.SettingsFragment())
                .commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(Settings.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return true;
    }

    private void initToolbar() {
        setting_toolbar.setTitle("");
        setting_toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(setting_toolbar);

    }}
