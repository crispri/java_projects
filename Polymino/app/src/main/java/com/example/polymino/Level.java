package com.example.polymino;

public class Level {
    private int levelNumber;
    private int rows;
    private int columns;
    private int time_for_task;
    public Level(int levelNumber, int rows, int columns, int time_for_task){
        this.levelNumber = levelNumber;
        this.rows = rows;
        this.columns = columns;
        this.time_for_task = time_for_task;
    }

    public int getLevelNumber() {
        return levelNumber;
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getTime_for_task() {
        return time_for_task;
    }

    public void setTime_for_task(int time_for_task) {
        this.time_for_task = time_for_task;
    }
}
