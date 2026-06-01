package se.su.inlupp;
/*
PROG2 VT2026, Inläamningsuppgift, del 1
Grupp 071
Nevi Ståhl, nest9126
Luka Pavlovic, lupa0486
Mustafa Akinci, muak 3529*/

public interface Edge<T> {

  int getWeight();

  void setWeight(int weight);

  T getDestination();

  String getName();
}
