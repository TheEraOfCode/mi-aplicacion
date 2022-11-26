package com.c.myapplication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;
import java.util.UUID;

public class BroadR extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        Random random= new Random();
        int aleatorio = random.nextInt(100);
        Intent i = new Intent(context, MainActivity3.class);
        i.putExtra("clave",MainActivity3.clavePrincipal );
        i.putExtra("identificador",MainActivity3.identificar );
        i.putExtra("Imagen",MainActivity3.laFoto );
        i.putExtra("fecha",MainActivity3.fechaIngreso );
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingoIntent = PendingIntent.getActivity(context, 0, i, PendingIntent.FLAG_UPDATE_CURRENT     );



        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "CHANNEL_ID")
                .setSmallIcon(R.drawable.logo)
                .setContentTitle(MainActivity3.Nombre.getText().toString())
                .setContentText("Este es el titulo")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(MainActivity3.textoNotificacion))
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingoIntent)
                .setAutoCancel(true);;

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(aleatorio, builder.build());



    }
}
