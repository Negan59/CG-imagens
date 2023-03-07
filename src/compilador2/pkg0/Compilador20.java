package compilador2.pkg0;

import Erro.Erro;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Compilador20 extends Application {

    public static Erro erro = new Erro();

    @Override
    public void start(Stage stage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Editor.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Compilador");
        stage.setScene(scene);
        stage.setFullScreen(false);
        
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
        

    }

}
