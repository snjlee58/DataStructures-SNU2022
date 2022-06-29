import java.io.*;
import java.util.*;

public class SortingTest
{
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		try
		{
			boolean isRandom = false;	// 입력받은 배열이 난수인가 아닌가?
			int[] value;	// 입력 받을 숫자들의 배열
			String nums = br.readLine();	// 첫 줄을 입력 받음
			if (nums.charAt(0) == 'r')
			{
				// 난수일 경우
				isRandom = true;	// 난수임을 표시

				String[] nums_arg = nums.split(" ");

				int numsize = Integer.parseInt(nums_arg[1]);	// 총 갯수
				int rminimum = Integer.parseInt(nums_arg[2]);	// 최소값
				int rmaximum = Integer.parseInt(nums_arg[3]);	// 최대값

				Random rand = new Random();	// 난수 인스턴스를 생성한다.

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 각각의 배열에 난수를 생성하여 대입
					value[i] = rand.nextInt(rmaximum - rminimum + 1) + rminimum;
			}
			else
			{
				// 난수가 아닐 경우
				int numsize = Integer.parseInt(nums);

				value = new int[numsize];	// 배열을 생성한다.
				for (int i = 0; i < value.length; i++)	// 한줄씩 입력받아 배열원소로 대입
					value[i] = Integer.parseInt(br.readLine());
			}

			// 숫자 입력을 다 받았으므로 정렬 방법을 받아 그에 맞는 정렬을 수행한다.
			while (true)
			{
				int[] newvalue = (int[])value.clone();	// 원래 값의 보호를 위해 복사본을 생성한다.

				String command = br.readLine();

				long t = System.currentTimeMillis();
				switch (command.charAt(0))
				{
					case 'B':	// Bubble Sort
						newvalue = DoBubbleSort(newvalue);
						break;
					case 'I':	// Insertion Sort
						newvalue = DoInsertionSort(newvalue);
						break;
					case 'H':	// Heap Sort
						newvalue = DoHeapSort(newvalue, newvalue.length);
						break;
					case 'M':	// Merge Sort
						newvalue = DoMergeSort(newvalue);
						break;
					case 'Q':	// Quick Sort
						newvalue = DoQuickSort(newvalue, 0, newvalue.length-1);
						break;
					case 'R':	// Radix Sort
						newvalue = DoRadixSort(newvalue);
						break;
					case 'X':
						return;	// 프로그램을 종료한다.
					default:
						throw new IOException("잘못된 정렬 방법을 입력했습니다.");
				}
				if (isRandom)
				{
					// 난수일 경우 수행시간을 출력한다.
					System.out.println((System.currentTimeMillis() - t) + " ms");

				}
				else
				{
					// 난수가 아닐 경우 정렬된 결과값을 출력한다.
					for (int i = 0; i < newvalue.length; i++)
					{
						System.out.println(newvalue[i]);
					}
				}

			}
		}
		catch (IOException e)
		{
			System.out.println("입력이 잘못되었습니다. 오류 : " + e.toString());
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////

	private static int[] DoBubbleSort(int[] value)
	{
		boolean swapped = true;
		for (int last = value.length-1; last >=1; last--){
			swapped = false;
			for (int i = 0; i <= last-1; i++){
				if (value[i] > value[i+1]){
					int temp = value[i+1];
					value[i+1] = value[i];
					value[i] = temp;
					swapped = true;
				}
			}
			if (!swapped)
				break;
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoInsertionSort(int[] value)
	{
		for (int i = 1; i <= value.length-1; i++){
			int j = i-1;
			int insertionItem = value[i];
			while (0 <= j && insertionItem < value[j]){
				value[j+1] = value[j];
				j--;
			}
			value[j+1] = insertionItem;
		}
		return (value);
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoHeapSort(int[] A, int n)
	{
		BuildHeap(A);

		for (int i = n-1; i >= 1; i--){
			A[i] = deleteMax(A, i+1);
		}
		return (A);
	}

	private static void BuildHeap(int[] A){
		int n = A.length;
		for (int i = (n-2)/2; i >= 0; i--){
			percolateDownBuildHeap(A, i);
		}
	}

	private static void percolateDownBuildHeap(int[] A, int k){
		int n = A.length;
		int child = 2*k + 1, right = 2*k + 2;
		if (child <= n-1){
			if (right <= n-1 && A[child] < A[right]){
				child = right;
			}
			if (A[k] < A[child]){
				int temp = A[child];
				A[child] = A[k];
				A[k] = temp;
				percolateDownBuildHeap(A, child);
			}
		}
	}

	private static int deleteMax(int[] A, int n){
		int max = A[0];
		A[0] = A[n-1];
		percolateDownHeapSort(A, 0, n);
		return max;
	}

	private static void percolateDownHeapSort(int[] A, int k, int n){
		int child = 2*k + 1, right = 2*k + 2;
		if (child <= n-1){
			if (right <= n-1 && A[child] < A[right]){
				child = right;
			}
			if (A[k] < A[child]){
				int temp = A[child];
				A[child] = A[k];
				A[k] = temp;
				percolateDownHeapSort(A, child, n);
			}
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoMergeSort(int[] A){
		int[] B = new int[A.length];
		for (int i = 0; i < A.length; i++){
			B[i] = A[i];
		}
		switchingMergeSort(0, A.length-1, A, B);
		return (A);
	}

	private static void switchingMergeSort(int p, int r, int[] A, int B[]){
		if (p < r){
			int q = (p+r)/2;
			switchingMergeSort(p, q, B, A);
			switchingMergeSort(q+1, r, B, A);
			switchingMerge(p, q, r, B, A);
		}
	}

	private static void switchingMerge(int p, int q, int r, int C[], int D[]){
		int i = p, j = q+1, t = p;
		while (i <= q && j <= r){
			if (C[i] <= C[j]){
				D[t++] = C[i++];
			} else{
				D[t++] = C[j++];
			}
		}
		while (i <= q){
			D[t++] = C[i++];
		}
		while (j <= r){
			D[t++] = C[j++];
		}
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoQuickSort(int[] A, int p, int r)
	{
		if (p < r){
			int q = Partition(A, p, r);
			DoQuickSort(A, p, q-1);
			DoQuickSort(A, q+1, r);
		}

		return (A);
	}

	private static int Partition(int[] A, int p, int r){
		int x = A[r];
		int i = p-1;
		for (int j = p; j <= r-1; j++){
			if (A[j] < x) {
				int temp = A[++i];
				A[i] = A[j];
				A[j] = temp;
			}
		}
		int temp = A[r];
		A[r] = A[i+1];
		A[i+1] = temp;

		return i+1;
	}

	////////////////////////////////////////////////////////////////////////////////////////////////////
	private static int[] DoRadixSortPositiveArray(int[] A, int n, int maxElement) {
		for (int place = 1; maxElement / place > 0; place *= 10){
			DoCountingSort(A, n, place);
		}

		return (A);
	}

	private static int[] DoRadixSort(int[] A)
	{
		int positiveElementsCount = 0, negativeElementsCount = 0;
		int positiveMaxElement = -1, negativeMaxElement = -1;
		int[] positiveElements = new int[1000000];
		int[] negativeElements = new int[1000000];

		// Iterate through array A to 1. Find maximum element, 2. Collect positive and negative elements in separate arrays
		for (int i = 0; i < A.length; i++){
			if (A[i] >= 0){
				positiveElements[positiveElementsCount++] = A[i];

				if (A[i] > positiveMaxElement){
					positiveMaxElement = A[i];
				}
			} else{
				negativeElements[negativeElementsCount++] = A[i]*(-1);

				if (A[i]*(-1) > negativeMaxElement){
					negativeMaxElement = A[i]*(-1);
				}
			}
		}

		int[] positiveElementsSorted = DoRadixSortPositiveArray(positiveElements, positiveElementsCount, positiveMaxElement);
		int[] negativeElementsSorted = DoRadixSortPositiveArray(negativeElements, negativeElementsCount, negativeMaxElement);


		int i = 0, r = 0;
		for (int j = negativeElementsCount-1; j >=0; j--){
			A[i++] = negativeElementsSorted[j]*(-1);
		}
		while (r < positiveElementsCount){
			A[i++] = positiveElementsSorted[r++];
		}
		return (A);
	}

	private static int findMaxElement(int[] A){
		int maxElement = A[0];
		for (int i = 1; i < A.length; i++){
			if (A[i] > maxElement){
				maxElement = A[i];
			}
		}
		return maxElement;
	}

	private static void DoCountingSort(int[] A, int n, int place){
		int[] B = new int[n+1];
		int maxElement = findMaxElement(A);

		int[] C = new int[10];
		// Initialize counter array to 0
		for (int i = 0; i < 10; i++){
			C[i] = 0;
		}
		// Count number of occurrences of each number in A
		for (int j = 0; j < n; j++){
			C[(A[j] / place) % 10]++; // Count based on the digit place
		}
		// Calculate cumulative count
		for (int i = 1; i < 10; i++){
			C[i] = C[i] + C[i-1];
		}
		for (int j = n-1; j >= 0; j--){
			B[C[(A[j] / place) % 10] - 1] = A[j];
			C[(A[j] / place) % 10]--;
		}

		// Copy sorted array B to A
		for (int i = 0; i < n; i++){
			A[i] = B[i];
		}
}
}