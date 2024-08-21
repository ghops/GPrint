package ghops.gprint.reportview;

import ghops.gprint.tools.PDFCreator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class ReportView extends AnchorPane {

    @FXML
    private Button pdfButton;

    @FXML
    private ListView<IReport> reportTypes;

    @FXML
    private TableView<?> table;

    Map<Integer, Double> sizes = new TreeMap<>();
    Map<Integer, String> headers = new TreeMap<>();
    //Map<Integer, Object> data = new TreeMap<>();

    public ReportView() {

        //last = quantity;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ReportView.fxml"));
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

        this.pdfButton.setOnAction((action) -> { 

            this.reportTypes.getSelectionModel().getSelectedItem().writePDF(this.table); 
        });

        this.initReportTypes();

        this.reportTypes.getSelectionModel().selectedItemProperty().addListener((ov, oval, nval) -> {
            this.table.getColumns().setAll(nval.getColumns());
        });
    }

    private void initReportTypes() {
        IReport orderReport = new OrderReport();
        orderReport.setText("Sipariş raporları");
        this.table.setItems(orderReport.getData());
        this.reportTypes.getItems().add(orderReport);
    }

}
