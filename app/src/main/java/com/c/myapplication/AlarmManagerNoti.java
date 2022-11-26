package com.c.myapplication;

import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class AlarmManagerNoti extends Worker {

    public AlarmManagerNoti(Context context, WorkerParameters workerParameters) {
        super(context,workerParameters);
    }

    public static void GuardarAlarm(Long duracion, Data data, String tag ){
        OneTimeWorkRequest alarma= new OneTimeWorkRequest.Builder(AlarmManagerNoti.class)
                .setInitialDelay(duracion, TimeUnit.MILLISECONDS).addTag(tag)
                .setInputData(data)
                .build();

        WorkManager instance = WorkManager.getInstance();
        instance.enqueue(alarma);


    }



    @NonNull
    @Override
    public Result doWork() {
        String mensaje = getInputData().getString("mensaje");





        return Result.success();
    }

    private void alarmSet(String mensaje){
        Calendar calendar= Calendar.getInstance();

        Intent intent = new Intent(AlarmClock.ACTION_SET_ALARM)
                .putExtra(AlarmClock.EXTRA_MESSAGE,mensaje)
                .putExtra(AlarmClock.EXTRA_HOUR,calendar.getTime().getHours())
                .putExtra(AlarmClock.EXTRA_MINUTES,calendar.getTime().getMinutes());

        if (intent.resolveActivity(getApplicationContext().getPackageManager())!= null){



        }


    }


}
