package ghops.gprint.models;

public class Fabric {

    private int id = 0;
    private String name = "", name2 = "", name3 = "";
    private PrintType printType;
    private int width;
    private int weight;
    private String description;
    private boolean status;

    public boolean getStatus() {
        return status;
    }

    public int getStatusValue() {
        return (this.status) ? 1 : 0;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setStatus(int st) {
        if (st == 1) {
            this.status = true;

        } else {
            this.status = false;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public PrintType getPrintType() {
        return printType;
    }

    public void setPrintType(PrintType printType) {
        this.printType = printType;
    }

    public String getName3() {
        return name3;
    }

    public void setName3(String name3) {
        this.name3 = name3;
    }

    public String getName2() {
        return name2;
    }

    public void setName2(String name2) {
        this.name2 = name2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Fabric() {
    }

    public Fabric(int id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public String toString() {
        String text = this.getName();
        if (this.getName2() != null) {
            if (this.getName2().trim().length() > 0) {
                text += "  { " + this.getName2() + " } ";
            }
        }

        if (this.getName3() != null) {
            if (this.getName3().trim().length() > 0) {
                text += "  { " + this.getName3() + " } ";
            }
        }

        return text;

    }
}
