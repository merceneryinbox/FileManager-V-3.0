/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * @author mercenery
 */
public final class FXMLnewFileDialog implements Initializable {

    @FXML
    public Button newFileDialogOkBtn;
    @FXML
    public Button newFileDialogCancelBtn;
    @FXML
    public TextField newFileDialogTxtField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newFileDialogTxtField.setText(FXMLMainFormController.selectedPTI.getValue().toString());

        newFileDialogOkBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                String inputName = newFileDialogTxtField.getText();
                Path testPath = Paths.get(inputName);
                try {
                    Files.createFile(testPath);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Success Create file " + inputName);
                    alert.show();
                } catch (FileAlreadyExistsException faee) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Such file allready exist in this directory");
                    alert.show();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setContentText("Error Create" + inputName);
                    alert.show();
                    e.printStackTrace();
                } finally {
                    Node closeSourceNode = (Node) event.getSource();
                    Stage closeStage = (Stage) closeSourceNode.getScene().getWindow();
                    closeStage.close();
                }
            }
        });

        newFileDialogCancelBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Node closeSourceNode = (Node) event.getSource();
                Stage closeStage = (Stage) closeSourceNode.getScene().getWindow();
                closeStage.close();

                Alert cancelNewAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelNewAlert.setContentText("Cancel Create file");
                cancelNewAlert.show();
            }
        });
    }
}
