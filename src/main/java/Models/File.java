package Models;

public class File {

    private String fileName;
    public String getFileName() {
        return fileName;
    }

    private String content;
    public String getContent(){
        return content;
    }

    public File(String fileName, String content) {
        this.fileName = fileName;
        this.content = content;
    }

}
