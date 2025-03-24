package com.example.touch;

import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements View.OnTouchListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.layout1).setOnTouchListener( this);
        findViewById(R.id.layout3).setOnTouchListener( this);
        findViewById(R.id.layout4).setOnTouchListener( this);
        findViewById(R.id.layout2).setOnTouchListener( this);
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                showToast("Premut " + getLayoutName(v.getId()));
                return true;

            case MotionEvent.ACTION_UP:
                showToast("Deixat de prémer " + getLayoutName(v.getId()));
                return true;
        }
        return false;
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private String getLayoutName(int id) {
        if (id == R.id.layout1) return "Vermell";
        if (id == R.id.layout2) return "Verd";
        if (id == R.id.layout3) return "Blau";
        if (id == R.id.layout4) return "Groc";
        return "Desconegut";
    }
}