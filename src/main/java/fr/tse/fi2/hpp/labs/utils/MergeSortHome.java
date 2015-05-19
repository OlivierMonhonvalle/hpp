package fr.tse.fi2.hpp.labs.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import fr.tse.fi2.hpp.labs.beans.DebsRecord;
import fr.tse.fi2.hpp.labs.queries.AbstractQueryProcessor;

public class MergeSortHome  {
	

	public MergeSortHome() {
		// TODO Auto-generated constructor stub
	}
	
	public static int[] triestp(int[] a){
		int[] b1 = new int[a.length/2];
	    int [] b2 = new int[a.length-a.length/2];
		int[] r1 = new int[a.length/2];
	    int [] r2 = new int[a.length-a.length/2];
		
	 
		if (a.length==1){
			return a;
		}
		else if (a.length == 2)
		{
			if(a[0]<a[1])
			{
				return a;
			}
			else
			{
				int[] b = null;
				b[0] = a[1];
				b[1] = a[0];
				return b;
			}
		}
		else
		{
			System.arraycopy(a, 0, b1, 0, a.length/2);
			System.arraycopy(a, a.length/2, b2, 0, a.length-a.length/2);
		}
		
		if (b1.length <= 20){
			return insertionSort(b1);
		}
		if (b2.length <= 20){
			return insertionSort(b2);
		}
		r1 = triestp(b1);
		r2 = triestp(b2);
		return fusion(r1,r2);
	}
	
	private static int[] fusion(int[] tab1,int [] tab2)
	{
		int k=0;
		int index1=0;
		int index2=0;
		int[] rst = new int[tab1.length+tab2.length];
		while(k<rst.length){
			if (index1==tab1.length){
				try {
					System.arraycopy(tab2,index2,rst,k,tab2.length-index2);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			}
			if (index2==tab2.length){
				try {
					System.arraycopy(tab1,index1,rst,k,tab1.length-index1);
				} catch (Exception e) {
					// TODO: handle exception
				}
				break;
			}
			if(tab1[index1]<tab2[index2]){
				rst[k]=tab1[index1];
				index1++;
				k++;
			}
			else{
				rst[k]=tab2[index2];
				index2++;
				k++;	
			}
		}
		return rst;
	}
	
	public static int[] insertionSort(int [] a)
	{
		for (int i=1; i < a.length; i++){
			int temp = a[i];
			int j;
			for (j = i - 1; j >= 0 && temp <a[j]; j--)
				a[j+1] = a[j];
			a[j+1] = temp;
		}
		return a;
	}
	
	public static void jhmtest(int[] a){
		triestp(a);
	}
	
}
