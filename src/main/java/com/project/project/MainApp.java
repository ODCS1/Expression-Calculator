package com.project.project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("tela.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("App Expression Calculator");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/icons/java-icon.png")));

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}