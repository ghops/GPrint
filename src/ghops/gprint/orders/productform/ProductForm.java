package ghops.gprint.orders.productform;

import ghops.gprint.database.FabricDB;
import ghops.gprint.database.PrintTypeDB;
import ghops.gprint.database.ProductDB;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.PrintType;
import ghops.gprint.models.Product;
import ghops.gprint.tools.Config;
import ghops.gprint.tools.WindowMover;
import ghops.gprint.tools.searchbox.SearchBox;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import javafx.scene.control.Label;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javafx.beans.binding.BooleanBinding;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.CheckBox;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;

public class ProductForm extends AnchorPane {

    @FXML
    private CheckBox status;

    @FXML
    private Label messageText;

    @FXML
    private Button closeButton;

    @FXML
    private Button imageButton;

    @FXML
    private TextField description;

    @FXML
    private TextField design;

    @FXML
    private Button fabricButton;

    @FXML
    private TextField meters;

    @FXML
    private ComboBox<PrintType> printType;

    @FXML
    private Button saveButton;

    @FXML
    private TextField weight;

    @FXML
    private TextField width;

    private Stage window;

    private Product product = null;
    private Product result = null;

    private ObjectProperty<Fabric> fabricProperty = new SimpleObjectProperty<>(new Fabric(0, ""));

    private File selectedImage = null;

    private Service imageService;

    public ProductForm(Product p) {
        this.product = p;

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ProductForm.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
            imageService = new Service() {
                @Override
                protected Task createTask() {
                    return new ImageTask(selectedImage, String.valueOf(product.getId()));
                }
            };
            this.messageText.textProperty().bind(imageService.messageProperty());

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

    public Product open() {
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
        printType.setItems(FXCollections.observableList(new PrintTypeDB().getAll()));
        fabricButton.textProperty().bind(Bindings.createObjectBinding(() -> fabricProperty.getValue().getName(), fabricProperty));

        design.setText(product.getDesign());
        description.setText(product.getDescription());
        printType.getSelectionModel().select(product.getPrintType());
        width.setText(String.valueOf(product.getWidth()));
        weight.setText(String.valueOf(product.getWeight()));
        meters.setText(String.valueOf(product.getMeters()));
        fabricProperty.setValue(product.getFabric());
        status.setSelected(product.getStatus());

        fabricButton.setOnAction((action) -> {
            Fabric c = new SearchBox<>(new FabricDB().getAll()).open("Kalite seç", window);
            if (c != null) {
                fabricProperty.set(c);
            }
        });

        saveButton.setOnAction((action) -> {
            Product p = new Product(product.getOrderId());
            p.setDescription(description.getText());
            p.setDesign(design.getText());
            p.setFabric(fabricProperty.getValue());
            p.setId(product.getId());
            p.setMeters(Double.parseDouble(meters.getText()));
            p.setPrintType(printType.getValue());
            p.setWeight(Double.parseDouble(weight.getText()));
            p.setWidth(Double.parseDouble(width.getText()));
            p.setStatus(status.isSelected());

            Product saved = new ProductDB().save(p);
            if (saved != null) {
                this.result = saved;
                if (this.product.getId() == 0) {
                    this.product.setId(saved.getId());
                }
                product.setDescription(saved.getDescription());
                product.setDesign(saved.getDesign());
                product.setFabric(saved.getFabric());
                product.setId(product.getId());
                product.setMeters(saved.getMeters());
                product.setPrintType(saved.getPrintType());
                product.setStatus(saved.getStatus());
                product.setWeight(saved.getWeight());
                product.setWidth(saved.getWidth());
                product.setStatus(saved.getStatus());

                if (this.selectedImage != null) {
                    imageService.start();
                }

                //this.window.close();
            }

        });
        closeButton.setOnAction((action) -> {
            this.result = null;
            this.window.close();

        });

        imageButton.setOnAction((action) -> {
            FileChooser chooser = new FileChooser();
            chooser.getExtensionFilters().addAll(
                    //new FileChooser.ExtensionFilter("JPG Files", "*.JPG"),
                    //new FileChooser.ExtensionFilter("PNG Files", "*.PNG"),                   
                    // new FileChooser.ExtensionFilter("TIFF Files", "*.TIFF"),
                    new FileChooser.ExtensionFilter("TIF Files", "*.TIF,", "*.TIFF,", "*.tif", "*.tiff")
            );

            File tmp = chooser.showOpenDialog(window);
            if (tmp != null) {
                this.selectedImage = tmp;
            }

        });

        this.initValidation();

    }

    private void initValidation() {
        BooleanBinding designValid = Bindings.createBooleanBinding(() -> {
            if (design.getText().trim().length() < 3) {
                return false;
            } else {
                return true;
            }
        }, this.design.textProperty());

        BooleanBinding meterValid = Bindings.createBooleanBinding(() -> {
            if (this.meters.getText().trim().length() < 1) {
                return false;
            } else if (Double.parseDouble(this.meters.getText()) < 0.1) {
                return false;
            } else {
                return true;
            }
        }, this.meters.textProperty());

        BooleanBinding widthValid = Bindings.createBooleanBinding(() -> {
            if (this.width.getText().trim().length() < 1) {
                return false;
            } else if (Double.parseDouble(this.width.getText()) < 1) {
                return false;
            } else {
                return true;
            }
        }, this.width.textProperty());

        BooleanBinding weightValid = Bindings.createBooleanBinding(() -> {
            if (this.weight.getText().trim().length() < 1) {
                return false;
            } else if (Double.parseDouble(this.weight.getText()) < 1) {
                return false;
            } else {
                return true;
            }
        }, this.weight.textProperty());

        BooleanBinding printTypeValid = Bindings.createBooleanBinding(() -> {
            if (this.printType.getValue().getId() < 1) {
                return false;
            } else {
                return true;
            }
        }, this.printType.valueProperty());

        BooleanBinding fabricValid = Bindings.createBooleanBinding(() -> {
            if (fabricProperty.getValue().getId() > 0) {
                return true;
            } else {
                return false;
            }
        }, fabricProperty);

        BooleanBinding imageValid = Bindings.createBooleanBinding(() -> {
            if (this.product.getId() > 0) {
                return true;
            } else {
                return false;
            }
        }, this.product.id());

        //  saveButton.disableProperty().bind(orderNoValid.not().or(dateValid.not()).or(deliveryValid.not()).or(customerValid.not()));
        saveButton.disableProperty().bind(designValid.not().or(meterValid.not()).or(widthValid.not()).or(weightValid.not()).or(printTypeValid.not()).or(fabricValid.not()));
        imageButton.disableProperty().bind(imageValid.not());
    }

}

class ImageTask extends Task {

    File file = null;
    String name = "";
    Config cfg = new Config();

    public ImageTask(File f, String n) {
        this.file = f;
        this.name = n;

    }

    @Override
    protected Object call() throws Exception {
        if (this.file != null) {

            try {
                updateMessage("Resim formatı dönüştürülüyor...");
                javaxt.io.Image image = new javaxt.io.Image(this.file.getPath());
                File targetFile = new File(cfg.getImageseUrl() + this.name + "." + cfg.getImageType());
                image.saveAs(targetFile);
                updateMessage("Yeni resim okunuyor...");
                BufferedImage source = ImageIO.read(targetFile);

                updateMessage("Resim boyutlandırılıyor...");
                BufferedImage resized = resize(source, cfg.getImageHeight(), cfg.getImageHeight());

                updateMessage("Resim kaydediliyor...");
                File output = new File(targetFile.getPath());
                ImageIO.write(resized, "png", output);
                updateMessage("Resim işleme başarıyla tamamlandı!");
            } catch (IOException e) {
                System.err.println(e);
                updateMessage("Beklenmeyen bir hata oluştu...");
            }

        }
        return file;
    }

    private BufferedImage resize(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        System.out.println("Resim işleme tamamlandı!");
        return resized;
    }
}
