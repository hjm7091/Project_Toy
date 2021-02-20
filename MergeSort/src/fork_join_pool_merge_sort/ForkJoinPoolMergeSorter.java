package fork_join_pool_merge_sort;

import java.util.concurrent.RecursiveAction;

@SuppressWarnings("serial")
public class ForkJoinPoolMergeSorter extends RecursiveAction {
	 
    private final Integer[] arr;
    private final int left;
    private final int right;

    public ForkJoinPoolMergeSorter(Integer[] arr, int left, int right) {
        this.arr = arr;
        this.left = left;
        this.right = right;
    }

    @Override
    protected void compute() {
        if (right - left < 2) 
        	return;
                
        int mid = (left + right) / 2;
        invokeAll(new ForkJoinPoolMergeSorter(arr, left, mid), new ForkJoinPoolMergeSorter(arr, mid, right));
        merge(arr, left, mid, right);    
    }

    void merge(Integer[] arr, int left, int mid, int right) {
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
