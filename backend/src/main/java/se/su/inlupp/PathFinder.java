/*Nevi Ståhl, nest9126
Luka Pavlovic, 0486
Mustafa Akinci, muak 3529*/
package se.su.inlupp;

public interface PathFinder<T> {

  Path<T> findPath(Graph<T> graph, T from, T to);
}

