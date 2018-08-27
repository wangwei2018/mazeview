package com.example.ww.mazeview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.shapes.RoundRectShape;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static android.content.ContentValues.TAG;

public class MazeView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private RenderThread renderThread;

    private ArrayStack<Coordinate> arrayStack;
    private List<Coordinate> coordinateList;

    private boolean isDraw = false;

    private Paint borderPanit;  // 边界画笔
    private Paint startPaint;   // 起始点画笔
    private Paint endPaint;     // 终点画笔
    private Paint pathPaint;    // 轨迹画笔
    private Paint buttonPaint;  // 按钮画笔
    private Paint wordPaint;    // 写字画笔
    private Paint clearPaint;   // 清屏画笔

    private float button_width;     // 按钮的宽度
    private float button_height;    // 按钮的高度

    private int column;     // 行数
    private int row;        // 列数

    private float cell_width;   // 单元格宽度
    private float cell_height;  // 单元格高度

    private float left_border;  // 左边距
    private float top_border;  // 上边距

    // 寻找最优路径时的迷宫矩阵
    private int[][] maze;

    // 起始点、终点的圆的半径
    private float radius;


    private ArrayStack<Coordinate> pathStack;


    private int start_x = 0;    // 起始横坐标
    private int start_y = 0;    // 其实的纵坐标
    private int end_x = 0;      // 结束横坐标
    private int end_y = 0;      // 结束纵坐标
    private Canvas canvas;

    public MazeView(Context context, int column, int row) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

        maze = new int[row][column];
        for (int i = 0; i < row; i++)
            for (int j = 0; j < column; j++)
                maze[i][j] = 0;

        arrayStack = new ArrayStack<>();
        arrayStack = UnitMaze.getArrayStack();

        pathStack = new ArrayStack<>();


        coordinateList = new LinkedList<>();

        this.column = column;
        this.row = row;

        initConfiguration();

        renderThread = new RenderThread();


    }

    /**
     * 做一些初始化的工作
     */
    private void initConfiguration() {
        /**设置左边距 上边距**/
        left_border = 20;
        top_border = 20;

        /**设置边界的画笔**/
        borderPanit = new Paint();
        borderPanit.setColor(Color.BLACK);
        borderPanit.setStyle(Paint.Style.STROKE);

        /**起始点画笔**/
        startPaint = new Paint();
        startPaint.setColor(Color.BLUE);
        startPaint.setStyle(Paint.Style.FILL);

        /**终点画笔**/
        endPaint = new Paint();
        endPaint.setColor(Color.RED);
        endPaint.setStyle(Paint.Style.FILL);

        /**轨迹画笔**/
        pathPaint = new Paint();
        pathPaint.setColor(Color.GREEN);
        pathPaint.setStyle(Paint.Style.FILL);

        /**按钮画笔**/
        buttonPaint = new Paint();
        buttonPaint.setColor(Color.GRAY);
        buttonPaint.setStyle(Paint.Style.FILL);

        /**写字画笔**/
        wordPaint = new Paint();
        wordPaint.setColor(Color.WHITE);
        wordPaint.setTextAlign(Paint.Align.CENTER);
        wordPaint.setTextSize(24);

        /**清屏画笔**/
        clearPaint = new Paint();
        clearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        isDraw = true;
        renderThread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        isDraw = false;
    }

    public class RenderThread extends Thread {
        @Override
        public void run() {

            drawUI();
            super.run();
        }
    }

    private void drawStart_End(Canvas canvas) {
        Random random = new Random();
        while (start_x == end_x && start_y == end_y) {
            start_x = random.nextInt(row);
            start_y = random.nextInt(column);
            end_x = random.nextInt(row);
            end_y = random.nextInt(column);
        }
        Log.e(TAG, "drawStart_End: " + "start_x=" + start_x + "  start_y=" + start_y);
        Log.e(TAG, "drawStart_End: " + "end_x=" + end_x + "  end_y=" + end_y);

        radius = (cell_height < cell_width ? cell_height : cell_width) / 2 - 1;

        canvas.drawCircle((left_border + start_x * cell_width + left_border + (start_x + 1) * cell_width) / 2, (top_border + start_y * cell_height + top_border + (start_y + 1) * cell_height) / 2, radius, startPaint);
        canvas.drawCircle((left_border + end_x * cell_width + left_border + (end_x + 1) * cell_width) / 2, (top_border + end_y * cell_height + top_border + (end_y + 1) * cell_height) / 2, radius, endPaint);


    }


    private void drawUI() {
        canvas = holder.lockCanvas();
        try {
            drawCanvas(canvas);
            drawButtons(canvas);

            drawStart_End(canvas);
            drawPath(canvas);
        } catch (Exception e) {

        } finally {
            holder.unlockCanvasAndPost(canvas);
        }
    }

    private void drawButtons(Canvas canvas) {
        RectF rectF = new RectF(left_border, 10, left_border + 130, 10 + button_height);
        RectF rectF2 = new RectF(getWidth() - left_border - 130, 10, getWidth() - left_border, 10 + button_height);

        canvas.drawRoundRect(rectF, 9, 9, buttonPaint);
        canvas.drawText("更换地图", (left_border + left_border + 130) / 2, (10 + 15 + button_height + 10) / 2, wordPaint);

        canvas.drawRoundRect(rectF2, 9, 9, buttonPaint);
        canvas.drawText("生成路径", (getWidth() - left_border - 130 + getWidth() - left_border) / 2, (10 + 15 + button_height + 10) / 2, wordPaint);


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        /**设置点击的监听事件**/
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            float x = event.getX();
            float y = event.getY();
            if (x >= left_border && x <= left_border + 130 && y >= 10 && y <= 10 + button_height) {
                /**实现更换地图**/
                Log.e(TAG, "onTouchEvent: " + "实现更换地图");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        synchronized (holder){
//                            Canvas canvas = holder.lockCanvas();
                            clearDraw(canvas);
                        }
                    }
                }).start();
//                arrayStack = UnitMaze.getArrayStack();
//                clearDraw(canvas);
//                drawUI();
            } else if (x >= getWidth() - left_border - 130 && x <= getWidth() - left_border && y >= 10 && y <= 10 + button_height) {
                /**实现生成路径**/
                Log.e(TAG, "onTouchEvent: " + "实现生成路径");
//                pathStack.clear();
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        synchronized (holder){
//                            canvas=holder.lockCanvas();
//                            drawPath(canvas);
//                        }
//                    }
//                }).start();
//                drawPath(canvas);
            }

        }
        return super.onTouchEvent(event);
    }

    private void clearDraw(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    private void drawPath(Canvas canvas) {

        Coordinate coordinate = new Coordinate(start_x, start_y);
        pathStack.push(coordinate);

        visit(start_x, start_y);
        Log.e(TAG, "drawPath: " + "栈长：" + pathStack.length());
        while (!pathStack.isEmpty()) {
            Coordinate pop = pathStack.pop();
            int x = pop.getX();
            int y = pop.getY();

            if (x == start_x && y == start_y) {
                // 如果该点是起点 则不用画
            } else
                canvas.drawCircle((left_border + x * cell_width + left_border + (x + 1) * cell_width) / 2, (top_border + y * cell_height + top_border + (y + 1) * cell_height) / 2, radius, pathPaint);
        }


    }


    private void visit(int start_x, int start_y) {
        maze[start_x][start_y] = 1;
        Coordinate coordinate = new Coordinate(start_y, start_x);
        int index = coordinateList.indexOf(coordinate);
        coordinate = coordinateList.get(index);

        if (start_x == end_x && start_y == end_y) {
            pathStack.pop();
            return;
        }
        if (start_x <= end_x && start_y <= end_y) {
            /**优先将下面的、右边的入栈**/

            Log.e(TAG, "visit: " + "优先将下面的、右边的入栈");

            /**将满足条件的下方格入栈**/
            if (coordinate.getA(1) == 1 && maze[start_x][start_y + 1] == 0) {
                Log.e(TAG, "visit: " + "下方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y + 1);
                pathStack.push(tmp);
                visit(start_x, start_y + 1);
            }

            /**将满足条件的右方格入栈**/
            else if (coordinate.getA(3) == 1 && maze[start_x + 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "右方格入栈");
                Coordinate tmp = new Coordinate(start_x + 1, start_y);
                pathStack.push(tmp);
                visit(start_x + 1, start_y);
            }

            /**将满足条件的上方格入栈**/
            else if (coordinate.getA(0) == 1 && maze[start_x][start_y - 1] == 0) {
                Log.e(TAG, "visit: " + "上方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y - 1);
                pathStack.push(tmp);
                visit(start_x, start_y - 1);
            }

            /**将满足条件的左方格入栈**/
            else if (coordinate.getA(2) == 1 && maze[start_x - 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "左方格入栈");
                Coordinate tmp = new Coordinate(start_x - 1, start_y);
                pathStack.push(tmp);
                visit(start_x - 1, start_y);
            } else {
                pathStack.pop();
                visit(pathStack.peek().getX(), pathStack.peek().getY());

            }


        } else if (start_x <= end_x && start_y > end_y) {
            /**优先将上面的、右边的入栈**/

            Log.e(TAG, "visit: " + "优先将上面的、右边的入栈");

            /**将满足条件的上方格入栈**/
            if (coordinate.getA(0) == 1 && maze[start_x][start_y - 1] == 0) {
                Log.e(TAG, "visit: " + "上方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y - 1);
                pathStack.push(tmp);
                visit(start_x, start_y - 1);
            }

            /**将满足条件的右方格入栈**/
            else if (coordinate.getA(3) == 1 && maze[start_x + 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "右方格入栈");
                Coordinate tmp = new Coordinate(start_x + 1, start_y);
                pathStack.push(tmp);
                visit(start_x + 1, start_y);
            }

            /**将满足条件的下方格入栈**/
            else if (coordinate.getA(1) == 1 && maze[start_x][start_y + 1] == 0) {
                Log.e(TAG, "visit: " + "下方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y + 1);
                pathStack.push(tmp);
                visit(start_x, start_y + 1);
            }

            /**将满足条件的左方格入栈**/
            else if (coordinate.getA(2) == 1 && maze[start_x - 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "左方格入栈");
                Coordinate tmp = new Coordinate(start_x - 1, start_y);
                pathStack.push(tmp);
                visit(start_x - 1, start_y);
            } else {
                pathStack.pop();
                visit(pathStack.peek().getX(), pathStack.peek().getY());


            }

        } else if (start_x > end_x && start_y <= end_y) {
            /**优先将下面的、左边的入栈**/

            Log.e(TAG, "visit: " + "优先将下面的、左边的入栈");

            /**将满足条件的下方格入栈**/
            if (coordinate.getA(1) == 1 && maze[start_x][start_y + 1] == 0) {
                Log.e(TAG, "visit: " + "下方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y + 1);
                pathStack.push(tmp);
                visit(start_x, start_y + 1);
            }

            /**将满足条件的左方格入栈**/
            else if (coordinate.getA(2) == 1 && maze[start_x - 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "左方格入栈");
                Coordinate tmp = new Coordinate(start_x - 1, start_y);
                pathStack.push(tmp);
                visit(start_x - 1, start_y);
            }

            /**将满足条件的上方格入栈**/
            else if (coordinate.getA(0) == 1 && maze[start_x][start_y - 1] == 0) {
                Log.e(TAG, "visit: " + "上方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y - 1);
                pathStack.push(tmp);
                visit(start_x, start_y - 1);
            }

            /**将满足条件的右方格入栈**/
            else if (coordinate.getA(3) == 1 && maze[start_x + 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "右方格入栈");
                Coordinate tmp = new Coordinate(start_x + 1, start_y);
                pathStack.push(tmp);
                visit(start_x + 1, start_y);
            } else {
                pathStack.pop();
                visit(pathStack.peek().getX(), pathStack.peek().getY());
            }

        } else {
            /**优先将上面的、左边的入栈**/

            Log.e(TAG, "visit: " + "优先将上面的、左边的入栈");

            /**将满足条件的上方格入栈**/
            if (coordinate.getA(0) == 1 && maze[start_x][start_y - 1] == 0) {
                Log.e(TAG, "visit: " + "上方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y - 1);
                pathStack.push(tmp);
                visit(start_x, start_y - 1);
            }

            /**将满足条件的左方格入栈**/
            else if (coordinate.getA(2) == 1 && maze[start_x - 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "左方格入栈");
                Coordinate tmp = new Coordinate(start_x - 1, start_y);
                pathStack.push(tmp);
                visit(start_x - 1, start_y);
            }

            /**将满足条件的下方格入栈**/
            else if (coordinate.getA(1) == 1 && maze[start_x][start_y + 1] == 0) {
                Log.e(TAG, "visit: " + "下方格入栈");
                Coordinate tmp = new Coordinate(start_x, start_y + 1);
                pathStack.push(tmp);
                visit(start_x, start_y + 1);
            }

            /**将满足条件的右方格入栈**/
            else if (coordinate.getA(3) == 1 && maze[start_x + 1][start_y] == 0) {
                Log.e(TAG, "visit: " + "右方格入栈");
                Coordinate tmp = new Coordinate(start_x + 1, start_y);
                pathStack.push(tmp);
                visit(start_x + 1, start_y);
            } else {
                pathStack.pop();
                visit(pathStack.peek().getX(), pathStack.peek().getY());


            }

        }
    }

    private void drawCanvas(Canvas canvas) {
        /**设置按钮的宽度、高度**/
        button_width = (getWidth() - left_border * 2 - 20) / 2;
        button_height = 0;

        top_border = 20 + button_height;

        /**设置单元格的宽度和高度**/
        cell_width = (getWidth() - left_border * 2) / row;
        cell_height = (getHeight() - top_border - 20) / column;

        while (!arrayStack.isEmpty()) {
            Coordinate coordinate = arrayStack.pop();
            coordinateList.add(coordinate);
            int i = coordinate.getX();
            int j = coordinate.getY();
            if (coordinate.getA(0) == 0)
                canvas.drawLine(left_border + j * cell_width, top_border + i * cell_height, left_border + (j + 1) * cell_width, top_border + i * cell_height, borderPanit);

            if (coordinate.getA(1) == 0)
                canvas.drawLine(left_border + j * cell_width, top_border + (i + 1) * cell_height, left_border + (j + 1) * cell_width, top_border + (i + 1) * cell_height, borderPanit);

            if (coordinate.getA(2) == 0)
                canvas.drawLine(left_border + j * cell_width, top_border + i * cell_height, left_border + j * cell_width, top_border + (i + 1) * cell_height, borderPanit);

            if (coordinate.getA(3) == 0)
                canvas.drawLine(left_border + (j + 1) * cell_width, top_border + i * cell_height, left_border + (j + 1) * cell_width, top_border + (i + 1) * cell_height, borderPanit);

        }

    }
}
