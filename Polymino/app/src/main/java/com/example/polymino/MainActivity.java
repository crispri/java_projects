package com.example.polymino;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Level currentLevel;
    static public TextView level;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LevelManager.initialize(this);
        //LevelManager.setCurrentLevel(1);

        currentLevel = LevelManager.getCurrentLevel();
        int levelNumber = currentLevel.getLevelNumber();
        Log.d("ff", "current level is "+levelNumber);
        level = findViewById(R.id.mainLevel);
        level.setText("Your level is " + levelNumber);

    }

    public void onClick(View view) {
        if(view.getId() == R.id.startGame){
            Intent game = new Intent(this, GameActivity.class);
            startActivity(game);
        }
        if(view.getId() == R.id.exit){
            finish();
        }
    }
}