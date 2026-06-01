package se.su.inlupp;
/*
PROG2 VT2026, Inläamningsuppgift, del 1
Grupp 071
Nevi Ståhl, nest9126
Luka Pavlovic, lupa0486
Mustafa Akinci, muak 3529*/

import java.util.*;

public class ListGraph<T> implements Graph<T> {

  private Map<T, List<Edge<T>>> nodes = new HashMap<>();

  @Override
  public void add(T node) {
    if (!nodes.containsKey(node)) {
      nodes.put(node, new ArrayList<>());
    }
  }

  @Override
  public void remove(T node) {
    if (!nodes.containsKey(node)) {
      throw new NoSuchElementException("Noden finns inte");
    }
    for (T n : nodes.keySet()) {
      nodes.get(n).removeIf(kant -> kant.getDestination().equals(node));
    }
    nodes.remove(node);
  }

  @Override
  public boolean hasNode(T node) {
    return nodes.containsKey(node);
  }

  @Override
  public void connect(T node1, T node2, String name, int weight) {
    if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
      throw new NoSuchElementException("Någen eller båda noderna finns inte");
    } else if (weight < 0) {
      throw new IllegalArgumentException("Vikten är negativ");
    } else if (getEdgeBetween(node1, node2) != null) {
      throw new IllegalStateException("Kanten finns redan");
    }
    ListEdge edge1 = new ListEdge(name, weight, node2);
    nodes.get(node1).add(edge1);
    ListEdge edge2 = new ListEdge(name, weight, node1);
    nodes.get(node2).add(edge2);

  }

  @Override
  public void disconnect(T node1, T node2) {
    if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
      throw new NoSuchElementException("Någen eller båda noderna finns inte");
    } else if (getEdgeBetween(node1, node2) == null) {
      throw new IllegalStateException("Kanten finns inte");
    }
    nodes.get(node1).removeIf(kant -> kant.getDestination().equals(node2));
    nodes.get(node2).removeIf(kant -> kant.getDestination().equals(node1));
  }

  @Override
  public void setConnectionWeight(T node1, T node2, int weight) {
    if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
      throw new NoSuchElementException("Någen eller båda noderna finns inte");
    } else if (getEdgeBetween(node1, node2) == null) {
      throw new NoSuchElementException("Kanten finns inte");
    } else if (weight < 0) {
      throw new IllegalArgumentException("Vikten är negativ");
    }
    getEdgeBetween(node1, node2).setWeight(weight);
    getEdgeBetween(node2, node1).setWeight(weight);
  }

  @Override
  public Set<T> getNodes() {
    return new HashSet<>(nodes.keySet());
  }

  @Override
  public Collection<Edge<T>> getEdgesFrom(T node) {
    if (!nodes.containsKey(node)) {
      throw new NoSuchElementException("Noden finns inte");
    }
    return new ArrayList<>(nodes.get(node));
  }

  @Override
  public Edge<T> getEdgeBetween(T node1, T node2) {
    if (!nodes.containsKey(node1) || !nodes.containsKey(node2)) {
      throw new NoSuchElementException("Någen eller båda noderna finns inte");
    }

    for (Edge<T> kant : nodes.get(node1)) {
      if (kant.getDestination().equals(node2)) {
        return kant;
      }
    }
    return null;
  }

  @Override
  public String toString() {
    String result = "";
    for (T node : nodes.keySet()) {
      result += node + ": " + nodes.get(node) + "\n";
    }
    return result;
  }

  @Override
  public Iterator<T> iterator() {
    return nodes.keySet().iterator();
  }

  private class ListEdge implements Edge<T> {
    private String name;
    private int weight;
    private T destination;

    public ListEdge(String name, int weight, T destination) {
      this.name = name;
      this.weight = weight;
      this.destination = destination;
    }

    public int getWeight() {
      return weight;
    }

    public void setWeight(int weight) {
      if (weight < 0) {
        throw new IllegalArgumentException("Vikten är negativ");
      }
      this.weight = weight;
    }

    public T getDestination() {
      return destination;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return "till " + destination + " med " + name + " tar " + weight;
    }

  }
}
