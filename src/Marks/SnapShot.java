package Marks;

/**
 * Created by ROSE on 11/18/2016.
 */
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import javafx.application.Application;
import javafx.event.*;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import javafx.scene.shape.Rectangle;
import javafx.stage.*;

import javax.imageio.ImageIO;

public class SnapShot extends Application {
    private final Circle    circle = new Circle(100, 100, 75, Color.RED);
    private final Rectangle rectangle = new Rectangle(120, 130, 100, 80);

    public static void main(String[] args) { launch(args); }
    @Override public void start(Stage stage) throws Exception {
        rectangle.setFill(Color.GREEN);
        Group group = new Group(circle, rectangle);

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().setAll(group);

        Scene scene = new Scene(layout);
        stage.setTitle("Primary");
        stage.setScene(scene);
        stage.show();



        showControlStage(stage);
    }

    private void showControlStage(final Stage owner) {
        Button snapshotButton = new Button("Take Snapshot");
        snapshotButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent t) {
                try {
                    Screen screen = Screen.getPrimary();
                    Rectangle2D bounds = screen.getVisualBounds();
                    //Dimension dimension = new Dimension(bounds.getWidth(), bounds.getHeight());

                    BufferedImage screencapture = new Robot().createScreenCapture(new java.awt.Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));

                    // Save as JPEG
                    File file = new File("screencapture.jpg");
                    ImageIO.write(screencapture, "jpg", file);
                } catch (AWTException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                // Save as PNG
                // File file = new File("screencapture.png");
                // ImageIO.write(screencapture, "png", file);


                Image snapshot = takeSnapshot(owner.getScene(), rectangle);
                showSnapshotStage(owner, snapshot);
            }
        });

        VBox layout = new VBox(20);
        layout.setAlignment(Pos.CENTER);
        layout.setStyle("-fx-background-color: cornsilk; -fx-padding: 10;");
        layout.getChildren().setAll(snapshotButton);

        Stage controlStage = new Stage(StageStyle.UTILITY);
        controlStage.initOwner(owner);
        controlStage.setX(0); controlStage.setY(0);
        controlStage.setTitle("Control");
        controlStage.setScene(new Scene(layout));
        controlStage.show();
    }

    private void showSnapshotStage(final Stage owner, final Image snapshot) {
        ImageView snapshotView = new ImageView(snapshot);

        Stage controlStage = new Stage(StageStyle.UTILITY);
        controlStage.initOwner(owner);
        controlStage.setTitle("Snapshot: " + new Date());
        controlStage.setScene(new Scene(new Group(snapshotView)));
        controlStage.show();
    }

    public Image takeSnapshot(Scene scene, final Node... hideNodes) {
        for (Node node: hideNodes) node.setVisible(false);
        Image image = scene.snapshot(null);
        for (Node node: hideNodes) node.setVisible(true);

        return image;
    }
}