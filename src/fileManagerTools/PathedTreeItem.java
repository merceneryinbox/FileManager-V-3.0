/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileManagerTools;

import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.FileTime;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author mercenery
 */
public final class PathedTreeItem extends TreeItem<Path> {

    private boolean visited = false;

    private boolean leafPropertyComputed = false;
    private Path path;
    private String fullFileName;
    private String extension;
    private PathedItemsIconSetter viewSetter;

    private String nameOfFile;
    private long size;
    private String lastModFormatedString;
    private final DateFormat df = new SimpleDateFormat("dd MMMM yyyy - в hh:mm");
    private FileTime ft;

    {
        viewSetter = new PathedItemsIconSetter();
    }

    // string constructor
    public PathedTreeItem(String strPath) throws IOException {
        super(Paths.get(strPath));
        path = Paths.get(strPath);
        fullFileName = strPath;
        nameOfFile = getNameOfFile();
        extension = getExtension();
        size = getSize();
//        lastModFormatedString = df.format(Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS));
    }
    // Path constructor

    public PathedTreeItem(Path path) throws IOException {
        super(path);
        this.path = path;
        fullFileName = path.toString();
        nameOfFile = getNameOfFile();
        extension = getExtension();
        size = getSize();
//        lastModFormatedString = df.format(Files.getLastModifiedTime(path, LinkOption.NOFOLLOW_LINKS));

    }

    @Override
    public boolean isLeaf() {
        return !Files.isDirectory(this.getValue()) && this.getValue() != null;
    }
    //------------

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public boolean isVisited() {
        return visited;
    }
    //------------

    public void getLastModFormatedString(FileTime lastMod) {
        this.lastModFormatedString = df.format(lastMod);
    }

    public String getLastModFormatedString() {
        return lastModFormatedString;
    }
    //-------------

    public void setNameOfFile(String nameOfFile) {
        this.nameOfFile = nameOfFile;
    }

    public String getNameOfFile() {
        if (!Files.isDirectory(this.getValue()) && Files.isReadable(this.getValue())) {
            if (fullFileName.contains(".")) {
                nameOfFile = fullFileName.substring(0, fullFileName.lastIndexOf("."));
            }
        } else {
            nameOfFile = fullFileName;
        }
        return nameOfFile;
    }
    //-----------

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        extension = "--FOLDER--";
        if (!Files.isDirectory(this.getValue()) && fullFileName.contains(".")) {
            extension = fullFileName.substring(fullFileName.lastIndexOf(".") + 1, fullFileName.length());
        }
        return extension;
    }

    //--------------
    public void setSize(long size) {
        this.size = size;
    }

    public Long getSize() {
        try {
            size = Files.size(this.getValue()) / 1024;
        } catch (FileSystemException fse) {
            System.out.println("The disk " + this.getValue() + " is not ready !");
            Alert fseAlert = new Alert(Alert.AlertType.ERROR);
            fseAlert.setContentText("The disk " + this.getValue() + " is not ready !");
            fseAlert.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {

        }
        return size;
    }

    //------------

    public synchronized void addDirsToRootDirs() {
        Path tmpP = Paths.get(this.fullFileName);
        List<PathedTreeItem> tmpPaList = new ArrayList<>();
        try (DirectoryStream<Path> tmpDS = Files.newDirectoryStream(tmpP)) {
            for (Path p :
                    tmpDS) {
                if (Files.isDirectory(p)) {
                    PathedTreeItem item = new PathedTreeItem(p);
                    item.setGraphic(viewSetter.getView(item));
                    tmpPaList.add(item);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.getChildren().setAll(tmpPaList);
    }
}
