package ghops.gprint.fabric.fabricview;

import ghops.gprint.customer.customerview.*;
import ghops.gprint.customer.customerForm.CustomerForm;
import ghops.gprint.database.CustomerDB;
import ghops.gprint.database.FabricDB;
import ghops.gprint.fabric.fabricform.FabricForm;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.PrintType;
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

public class FabricView extends StackPane {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<Fabric, String> colDescription;

    @FXML
    private TableColumn<Fabric, String> colName;

    @FXML
    private TableColumn<Fabric, String> colName2;

    @FXML
    private TableColumn<Fabric, String> colName3;

    @FXML
    private TableColumn<Fabric, PrintType> colPrintType;

    @FXML
    private TableColumn<Fabric, Boolean> colStatus;

    @FXML
    private TableColumn<Fabric, Integer> colWeight;

    @FXML
    private TableColumn<Fabric, Integer> colWidth;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Fabric> table;

    public FabricView() {

        //last = quantity;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FabricView.fxml"));
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

        });

        this.deleteButton.setOnAction((action) -> {
            boolean res = new Delete("Kalite sil", this.table.getSelectionModel().getSelectedItem().getName() + " adlı kaydı silmek üzeresiniz!").open();
            if (res) {
                boolean del = new FabricDB().delete(this.table.getSelectionModel().getSelectedItem().getId());
                if (del) {
                    this.table.getItems().remove(this.table.getSelectionModel().getSelectedItem());
                }
            }
        });
    }

    private void initTable() {

        this.colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        this.colName.setCellValueFactory(new PropertyValueFactory<>("name"));
        this.colName2.setCellValueFactory(new PropertyValueFactory<>("name2"));
        this.colName3.setCellValueFactory(new PropertyValueFactory<>("name3"));
        this.colPrintType.setCellValueFactory(new PropertyValueFactory<>("printType"));
        this.colWeight.setCellValueFactory(new PropertyValueFactory<>("weight"));
        this.colWidth.setCellValueFactory(new PropertyValueFactory<>("width"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        this.table.setItems(FXCollections.observableList(new FabricDB().getAll()));

        this.colStatus.setCellFactory((tableColumn) -> {
            TableCell<Fabric, Boolean> tableCell = new TableCell<>() {

                @Override
                protected void updateItem(Boolean t, boolean bln) {
                    super.updateItem(t, bln);
                    if (t != null) {
                        CheckBox cb = new CheckBox();
                        cb.selectedProperty().setValue(t);
                        setGraphic(cb);

                        Fabric fabric = (Fabric) getTableRow().getItem();
                        cb.selectedProperty().addListener((o, oldValue, newValue) -> {
                            System.out.println(fabric);
                            fabric.setStatus(newValue);
                            new FabricDB().update(fabric);
                        });
                    } else {
                        setGraphic(null);
                    }
                }
            };

            return tableCell;
        });

        this.table.setRowFactory(tv -> {
            TableRow<Fabric> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    //Fabric f = row.getItem();
                    Fabric f = new FabricForm(row.getItem()).open();
                    row.setItem(f);
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
