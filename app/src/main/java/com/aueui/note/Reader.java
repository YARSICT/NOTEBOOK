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

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aueui.note.utils.StatusBarUtils;
import com.aueui.note.write.notes;

import org.litepal.LitePal;


public class Reader extends BaseActivity {
    private TextView read_title, read_context, read_date;
    private Button read_edit, read_share;
    String title, context, date;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        initIntent();
        initView();
        read_edit.setOnClickListener(v -> {
            Intent intent1 = new Intent(Reader.this, Editor.class);
            intent1.putExtra("title", title);
            intent1.putExtra("context", context);
            SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
            editor.putString("where", "read");
            editor.apply();
            startActivity(intent1);
            finish();
        });
        read_share.setOnClickListener(v -> {
            shareText("分享", title, context);
            loadCircularRevealAnim(500, linearLayout);
        });

    }

    private void initIntent() {
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        context = intent.getStringExtra("context");
        date = intent.getStringExtra("date");
    }

    private void initView() {
        read_context = findViewById(R.id.read_context);
        read_title = findViewById(R.id.read_title);
        read_date = findViewById(R.id.read_date);
        read_edit = findViewById(R.id.edit);
        read_share = findViewById(R.id.share);
        linearLayout = findViewById(R.id.read_main);
        read_title.setText(title);
        read_context.setText(context);
        read_date.setText(date);
        StatusBarUtils.StatusBarTransMode(this);
        switch (isBg()) {
            case "blue":
                linearLayout.setBackgroundResource(R.drawable.read_gradient);
                getWindow().setStatusBarColor(getResources().getColor(R.color.read_gradient));
                break;
            case "white":
                read_date.setTextColor(getResources().getColor(R.color.black));
                linearLayout.setBackgroundResource(R.drawable.read_white);
                getWindow().setStatusBarColor(getResources().getColor(R.color.white));
                break;
        }
        read_context.setTextSize(Integer.parseInt(isTextSize()));
        read_context.setTextColor(Color.parseColor(isTextColor()));
        Button Button1 = findViewById(R.id.delete);
        Button1.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(Reader.this);
            builder.setTitle("删除");
            builder.setMessage("确认删除本条记事吗？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                LitePal.deleteAll(notes.class, "notes_context = ? and notes_title = ?", read_context.getText().toString(), read_title.getText().toString());
                Intent intent = new Intent(Reader.this, MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade, R.anim.fade_exit);
            });
            builder.setNegativeButton("取消", (dialog, which) -> {

            });
            builder.show();

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

    public void loadCircularRevealAnim(int dur, View view) {
        Animator animator = ViewAnimationUtils.createCircularReveal(view, view.getWidth(), 0, 0, view.getHeight());
        animator.setDuration(dur);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(Reader.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade, R.anim.fade_exit);
        }
        return false;
    }

    public String isBg() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("read_bg", "");
    }

    public String isTextSize() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("read_textsize", "20");
    }

    public String isTextColor() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        return sharedPreferences.getString("read_textcolor", "#000000");
    }
}