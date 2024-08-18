package ghops.gprint.models;

import ghops.gprint.tools.StatusType;
import java.text.DecimalFormat;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Product {

    private final IntegerProperty id = new SimpleIntegerProperty(0);
    private final IntegerProperty orderId = new SimpleIntegerProperty(0);
    private final DoubleProperty meters = new SimpleDoubleProperty(0);
    private final DoubleProperty printedMeters = new SimpleDoubleProperty(0);
    private final DoubleProperty width = new SimpleDoubleProperty(0);
    private final DoubleProperty weight = new SimpleDoubleProperty(0);
    private final StringProperty design = new SimpleStringProperty("");
    private final StringProperty description = new SimpleStringProperty("");
    private final ObjectProperty<Fabric> fabric = new SimpleObjectProperty<>(new Fabric(0, ""));
    private final BooleanProperty status = new SimpleBooleanProperty(false);
    private final ObjectProperty<PrintType> printType = new SimpleObjectProperty<>(new PrintType(0, ""));

    private static DecimalFormat formatter = new DecimalFormat("#.##");

    public Product() {
    }

    public Product(int orderId) {
        this.setOrderId(orderId);
    }

    public StringProperty description() {
        return description;
    }

    public String getDescription() {
        return description.getValue();
    }

    public void setDescription(String description) {
        this.description.setValue(description);
    }

    public StringProperty design() {
        return design;
    }

    public String getDesign() {
        return design.getValue();
    }

    public void setDesign(String design) {
        this.design.setValue(design);
    }

    public ObjectProperty<Fabric> fabric() {
        return fabric;
    }

    public Fabric getFabric() {
        return fabric.getValue();
    }

    public void setFabric(Fabric fabric) {
        this.fabric.setValue(fabric);
    }

    public IntegerProperty id() {
        return id;
    }

    public int getId() {
        return id.getValue();
    }

    public void setId(int id) {
        this.id.setValue(id);
    }

    public DoubleProperty meters() {
        return meters;
    }

    public double getMeters() {
        return meters.getValue();
    }

    public void setMeters(double meters) {
        this.meters.setValue(meters);
    }

    public IntegerProperty orderId() {
        return orderId;
    }

    public int getOrderId() {
        return orderId.getValue();
    }

    public void setOrderId(int orderId) {
        this.orderId.set(orderId);
    }

    public ObjectProperty<PrintType> printType() {
        return printType;
    }

    public PrintType getPrintType() {
        return printType.getValue();
    }

    public void setPrintType(PrintType printType) {
        this.printType.setValue(printType);
    }

    public DoubleProperty printedMeters() {
        return printedMeters;
    }

    public double getPrintedMeters() {
        return printedMeters.getValue();
    }

    public void setPrintedMeters(double printedMeters) {
        this.printedMeters.set(printedMeters);
    }

    public DoubleProperty weight() {
        return weight;
    }

    public double getWeight() {
        return weight.getValue();
    }

    public void setWeight(double weight) {
        this.weight.setValue(weight);
    }

    public DoubleProperty width() {
        return width;
    }

    public double getWidth() {
        return width.getValue();
    }

    public void setWidth(double width) {
        this.width.setValue(width);
    }

    public BooleanProperty status() {
        return status;
    }

    public boolean getStatus() {
        return status.getValue();
    }

    public void setStatus(boolean status) {
        this.status.setValue(status);
    }

    public void setStatus(int status) {
        if (status == 1) {
            this.status.setValue(true);
        } else {
            this.status.setValue(false);
        }
    }

    public int getStatusValue() {
        return (this.getStatus()) ? 1 : 0;
    }

    @Override
    public String toString() {
        return this.getDesign();
    }

}
