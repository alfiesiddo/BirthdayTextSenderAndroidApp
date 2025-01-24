package com.example.happybirthdaysenderv3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.lang.reflect.Method;

public class editBirthdayActivity extends AppCompatActivity {

    DatabaseOperations dbO;
    EditText nameEdit;
    EditText newTextEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_birthday);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            dbO = new DatabaseOperations(this);
            Button editButton = findViewById(R.id.editButton);
            nameEdit = findViewById(R.id.nameTextEdit);
            newTextEdit = findViewById(R.id.newtextEditText);

            editButton.setOnClickListener(h -> {
                updateText();
            });
            return insets;

        });
    }
    private void updateText(){
        dbO.open();
        String name = nameEdit.getText().toString().trim();
        String newText = newTextEdit.getText().toString().trim();
        String output = dbO.editPerson(name, newText);
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
        dbO.close();
    }
}