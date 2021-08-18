package com.aueui.note.activities;

import android.app.ActivityManager;
import android.arch.lifecycle.Lifecycle;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.aueui.note.BaseActivity;
import com.aueui.note.MainActivity;
import com.aueui.note.R;
import com.aueui.note.Settings;

public class FloatMenuActivity extends BaseActivity implements View.OnClickListener {
    private Button main_menu_nightmode, main_menu_settings;
    private static final String TAG = "FMA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_float_menu);
        initView();
        Log.d(TAG, "onCreate: ");
        System.out.println(getIntent().getIntExtra("STAT", 0));

    }
    private boolean isActivityTop(Class cls,Context context){
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        String name = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        return name.equals(cls.getName());
    }
    private void initView() {
        main_menu_nightmode = findViewById(R.id.main_menu_nightmode);
        main_menu_settings = findViewById(R.id.main_menu_settings);
        main_menu_nightmode.setOnClickListener(this);
        main_menu_settings.setOnClickListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.finish();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: ");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_menu_nightmode:
                switchNightMode(FloatMenuActivity.this);
                startSwitchNightMode();
                break;
            case R.id.main_menu_settings:
                startActivity(new Intent(FloatMenuActivity.this, Settings.class));
                break;
        }
    }

    private void startSwitchNightMode() {
        finish();
        sendBroadcast(new Intent().setAction("switchNightMode"));

    }

    private void switchNightMode(Context context) {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        Boolean isChecked = sharedPreferences.getBoolean("isNight", false);

        if (!isChecked) {

            SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
            editor.putString("theme_items", "night");
            editor.putString("theme_items_now", isTheme());
            editor.putBoolean("isNight", true);
            editor.apply();
            Toast.makeText(context, "夜间模式", Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferencess = getSharedPreferences(SP_NAME, MODE_PRIVATE);
            String theme_now_resume = sharedPreferencess.getString("theme_items_now", "");
            SharedPreferences.Editor editor = getSharedPreferences(SP_NAME, MODE_PRIVATE).edit();
            editor.putString("theme_items", theme_now_resume);
            editor.putBoolean("isNight", false);
            editor.apply();
            Toast.makeText(context, "正常模式", Toast.LENGTH_SHORT).show();
        }
        finish();
        startActivity(new Intent(this, MainActivity.class));

        overridePendingTransition(R.anim.fade, 0);
    }

}
