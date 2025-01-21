package com.example.persistencia;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.DialogInterface;
import android.database.Cursor;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import java.util.ArrayList;

public class SixActivity extends AppCompatActivity {

    private EditText etDni, etNom;
    private Button btnInsert, btnList, btnDelete, btnUpdate;
    private ListView lvData;
    private SqliteHelp dbHelper;

    private ArrayList<String> dataList;
    private ArrayAdapter<String> adapter;

    private int selectedId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_six);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        etDni = findViewById(R.id.etDni);
        etNom = findViewById(R.id.etNom);
        btnInsert = findViewById(R.id.btnInsert);
        btnList = findViewById(R.id.btnList);
        btnDelete = findViewById(R.id.btnDelete);
        btnUpdate = findViewById(R.id.btnUpdate);
        lvData = findViewById(R.id.lvData);

        dbHelper = new SqliteHelp(this);

        dataList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, dataList);
        lvData.setAdapter(adapter);

        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
            }
        });

        btnList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listData();
            }
        });

        lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String item = dataList.get(position);
                String[] parts = item.split(" - ");
                selectedId = Integer.parseInt(parts[0]);
                etDni.setText(parts[1]);
                etNom.setText(parts[2]);
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAction("Esborrar", "Segur que vols esborrar aquest registre?", new Runnable() {
                    @Override
                    public void run() {
                        deleteData();
                    }
                });
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmAction("Modificar", "Segur que vols modificar aquest registre?", new Runnable() {
                    @Override
                    public void run() {
                        updateData();
                    }
                });
            }
        });

    }

    private void insertData() {
        String dni = etDni.getText().toString();
        String nom = etNom.getText().toString();
        if (dni.isEmpty() || nom.isEmpty()) {
            Toast.makeText(this, "Els camps no poden estar buits.", Toast.LENGTH_SHORT).show();
            return;
        }

        long result = dbHelper.insertData(dni, nom);
        if (result != -1) {
            Toast.makeText(this, "Registre inserit correctament.", Toast.LENGTH_SHORT).show();
            etDni.setText("");
            etNom.setText("");
        } else {
            Toast.makeText(this, "Error en inserir el registre.", Toast.LENGTH_SHORT).show();
        }
    }

    private void listData() {
        dataList.clear();
        Cursor cursor = dbHelper.getAllData();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(SqliteHelp.COLUMN_ID));
                String dni = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelp.COLUMN_DNI));
                String nom = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelp.COLUMN_NOM));
                dataList.add(id + " - " + dni + " - " + nom);
            } while (cursor.moveToNext());
            cursor.close();
        }
        adapter.notifyDataSetChanged();
    }

    private void deleteData() {
        if (selectedId == -1) {
            Toast.makeText(this, "Selecciona un registre primer.", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.deleteData(selectedId);
        if (result > 0) {
            Toast.makeText(this, "Registre esborrat correctament.", Toast.LENGTH_SHORT).show();
            listData();
            etDni.setText("");
            etNom.setText("");
            selectedId = -1;
        } else {
            Toast.makeText(this, "Error en esborrar el registre.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateData() {
        if (selectedId == -1) {
            Toast.makeText(this, "Selecciona un registre primer.", Toast.LENGTH_SHORT).show();
            return;
        }

        String dni = etDni.getText().toString();
        String nom = etNom.getText().toString();
        if (dni.isEmpty() || nom.isEmpty()) {
            Toast.makeText(this, "Els camps no poden estar buits.", Toast.LENGTH_SHORT).show();
            return;
        }

        int result = dbHelper.updateData(selectedId, dni, nom);
        if (result > 0) {
            Toast.makeText(this, "Registre actualitzat correctament.", Toast.LENGTH_SHORT).show();
            listData();
            etDni.setText("");
            etNom.setText("");
            selectedId = -1;
        } else {
            Toast.makeText(this, "Error en actualitzar el registre.", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmAction(String title, String message, final Runnable action) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        action.run();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

}