package se.su.inlupp;
/*
PROG2 VT2026, Inläamningsuppgift, del 2
Grupp 071
Nevi Ståhl, nest9126
Luka Pavlovic, lupa0486
Mustafa Akinci, muak 3529*/

import javafx.application.Application;
import javafx.stage.Stage;

public class TravelApp extends Application {
    @Override
    public void start(Stage stage){
        TravelModel model = new TravelModel();
        new TravelView(model, stage);
    }

    public static void main(String [] args){
        launch(args);
    }
}
