package com.example.polymino;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LevelManager {
    private static final String PREF_CURRENT_LEVEL = "current_level";
    private static List<Level> levels;
    private static int currentLevel;
    private static SharedPreferences preferences;
    private LevelManager(){

    }

    public static void initialize(Context context) {
        levels = new ArrayList<>();
        levels.add(new Level(1, 3, 3, 120000));
        levels.add(new Level(2, 3, 4, 120000));
        levels.add(new Level(3, 4, 4, 120000));
        levels.add(new Level(4, 5, 5, 120000));
        levels.add(new Level(5, 4, 5, 60000));
        levels.add(new Level(6, 5, 5, 60000));
        levels.add(new Level(7, 6, 5, 120000));
        levels.add(new Level(8, 6, 6, 120000));
        levels.add(new Level(9, 7, 7, 120000));
        levels.add(new Level(10, 10, 10, 180000));

        preferences = PreferenceManager.getDefaultSharedPreferences(context);


        currentLevel = loadCurrentLevel();
    }

    public static Level getCurrentLevel(){
        return levels.get(currentLevel - 1);
    }

    public static void setCurrentLevel(int levelNumber){
        if(levelNumber > 0 && levelNumber <= levels.size()){
            currentLevel = levelNumber;
            saveCurrentLevel();
        }
    }
    private static int loadCurrentLevel(){
        return preferences.getInt(PREF_CURRENT_LEVEL, 1);  // Уровень по умолчанию: 1
    }
    private static void saveCurrentLevel(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(PREF_CURRENT_LEVEL, currentLevel);
        editor.apply();
    }

}
