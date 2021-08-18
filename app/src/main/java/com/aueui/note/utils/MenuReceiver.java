package com.aueui.note.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.aueui.note.MainActivity;
import com.aueui.note.R;
import com.aueui.note.activities.FloatMenuActivity;

import static android.content.Context.MODE_PRIVATE;
import static com.aueui.note.BaseActivity.SP_NAME;

public class MenuReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
       if(intent.getAction().equals("switchNightMode")){
           Toast.makeText(context, "重启后生效", Toast.LENGTH_SHORT).show();
       }
    }


}
