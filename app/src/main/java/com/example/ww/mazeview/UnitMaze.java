package com.example.ww.mazeview;

import java.util.Random;

public class UnitMaze {
    private static int[][] maze;
    private static ArrayStack<Coordinate> arrayStack;
    private static int column;
    private static int row;
    private static ArrayStack<Coordinate> stack;
    private static Coordinate present;

    public static int[][] createMaze(int column,int row){
        UnitMaze.column=column;
        UnitMaze.row=row;
        maze= new int[column][row];
        for(int i=0;i<column;i++)
            for(int j=0;j<row;j++)
                maze[i][j]=0;
        Random random = new Random();
        int x = random.nextInt(column);
        int y = random.nextInt(row);
        present = new Coordinate(x, y);

        stack=new ArrayStack<>();
        arrayStack = new ArrayStack<>();

        stack.push(present);
        arrayStack.push(present);
        fill_maze(x,y);
        return maze;

    }

    private static void fill_maze(int i,int j){
        maze[i][j]=1;
//        结束位置的坐标
        if(arrayStack.isEmpty())
            return ;
        Random random = new Random();
        int order = Unit.get_order(random.nextInt(24));
        int first=order/1000;
        int second=order/100-first*10;
        int forth=order%10;
        int third=(order%100)/10;

        push_2_stack(i,j,first);
        push_2_stack(i,j,second);
        push_2_stack(i,j,third);
        push_2_stack(i,j,forth);

        stack.push(present);

        present = arrayStack.pop();
        fill_maze(present.getX(), present.getY());

    }


    /**
     * 将满足条件的相邻方格入栈
     * @param i		x坐标
     * @param j		y坐标
     * @param order		操作指令 0、1、2、3 分别代表：上、下、左、右
     */
    private static void push_2_stack(int i, int j, int order) {
        switch(order) {
            case 0:
                if(i-1>=0&&maze[i-1][j]==0) {
                    Coordinate coordinate = new Coordinate(i-1, j);
                    if(arrayStack.search(coordinate)==-1){
                        present.setA(0);
                        coordinate.setA(1);
                        stack.push(coordinate);
                        arrayStack.push(coordinate);
                    }
                }
                break;
            case 1:
                if(i+1<column&&maze[i+1][j]==0) {
                    Coordinate coordinate = new Coordinate(i+1, j);
                    if(arrayStack.search(coordinate)==-1){
                        present.setA(1);
                        coordinate.setA(0);
                        stack.push(coordinate);
                        arrayStack.push(coordinate);
                    }

                }
                break;
            case 2:
                if(j-1>=0&&maze[i][j-1]==0) {
                    Coordinate coordinate = new Coordinate(i, j-1);
                    if(arrayStack.search(coordinate)==-1){
                        present.setA(2);
                        coordinate.setA(3);
                        stack.push(coordinate);
                        arrayStack.push(coordinate);
                    }
                }
                break;
            case 3:
                if(j+1<row&&maze[i][j+1]==0) {
                    Coordinate coordinate = new Coordinate(i, j+1);
                    if(arrayStack.search(coordinate)==-1){
                        present.setA(3);
                        coordinate.setA(2);
                        stack.push(coordinate);
                        arrayStack.push(coordinate);
                    }
                }
                break;

        }
    }

    public static ArrayStack<Coordinate> getArrayStack(){
        createMaze(column,row);
        return stack;
    }

}
