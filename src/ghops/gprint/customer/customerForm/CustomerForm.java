package ghops.gprint.customer.customerForm;

import ghops.gprint.database.CustomerDB;
import ghops.gprint.models.Customer;
import ghops.gprint.tools.WindowMover;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

public class CustomerForm extends AnchorPane {

    @FXML
    private TextField address;

    @FXML
    private TextField city;

    @FXML
    private Button closeButton;

    @FXML
    private TextField email;

    @FXML
    private TextField fax;

    @FXML
    private TextField name;

    @FXML
    private Button saveButton;

    @FXML
    private CheckBox status;

    @FXML
    private TextField telephone;

    @FXML
    private TextField web;

    private Stage window;
    private Customer customer;
    private Customer result = null;

    private BooleanProperty statusProperty = new SimpleBooleanProperty(false);

    public CustomerForm(Customer c) {
        this.customer = c;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            this.window = new Stage();
            this.window.initModality(Modality.APPLICATION_MODAL);
            this.window.setTitle("Firma Formu");
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

    public Customer open() {
        WindowMover.open(window);
        this.window.showAndWait();
        return this.result;
    }

    public Customer getCustomer() {
        WindowMover.open(window);
        this.window.showAndWait();
        return this.customer;
    }

    private void init() {
        this.status.selectedProperty().bindBidirectional(statusProperty);
        closeButton.setOnAction((action) -> {
            this.customer = null;
            this.window.close();
        });

        saveButton.setOnAction((action) -> {
            this.setCustomer();

            Customer saved = new CustomerDB().save(this.customer);

            if (saved != null) {
                this.customer = saved;
                this.result = saved;

            } else {
                System.out.println("kayıt başarısız");
            }
        });

        this.setForm();

    }

    private void setForm() {
        this.address.setText(this.customer.getAddress());
        this.city.setText(this.customer.getCity());
        this.email.setText(this.customer.getEmail());
        this.name.setText(this.customer.getName());
        this.telephone.setText(this.customer.getTelephone());
        this.web.setText(this.customer.getWeb());
        this.fax.setText(this.customer.getFax());
        this.statusProperty.set(this.customer.getStatus());
    }

    private void setCustomer() {
        this.customer.setAddress(address.getText());
        this.customer.setCity(this.city.getText());
        this.customer.setEmail(this.email.getText());
        this.customer.setFax(this.fax.getText());
        this.customer.setName(name.getText());
        this.customer.setStatus(this.statusProperty.getValue());
        this.customer.setTelephone(telephone.getText());
        this.customer.setWeb(web.getText());
    }

}
