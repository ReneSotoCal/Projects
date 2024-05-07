
public class Quicksort {
	
		//static variables to count the key comparisons and swaps
		static int lomutoKC = 0;
		static int hoareKC = 0;
		static int lomutoSwapCount = 0;
		static int hoareSwapCount = 0;
		
	public static void main(String[] args) {
		
		//Initialization of test arrays
		int arr[] = new int[100];
//		int arr[] = new int[1000];
//		int arr[] = new int[10000];
		int arr2[] = new int[100];
//		int arr2[] = new int[1000];
//		int arr2[] = new int[10000];
		
		//Method calls to populate the array with different orders
//		populateAsc(arr); //Populates arr1 in an ascending order of distinct numbers
		populateDesc(arr); //Populates arr1 in a descending order of distinct numbers
//		populateSame(arr); //Populates arr with the same numbers		
//		populateRand(arr); //Populates arr1 with random distinct numbers
		
		arr2 = arr.clone(); //Clones arr1 to arr2 so both arrays have the same order and numbers before sorting


		//Calling both types of quicksorts with their respective arrays
		lomutoQuicksort(arr, 0, arr.length - 1);
		hoareQuicksort(arr2, 0, arr.length - 1);
		
		//Prints the first twenty sorted items in both arrays
		for(int i = 0; i < 20; i++) {
			System.out.println(arr[i] + "	" + arr2[i]);
		}
		
		//Prints the key comparison counts and swap counts for Lomuto's and Hoare's quicksort.
		System.out.println();
		System.out.println("Lomuto's Key Comparison Count = " + lomutoKC);
		System.out.println("Lomuto's Swap Count = " + lomutoSwapCount);
		System.out.println("Hoare's Key Comparison Count = " + hoareKC);
		System.out.println("Hoare's Swap Count = " + hoareSwapCount);
	}
	
	//Lomuto's Quicksort algorithm implementation
	public static void lomutoQuicksort(int[] nums, int p, int r) {
		
		if(p < r) {
			int q = lomutoPartition(nums, p, r); // Conquer
			lomutoQuicksort(nums, p, q - 1); // Divide
			lomutoQuicksort(nums, q + 1, r); // Divide
		}
	}
	
	//Lomuto's Partition algorithm implementation
	public static int lomutoPartition(int[] nums, int p, int r) {
		
		int x = nums[r];
		int i = p-1; 
		
		for(int j = p; j < r; j++) { // Loop from the starting index to the penultimate index
			if(nums[j] <= x) {
				i++;
				swap(nums, i, j); // Swaps elements at indexes i & j
				lomutoSwapCount++; //Increments Lomuto's swap count by one
			}
			lomutoKC++; //Increment Lomuto's key comparison count by one
		}
		
		i++;
		swap(nums, i, r);// Swaps elements at indexes i & r
		lomutoSwapCount++;// increment Lomuto's swap count by one
		
		return i; //return the index of the element used as a pivot
	}
	
	public static void hoareQuicksort(int[] nums, int p, int r) {
		
		if(p < r) {
			int q = hoarePartition(nums, p, r);
			hoareQuicksort(nums, p, q);
			hoareQuicksort(nums, q + 1, r);
		}
	}
	
	public static int hoarePartition(int[] nums, int p, int r) {
		
		int x = nums[p];
		int i = p - 1;
		int j = r + 1;
		
		while(true) {
			
			do {
				i++;
				hoareKC++;
			} while(nums[i] < x);
			
			do {
				j--;
				hoareKC++;
			} while(nums[j] > x);
			
			if(i < j) {
				swap(nums, i, j);
				hoareSwapCount++;
			} else {
				return j;
			}
		}
	}
	
	//Populate an array with random numbers from 1 - 100000
	public static void populateRand(int arr[]) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = 1 + (int)(Math.random() * 100000);
		}
	}
	
	//Populate an array with the same number
	public static void populateSame(int arr[]) {
		for(int i = 0; i < arr.length; i++) {
			arr[i] = 5;
		}
	}

	//Populate an array with descending numbers
	public static void populateDesc(int arr[]) {
		int j = 0;
		for(int i = arr.length; i > 0; i--) {
			arr[j] = i;
			j++;
		}
	}
	
	//Populate an array with ascending numbers
	public static void populateAsc(int arr[]) {
		for(int i = 1; i <= arr.length; i ++) {
			arr[i-1] = i;
		}
	}
	
	//Swap method to swap to elements in an array
	public static void swap(int[] arr, int i, int j) {
		int temp = arr[i];
		arr[i] = arr[j];
		arr[j] = temp;
	}
}
