package com.example.persistencia;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FifthActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String FILE_NAME = "dades_sd.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fifth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText editText = findViewById(R.id.editText);
        Button btnDesar = findViewById(R.id.btnDesar);
        Button btnRecuperar = findViewById(R.id.btnRecuperar);
        TextView textView = findViewById(R.id.textView);

        // Comprova els permisos
        if (!checkPermission()) {
            requestPermission();
        }

        btnDesar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    Toast.makeText(FifthActivity.this, "Permís denegat!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String textToSave = editText.getText().toString();
                if (!textToSave.isEmpty()) {
                    if (saveToFile(textToSave)) {
                        Toast.makeText(FifthActivity.this, "Text desat correctament!", Toast.LENGTH_SHORT).show();
                        editText.getText().clear();
                    } else {
                        Toast.makeText(FifthActivity.this, "Error en desar el fitxer.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FifthActivity.this, "Si us plau, escriu alguna cosa.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRecuperar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkPermission()) {
                    Toast.makeText(FifthActivity.this, "Permís denegat!", Toast.LENGTH_SHORT).show();
                    return;
                }

                String retrievedText = readFromFile();
                if (retrievedText != null) {
                    textView.setText(retrievedText);
                } else {
                    Toast.makeText(FifthActivity.this, "No s'ha trobat cap dada.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean saveToFile(String content) {
        if (!isExternalStorageWritable()) {
            Toast.makeText(this, "La targeta SD no està disponible!", Toast.LENGTH_SHORT).show();
            return false;
        }

        File file = new File(getExternalFilesDir(null), FILE_NAME);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(content.getBytes());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String readFromFile() {
        if (!isExternalStorageReadable()) {
            Toast.makeText(this, "La targeta SD no està disponible!", Toast.LENGTH_SHORT).show();
            return null;
        }

        File file = new File(getExternalFilesDir(null), FILE_NAME);
        if (!file.exists()) {
            return null;
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            StringBuilder stringBuilder = new StringBuilder();
            int ch;
            while ((ch = fis.read()) != -1) {
                stringBuilder.append((char) ch);
            }
            return stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    private boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

}