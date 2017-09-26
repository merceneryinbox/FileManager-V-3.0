package fileManagerTools;

public final class TypeMatch {
    public static boolean isOffice(String elementName) {
        return (elementName.endsWith(".txt") || elementName.endsWith(".doc") || elementName.endsWith(".ini")
                || elementName.endsWith(".docx") || elementName.endsWith(".sql") || elementName.endsWith(".xml")
                || elementName.endsWith(".htm") || elementName.endsWith(".html")
                || elementName.endsWith(".iml") || elementName.endsWith(".bac")
                || elementName.endsWith(".log")
                || elementName.endsWith(".chm"));
    }

    public static boolean isMultiMedia(String elementName) {
        return (elementName.endsWith(".mp3") || elementName.endsWith(".avi") || elementName.endsWith(".mov")
                || elementName.endsWith(".mpeg") || elementName.endsWith(".wmv")
                || elementName.endsWith(".wav") || elementName.endsWith(".flv")
                || elementName.endsWith(".mp4"));

    }
}
