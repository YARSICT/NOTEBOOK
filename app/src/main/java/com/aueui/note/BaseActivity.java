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


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    public static int Theme_all;
    public int CurrentTheme;
    public int toolbarcolor = R.color.pure_white;
    public final static String SP_NAME = "com.aueui.note_preferences";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setAllTheme(int Theme) {

        setTheme(Theme);
    }

    public String isTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences(SP_NAME, MODE_PRIVATE);
        String theme_now = sharedPreferences.getString("theme_items", "");
        return theme_now;
    }

    public void setUserTheme() {
        if (isTheme().equals("blue")) {
            setAllTheme(R.style.BLUE);
        }
        if (isTheme().equals("red")) {
            setAllTheme(R.style.RED);
        }
        if (isTheme().equals("green")) {
            setAllTheme(R.style.GREEN);
        }
        if (isTheme().equals("orange")) {
            setAllTheme(R.style.ORANGE);
        }
        if (isTheme().equals("purple")) {
            setAllTheme(R.style.PURPLE);
        }
        if (isTheme().equals("night")) {
            setAllTheme(R.style.night);
        }
        if (isTheme().equals("pure_white")) {
            setAllTheme(R.style.pure_white);
        }
        if (isTheme().equals("pure_blue")) {
            setAllTheme(R.style.pure_blue);
        }
        if (isTheme().equals("pure_red")) {
            setAllTheme(R.style.pure_red);
        }
    }

}
