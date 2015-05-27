package fr.tse.fi2.hpp.labs.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveAction;

public class MergeSortPara extends RecursiveAction   {
	
	private int[] liste;
	private int longueur;
	
	public MergeSortPara(int[] t) {
		this.liste = t;
		this.longueur = t.length;
	}

	@Override
	protected void compute() {
		// TODO Auto-generated method stub
		 if(this.longueur > 20) {
	            List<MergeSortPara> subtasks = new ArrayList<MergeSortPara>();
	            subtasks.addAll(createSubtasks());
	            for(RecursiveAction subtask : subtasks){
	                subtask.fork();
	            }
	            for(RecursiveAction subtask : subtasks){
	                subtask.join();
	            }
	            //merge
	            fusion(subtasks.get(0).liste,subtasks.get(1).liste);
	        }
		 else{
			 insertionSort(liste);
		 }
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
	
	 private List<MergeSortPara> createSubtasks() {
	        List<MergeSortPara> subtasks = new ArrayList<MergeSortPara>();
			int[] b1 = new int[liste.length/2];
		    int [] b2 = new int[liste.length-liste.length/2];
	        System.arraycopy(liste, 0, b1, 0, liste.length/2);
			System.arraycopy(liste, liste.length/2, b2, 0, liste.length-liste.length/2);
	        MergeSortPara subtask1 = new MergeSortPara(b1);
	        MergeSortPara subtask2 = new MergeSortPara(b2);
	        subtasks.add(subtask1);
	        subtasks.add(subtask2);
	        return subtasks;
	    }
	
}