package ghops.gprint.orders.orderitem.productItem;

import ghops.gprint.database.ProductDB;
import ghops.gprint.models.Product;
import ghops.gprint.orders.orderitem.productItem.printpane.PrintPane;
import ghops.gprint.orders.orderview.OrderView;
import ghops.gprint.orders.productform.ProductForm;
import ghops.gprint.tools.delete.Delete;
import java.io.IOException;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ProductItem extends GridPane {

    @FXML
    private Label description;

    @FXML
    private Label design;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button printButton;

    @FXML
    private Label fabric;

    @FXML
    private Label meters;

    @FXML
    private Label printedMeters;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Label weight;

    @FXML
    private Label width;

    @FXML
    private ImageView preview;

    @FXML
    private CheckBox status;

    private Product product;

    private DoubleProperty percent;

    public ProductItem(Product p) {
        this.product = p;
        this.percent = new SimpleDoubleProperty(this.product.getPrintedMeters() / this.product.getMeters());
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductItem.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
            try {
                // preview.setImage(new Image("file:" + System.getProperty("user.dir") + "/previews/" + String.valueOf(product.getId()) + ".PNG"));
                init();
            } catch (Exception e) {
            }

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
    }

    private void init() {
        initProperties();
        initButtons();

    }

    private void initButtons() {
        
        this.printButton.setOnAction((action) -> {
            double total = new PrintPane(product).open();
            if(total >=0 ) this.product.setPrintedMeters(total);
        });

    }

    public Button getDeleteButton() {
        return this.deleteButton;
    }

    private void initProperties() {
        design.textProperty().bind(this.product.design());
        fabric.textProperty().bind(product.fabric().asString());
        meters.textProperty().bind(product.meters().asString());
        printedMeters.textProperty().bind(product.printedMeters().asString());
        description.textProperty().bind(product.description());
        progressBar.progressProperty().bind(percent);
        width.textProperty().bind(product.width().asString());
        weight.textProperty().bind(product.weight().asString());
        status.selectedProperty().bindBidirectional(product.status());
        status.selectedProperty().addListener((o, oldValue, newValue) -> {
            this.product.setStatus(newValue);
            new ProductDB().update(this.product);
        });
        preview.setImage(new Image(OrderView.previewsPath + product.getId() + ".PNG"));

        editButton.setOnAction((action) -> {
            new ProductForm(product).open();
        });

        //    customer.textProperty().bind(Bindings.createObjectBinding(() -> customerProperty.getValue().getName(), customerProperty));
        /*
        designProperty = new SimpleStringProperty(product.getDesign());
        design.textProperty().bind(designProperty);

        fabricProperty = new SimpleStringProperty(product.getFabric().getName());
        fabric.textProperty().bind(fabricProperty);

        widthProperty = new SimpleStringProperty(String.valueOf(product.getWidth()));
        width.textProperty().bind(widthProperty);

        metersProperty = new SimpleStringProperty(String.valueOf(product.getMeters() + " mt"));
        meters.textProperty().bind(metersProperty);

        printedProperty = new SimpleStringProperty(String.valueOf(product.getPrintedMeters() + " mt"));
        printed.textProperty().bind(printedProperty);

        weightProperty = new SimpleStringProperty(String.valueOf(product.getWeight()) + "gr");
        weight.textProperty().bind(weightProperty);

        descriptionProperty = new SimpleStringProperty(product.getDescription());
        description.textProperty().bind(descriptionProperty);

         */
    }

}
