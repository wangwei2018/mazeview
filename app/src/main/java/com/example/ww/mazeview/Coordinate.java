package com.example.ww.mazeview;

/**
 *
 * @author WW
 *
 */
public class Coordinate {
	private int x;	// x 横坐标
	private int y;	// y 纵坐标

	int a[];
	
	public Coordinate(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.a=new int[4];
		for(int i=0;i<4;i++)
			a[i]=0;

	}

	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setA(int i){
		a[i]=1;
	}

	public int getA(int i){
		return a[i];
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Coordinate) {
			Coordinate coordinate=(Coordinate)obj;
			if(coordinate.getX()==this.x&&coordinate.getY()==this.y)
				return true;
		}
		return false;
		
	}

}
