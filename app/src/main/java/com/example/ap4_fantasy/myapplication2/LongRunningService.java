package com.example.ap4_fantasy.myapplication2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LongRunningService extends Service {
    public LongRunningService() {
    }

    public static final int anHour = 60 * 1000;//設置每隔1分鐘打印一次時間 API22 強制必需要大於60秒

    final int notifyID = 1; // 通知的識別號碼
    //private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //定時任務為：發送一條廣播。在收到廣播後啟動本服務，本服務啟動後又發送一條廣播……
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(this, AlarmReceiver.class), 0);
        //使用【警報、鬧鈴】服務設置定時任務。CPU一旦休眠(比如關機狀態)，Timer中的定時任務就無法運行；而Alarm具有喚醒CPU的功能。
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long triggerAtTime;//鬧鍾(首次)執行時間
        String type = intent.getStringExtra("type");
        switch (type) {
            case "onceAlarm": //設置在triggerAtTime時間啟動由operation參數指定的組件。該方法用於設置一次性鬧鍾
                triggerAtTime = SystemClock.elapsedRealtime() + anHour;//相對於系統啟動時間，Returns milliseconds since boot, including time spent in sleep.
                manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pendingIntent);
                break;
            case "repeatAlarm": //設置一個周期性執行的定時服務，參數表示首次執行時間和間隔時間
                triggerAtTime = System.currentTimeMillis() + anHour;//相對於1970……絕對時間，Returns milliseconds since boot, including time spent in sleep.
                manager.setRepeating(AlarmManager.RTC_WAKEUP, triggerAtTime, anHour, pendingIntent);
                break;
            case "work":
                System.out.println("work");
                //開辟一條線程，用來執行具體的定時邏輯操作
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                        Log.i("bqt", new SimpleDateFormat("yyyy.MM.dd HH-mm-ss", Locale.getDefault()).format(new Date()));
                    }
                }).start();
                break;
            case "test":
                // 取得系統的通知服務
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //                int notifyID = 1; // 通知的識別號碼
//                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
                Notification.Builder builder = new Notification.Builder(getApplicationContext());
                builder.setSmallIcon(R.drawable.ic_notifications_none_black);
//                builder.setSmallIcon(R.drawable.ic_menu_manage);
                builder.setContentTitle("標題");
                builder.setContentText(new Date().toString());
                Notification notification = builder.build(); // 建立通知
                notificationManager.notify(notifyID, notification); // 發送通知
                break;
        }
        /**type有五個可選值:
         AlarmManager.ELAPSED_REALTIME  鬧鍾在睡眠狀態下不可用，如果在系統休眠時鬧鍾觸發，它將不會被傳遞，直到下一次設備喚醒；使用相對系統啟動開始的時間
         AlarmManager.ELAPSED_REALTIME_WAKEUP  鬧鍾在手機睡眠狀態下會喚醒系統並執行提示功能，使用相對時間
         AlarmManager.RTC  鬧鍾在睡眠狀態下不可用，該狀態下鬧鍾使用絕對時間，即當前系統時間
         AlarmManager.RTC_WAKEUP  表示鬧鍾在睡眠狀態下會喚醒系統並執行提示功能，使用絕對時間
         AlarmManager.POWER_OFF_WAKEUP  表示鬧鍾在手機【關機】狀態下也能正常進行提示功能，用絕對時間，但某些版本並不支持！ */
        return super.onStartCommand(intent, flags, startId);
    }
}
