package filemanager;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.nio.file.*;
import java.util.ResourceBundle;

public class FXMLSaveAsController implements Initializable {

    @FXML
    public Button svDlgBtnSave;
    @FXML
    public Button svDlgBtnCancel;
    @FXML
    public TextField svDlgTxtFld;

    private Path initPath;
    private Path targetPath;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        svDlgTxtFld.setText(FXMLMainFormController.selectedPath.toString());
        initPath = FXMLMainFormController.selectedPath;
    }

    public void btnSaveFile(ActionEvent actionEvent) {
        String ss = svDlgTxtFld.getText();
        if (ss != null) {
            targetPath = Paths.get(ss);
            synchronized (this) {
                if (targetPath != null) {
                    try {
                        Files.copy(initPath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                        Alert alertSuccess = new Alert(Alert.AlertType.INFORMATION);
                        alertSuccess.setContentText("Succesfully saved " + initPath.toString() + " to " + targetPath.toString() + " !");
                        alertSuccess.show();
                    } catch (IOException e) {

                        Alert alertSuccess = new Alert(Alert.AlertType.ERROR);
                        alertSuccess.setContentText("Saved " + targetPath.toString() + " FAILED because of : ");
                        alertSuccess.show();

                        e.printStackTrace();
                    } finally {
                        Node closeSourceNode = (Node) actionEvent.getSource();
                        Stage closeStage = (Stage) closeSourceNode.getScene().getWindow();
                        closeStage.close();
                    }
                } else {
                    Node closeSourceNade = (Node) actionEvent.getSource();
                    Stage closingStage = (Stage) closeSourceNade.getScene().getWindow();
                    closingStage.close();
                }
            }
        }
    }

    public void btnCancelSave(ActionEvent cancelActionEvent) {
        String ss = svDlgTxtFld.getText();
        if (ss != null) {
            targetPath = Paths.get(ss);

            Alert alertCancel = new Alert(Alert.AlertType.INFORMATION);
            alertCancel.setContentText("Canceled save ==" + targetPath.toString() + "== file !");
            alertCancel.show();

            Node closeSourceNade = (Node) cancelActionEvent.getSource();
            Stage closingStage = (Stage) closeSourceNade.getScene().getWindow();
            closingStage.close();
        } else {
            Alert alertCancel = new Alert(Alert.AlertType.INFORMATION);
            alertCancel.setContentText("Canceled save file !");
            alertCancel.show();

            Node closeSourceNade = (Node) cancelActionEvent.getSource();
            Stage closingStage = (Stage) closeSourceNade.getScene().getWindow();
            closingStage.close();
        }
    }
}