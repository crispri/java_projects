package com.example.polymino;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnBack;
    Game game;
    TextView timer;
    TextView level;
    private CountDownTimer countDownTimer;

    private Level currentLevel;
    private Context context;
    View v;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        Log.d("ff", "createGameActivity");
        btnBack = findViewById(R.id.btnBack);
        game = findViewById(R.id.game);
        Log.d("ff", "gameW and gameH "+ game.getWidth() + " "+game.getHeight());


        currentLevel = LevelManager.getCurrentLevel();
        this.context = this;
        timer = findViewById(R.id.timer);
        timer.setTextSize(30);
        timer.setGravity(Gravity.CENTER);
        timer.setTypeface(Typeface.DEFAULT_BOLD);
        timer.setText("Подождите, идет загрузка пазлов :)");


        level = findViewById(R.id.level);
        level.setText("Your level is "+ currentLevel.getLevelNumber());
        long millisInFuture = currentLevel.getTime_for_task();
        setCountDownTimer(millisInFuture);
        initGame(context, countDownTimer);









    }
    void setCountDownTimer(long millisInFuture){
        countDownTimer = new CountDownTimer(millisInFuture, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long seconds = millisUntilFinished / 1000;
                long minutes_ = seconds / 60;
                long secs_ = seconds - minutes_ * 60;
                timer.setTextSize(30);
                timer.setGravity(Gravity.CENTER);
                timer.setTypeface(Typeface.DEFAULT_BOLD);
                timer.setText("У вас осталось: " + String.valueOf(minutes_)+":" + String.valueOf(secs_));
            }

            @Override
            public void onFinish() {
                if(!game.isAnswerIsGot()){
                    Log.d("ff", "timer on level "+ LevelManager.getCurrentLevel().getLevelNumber() + " stopped WHYYYYYY?");
                    showLoserDialog();
                }
                else{
                    showLevelUpDialog();
                }
            }
        };
    }
    private void initGame(Context context, CountDownTimer countDownTimer){
        ViewGroup parentContainer = (ViewGroup) findViewById(R.id.mainContainer);
        int gameIndex = parentContainer.indexOfChild(game);
        Log.d("ff", "indexOfGameView is " + gameIndex);
        game = new Game(context, null, countDownTimer);
        LinearLayout.LayoutParams gameLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,  // Ширина
                0,  // Высота
                0.95f  // Вес
        );
        game.setLayoutParams(gameLayoutParams);

        parentContainer.removeViewAt(gameIndex);
        parentContainer.addView(game, gameIndex);
    }
    @Override
    public void onClick(View view) {
        game.onClick(view);
        if(view.getId() == R.id.btnBack){
            countDownTimer.cancel();
            finish();
        }
        if(view.getId() == R.id.enter){
            if(game.gameField.haveAlreadyFilled()){
                countDownTimer.cancel();
                showLevelUpDialog();
            }
            else{
                showContinueDialog("Вы собрали еще не все поле");
            }
        }
    }
    private void showContinueDialog(String initialMsg) {
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(initialMsg).setCancelable(true);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }
    private void showLevelUpDialog(){
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Ура! Этот уровень пройден!");
            builder.setMessage("Хотите продолжить?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    LevelManager.setCurrentLevel(currentLevel.getLevelNumber() + 1);
                    dialog.dismiss();
                    Log.d("ff", "timer on level " + (LevelManager.getCurrentLevel().getLevelNumber() - 1) + " stopped");
                    currentLevel = LevelManager.getCurrentLevel();
                    setCountDownTimer(currentLevel.getTime_for_task());
                    timer.setText("Подождите, идет загрузка пазлов :)");
                    level.setText("Your level is " + currentLevel.getLevelNumber());
                    MainActivity.level.setText("Your level is "+LevelManager.getCurrentLevel().getLevelNumber());

                    initGame(context, countDownTimer);
                }
            });
            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    LevelManager.setCurrentLevel(currentLevel.getLevelNumber() + 1);
                    dialog.dismiss();
                    Log.d("ff", "timer on level " + (LevelManager.getCurrentLevel().getLevelNumber() - 1) + " stopped");
                    MainActivity.level.setText("Your level is "+LevelManager.getCurrentLevel().getLevelNumber());
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }
    private void showLoserDialog(){
        if (!isFinishing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Время вышло((");
            builder.setMessage("Хотите попробовать еще раз?");
            builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Log.d("ff", "timer on level " + (LevelManager.getCurrentLevel().getLevelNumber())+" stopped");
                    timer.setText("Подождите, идет загрузка пазлов :)");
                    level.setText("Your level is " + currentLevel.getLevelNumber());
                    initGame(context, countDownTimer);
                }
            });
            builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    countDownTimer.cancel();
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    }


}