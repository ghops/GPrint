package ghops.gprint.customer.customerview;

import ghops.gprint.customer.customerForm.CustomerForm;
import ghops.gprint.database.CustomerDB;
import ghops.gprint.models.Customer;
import ghops.gprint.tools.delete.Delete;
import java.io.IOException;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;

public class CustomerView extends StackPane {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Customer, String> colAddress;

    @FXML
    private TableColumn<Customer, String> colCity;

    @FXML
    private TableColumn<Customer, String> colEmail;

    @FXML
    private TableColumn<Customer, String> colFax;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<Customer, Boolean> colStatus;

    @FXML
    private TableColumn<Customer, String> colTelephone;

    @FXML
    private TableColumn<Customer, String> colWeb;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Customer> table;

    public CustomerView() {
 
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("CustomerView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init() {
        this.initTable();

        this.addButton.setOnAction((action) -> {
            this.table.getSelectionModel().clearSelection();
            Customer c = new CustomerForm(new Customer()).getCustomer();
            if (c != null) {
                this.table.getItems().add(0, c);
            }

        });

        this.deleteButton.setOnAction((action) -> {
            boolean res = new Delete("Firma sil", this.table.getSelectionModel().getSelectedItem().getName() + " adlı kaydı silmek üzeresiniz!").open();
            if (res) {
                boolean del = new CustomerDB().delete(this.table.getSelectionModel().getSelectedItem().getId());
                if (del) {
                    this.table.getItems().remove(this.table.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    private void initTable() {
        this.colAddress.setCellValueFactory(new PropertyValueFactory<>("address"));
        this.colCity.setCellValueFactory(new PropertyValueFactory<>("city"));
        this.colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        this.colFax.setCellValueFactory(new PropertyValueFactory<>("fax"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colTelephone.setCellValueFactory(new PropertyValueFactory<>("telephone"));
        this.colWeb.setCellValueFactory(new PropertyValueFactory<>("web"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        this.table.setItems(FXCollections.observableList(new CustomerDB().getAll()));

        this.colStatus.setCellFactory((tableColumn) -> {
            TableCell<Customer, Boolean> tableCell = new TableCell<>() {

                @Override
                protected void updateItem(Boolean t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        CheckBox cb = new CheckBox();
                        cb.selectedProperty().setValue(t);
                        setGraphic(cb);

                        Customer customer = (Customer) getTableRow().getItem();
                        cb.selectedProperty().addListener((o, oldValue, newValue) -> {
                            System.out.println(customer);
                            customer.setStatus(newValue);
                            new CustomerDB().update(customer);
                        });
                    } else {
                        setGraphic(null);
                    }
                }
            };

            return tableCell;
        });

        this.table.setRowFactory(tv -> {
            TableRow<Customer> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //Customer c = row.getItem();
                    Customer c = new CustomerForm(row.getItem()).getCustomer();
                    row.setItem(c);
                    this.table.refresh();

                }
            });
            return row;
        });

        this.table.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            System.out.println(newSelection);
            if (newSelection == null) {
                this.deleteButton.setDisable(true);
            } else {
                this.deleteButton.setDisable(false);
            }

        });

        this.deleteButton.setDisable(true);

    }

}
