
package ghops.gprint;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class Gprint extends Application{

 
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
            
        System.out.println("sdds");
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        
        //stage.getIcons().add(new Image("file:logo.png"));
        stage.setTitle("GPrint ");
        Scene scene = new Scene(root ,1500,600);
       // scene.getStylesheets().add(GPrint.class.getResource("style.css").toURI().toURL().toExternalForm());
        
        stage.setScene(scene);

        Button b = new Button("");
        stage.show();   
    }
    
}
