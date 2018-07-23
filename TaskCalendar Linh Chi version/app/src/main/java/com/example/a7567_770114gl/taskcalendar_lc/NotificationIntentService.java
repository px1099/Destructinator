package com.example.a7567_770114gl.taskcalendar_lc;

import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;

public class NotificationIntentService extends IntentService{
    //Constructor
    public NotificationIntentService(){
        super("NotificationIntentSerice");
    }

    /**
     * This method is invoked on the worker thread with a request to process.
     * Only one Intent is processed at a time, but the processing happens on a
     * worker thread that runs independently from other application logic.
     * So, if this code takes a long time, it will hold up other requests to
     * the same IntentService, but it will not hold up anything else.
     * When all requests have been handled, the IntentService stops itself,
     * so you should not call {@link #stopSelf}.
     *
     * @param intent The value passed to {@link
     *               Context#startService(Intent)}.
     *               This may be null if the service is being restarted after
     *               its process has gone away; see
     *               {@link Service#onStartCommand}
     *               for details.
     */
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Notification.Builder builder = new Notification.Builder(this);
        int pos = intent.getIntExtra("pos", 0);

        Task t = Task.getTaskList().get(pos);

        String task_name = t.task_name;
        String deadline = t.getDeadlineDate(); //change from Date to String
        String description = t.description;

        builder.setContentTitle(task_name);
        builder.setContentTitle(description.concat("\n").concat(description));

        builder.setSmallIcon(R.mipmap.ic_launcher);

        //TODO: OPEN SHOWINFO.CLASS
        Intent notifyIntent = new Intent(this, ShowInfo.class);
        notifyIntent.putExtra("pos", pos);
        PendingIntent pendingIntent = PendingIntent.getActivity
                (this, t.getNoti_ID(), notifyIntent, PendingIntent.FLAG_ONE_SHOT);

        //To be able to launch your activity from the notification
        builder.setContentIntent(pendingIntent);
        Notification notificationCompat = builder.build();

        notificationCompat.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        notificationCompat.priority = Notification.PRIORITY_MAX;

        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(this);
        managerCompat.notify(t.getNoti_ID(), notificationCompat);
    }
}
