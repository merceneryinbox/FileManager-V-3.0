/*.
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.FileInputStream;

/**
 * @author mercenery
 */
public class FileManager extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("FXMLMainForm.fxml"));
        setUserAgentStylesheet(Application.STYLESHEET_CASPIAN);
        primaryStage.setTitle("Fedulov Oleg (mercenery@inbox.ru; +7 921 989 68 90) file's commander");
        primaryStage.getIcons().add(new Image(FileManager.class.getResourceAsStream("foldericons/bsd.png")));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setAlwaysOnTop(false);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
