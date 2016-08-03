package com.example.serg.testwork.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.NotificationCompat;

import com.example.serg.testwork.R;

import static android.support.v4.app.NotificationCompat.Builder;


/**
 * Created by SerG3z on 19.07.16.
 */

public class MusicInputReceiver extends BroadcastReceiver {

    private final static String YANDEX_MUSIC_PACKAGE = "ru.yandex.music";
    private final static String YANDEX_RADIO_PACKAGE = "ru.yandex.radio";

    private NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!onHeadsetPlug(intent)) {
            return;
        }

        Resources resources = context.getResources();
        PackageManager packageManager = context.getPackageManager();
        Intent music = packageManager.getLaunchIntentForPackage(YANDEX_MUSIC_PACKAGE);
        Intent radio = packageManager.getLaunchIntentForPackage(YANDEX_RADIO_PACKAGE);

        music = getIntentMarket(music, YANDEX_MUSIC_PACKAGE);
        radio = getIntentMarket(radio, YANDEX_RADIO_PACKAGE);

        Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.drawable.music_icon);

        Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.music_circle)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(resources.getString(R.string.content_title))
                        .setContentText(resources.getString(R.string.content_text))
                        .setDefaults(Notification.DEFAULT_VIBRATE)
                        .addAction(R.drawable.album, resources.getString(R.string.button_title_music),
                                PendingIntent.getActivity(context, 0, music, 0))
                        .addAction(R.drawable.radio_tower, resources.getString(R.string.button_title_radio),
                                PendingIntent.getActivity(context, 0, radio, 0));

        notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, builder.build());
    }

    private boolean onHeadsetPlug(Intent intent) {
        return intent.getAction().equals(Intent.ACTION_HEADSET_PLUG) && intent.getExtras().getInt("state") == 1;
    }

    private Intent getIntentMarket(Intent intent, String namePackage) {
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id="
                    + namePackage));
        }
        return intent;
    }

    public void closeNotify() {
        if (notificationManager != null) {
            notificationManager.cancel(0);
        }
    }
}
