package ghops.gprint.orders.orderform;

import ghops.gprint.database.CustomerDB;
import ghops.gprint.database.OrderDB;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Order;
import ghops.gprint.tools.WindowMover;
import ghops.gprint.tools.searchbox.SearchBox;
import java.io.IOException;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class OrderForm extends AnchorPane {

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

    private ObjectProperty<Customer> customerProperty = new SimpleObjectProperty<>(null);

    private Order result = null;

    public OrderForm(Order o) {
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

    public Order open() {
        this.getScene().setOnKeyPressed((key) -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                this.window.close();
            }
        });
        WindowMover.open(window);
        this.window.showAndWait();
        return result;
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
            Customer c = new SearchBox<>(new CustomerDB().getAll()).open("Müşteri seçimi ...", window);
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

            saveOrder = new OrderDB().save(saveOrder);
            System.out.println(saveOrder);

            if (saveOrder != null) {
                this.result = saveOrder;
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
            this.result = null;
            this.window.close();

        });

        BooleanBinding orderNoValid = Bindings.createBooleanBinding(() -> {
            if (orderNo.getText().trim().length() < 3) {
                return false;
            } else {
                return true;
            }
        }, this.orderNo.textProperty());

        BooleanBinding dateValid = Bindings.createBooleanBinding(() -> {
            if (date.getValue() != null) {
                return true;
            } else {
                return false;
            }
        }, this.date.valueProperty());

        BooleanBinding deliveryValid = Bindings.createBooleanBinding(() -> {
            if (delivery.getValue() != null) {
                return true;
            } else {
                return false;
            }
        }, this.delivery.valueProperty());

        BooleanBinding customerValid = Bindings.createBooleanBinding(() -> {
            if (customerProperty.getValue().getId() > 0) {
                return true;
            } else {
                return false;
            }
        }, customerProperty);

        saveButton.disableProperty().bind(orderNoValid.not().or(dateValid.not()).or(deliveryValid.not()).or(customerValid.not()));

    }
}
