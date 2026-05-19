/*Nevi Ståhl, nest9126
Luka Pavlovic, 0486
Mustafa Akinci, muak 3529*/
package se.su.inlupp;

import java.util.*;

public class BFSPathFinder<T> implements PathFinder<T> {

  @Override
  public Path<T> findPath(Graph<T> graph, T from, T to) {
    Queue<T> queue = new LinkedList<>();
    Map<T, Edge<T>> previous = new HashMap<>();
    Map<T, T> nodeMap = new HashMap<>();
    queue.add(from);
    previous.put(from, null);
    while (!queue.isEmpty()) {
      T current = queue.poll();
      if (current.equals(to)) {
        List<Edge<T>> path = new ArrayList<>();
        current = to;
        while (!current.equals(from)) {
          Edge<T> kant = previous.get(current);
          path.add(kant);
          current = nodeMap.get(current);

        }
        Collections.reverse(path);
        return new GraphPath<>(from, path);
      }

      for (Edge<T> kant : graph.getEdgesFrom(current)) {
        if (!previous.containsKey(kant.getDestination())) {
          previous.put(kant.getDestination(), kant);
          nodeMap.put(kant.getDestination(), current);
          queue.add(kant.getDestination());

        }
      }
    }

    return null;
  }
}
