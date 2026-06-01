package se.su.inlupp;
/*
PROG2 VT2026, Inläamningsuppgift, del 2
Grupp 071
Nevi Ståhl, nest9126
Luka Pavlovic, lupa0486
Mustafa Akinci, muak 3529*/

// Lagrar stadens namn och position på kartan.
// Används för att kunna spara och läsa in koordinater eftersom grafen bara lagrar stadsnamn.

public class CityData {

    private String name;
    private double x;
    private double y;

    public CityData(String name, double x, double y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    public String getName() {
        return name;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return name + " (" + x + ", " + y + ")";
    }
}
