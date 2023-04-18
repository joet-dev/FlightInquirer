# Flight Inquirer :airplane: 

## Description
A program that implements merge sort, quick sort and bucket sort algorithms to efficiently sort historical flight records. There is also a itinerary explorer to calculate the shortest flight path between two cities. 

*Merge-Sort*

Merge sort is a divide-and-conquer algorithm. It sorts through data by recursively dividing an input array into two halves and then merging the two sorted halves together. At each level of the recursive tree, O(n) time is spent sorting the elements. The height of the tree is presented as O(log n), therefore, this merge sort algorithm runs in O(n log n) logarithmic time, where n is the size of the input array. The space complexity is O(n) or linear. 

*Quick-Sort*

Quick-sort is another recursive, divide-and-conquer algorithm, but unlike the merge sort algorithm, it has a pivot element. The array is partially sorted by a function (‘part’) that swaps values so that values smaller than the pivot are on the left of it and values larger are on the right of it. The position of the pivot at the end of partitioning is used to divide the array into a lower part and upper part which will continue to be recursively sorted and divided. The average time for this quick sort algorithm is O(n log n), where n is the number of elements in the array. In the case where the pivot element selected is always the largest or smallest element, quick sort runs in O(n^2) time - which is its worst-case time complexity. The space complexity is O(n) or linear. 

*Bucket-Sort*

The bucket-sort algorithm works by distributing elements into their respective buckets (ArrayList) according to their values. Each bucket is then sorted using the insertion-sort algorithm which simply moves values from the unsorted portion of the array and places them in their correct position in the sorted portion. After all the buckets are sorted, they are combined to create one fully sorted array. The worst-case time complexity for bucket-sort is O(n^2) – which is effectively the worst-case time for the insertion-sort algorithm that is used to sort the buckets. The best case time complexity is O(n + k) where n is the number of operations used to sort the elements and k is the number of buckets that were created (as the creation of one bucket takes O(1) time). A factor to note is that bucket sort is most effectively used when the values are uniformly distributed, as each bucket will have a similar number of values to sort. The space complexity of the bucket-sort algorithm is O(n + k), where n represents the number of elements in the array and k represents the number of buckets used. 

*Itinerary Explorer* 

The itinerary explorer uses a combination of a graph (a list of nodes) and Dijkstra’s shortest path algorithm to calculate the shortest path between two cities. 
Nodes are the primary method of data storage and calculation. They contain the origin city name, a list of the shortest-path cities from the origin, the total distance of the shortest-path, and a HashMap of the adjacent nodes and their distance from the origin node. Firstly, the csv file is read, and full records are added to an array list. All cities are put into a HashMap (‘unique’) that receives a string as the key and a node as the value. The HashMap ensures each city has only one node to associate with it. The ArrayList graph is then populated by one of each origin city nodes and destination cities and their distances are added to the suitable origin city nodes in accordance with the records. Once the ‘graph’ is populated by origin nodes and their adjacent nodes, the ‘checkItinerary’ method can be ran. The two input cities are checked for validity before being searched for in the ‘graph’. The requested origin node is passed on to the ‘getPath’ method where nodes are updated to contain the shortest path to them from the requested origin node. A string is then returned with the shortest flight path between the two input cities along with the total distance. 
From my analysis, the worst-case time complexity of Dijkstra’s algorithm in my implementation is O(n*m), where n is the number of nodes and m is the total number of nodes directly or indirectly adjacent to the origin node. 


