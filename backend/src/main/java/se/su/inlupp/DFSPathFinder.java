package se.su.inlupp;
/*
PROG2 VT2026, Inläamningsuppgift, del 1
Grupp 071
Nevi Ståhl, nest9126
Luka Pavlovic, lupa0486
Mustafa Akinci, muak 3529*/

import java.util.*;

public class DFSPathFinder<T> implements PathFinder<T> {

  @Override
  public Path<T> findPath(Graph<T> graph, T from, T to) {
    Set<T> visited = new HashSet<>();
    List<Edge<T>> path = new ArrayList<>();
    List<Edge<T>> result = dfs(graph, from, to, visited, path);

    if (result == null) {
      return null;
    }
    return new GraphPath<>(from, result);

  }

  private List<Edge<T>> dfs(Graph<T> graph, T current, T to, Set<T> visited, List<Edge<T>> path) {
    visited.add(current);
    if (current.equals(to)) {
      return path;
    }
    for (Edge<T> kant : graph.getEdgesFrom(current)) {
      if (!visited.contains(kant.getDestination())) {
        path.add(kant);
        List<Edge<T>> result = dfs(graph, kant.getDestination(), to, visited, path);
        if (result != null) {
          return result;
        } else {
          path.remove(path.size() - 1);
        }
      }
    }

    return null;
  }
}
