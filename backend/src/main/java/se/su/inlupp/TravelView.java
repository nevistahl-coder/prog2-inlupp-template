package se.su.inlupp;
/*
PROG2 VT2026, Inläamningsuppgift, del 2
Grupp 071
Nevi Ståhl, nest9126
Luka Pavlovic, lupa0486
Mustafa Akinci, muak 3529*/

import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.text.*;
import javafx.stage.*;


import java.io.File;
import java.io.IOException;
import java.util.*;


public class TravelView {

    private TravelModel model;
    private Stage stage;

    private Pane mapPane = new Pane();
    private ImageView backgroundImage = new ImageView();
    private ComboBox<String> algorithmBox = new ComboBox<>();
    private ArrayList<String> selectedCities = new ArrayList<>();

    private MenuItem newItem = new MenuItem("New");
    private MenuItem openItem = new MenuItem("Open");
    private MenuItem saveItem = new MenuItem("Save");
    private MenuItem exitItem = new MenuItem("Exit");
    private MenuItem loadImageItem = new MenuItem("Load Image");

    private Button aButton = new Button("Add city");
    private Button cButton = new Button("Add connection");
    private Button fButton = new Button("Find path");
    private Button rButton = new Button("Remove");

    public TravelView(TravelModel model, Stage stage) {
        this.model = model;
        this.stage = stage;

        MenuBar menuBar = new MenuBar();
        Menu filMenu = new Menu("File");

        filMenu.getItems().addAll(newItem, openItem, saveItem, loadImageItem, exitItem);
        menuBar.getMenus().add(filMenu);

        HBox hBox = new HBox(aButton, cButton, fButton, rButton);

        algorithmBox.getItems().addAll("BFS", "DFS");
        algorithmBox.setValue("BFS");

        Label algorithmLabel = new Label("Algorithm");
        HBox algorithmBox2 = new HBox(algorithmLabel, algorithmBox);

        BorderPane borderPane = new BorderPane();

        VBox topBox = new VBox(menuBar, hBox, algorithmBox2);
        borderPane.setTop(topBox);
        borderPane.setCenter(mapPane);

        Scene scene = new Scene(borderPane, 600, 800);
        stage.setScene(scene);
        stage.setTitle("Reseplaneraren");
        stage.show();

        exitItem.setOnAction(e -> {
            if (model.hasUnsavedChanges()) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText("You have unsaved changes, are you sure you want to exit?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK)
                    stage.close();
            } else {
                stage.close();
            }
        });

        aButton.setOnAction(e -> handleAddCity());

        cButton.setOnAction(e -> handleAddConnection());

        fButton.setOnAction(e -> handleFindPath());

        rButton.setOnAction(e -> handleRemove());

        newItem.setOnAction(e -> handleNew());

        saveItem.setOnAction(e -> handleSave());

        openItem.setOnAction(e -> handleOpen());

        loadImageItem.setOnAction(e -> handleLoadImage());

        algorithmBox.setOnAction(e -> {
            if (algorithmBox.getValue().equals("BFS")) {
                model.setPathFinder(new BFSPathFinder<>());
            } else {
                model.setPathFinder(new DFSPathFinder<>());
            }
        });
    }

    private void handleAddCity() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add city");
        dialog.setHeaderText("Enter a city: ");
        Optional<String> result = dialog.showAndWait();

        if (result.isPresent() && !result.get().isBlank()) {
            String cityName = result.get();
            mapPane.setOnMouseClicked(e -> {
                model.addCity(cityName, e.getX(), e.getY());
                drawCity(cityName, e.getX(), e.getY());
                mapPane.setOnMouseClicked(null);
            });

        }
    }

    private void drawCity(String name, double x, double y) {
        Circle circle = new Circle(x, y, 10);

        circle.setOnMouseClicked(e -> {
            if (selectedCities.contains(name)) {
                selectedCities.remove(name);
                circle.setFill(Color.BLUE);
            } else if (selectedCities.size() < 2) {
                selectedCities.add(name);
                circle.setFill(Color.RED);
            }
            e.consume();
        });

        circle.setFill(Color.BLUE);
        Text text = new Text(x + 12, y, name);

        circle.setOnMouseDragged(e -> {
            double newX = e.getX();
            double newY = e.getY();
            circle.setCenterX(newX);
            circle.setCenterY(newY);
            text.setX(newX + 12);
            text.setY(newY);
            model.getCityData(name).setX(newX);
            model.getCityData(name).setY(newY);
            redrawEdges();
            e.consume();
        });

        mapPane.getChildren().addAll(circle, text);
    }

    private void redrawEdges() {
        mapPane.getChildren().removeIf(node -> "edge".equals(node.getId()));
        for (String city : model.getCities()) {
            for (Edge<String> edge : model.getEdgesFrom(city)) {
                if (city.compareTo(edge.getDestination()) < 0) {
                    drawEdge(city, edge.getDestination(), edge.getName(), edge.getWeight());

                }
            }
        }
    }

    private void drawEdge(String city1, String city2, String name, int weight) {
        CityData c1 = model.getCityData(city1);
        CityData c2 = model.getCityData(city2);

        Line line = new Line(c1.getX(), c1.getY(), c2.getX(), c2.getY());
        line.setId("edge");
        mapPane.getChildren().add(line);
        line.toBack();
    }

    private void handleAddConnection() {
        if (selectedCities.size() != 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong number of cities selected");
            alert.showAndWait();
            return;
        }

        TextInputDialog nameDialog = new TextInputDialog();
        nameDialog.setTitle("Add conecction");
        nameDialog.setHeaderText("Enter connection name: ");
        Optional<String> nameResult = nameDialog.showAndWait();

        TextInputDialog weightDialog = new TextInputDialog();
        weightDialog.setTitle("Add conecction");
        weightDialog.setHeaderText("Enter connection weight");
        Optional<String> weightResult = weightDialog.showAndWait();

        if (nameResult.isPresent() && !nameResult.get().isBlank() && weightResult.isPresent()
                && !weightResult.get().isBlank()) {
            try {
                int weight = Integer.parseInt(weightResult.get());

                String city1 = selectedCities.get(0);
                String city2 = selectedCities.get(1);
                model.connectCities(city1, city2, nameResult.get(), weight);
                drawEdge(city1, city2, nameResult.get(), weight);

            } catch(NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Weight must be a number");
                alert.showAndWait();
            }catch(IllegalArgumentException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Weight cannot be negative");
                alert.showAndWait();
            }catch(IllegalStateException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText("Cities are already connected");
                alert.showAndWait();
            }
        }
    }

    private void handleFindPath() {
        if (selectedCities.size() != 2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong number of cities selected");
            alert.showAndWait();
            return;
        }
        String city1 = selectedCities.get(0);
        String city2 = selectedCities.get(1);
        Path<String> result = model.findPath(city1, city2);

        if (result == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Value cannot be Null");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Path found");
            alert.setHeaderText("Path from city " + city1 + " to " + city2);
            alert.setContentText(result.toString());
            alert.showAndWait();
        }
    }

    private void handleRemove() {
        List<String> toRemove = new ArrayList<>(selectedCities);

        if (toRemove.size() < 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Wrong number of selected cities");
            alert.showAndWait();
            return;
        }
        for (String city : toRemove) {
            model.removeCity(city);
        }
        redrawAll();
        selectedCities.clear();
    }

    private void redrawAll() {

        mapPane.getChildren().clear();
        mapPane.getChildren().add(backgroundImage);

        for (String city : model.getCities()) {
            CityData data = model.getCityData(city);
            drawCity(city, data.getX(), data.getY());
        }
        redrawEdges();
    }

    private void handleNew() {
        if (model.hasUnsavedChanges()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes, are you sure you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                model.clear();
                redrawAll();
                selectedCities.clear();
            }

        } else {
            model.clear();
            redrawAll();
            selectedCities.clear();
        }
    }

    private void handleSave() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showSaveDialog(stage);
        if (file == null)
            return;

        try {
            model.saveToFile(file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void handleOpen() {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(stage);
        if (file == null)
            return;

        if (model.hasUnsavedChanges()) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Unsaved Changes");
            alert.setHeaderText("You have unsaved changes, are you sure you want to continue?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.OK)
                return;
        }

        try {
            model.loadFromFile(file.getAbsolutePath());
            if (model.getImagePath() != null) {
                loadImage(model.getImagePath());
            }
            selectedCities.clear();
            redrawAll();

        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    private void loadImage(String path) {
        Image image = new Image("file:" + path);
        backgroundImage.setImage(image);
        backgroundImage.setFitWidth(mapPane.getWidth());
        backgroundImage.setFitHeight(mapPane.getHeight());
        if (!mapPane.getChildren().contains(backgroundImage)) {
            mapPane.getChildren().add(0, backgroundImage);
        }
    }

    private void handleLoadImage(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Image files", "*.png", "*.jpg", "*.jpeg"));
            File file = fileChooser.showOpenDialog(stage);
            if(file==null) return;

            String path = file.getAbsolutePath();
            model.setImagePath(path);
            loadImage(path);

    }

}
