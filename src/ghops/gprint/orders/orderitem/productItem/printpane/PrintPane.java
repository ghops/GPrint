package ghops.gprint.orders.orderitem.productItem.printpane;

import ghops.gprint.database.MachineDB;
import ghops.gprint.database.PrintDB;
import ghops.gprint.models.Machine;
import ghops.gprint.models.Print;
import ghops.gprint.models.Product;
import ghops.gprint.tools.WindowMover;
import ghops.gprint.tools.delete.Delete;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.time.LocalDate;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class PrintPane extends AnchorPane {

    @FXML
    private Button closeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<Print, LocalDate> colDate;

    @FXML
    private TableColumn<Print, Machine> colMachine;

    @FXML
    private TableColumn<Print, Integer> colMeters;

    @FXML
    private DatePicker date;

    @FXML
    private ComboBox<Machine> machine;

    @FXML
    private TextField meters;

    @FXML
    private Button saveButton;

    @FXML
    private Button addButton;

    @FXML
    private TableView<Print> table;

    @FXML
    private Label title;

    private Stage window;

    private Product product;

    private double result = -1;

    public PrintPane(Product p) {
        this.product = p;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("PrintPane.fxml"));
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

    public double open() {
        this.getScene().setOnKeyPressed((key) -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                this.window.close();
            }
        });
        WindowMover.open(window);
        this.window.showAndWait();
        return this.result;
    }

    private void init() {
        this.table.setItems(FXCollections.observableList(new PrintDB().getAll(this.product.getId())));
        this.closeButton.setOnAction((action) -> {
            this.window.close();
        });

        this.colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.colMeters.setCellValueFactory(new PropertyValueFactory<>("meters"));
        this.colMachine.setCellValueFactory(new PropertyValueFactory<>("machine"));

        this.table.getSelectionModel().selectedItemProperty().addListener((item, oldSel, selected) -> {
            if (selected != null) {
                this.setForm(selected);
                this.deleteButton.setDisable(false);
            } else {
                this.setForm(new Print(this.product.getId()));
                this.deleteButton.setDisable(true);
            }
        });

        this.machine.setItems(FXCollections.observableList(new MachineDB().getAll()));

        this.addButton.setOnAction((action) -> {
            this.table.getSelectionModel().clearSelection();
        });

        this.saveButton.setOnAction((action) -> {

            Print p;
            if (this.table.getSelectionModel().isEmpty()) {
                p = new PrintDB().add(this.getForm());
                if (p != null) {
                    this.table.getItems().add(0, p);
                    this.table.getSelectionModel().select(p);
                    this.result = this.getTotal();
                }
            } else {
                p = new PrintDB().update(this.getForm());
                if (p != null) {
                    int index = this.table.getSelectionModel().getSelectedIndex();
                    this.table.getItems().set(index, p);
                    this.result = this.getTotal();
                }
            }

        });

        this.deleteButton.setDisable(true);
        this.deleteButton.setOnAction((action) -> {
            boolean del = new Delete(this.table.getSelectionModel().getSelectedItem().getMeters() + " siliniyor ...", " Bu kaydı kalıcı olarak silmeek üzeresiniz!").open(false);
            if (del) {
                boolean res = new PrintDB().delete(this.table.getSelectionModel().getSelectedItem().getId());
                if (res) {
                    this.table.getItems().remove(this.table.getSelectionModel().getSelectedItem());
                    this.result = this.getTotal();
                }
            }
        });

    }

    private void setForm(Print p) {
        this.meters.setText(String.valueOf(p.getMeters()));
        this.machine.setValue(p.getMachine());
        this.date.setValue(p.getDate());
    }

    private Print getForm() {
        Print p = new Print(this.product.getId());
        if (!this.table.getSelectionModel().isEmpty()) {
            p.setId(this.table.getSelectionModel().getSelectedItem().getId());
        }
        p.setDate(this.date.getValue());
        p.setMeters(Double.parseDouble(this.meters.getText()));
        p.setMachine(this.machine.getValue());
        return p;
    }

    private double getTotal() {
        double total = 0;
        for (Print p : this.table.getItems()) {
            total += p.getMeters();
        }
        return total;
    }
}
