package au.edu.usc;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The shortest flight path between two cities can be found using a graph and Dijkstra's shortest path algorithm.
 *
 * @author Joseph Thurlow
 */
public class ItineraryExplorer {

    private final List<String> records = new ArrayList<>();
    private Set<String> origin = new HashSet<>();
    private Set<String> destination = new HashSet<>();
    private List<Node> graph = new ArrayList<>();
    private HashMap<String, Node> unique = new HashMap<>();

    /**
     * Reads the data from the csv file specified in the parameters.
     * Appends the required columns into lists for both origin and destination.
     * Adds Nodes to the "unique" HashMap to be used in the "graph" List.
     * Adds destinations to the nodes.
     * Creates a List of records containing unedited rows from the csv file for use when retrieving rank.
     *
     * @param file CVS file required for sorting by column.
     */
    public void sortRecords(String file) throws IOException {
        // Reads the csv file.
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine(); // To skip reading the first line.
            String line;
            while ((line = br.readLine()) != null) {
                records.add(line);
                // Use comma as separator.
                String[] cols = line.split(",");
                unique.put(cols[0], new Node(cols[0]));
                unique.put(cols[1], new Node(cols[1]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        for (String line : records) {
            String[] cols = line.split(",");
            boolean inList = false;

            for (Node n : graph) {
                if (n.getName().equals(cols[0])) {
//                        System.out.println("Already in list: " + cols[0] + " - " + cols[1] + " - " + cols[6]);
                    n.addDest(unique.get(cols[1]), Integer.parseInt(cols[6]));
                    inList = true;
                    break;
                }
            }
            if (!inList) {
//                    System.out.println("Not in list: " + cols[0] + " - " + cols[1] + " - " + cols[6]);
                graph.add(unique.get(cols[0]).addDest(unique.get(cols[1]), Integer.parseInt(cols[6])));
            }
            origin.add(cols[0]);
            destination.add(cols[1]);
        }
    }

    /**
     * Takes the input cities and finds the shortest path between them - if there is a path between the cities.
     *
     * @param city1 first city.
     * @param city2 second city.
     * @return the result of the calculations.
     */
    public String checkItinerary(String city1, String city2) {
        System.out.println("Searching Itineraries...");
        city1 = city1.toUpperCase();
        city2 = city2.toUpperCase();

        if (!origin.contains(city1)) {
            return "Origin city: " + city1 + " doesn't exist!";
        }
        if (!destination.contains(city2)) {
            return "Destination city: " + city2 + " doesn't exist!";
        }

        for (Node n : graph) {
            if (n.getName().equals(city1)) {
                getPath(n);
            }
        }

        if (unique.containsKey(city2) && !unique.get(city2).getShortestPath().isEmpty()) {
            String result = unique.get(city2).getShortestPath().stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(" -> ", "",""));
            return ("\nFlight itinerary: " + result + " -> " + city2.toUpperCase() + " | " + unique.get(city2).getDist() + " GC_KM");
        }
        return "\nThese 2 cities are not connected.";
    }

    /**
     * Used to add shortest path attributes to nodes.
     *
     * @param source the source location for where all paths will be mapped from.
     */
    public static void getPath(Node source) {
        source.setDist(0);

        // Known minimum distance from the source.
        Set<Node> setNodes = new HashSet<>();
        // Gathers nodes that can be reached from the source.
        Set<Node> unsetNodes = new HashSet<>();

        unsetNodes.add(source);

        while (unsetNodes.size() != 0) {
            // The adjacent node with the closest proximity.
            Node currentNode = getLowestDistanceNode(unsetNodes);
            unsetNodes.remove(currentNode);

            for (Map.Entry<Node, Integer> adjacencyPair:
                    currentNode.getAdjNodes().entrySet()) {

                // Gets the destination and distance for the closest adjacent node.
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();

                if (!setNodes.contains(adjacentNode)) {
                    calculateMinDist(adjacentNode, edgeWeight, currentNode);
                    unsetNodes.add(adjacentNode);
                }
            }
            setNodes.add(currentNode);
        }
    }

    /**
     * Returns the node with the shortest distance from the unsetNodes set.
     *
     * @param unsetNodes set of nodes.
     * @return the node with the shortest distance in a set.
     */
    public static Node getLowestDistanceNode(Set<Node> unsetNodes) {
        Node shortDistNode = null;
        int shortestDist = Integer.MAX_VALUE;
        // Loops through the Set of nodes to find the node with the lowest distance.
        for (Node node: unsetNodes) {
            int nodeDist = node.getDist();
            if (nodeDist < shortestDist) {
                shortestDist = nodeDist;
                shortDistNode = node;
            }
        }
        return shortDistNode;
    }

    /**
     * Checks whether the newly calculated distance is shorter than the actual distance.
     *
     * @param evaluationNode the node adjacent to the previously tested node that needs to be evaluated.
     * @param edgeWeigh the distance of the node adjacent to the previously tested node.
     * @param sourceNode the previously tested node.
     */
    private static void calculateMinDist(Node evaluationNode,
                                         Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDist();

        if (sourceDistance + edgeWeigh < evaluationNode.getDist()) {
            evaluationNode.setDist(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    public static void main(String[] args) throws IOException {
        ItineraryExplorer ex = new ItineraryExplorer();
        ex.sortRecords("./dataset/flights_201812.csv");

        System.out.println(ex.checkItinerary("CANBERRA", "TOWNSVILLE"));
    }
}
