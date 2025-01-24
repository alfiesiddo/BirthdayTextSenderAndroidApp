package com.example.happybirthdaysenderv3;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;

import kotlin.jvm.internal.MagicApiIntrinsics;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    TextView listView;
    DatabaseOperations dbO;
    ArrayList<Person> p = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        listView = findViewById(R.id.listOfBdaysTextView);
        Button addButton = findViewById(R.id.addBirthdayButton);
        Button remButton = findViewById(R.id.remBirthdayButton);
        Button editButton = findViewById(R.id.editBdayButton);


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Check if the SEND_SMS permission is already available. and request from user if not.
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
            scheduleDailyTask();

            remButton.setOnClickListener(h -> {
                Intent i = new Intent(MainActivity.this, deleteBirthdayActivity.class);
                startActivity(i);
            });
            addButton.setOnClickListener(h -> {
                Intent i = new Intent(MainActivity.this, addBirthdayActivity.class);
                startActivity(i);
            });
            editButton.setOnClickListener(h -> {
                Intent i = new Intent(MainActivity.this, editBirthdayActivity.class);
                startActivity(i);
            });

            dbO = new DatabaseOperations(this);
            dbO.open();
            p = dbO.getAllPersons();
            showList(p);
            dbO.close();

            return insets;
        });
    }

    private void scheduleDailyTask() {
        Intent intent = new Intent(this, SendText.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 9);
        calendar.set(Calendar.MINUTE, 0);

        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }
    }
    public void showList(ArrayList<Person> p){
        StringBuilder sb = new StringBuilder();
        for (Person person : p) {
            sb.append(person.getName());
            sb.append(" - ");
            sb.append(person.getBirthday() + "\n");
            sb.append(person.getText() + "\n" + "\n");

        }
        listView.setText(sb.toString());
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
                Toast.makeText(this, "Permission granted to send SMS", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied to send SMS", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }

    private void refreshList() {
        dbO = new DatabaseOperations(this);
        dbO.open();
        p = dbO.getAllPersons();
        showList(p);
        dbO.close();
    }
}
