/*Nevi Ståhl, nest9126
Luka Pavlovic, 0486
Mustafa Akinci, muak 3529*/
package se.su.inlupp;

import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class GraphPath<T> implements Path<T> {
    private T start;
    private List<Edge<T>> edges;

    public GraphPath(T start, List<Edge<T>> edges) {
        this.start = start;
        this.edges = edges;
    }

    public T getStart() {
        return start;
    }

    public T getEnd() {
        return edges.get(edges.size() - 1).getDestination();
    }

    public int getTotalWeight() {
        int total = 0;
        for (Edge<T> kant : edges) {
            total += kant.getWeight();
        }
        return total;
    }

    public List<Edge<T>> getEdges() {
        return new ArrayList<>(edges);
    }

    public List<T> getNodes() {
        List<T> nodeList = new ArrayList<>();
        nodeList.add(start);
        for (Edge<T> kant : edges) {
            nodeList.add(kant.getDestination());
        }
        return nodeList;
    }

    @Override
    public Iterator<Edge<T>> iterator() {
        return edges.iterator();
    }

    public String toString() {
      return "Startnod " + start + ", slutnod: " + getEnd() + ", kanter: " + edges + ", totalvikt: " + getTotalWeight();
    }

}
