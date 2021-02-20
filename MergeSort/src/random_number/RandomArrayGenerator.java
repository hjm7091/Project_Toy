package random_number;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

public class RandomArrayGenerator {

	public static Integer[] createRandomArray(int size, int range) {
		Set<Integer> set = new HashSet<Integer>();
		Integer[] arr = new Integer[size];
		while(set.size() < size) {
			Integer num = (int)(Math.random() * range) + 1;
			set.add(num);
		}
		arr = set.toArray(arr);
		return arr;
	}
	
	public static boolean checkSorted(Integer[] arr) {
		Comparator<Integer> comp = new Comparator<Integer>() {
            public int compare(Integer d1, Integer d2) {
                return d1.compareTo(d2);
            }
        };
        
        for(int i = 0; i < arr.length - 1; i++) {
        	if(comp.compare(arr[i], arr[i+1]) > 0) {
        		System.out.println(arr[i] + ">" + arr[i+1]);
        		return false;
        	}
        }
        return true;
	}
	
	public static Integer[] shuffle(Integer[] arr) {
		int size = arr.length;
		Integer[] copy = new Integer[size];
		System.arraycopy(arr, 0, copy, 0, size); 
		for(int i = 0; i < size; i++) {
			int randomIdx = (int)(Math.random() * size - i);
			swap(copy, i, i + randomIdx);
		}
		return copy;
	}
	
	private static void swap(Integer[] copy, int i, int j) {
		if(i != j) {
			Integer tmp = copy[i];
			copy[i] = copy[j];
			copy[j] = tmp;
		}
	}
}
