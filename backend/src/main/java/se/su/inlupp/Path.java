/*Nevi Ståhl, nest9126
Luka Pavlovic, 0486
Mustafa Akinci, muak 3529*/
package se.su.inlupp;

import java.util.List;

public interface Path<T> extends Iterable<Edge<T>> {

  T getStart();

  T getEnd();

  int getTotalWeight();

  List<Edge<T>> getEdges();

  List<T> getNodes();
}

