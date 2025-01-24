package com.example.happybirthdaysenderv3;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;

import java.util.ArrayList;

public class  SendText extends BroadcastReceiver {

    DatabaseOperations dbO;
    @Override
    public void onReceive(Context c, Intent i){
        ArrayList<Person> people;
        dbO = new DatabaseOperations(c);

        dbO.open();
        people = dbO.getAllCurrentBirthdays();
        dbO.close();

        if(!people.isEmpty()){
            for (Person p: people) {
                String num = p.getPhone();
                String text = p.getText();
                SmsManager s = SmsManager.getDefault();
                s.sendTextMessage(num, null, text, null, null);
            }
        }
    }
}
