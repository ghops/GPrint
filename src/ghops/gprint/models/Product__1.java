package ghops.gprint.models;

import ghops.gprint.tools.StatusType;
import java.text.DecimalFormat;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product__1 {

    private int id, orderId = 0, meters = 0, weight, width = 0;
    private Fabric fabric;
    private StatusType status;
    private int printedMeters = 0;
    private PrintType printType;
    private String design = "", descrption = "";
    private SimpleDoubleProperty percent = new SimpleDoubleProperty(0);

    private StringProperty dsgn = new SimpleStringProperty("");

    private static DecimalFormat formatter = new DecimalFormat("#.##");

    public SimpleDoubleProperty getPercent() {
        return percent;
    }

    public PrintType getPrintType() {
        return printType;
    }

    public void setPrintType(PrintType printType) {
        this.printType = printType;
    }

    public int getPrintedMeters() {
        return printedMeters;
    }

    public void setPrintedMeters(int printedMeters) {
        this.printedMeters = printedMeters;
        this.calculatePercent();
    }

    private void calculatePercent() {
        if (this.getMeters() > 0) {
            double pr = (double) this.getPrintedMeters() / (double) this.getMeters();
            String s = formatter.format(pr).replace(",", ".");
            this.percent.set(Double.parseDouble(s));
        }

    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public void setStatus(int status) {
        this.status.setValue(status);
    }

    public Fabric getFabric() {
        return fabric;
    }

    public void setFabric(Fabric fabric) {
        this.fabric = fabric;
    }

    public int getOrderId() {
        return orderId;
    }

    public Product__1(int orderId) {
        this.orderId = orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMeters() {
        return meters;
    }

    public void setMeters(int meters) {
        this.meters = meters;
        this.calculatePercent();
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getDesign() {
        return design;
    }

    public StringProperty getDsgn() {
        return dsgn;
    }

    public void setDesign(String design) {
        this.dsgn.setValue(design);
        this.design = design;
    }

    public String getDescrption() {
        return descrption;
    }

    public void setDescrption(String descrption) {
        this.descrption = descrption;
    }

    public void init() {
        this.setId(0);
        this.setDescrption("");
        this.setDesign("");
        this.setFabric(new Fabric(0, ""));
        this.setMeters(0);
        this.setWeight(0);
        this.setWidth(0);
        this.setPrintedMeters(0);
        this.setPrintType(new PrintType(1, ""));
        this.setStatus(new StatusType(0));

        if (this.getFabric() == null) {
            this.setFabric(new Fabric(0, ""));
        }
        if (this.getPrintType() == null) {
            this.setPrintType(new PrintType(1, ""));
        }
        if (this.status == null) {
            this.setStatus(new StatusType(0));
        }
    }

}
