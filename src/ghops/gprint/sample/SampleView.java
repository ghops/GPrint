package ghops.gprint.sample;

import ghops.gprint.customer.customerForm.CustomerForm;
import ghops.gprint.database.CustomerDB;
import ghops.gprint.database.FabricDB;
import ghops.gprint.database.MachineDB;
import ghops.gprint.database.PrintTypeDB;
import ghops.gprint.database.SampleDB;
import ghops.gprint.database.SampleTypeDB;
import ghops.gprint.fabric.fabricform.FabricForm;
import ghops.gprint.models.Customer;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.Machine;
import ghops.gprint.models.PrintType;
import ghops.gprint.models.Sample;
import ghops.gprint.models.SampleType;
import ghops.gprint.tools.Config;
import ghops.gprint.tools.TextEdit;
import ghops.gprint.tools.searchbox.SearchBox;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.UnaryOperator;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.kordamp.ikonli.javafx.FontIcon;

public class SampleView extends AnchorPane {

    @FXML
    private DatePicker date;

    @FXML
    private TableColumn<Sample, Customer> colCustomer;

    @FXML
    private TableColumn<Sample, LocalDate> colDate;

    @FXML
    private TableColumn<Sample, String> colDesign;

    @FXML
    private TableColumn<Sample, Boolean> colStatus;

    @FXML
    private TableColumn<Sample, Fabric> colFabric;

    @FXML
    private TableColumn<Sample, Machine> colMachine;

    @FXML
    private TableColumn<Sample, Double> colMeters;

    @FXML
    private TableColumn<Sample, PrintType> colPrintType;

    @FXML
    private TableColumn<Sample, SampleType> colSampleType;

    @FXML
    private TableView<Sample> table;

    @FXML
    private Button addFabricButton;

    @FXML
    private Button addCustomerButton;

    @FXML
    private Button clearButton;

    @FXML
    private Button fileImport;

    @FXML
    private Button saveButton;

    @FXML
    private Button customerApplyButton;

    @FXML
    private Button dateApplyButton;
    @FXML
    private Button customerbutton;

    @FXML
    private Button designApplyButton;

    @FXML
    private ComboBox<Machine> machine;

    @FXML
    private Button fabricApplyButton;

    @FXML
    private Button fabricButton;

    @FXML
    private Button machineApplyButton;

    @FXML
    private Button metersApplyButton;

    @FXML
    private Button scanButton;

    @FXML
    private ComboBox<PrintType> printType;

    @FXML
    private Button printTypeApplyButton;

    @FXML
    private ComboBox<SampleType> sampleType;

    @FXML
    private Button sampleTypeApplyButton;

    @FXML
    private TextField meters;

    @FXML
    private TextField design;

    private ObjectProperty<Customer> customerProperty = new SimpleObjectProperty<>(new Customer(0, "firma seç"));
    private ObjectProperty<Fabric> fabricProperty = new SimpleObjectProperty<>(new Fabric(0, "kalite seç"));
    private Stage win = new Stage();
    private Config cfg = new Config();

    private ExecutorService executorService = Executors.newCachedThreadPool();

    public SampleView() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("SampleView.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            init();

            Platform.runLater(() -> {
                this.win = (Stage) this.getScene().getWindow(); 
            });
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init() {
        this.initTable();
        this.initButtons();

        this.machine.setItems(FXCollections.observableList(new MachineDB().getAll()));
        this.printType.setItems(FXCollections.observableList(new PrintTypeDB().getAll()));
        this.sampleType.setItems(FXCollections.observableList(new SampleTypeDB().getAll()));

    }

    private void initTable() {
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.colCustomer.setCellValueFactory(new PropertyValueFactory<>("customer"));
        this.colDesign.setCellValueFactory(new PropertyValueFactory<>("design"));
        this.colFabric.setCellValueFactory(new PropertyValueFactory<>("fabric"));
        this.colMeters.setCellValueFactory(new PropertyValueFactory<>("meters"));
        this.colMachine.setCellValueFactory(new PropertyValueFactory<>("machine"));
        this.colPrintType.setCellValueFactory(new PropertyValueFactory<>("printType"));
        this.colSampleType.setCellValueFactory(new PropertyValueFactory<>("sampleType"));
        this.colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        this.colStatus.setCellFactory((tableColumn) -> {
            TableCell<Sample, Boolean> cell = new TableCell<>() {
                @Override
                protected void updateItem(Boolean item, boolean bln) {
                    if (item != null) {
                        FontIcon fi = new FontIcon();
                        setText("");
                        fi.setIconSize(20);
                        if (item) {

                            fi.setIconLiteral("mdi2c-check-box-outline");

                        } else {

                            fi.setIconLiteral("mdi2c-checkbox-blank-outline");

                        }
                        setGraphic(fi);

                    } else {
                        setText("");
                        setGraphic(null);
                    }
                }

            };
            return cell;

        });

        this.table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        this.table.setItems(FXCollections.observableList(new SampleDB().getAll(LocalDate.parse("2024-01-01"), LocalDate.parse("2024-06-01"))));

    }

    private void initButtons() {
        clearButton.setOnAction((action) -> {
            this.table.getItems().clear();
        });
        fileImport.setOnAction((action) -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setInitialDirectory(new File(cfg.getDefaultImageFolder()));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("TIFF files (*.tiff)", "*.TIF", "*.TIFF", "*.tif", "*.tiff")
            );
            List<File> files;
            try {
                files = fileChooser.showOpenMultipleDialog(win);
            } catch (Exception e) {
                fileChooser.setInitialDirectory(null);
                files = fileChooser.showOpenMultipleDialog(win);
            }
            if (files != null) {
                this.table.setItems(this.fileToSample(files));
                this.cfg.setDefaultImageFolder(files.getFirst().getParent());
            }
        });
        customerbutton.textProperty().bind(customerProperty.asString());
        customerbutton.setOnAction((action) -> {
            SearchBox<Customer> cBox = new SearchBox<>(new CustomerDB().getAll());
            Customer c = cBox.open("Müştteri seç", win);
            if (c != null) {
                this.customerProperty.setValue(c);
            }

        });
        fabricButton.textProperty().bind(fabricProperty.asString());
        fabricButton.setOnAction((action) -> {
            SearchBox<Fabric> fBox = new SearchBox<>(new FabricDB().getAll());
            Fabric f = fBox.open("Kalite seç", win);
            if (f != null) {
                this.fabricProperty.setValue(f);
            }

        });
        customerApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setCustomer(customerProperty.getValue());
            }
            this.table.refresh();
        });
        fabricApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setFabric(fabricProperty.getValue());
            }
            this.table.refresh();
        });
        machineApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setMachine(machine.getValue());
            }
            this.table.refresh();
        });
        sampleTypeApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setSampleType(sampleType.getValue());
            }
            this.table.refresh();
        });
        printTypeApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setPrintType(printType.getValue());
            }
            this.table.refresh();
        });
        designApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setDesign(design.getText());
            }
            this.table.refresh();
        });

        dateApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setDate(this.date.getValue());
            }
            this.table.refresh();
        });

        metersApplyButton.setOnAction((action) -> {
            for (Sample sample : this.table.getSelectionModel().getSelectedItems()) {
                sample.setMeters(Integer.parseInt(meters.getText()));
            }
            this.table.refresh();
        });
        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("\\d?")) {
                return change;
            }
            return null;
        };
        this.meters.setTextFormatter(new TextFormatter<String>(filter));

        saveButton.setOnAction((action) -> {
            SampleDB sdb = new SampleDB();
            for (Sample s : this.table.getSelectionModel().getSelectedItems()) {
                Sample tmp = sdb.save(s);
                if (tmp != null) {
                    s.setStatus(true);
                } else {
                    s.setStatus(false);
                }
            }

            this.table.refresh();
        });

        addFabricButton.setOnAction((action) -> {
            new FabricForm(new Fabric()).open();

        });

        addCustomerButton.setOnAction((action) -> {
            Customer c = new CustomerForm(new Customer(0, "")).open();
            if (c != null) {
                this.customerProperty.set(c);
            }
        });

        scanButton.setOnAction((action) -> {
            this.scan();
        });

    }

    private ObservableList<Sample> fileToSample(List<File> files) {
        List<Fabric> fabricList = new FabricDB().getAll();
        ObservableList<Sample> smpList = FXCollections.observableArrayList();

        for (File f : files) {
            Sample s = new Sample();
            s.setMeters(this.findMeter(f.getName()));
            s.setDesign(f.getName());
            s.setCustomer(this.findCustomer(new CustomerDB().getAll(), f.getName()));
            s.setDate(this.findDate(f.getPath()));
            s.setFabric(this.findFabric(fabricList, f.getName()));
            s.setPrintType(this.findPrintType(f.getPath()));
            s.setSampleType(this.findSampleType(f.getPath()));
            s.setStatus(false);
            smpList.add(s);
        }

        return smpList;
    }

    private void scan() {
        List<Fabric> fabricList = new FabricDB().getAll();
        List<Customer> customerList = new CustomerDB().getAll();

        for (Sample s : this.table.getItems()) {

            s.setFabric(this.findFabric(fabricList, s.getDesign()));
            s.setCustomer(this.findCustomer(customerList, s.getDesign()));

        }

        this.table.refresh();
    }

    private Customer findCustomer(List<Customer> list, String text) {

        String fileName = TextEdit.toUpperEN(text).replace(" ", "");

        for (Customer customer : list) {
            if (fileName.contains(TextEdit.toUpperEN(customer.getName()).replace(" ", ""))) {
                return customer;
            }
        }
        return null;
    }

    private int findMeter(String text) {
        int meter = 0;
        try {
            int index = text.toUpperCase().indexOf("MT");
            if (index > 0) {
                index -= 2;
            }
            String str = text.substring(index, index + 2).trim();

            meter = Integer.parseInt(str);

        } catch (Exception e) {

        }

        return meter;
    }

    private LocalDate findDate(String text) {
        LocalDate date = null;
        String dt = text.replace("\\", "/").replace("//", "/");
        String arr[] = dt.split("/");

        try {
            date = LocalDate.parse(arr[arr.length - 2]);
        } catch (Exception e) {
        }
        return date;
    }

    private Fabric findFabric(List<Fabric> list, String text) {
        String fileName = TextEdit.toUpperEN(text);

        for (Fabric fbr : list) {
            if (fbr.getName() != null && fbr.getName().length() > 1 && fileName.contains(fbr.getName())) {
                return fbr;
            } else if (fbr.getName2() != null && fbr.getName2().length() > 1 && fileName.contains(fbr.getName2())) {
                return fbr;
            } else if (fbr.getName3() != null && fbr.getName3().length() > 1 && fileName.contains(fbr.getName3())) {
                return fbr;
            }

        }
        return null;
    }

    private PrintType findPrintType(String text) {

        for (PrintType pt : new PrintTypeDB().getAll()) {
            if (text.toUpperCase().contains(pt.getName())) {
                return pt;
            }
        }
        return null;
    }

    private SampleType findSampleType(String text) {

        for (SampleType st : new SampleTypeDB().getAll()) {
            if (text.toUpperCase().contains(st.getName())) {
                return st;
            }
        }
        return null;
    }
}
