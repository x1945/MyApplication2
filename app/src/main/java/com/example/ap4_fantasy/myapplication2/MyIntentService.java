package com.example.ap4_fantasy.myapplication2;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * 所有請求的Intent記錄會按順序加入到【隊列】中並按順序【異步】執行，並且每次只會執行【一個】工作線程，當所有任務執行完後IntentService會【自動】停止
 */
public class MyIntentService extends IntentService {
    public MyIntentService() { //必須實現父類的構造方法
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {//注意，因為這裡是異步操作，所以這裡不能直接使用Toast。
        int intentNumber = intent.getExtras().getInt("intentNumber");//根據Intent中攜帶的參數不同執行不同的任務
        Log.i("bqt", "第" + intentNumber + "個工作線程啟動了");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("bqt", "第" + intentNumber + "個工作線程完成了");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i("bqt", "onBind");
        return super.onBind(intent);
    }

    @Override
    public void onCreate() {
        Log.i("bqt", "onCreate");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("bqt", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void setIntentRedelivery(boolean enabled) {
        super.setIntentRedelivery(enabled);
        Log.i("bqt", "setIntentRedelivery");
    }

    @Override
    public void onDestroy() {
        Log.i("bqt", "onDestroy");
        super.onDestroy();
    }
}
