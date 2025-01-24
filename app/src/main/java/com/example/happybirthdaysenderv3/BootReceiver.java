package com.example.happybirthdaysenderv3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    public void onReceive(Context c, Intent i){
        if(i.getAction().equals(Intent.ACTION_BOOT_COMPLETED)){

            Intent newIntent = new Intent(c, MainActivity.class);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            c.startActivity(newIntent);
        }
    }
}
