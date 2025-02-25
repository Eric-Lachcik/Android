package com.example.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class space extends View {
    private Bitmap shipBitmap;
    private Bitmap invaderBitmap;
    private int screenWidth;
    private int screenHeight;
    private final int INVADER_ROWS = 5;
    private final int INVADER_COLUMNS = 5;
    private final int INVADER_SIZE = 80;
    private final int SHIP_WIDTH = 150;
    private final int SHIP_HEIGHT = 100;

    public space(Context context, AttributeSet attrs) {
        super(context, attrs);

        // Cargamos la imagen de la nave
        shipBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.starship);
        // Cargamos la imagen del invader
        invaderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.invader);
    }


    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenWidth = w;
        screenHeight = h;
        shipBitmap = Bitmap.createScaledBitmap(shipBitmap, SHIP_WIDTH, SHIP_HEIGHT, false);
        invaderBitmap = Bitmap.createScaledBitmap(invaderBitmap, INVADER_SIZE, INVADER_SIZE, false);
    }

    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint paint = new Paint();

        // Dibujamos a los 25 invaders en la parte de arriba
        for (int row = 0; row < INVADER_ROWS; row++) {
            for (int col = 0; col < INVADER_COLUMNS; col++) {
                int x = col * (INVADER_SIZE + 10);
                int y = row * (INVADER_SIZE + 10);
                canvas.drawBitmap(invaderBitmap, x, y, paint);
            }
        }

        // Dibujamos la nave en su sitio correspondiente
        int shipX = (screenWidth - SHIP_WIDTH) / 2;
        int shipY = screenHeight - SHIP_HEIGHT - 20;
        canvas.drawBitmap(shipBitmap, shipX, shipY, paint);
    }
}
