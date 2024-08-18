package ghops.gprint;

import ghops.gprint.customer.customerview.CustomerView;
import ghops.gprint.fabric.fabricview.FabricView;
import ghops.gprint.orders.orderview.OrderView;
import ghops.gprint.orders.orderview.OrderView2;
import ghops.gprint.reportview.ReportView;
import ghops.gprint.sample.SampleView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class MainController implements Initializable {

    @FXML
    private Button customerButton;

    @FXML
    private Button orderButton;

    @FXML
    private StackPane page;

    @FXML
    private Button sampleButton;

    @FXML
    private Button fabricButton;

    @FXML
    private Button reportButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        orderButton.setOnAction((action) -> {
            page.getChildren().setAll(new OrderView());
        });

        sampleButton.setOnAction((action) -> {
            page.getChildren().setAll(new SampleView());
        });

        customerButton.setOnAction((action) -> {
            page.getChildren().setAll(new CustomerView());
        });

        fabricButton.setOnAction((action) -> {
            page.getChildren().setAll(new FabricView());
        });

        reportButton.setOnAction((action) -> {
            page.getChildren().setAll(new ReportView());
        });
        page.getChildren().setAll(new OrderView());
    }

}
