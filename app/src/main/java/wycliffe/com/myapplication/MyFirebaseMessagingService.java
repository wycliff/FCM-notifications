package wycliffe.com.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static android.R.id.message;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

   private static final String TAG = "FCM Service";

   @Override
   public void onMessageReceived(RemoteMessage remoteMessage) {
       // TODO: Handle FCM messages here.
       // If the application is in the foreground handle both data and notification messages here.
       // Also if you intend on generating your own notifications as a result of a received FCM
       // message, here is where that should be initiated.
       Log.d(TAG, "From: " + remoteMessage.getFrom());
       Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());

       String image = remoteMessage.getNotification().getIcon();
       String title = remoteMessage.getNotification().getTitle();
       String text = remoteMessage.getNotification().getBody();
       String sound = remoteMessage.getNotification().getSound();

       //
       int id = 0;
       Object obj = remoteMessage.getData().get("id");
       if (obj != null) {
           id = Integer.valueOf(obj.toString());
       }

       this.sendNotification(new NotificationData(image, id, title, text, sound));

   }// end onMessageReceived


   /**
    * Create and show a simple notification containing the received FCM message.
    *
    * @param notificationData FCM message body received.
    */
   private void sendNotification(NotificationData notificationData) {


       Intent intent = new Intent(this, FCMDater.class);
       intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


       PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
               PendingIntent.FLAG_ONE_SHOT);

       Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
       NotificationCompat.Builder notificationBuilder = null;

 try{
       notificationBuilder = new NotificationCompat.Builder(this)
               .setSmallIcon(R.drawable.ic_notification)
//               .setContentTitle("FCM Message")
//               .setContentText(messageBody)
//               .setAutoCancel(true)
//               .setSound(defaultSoundUri)
//               .setContentIntent(pendingIntent);
               .setContentTitle(URLDecoder.decode(notificationData.getTitle(), "UTF-8"))
               .setContentText(URLDecoder.decode(notificationData.getTextMessage(), "UTF-8"))
               .setAutoCancel(true)
               .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
               .setContentIntent(pendingIntent);

   } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }


       if (notificationBuilder != null) {
           NotificationManager notificationManager =
                   (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
           notificationManager.notify(notificationData.getId(), notificationBuilder.build());
       } else {
           Log.d(TAG, " ALAS!!!! No message was built ");
       }


   }
}
