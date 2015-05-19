//package fr.tse.fi2.hpp.labs.test;
//
//import java.util.Arrays;
//import java.util.Random;
//
//import org.junit.Test;
//
//import fr.tse.fi2.hpp.labs.utils.MergeSortHome;
//
//public class MergeTest {
//
//	@Test
//	public void test() {
//		int [] t = generateT(100);
//		int [] tab = t;
//		Arrays.sort(tab);
//		assert(tab == MergeSortHome.triestp(t));
//	}
//	
//	public int[] generateT(int taille)
//	{
//		int[] n = new int[taille];
//		Random r = new Random();
//		
//		for(int i =0; i<taille; i++){
//			n[i] = r.nextInt(i+1);
//		}
//		
//		return n;
//	}
//}
