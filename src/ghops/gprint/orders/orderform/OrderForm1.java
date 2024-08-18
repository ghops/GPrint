package ghops.gprint.orders.orderform;

import ghops.gprint.database.CustomerDB;
import ghops.gprint.database.OrderDB;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Order;
import ghops.gprint.tools.WindowMover;
import ghops.gprint.tools.searchbox.SearchBox;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OrderForm1 extends AnchorPane {

    @FXML
    private Button closeButton;
    @FXML
    private Button saveButton;

    @FXML
    private Button customerButton;

    @FXML
    private TextField orderNo;

    @FXML
    private TextField docket;

    @FXML
    private DatePicker date;

    @FXML
    private DatePicker delivery;

    @FXML
    private TextField description;

    @FXML
    private CheckBox status;
    
    @FXML
    private Label header;

    private Stage window;

    private Order order;

    private ObjectProperty<Customer> customerProperty = new SimpleObjectProperty<>(new Customer(0, ""));

 

    public OrderForm1(Order o) {
        this.order = o;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("OrderForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
 
            this.window = new Stage();
            this.window.initModality(Modality.APPLICATION_MODAL);
            this.window.setTitle("Sipariş Formu");
            this.window.initStyle(StageStyle.UNDECORATED); 
            try {
                Scene scene = new Scene(fxmlLoader.load());
                this.window.setScene(scene);
                init();

            } catch (IOException ex) {
                System.out.println(ex);
            }

        } catch (IOException exception) {
            System.err.println(exception);
        }

    }

    public void open() {
        WindowMover.open(window);
        this.window.showAndWait();
    }

    private void init() {
        customerProperty.setValue(order.getCustomer());
        orderNo.setText(order.getOrderNo());
        docket.setText(order.getDocket());
        description.setText(order.getDescription());
        date.setValue(order.getDate());
        delivery.setValue(order.getDelivery());
        status.selectedProperty().set(order.getStatus());
        //customerButton.textProperty().bind(order.getCustomer().asString());

        customerButton.textProperty().bind(Bindings.createObjectBinding(() -> customerProperty.getValue().getName(), customerProperty));

        customerButton.setOnAction((action) -> {
            Customer c = new SearchBox<>(new CustomerDB().getAll()).open("edsd", window);
            if (c != null) {
                customerProperty.set(c);
                // order.getCustomer().setValue(c);
            }

        });

        saveButton.setOnAction((action) -> {
            Order saveOrder = new Order();
            saveOrder.setId(order.getId());
            saveOrder.setOrderNo(orderNo.getText());
            saveOrder.setDocket(docket.getText());
            saveOrder.setCustomer(customerProperty.getValue());
            saveOrder.setDate(date.getValue());
            saveOrder.setDelivery(delivery.getValue());
            saveOrder.setDescription(description.getText());
            saveOrder.setStatus(status.selectedProperty().get());

            saveOrder = new OrderDB().update(saveOrder);
            if (saveOrder != null) {
                order.setOrderNo(saveOrder.getOrderNo());
                order.setDocket(saveOrder.getDocket());
                order.setCustomer(customerProperty.getValue());
                order.setDate(saveOrder.getDate());
                order.setDelivery(saveOrder.getDelivery());
                order.setDescription(saveOrder.getDescription());
                order.setStatus(saveOrder.getStatus());
                this.window.close();
            }

        });
        closeButton.setOnAction((action) -> {
            this.window.close();

        });

    }
}
