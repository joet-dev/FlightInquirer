package au.edu.usc;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Bucket sort implementation for sorting historical records of passenger flights.
 *
 * @author Joseph Thurlow
 */
public class BucketSort {

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
        // Finds the largest Value.
        double largestVal = 0;
        arr = new KeySet[data.size()];
        for(int i = 0; i<data.size(); i++) {
            double val = Double.parseDouble(data.get(i));
            if (val > largestVal) largestVal = val;
            double prepVal = prepData(val);
            arr[i] = new KeySet(i, prepVal);
        }

        // Run the sorting algorithm.
        long start = System.currentTimeMillis();
        bucketSort(arr, (int) largestVal + 1);
        long result = System.currentTimeMillis() - start;

        System.out.println("\nRuntime of " + this.getClass().getName() + " on " + columnName + ": " + result + "ms\n");
    }

    /**
     * Prepares the data for use with algorithm.
     *
     * @param val Unprepared data.
     * @return the value prepared for use with the bucket sort algorithm.
     */
    private Double prepData(Double val) {
        val *= 10;
        val = Double.parseDouble(new DecimalFormat("#").format(val));
        return val;
    }

    /**
     * Simple insertion sort algorithm.
     * Used to sort the 'buckets'.
     *
     * @param arr array to be sorted.
     */
    private static void insertSort (ArrayList<KeySet> arr) {

        int size = arr.size();

        for (int i = 0; i < size; ++i) {
            KeySet key = arr.get(i);
            int j = i - 1;

            // While j is larger than or equal to zero and val at j is bigger than val at increment.
            while (j >= 0 && arr.get(j).getVal().intValue() > key.getVal().intValue()) {
                arr.set(j + 1, arr.get(j));
                j--;
            }
            arr.set(j + 1, key);
        }
    }

    /**
     * The main recursive bucket sort operation.
     *
     * @param arr the array to be sorted.
     * @param size the size of the array.
     */
    private static void bucketSort(KeySet[] arr, int size) {
        if (size <= 0) return;

        @SuppressWarnings("unchecked")
        ArrayList<KeySet>[] bucketArr = new ArrayList[size];

        // Create buckets.
        for (int i = 0; i < size; i++) {
            bucketArr[i] = new ArrayList<>();
        }

        // Sort elements into buckets.
        for (KeySet set : arr) {
            int bucketIdx = set.getVal().intValue() / 10;
            bucketArr[bucketIdx].add(set);
        }

        // Sort the elements in each bucket.
        for (ArrayList<KeySet> bucket : bucketArr) {
            insertSort(bucket);
        }

        // Assemble array.
        int index = 0;
        for(List<KeySet> bucket : bucketArr) {
            for(KeySet val : bucket) {
                arr[index++] = val;
            }
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
        BucketSort sort = new BucketSort();
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
