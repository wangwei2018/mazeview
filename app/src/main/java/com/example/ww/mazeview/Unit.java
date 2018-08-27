package com.example.ww.mazeview;

/**
 * ѡ��һ����ջ˳�� 0��1��2��3 �ֱ��ʾ���ϡ��¡�����
 * @author WW
 *
 */
public class Unit {
	public static int get_order(int i) {
		int[] orders=new int[24];
		orders[0]=123;
		orders[1]=132;
		orders[2]=213;
		orders[3]=231;
		orders[4]=312;
		orders[5]=321;
		
		orders[6]=1023;
		orders[7]=1032;
		orders[8]=1203;
		orders[9]=1230;
		orders[10]=1302;
		orders[11]=1320;
		
		orders[12]=2013;
		orders[13]=2031;
		orders[14]=2103;
		orders[15]=2130;
		orders[16]=2301;
		orders[17]=2310;
		
		orders[18]=3012;
		orders[19]=3021;
		orders[20]=3102;
		orders[21]=3120;
		orders[22]=3201;
		orders[23]=3210;
		
		
		return orders[i];
		
	}
	
	

}
