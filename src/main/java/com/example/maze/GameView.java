package com.example.maze;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameView extends View {
    private Cell[][] cells;
    private static final int COLS = 7, ROWS = 10;
    private static final float WALL_THICKNESS = 10;
    private float cellSize, hMargin, vMargin;
    private Paint wallPaint;
    private Random random;

    public GameView(Context context) {
        super(context);

        wallPaint = new Paint();
        wallPaint.setColor(Color.BLACK);
        wallPaint.setStrokeWidth(WALL_THICKNESS);

        random = new Random();

        createMaze();
    }

    private Cell getNeighbour(Cell cell){
        ArrayList<Cell> neighbours = new ArrayList<>();

        //Left neighbour
        if(cell.col > 0) {
            if (!cells[cell.col-1][cell.row].visited) {
                neighbours.add(cells[cell.col-1][cell.row]);
            }
        }

        //Right neighbour
        if(cell.col < COLS-1) {
            if (!cells[cell.col+1][cell.row].visited) {
                neighbours.add(cells[cell.col+1][cell.row]);
            }
        }

        //Top neighbour
        if(cell.row > 0) {
            if (!cells[cell.col][cell.row-1].visited) {
                neighbours.add(cells[cell.col][cell.row-1]);
            }
        }

        //Bottom neighbour
        if(cell.row < ROWS-1) {
            if (!cells[cell.col][cell.row+1].visited) {
                neighbours.add(cells[cell.col][cell.row+1]);
            }
        }

        if(neighbours.size() > 0) {
            int index = random.nextInt(neighbours.size());
            return neighbours.get(index);
        }
        return null;
    }

    private void removeWall(Cell current, Cell next){
        //topwall
        if(current.col == next.col && current.row == next.row+1){
            current.topwall = false;
            next.bottomwall = false;
        }

        //rightwall
        if(current.col == next.col-1 && current.row == next.row){
            current.rightwall = false;
            next.leftwall = false;
        }

        //bottomwall
        if(current.col == next.col && current.row == next.row-1){
            current.bottomwall = false;
            next.topwall = false;
        }

        //leftwall
        if(current.col == next.col+1 && current.row == next.row){
            current.leftwall = false;
            next.rightwall = false;
        }
    }

    private void createMaze(){
        Stack<Cell> stack = new Stack<>();
        Cell current, next;

        cells = new Cell[COLS][ROWS];
        for(int x=0; x<COLS; x++){
            for(int y=0; y<ROWS; y++){
                cells[x][y] = new Cell(x,y);
            }
        }

        current = cells[0][0];
        current.visited = true;
        do{
            next = getNeighbour(current);
            if (next != null) {
                removeWall(current, next);
                stack.push(current);
                current = next;
                current.visited = true;
            } else {
                current = stack.pop();
            }
        }while(!stack.empty());
    }
    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.GREEN);

        //int width = getWidth();
        //int height = getHeight();
        int height = Resources.getSystem().getDisplayMetrics().heightPixels;
        int width = Resources.getSystem().getDisplayMetrics().widthPixels;

        if (width / height < COLS / ROWS){
            cellSize = width/(COLS+1);
        }else{
            cellSize = height/(ROWS+1);
        }

        hMargin = (width - COLS*cellSize)/2;
        vMargin = (height - ROWS*cellSize)/2;

        canvas.translate(hMargin, vMargin);

        for(int x=0; x<COLS; x++){
            for(int y=0; y<ROWS; y++){
                if(cells[x][y].topwall)
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            y*cellSize,
                            wallPaint);
                if(cells[x][y].leftwall)
                    canvas.drawLine(
                            x*cellSize,
                            y*cellSize,
                            x*cellSize,
                            (y+1)*cellSize,
                            wallPaint);
                if(cells[x][y].rightwall)
                    canvas.drawLine(
                            (x+1)*cellSize,
                            y*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint);
                if(cells[x][y].bottomwall)
                    canvas.drawLine(
                            x*cellSize,
                            (y+1)*cellSize,
                            (x+1)*cellSize,
                            (y+1)*cellSize,
                            wallPaint);
            }
        }
    }

    private class Cell{
        boolean topwall = true, leftwall = true, bottomwall = true, rightwall= true, visited = false;
        int col, row;

        public Cell(int col, int row) {
            this.col = col;
            this.row = row;
        }
    }
}
