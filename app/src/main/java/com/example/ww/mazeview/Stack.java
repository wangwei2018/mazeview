package com.example.ww.mazeview;

/**
 * ջ�Ĳ�������
 * @author WW
 *
 */
public interface Stack<T> {
	 /*�п�*/
    boolean isEmpty();
    
    /*���ջ*/
    void clear();
    
    /*��ջ*/
    T pop();
    
    /*��ջ*/
    boolean push(T data);
    
    /*ջ�ĳ���*/
    int length();
    
    /*�鿴ջ����Ԫ�أ������Ƴ���*/
    T peek();
    
    /*���ض�����ջ�е�λ��*/
    int search(T t);

}
