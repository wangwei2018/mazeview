package com.example.ww.mazeview;

public class ArrayStack<T> implements Stack<T>{
	private int size=0;
	private T[] t = (T[]) new Object[16];

	@Override
	public boolean isEmpty() {
		return size==0;
	}

	@Override
	public void clear() {
		for(int i=0;i<size;i++) {
			t[i]=null;
		}
		size=0;		
	}

	@Override
	public T pop() {
		if(size==0)
			return null;
		T tem=t[size-1];
		t[size-1]=null;
		size--;
		return tem;	
	}

	@Override
	public boolean push(T data) {
		if(size>=t.length)
			resize();
		t[size++]=data;
		return true;
	}

	private void resize() {
		T[] t2=(T[]) new Object[t.length*2];
		for(int i=0;i<t.length;i++) {
			t2[i]=t[i];
			t[i]=null;
		}
		t=t2;
		t2=null;
	}

	@Override
	public int length() {
		return size;
	}

	@Override
	public T peek() {
		if(size==0)
			return null;
		return t[size-1];
	}

	@Override
	public int search(T t) {
		for(int i=0;i<size;i++)
			if(t.equals(this.t[i]))
				return i;
		return -1;	//��ʾû���ҵ�
	}
	

}
