package ghops.gprint.orders.orderitem;

import ghops.gprint.database.OrderDB;
import ghops.gprint.database.ProductDB;
import ghops.gprint.models.Order;
import ghops.gprint.models.Product;
import ghops.gprint.orders.orderform.OrderForm;
import ghops.gprint.orders.orderitem.productItem.ProductItem;
import ghops.gprint.orders.productform.ProductForm;
import ghops.gprint.tools.delete.Delete;
import java.io.IOException;
import java.text.SimpleDateFormat;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class OrderItem extends StackPane {

    @FXML
    private Label orderNo;

    @FXML
    private Label docket;

    @FXML
    private Label customer;

    @FXML
    private Button editButton;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Label description;

    @FXML
    private Label date;

    @FXML
    private Label delivery;

    @FXML
    private CheckBox status;

    @FXML
    private VBox productList;

    ObservableList<ProductItem> items = FXCollections.observableArrayList();

    private final ObjectProperty<Order> orderProperty;

    private Order order;

    public OrderItem(Order o) {

        this.order = o;
        this.orderProperty = new SimpleObjectProperty<>(this.order);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();
        } catch (IOException exception) {
            System.err.println(exception);
        }
    }

    private void init() {

        editButton.setOnAction((action) -> {
            new OrderForm(this.order).open();
        });

        /*
        for (Product p : order.getProducts()) {
            productList.getChildren().add(new ProductItem(p));
        }*/
        orderNo.textProperty().bind(order.orderNo());
        customer.textProperty().bind(order.customer().asString());
        docket.textProperty().bind(order.docket());
        date.textProperty().bind(order.date().asString());
        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
        delivery.textProperty().bind(order.delivery().asString());
        description.textProperty().bind(order.description());
        status.selectedProperty().bindBidirectional(order.status());
        status.selectedProperty().addListener((o, oldValue, newValue) -> {
            this.order.setStatus(newValue);
            new OrderDB().update(this.order);
        });

        for (Product p : this.order.getProducts()) {
            ProductItem pi = new ProductItem(p);
            pi.getDeleteButton().setOnAction((action) -> {
               boolean del = new Delete("Üretim siliniyor...", p.getDesign() + " desen numaralı üretim siliniyor...").open();
               if(del){
                   boolean res = new ProductDB().delete(p.getId());
                   if(res){
                       this.items.remove(pi);
                       this.productList.getChildren().setAll(items);
                   }
               }
                
            });
            items.add(pi);
        }
        productList.getChildren().setAll(items);

        this.addButton.setOnAction((action) -> {
            Product p = new ProductForm(new Product(this.order.getId())).open();
            if(p!=null) this.items.add(new ProductItem(p)); 
            this.productList.getChildren().setAll(items);
        });

    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

}


/*
    customer.textProperty().bind(Bindings.createObjectBinding(() -> orderProperty.getValue().getCustomer().getName(), orderProperty));
    customer.textProperty().bind(Bindings.createObjectBinding(() -> customerProperty.getValue().getName(), customerProperty));
    productList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends ProductItem> observable, ProductItem oldValue, ProductItem newValue) -> {
    });

 */
