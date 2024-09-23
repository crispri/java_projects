package com.example.polymino;

import static java.lang.Integer.max;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import com.example.polymino.Level;


public class Game extends View implements OnFiguresLoadedListener {
    private boolean running = true;
    private boolean gameOver = false;
    private boolean answerIsGot = false;
    private boolean createAllFigures = false;
    private boolean moving = false;
    private Figure draggedFigure = null;

    private CountDownTimer timer;

    private Context context;
    private volatile List<Figure> figures;
    Field gameField;
    int viewWidth;
    int viewHeight;

    int n;
    int m;
    int margin = 5;
    int cellSize;
    int offset_x;
    int offset_y;

    Level currentLevel;


    private int decoord(int coord, boolean by_width) { //i j - координата квадратика на клеточном поле
        if (by_width) {
            return (coord - (gameField.screenWidth - gameField.getFieldWidth()) / 2) / (margin + cellSize); //возвращает i
        } else {
            return coord / (margin + cellSize); //возвращает j

        }
    }

    private int[] found_cell_on_game_field(int rect_l, int rect_t) {
        int[] a = {-1, -1};
        a[1] = decoord(rect_l, true);
        a[0] = decoord(rect_t, false);
        return a;
    }

    private int[] found_cell_on_view(int i, int j) {
        int[] a = {(gameField.getScreenWidth() - gameField.getFieldWidth()) / 2 + j * (margin + cellSize), i * (margin + cellSize)};
        return a;
    }

    public Game(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Game(Context context, @Nullable AttributeSet attrs, CountDownTimer timer) {
        super(context, attrs);
        this.setBackgroundColor(Color.BLACK);

        this.currentLevel = LevelManager.getCurrentLevel();

        Log.d("ff", "constructor with timer");
        this.context = context;
        this.timer = timer;
        this.n = currentLevel.getRows();
        this.m = currentLevel.getColumns();
        this.cellSize = 700 / max(n, m);

        figures = new ArrayList<>();
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        moving = true;
                        for (Figure figure : figures) {
                            if (isPontInsideView(x, y, figure)) {
                                draggedFigure = figure;
                                offset_x = Math.round(x);
                                offset_y = Math.round(y);
                                break;
                            }
                        }
                        break;
                    case MotionEvent.ACTION_MOVE:
                        if (moving) {
                            int mouse_x = Math.round(event.getX());
                            int mouse_y = Math.round(event.getY());
                            Rect before = draggedFigure.getCoordsOnVIew();
                            int left_after = mouse_x - offset_x + before.left;
                            int top_after = mouse_y - offset_y + before.top;
                            draggedFigure.setCoordsForRect(left_after, top_after);
                            invalidate();
                            offset_x = mouse_x;
                            offset_y = mouse_y;
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        moving = false;
                        Rect r = draggedFigure.getCoordsOnVIew();
                        gameField.removeFigureById(draggedFigure.getId());
                        // найдем координаты клеток в которые попала фигура на поле(если попала)

                        int left = (gameField.getScreenWidth() - gameField.getFieldWidth()) / 2;
                        int top = 0;
                        boolean try_add = false;
                        for (int i = 0; i < n; ++i) {
                            for (int j = 0; j < m; ++j) {
                                int cur_left = left + j * (cellSize + margin);
                                int cur_top = top + i * (cellSize + margin);
                                int cur_right = cur_left + cellSize + margin;
                                int cur_bottom = cur_top + cellSize + margin;
                                if (r.left >= cur_left - 50 && r.left + margin + cellSize <= cur_right + 50 &&
                                        r.top >= cur_top - 50 && r.top + margin + cellSize <= cur_bottom + 50) {
                                    draggedFigure.setCoordsForRect(cur_left, cur_top);
                                    invalidate();
                                    try_add = true;
                                    break;
                                }
                            }
                        }
                        if (try_add) {
                            int[] lb = found_cell_on_game_field(draggedFigure.rect.left, draggedFigure.rect.top);
                            Log.d("ff", "lb is " + lb[0] + " " + lb[1]);
                            int[][] cur_fig = draggedFigure.getFigureForm();
                            List<int[]> cur_fig_coords_on_game_field = new ArrayList<>();
                            for (int i = 0; i < cur_fig.length; ++i) {
                                for (int j = 0; j < cur_fig[i].length; ++j) {
                                    if (cur_fig[i][j] != 0) {
                                        cur_fig_coords_on_game_field.add(new int[]{lb[0] + i, lb[1] + j});
                                    }
                                }
                            }
                            if (gameField.rightCrd(cur_fig_coords_on_game_field)) {
                                if (gameField.canAdd(cur_fig_coords_on_game_field)) {
                                    for (int i = 0; i < cur_fig_coords_on_game_field.size(); ++i) {
                                        Log.d("ff", "add" + cur_fig_coords_on_game_field.get(i)[0] + " " + cur_fig_coords_on_game_field.get(i)[1]);
                                    }
                                    gameField.addOnGameField(cur_fig_coords_on_game_field, draggedFigure.getId());
                                }
                                Log.d("ff", "===================================");
                            }
                            //like
                            // 0 1
                            // 1 1
                        }
                        draggedFigure = null;
                        break;
                }

                return true;
            }
        });

    }

    public void onClick(View view) {
    }

    public boolean isAnswerIsGot() {
        return answerIsGot;
    }

    public boolean isCreateAllFigures() {
        return createAllFigures;
    }

    @Override
    public void onFiguresLoaded() {
        timer.start();
    }

    private class AddFiguresTask extends AsyncTask<Void, Figure, Void> {
        private OnFiguresLoadedListener listener;

        public AddFiguresTask(OnFiguresLoadedListener listener) {
            this.listener = listener;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            if (!gameField.getAddedFigures().isEmpty()) {
                Log.d("ff", "createNOOORM");
                List<int[][]> addFig = gameField.getAddedFigures();
                for (int i = 0; i < addFig.size(); ++i) {
                    Figure figure = new Figure(context, cellSize, margin, addFig.get(i), gameField.getScreenWidth(), gameField.getFieldHeight(), gameField.getScreenHeight());
                    publishProgress(figure);
                }
                createAllFigures = true;

            }
            return null;


        }

        @Override
        protected void onProgressUpdate(Figure... values) {

            // Обновление UI в основном потоке
            figures.add(values[0]);
            invalidate();  // Запрос на перерисовку
            if (figures.size() == gameField.getAddedFigures().size()) {
                // Запуск таймера после загрузки всех пазлов
                listener.onFiguresLoaded();
                Log.d("ff", "timer on level " + LevelManager.getCurrentLevel().getLevelNumber() + " have started");
            }
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        gameField.draw(canvas);
        List<Figure> currentFigures = figures;

        for (int i = 0; i < figures.size(); ++i) {
            currentFigures.get(i).draw(canvas);
        }


    }


    private boolean isPontInsideView(float x, float y, Figure figure) {
        Rect r = figure.getCoordsOnVIew();
        int cur_left = r.left;
        int cur_top = r.top;

        int[][] form = figure.getFigureForm();

        for (int i = 0; i < form.length; ++i) {
            for (int j = 0; j < form[i].length; ++j) {
                if (form[i][j] != 0) {
                    if (x >= (cur_left + j * (cellSize + margin)) && x <= (cur_left + j * (cellSize + margin) + (cellSize + 2 * margin)) &&
                            y >= (cur_top + i * (cellSize + margin)) && y <= (cur_top + i * (cellSize + margin) + 2 * margin + cellSize))
                        return true;
                }
            }
        }

        return false;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;
        Log.d("Game", "onSizeChanged: width=" + viewWidth + ", height=" + viewHeight);
        gameField = new Field(context, n, m, viewWidth, viewHeight);
        new AddFiguresTask(Game.this).execute();


    }
}
