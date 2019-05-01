package com.fiuba.tdpii.correapp.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.fiuba.tdpii.correapp.activities.ChoferActivity;
import com.fiuba.tdpii.correapp.activities.ChoferViewTripActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class CorreappFirebaseMessagingService extends FirebaseMessagingService {



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
//        Toast.makeText(CorreappFirebaseMessagingService.this,"From: " + remoteMessage.getFrom(), Toast.LENGTH_LONG );

        Map<String, String> data = remoteMessage.getData();

        String tipo = data.get("type");
        if("viajeAsignado".equals(tipo)){
            Long tripId = Long.parseLong(data.get("tripId"));
            Long driverId = Long.parseLong(data.get("driverId"));



            Intent navigationIntent = new Intent(CorreappFirebaseMessagingService.this, ChoferViewTripActivity.class);
            Bundle bundle = new Bundle();

            bundle.putLong("tripId", tripId);
            bundle.putLong("driverId", driverId);

            navigationIntent.putExtra("bundle", bundle);
            startActivity(navigationIntent);
        }

//
//        // Check if message contains a data payload.
//        if (remoteMessage.getData().size() > 0) {
//            Toast.makeText(CorreappFirebaseMessagingService.this,"Message data payload: " + remoteMessage.getData(), Toast.LENGTH_LONG );
//
//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                Toast.makeText(CorreappFirebaseMessagingService.this,"LLEGUE" , Toast.LENGTH_LONG );
//            }
//
//        }
//
//        // Check if message contains a notification payload.
//        if (remoteMessage.getNotification() != null) {
//            Toast.makeText(CorreappFirebaseMessagingService.this,"Message Notification Body: " + remoteMessage.getNotification().getBody() , Toast.LENGTH_LONG );
//
//        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
}
