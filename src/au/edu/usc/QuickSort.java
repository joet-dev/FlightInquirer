package au.edu.usc;

import java.io.*;
import java.util.*;

/**
 * Quick sort implementation for sorting historical records of passenger flights.
 *
 * @author Joseph Thurlow
 */
public class QuickSort {

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
        quickSort(arr, 0, arr.length-1);
        long result = System.currentTimeMillis() - start;

        System.out.println("\nRuntime of " + this.getClass().getName() + " on " + columnName + ": " + result + "ms\n");
    }

    /**
     * Swaps the elements so that any elements less than the pivot are on the left
     * and elements more than the pivot ar on the right.
     *
     * @param arr the array to be sorted.
     * @param low the lowest index of the unsorted part of the array.
     * @param high the highest index of the unsorted part of the array.
     * @return the index for the pivot point after sorting.
     */
    private static int part(KeySet[] arr, int low, int high) {
        KeySet pivot = arr[high]; // The pivot is the last element in the array.
        int i = (low-1);

        for (int j = low; j < high; j++) {
            if (arr[j].getVal() <= pivot.getVal()) {
                i++;

                KeySet temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }

        KeySet temp = arr[i+1];
        arr[i+1] = arr[high];
        arr[high] = temp;

        return i+1;
    }

    /**
     * The main recursive quick sort operation.
     *
     * @param arr the array to be sorted.
     * @param low the first index of the array.
     * @param high the last index of the array.
     */
    private static void quickSort(KeySet[] arr, int low, int high) {
        if (low < high) {
            int partIdx = part(arr, low, high);

            // Starts two recursive calls to part sub-arrays below the pivot and above the pivot
            quickSort(arr, low, partIdx-1);
            quickSort(arr, partIdx+1, high);
        }
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
        QuickSort sort = new QuickSort();
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
