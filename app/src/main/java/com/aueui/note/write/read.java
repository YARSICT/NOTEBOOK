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
package com.aueui.note.write;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.aueui.note.BaseActivity;
import com.aueui.note.MainActivity;
import com.aueui.note.R;
import com.aueui.note.editor;
import com.aueui.note.settings;

import java.util.Date;

public class read extends BaseActivity {
    private TextView read_title;
    private TextView read_context;
    private Button read_edit;
    private Button read_share;
    private TextView read_date;
    String title;
    String context;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        context = intent.getStringExtra("context");
        date = intent.getStringExtra("date");
        read_context = (TextView) findViewById(R.id.read_context);
        read_title = (TextView) findViewById(R.id.read_title);
        read_date = (TextView) findViewById(R.id.read_date);
        read_title.setText(title);
        read_context.setText(context);
        read_date.setText(date);
        read_edit = (Button) findViewById(R.id.edit);
        read_share = (Button) findViewById(R.id.share);
        read_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(read.this, editor.class);
                intent1.putExtra("title", title);
                intent1.putExtra("context", context);
                SharedPreferences.Editor editor = getSharedPreferences("com.aueui.note_preferences", MODE_PRIVATE).edit();
                editor.putString("where", "read");
                editor.apply();
                startActivity(intent1);
                finish();
            }
        });
        read_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareText("分享", title, context);
            }
        });

    }

    private void shareText(String Title, String subject, String content) {
        if (content == null || "".equals(content)) {
            return;
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        if (subject != null && !"".equals(subject)) {
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        }
        intent.putExtra(Intent.EXTRA_TEXT, content);
        if (Title != null && !"".equals(Title)) {
            startActivity(Intent.createChooser(intent, Title));
        } else {
            startActivity(intent);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(read.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
}