package common_merge_sort;

public class CommonMergeSorter {

	public static void sortArray(Integer[] arr) {
		sort(arr, 0, arr.length);
	}
	
	public static void sort(Integer[] arr, int left, int right) {
		if(right - left < 2)
			return;
		int mid = (left + right) / 2;
		sort(arr, left, mid);
		sort(arr, mid, right);
		merge(arr, left, mid, right);
	}
	
	private static void merge(Integer[] arr, int left, int mid, int right) {
		int[] tmp = new int[right - left];
		int t = 0, l = left, r = mid;
		
		while(l < mid && r < right) {
			if(arr[l] < arr[r])
				tmp[t++] = arr[l++];
			else
				tmp[t++] = arr[r++];
		}
		
		while(l < mid)
			tmp[t++] = arr[l++];
		
		while(r < right)
			tmp[t++] = arr[r++];
		
		for(int i = left; i < right; i++)
			arr[i] = tmp[i - left];
	}
	
}
