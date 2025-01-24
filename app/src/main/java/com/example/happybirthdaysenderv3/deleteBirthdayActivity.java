package com.example.happybirthdaysenderv3;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class deleteBirthdayActivity extends AppCompatActivity {
    DatabaseOperations dbO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_delete_birthday);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            Button deleteButton = findViewById(R.id.addButton);

            dbO = new DatabaseOperations(this);

            deleteButton.setOnClickListener(h -> {
                removeExistingBday();
            });
            return insets;
        });
    }
    public void removeExistingBday(){
        dbO.open();
        EditText name = findViewById(R.id.newtextEditText);
        String tempName = name.getText().toString().trim();
        if (tempName.equals("")){
            Toast.makeText(deleteBirthdayActivity.this,"Must Enter a Name!", Toast.LENGTH_SHORT).show();
        }
        else{
            String output = dbO.remPerson(tempName);
            dbO.close();
            Toast.makeText(deleteBirthdayActivity.this,output, Toast.LENGTH_SHORT).show();
        }
    }
}