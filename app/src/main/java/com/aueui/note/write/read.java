package com.aueui.note.write;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.aueui.note.BaseActivity;
import com.aueui.note.R;
import com.aueui.note.editor;

public class read extends BaseActivity {
    private TextView read_title;
    private TextView read_context;
    private Button read_edit;
    String title;
    String context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.read);
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        context = intent.getStringExtra("context");
        read_context = (TextView) findViewById(R.id.read_context);
        read_title = (TextView) findViewById(R.id.read_title);
        read_title.setText(title);
        read_context.setText(context);
        read_edit = (Button) findViewById(R.id.edit);
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
    }
}