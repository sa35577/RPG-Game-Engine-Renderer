/*
FileData.java
Sat Arora
File Data class used for reading all files from directory.
 */

//importing packages
import java.io.File;

public class FileData {
    private File file; //the file
    private String fname; //file name
    //constructor
    public FileData(File file, String fname) {
        this.file = file;
        this.fname = fname;
    }
    //getters and setters
    public File getFile() { return file; }
    public void setFile(File file) { this.file = file; }
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }
}
