package test;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.ForkJoinPool;

import org.junit.Test;

import fork_join_pool_merge_sort.ForkJoinPoolMergeSorter;
import random_number.RandomArrayGenerator;

public class ForkJoinPoolMergeSorterTest {
	
	@Test
	public void testSize_100000() {
		int size = 100000; //배열의 크기
		int range = Integer.MAX_VALUE; //숫자의 범위, 범위는 최소한 배열의 크기와 같아야함
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkJoinPoolMergeSorter(arr, 0, arr.length));
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}

	@Test
	public void testSize_1000000() {
		int size = 1000000; //배열의 크기
		int range = Integer.MAX_VALUE; //숫자의 범위, 범위는 최소한 배열의 크기와 같아야함
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkJoinPoolMergeSorter(arr, 0, arr.length));
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
	@Test
	public void testSize_10000000() {
		int size = 10000000; //배열의 크기
		int range = Integer.MAX_VALUE; //숫자의 범위, 범위는 최소한 배열의 크기와 같아야함
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkJoinPoolMergeSorter(arr, 0, arr.length));
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}

	@Test
	public void testSize_15000000() {
		int size = 15000000; //배열의 크기
		int range = Integer.MAX_VALUE; //숫자의 범위, 범위는 최소한 배열의 크기와 같아야함
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkJoinPoolMergeSorter(arr, 0, arr.length));
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
	@Test
	public void testSize_50000000() {
		int size = 50000000; //배열의 크기
		int range = Integer.MAX_VALUE; //숫자의 범위, 범위는 최소한 배열의 크기와 같아야함
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new ForkJoinPoolMergeSorter(arr, 0, arr.length));
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
}
