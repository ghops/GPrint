package ghops.gprint.models;

public class ReportColumn {

    private String text = "";
    private float width = 100;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public ReportColumn(String text, float width) {
        this.setText(text);
        this.setWidth(width);
    }

    
    
    @Override
    public String toString() {
        return this.getText();
    }

}
