package com.example.persistencia;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SecondActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        EditText edit1 = findViewById(R.id.edit1);
        EditText edit2 = findViewById(R.id.edit2);
        Button buttonDesa = findViewById(R.id.desa);
        Button buttonRecuperar = findViewById(R.id.recuperar);
        TextView text1 = findViewById(R.id.text1);
        TextView text2 = findViewById(R.id.text2);


        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        editor = sharedPreferences.edit();


        buttonDesa.setOnClickListener(v -> {
            String textToSave1 = edit1.getText().toString();
            String textToSave2 = edit2.getText().toString();

            editor.putString("KEY_TEXT1", textToSave1);
            editor.putString("KEY_TEXT2", textToSave2);
            editor.apply();


            edit1.setText("");
            edit2.setText("");
        });


        buttonRecuperar.setOnClickListener(v -> {
            String savedText1 = sharedPreferences.getString("KEY_TEXT1", "No hi ha text desat");
            String savedText2 = sharedPreferences.getString("KEY_TEXT2", "No hi ha text desat");



            text1.setText(savedText1);
            text2.setText(savedText2);
        });
    }
}