/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filemanager;

import fileManagerTools.PathedItemsIconSetter;
import fileManagerTools.PathedTreeItem;
import fileManagerTools.TypeMatch;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaErrorEvent;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.*;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

import static fileManagerTools.TypeMatch.isMultiMedia;
import static fileManagerTools.TypeMatch.isOffice;
import static java.lang.System.out;


public class FXMLMainFormController implements Initializable {

    // common variables
    private Iterable<Path> rootDirs;
    private List<PathedTreeItem> rootTreeItemsList;
    private volatile ArrayList<PathedTreeItem> subPathedTIListInSelectedPTI;
    private List<MenuItem> mnBtnItemsList;
    private volatile PathedTreeItem selectedRootItem;
    private ExecutorService threadExetutorServise;
    private PathedItemsIconSetter imageSetter;
    public volatile static Path selectedPath;
    public volatile static PathedTreeItem selectedPTI;
    private ImageView refreshPic;

    private Image folder_opened = new Image(getClass().getResourceAsStream("foldericons/folder_opened.png"));
    private Image folder_closed = new Image(getClass().getResourceAsStream("foldericons/folder_closed.png"));

    private Lock lock;

    public static boolean confirm = false;

    // Window controls
    @FXML
    private ProgressIndicator prgrsNdctr;
    @FXML
    private MediaView mdaVw;

    @FXML
    private MenuBar mnBar;
    // File Menu

    @FXML
    private MenuItem subMnFile;
    @FXML
    private MenuItem ssMnNew;
    @FXML
    private MenuItem ssmnOpen;
    @FXML
    private MenuItem ssMnSaveAs;
    @FXML
    private MenuItem ssMnPreferences;
    @FXML
    private MenuItem ssMnQuit;
    //Edit Menu

    @FXML
    private MenuItem subMnEdit;
    @FXML
    private MenuItem ssMnEditCut;
    @FXML
    private MenuItem ssMnEditCopy;
    @FXML
    private MenuItem ssMnEditPaste;
    @FXML
    private MenuItem ssMnEditDel;
    @FXML
    private MenuItem ssMnEditSelAll;
    @FXML
    private MenuItem ssMnEditUnSelAll;
    // Help About Menu

    @FXML
    private MenuItem subMnHelp;
    @FXML
    private MenuItem ssMnHelp;
    // Tree View component

    @FXML
    private volatile TreeView tvLeft;
    // Diskchoice button

    @FXML
    private MenuButton mnBtn;
    // TableView

    @FXML
    private TableView tblVCenter;
    @FXML
    private volatile TableColumn<PathedTreeItem, String> tblViewClmnFileName;
    @FXML
    private volatile TableColumn<PathedTreeItem, String> tblViewClmnFileExt;
    @FXML
    private volatile TableColumn<PathedTreeItem, Number> tblViewClmnFileSize;
    @FXML
    private volatile TableColumn<PathedTreeItem, Number> tblViewClmnFileLastMod;
    @FXML
    public MenuItem cntxtMn;

    @FXML
    private TextArea txtAreaLeft;

    // main method in Controller
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ImageView folderAssigner = new ImageView(folder_closed);

//        lock = new ReentrantLock();
        imageSetter = new PathedItemsIconSetter();
        threadExetutorServise = Executors.newFixedThreadPool(5);
//        img = new Image(getClass().getResourceAsStream(hdPicPath));
//        ImageView rootImgView = new ImageView(img);
        rootDirs = new ArrayList<>();
        rootTreeItemsList = new ArrayList<>();
        mnBtnItemsList = new ArrayList<>();
        subPathedTIListInSelectedPTI = new ArrayList<>();

        rootDirs = FileSystems.getDefault().getRootDirectories();

        // not realized methods

        tvLeft.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        tblViewClmnFileName.setCellValueFactory(new PropertyValueFactory<PathedTreeItem, String>("nameOfFile"));
        tblViewClmnFileExt.setCellValueFactory(new PropertyValueFactory<PathedTreeItem, String>("extension"));
        tblViewClmnFileSize.setCellValueFactory(new PropertyValueFactory<PathedTreeItem, Number>("size"));
        tblViewClmnFileLastMod.setCellValueFactory(new PropertyValueFactory<PathedTreeItem, Number>("lastModFormatedString"));


        // File menu event handler


        // Context menu event handler

// create list with PathedTreeItems from list of root directoryes of filesystem

        for (Path rootDir : rootDirs) {
            MenuItem tmpMMenuItem = null;
            try {
                tmpMMenuItem = new MenuItem(rootDir.toString(), imageSetter.getView(new PathedTreeItem(rootDir)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            mnBtnItemsList.add(tmpMMenuItem);

//            tmpMMenuItem.setGraphic(rootImgView);
        }

// add list of MenuItems with names of found drives in menu button drop down items
        mnBtn.getItems().addAll(mnBtnItemsList);
        mnBtn.setGraphic(refreshPic);

// assign Action handle to each menu item in drop button menu then click on it
        mnBtnItemsList.forEach(new Consumer<MenuItem>() {
            @Override
            public void accept(MenuItem mnBtnSelectedItem) {
                mnBtnSelectedItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public synchronized void handle(ActionEvent event) {

                        tvLeft.setRoot(null);
                        try {
                            selectedRootItem = new PathedTreeItem(mnBtnSelectedItem.getText());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        selectedRootItem.addDirsToRootDirs();
                        tvLeft.setRoot(selectedRootItem);
                        selectedRootItem.setExpanded(true);
                        tvLeft.setShowRoot(true);
                    }
                });
            }
        });

        // определяем поведение нашего приложения для случая если пользователь изменил(в основном
        // - выделил(мышью или клавишами)) элемент TreeView


        tvLeft.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public synchronized void changed(ObservableValue observable, Object oldValue, Object newValue) {
                subPathedTIListInSelectedPTI.clear();
                try {
//                    lock.lock();
                    if (newValue != null) {
                        selectedPTI = (PathedTreeItem) newValue; // приведение типа
                        selectedPTI.setGraphic(null);
                        selectedPTI.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                            if (Files.isDirectory(selectedPTI.getValue()) && !selectedPTI.isExpanded()) {
                                if (event.getClickCount() == 2) {
                                    folderAssigner.setImage(folder_opened);
                                    Alert doubleClickOk = new Alert(Alert.AlertType.INFORMATION);
                                    doubleClickOk.setContentText("Folder closed doebleclick catched");
                                    doubleClickOk.show();
                                }
                            } else if (Files.isDirectory(selectedPTI.getValue()) && selectedPTI.isExpanded()) {
                                if (event.getClickCount() == 2) {
                                    folderAssigner.setImage(folder_closed);
                                    Alert doubleClickOk = new Alert(Alert.AlertType.INFORMATION);
                                    doubleClickOk.setContentText("Folder opened doebleclick catched");
                                    doubleClickOk.show();
                                }

                            }
                        });

                        selectedPTI.setGraphic(imageSetter.getView(selectedPTI));
                        Path tmpPath = selectedPTI.getValue(); // получение элемента файловой системы выделенного триитема

                        if (Files.isDirectory(tmpPath)) {

                            Files.list(tmpPath).forEach(pa -> {
                                PathedTreeItem pathedTreeItem = null;
                                try {
                                    pathedTreeItem = new PathedTreeItem(pa);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                subPathedTIListInSelectedPTI.add(pathedTreeItem);
                            });
                            selectedPTI.getChildren().addAll(subPathedTIListInSelectedPTI);
                            tblVCenter.setItems(FXCollections.observableList(subPathedTIListInSelectedPTI));
//                            tblVCenter.refresh();
                        } else if (!Files.isDirectory(tmpPath)) {
                            PathedTreeItem pathedTreeItem = new PathedTreeItem(tmpPath);
//                            pathedTreeItem.setGraphic(imageSetter.getView(pathedTreeItem));
                            subPathedTIListInSelectedPTI.add(pathedTreeItem);
                            tblVCenter.setItems(FXCollections.observableList(subPathedTIListInSelectedPTI));
                            tblVCenter.refresh();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
//                    lock.unlock();
                }
            }
        });

        tblVCenter.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public synchronized void changed(ObservableValue observable, Object oldValue, Object newValue) {
                try {
                    if (newValue != null) {
                        PathedTreeItem selectedPTI = (PathedTreeItem) newValue;
                        selectedPath = selectedPTI.getValue();
                        String elementName = selectedPath.toString();

                        txtAreaLeft.setText(null);

                        if (isOffice(elementName)) {
                            StringBuilder sb = new StringBuilder("Имя файла: \n" + elementName + "\n");
//                            try (BufferedReader fis = new BufferedReader(new FileReader(selectedPath.toFile()))) {
//                                if (fis != null) {
//                                    for (int j = 0; j < 8; j++) {
//                                        sb = sb.append("\n" + fis.readLine());
//                                    }
//                                }
//                                txtAreaLeft.setText(sb.toString());
//                            } catch (FileNotFoundException e) {
//                                e.printStackTrace();
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            List<String> strs = Files.readAllLines(selectedPath);
                            List<String> attr = new ArrayList<>();

                            sb.append("Имя владельца: \n" + Files.getOwner(selectedPath, LinkOption.NOFOLLOW_LINKS).getName() + "\n");
                            sb.append("Дата последнего редактирования: \n" + Files.getLastModifiedTime(selectedPath).toString() + "\n");
                            sb.append("Место хранения файла: \n" + Files.getFileStore(selectedPath).name().toString());

                            txtAreaLeft.setText(sb.toString());

                        } else if (isMultiMedia(elementName)) {
                            StringBuilder sb = new StringBuilder("Выбранный элемент \n" + elementName + "\n");
                            txtAreaLeft.setText(sb.toString());
                            Media media = new Media(elementName);
// Create the player and set to play automatically.
                            MediaPlayer mediaPlayer = new MediaPlayer(media);
                            mediaPlayer.setAutoPlay(true);

                            mdaVw.setMediaPlayer(mediaPlayer);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    } // не удалять !


// Start menu File commands

    /**
     * @throws IOException
     */
    @FXML
    private synchronized void makeNew() throws IOException {
//        ReentrantLock lock = new ReentrantLock();
        try {
//            lock.lock();
            if (selectedPTI != null) {
                Scene addDialogScene;
                try {
                    addDialogScene = new Scene((new FXMLLoader(getClass().getResource("FXMLnewFileDialog.fxml"))).load());
                    Stage addDialogStage = new Stage();
                    addDialogStage.setTitle("Make new file dialog");
                    addDialogStage.initModality(Modality.WINDOW_MODAL);
                    addDialogStage.setScene(addDialogScene);
                    addDialogStage.setResizable(false);
                    addDialogStage.show();

//                    lock.unlock();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                Alert cancelNewAlert = new Alert(Alert.AlertType.INFORMATION);
                cancelNewAlert.setContentText("Choose folder first");
                cancelNewAlert.show();
            }
//                }
//            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void openFolder() throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Открыть файл программой по умолчанию");
        File choosenFile = fileChooser.showOpenDialog(null);

        Desktop opnFlDsktp = Desktop.getDesktop();
        if (choosenFile != null) {
            opnFlDsktp.open(choosenFile);
        }
    }

    @FXML
    public void openSelected() {
        if (selectedPath != null) {
            Desktop openSelectedDeskTop = Desktop.getDesktop();
            try {
                try {
                    openSelectedDeskTop.open(selectedPath.toFile());
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void saveAs() throws IOException {
        if (selectedPath != null) {
            Scene saveScene = new Scene((new FXMLLoader(getClass().getResource("FXMLSaveAsDialog.fxml"))).load());
            Stage saveStage = new Stage();
            saveStage.setResizable(false);
            saveStage.setTitle("Saving file dialog!");
            saveStage.initModality(Modality.WINDOW_MODAL);
            saveStage.setScene(saveScene);
            saveStage.show();
        } else {
            Alert nullFileSave = new Alert(Alert.AlertType.ERROR);
            nullFileSave.setContentText("Empty path save attempt, choose one to save it!");
            nullFileSave.show();
        }

    }

    @FXML
    public void preferences() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();

    }

    @FXML
    public void quitApp() {
        if (!threadExetutorServise.isShutdown()) {
            try {
                threadExetutorServise.shutdown();
            } finally {
                threadExetutorServise.shutdownNow();
            }
            Alert exitingAlert = new Alert(Alert.AlertType.INFORMATION);
            exitingAlert.setContentText("GOOD_BY ! \nWait you next time !");
            exitingAlert.show();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.exit();
        } else {
            Alert exitingAlert = new Alert(Alert.AlertType.INFORMATION);
            exitingAlert.setContentText("GOOD_BY ! \nWait you next time ! 2");
            exitingAlert.show();
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.exit();
        }
    }


    // start Edit menu commands
    @FXML
    public void cut() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();
    }

    @FXML
    public void copy() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();
    }

    @FXML
    public void paste() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();
    }

    @FXML
    public void delete() {
        try {
            Parent confirmRoot = FXMLLoader.load(getClass().getResource("FXMLConfirmForm.fxml"));
            Scene deleteScene = new Scene(confirmRoot, 300, 100);

            Stage deleteStage = new Stage();
            deleteStage.setScene(deleteScene);
            deleteStage.setTitle("Delete confirmation");
            deleteStage.show();

            if (confirm) {
                ObservableList<Integer> list = (tblVCenter.getSelectionModel()).getSelectedIndices();
                if (list != null) {
                    tblVCenter.getItems().removeAll(list);
                    tblVCenter.refresh();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void selectAll() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();
    }

    @FXML
    public void unselectAll() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();
    }

    // Start Help menu commands

    @FXML
    public void helpAbout() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();
    }
    // Start menu button Check Disks Commands

    @FXML
    public void dropDisks() {
        Alert notReadyYetAlert = new Alert(Alert.AlertType.INFORMATION);
        notReadyYetAlert.setContentText("Out off Order !");
        notReadyYetAlert.show();
    }

    // main controller methods
    @FXML
    public void tblViewElementMouseClicked(MouseEvent mouseEvent) {
        if (mouseEvent.getClickCount() == 2) {
            openSelected();
        }
    }

    @FXML
    public void tblViewCntxtMnRequest(ContextMenuEvent contextMenuEvent) {

    }

    private void closeService(ExecutorService exeSer) {
        if (!exeSer.isShutdown()) {
            try {
                exeSer.awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            exeSer.shutdownNow();
        }
    }

    public static synchronized void setConfirm(boolean confirm) {
        FXMLMainFormController.confirm = confirm;
    }

    public void onErrAct(MediaErrorEvent mediaErrorEvent) {

    }
}
