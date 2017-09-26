/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fileManagerTools;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

/**
 * @author mercenery
 */
public class FileTypesFilter {

    private Path path;

    private String fullFileName;
    private String resultType;

    private boolean isFolder = false;
    private boolean isExecutable = false;
    private boolean isOffice = false;
    private boolean isArchive = false;
    private boolean isWeb = false;
    private boolean isPicture = false;
    private boolean isMultimedia = false;
    private boolean isOthers = false;

    /**
     * FileTypesFilter constructor take String name of Path to witch apply
     * filter of types
     *
     * @param s
     */
    public FileTypesFilter(String s) {

        if (s != null) {
            fullFileName = s.toLowerCase();
            whatIsTrue();
        }

    }

    /**
     * FileTypesFilter constructor Path to witch apply filter of types
     *
     * @param givenPath
     */
    public FileTypesFilter(Path givenPath) {

        if (givenPath != null) {
            path = givenPath;
            fullFileName = path.toString().toLowerCase();
            whatIsTrue();
        }
    }

    /**
     * Single method of FiletypesFilter witch is really handle the filename
     * type, and return result in String answer - its tell what type of file by
     * its filename
     */
    private void whatIsTrue() {
        if (fullFileName.contains(".") && !Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
            isArchive = fullFileName.endsWith(".zip") || fullFileName.endsWith(".arj") || fullFileName.endsWith("rar");
            isExecutable = fullFileName.endsWith(".exe") || fullFileName.endsWith(".msi") || fullFileName.endsWith(".bat");
            isOffice = fullFileName.endsWith(".djvu") || fullFileName.endsWith(".doc") || fullFileName.endsWith(".xls") || fullFileName.endsWith(".txt") || fullFileName.endsWith(".ppt") || fullFileName.endsWith(".pdf");
            isWeb = fullFileName.endsWith(".html") || fullFileName.endsWith(".xml") || fullFileName.endsWith(".fxml");
            isPicture = fullFileName.endsWith(".jpg") || fullFileName.endsWith(".bmp") || fullFileName.endsWith(".tiff") || fullFileName.endsWith(".gif") || fullFileName.endsWith(".png");
            isMultimedia = fullFileName.endsWith(".mp3") || fullFileName.endsWith(".avi") || fullFileName.endsWith(".mp4") || fullFileName.endsWith(".wmv");
            isOthers = (isFolder == false) && (isArchive == false) && (isExecutable == false) && (isOffice == false) && (isWeb == false) && (isPicture == false) && (isMultimedia == false);
        } else {
            isFolder = true;
        }
    }

    public String filterFileByType() {

        if (isFolder) {
            resultType = "folder";
        } else if (isArchive) {
            resultType = "archive";
        } else if (isExecutable) {
            resultType = "exe";
        } else if (isOffice) {
            resultType = "office";
        } else if (isWeb) {
            resultType = "web";
        } else if (isPicture) {
            resultType = "picture";
        } else if (isMultimedia) {
            resultType = "multimedia";
        } else if (isOthers) {
            resultType = "others";
        } else {
            resultType = "others";
        }
        return resultType;
    }
}
