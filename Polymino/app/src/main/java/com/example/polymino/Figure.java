package com.example.polymino;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.io.InputStream;
import java.util.Random;

public class Figure  {
    private int cellSize; //размер клетки фигуры
    private int margin; // расстояние между клетками фигуры (задает отступы)
    private int[][] arr; // форма фигуры
    private String pngId; // идентификатор фигуры для определения пути к изображению (строится на основе arr)

    private Bitmap image; // изображение фигуры
    private int r, g, b;
    private int id;

    Rect rect;


    public Figure(Context context, int cellSize, int margin, int[][] arr, int screenWidth, int fieldHeight, int screenHeight) {

        Log.d("ff", "Figure Constructor");

        this.cellSize = cellSize;
        this.margin = margin;
        this.arr = arr;
        this.pngId = generatePngId();

        String name = 'f' + pngId + arr.length + arr[0].length + getRandomNumber();

        @SuppressLint("DiscouragedApi") int resourceId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());

        image = BitmapFactory.decodeResource(context.getResources(), resourceId);

        image = Bitmap.createScaledBitmap(image,
                cellSize * arr[0].length + margin * (arr[0].length - 1),
                cellSize * arr.length + margin * (arr.length - 1),
                false
        ); //масштабирование изображения
        Log.d("ff", "we scale image");


        this.r = getRandomNumber(255);
        this.g = getRandomNumber(255);
        this.b = getRandomNumber(255);



        int min_l = 100;
        int max_r = screenWidth - 100 - image.getWidth();
        int min_t = fieldHeight + 100;
        int max_b = screenHeight - 100 - image.getHeight();
        Log.d("ff", "screenHeight is " + screenHeight);
        Log.d("ff", "fieldHeight is " + fieldHeight);
        Log.d("ff", "max_b is " + max_b);
        Log.d("ff", "imgHeight" + image.getHeight());



        int myLeft = getRandomNumber(max_r - min_l) + min_l;
        int myTop = getRandomNumber(max_b - min_t)  + min_t;
        Log.d("ff", "myTop " +myTop);
        this.rect = new Rect(myLeft, myTop, image.getWidth() + myLeft, image.getHeight() + myTop);


        this.id = getRandomNumber(1000000);


    }


    String generatePngId() {
        StringBuilder result = new StringBuilder();
        for (int[] row : arr) {
            for (int cell : row) {
                result.append(cell);
            }
        }
        return result.toString();
    }
    int [][] getFigureForm(){
        return arr;
    }

    int[] getScaledImageSize() {
        return new int[]{image.getWidth(), image.getHeight()};
    }

    private int getRandomNumber() {
        return new Random().nextInt(4) + 1;
    }

    private int getRandomNumber(int maxSize) {
        return new Random().nextInt(maxSize + 1);
    }

    public void draw(Canvas canvas) {

        //Log.d("ff", "drawFigure");
        Paint p = new Paint();
        p.setColor(Color.rgb(r, g, b));
        canvas.drawBitmap(image, rect.left, rect.top, p);
    }

   Rect getCoordsOnVIew(){
        return rect;
   }
   void setCoordsForRect(int x, int y){
        int width = rect.width();
        int height = rect.height();
        rect.set(x, y, x+width, y+height);
   }
   int getId(){
        return id;
    }

}
