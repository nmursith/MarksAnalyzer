package Marks;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.CacheHint;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;


import java.io.*;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.*;
public class Solution extends Application {
    int id = 0;

    @Override
    public void start(Stage primaryStage) throws Exception{




        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("marksVisualizer.fxml"));

        Parent root = fxmlLoader.load();

        root.setCache(true);
        root.setCacheHint(CacheHint.DEFAULT);
        primaryStage.setTitle("Marks Visualizer");
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);

        scene.getStylesheets().add(getClass().getResource("theme.css").toExternalForm());
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        Image ico = new Image(getClass().getResourceAsStream("appIcon.jpg"));
        primaryStage.getIcons().add(ico);

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());
        primaryStage.show();

        System.out.println("Controller Starting");
        Controller controller= fxmlLoader.<Controller>getController();
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                System.out.println(controller);
                controller.init();
                controller.setStage(primaryStage);
            }
        });

        scene.setOnMousePressed(new EventHandler() {
            @Override
            public void handle(Event event) {
//                System.out.println("X: " + mouseEvent.getX() + " Y: " + mouseEvent.getY());
                if(id<35) {
                    controller.save(id);
                    id++;
                }
            }


        });
    }
    public Solution(){


    }


    public static void main(String[] args) {

        launch(args);


        /*System.out.println(sol.table.get("B7").getName());
        System.out.println(sol.table.get("B7").getIndexNo());
        System.out.println(sol.table.get("B7").getSeriesChem().get(1));*/
    }




}
