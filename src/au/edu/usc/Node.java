package au.edu.usc;

import java.util.*;

/**
 * Used to create nodes that represent source cities including their adjacent cities and distance between them.
 *
 * @Author Joseph Thurlow
 */
public class Node {

    private final String name;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer dist = Integer.MAX_VALUE;

    Map<Node, Integer> adjNodes = new HashMap<>();

    public Node addDest(Node destination, int dist) {
        adjNodes.put(destination, dist);
        return this;
    }

    public Node(String name) {
        this.name = name;
    }

    public void setDist(int dist) {
        this.dist = dist;
    }

    public Integer getDist() {
        return dist;
    }

    public String getName() {
        return name;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> sp) {
        shortestPath = sp;
    }

    public Map<Node, Integer> getAdjNodes() {
        return adjNodes;
    }

    public String toString() {
        return name;
    }
}
