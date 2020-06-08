import java.io.File;

public class FileData {
    private File file;
    private String fname;

    public FileData(File file, String fname) {
        this.file = file;
        this.fname = fname;
    }

    public File getFile() { return file; }
    public void setFile(File file) { this.file = file; }
    public String getFname() { return fname; }
    public void setFname(String fname) { this.fname = fname; }
}
