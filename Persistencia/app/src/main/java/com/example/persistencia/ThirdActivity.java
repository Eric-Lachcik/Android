package com.example.persistencia;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ThirdActivity extends AppCompatActivity {

    private static final String FILE_NAME = "archivo.txt";

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


        buttonSaveToFile.setOnClickListener(v -> {
            String textToSave = editTextFile.getText().toString();
            if (!textToSave.isEmpty()) {
                saveToFile(textToSave);
                editTextFile.setText("");
                Toast.makeText(this, "Text desat correctament", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "El camp està buit!", Toast.LENGTH_SHORT).show();
            }
        });

        buttonRetrieveFromFile.setOnClickListener(v -> {
            String fileContent = readFromFile();
            if (!fileContent.isEmpty()) {
                textViewFileContent.setText(fileContent);
            } else {
                textViewFileContent.setText("El fitxer està buit");
            }
        });
    }

    private void saveToFile(String text) {
        try (FileOutputStream fos = openFileOutput(FILE_NAME, MODE_PRIVATE)) {
            fos.write(text.getBytes());
        } catch (IOException e) {
            Toast.makeText(this, "Error al desar el fitxer!", Toast.LENGTH_SHORT).show();
        }
    }


    private String readFromFile() {
        StringBuilder stringBuilder = new StringBuilder();
        try (FileInputStream fis = openFileInput(FILE_NAME)) {
            int character;
            while ((character = fis.read()) != -1) {
                stringBuilder.append((char) character);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Error al llegir el fitxer!", Toast.LENGTH_SHORT).show();
        }
        return stringBuilder.toString();
    }
}