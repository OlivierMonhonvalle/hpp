//package fr.tse.fi2.hpp.labs.utils;
//
//import java.util.ArrayList;
//import java.util.Random;
//import java.util.concurrent.ForkJoinPool;
//import java.util.concurrent.TimeUnit;
//
//import org.openjdk.jmh.annotations.Benchmark;
//import org.openjdk.jmh.annotations.BenchmarkMode;
//import org.openjdk.jmh.annotations.Fork;
//import org.openjdk.jmh.annotations.Measurement;
//import org.openjdk.jmh.annotations.Mode;
//import org.openjdk.jmh.annotations.OutputTimeUnit;
//import org.openjdk.jmh.annotations.Scope;
//import org.openjdk.jmh.annotations.Setup;
//import org.openjdk.jmh.annotations.State;
//import org.openjdk.jmh.annotations.Warmup;
//
//
//
//
//@State(Scope.Thread)
//public class JHMMergeSort {
//
//	private int[] t; 
//
//	
//	@Setup
//	public void prepare(){
//		t = generateT(1000000);
//	}
//	
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
//	
//	@Benchmark
//	@BenchmarkMode(Mode.AverageTime)
//    @OutputTimeUnit(TimeUnit.MILLISECONDS)
//	@Fork(1)
//	@Warmup(iterations=3)
//	@Measurement(iterations=3)
//	public boolean testMethod(){
//		//MergeSortHome.jhmtest(t);
//		MergeSortPara tri1 = new MergeSortPara(t);
//		int cores = Runtime.getRuntime().availableProcessors();
//		ForkJoinPool forkJoinPool = new ForkJoinPool(cores);
//		forkJoinPool.invoke(tri1);
//		return true;
//	}
//	
//	
//
//}