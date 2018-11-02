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

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class About extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        if (isTheme().equals("night")) {
            initView();
        }

    }

    private void initView() {
        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.about_layout);
        TextView textView = (TextView) findViewById(R.id.about_name);
        TextView textView1 = (TextView) findViewById(R.id.about_info);
        TextView textView2 = (TextView) findViewById(R.id.about_us);
        constraintLayout.setBackgroundColor(getResources().getColor(R.color.night));
        textView.setTextColor(getResources().getColor(R.color.white));
        textView1.setTextColor(getResources().getColor(R.color.white));
        textView2.setTextColor(getResources().getColor(R.color.white));
    }
}
