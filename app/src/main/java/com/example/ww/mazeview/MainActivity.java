package com.example.ww.mazeview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        int column=intent.getIntExtra("column",0);
        int row=intent.getIntExtra("row",0);
        int[][] maze = UnitMaze.createMaze(column, row);
        MazeView mazeView = new MazeView(this, column, row);
        setContentView(mazeView);
    }
}
