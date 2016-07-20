package com.example.serg.testwork.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.example.serg.testwork.R;
import com.example.serg.testwork.activity.MainActivity;


/**
 * Created by user on 19.07.16.
 */

public class MusicInputReceiver extends BroadcastReceiver {

    private static final String TAG = "BROADCAST_INPUT_MUSIC";
    private final static String YANDEX_MUSIC_PACKAGE = "ru.yandex.music";
    private final static String YANDEX_RADIO_PACKAGE = "ru.yandex.radio";

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
            int state = intent.getIntExtra("state", -1);

            Intent music = context.getPackageManager().getLaunchIntentForPackage(YANDEX_MUSIC_PACKAGE);
            Intent radio = context.getPackageManager().getLaunchIntentForPackage(YANDEX_RADIO_PACKAGE);

            if (music != null) {
                music.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else
                music = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + YANDEX_MUSIC_PACKAGE));

            if (radio != null) {
                radio.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else
                radio = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + YANDEX_RADIO_PACKAGE));

            Intent resultIntent = new Intent(context, MainActivity.class);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(MainActivity.class);
            stackBuilder.addNextIntent(resultIntent);
            PendingIntent resultPendingIntent =
                    stackBuilder.getPendingIntent(
                            0,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );

            Bitmap longIconNotif = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.music_icon);

            android.support.v4.app.NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.music_circle)
                            .setLargeIcon(longIconNotif)
                            .setContentTitle(context.getResources().getString(R.string.content_title))
                            .setContentText(context.getResources().getString(R.string.content_text))
                            .setDefaults(Notification.DEFAULT_VIBRATE)
                            .addAction(R.drawable.album, context.getResources().getString(R.string.button_title_music),
                                    PendingIntent.getActivity(context, 0, music, 0))
                            .addAction(R.drawable.radio_tower, context.getResources().getString(R.string.button_title_radio),
                                    PendingIntent.getActivity(context, 0, radio, 0));

            notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (!PreferenceManager.getDefaultSharedPreferences(context)
                    .getBoolean(context.getString(R.string.preference_notify), true)) {
                closeNotify();
                return;
            }

            switch (state) {
                case 0:
                    Log.d(TAG, "Headset is unplugged");
                    closeNotify();
                    break;
                case 1:
                    Log.d(TAG, "Headset is plugged");
                    mBuilder.setContentIntent(resultPendingIntent);
                    notificationManager.notify(0, mBuilder.build());

                    break;
                default:
                    Log.d(TAG, "I have no idea what the headset state is");
                    closeNotify();
            }
        }
    }

    public void closeNotify() {
        notificationManager.cancel(0);
    }



}
