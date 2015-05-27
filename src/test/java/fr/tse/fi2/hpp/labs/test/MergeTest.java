package fr.tse.fi2.hpp.labs.test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

import fr.tse.fi2.hpp.labs.utils.MergeSortHome;
import fr.tse.fi2.hpp.labs.utils.MergeSortPara;

public class MergeTest {

	@Test
	public void test() {
		int [] t = generateT(100);
		int [] tab = t;
		Arrays.sort(tab);
		MergeSortPara tri1 = new MergeSortPara(t);
		int cores = Runtime.getRuntime().availableProcessors();
		ForkJoinPool forkJoinPool = new ForkJoinPool(cores);
		forkJoinPool.invoke(tri1);
		//assert(tab == forkJoinPool.invoke(tri1));
	}
	
	public int[] generateT(int taille)
	{
		int[] n = new int[taille];
		Random r = new Random();
		
		for(int i =0; i<taille; i++){
			n[i] = r.nextInt(i+1);
		}
		
		return n;
	}
}
