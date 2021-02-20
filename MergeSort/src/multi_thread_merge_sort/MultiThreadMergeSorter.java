package multi_thread_merge_sort;

import common_merge_sort.CommonMergeSorter;

public class MultiThreadMergeSorter extends Thread {

    public static void sortArray(Integer[] arr, int threadCount) {
        sort(arr, 0, arr.length, threadCount);
    }

    private static void sort(Integer[] arr, int left, int right, int threadCount){
    	if(right - left < 2)
    		return;
    	
        if (threadCount <= 1) {
        	CommonMergeSorter.sort(arr, left, right);
        } else {
        	int mid = (left + right)/2;
            
            Thread firstHalf = new Thread(){
                public void run(){
                    sort(arr, left, mid, threadCount - 1);
                }
            };
            Thread secondHalf = new Thread(){
                public void run(){
                    sort(arr, mid, right, threadCount - 1);
                }
            };
        
            firstHalf.start();
            secondHalf.start();
    	
            try {
                firstHalf.join();
                secondHalf.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        
            merge(arr, left, mid, right);
        }
    
           
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
