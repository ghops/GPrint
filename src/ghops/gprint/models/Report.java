package ghops.gprint.models;

import java.util.ArrayList;
import java.util.List;

public class Report {
    private String header = "";
    private int width = 100;
    List<String> data = new ArrayList<>();

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }
    
    public void addData(String text){
        this.data.add(text);
    }
    
    
}
