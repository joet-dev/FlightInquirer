package au.edu.usc;

import java.io.*;
import java.util.*;


/**
 * Merge sort implementation for sorting historical records of passenger flights.
 *
 * @author Joseph Thurlow
 */
public class MergeSort {

    private KeySet[] arr;
    private final List<String> records = new ArrayList<>();
    private String columnName;

    /**
     * Reads the data from the csv file specified in the parameters.
     * Appends the specified column of data into List.
     * Creates a List of records containing unedited rows from the csv file for use when retrieving rank.
     *
     * @param file CVS file required for sorting by column.
     * @param column Specifies the column to sort by.
     */
    public void sortRecords(String file, int column) throws IOException {
        List<String> data = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            columnName = br.readLine().split(",")[column - 1]; // To skip reading the first line.
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
                // Use comma as separator.
                String[] cols = line.split(",");
                data.add(cols[column - 1]);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Append all data to array (arr) as 'set' objects.
        arr = new KeySet[data.size()];
        for(int i = 0; i<data.size(); i++) {
            arr[i] = new KeySet(i, Double.parseDouble(data.get(i)));
        }

        // Run the sorting algorithm.
        long start = System.currentTimeMillis();
        mergeSort(arr);
        long result = System.currentTimeMillis() - start;

        System.out.println("\nRuntime of " + this.getClass().getName() + " on " + columnName + ": " + result + "ms\n");
    }

    /**
     * Recursive compare and merge operation for mergeSort function.
     *
     * @param left lhs sub-array of arr.
     * @param right rhs sub-array of arr.
     * @param arr full array to be merged.
     * @param leftBound contains the index where the lhs of the array ends.
     * @param rightBound contains the index where the rhs of the array ends.
     */
    private static void merge(KeySet[] left, KeySet[] right, KeySet[] arr, int leftBound, int rightBound) {
        int leftIdx = 0, rightIdx = 0, arrIdx = 0;

        while (leftIdx < leftBound && rightIdx < rightBound) {
            if(left[leftIdx].getVal() <= right[rightIdx].getVal()) arr[arrIdx++] = left[leftIdx++];
            else arr[arrIdx++] = right[rightIdx++];
        }

        while (leftIdx < leftBound) {
            arr[arrIdx++] = left[leftIdx++];
        }

        while (rightIdx < rightBound) {
            arr[arrIdx++] = right[rightIdx++];
        }
    }

    /**
     * The main recursive merge sort operation.
     *
     * @param arr Array to complete the merge sort algorithm on.
     */
    private static void mergeSort(KeySet[] arr) {
        int n = arr.length;
        if (n < 2) return;

        // Recursively divide the array into halves.
        int mid = n/2;
        KeySet[] left = Arrays.copyOfRange(arr, 0, mid);
        KeySet[] right = Arrays.copyOfRange(arr, mid, n);

        mergeSort(left);
        mergeSort(right);

        // Merge both sides together.
        merge(left, right, arr, mid, n - mid);
    }

    /**
     * Returns the flight records with the given ranking order.
     *
     * @param rank Specifies the rank of the records desired.
     */
    public void get(int rank) {
        // Checks to make sure the records are imported and sorted before rank lookup.
        if (arr == null) {
            System.out.println("Records must be imported and sorted before rank is called!");
            return;
        }

        // Finds and prints records with the desired rank.
        if (rank == 0) {
            System.out.println(Arrays.toString(arr)); // Prints the entire ordered list of keys and values.
        } else if (rank < 0) {
            System.out.println("ERROR: Invalid rank input.");
        } else {
            System.out.println("RECORDS for " + columnName + " at RANK " + rank + ":");
            int iterateRank = 1;
            boolean recordFound = false;
            double currentVal = arr[arr.length - 1].getVal();

            for (int i = arr.length - 1; i > -1; i--) {

                if (currentVal != arr[i].getVal()) {
                    if (recordFound) break;
                    currentVal = arr[i].getVal();
                    iterateRank++;
                }

                if (rank == iterateRank) {
                    recordFound = true;
                    System.out.println("ROW " + (arr[i].getIdx() + 2) + ": " + records.get(arr[i].getIdx()));
                }
            }
            if (!recordFound) System.out.println("No RECORDS found for " + columnName + " at RANK " + rank + "!");
        }
    }

    public static void main(String[] args) throws IOException {
        MergeSort sort = new MergeSort();
        sort.sortRecords("./dataset/dom_citypairs_web.csv", 6);

        // Change parameter value below to get records according to rank.
        // Note: The parameter value 0 will print a sorted list of all the Set objects
        // The format for a set is {original_array_index, value}.
        sort.get(5);

        // Uncomment below for bulk output of records divided by rank.
//        for (int i = 0; i<20; i++) {
//            System.out.println("");
//            sort.get(i+1);
//        }
    }
}
