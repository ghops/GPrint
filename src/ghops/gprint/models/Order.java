package ghops.gprint.models;

import ghops.gprint.tools.StatusType;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Order {

    private final IntegerProperty id = new SimpleIntegerProperty(0);
    private final StringProperty orderNo = new SimpleStringProperty("");
    private final ObjectProperty<Customer> customer = new SimpleObjectProperty<>(new Customer(0, ""));
    private final StringProperty description = new SimpleStringProperty("");
    private final StringProperty docket = new SimpleStringProperty("");
    private final ObjectProperty<LocalDate> date = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> delivery = new SimpleObjectProperty<>();
    private final ObservableList<Product> products = FXCollections.observableArrayList();
    private final BooleanProperty status = new SimpleBooleanProperty(false);

    /*
    private int id;
    private String orderNo, docket, description;
    private Customer customer;
    private LocalDate date, delivery;
    private StatusType status;
    private List<Product> products;

     */
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");

    public DateTimeFormatter dateFormat() {
        return this.formatter;
    }

    public ObjectProperty<Customer> customer() {
        return customer;
    }

    public Customer getCustomer() {
        return customer.getValue();
    }

    public void setCustomer(Customer customer) {
        this.customer.setValue(customer);
    }

    public ObjectProperty<LocalDate> date() {
        return date;
    }

    public LocalDate getDate() {
        return date.getValue();
    }

    public void setDate(LocalDate date) {
        this.date.setValue(date);
    }

    public ObjectProperty<LocalDate> delivery() {
        return delivery;
    }

    public LocalDate getDelivery() {
        return delivery.getValue();
    }

    public void setDelivery(LocalDate delivery) {
        this.delivery.setValue(delivery);
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

    public StringProperty docket() {
        return docket;
    }

    public String getDocket() {
        return docket.getValue();
    }

    public void setDocket(String docket) {
        this.docket.setValue(docket);
    }

    public IntegerProperty id() {
        return id;
    }

    public Integer getId() {
        return id.getValue();
    }

    public void setId(int id) {
        this.id.setValue(id);
    }

    public StringProperty orderNo() {
        return orderNo;
    }

    public String getOrderNo() {
        return orderNo.getValue();
    }

    public void setOrderNo(String orderNo) {
        this.orderNo.setValue(orderNo);
    }

    public ObservableList<Product> getProducts() {
        return products;
    }

    public void setProducts(ObservableList<Product> p) {
        this.products.clear();
        this.products.setAll(p);
    }

    public BooleanProperty status() {
        return status;
    }

    public boolean getStatus() {
        return this.status.getValue();
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
        return this.getOrderNo();
    }

}
