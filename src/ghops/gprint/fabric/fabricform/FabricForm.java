package ghops.gprint.fabric.fabricform;

import ghops.gprint.database.FabricDB;
import ghops.gprint.database.PrintTypeDB;
import ghops.gprint.models.Fabric;
import ghops.gprint.models.PrintType;
import ghops.gprint.tools.WindowMover;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.css.Stylesheet;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class FabricForm extends AnchorPane {

    @FXML
    private Button closeButton;

    @FXML
    private TextField description;

    @FXML
    private TextField name;

    @FXML
    private TextField name2;

    @FXML
    private TextField name3;

    @FXML
    private Button saveButton;

    @FXML
    private CheckBox status;

    @FXML
    private TextField weight;

    @FXML
    private TextField width;
    @FXML
    private ComboBox<PrintType> printType;

    private Stage window;
    private Fabric fabric;

    public FabricForm(Fabric f) {
        this.fabric = f;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FabricForm.fxml"));
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

    public Fabric open() {
        this.getScene().setOnKeyPressed((key) -> {
            if (key.getCode() == KeyCode.ESCAPE) {
                this.window.close();
            }
        });
        this.window.getScene().setFill(Color.TRANSPARENT);


 
        WindowMover.open(window);
        this.window.showAndWait();
        return this.fabric;
    }

    private void init() {
        this.setForm();
        closeButton.setOnAction((action) -> {
            this.fabric = null;
            this.window.close();
        });

        saveButton.setOnAction((action) -> {
            this.setFabric();
            Fabric saved = new FabricDB().save(this.fabric);
            if (saved != null) {
                this.fabric = saved;
                System.out.println(saved.getId());
            } else {
                System.out.println("kayıt başarısız");
            }
        });

        this.printType.setItems(FXCollections.observableList(new PrintTypeDB().getAll()));

    }

    private void setForm() {
        this.name.setText(this.fabric.getName());
        this.name2.setText(this.fabric.getName2());
        this.name3.setText(this.fabric.getName3());
        this.description.setText(this.fabric.getDescription());
        this.weight.setText(String.valueOf(this.fabric.getWeight()));
        this.width.setText(String.valueOf(this.fabric.getWidth()));
        this.status.selectedProperty().set(this.fabric.getStatus());
        this.printType.getSelectionModel().select(this.fabric.getPrintType());
    }

    private void setFabric() {
        this.fabric.setName(name.getText());
        this.fabric.setName2(name2.getText());
        this.fabric.setName3(name3.getText());
        if (name2.getText().length() < 1) {
            this.fabric.setName2(null);
        }
        if (name3.getText().length() < 1) {
            this.fabric.setName3(null);
        }
        this.fabric.setDescription(description.getText());
        this.fabric.setPrintType(new PrintType(0, ""));
        this.fabric.setWeight(Integer.parseInt(weight.getText()));
        this.fabric.setWidth(Integer.parseInt(width.getText()));
        this.fabric.setPrintType(printType.getValue());

    }

}
