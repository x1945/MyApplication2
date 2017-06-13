package com.example.ap4_fantasy.myapplication2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "定時任務已經執行了!!", Toast.LENGTH_SHORT).show();
        Intent workIntent = new Intent(context, LongRunningService.class);
        workIntent.putExtra("type", "work");
        context.startService(workIntent);

    }
}
