package test;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import multi_thread_merge_sort.MultiThreadMergeSorter;
import random_number.RandomArrayGenerator;

public class MultiThreadMergeSorterTest {

	@Test
	public void testSize_100000() {
		final int size = 100000; //�迭�� ũ��
		final int range = Integer.MAX_VALUE; //������ ����, ������ �ּ��� �迭�� ũ��� ���ƾ���
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		MultiThreadMergeSorter.sortArray(arr, 8);
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
	@Test
	public void testSize_1000000() {
		final int size = 1000000; //�迭�� ũ��
		final int range = Integer.MAX_VALUE; //������ ����, ������ �ּ��� �迭�� ũ��� ���ƾ���
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		MultiThreadMergeSorter.sortArray(arr, 8);
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
	@Test
	public void testSize_10000000() {
		final int size = 10000000; //�迭�� ũ��
		final int range = Integer.MAX_VALUE; //������ ����, ������ �ּ��� �迭�� ũ��� ���ƾ���
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		MultiThreadMergeSorter.sortArray(arr, 8);
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
	@Test
	public void testSize_15000000() {
		final int size = 15000000; //�迭�� ũ��
		final int range = Integer.MAX_VALUE; //������ ����, ������ �ּ��� �迭�� ũ��� ���ƾ���
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		MultiThreadMergeSorter.sortArray(arr, 8);
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
	@Test
	public void testSize_50000000() {
		final int size = 50000000; //�迭�� ũ��
		final int range = Integer.MAX_VALUE; //������ ����, ������ �ּ��� �迭�� ũ��� ���ƾ���
		Integer[] arr = RandomArrayGenerator.createRandomArray(size, range);
		long start = System.currentTimeMillis();
		MultiThreadMergeSorter.sortArray(arr, 8);
		long end = System.currentTimeMillis();
		long diff = end - start;
		System.out.printf("%10d elements  =>  %6d ms \n", size, diff);
		assertTrue(RandomArrayGenerator.checkSorted(arr));
	}
	
}
