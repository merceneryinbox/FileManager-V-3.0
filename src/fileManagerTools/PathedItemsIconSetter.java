package fileManagerTools;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Files;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;

import static javafx.scene.input.KeyCode.Z;

public class PathedItemsIconSetter {

    // Images

    private Image folder_opened;
    private Image folder_closed;
    private Image add;
    private Image archive;
    private Image delete;
    private Image exe;
    private Image hd_32x32;
    private Image lifebelt;
    private Image multimedia;
    private Image office;
    private Image other;
    private Image picture;
    private Image properties;
    private Image refresh;
    private Image rename;
    private Image save;
    private Image web;


    public PathedItemsIconSetter() {
        try {
            folder_opened = new Image(getClass().getResourceAsStream("icons/folder_opened.png"));
            folder_closed = new Image(getClass().getResourceAsStream("icons/folder_closed.png"));
            add = new Image(getClass().getResourceAsStream("icons/add.png"));
            archive = new Image(getClass().getResourceAsStream("icons/archive.png"));
            delete = new Image(getClass().getResourceAsStream("icons/delete.png"));
            exe = new Image(getClass().getResourceAsStream("icons/exe.png"));
            hd_32x32 = new Image(getClass().getResourceAsStream("icons/hd_32x32.png"));
            lifebelt = new Image(getClass().getResourceAsStream("icons/lifebelt.png"));
            multimedia = new Image(getClass().getResourceAsStream("icons/multimedia.png"));
            office = new Image(getClass().getResourceAsStream("icons/office.png"));
            other = new Image(getClass().getResourceAsStream("icons/other.png"));
            picture = new Image(getClass().getResourceAsStream("icons/picture.png"));
            properties = new Image(getClass().getResourceAsStream("icons/properties.png"));
            refresh = new Image(getClass().getResourceAsStream("icons/refresh.gif"));
            rename = new Image(getClass().getResourceAsStream("icons/rename.png"));
            save = new Image(getClass().getResourceAsStream("icons/save.png"));
            web = new Image(getClass().getResourceAsStream("icons/web.png"));

        } catch (NullPointerException npe) {
            Alert alertNPE = new Alert(Alert.AlertType.ERROR);
            alertNPE.setContentText("Icons were not found !");
            alertNPE.show();
        }

    }

    public synchronized ImageView getView(PathedTreeItem inputItem) {
        ImageView resultView = new ImageView();
        Lock imgGetLock = new ReentrantLock();
        try {
            imgGetLock.lock();

            if (inputItem != null && inputItem.getParent() != null) {
                String test = getType(inputItem);
                // Getting the type of selected PathedTreeItem
                if (test != null) {
                    try {
                        switch (test) {
                            case "folder":
                                resultView = inputItem.isExpanded() ? new ImageView(folder_closed) : new ImageView(folder_closed);
                                break;

                            case "archive":
                                resultView = new ImageView(archive);
                                break;

                            case "exe":
                                resultView = new ImageView(exe);
                                break;

                            case "office":
                                resultView = new ImageView(office);
                                break;

                            case "web":
                                resultView = new ImageView(web);
                                break;

                            case "picture":
                                resultView = new ImageView(picture);
                                break;

                            case "multimedia":
                                resultView = new ImageView(multimedia);
                                break;

                            case "others":
                                resultView = new ImageView(other);
                                break;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (inputItem.getValue().getParent() == null) {
                resultView = new ImageView(hd_32x32);
            } else if (inputItem == null) {
                System.out.println("ERROR ICON SETTER SELECTED \nCAUSE OF PATH IS NOT EXISTING ANY MORE");
            }
            return resultView;
        } finally {
            imgGetLock.unlock();
        }
    }

    public synchronized String getType(PathedTreeItem p) {
        String fullFileName = p.getValue().toString();
        String resultType = null;
        Lock getTypeLock = new ReentrantLock();
        try {
            getTypeLock.lock();
            if (fullFileName != null) {
                if (!Files.isDirectory(p.getValue())) {
                    if (fullFileName.endsWith(".zip") || fullFileName.endsWith(".arj") || fullFileName.endsWith(".rar")) {
                        resultType = "archive";
                    } else if (fullFileName.endsWith(".exe") || fullFileName.endsWith(".msi") || fullFileName.endsWith(".bat")) {
                        resultType = "exe";
                    } else if (fullFileName.endsWith(".djvu") || fullFileName.endsWith(".doc") || fullFileName.endsWith(".xls") || fullFileName.endsWith(".txt") || fullFileName.endsWith(".ppt") || fullFileName.endsWith(".pdf") || fullFileName.endsWith(".ini")) {
                        resultType = "office";
                    } else if (fullFileName.endsWith(".html") || fullFileName.endsWith(".xml") || fullFileName.endsWith(".fxml")) {
                        resultType = "web";
                    } else if (fullFileName.endsWith(".jpg") || fullFileName.endsWith(".bmp") || fullFileName.endsWith(".tiff") || fullFileName.endsWith(".gif") || fullFileName.endsWith(".png")) {
                        resultType = "picture";
                    } else if (fullFileName.endsWith(".mp3") || fullFileName.endsWith(".avi") || fullFileName.endsWith(".mp4") || fullFileName.endsWith(".wmv")) {
                        resultType = "multimedia";
                    }
                } else if (Files.isDirectory(p.getValue())) {
                    resultType = "folder";
                } else {
                    resultType = "others";
                }
            }
            return resultType;
        } finally {
            getTypeLock.unlock();
        }
    }
}
