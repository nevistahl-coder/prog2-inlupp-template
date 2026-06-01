
package se.su.inlupp;
/*
PROG2 VT2026, Inläamningsuppgift, del 2
Grupp 071
Nevi Ståhl, nest9126
Luka Pavlovic, lupa0486
Mustafa Akinci, muak 3529*/

import java.util.*;
import java.io.*;

public class TravelModel {

    private ListGraph<String> graph = new ListGraph<>();
    private PathFinder<String> pathFinder = new BFSPathFinder<>();
    private Map<String, CityData> cityPositions = new HashMap<>();
    private String imagePath = null;
    private boolean unsavedChanges = false;

    // Noder
    public void addCity(String name, double x, double y) {
        graph.add(name);
        CityData cityData = new CityData(name, x, y);
        cityPositions.put(name, cityData);
        unsavedChanges = true;
    }

    public void removeCity(String name) {
        graph.remove(name);
        cityPositions.remove(name);
        unsavedChanges = true;
    }

    public boolean hasCity(String name) {
        return graph.hasNode(name);
    }

    public Set<String> getCities() {
        return graph.getNodes();
    }

    public CityData getCityData(String name) {
        return cityPositions.get(name);
    }

    // Kanter

    public void connectCities(String city1, String city2, String name, int weight) {
        graph.connect(city1, city2, name, weight);
        unsavedChanges = true;

    }

    public void disconnectCities(String city1, String city2) {
        graph.disconnect(city1, city2);
        unsavedChanges = true;
    }

    public Edge<String> getEdgeBetween(String city1, String city2) {
        return graph.getEdgeBetween(city1, city2);
    }

    public Collection<Edge<String>> getEdgesFrom(String city) {
        return graph.getEdgesFrom(city);
    }

    public void setPathFinder(PathFinder<String> pathfinder) {
        this.pathFinder = pathfinder;
    }

    public Path<String> findPath(String from, String to) {
        return pathFinder.findPath(graph, from, to);
    }

    public void setImagePath(String path) {
        this.imagePath = path;
        unsavedChanges = true;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean hasUnsavedChanges() {
        return unsavedChanges;
    }

    public void saveToFile(String filename) throws IOException {
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(filename))) {
            printWriter.println(imagePath != null ? imagePath : "");

            for (CityData city : cityPositions.values()) {
                printWriter.println(city.getName() + ";" + city.getX() + ";" + city.getY());
            }
            printWriter.println("---");

            Set<String> written = new HashSet<>();
            for (String city : graph.getNodes()) {
                for (Edge<String> edge : graph.getEdgesFrom(city)) {
                    String key = city + ";" + edge.getDestination();
                    String reverseKey = edge.getDestination() + ";" + city;
                    if (!written.contains(reverseKey)) {
                        printWriter.println(
                                city + ";" + edge.getDestination() + ";" + edge.getName() + ";" + edge.getWeight());
                        written.add(key);
                    }

                }
            }
            unsavedChanges = false;
        }

    }

    public void loadFromFile(String filename) throws IOException {
        clear();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filename))) {
            String line = bufferedReader.readLine();
            imagePath = line.isBlank() ? null : line;
            // Läs städer rad för rad tills separatorn "---"
            while (!(line = bufferedReader.readLine()).equals("---")) {
                String[] splits = line.split(";");
                addCity(splits[0], Double.parseDouble(splits[1]), Double.parseDouble(splits[2]));
            }
            // Läs kanter rad för rad tills filens slut
            while ((line = bufferedReader.readLine()) != null) {
                String[] splits = line.split(";");
                graph.connect(splits[0], splits[1], splits[2], Integer.parseInt(splits[3]));
            }
            unsavedChanges = false;
        }

    }

    public void clear() {
        graph = new ListGraph<>();
        cityPositions.clear();
        imagePath = null;
        unsavedChanges = false;
    }

}
