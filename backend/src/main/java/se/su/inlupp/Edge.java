/*Nevi Ståhl, nest9126
Luka Pavlovic, 0486
Mustafa Akinci, muak 3529*/
package se.su.inlupp;

public interface Edge<T> {

  int getWeight();

  void setWeight(int weight);

  T getDestination();

  String getName();
}
