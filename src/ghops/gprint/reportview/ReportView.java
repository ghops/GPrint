package ghops.gprint.reportview;

import ghops.gprint.tools.ReportPDF;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
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
    Map<Integer, Object> data = new TreeMap<>();

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
            new ReportPDF(this.table).write(true,true);
            
        });

        this.initReportTypes();
        
        this.reportTypes.getSelectionModel().selectedItemProperty().addListener((ov, oval,nval)->{
            this.table.getColumns().setAll(nval.getColumns());
        });
    }

    
    private void initReportTypes(){
        IReport orderReport = new OrderReport();
        orderReport.setText("Sipariş raporları");
        this.table.setItems(orderReport.getData());
        this.reportTypes.getItems().add(orderReport);
    }
    
    private void setHeaders(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            headers.put(i, list.get(i));
        }
        System.out.println(headers);
    }

    private void setData(List<Object> list) {
        for (int i = 0; i < list.size(); i++) {
            data.put(i, list.get(i));
        }
        System.out.println(data);
    }

    private void setWidth(List<Double> list) {
        for (int i = 0; i < list.size(); i++) {
            sizes.put(i, list.get(i));
        }
        System.out.println(sizes);
    }

}
