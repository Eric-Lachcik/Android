package com.example.persistencia;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ThirdActivity extends AppCompatActivity {

    private static final String FILE_NAME = "archivus.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_third);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editTextFile = findViewById(R.id.editTextFile);
        Button buttonSaveToFile = findViewById(R.id.buttonSaveToFile);
        Button buttonRetrieveFromFile = findViewById(R.id.buttonRetrieveFromFile);
        TextView textViewFileContent = findViewById(R.id.textViewFileContent);


        buttonSaveToFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToSave = editTextFile.getText().toString();
                if (!textToSave.isEmpty()) {
                    saveToFile(textToSave);
                    Toast.makeText(ThirdActivity.this, "Text desat correctament!", Toast.LENGTH_SHORT).show();
                    editTextFile.getText().clear();
                } else {
                    Toast.makeText(ThirdActivity.this, "Si us plau, escriu alguna cosa.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonRetrieveFromFile.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                String retrievedText = readFromFile();
                if (retrievedText != null) {
                    textViewFileContent.setText(retrievedText);
                } else {
                    Toast.makeText(ThirdActivity.this, "No s'ha trobat cap dada.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveToFile(String content) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String readFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fis = openFileInput(FILE_NAME);
             InputStreamReader isr = new InputStreamReader(fis);
             BufferedReader br = new BufferedReader(isr)) {

            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return stringBuilder.toString().trim();
    }
}