package filemanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class FXMLConfirmDelete implements Initializable {
    @FXML
    public Button btnOkDelete;

    private volatile Path pathToDelete;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pathToDelete = FXMLMainFormController.selectedPath;

    }

    public synchronized void setYesConfirm(ActionEvent actionEvent) {
            if (pathToDelete != null) {
                if (Files.isDirectory(pathToDelete)) {
                    try (DirectoryStream<Path> ds = Files.newDirectoryStream(pathToDelete)) {
                        for (Path file : ds) {
                            Files.deleteIfExists(file);
                        }
                        Files.delete(pathToDelete);

                        Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION);
                        deleteAlert.setContentText("Successefully delete -- " + pathToDelete + " --");
                        deleteAlert.show();
                    } catch (IOException e) {
                        Alert errDeleteFile = new Alert(Alert.AlertType.ERROR);
                        errDeleteFile.setContentText("Error to delete ++ " + pathToDelete + " ++ because of " + e.getMessage());
                        errDeleteFile.show();
                        System.err.println(e);
                    }
                } else if (!Files.isDirectory(pathToDelete)) {
                    try {
                        Files.deleteIfExists(FXMLMainFormController.selectedPath);
                        Alert deleteAlert = new Alert(Alert.AlertType.INFORMATION);
                        deleteAlert.setContentText("Successefully delete -- " + pathToDelete + " --");
                        deleteAlert.show();
                    } catch (IOException e) {
                        Alert errDeleteFile = new Alert(Alert.AlertType.ERROR);
                        errDeleteFile.setContentText("Error to delete ++ " + pathToDelete + " ++ because of " + e.getMessage() + "\n--------------");
                        errDeleteFile.show();

                        e.printStackTrace();
                    }
                }
            } else {
                Alert errDeleteFile = new Alert(Alert.AlertType.ERROR);
                errDeleteFile.setContentText("Error to delete NOTHING\n\n select any item to delete first !");
                errDeleteFile.show();

                Node closeSourceNode = (Node) actionEvent.getSource();
                Stage closeStage = (Stage) closeSourceNode.getScene().getWindow();
                closeStage.close();
            }
            Node closeSourceNode = (Node) actionEvent.getSource();
            Stage closeStage = (Stage) closeSourceNode.getScene().getWindow();
            closeStage.close();
    }

    public void btnCancelConfirm(ActionEvent actionEvent) {
        Alert cancelDelete = new Alert(Alert.AlertType.CONFIRMATION);
        String cancelMessageText = null;
        if (pathToDelete != null) {
            cancelMessageText = "Cancel deletion file " + pathToDelete.toString() + " !";
            cancelDelete.setContentText(cancelMessageText);
            cancelDelete.show();
            Node closeSourceNode = (Node) actionEvent.getSource();
            Stage closeStage = (Stage) closeSourceNode.getScene().getWindow();
            closeStage.close();
        } else {
            Alert errDeleteFile = new Alert(Alert.AlertType.ERROR);
            errDeleteFile.setContentText("You changed your mind to delete anything !");
            errDeleteFile.show();

            Node closeSourceNode = (Node) actionEvent.getSource();
            Stage closeStage = (Stage) closeSourceNode.getScene().getWindow();
            closeStage.close();
        }
    }
}
