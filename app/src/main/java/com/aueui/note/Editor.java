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

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aueui.note.write.notes;

import org.litepal.LitePal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import ren.qinc.edit.PerformEdit;


public class Editor extends BaseActivity {

    private String note_text, note_titles, title, context;
    private EditText editText, editText1;
    private static final String save_success = "保存成功", edit_sucess = "修改成功";
    private PerformEdit performEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow()
                .getDecorView()
                .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        toolbarcolor = R.color.pure_white;
        getWindow().setStatusBarColor(getResources().getColor(R.color.pure_white));
        switch (isTheme()) {
            case "night":
                toolbarcolor = R.color.night;
                getWindow().setStatusBarColor(getResources().getColor(R.color.night));
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
                break;
        }
        setContentView(R.layout.editor);
        LitePal.getDatabase();
        initView();
        initToolbar();
        initEditor();
        switch (isTheme()) {
            case "night":
                editText.setHighlightColor(getResources().getColor(R.color.night_tra));
                editText1.setHighlightColor(getResources().getColor(R.color.night_tra));

                break;
        }
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String where = sharedPreferences.getString("where", "");
        if (where.equals("read")) {
            Intent intent = getIntent();
            title = intent.getStringExtra("title");
            context = intent.getStringExtra("context");
            editText.setText(context);
            editText1.setText(title);
        }
        if (where.equals("MainActivity")) {
            if (!TextUtils.isEmpty(load())) {
                editText.setText(load());
                editText.setSelection(load().length());
            }
            if (!TextUtils.isEmpty(load_title())) {
                editText1.setText(load_title());
                editText1.setSelection(load_title().length());
            }
        }

        if (isTheme().equals("night")) {
            ConstraintLayout constraintLayout = findViewById(R.id.editor);
            Button button = findViewById(R.id.editor_back);
            EditText textView1 = findViewById(R.id.note_title);
            EditText textView2 = findViewById(R.id.note_context);
            constraintLayout.setBackgroundColor(getResources().getColor(R.color.night));
            button.setBackground(getResources().getDrawable(R.drawable.back_white));
            textView1.setTextColor(getResources().getColor(R.color.white));
            textView1.setHintTextColor(getResources().getColor(R.color.editor));
            textView2.setTextColor(getResources().getColor(R.color.white));
            textView2.setHintTextColor(getResources().getColor(R.color.editor));
        }

    }



    private void initView() {
        editText = findViewById(R.id.note_context);
        editText1 = findViewById(R.id.note_title);

        final FloatingActionButton actionButton = findViewById(R.id.editor_reset);
        FloatingActionButton actionButton1 = findViewById(R.id.editor_fab);
        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Editor.this);
                builder.setTitle("清空所有内容");
                builder.setMessage("确认删除所有内容吗");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText("");
                        editText1.setText("");
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();

            }
        });
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    editText.setTextIsSelectable(true);
                    showKeyBoard(Editor.this, editText);
                }
            }
        });
        actionButton1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
                Boolean isexpand = sharedPreferences.getBoolean("fab_expand", false);
                if (!isexpand) {
                    SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("fab_expand", true);
                    editor.apply();
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
                    mShowAction.setDuration(500);
                    actionButton.startAnimation(mShowAction);

                    actionButton.setVisibility(View.VISIBLE);
                } else {
                    SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
                    editor.putBoolean("fab_expand", false);
                    editor.apply();
                    TranslateAnimation mShowAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                            Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                            0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
                    mShowAction.setDuration(500);
                    actionButton.startAnimation(mShowAction);
                    actionButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    public static void showKeyBoard(Context context, View view) {
        if (context == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_FORCED);
    }


    private void initToolbar() {
        Toolbar editor_toolbar = findViewById(R.id.editor_toolbar);

        if (isTheme().equals("night") ){
            editor_toolbar.inflateMenu(R.menu.editor_toolbar_white);
        } else {
            editor_toolbar.inflateMenu(R.menu.editor_toolbar);


}
        Button btn_back = findViewById(R.id.editor_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(Editor.this, MainActivity.class));
            }
        });
        editor_toolbar.setBackgroundColor(getResources().getColor(toolbarcolor));
        editor_toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.back:
                        performEdit.undo();
                        break;
                    case R.id.toward:
                        performEdit.redo();
                        break;
                    case R.id.go:
                        EditText note_context = findViewById(R.id.note_context);
                        EditText note_title = findViewById(R.id.note_title);
                        if (TextUtils.isEmpty(note_context.getText()) && TextUtils.isEmpty(note_title.getText())) {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "内容不能为空", Snackbar.LENGTH_LONG);
                            snackbar.show();
                        } else {
                            Date date = new Date(System.currentTimeMillis());
                            @SuppressLint("SimpleDateFormat") SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                            String date_string = simpleDateFormat.format(date);
                            notes notes = new notes();
                            note_text = editText.getText().toString();
                            note_titles = note_title.getText().toString();
                            SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
                            String where = sharedPreferences.getString("where", "");
                            switch (where) {
                                case "MainActivity":
                                    notes.setNotes_title(note_titles);
                                    notes.setNotes_context(note_text);
                                    notes.setDate(date_string);
                                    notes.save();
                                    Toast.makeText(Editor.this, save_success, Toast.LENGTH_SHORT).show();
                                    break;
                                case "read":
                                    notes.setNotes_title(note_titles);
                                    notes.setNotes_context(note_text);
                                    notes.setDate(date_string);
                                    notes.updateAll("notes_title = ? and notes_context = ?", title, context);
                                    Toast.makeText(Editor.this, edit_sucess, Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    notes.setNotes_title(note_titles);
                                    notes.setNotes_context(note_text);
                                    notes.setDate(date_string);
                                    notes.save();
                                    break;
                            }
                            SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
                            editor.putString("isCommit", "commit");
                            editor.apply();
                            FileOutputStream out = null;
                            BufferedWriter writer = null;
                            try {
                                out = openFileOutput("cache_data", Context.MODE_PRIVATE);
                                writer = new BufferedWriter(new OutputStreamWriter(out));
                                writer.write("");

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    {
                                        if (writer != null) {
                                            writer.close();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            try {
                                out = openFileOutput("title_data", Context.MODE_PRIVATE);
                                writer = new BufferedWriter(new OutputStreamWriter(out));
                                writer.write("");

                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                try {
                                    {
                                        if (writer != null) {
                                            writer.close();
                                        }
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }
                            Intent intent = new Intent(Editor.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        }
                }
                return true;
            }
        });
        editor_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Editor.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void initEditor() {
        EditText note_context = findViewById(R.id.note_context);
        EditText note_title = findViewById(R.id.note_title);
        performEdit = new PerformEdit(note_context);

    }

    public void save(String input) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("cache_data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(input);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                {
                    if (writer != null) {
                        writer.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void save_title(String titles) {
        FileOutputStream out = null;
        BufferedWriter writer = null;
        try {
            out = openFileOutput("title_data", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(titles);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                {
                    if (writer != null) {
                        writer.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor editor_fab = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
        editor_fab.putBoolean("fab_expand", false);
        editor_fab.apply();
        String input = editText.getText().toString();
        String titles = editText1.getText().toString();
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String isCommit = sharedPreferences.getString("isCommit", "");
        if (isCommit.equals("commit")) {
            SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
            editor.putString("isCommit", "uncommit");
            editor.apply();
        } else {
            save(input);
            save_title(titles);
        }
    }

    public String load() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("cache_data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return content.toString();
            }
        }
    }

    public String load_title() {
        FileInputStream in = null;
        BufferedReader reader = null;
        StringBuilder content = new StringBuilder();
        try {
            in = openFileInput("title_data");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                return content.toString();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            Intent intent = new Intent(Editor.this, MainActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fade, R.anim.fade_exit);
        }
        return false;
    }

  
}
