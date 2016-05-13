package me.onionpie.pandorabox.UI;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import me.onionpie.pandorabox.ConstansParam.Constans;
import me.onionpie.pandorabox.Utils.CommonPreference;

public class ValidateScanCodeService extends Service {
    public ValidateScanCodeService() {
    }
//    private Timer timer = null;
//    private TimerTask mTimerTask = new TimerTask() {
//        @Override
//        public void run() {
//
//        }
//    };
    int count =0;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("pd_service service",Thread.currentThread().getId()+"");
    }
//    int time = 1000*60*5;
    int time = 60000;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("pd_service commond",Thread.currentThread().getId()+"");
        Log.d("pd_service","service started");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                count++;
                CommonPreference.putBoolean(getApplicationContext(), Constans.KEY_IS_SCAN_CODE_USEFUL,false);
                Log.d("pd_service count",count+"");
//                Looper.prepare();
//
//                Toast.makeText(getApplicationContext(),count+"",Toast.LENGTH_SHORT).show();
//                Looper.loop();
            }
        },500,time);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//               Timer timer = new Timer();
//                timer.schedule(new TimerTask() {
//                    @Override
//                    public void run() {
//                        count++;
//                        Looper.prepare();
//                        Log.d("pd_timer",count+"");
//                        Toast.makeText(getApplicationContext(),"test",Toast.LENGTH_SHORT).show();
//                        Looper.loop();
//                    }
//                },500,500);
//            }
//        }).start();
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
