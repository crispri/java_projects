package com.example.polymino;

import static java.lang.Integer.max;
import static java.lang.Integer.min;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Field {
    int n, m;

    int cellSize;
    int margin = 5;
    int fieldWidth;
    int fieldHeight;

    int[][] gameField;

    List<int[][]> allTypesOfFigures;
    List<int[]> allCells;

    List<int[][]> addedFigures;
    Context context;
    int screenWidth;
    int screenHeight;

    public int getFieldWidth() {
        return fieldWidth;
    }

    public int getFieldHeight() {
        return fieldHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public Field(Context context, int n, int m, int viewWidth, int viewHeight) {
        this.context = context;
        this.n = n;
        this.m = m;
        gameField = new int[n][m];
        this.screenWidth = viewWidth;
        this.screenHeight = viewHeight;


        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                gameField[i][j] = 0;
            }
        }

        allTypesOfFigures = new ArrayList<>();
        allCells = new ArrayList<>();
        addedFigures = new ArrayList<>();



        initialiseAllFigures();
        initializeAllCells();
        new Thread(new Runnable() {
            @Override
            public void run() {
                generateFigures();
            }

        }).start();




        cellSize = 700 / max(n, m);


        fieldWidth = cellSize * m + margin * (m + 1);
        fieldHeight = cellSize * n + margin * (n + 1);
        Log.d("ff", "field width is " + fieldWidth);


    }

    public void generateFigures(){

        while (!haveAlreadyFilled()) {
            int[][] tmpFig = allTypesOfFigures.get(new Random().nextInt(allTypesOfFigures.size()));
            List<int[]> coordsOfTmpFig = new ArrayList<>();
            int[] firstFreeCell = allCells.get(0); // будет левым верхним углом для нашей фигуры

            for (int i = 0; i < tmpFig.length; ++i) {
                for (int j = 0; j < tmpFig[i].length; ++j) {
                    if (tmpFig[i][j] == 1) {
                        coordsOfTmpFig.add(new int[]{firstFreeCell[0] + i, firstFreeCell[1] + j});
                    }
                }
            }
            if (rightCrd(coordsOfTmpFig) && canAdd(coordsOfTmpFig)) {
                addOnGameField(coordsOfTmpFig, 1);
                for (int[] cell : coordsOfTmpFig) {
                    Iterator<int[]> iterator = allCells.iterator();
                    while (iterator.hasNext()) {
                        int[] cell_ = iterator.next();
                        if (Arrays.equals(cell_, cell)) {
                            iterator.remove();
                        }
                    }
                }
                    addedFigures.add(tmpFig);



                Log.d("ff", "addFigure");
            }
        }
        removeFigureById(1); //очистили поле которое заполняется при создании фигур разрезанием

    }

    private void initialiseAllFigures() {

        allTypesOfFigures.add(new int[][]{{1}});
        allTypesOfFigures.add(new int[][]{{1, 1}});
        allTypesOfFigures.add(new int[][]{{1}, {1}});
        allTypesOfFigures.add(new int[][]{{1, 1, 1}});
        allTypesOfFigures.add(new int[][]{{1, 1, 1, 1}});
        allTypesOfFigures.add(new int[][]{{1}, {1}, {1}});
        allTypesOfFigures.add(new int[][]{{1, 1}, {1, 1}});
        allTypesOfFigures.add(new int[][]{{1, 1}, {1, 0}});
        allTypesOfFigures.add(new int[][]{{0, 1}, {1, 1}});
        allTypesOfFigures.add(new int[][]{{1, 0}, {1, 1}});
        allTypesOfFigures.add(new int[][]{{1, 1}, {0, 1}});
        allTypesOfFigures.add(new int[][]{{1}, {1}, {1}, {1}});
        allTypesOfFigures.add(new int[][]{{0, 1, 0}, {1, 1, 1}});
        allTypesOfFigures.add(new int[][]{{1, 1, 1}, {0, 1, 0}});
        allTypesOfFigures.add(new int[][]{{1, 0, 0}, {1, 1, 1}});
        allTypesOfFigures.add(new int[][]{{1, 1, 1}, {0, 0, 1}});
        allTypesOfFigures.add(new int[][]{{1, 0}, {1, 1}, {1, 0}});
        allTypesOfFigures.add(new int[][]{{1, 1}, {1, 0}, {1, 0}});
        allTypesOfFigures.add(new int[][]{{0, 1}, {1, 1}, {0, 1}});
        allTypesOfFigures.add(new int[][]{{0, 1}, {0, 1}, {1, 1}});
        Log.d("ff", "initialiseAllFigures");

    }

    private void initializeAllCells() {

        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                allCells.add(new int[]{i, j});
            }
        }
        Log.d("ff", "initializeAllCells");
    }

     boolean rightCrd(List<int[]> coords) {
        // каждая фигура представлена списком из клеток, которые имеют индексы x и y на поле размера n*m
        for (int[] cell : coords) {
            if (!(cell[0] >= 0 && cell[0] < n && cell[1] >= 0 && cell[1] < m))
                return false;
        }
        return true;
    }

      boolean canAdd(List<int[]> coords) {
        for (int[] cell : coords) {
            if (gameField[cell[0]][cell[1]] != 0)
                return false;
        }
        return true;
    }

    void addOnGameField(List<int[]> coords, int id) {
        for (int[] cell : coords) {
            gameField[cell[0]][cell[1]] = id; //клетка поля заполняется
        }
    }

    void removeFigureById(int id) {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (gameField[i][j] == id) {
                    gameField[i][j] = 0;
                }
            }
        }
    }

    boolean haveAlreadyFilled() {
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                if (gameField[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }



    public void draw(Canvas canvas) {
        float left = (float) (screenWidth - fieldWidth) / 2;
        float top = 0;
        Paint p_rect = new Paint();
        p_rect.setColor(Color.WHITE);
        Paint p_marg = new Paint();
        p_marg.setColor(Color.BLACK);
        canvas.drawRect(left, top, left + fieldWidth, top + margin, p_marg);
        for (int i = 0; i < n; ++i) {
            canvas.drawRect(left, top + margin + i * (margin + cellSize), left + margin, top + margin + i * (margin + cellSize) + cellSize, p_marg);
            for (int j = 0; j < m; ++j) {
                canvas.drawRect(left + margin + j * (cellSize + margin), top + margin + i * (cellSize + margin), left + margin + j * (cellSize + margin) + cellSize, top + margin + i * (cellSize + margin) + cellSize, p_rect);
                canvas.drawRect(left + margin + j * (cellSize + margin) + cellSize, top + margin + i * (cellSize + margin), left + margin + j * (cellSize + margin) + cellSize + margin, top + margin + i * (cellSize + margin) + cellSize, p_marg);
            }
            canvas.drawRect(left, top + (cellSize + margin) * (i + 1), left + fieldWidth, top + (cellSize + margin) * (i + 1) + margin, p_marg);
        }

    }

    private List<int[][]> sort(List<int[][]> myNotSortedList) {
        myNotSortedList.sort(new Comparator<int[][]>() {
            @Override
            public int compare(int[][] o1, int[][] o2) {
                int sz1 = o1[0].length;
                int sz2 = o2[0].length;
                return Integer.compare(sz1, sz2);
            }
        });
        return myNotSortedList;
    }

    public List<int[][]> getAddedFigures() {

            return addedFigures;

    }


}
