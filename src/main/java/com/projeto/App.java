package com.projeto;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;
    private static String origemAdmin;

    @Override
    public void start(Stage stage) throws IOException {
        // Determine windowed kiosk size based on primary screen, keeping 9:16 portrait
        Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
        double screenH = bounds.getHeight();
        // Target up to 90% of height; derive width by 9:16 aspect
        double targetH = Math.min(1920, screenH * 0.9);
        double targetW = Math.min(1080, targetH * 9.0 / 16.0);
        scene = new Scene(loadFXML("home"), targetW, targetH);
        
        // aplica stylesheet global
        String css = App.class.getResource("/com/projeto/styles.css") != null
            ? App.class.getResource("/com/projeto/styles.css").toExternalForm()
            : null;
        if (css != null) {
            scene.getStylesheets().add(css);
        }
        stage.setScene(scene);
        stage.setResizable(false);
        stage.setMinWidth(targetW);
        stage.setMinHeight(targetH);
        stage.setMaxWidth(targetW);
        stage.setMaxHeight(targetH);
        stage.centerOnScreen();
        stage.setTitle("Totem Self-Service");
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    public static void navegarParaAdmin(String origem) throws IOException {
        origemAdmin = origem;
        setRoot("admin");
    }

    public static String getOrigemAdmin() {
        return origemAdmin;
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}