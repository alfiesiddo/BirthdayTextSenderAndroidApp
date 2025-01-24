package com.example.happybirthdaysenderv3;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class addBirthdayActivity extends AppCompatActivity {

    DatabaseOperations dbO;
    String selectedMonth;
    String selectedDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_birthday);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            Button enterButton = findViewById(R.id.addButton);
            dbO = new DatabaseOperations(this);
            Spinner monthSpinner = findViewById(R.id.monthSpinner);
            Spinner daySpinner = findViewById(R.id.daySpinner);

            ArrayAdapter<CharSequence> monthAdapter = ArrayAdapter.createFromResource(this,
                    R.array.monthSpinnerItems, android.R.layout.simple_spinner_item);
            monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            monthSpinner.setAdapter(monthAdapter);

            ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this,
                    R.array.daySpinnerItems, android.R.layout.simple_spinner_item);
            dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            daySpinner.setAdapter(dayAdapter);

            enterButton.setOnClickListener(h -> {
                enterNewBday();
            });
            daySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedDay = daySpinner.getItemAtPosition(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });

            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selectedMonth= monthSpinner.getItemAtPosition(i).toString();
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            return insets;
        });
    }

    public void enterNewBday(){
        dbO.open();
        EditText name = findViewById(R.id.newtextEditText);
        EditText mobile = findViewById(R.id.mobileEditText);
        EditText message = findViewById(R.id.messageEditText);

        String tempName = name.getText().toString().trim();
        String tempMobile = mobile.getText().toString().trim();
        String tempBday  = (selectedDay + "/" + selectedMonth);
        String tempMessage = message.getText().toString().trim();

        tempMobile = tempMobile.replaceAll("\\s", "");

        if(tempMobile.startsWith("+44")){
            tempMobile = tempMobile.replace("+44", "0");
        }
        String regEx = "^07\\d{9}$";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(tempMobile);

        if (!m.matches()){
            Toast.makeText(addBirthdayActivity.this, "Must Enter Valid UK Number!", Toast.LENGTH_SHORT).show();
        }
        else if(tempMessage.equals("")){
            Toast.makeText(addBirthdayActivity.this, "Must Enter a Message!", Toast.LENGTH_SHORT).show();
        }
        else if(tempName.equals("")){
            Toast.makeText(addBirthdayActivity.this, "Must Enter a Name!", Toast.LENGTH_SHORT).show();
        }
        else{
            dbO.addPerson(tempName, tempMobile, tempBday, tempMessage);
            dbO.close();
            Toast.makeText(addBirthdayActivity.this,"Birthday Message Added!", Toast.LENGTH_SHORT).show();

        }
    }
}
