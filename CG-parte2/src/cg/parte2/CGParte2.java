/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cg.parte2;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class CGParte2 extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("CanetaMaster.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Primitivas Gráficas e Transformações 2D");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("image.png")));
        stage.setScene(scene);
        stage.setFullScreen(false);
        
        stage.show();
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}
